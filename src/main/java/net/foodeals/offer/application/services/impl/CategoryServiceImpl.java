package net.foodeals.offer.application.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.CategoryService;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.offer.domain.enums.Category.CategoryPair;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	@Override
	public List<CategoryPair> getAllCategories() {
		return Category.getCategoryPairs();
	}

}
