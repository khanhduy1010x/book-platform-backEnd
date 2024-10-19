package thebook.fshop.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.BannerResponse;
import thebook.fshop.entity.Banner;
import thebook.fshop.exception.AppException;
import thebook.fshop.exception.ErrorCode;
import thebook.fshop.mapper.BannerMapper;
import thebook.fshop.repository.BannerRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerService {
    BannerRepository bannerRepository;
    BannerMapper bannerMapper;
    @NonFinal
    @Value("${upload.path}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path.avatar}")
    String PATH_AVATAR;
    public List<BannerResponse> viewBanner() {
        return bannerRepository.findAll().stream().map(bannerMapper::toResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
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
                String uuid = UUID.randomUUID().toString();
                String uniqueFileName = uuid + "_" + fileName;
                File uploadDir = new File(UPLOAD_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                try {
                    // Save the file
                    bannerFile.transferTo(new File(uniqueFileName));
                    // Set the URL of the banner image
                    banner.setImageURL(PATH_AVATAR + uniqueFileName);
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

            try {
                // Save the file
                bannerFile.transferTo(new File(uniqueFileName));
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
