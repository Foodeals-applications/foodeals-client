package net.foodeals.offer.application.services.impl;

import java.util.List;

import net.foodeals.core.domain.enums.Category;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.CategoryService;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	@Override
	public List<Category.CategoryPair> getAllCategories() {
		return Category.getCategoryPairs();
	}

}
