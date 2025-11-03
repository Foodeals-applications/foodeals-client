package net.foodeals.offer.infrastructure.interfaces.web;

import java.util.List;

import net.foodeals.core.domain.enums.PublishAs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.PublishAsService;

@RestController
@RequestMapping("v1/publishas")
@RequiredArgsConstructor
public class PublishAsController {
	
	private final PublishAsService publishAsService;
	
	@GetMapping("/all")
    public List<PublishAs.PublishAsPair> getPublishAs() {
        return publishAsService.getAllPublishAs();
    }
	
	

}
