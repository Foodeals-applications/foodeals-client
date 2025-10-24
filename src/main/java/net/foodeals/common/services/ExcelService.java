package net.foodeals.common.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
	
	 public void readProductsFromExcel(MultipartFile file) throws IOException;
	 public void readDlcsFromExcel(MultipartFile file) throws IOException;
	 

}
