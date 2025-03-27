package net.foodeals.common.services.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.foodeals.common.services.BarcodeService;

@Service
public class BarcodeServiceImpl implements BarcodeService {

	@Override
	public String readBarcode(InputStream inputStream) throws IOException, NotFoundException {
	    
	    BufferedImage bufferedImage = ImageIO.read(inputStream);
	    
	    if (bufferedImage == null) {
	        throw new IOException("Failed to read image from InputStream");
	    }

	    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

	    return new MultiFormatReader().decode(bitmap).getText();
	}


}
