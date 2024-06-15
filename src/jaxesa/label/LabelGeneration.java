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
    public static boolean generate(int       pPrinterCodeType, 
                                   String    pFilePath, 
                                   LabelItem pTitle, 
                                   LabelItem pDetails, 
                                   LabelItem pPrice1,
                                   LabelItem pPrice2, 
                                   String    pCurrency, 
                                   LabelItem pSize, 
                                   LabelItem pFsize, 
                                   LabelItem pQRString)
    {
        switch(pPrinterCodeType)
        {
            case 0:// DYMO

               return generateQRLabel(pFilePath, pTitle, pDetails, pPrice1, pCurrency, pSize, pFsize, pQRString);

            case 1://BROTHER QL 810W

                int iStartXSTR = 105;
                int iStartYSTR = 20;

                // BROTHER ADJUSTMENTS
                //------------------------------------------------------
                pQRString.x = 0;
                pQRString.y = 0;//iStartYSTR;
                pQRString.w = 90;
                pQRString.h = 90;

                pTitle.x = iStartXSTR;
                pTitle.y = iStartYSTR;
                pTitle.p = 0;
                pTitle.h = 0;

                pDetails.x = iStartXSTR;
                pDetails.y = 20 + iStartYSTR;
                pDetails.p = 0;
                pDetails.h = 0;

                // Price Installment
                pPrice2.x = iStartXSTR;
                pPrice2.y = 40 + iStartYSTR;
                pPrice2.p = 0;
                pPrice2.h = 0;

                // Price Upfront
                pPrice1.x = iStartXSTR;
                pPrice1.y = 70 + iStartYSTR;
                pPrice1.p = 0;
                pPrice1.h = 0;

                // OPTION
                pSize.x = 10;
                pSize.y = 85 + iStartYSTR;//no iStartYSTR
                pSize.p = 0;
                pSize.h = 0;

                return generateQRLabel4BrotherQL810W(pFilePath, 
                                                     pTitle, 
                                                     pDetails, 
                                                     pPrice1, 
                                                     pPrice2, 
                                                     pCurrency, 
                                                     pSize, 
                                                     pFsize, 
                                                     pQRString);

        }

        return true;
    }

    public static boolean generateQRLabel4BrotherQL810W(String    pFilePath, 
                                                        LabelItem pTitle, 
                                                        LabelItem pDetails, 
                                                        LabelItem pPriceUpfront,
                                                        LabelItem pPriceInstallment,
                                                        String    pCurrency,
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
            
            String sTitle     = pTitle.name;
            String sDetails   = pDetails.name;
            String sPrice1    = pPriceUpfront.name;
            String sPrice2    = pPriceInstallment.name;
            String sQRStr     = pQRString.name;
            String sSize      = pSize.name;
            String sFsize     = pFsize.name;

            BarcodeQRCode qr = new BarcodeQRCode(sQRStr, 1, 1, null);

            Image img = qr.createAwtImage(Color.BLACK, Color.WHITE);

            // PIXEL = 3.78 x MM (FOR IMAGE = PIXEL)
            // INCH  = 72 POINT
            // INCH  = 25.4 MM

            //--------------------------------------
            // 62 X 29 mm (values)
            // MM    = 62 X 29
            // INCH  = 2.44 - 1.14
            // POINT = 175 - 82
            //--------------------------------------

            // PIXELS = MM x 3.78
            // Printer Label = 62 x 29 mm = 234 x 109 px
            int BRO_SMALL_ADDRESS_WIDTH_IN_PIXELS  = 234;//62mm
            int BRO_SMALL_ADDRESS_HEIGHT_IN_PIXELS = 109;//29mm

            BufferedImage bufImg = new BufferedImage(BRO_SMALL_ADDRESS_WIDTH_IN_PIXELS, BRO_SMALL_ADDRESS_HEIGHT_IN_PIXELS, 1);//pixels

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

            x  = pFsize.x;
            y  = pFsize.y;
            p  = pFsize.p;
            sz = pFsize.sz;

            Font fontTitle  = new Font ("SansSerif", Font.PLAIN, 14);//dont change this and the next. Because phone camera get lost of focusing barcode
            Font fontDets = new Font ("Arial Nova", Font.PLAIN, 14);
            Font fontPrice  = new Font ("TimesNewRoman", Font.BOLD, 15);
            Font fontPriceIns  = new Font ("TimesNewRoman", Font.PLAIN, 14);
            Font fontsize  = new Font ("TimesNewRoman", Font.BOLD, 14);
            /*
            Font fontTitle  = new Font ("Consolas", Font.PLAIN, 14);
            Font fontDets = new Font ("Consolas", Font.PLAIN, 14);
            Font fontPrice  = new Font ("Courier New", Font.BOLD, 14);
            Font fontsize  = new Font ("Courier New", Font.BOLD, 14);//OPTION
            */
            //Font fontsize  = new Font ("TimesNewRoman", Font.PLAIN, sz);//OPTION

            x = pTitle.x;
            y = pTitle.y;
            graphSheet.setFont(fontTitle);
            graphSheet.drawString(sTitle, x, y);

            x = pDetails.x;
            y = pDetails.y;
            graphSheet.setFont(fontDets);
            graphSheet.drawString(sDetails, x, y);

            x = pPriceUpfront.x;
            y = pPriceUpfront.y;
            graphSheet.setFont(fontPrice);
            graphSheet.drawString(sPrice1 + " " + pCurrency, x, y);

            if(sPrice2.trim().length()>0)
            {
                x = pPriceInstallment.x;
                y = pPriceInstallment.y;
                graphSheet.setFont(fontPriceIns);
                graphSheet.drawString(sPrice2 + " " + pCurrency, x, y);
            }

            x = pSize.x;
            y = pSize.y;
            graphSheet.setFont(fontsize);
            //graphSheet.setColor(Color.BLUE);
            graphSheet.drawString(sSize, x, y);

            ImageIO.write(bufImg, "jpg", new File(pFilePath));

            return true;

            /*
            Scanner s = new Scanner(System.in);
            int width, height ;
            String tl;
            long a;

            String sTitle     = pTitle.name;
            String sDetails   = pDetails.name;
            String sPrice1    = pPriceUpfront.name;
            String sPrice2    = pPriceInstallment.name;
            String sQRStr     = pQRString.name;
            String sSize      = pSize.name;
            String sFsize     = pFsize.name;

            BarcodeQRCode qr = new BarcodeQRCode(sQRStr, 1, 1, null);

            Image img = qr.createAwtImage(Color.BLACK, Color.WHITE);

            BufferedImage bufImg = new BufferedImage(850, 400, 1);//dymo
            //BufferedImage bufImg = new BufferedImage(234, 109, 1);//brother worked 
            //BufferedImage bufImg = new BufferedImage(234, 109, 1);//pixels

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

            Font fontTitle  = new Font ("Consolas", Font.PLAIN, 14);
            Font fontDets = new Font ("Consolas", Font.PLAIN, 14);
            Font fontPrice  = new Font ("Courier New", Font.BOLD, 14);
            Font fontsize  = new Font ("Courier New", Font.BOLD, 14);//OPTION
            //Font fontsize  = new Font ("TimesNewRoman", Font.PLAIN, sz);//OPTION

            x = pTitle.x;
            y = pTitle.y;
            graphSheet.setFont(fontTitle);
            graphSheet.drawString(sTitle, x, y);

            x = pDetails.x;
            y = pDetails.y;
            graphSheet.setFont(fontDets);
            graphSheet.drawString(sDetails, x, y);
            
            x = pPriceUpfront.x;
            y = pPriceUpfront.y;
            graphSheet.setFont(fontPrice);
            graphSheet.drawString(sPrice1 + " " + pCurrency, x, y);

            if(sPrice2.trim().length()>0)
            {
                x = pPriceInstallment.x;
                y = pPriceInstallment.y;
                graphSheet.setFont(fontPrice);
                graphSheet.drawString(sPrice2 + " " + pCurrency, x, y);
            }
            
            x = pSize.x;
            y = pSize.y;
            graphSheet.setFont(fontsize);
            //graphSheet.setColor(Color.BLUE);
            graphSheet.drawString(sSize, x, y);

            ImageIO.write(bufImg, "png", new File(pFilePath));

            return true;
            */
        }
        catch(Exception e)
        {
            //String s = "";
            return false;
        }

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
