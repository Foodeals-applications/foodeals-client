package net.foodeals.offer.infrastructure.interfaces.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.offer.application.services.CategoryService;
import net.foodeals.offer.domain.enums.Category.CategoryPair;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService ;
	
	
	@GetMapping("/all")
    public List<CategoryPair> getCategories() {
        return categoryService.getAllCategories();
    }

}
