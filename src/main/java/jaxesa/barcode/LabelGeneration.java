/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.barcode;

import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfName;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Lenovo
 */
public final class LabelGeneration 
{
    public static boolean generate(int pCodeType, String pFilePath, LabelItem pTitle, LabelItem pDetails, LabelItem pPrice, String pCurrency, LabelItem pSize, LabelItem pFsize, LabelItem pQRString)
    {
        switch(pCodeType)
        {
            case 0:

               generateQRLabel(pFilePath, pTitle, pDetails, pPrice, pCurrency, pSize, pFsize, pQRString);

               break;
        }

        return true;
    }
    
    private static void generateQRLabel(String pFilePath, LabelItem pTitle, LabelItem pDetails, LabelItem pPrice, String pCurrency, LabelItem pSize, LabelItem pFsize, LabelItem pQRString)
    {
        try
        {
            Scanner s = new Scanner(System.in);
            int width, height ;
            String tl;
            long a;

            String sTitle    = pTitle.name;
            String sDetails  = pDetails.name;
            String sPrice    = pPrice.name;
            String sQRStr    = pQRString.name;
            String sSize     = pSize.name;
            String sFsize     = pFsize.name;

            
            BarcodeQRCode qr = new BarcodeQRCode(sQRStr, 1, 1, null);

            Image img = qr.createAwtImage(Color.BLACK, Color.WHITE);

            BufferedImage bufImg = new BufferedImage(900, 400, 1);

            Graphics2D graphSheet = bufImg.createGraphics();
          
            graphSheet.setBackground(Color.WHITE);
            graphSheet.setColor(Color.BLACK);
            graphSheet.clearRect( 0, 0, bufImg.getWidth(), bufImg.getHeight() );

            // QR BARCODE SIZE
            int x, y;
            int w = 370; int h = 370;
            int wBarcode = w;
            
            x = pQRString.x;
            y = pQRString.y;
            w = pQRString.w;
            h = pQRString.h;
            
            graphSheet.drawImage(img, x, y, w, h, null);
            
            x = pFsize.x;
            y = pFsize.y;

            Font fontBold  = new Font ("SansSerif", Font.BOLD, x);
            Font fontPlain = new Font ("Courier New", Font.PLAIN, y);
            Font fontBold1  = new Font ("TimesNewRoman", Font.BOLD, x);
            
           
            
            x = pTitle.x;
            y = pTitle.y;
            graphSheet.setFont(fontBold);
            graphSheet.drawString(sTitle, x, y);


            x = pDetails.x;
            y = pDetails.y;
            graphSheet.setFont(fontPlain);
            graphSheet.drawString(sDetails, x, y);

            x = pPrice.x;
            y = pPrice.y;
            graphSheet.setFont(fontBold1);
            graphSheet.drawString(sPrice + " " + pCurrency, x, y);

            x = pSize.x;
            y = pSize.y;
            graphSheet.setFont(fontBold);
            //graphSheet.setColor(Color.BLUE);
            graphSheet.drawString(sSize, x, y);

      
            ImageIO.write(bufImg, "jpg", new File(pFilePath));

        }
        catch(Exception e)
        {
            String s = "";
        }

    }
}
