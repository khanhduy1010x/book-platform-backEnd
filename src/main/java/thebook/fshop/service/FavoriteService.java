package thebook.fshop.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Request.AddToFavoriteRequest;
import thebook.fshop.entity.WishList;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BookRepository;
import thebook.fshop.repository.WishListRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteService {

    WishListRepository wishListRepository;
    SecurityService securityService;
    BookRepository bookRepository;

    // Add a favorite
    public void addFavorite(AddToFavoriteRequest request) {
        var account = securityService.getAccountByJWT();
        var wishBook = wishListRepository.findByBook_IDAndAccount_AccID(request.getBookID(), account.getAccID());
        log.info("Added to favorites: {}", wishBook);
        if (wishBook != null) {
            throw new AppException(ErrorCode.DUPLICATE_BOOK);
        }
        log.info("Added to favorites: {}", request.getBookID());
        var wishList = WishList.builder()
                .account(account)
                .book(bookRepository
                        .findById(request.getBookID())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)))
                .build();
        wishListRepository.save(wishList); // Save the wishlist item in the database
    }

    // Delete a favorite by ID
    public void deleteFavorite(Integer id) {
        var account = securityService.getAccountByJWT();
        var wishBook = wishListRepository.findByBook_IDAndAccount_AccID(id, account.getAccID());
        if (wishBook != null) {
            wishListRepository.deleteById(wishBook.getID()); // Delete the wishlist item by ID
        } else {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
    }
}
