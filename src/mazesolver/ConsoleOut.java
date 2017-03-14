/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import scala.tools.jline.TerminalFactory;
import scala.tools.jline.UnixTerminal;

/**
 *
 * @author habib
 */
public class ConsoleOut {
    //Déclaration des balises de style ANSI
    private static final String CLOSE = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    /* => codes couleur pour un fond de texte */
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String WHITE_BG	= "\u001B[47m";

    //Affichage de l'écran d'accueil
    public static void ShowLoader() {
      //Nettoyage de l'écran
      flushScreen();

      //Logo WScout
      String logo = GREEN
        +"███╗   ███╗ █████╗ ███████╗███████╗███████╗ ██████╗ ██╗    ██╗   ██╗███████╗██████╗\n"
        +"████╗ ████║██╔══██╗╚══███╔╝██╔════╝██╔════╝██╔═══██╗██║    ██║   ██║██╔════╝██╔══██╗\n"
        +"██╔████╔██║███████║  ███╔╝ █████╗  ███████╗██║   ██║██║    ██║   ██║█████╗  ██████╔╝\n"
        +"██║╚██╔╝██║██╔══██║ ███╔╝  ██╔══╝  ╚════██║██║   ██║██║    ╚██╗ ██╔╝██╔══╝  ██╔══██╗\n"
        +"██║ ╚═╝ ██║██║  ██║███████╗███████╗███████║╚██████╔╝███████╗╚████╔╝ ███████╗██║  ██║\n"
        +"╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚══════╝ ╚═══╝  ╚══════╝╚═╝  ╚═╝\n"
                                                                                    +CLOSE;

      logo = returnCentredTxt(logo, 84, 7, 1);

      //On reset l'état de la console
      resetConsole();

      //On affiche le résultat en ConsoleOut
      System.out.print (logo);
      System.out.println (" MazeSolver version "+ MazeSolver.VERS);
      System.out.println (" Entrez /h pour obtenir de l'aide");
    }

    //Reset de la console
    public static void resetConsole() {
        try {
            UnixTerminal console_pointer = new UnixTerminal();
            console_pointer.restore();
        } catch (Exception e) {
            outError ("Erreur lors du reset de la console.");
        }
    }

    public static void outError (String msg) {
      System.out.println (" "+RED_BG+"ERREUR :"+CLOSE+" "+msg);
    }

    public static void afficheMap (MazeMap mapcp) {
      // Copie pour ne pas modifier mapcp dans le prg principal
      MazeMap map = mapcp;
      int[] dim = map.getDim();
      int type;
      String map2string = "";

      // On remplace les cases départ et arrivée par des types "-2
      map.getCase()

      System.out.println (" "+GREEN_BG+"RENDU :"+CLOSE+" "+outTxtFile(map.img_adr));

      for (int y = 0; y<dim[1]; y++){ //hauteur
        for (int x = 0; x<dim[0]; x++){ //largeur
          type = map.getCase(x, y).getType();

          if (type==-1)
              map2string += RED+"88"+CLOSE;
          else if (type==0)
              map2string += "::";
          else
              map2string += "██";
        }
        map2string+="\n";
      }

      map2string = returnCentredTxt (map2string, dim[0]*2, dim[1], 0);
      System.out.print (map2string);
    }

    public static String outTxtFile (String filen) {
      return (WHITE_BG+BLACK+"{"+filen+"}"+CLOSE);
    }


    //Méthodes internes à la classse
    //==================================

    //Renvoie les dimensions de la console
    private static int[] getConsoleDim () {
      int[] dim = new int[2];
      dim[0] = TerminalFactory.get().getWidth();
      dim[1] = TerminalFactory.get().getHeight();

      //On réactive l'echo console
      enableEcho();
      return dim;
    }

    //Activation de l'écho
    private static void enableEcho () {
        try {
            UnixTerminal console = new UnixTerminal();
            console.setEchoEnabled(true);
        } catch (Exception e) {
            outError ("Erreur lors de l'activation de l'echo.");
        }
    }

    //Nettoyage de la console
    private static void flushScreen () {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /* Retourne une version centrée dans la console d'une chaîne de caractères.
          => optvertical = 1 : centrée horizontalement ET verticalement
          => optvertical = 0 : centrée horizontalement uniquement                */
    private static String returnCentredTxt (String txt, int width, int height, int optvertical) {
      //Récupère la dimension de la console
      int[] dim = getConsoleDim();

      //Calcul de l'indentation et du saut de lignes
      int indent = (dim[0] - width) / 2;
      int newlines = optvertical==0 ? 3 : ((dim[1] - height) / 2);

      //Si la fenêtre est trop petite => valeurs par défaut
      if (indent < 0) {
        indent = 35;
      }
      if (newlines < 0) {
        newlines = 10;
      }

      //On crée les chaînes correspondantes
      String s_indent = new String(new char[indent]).replace("\0", " ");
      String s_newlines = new String(new char[newlines]).replace("\0", "\n");

      //On remplace chaque début de ligne par l'indentation
      txt = txt.replaceAll("(?m)^", s_indent);

      //On intercale le début et la fin de la chaîne avec les sauts de lignes
      txt = s_newlines + txt + s_newlines;

      return txt;
    }
}
