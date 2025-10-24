package net.foodeals.dlc.application.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StickerGenerator {

    // Méthode principale pour générer un sticker avec les données fournies
    public static String generateStickerFromData(String productName, String discountedPrice, String barcode) {
        try {
            // Dimensions de l'image du sticker
            int width = 300;
            int height = 400;
            
            // Créer une image vide
            BufferedImage sticker = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = sticker.createGraphics();
            
            // Définir les couleurs et les polices pour l'image
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);  // Fond blanc
            g2d.fillRect(0, 0, width, height);

            // Dessiner du texte (nom du produit)
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Produit: " + productName, 20, 30);
            
            // Dessiner le prix
            g2d.setFont(new Font("Arial", Font.BOLD, 22));
            g2d.setColor(Color.RED);  // Prix en rouge
            g2d.drawString("Prix: " + discountedPrice + " DH", 20, 70);
            
            // Générer et ajouter le code-barres
            String barcodeImage = generateBarcode(barcode);
            g2d.drawImage(getBarcodeImage(barcodeImage), 20, 100, null);
            
            // Ajouter d'autres informations si nécessaire, comme un logo, etc.

            // Sauvegarder l'image dans un fichier (optionnel)
            File outputFile = new File("sticker.png");
            ImageIO.write(sticker, "PNG", outputFile);
            
            // Convertir l'image en chaîne Base64 si nécessaire
            return encodeImageToBase64(sticker);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour générer un code-barres en image à partir d'une donnée
    private static String generateBarcode(String barcodeData) {
        try {
            MultiFormatWriter barcodeWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, 200, 100, hints);
            
            // Convertir le BitMatrix en image
            return encodeToImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour convertir le BitMatrix en image
    private static  String encodeToImage(BitMatrix matrix) throws Exception {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? 0 : 0xFFFFFF)); // Noir ou Blanc
            }
        }
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        
        // Retourner l'image sous forme de chaîne Base64
        return byteArrayOutputStream.toString();
    }

    // Méthode pour convertir une image en chaîne Base64 (si nécessaire)
    private static String encodeImageToBase64(BufferedImage image) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return java.util.Base64.getEncoder().encodeToString(imageBytes);
    }

    // Convertir l'image du code-barres en BufferedImage
    private static BufferedImage getBarcodeImage(String barcodeImage) throws Exception {
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(barcodeImage);
        return ImageIO.read(new java.io.ByteArrayInputStream(decodedBytes));
    }

   
}
