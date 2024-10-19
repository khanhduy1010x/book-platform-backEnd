package thebook.fshop.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.DTO.Response.BannerResponse;
import thebook.fshop.entity.Banner;
import thebook.fshop.service.BannerService;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerController {

    BannerService bannerService;

    @GetMapping("/view-banner")
    ApiResponse<List<BannerResponse>> viewBanner() {
        return ApiResponse.<List<BannerResponse>>builder()
                .result(bannerService.viewBanner())
                .build();
    }

    @PostMapping(
            value = "/edit-banner",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Banner> editBanner(
            @RequestParam(value = "id", required = false) int id,
            @RequestParam(value = "banner", required = false) MultipartFile banner) {
        bannerService.updateBanner(id, banner);
        return ApiResponse.<Banner>builder().build();
    }

    @PostMapping(
            value = "/create-banner",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Banner> createBanner(@RequestParam(value = "banner", required = false) MultipartFile banner) {
        bannerService.createBanner(banner);
        return ApiResponse.<Banner>builder().build();
    }
    @DeleteMapping( value = "/delete-banner")
    public ApiResponse<Banner> deleteBanner(@RequestParam(value = "id", required = false) int id) {
        bannerService.deleteBanner(id);
        return ApiResponse.<Banner>builder().build();}

}
