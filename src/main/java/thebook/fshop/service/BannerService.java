package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.BannerResponse;
import thebook.fshop.entity.Banner;
import thebook.fshop.entity.BookRate;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BannerRepository;
import thebook.fshop.repository.BookRateRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerService {
    BannerRepository bannerRepository;
    BookRateRepository bookRateRepository;

    @NonFinal
    @Value("${upload.path_banner}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path.banner}")
    String PATH_AVATAR;

    public List<BannerResponse> viewBanner() {
        return bannerRepository.findAll().stream()
                .map(banner -> {
                    BannerResponse.BookInfo bookInfo = null;
                    if (banner.getBook() != null) {
                        var book = banner.getBook();
                        List<BookRate> rates = bookRateRepository.findByBook_ID(book.getID());
                        List<BannerResponse.RateInfo> rateInfos = rates.stream()
                                .map(r -> BannerResponse.RateInfo.builder()
                                        .id(r.getId())
                                        .rate(r.getRate())
                                        .comment(r.getComment())
                                        .date(r.getDate())
                                        .build())
                                .collect(Collectors.toList());
                        BannerResponse.AuthorInfo authorInfo = null;
                        if (book.getAuthor() != null) {
                            authorInfo = BannerResponse.AuthorInfo.builder()
                                    .id(book.getAuthor().getId())
                                    .name(book.getAuthor().getName())
                                    .build();
                        }
                        bookInfo = BannerResponse.BookInfo.builder()
                                .id(book.getID())
                                .bookName(book.getBookName())
                                .coverImage(book.getCoverImage())
                                .author(authorInfo)
                                .bookRates(rateInfos)
                                .build();
                    }
                    return BannerResponse.builder()
                            .id(banner.getID())
                            .imageURL(banner.getImageURL())
                            .book(bookInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void updateBanner(int id, MultipartFile bannerFile) {
        // Retrieve the banner associated with the request
        Banner banner = bannerRepository.findById(id).get();
        if (banner != null) {
            // Check if the file is not null and not empty
            if (bannerFile != null && !bannerFile.isEmpty()) {
                String fileName = bannerFile.getOriginalFilename();
                log.info(fileName);

                // Validate the file extension
                String fileExtension =
                        fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("webp")) {
                    throw new AppException(ErrorCode.INVALID_BANNER); // Invalid file extension
                }
                File uploadDir = new File(UPLOAD_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String filePart = UPLOAD_PATH + File.separator;
                log.info(filePart);

                try {
                    // Save the file
                    bannerFile.transferTo(new File(filePart, fileName));
                    // Set the URL of the banner image
                    banner.setImageURL(PATH_AVATAR + fileName);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new AppException(ErrorCode.INVALID_BANNER_NULL);
                }

                // Save the updated banner
                bannerRepository.save(banner);
            } else {
                throw new AppException(ErrorCode.NULL_BANNER); // No file provided
            }
        }
    }
    public void deleteBanner(int id) {
        bannerRepository.deleteById(id);
    }
    public void createBanner(MultipartFile bannerFile) {

        Banner banner = new Banner();
        if (bannerFile != null && !bannerFile.isEmpty()) {
            String originalFileName = bannerFile.getOriginalFilename();
            log.info(originalFileName);
            // Validate the file extension
            String fileExtension = originalFileName
                    .substring(originalFileName.lastIndexOf(".") + 1)
                    .toLowerCase();

            if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("webp")) {
                throw new AppException(ErrorCode.INVALID_BANNER); // Invalid file extension
            }
            // Generate a unique file name using UUID
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + "_" + originalFileName;
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePart = UPLOAD_PATH + File.separator;
            try {
                // Save the file
                bannerFile.transferTo(new File(filePart, uniqueFileName));
                // Set the URL of the banner image
                banner.setImageURL(PATH_AVATAR + uniqueFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new AppException(ErrorCode.INVALID_BANNER_NULL);
            }
            bannerRepository.save(banner);
        } else {
            throw new AppException(ErrorCode.NULL_BANNER); // No file provided
        }
    }
}
