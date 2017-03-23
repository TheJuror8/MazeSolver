/**
 *
 * @author slimh
 */
package mazesolver;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ImagePop {

    private static JFrame frame;
    private static JLabel lblimg;

    public static void imgPop (MazeMap currmap) throws Exception {
        /* On crée un label et on l'intègre à la fenêtre,
              c'est le nouveau label actif de la classe  */
        lblimg = new JLabel();

        /* On définit l'image de fond du label et on le redimensionne */
        setBuffer(currmap);

        /* On définit la taille du label en fonction des dimensions du labyrinthe chargé */
        lblimg.setPreferredSize(new Dimension(currmap.getDim()[0]*10, currmap.getDim()[1]*10));

        frame.getContentPane().add(lblimg);

        /* On centre la fenêtre fonction des dimensions de l'écran */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - frame.getSize().width)/2;
        int y = (screenSize.height - frame.getSize().height)/2;

        frame.setLocation(x, y);

        /* On affiche le popup */
        frame.pack();
        frame.setVisible(true);

    }

    public static boolean isDisplayable() {
      if (frame != null)
        return frame.isDisplayable();
      else
        return false;
    }

    public static void setBuffer (MazeMap currmap) throws Exception {
        /* On calcule les dimensions de l'image à afficher
        => pour l'instant : 10*width, 10*height */
        int width = currmap.getDim()[0]*10;
        int height = currmap.getDim()[1]*10;

        /* On charge le buffer actif dans une variable locale
                       et on la convertit en Image            */
        BufferedImage imgbuff_0 = ImageLecture.getBuffer (currmap);
        Image imgbuff = (Image) imgbuff_0;

        /* On redimensionne l'image */
        imgbuff = imgbuff.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);

        /* On crée un objet icone à partir de l'objet image et on modifie le label avec */
        ImageIcon imgico = new ImageIcon(imgbuff);

        /* On assigne au JLabel l'objet ImageIcon */
        lblimg.setIcon (imgico);
    }

    public static void createGUI(String imgsrc) {
      frame = new JFrame(imgsrc);

      //On configure l'évènement de suppression de l'objet
      WindowListener exitListener = new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
              //On supprime la fenêtre JFrame
              frame.dispose();
          }
      };

      frame.addWindowListener(exitListener);
   }
}
