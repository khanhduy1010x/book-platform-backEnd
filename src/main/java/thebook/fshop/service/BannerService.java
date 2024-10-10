package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.Banner;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.repository.BannerRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerService {
    BannerRepository bannerRepository;
SecurityService securityService;
    public List<Banner> viewBanner() {
        return bannerRepository.findAll();
    }
@PreAuthorize("hasRole('ADMIN')")
    public void updateBanner(int id, MultipartFile bannerFile) {

            String UPLOAD_PATH = "D:\\OJT\\Book4.0\\book4_0\\src\\main\\resources\\static\\banner";
            String PATH_AVATAR = "http://localhost:9999/banner/";
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
    @PreAuthorize("hasRole('ADMIN')")
    public void createBanner(MultipartFile bannerFile) {
        String UPLOAD_PATH = "D:\\OJT\\Book4.0\\book4_0\\src\\main\\resources\\static\\banner";
        String PATH_AVATAR = "http://localhost:9999/banner/";
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBanner(int id) {
        Banner banner = bannerRepository.findById(id).get();
        if (banner==null){
            throw new AppException(ErrorCode.NULL_BANNER);
        }else {
            bannerRepository.delete(banner);
        }

    }
}
