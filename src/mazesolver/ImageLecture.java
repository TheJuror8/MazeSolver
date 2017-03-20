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

    public static void saveBuffer (File img_src, File img_name, MazeMap solved_maze) throws IOException {
      // Variables locales utiles
      BufferedImage img_buffer;
      int[] dim = solved_maze.getDim();
      NodeType type;

      // On charge en mémoire l'image donnée en source
      img_buffer = ImageIO.read(img_src);

      // On parcourt le MazeMap et pour chaque case en PATH\START\END on change le pixel correspondant sur le buffer
      for (int y = 0; y<dim[1]; y++){ //hauteur
        for (int x = 0; x<dim[0]; x++){ //largeur
          type = solved_maze.getCase(x, y).getType();

          switch (type) {
            case PATH:
              img_buffer.setRGB (x, y, -10496); break; // couleur RGB int dorée
            case START:
              img_buffer.setRGB (x, y, -15277799); break; // couleur RGB int vert
            case END:
              img_buffer.setRGB (x, y, -906214); break; // couleur RGB int rouge
          }
        }
      }

      // On écrit le buffer de l'image modifiée dans le fichier img_src
      ImageIO.write(img_buffer, "bmp", img_name);

    }

    public static MazeMap chargerImage (File img_location) throws IOException {
        // Variables locales utiles
        BufferedImage img_buffer;
        MazeMap img_map;

        img_buffer = ImageIO.read(img_location);

        int[][] result = bufferTo2D (img_buffer);

        img_map = new MazeMap(result.length, result[0].length, img_location.getPath()); // on crée une nouvelle map de dimension appropriée

        for (int y = 0; y<result.length; y++){
          for (int x = 0; x<result[y].length; x++){
            if (result[y][x]==-1) {// si la case est blanche
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

        int[][] result = new int[height][width];
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

         return result;
   }
}
