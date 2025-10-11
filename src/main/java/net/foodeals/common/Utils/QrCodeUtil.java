package net.foodeals.common.Utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QrCodeUtil {

    public static String generateQRCodeBase64(String text, int width, int height) throws WriterException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            var bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération QR Code", e);
        }
    }
}