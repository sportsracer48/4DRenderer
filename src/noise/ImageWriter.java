package noise;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageWriter {
    //just convinence methods for debug

    public static void greyWriteImage(double[][] data){
        //this takes and array of doubles between 0 and 1 and generates a grey scale image from them

        BufferedImage image = new BufferedImage(data.length,data[0].length, BufferedImage.TYPE_INT_RGB);
        
        double min=Double.POSITIVE_INFINITY;
        double max=Double.NEGATIVE_INFINITY;
        
        for (int y = 0; y < data[0].length; y++)
        {
          for (int x = 0; x < data.length; x++)
          {
            min=Math.min(data[x][y], min);
            max=Math.max(data[x][y], max);
          }
        }
        
        for (int y = 0; y < data[0].length; y++)
        {
          for (int x = 0; x < data.length; x++)
          {
            
            float val=(float) rescale(data[x][y],min,max);
            Color col=new Color(val,val,val); 
            image.setRGB(x, y, col.getRGB());
          }
        }

        try {
            // retrieve image
            File outputfile = new File("saved.png");
            outputfile.createNewFile();

            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            //o no! Blank catches are bad
            throw new RuntimeException("I didn't handle this very well");
        }
    }
    
    public static double rescale(double val, double min, double max)
    {
    	return (val-min)/(max-min);
    }



}