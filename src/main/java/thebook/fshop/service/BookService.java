package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import thebook.fshop.DTO.Request.*;
import thebook.fshop.DTO.Response.*;
import thebook.fshop.entity.*;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.helper.*;
import thebook.fshop.mapper.AccountMapper;
import thebook.fshop.mapper.SearchBookMapper;
import thebook.fshop.repository.*;

import static thebook.fshop.helper.StatisticType.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookService {
    @NonFinal
    @Value("${upload.path_coverBook}")
    String UPLOAD_COVER;
    @NonFinal
    @Value("${upload.path_epubFile}")
    String UPLOAD_EPUB;
    @NonFinal
    @Value("${path.coverImage}")
    String PATH_COVER;
    @NonFinal
    @Value("${path.epub}")
    String PATH_EPUB;

    BookRepository bookRepository;
    CategoryRepository categoryRepository;
    BookRateRepository bookRateRepository;
    SecurityService securityService;
    EbookShelfRepository ebookShelfRepository;
    BooKReadHistoryRepository bookReadHistoryRepository;
    TransactionRepository transactionRepository;
    AuthorRepository authorRepository;
    OrderDetailRepository orderDetailRepository;
    AccountsRepository accountsRepository;
    BooKReadHistoryRepository booKReadHistoryRepository;
    AccountMapper accountMapper;

    public List<ListBookByCateResponse> searchBook(String query) {
        // Fetching books from the repository
        log.info(query);
        List<Book> books = bookRepository.findByBookNameAndAuthorAndCategoryUser(
                query.toLowerCase());
        if (books.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return getListBookByCateResponses(books);
    }
    public List<Book> searchBookAdmin(String query) {
        log.info(query);
        return bookRepository.findByBookNameAndAuthorAndCategory(
                query.toLowerCase());
    }

    public List<ListBookByCateResponse> getListBook() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new AppException(ErrorCode.NOT_FOUND);
        List<ListBookByCateResponse> books = new ArrayList<>();
        for (Category category : categories) {
            var listBook = bookRepository.findBookByCategory_IDAndIsVisible(category.getID(),true);
            ListBookByCateResponse listBookResponse = ListBookByCateResponse.builder()
                    .cateName(category.getCateName())
                    .listBook(listBook)
                    .build();
            books.add(listBookResponse);
        }
        return books;
    }

    public BookDetailResponse getBookById(int id) {
        Account account = null;
        BookRate bookRate = null;
        double percent = -1;
        var book =bookRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        try {
            account = securityService.getAccountByJWT();
        }catch (AppException e) {

        }
        boolean isOwned = false;
        if(account !=null) {
         bookRate =  bookRateRepository.findByAccount_AccIDAndBook_ID(account.getAccID(),book.getID());
         if(book.getMemberType() == MemberType.NONE && book.getBookType() == BookType.EBOOK && book.getPrice() == 0){
             isOwned = true;
         }
         if(book.getMemberType().ordinal() <= account.getMemberType().ordinal() && book.getPrice() == 0 ) {
             isOwned = true;
         }
         var bookShelf = ebookShelfRepository.findByAccount_AccIDAndBook_ID(account.getAccID(),book.getID());
         if(bookShelf != null) {
             isOwned=true;
         }
            var bookRead = bookReadHistoryRepository.findByAccount_AccIDAndBook_ID(account.getAccID(), book.getID());
             percent = 0;
            if(bookRead != null) {
                percent = bookRead.getReadPercent();
            }

        }


        return
                BookDetailResponse.builder()
                        .bookRateOfUser(bookRate)
                        .isOwned(isOwned)
                        .bookRates(bookRateRepository.findByBook_ID(id))
                        .book(book)
                        .readPercent(percent)
                        .build();
    }

    public List<Book> searchByFilter(FilterRequest query) {
        List<Book> books = bookRepository.findAll();
        if (query.getType() != null) {
            BookType bookType = BookType.valueOf(query.getType());
            books = books.stream()
                    .filter(book -> book.getBookType() == bookType)
                    .collect(Collectors.toList());
        }
        if (query.getAuthor() != null) {
            String author = query.getAuthor();
            books = books.stream()
                    .filter(book -> book.getAuthor().getName().contains(author))
                    .collect(Collectors.toList());
        }

        return books;
    }

    public List<Category> getCateIdsByBookType(String bookType) {
        return bookRepository.findCateIdsByBookType(BookType.valueOf(bookType));
    }

    public List<BookCateResponse> getByCateAndBookType(int cateID, String bookType) {
        List<Book> books = bookRepository.findBookByCategory_IDAndBookTypeAndIsVisibleOrderByMemberTypeDesc(cateID, BookType.valueOf(bookType),true);
        Comparator<MemberType> memberTypeComparator = Comparator.comparingInt(memberType -> {
            switch (memberType) {
                case PREMIUM:
                    return 1;
                case ADVANCE:
                    return 2;
                case BASIC:
                    return 3;
                case NONE:
                    return 4;
                default:
                    throw new IllegalArgumentException("Unknown MemberType: " + memberType);
            }
        });

        books = books.stream()
                .sorted((b1, b2) -> memberTypeComparator.compare(b1.getMemberType(), b2.getMemberType()))
                .collect(Collectors.toList());

        Map<MemberType, List<Book>> booksByMemberType = books.stream()
                .collect(Collectors.groupingBy(Book::getMemberType));

        List<BookCateResponse> response = booksByMemberType.entrySet().stream()
                .map(entry -> new BookCateResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return response;
    }

    public List<ListBookByCateResponse> getListBookByType(String type) {
        var listBook = bookRepository.findBookByBookTypeAndIsVisible(BookType.valueOf(type),true);
        return getListBookByCateResponses(listBook);
    }

    private List<ListBookByCateResponse> getListBookByCateResponses(List<Book> listBook) {
        Map<String, List<Book>> booksByCate = listBook.stream()
                .collect(Collectors.groupingBy(book -> book.getCategory().getCateName()));
        List<ListBookByCateResponse> response = booksByCate.entrySet().stream()
                .map(entry -> new ListBookByCateResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return response;
    }

    public void buyEbook(BuyEbookRequest request) {
        var account = securityService.getAccountByJWT();
        var beforeAmount = account.getAmount();
        var book = bookRepository.findByID(request.getBookID()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if (account.getAmount() < book.getPrice()) throw new AppException(ErrorCode.NOT_ENOUGH_TOTAL_PRICE);
        account.setAmount(account.getAmount() - book.getPrice());
        var ebookShelf = EbookShelf.builder()
                .account(account)
                .book(book)
                .date(new Date())
                .build();

        ebookShelfRepository.save(ebookShelf);
        var trans = createTransaction(account,book,beforeAmount, MethodType.WEBSITE, TransactionType.BUY_BOOK,book.getPrice());
        transactionRepository.save(trans);
        accountsRepository.save(account);
    }
    public Transaction createTransaction(Account account, Book book, long beforeAmount, MethodType methodType, TransactionType transactionType, long price ) {
        String content = "Mua nội dung sách " + book.getBookName();

        return Transaction.builder()
                .account(account)
                .beforeAmount(beforeAmount)
                .afterAmount(account.getAmount())
                .methodType(methodType)
                .content(content)
                .priceQR(price)
                .time(new Date())
                .transactionType(transactionType)
                .build();
    }

    public List<EbookShelf> getBookBought () {
        return ebookShelfRepository.findByAccount_AccID(securityService.getAccountByJWT().getAccID());
    }
    public ListBookAdminResponse getAllBookByType (int type, int page , int size ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ID"));
        var bookType = BookType.values()[type];
        var listBook = bookRepository.findBookByBookTypeOrderByIDAsc(bookType,pageable);
        return ListBookAdminResponse.builder()
                .listBook(listBook.getContent())
                .currentPage(listBook.getNumber())
                .totalPages(listBook.getTotalPages())
                .build();

    }
    public List<Category> getAllCate() {
        return categoryRepository.findAll();
    }
    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }
    public void addNewBook(AddNewBookRequest request) {
        var author = authorRepository.findByName(request.getAuthor());
        if(author == null){
            author = authorRepository.save(Author.builder()
                    .name(request.getAuthor())
                    .build());
        }
        var cate = categoryRepository.findByCateName(request.getCateName());
        if(cate == null){
            cate = categoryRepository.save(Category.builder()
                            .cateName(request.getCateName())
                    .build());
        }
        log.info("file: {}",request.getUrl());
        log.info("file: {}",request.getCoverImage());

       String coverImage = saveFile(request.getCoverImage(),UPLOAD_COVER,PATH_COVER);
       String epubFile= null;
        if(BookType.valueOf(request.getBookType()) == BookType.EBOOK) {
             epubFile = saveFile(request.getUrl(),UPLOAD_EPUB,PATH_EPUB);

        }
        var book = Book.builder()
                .author(author)
                .category(cate)
                .isVisible(request.getIsVisible())
                .bookName(request.getBookName())
                .bookType(BookType.valueOf(request.getBookType()))
                .coverImage(coverImage)
                .description(request.getDescription())
                .price(Long.parseLong(request.getPrice()))
                .memberType( MemberType.valueOf(request.getMemberType()))
                .url(epubFile)
                .ebookType(BookType.valueOf(request.getBookType()) == BookType.EBOOK ? EbookType.EPUB : null)
                .build();
        bookRepository.save(book);

    }
    private String saveFile(MultipartFile file, String uploadPath, String pathPrefix) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE_NULL);
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String uniqueID = UUID.randomUUID().toString();
            String fileName = baseName + "_" + uniqueID + fileExtension;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + File.separator + fileName;
            file.transferTo(new File(filePath));

            return pathPrefix + fileName;
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_FILE_NULL);
        }
    }
    public void updateBook(UpdateBookRequest request) {
        var book = bookRepository.findById(Integer.parseInt(request.getId())).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        var author = authorRepository.findByName(request.getAuthor());
        if(author == null){
            author = authorRepository.save(Author.builder()
                    .name(request.getAuthor())
                    .build());
        }
        var cate = categoryRepository.findByCateName(request.getCateName());
        if(cate == null){
            cate = categoryRepository.save(Category.builder()
                    .cateName(request.getCateName())
                    .build());
        }

        String coverImage = book.getCoverImage();
        if(request.getCoverImage() != null) {
             coverImage = saveFile(request.getCoverImage(),UPLOAD_COVER,PATH_COVER);
        }
        String epubFile= book.getUrl();
        if(BookType.valueOf(request.getBookType()) == BookType.EBOOK && request.getUrl() !=null) {
            epubFile = saveFile(request.getUrl(),UPLOAD_EPUB,PATH_EPUB);
        }
         book = Book.builder()
                 .ID(Integer.parseInt(request.getId()))
                .author(author)
                .category(cate)
                .bookName(request.getBookName())
                .bookType(BookType.valueOf(request.getBookType()))
                .coverImage(coverImage)
                .description(request.getDescription())
                .price(Long.parseLong(request.getPrice()))
                 .isVisible(request.getIsVisible())
                 .memberType( MemberType.valueOf(request.getMemberType()))
                .url(epubFile)
                .ebookType(BookType.valueOf(request.getBookType()) == BookType.EBOOK ? EbookType.EPUB : null)
                .build();
        bookRepository.save(book);

    }
    public List<ListBookMostStatistic> getStatisticsOnMostPurchasedBooks() {
        List<Book> topBooks = bookRepository.findTop10MostPurchasedBooks();
        return topBooks.stream()
                .map(book -> {
                    int totalQuantity = orderDetailRepository.findTotalQuantityByBookId(book.getID());
                    return ListBookMostStatistic.builder()
                            .book(book)
                            .total_quantity(totalQuantity)
                            .build();
                })
                .collect(Collectors.toList());
    }


    public List<ListReadBookStatisticResponse> getStatisticsOnMostReadBooks() {
        List<Book> topBooks = booKReadHistoryRepository.findMostReadBooks().subList(0,9);
        return topBooks.stream()
                .map(bookProj -> {
                    int totalQuantity = booKReadHistoryRepository.findTotalQuantityByBookId(bookProj.getID());
                    return ListReadBookStatisticResponse.builder()
                            .book(bookProj)
                            .total_read_user(totalQuantity)
                            .build();

                })
                .collect(Collectors.toList());
    }

    public List<TopReaderDTO> getStatisticsOnMostReader() {
       return convertToTopReaderDTO(booKReadHistoryRepository.findTop10ReadersRaw()).subList(0,9);
    }

    public List<TopReaderDTO> convertToTopReaderDTO(List<Object[]> rawResults) {
        List<TopReaderDTO> topReaderDTOList = new ArrayList<>();
        for (Object[] result : rawResults) {
            Integer accid = (Integer) result[0];
            Long totalBooksRead = (Long) result[1];
            TopReaderDTO dto = new TopReaderDTO(accid, totalBooksRead);
            topReaderDTOList.add(dto);
        }
        return topReaderDTOList;
    }

    public List<ListStatisticTopContentResponse> getStatisticsTopContent() {
        List<AccountResponse> topBooks = bookRepository.findTop10Content().stream().map(accountMapper::toAccountResponse).toList();
        return topBooks.stream()
                .map(reader -> {
                    int total_content = bookRepository.findTotalContentByAccount(reader.getAccID());
                    return ListStatisticTopContentResponse.builder()
                            .account(reader)
                            .total_content(total_content)
                            .build();

                })
                .collect(Collectors.toList());
    }

    public List<ListStatisticRevenueByBookResponse> getStatisticsRevenueByBook(ListStatisticRevenueByBookRequest request) {
        List<ListStatisticRevenueByBookResponse> result = new ArrayList<>();
        switch (request.getStatisticType()) {
            case DAYS:
                List<Object[]> dailyRevenueData = bookRepository.findRevenueByDateRange(request.getStDate(), request.getEdDate());
                for (Object[] row : dailyRevenueData) {
                    Book book = new Book();
                    book.setID(((Integer) row[1]).intValue());
                    book.setBookName((String) row[2]);
                    book.setAuthor(authorRepository.findById((Integer) row[3]).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)));
                    book.setUrl((String) row[4]);
                    book.setCoverImage((String) row[5]);
                    int revenue = ((Number) row[6]).intValue();

                    result.add(new ListStatisticRevenueByBookResponse(book, revenue));
                }
                break;

            case MONTH:
                List<Object[]> monthlyRevenueData = bookRepository.findRevenueByMonth(request.getMonth(), request.getYear());
                List<ListStatisticRevenueByBookResponse> tempList = new ArrayList<>();
                for (Object[] row : monthlyRevenueData) {
                    Book book = new Book();
                    book.setID(((Number) row[1]).intValue());
                    book.setBookName((String) row[2]);
                    book.setAuthor(authorRepository.findById((Integer) row[3]).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)));
                    book.setUrl((String) row[4]);
                    book.setCoverImage((String) row[5]);
                    int revenue = ((Number) row[6]).intValue();

                    boolean found = false;
                    if(result.size() > 0) {
                        for (ListStatisticRevenueByBookResponse item : result) {
                            log.info(item.getBook().getBookName(), book.getBookName());
                            if (book.getID() == item.getBook().getID()) {
                                item.setRevenue(item.getRevenue() + revenue);
                                found = true;
                                break;
                            }
                        }
                    }


                    if (!found) {
                        tempList.add(new ListStatisticRevenueByBookResponse(book, revenue));
                    }
                }
                result.addAll(tempList);
                break;

            case YEAR:
                List<Object[]> yearlyRevenueData = bookRepository.findRevenueByYear(request.getYear());
                for (Object[] row : yearlyRevenueData) {
                    Book book = new Book();
                    book.setID(((Number) row[1]).intValue());
                    book.setBookName((String) row[2]);
                    book.setAuthor(authorRepository.findById((Integer) row[3]).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)));
                    book.setUrl((String) row[4]);
                    book.setCoverImage((String) row[5]);

                    int revenue = ((Number) row[6]).intValue();

                    result.add(new ListStatisticRevenueByBookResponse(book, revenue));
                }
                break;
        }
        return result;
    }

    public List<Book> findByCateAndBooKType(String cate, String type){
        return bookRepository.findAllByCategory_CateNameAndBookTypeAndIsVisible(cate,BookType.valueOf(type),true);
    }

}
