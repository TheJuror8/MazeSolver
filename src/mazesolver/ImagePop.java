/**
 *
 * @author slimh
 */
package mazesolver;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePop {

    private static JFrame frame;
    private static final String CACHE_F = "tmp/buff01.bmp";

    public static void imgPop (MazeMap currmap) throws Exception {
        //On copie le buffer actif dans un fichier cache
        File imageFile = new File (CACHE_F);
        ImageLecture.saveBuffer (new File(currmap.img_adr), imageFile, currmap);

        if (imageFile.exists()) {
            ConsoleOut.outError("test", "le fichier a été créé.");
        }

        //On récupère la dimension active de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* On calcule les dimensions de l'image à afficher
              => pour l'instant : 10*width, 10*height      */
        int width = currmap.getDim()[0]*10;
        int height = currmap.getDim()[1]*10;

        //On lit l'image et on la redimensionne
        Image imgbuff = ImageIO.read(imageFile);
        imgbuff = imgbuff.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);

        //On crée un objet icone à partir de l'objet image et on l'intègre à la fenêtre
        ImageIcon image = new ImageIcon(imgbuff);
        JLabel emptyLabel = new JLabel(image);
        emptyLabel.setPreferredSize(new Dimension(width, height));

        frame.getContentPane().add(emptyLabel);

        int x = (screenSize.width - frame.getSize().width)/2;
        int y = (screenSize.height - frame.getSize().height)/2;

        frame.setLocation(x, y);

        frame.pack();
        frame.setVisible(true);

    }


    public static void createGUI() {
        frame = new JFrame(" ");

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
