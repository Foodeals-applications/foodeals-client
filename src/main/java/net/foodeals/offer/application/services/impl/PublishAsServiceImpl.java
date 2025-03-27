package net.foodeals.offer.application.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.PublishAsService;
import net.foodeals.offer.domain.enums.PublishAs;
import net.foodeals.offer.domain.enums.PublishAs.PublishAsPair;


@Service
@Transactional
@RequiredArgsConstructor
public class PublishAsServiceImpl implements PublishAsService{

	@Override
	public List<PublishAsPair> getAllPublishAs() {
		return PublishAs.getPublishAsPairs();
	}

}
