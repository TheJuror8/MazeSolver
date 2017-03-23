package mazesolver;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class justatest {
    
    private static JFrame frame;
    private static final String CACHE_F = "/temp/buff01.bmp";
    
    private static void imgPop (MazeMap currmap, JFrame frame) {
        try {
            //On copie et enregistre le buffer actif dans un fichier temporaire
            File imageFile = new File (CACHE_F);
            ImageLecture.saveBuffer (new File(currmap.img_adr), imageFile, currmap);

            //On récupère la dimension active de l'écran
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            Image imgbuff = ImageIO.read(imageFile);
            imgbuff = imgbuff.getScaledInstance(700, 700, java.awt.Image.SCALE_DEFAULT);
            ImageIcon image = new ImageIcon(imgbuff);
            JLabel emptyLabel = new JLabel(image);
            emptyLabel.setPreferredSize(new Dimension(700, 700));

            frame.getContentPane().add(emptyLabel);

            int x = (screenSize.width - frame.getSize().width)/2;
            int y = (screenSize.height - frame.getSize().height)/2;

            frame.setLocation(x, y);

            frame.pack();
            frame.setVisible(true);
            
        } catch (Exception e) {
            ConsoleOut.outError("imgpop", "impossible de charger l'image source. Exception : "+e.getMessage());
        }

    }
    
    
    private static void createGUI() {
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

    public static void main(String[] args) {
        createGUI();
        
        try {
            imgPop (MazeSolver.current, frame);
        } catch (Exception e) {
            ConsoleOut.outError("imgpop", "impossible de charger l'image source. Exception : "+e.getMessage());
        }
    }
}
