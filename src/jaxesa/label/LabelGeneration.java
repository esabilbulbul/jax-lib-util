package jaxesa.label;

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

public final class LabelGeneration 
{
    public static boolean generate(int pCodeType, String pFilePath, LabelItem pTitle, LabelItem pDetails, LabelItem pPrice, String pCurrency, LabelItem pSize, LabelItem pFsize, LabelItem pQRString)
    {
        switch(pCodeType)
        {
            case 0:

               return generateQRLabel(pFilePath, pTitle, pDetails, pPrice, pCurrency, pSize, pFsize, pQRString);

        }

        return true;
    }

    private static boolean generateQRLabel(String pFilePath, 
                                        LabelItem pTitle, 
                                        LabelItem pDetails, 
                                        LabelItem pPrice, 
                                        String pCurrency, 
                                        LabelItem pSize, 
                                        LabelItem pFsize, 
                                        LabelItem pQRString)
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

            //BufferedImage bufImg = new BufferedImage(850, 400, 1);
            BufferedImage bufImg = new BufferedImage(850, 400, 1);

            Graphics2D graphSheet = bufImg.createGraphics();
          
            graphSheet.setBackground(Color.WHITE);
            graphSheet.setColor(Color.BLACK);
            graphSheet.clearRect( 0, 0, bufImg.getWidth(), bufImg.getHeight() );

            // QR BARCODE SIZE
            int x, y, p, sz;
            int w = 0, h = 0;  // = 370; int h = 370;
            int wBarcode = w;
            
            x = pQRString.x;
            y = pQRString.y;
            w = pQRString.w;
            h = pQRString.h;
            graphSheet.drawImage(img, x, y, w, h, null);
            
            x = pFsize.x;
            y = pFsize.y;
            p = pFsize.p;
            sz = pFsize.sz;

            /*
            Font fontTitle  = new Font ("SansSerif", Font.BOLD, 48);
            Font fontDets = new Font ("Arial Nova", Font.BOLD, y);
            Font fontPrice  = new Font ("TimesNewRoman", Font.BOLD, p);
            Font fontsize  = new Font ("TimesNewRoman", Font.BOLD, sz);
            */
            Font fontTitle  = new Font ("SansSerif", Font.PLAIN, 48);
            Font fontDets = new Font ("Arial Nova", Font.PLAIN, y);
            Font fontPrice  = new Font ("TimesNewRoman", Font.BOLD, p);
            Font fontsize  = new Font ("TimesNewRoman", Font.BOLD, 56);//OPTION
            //Font fontsize  = new Font ("TimesNewRoman", Font.PLAIN, sz);//OPTION
            
            x = pTitle.x;
            y = pTitle.y;
            graphSheet.setFont(fontTitle);
            graphSheet.drawString(sTitle, x, y);

            x = pDetails.x;
            y = pDetails.y;
            graphSheet.setFont(fontDets);
            graphSheet.drawString(sDetails, x, y);

            x = pPrice.x;
            y = pPrice.y;
            graphSheet.setFont(fontPrice);
            graphSheet.drawString(sPrice + " " + pCurrency, x, y);

            x = pSize.x;
            y = pSize.y;
            graphSheet.setFont(fontsize);
            //graphSheet.setColor(Color.BLUE);
            graphSheet.drawString(sSize, x, y);

      
            ImageIO.write(bufImg, "jpg", new File(pFilePath));

            return true;
        }
        catch(Exception e)
        {
            //String s = "";
            return false;
        }

    }
    
}
