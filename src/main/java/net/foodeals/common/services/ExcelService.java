package net.foodeals.common.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
	
	 public void readProductsFromExcel(MultipartFile file) throws IOException;
	 public void readDlcsFromExcel(MultipartFile file) throws IOException;
	 

}
