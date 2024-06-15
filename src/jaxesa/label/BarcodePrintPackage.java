
package jaxesa.label;

public class BarcodePrintPackage {

    public LabelItem title = new LabelItem();

    public LabelItem dets  = new LabelItem();

    public LabelItem price = new LabelItem();
    
    public LabelItem price2 = new LabelItem();//installment
    
    public String currency = "";

    public LabelItem size  = new LabelItem();
    
    public LabelItem fsize  = new LabelItem();

    public LabelItem font  = new LabelItem();

    public LabelItem qr    = new LabelItem();
    
    public int count    = 1 ;//default 1. Number of copy to print 
    
    public String brand = "";
    public String itemCode = "";
        
}
