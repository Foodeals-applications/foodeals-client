package net.foodeals.banner.infrastructure.controller;

import lombok.AllArgsConstructor;

import net.foodeals.core.domain.entities.Banner;
import net.foodeals.core.repositories.BannerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/banners")
@AllArgsConstructor
public class BannerController {

    private final BannerRepository bannerRepository;

    @GetMapping
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @PostMapping
    public Banner createBanner(@RequestBody Banner banner) {
        return bannerRepository.save(banner);
    }
}