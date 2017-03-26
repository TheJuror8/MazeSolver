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
  private static int r_ratio;
  private static final double I_RATIO = 1.4;

  public static void imgPop (MazeMap currmap, int ratio) throws Exception {
    /* On crée un label et on l'intègre à la fenêtre,
    c'est le nouveau label actif de la classe  */
    lblimg = new JLabel();

    /* On définit l'image de fond du label et on le redimensionne */
    setBuffer(currmap, ratio);

    /* On définit la taille du label en fonction des dimensions du labyrinthe chargé */
    lblimg.setPreferredSize(new Dimension(currmap.getDim()[0]*r_ratio, currmap.getDim()[1]*r_ratio));

    frame.getContentPane().add(lblimg);

    /* On centre la fenêtre fonction des dimensions de l'écran */
    Dimension screenSize = getScreenSize();

    int x = (screenSize.width - frame.getSize().width)/2;
    int y = (screenSize.height - frame.getSize().height)/2;

    frame.setLocation(x, y);
    frame.setResizable(false);

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

  public static boolean isVisible() {
    if (frame != null)
      return frame.isVisible();
    else
      return false;
  }

  private static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  public static int getDefaultRatio(int[] dim) {
    Dimension screenSize = getScreenSize();
    int ratio = (int)((screenSize.height/I_RATIO)/ dim[1]);

    return ratio;
  }

  public static int getRatio () {
    return r_ratio;
  }

  public static boolean ratio2Big(int[] dim, int ratio) {
    /* On calcule les dimensions de l'image à afficher
           en fonction du ratio passé en paramètres */
    int width = dim[0]*ratio;
    int height = dim[1]*ratio;

    Dimension screenSize = getScreenSize();

    return (screenSize.width < width || screenSize.height < height);
  }

  /* Surcharge de setBuffer uniquement pour les rafraîchissements */
  public static void setBuffer () throws Exception {
    setBuffer (MazeSolver.current, getRatio());
  }

  public static void setBuffer (MazeMap currmap, int ratio) throws Exception {
    /* Si le ratio appliqué est trop grand il est plafonné à la valeur par défaut
    if ratio2Big(currmap.getDim(), ratio) {
      ratio = getDefaultRatio(currmap.getDim());
    }
     */

    int width = currmap.getDim()[0]*ratio;
    int height = currmap.getDim()[1]*ratio;

    /* On actualise le ratio de l'instance */
    r_ratio = ratio;

    Dimension new_dim = new Dimension(width, height);

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

    /* On redéfinit la taille du label en fonction des dimensions du labyrinthe chargé*/
    lblimg.setPreferredSize(new_dim);
    frame.pack();

    /* On renomme le JFrame */
    frame.setTitle(currmap.img_adr+" - Ratio "+r_ratio+":1");
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
