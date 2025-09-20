package net.foodeals.home.infrastructure.controller;


import lombok.RequiredArgsConstructor;

import net.foodeals.home.application.dtos.HomeContentResponse;
import net.foodeals.home.application.service.HomeContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/home")
@RequiredArgsConstructor
public class HomeContentController {

    private final net.foodeals.home.application.service.HomeContentService homeContentService;

    @GetMapping("/content")
    public HomeContentResponse getHomeContent(
            @RequestParam(value = "filters", required = false) String filters
    ) {
        return homeContentService.getFilteredContent(filters);
    }

}