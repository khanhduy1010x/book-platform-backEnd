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
    @Value("${upload_banner.path_banner}")
    String UPLOAD_PATH;

    @NonFinal
    @Value("${path_banner.banner}")
    String PATH_AVATAR;
    public List<BannerResponse> viewBanner() {
        return bannerRepository.findAll().stream().map(bannerMapper::toResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void updateBanner(int id, MultipartFile bannerFile) {
        Banner banner = bannerRepository.findById(id).orElse(null);
        if (banner != null) {
            if (bannerFile != null && !bannerFile.isEmpty()) {
                String fileName = bannerFile.getOriginalFilename();
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
                String filePath = UPLOAD_PATH + File.separator + uniqueFileName;
                try {
                    bannerFile.transferTo(new File(filePath));
                    banner.setImageURL(PATH_AVATAR + uniqueFileName);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new AppException(ErrorCode.INVALID_BANNER_NULL);
                }
                bannerRepository.save(banner);
            } else {
                throw new AppException(ErrorCode.NULL_BANNER); // No file provided
            }
        }else{
            throw new AppException(ErrorCode.NULL_BANNER);
        }
    }
@PreAuthorize("hasRole('ADMIN')")
    public void createBanner(MultipartFile bannerFile) {
        Banner banner = new Banner();
        if (bannerFile != null && !bannerFile.isEmpty()) {
            String originalFileName = bannerFile.getOriginalFilename();
            log.info(originalFileName);
            String fileExtension = originalFileName
                    .substring(originalFileName.lastIndexOf(".") + 1)
                    .toLowerCase();
            if (!fileExtension.equals("jpg") && !fileExtension.equals("png") && !fileExtension.equals("webp")) {
                throw new AppException(ErrorCode.INVALID_BANNER); // Invalid file extension
            }
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + "_" + originalFileName;
            String filePath = UPLOAD_PATH + File.separator + uniqueFileName;
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            try {
                bannerFile.transferTo(new File(filePath));
                banner.setImageURL(PATH_AVATAR + uniqueFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new AppException(ErrorCode.INVALID_BANNER_NULL);
            }
            bannerRepository.save(banner);
        } else {
            throw new AppException(ErrorCode.NULL_BANNER);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBanner(int id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NULL_BANNER));
        bannerRepository.delete(banner);
    }


}
