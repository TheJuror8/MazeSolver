/*
* Note : https://www.dyclassroom.com/image-processing-project/how-to-get-and-set-pixel-value-in-java
*   WOW : http://www.astrolog.org/labyrnth/daedalus.htm
*/
package mazesolver;

import java.io.*;
import java.util.*;
import scala.tools.jline.*;
import scala.tools.jline.console.ConsoleReader;
import scala.tools.jline.console.completer.Completer;


/**
*
* @author habib
*/
public class MazeSolver {
  public static String VERS = "0.3";
  public static MazeMap current;
  public static ConsoleReader xterm;

  /**
  * @param args the command line arguments
  */
  public static void main(String[] args) {
    /* Chaîne pour l'input principal */
    String intxt;

    /* Création des compléteurs jline */
    final ArrayList<Completer> autocmp_list = ConsoleOut.getCompleter();

    /* Gestion du prompt, de l'historique et de l'auto-complétion avec jline */
    try {
      xterm = new ConsoleReader();

      /* Ajout de tous les completers à jline */
      for (Completer cmp : autocmp_list) {
        xterm.addCompleter(cmp);
      }

      /* Définition du prompt */
      xterm.setPrompt(" :msolv> ");

      /* Affichage de l'écran console d'accueuil */
      ConsoleOut.ShowLoader();

      while ((intxt = xterm.readLine()) != null) {
        SyntaxHandler.instrRead (intxt);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    /* Réactivation de l'écho console */
    ConsoleOut.enableEcho();
  }

}
