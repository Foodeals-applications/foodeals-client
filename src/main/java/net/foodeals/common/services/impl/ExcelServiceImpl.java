package net.foodeals.common.services.impl;

import net.foodeals.common.services.ExcelService;
import net.foodeals.dlc.domain.entities.Dlc;
import net.foodeals.product.domain.entities.*;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.product.domain.repositories.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

	// On a injecter le repository car on a besoin d'acceder directement au données
	// sans passer par la logique métier

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductBrandRepository productBrandRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductSubCategoryRepository productSubCategoryRepository;

	@Autowired
	private RayonRepository rayonRepository;

	@Override
	public void readProductsFromExcel(MultipartFile file) throws IOException {
		List<Product> products = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					products.add(createProductFromRow(row));
				}
			}
		}
		productRepository.saveAll(products);

	}

	private Product createProductFromRow(Row row) {
		Product product = new Product();
		product.setProductImagePath(getCellValueAsString(row.getCell(0)));
		product.setName(getCellValueAsString(row.getCell(1)));
		product.setBarcode(getCellValueAsString(row.getCell(2)));

		String brandName = getCellValueAsString(row.getCell(3));
		// product.setBrand(getOrCreateProductBrand(brandName));

		product.setDescription(getCellValueAsString(row.getCell(4)));

		// product.setDescription(getCellValueAsString(row.getCell(4)));
		// product.setPrice(new Price(new
		// BigDecimal(getCellValueAsString(row.getCell(5))),
		// Currency.getInstance("EUR")));

		String categoryName = getCellValueAsString(row.getCell(5));
		product.setCategory(getOrCreateProductCategory(categoryName));

		String subCategoryName = getCellValueAsString(row.getCell(6));
		product.setSubcategory(getOrCreateProductSubCategory(subCategoryName));

		String rayonName = getCellValueAsString(row.getCell(7));
		// product.setRayon(getOrCreateRayon(rayonName));

		return product;
	}

	private ProductBrand getOrCreateProductBrand(String brandName) {
		return productBrandRepository.findByName(brandName)
				.orElseGet(() -> productBrandRepository.save(ProductBrand.create(brandName, brandName)));
	}

	private ProductCategory getOrCreateProductCategory(String categoryName) {
		return productCategoryRepository.findByName(categoryName)
				.orElseGet(() -> productCategoryRepository.save(ProductCategory.create(categoryName, categoryName)));
	}

	private ProductSubCategory getOrCreateProductSubCategory(String subCategoryName) {
		return productSubCategoryRepository.findByName(subCategoryName).orElseGet(
				() -> productSubCategoryRepository.save(ProductSubCategory.create(subCategoryName, subCategoryName)));
	}

	private Rayon getOrCreateRayon(String rayonName) {
		return rayonRepository.findByName(rayonName).orElseGet(() -> rayonRepository.save(Rayon.create(rayonName)));
	}

	private String getCellValueAsString(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";
		}

	}

	@Override
	public void readDlcsFromExcel(MultipartFile file) throws IOException {

		List<Dlc> dlcs = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					dlcs.add(createDlcFromRow(row));
				}
			}
		}

	}

	public Dlc createDlcFromRow(Row row) {

		String barcode = getCellValueAsString(row.getCell(1));
		Product product = productRepository.findByBarcode(barcode)
				.orElseThrow(() -> new ProductNotFoundException(barcode));

		Date expiryDate = parseDate(getCellValueAsString(row.getCell(2)));

		int quantity = Integer.parseInt(getCellValueAsString(row.getCell(3)));
		Dlc dlc = new Dlc(product, expiryDate, quantity);
		return dlc;
	}

	private Date parseDate(String dateString) {
		final String DATE_FORMAT = "dd/MM/yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: " + dateString);
		}
	}
}