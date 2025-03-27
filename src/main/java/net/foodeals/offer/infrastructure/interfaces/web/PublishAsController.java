package net.foodeals.offer.infrastructure.interfaces.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.PublishAsService;
import net.foodeals.offer.domain.enums.PublishAs.PublishAsPair;

@RestController
@RequestMapping("v1/publishas")
@RequiredArgsConstructor
public class PublishAsController {
	
	private final PublishAsService publishAsService;
	
	@GetMapping("/all")
    public List<PublishAsPair> getPublishAs() {
        return publishAsService.getAllPublishAs();
    }
	
	

}
