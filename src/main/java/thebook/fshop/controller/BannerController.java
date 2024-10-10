package thebook.fshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thebook.fshop.DTO.Response.ApiResponse;
import thebook.fshop.entity.Banner;
import thebook.fshop.service.BannerService;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerController {
    BannerService bannerService;
    @GetMapping("/view-banner")
    ApiResponse<List<Banner>> viewBanner() {
        return ApiResponse.<List<Banner>>builder().result( bannerService.viewBanner()).build();


    }
}
