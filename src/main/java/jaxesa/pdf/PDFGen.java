/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.pdf;

import com.itextpdf.text.pdf.PdfDocument;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Administrator
 */
public final class PDFGen 
{
    public static void createPDFFromImages(ArrayList<String> pImgFiles, String pOutPDFFile) throws Exception
    {
        try
        {
            PDDocument document = new PDDocument();
            
            // MULTI THREAD ADDING BLOCK STARTING HERE
            //----------------------------------------------------------------
            
            // Create a thread pool
            ExecutorService executor = Executors.newFixedThreadPool(pImgFiles.size());
            
            // Add images to the PDF document using multiple threads
            for (String imagePath : pImgFiles) 
            {
                executor.execute(() -> {
                                            try 
                                            {
                                                addNewPage(document, imagePath);
                                            } 
                                            catch (Exception e) 
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                );
            }

            // Shut down the executor
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            
            // multi thread ends here
            //----------------------------------------------------------------

            // Save the document to a file
            document.save(pOutPDFFile);

            // Close the document
            document.close();

        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    public static void addNewPage(PDDocument document, String imagePath)
    {
        synchronized (document)
        {
            try
            {
                float widthInPoints = 175; // 8.5 inches
                float heightInPoints = 82; // 11 inches

                // Create a new page with letter size (8.5 inches by 11 inches)
                PDPage page = new PDPage(new PDRectangle(widthInPoints, heightInPoints));

                // Add the page to the document
                document.addPage(page);

                // Create a content stream for the page
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                float margin = 0; // Adjust margin as needed
                float startY = page.getMediaBox().getHeight() - margin;

                // Add the image to the PDF page
                //BufferedImage image = ImageIO.read(new File(imagePath));
                PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

                // Add the image to the PDF document
                float scale = 0.65f;  // Adjust the scale as needed
                contentStream.drawImage(//LosslessFactory.createFromImage(document, image), 
                                        image,
                                        margin, 
                                        0,//startY - image.getHeight() * scale, 
                                        image.getWidth() * scale, 
                                        image.getHeight() * scale);


                // Close the content stream
                contentStream.close();
            }
            catch(Exception e)
            {
                return;
            }
        }
    }
}
