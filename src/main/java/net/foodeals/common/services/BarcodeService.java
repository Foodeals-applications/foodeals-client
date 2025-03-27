package net.foodeals.common.services;

import java.io.IOException;
import java.io.InputStream;

import com.google.zxing.NotFoundException;



public interface BarcodeService {
	

	public String readBarcode(InputStream imageStream) throws IOException,NotFoundException;

}
