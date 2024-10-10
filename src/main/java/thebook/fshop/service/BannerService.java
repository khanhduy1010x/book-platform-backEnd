package thebook.fshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import thebook.fshop.entity.Banner;
import thebook.fshop.repository.BannerRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BannerService {
    BannerRepository bannerRepository;

    public List<Banner> viewBanner() {
        return bannerRepository.findAll();
    }
}
