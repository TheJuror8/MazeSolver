/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Color;

/**
 *
 * @author habib
 */
public class ImageLecture {

    public static MazeMap chargerImage (String img_location) throws IOException {
        // Variables locales utiles
        BufferedImage img_buffer;
        MazeMap img_map;
        Color pix;
        String hex;

        img_buffer = ImageIO.read(new File (img_location));

        int[][] result = bufferTo2D (img_buffer);

        img_map = new MazeMap(result.length, result[0].length, img_location); // on crée une nouvelle map de dimension appropriée

        for (int y = 0; y<result.length; y++){
          for (int x = 0; x<result[y].length; x++){
            // VRAIMENT PAS OPTIMISE
            // A SIMPLIFIER
            pix = new Color (result[y][x], false);
            hex = "#"+Integer.toHexString(pix.getRGB()).substring(2);
            if (hex.equals("#ffffff")) {// si la case est blanche
              img_map.getCase(x, y).setType (NodeType.EMPTY);
            } else { // si elle est noire
              img_map.getCase(x, y).setType (NodeType.OBSTACLE);
            }
          }
        }

        return img_map;
    }


    public static int[][] bufferTo2D(BufferedImage image) {
      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      final int width = image.getWidth();
      final int height = image.getHeight();
      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

      int[][] result = new int[height][width];
      if (hasAlphaChannel) {
         final int pixelLength = 4;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      } else {
         final int pixelLength = 3;
         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += -16777216; // 255 alpha
            argb += ((int) pixels[pixel] & 0xff); // blue
            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      }

      return result;
   }
}
