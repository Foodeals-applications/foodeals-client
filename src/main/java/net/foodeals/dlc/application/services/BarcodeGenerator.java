package net.foodeals.dlc.application.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BarcodeGenerator {
	
	public static String generate(String barcodeData) {
        try {
            // Création de l'instance MultiFormatWriter
            MultiFormatWriter barcodeWriter = new MultiFormatWriter();
            
            // Hints pour configurer l'encodage, ici spécification de l'encodage des caractères
            Map<EncodeHintType, String> hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Génération du BitMatrix représentant le code-barres
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, 200, 100, hints);

            // Conversion du BitMatrix en image ou en chaîne d'image (selon ce que tu souhaites faire)
            return encodeToImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour convertir le BitMatrix en image
    public static String encodeToImage(BitMatrix matrix) throws Exception {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        // Remplir les pixels du code-barres avec des couleurs noir et blanc
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? 0 : 0xFFFFFF)); // Noir ou Blanc
            }
        }

        // Conversion de l'image en format PNG (ou un autre format si nécessaire)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        
        // Retourner l'image encodée sous forme de chaîne (ou tu peux la sauvegarder dans un fichier)
        return byteArrayOutputStream.toString();
    }


}
