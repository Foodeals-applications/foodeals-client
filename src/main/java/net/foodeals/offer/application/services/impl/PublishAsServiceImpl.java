package net.foodeals.offer.application.services.impl;

import java.util.List;

import net.foodeals.core.domain.enums.PublishAs;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.PublishAsService;


@Service
@Transactional
@RequiredArgsConstructor
public class PublishAsServiceImpl implements PublishAsService{

	@Override
	public List<PublishAs.PublishAsPair> getAllPublishAs() {
		return PublishAs.getPublishAsPairs();
	}

}
