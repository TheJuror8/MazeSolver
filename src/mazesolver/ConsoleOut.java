/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.util.*;
import scala.tools.jline.TerminalFactory;
import scala.tools.jline.UnixTerminal;
import scala.tools.jline.console.completer.FileNameCompleter;
import scala.tools.jline.console.completer.StringsCompleter;
import scala.tools.jline.console.completer.Completer;

/**
 *
 * @author habib
 */
public class ConsoleOut {
    /* Déclaration des balises de style ANSI */
    public static final String CLOSE = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[92m";
    public static final String YELLOW = "\u001B[93m";
    public static final String BLUE = "\u001B[94m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    /* Autres styles de texte */
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String WHITE_BG	= "\u001B[47m";
    public static final String UNDERLINE	= "\u001b[4m";
    public static final String BOLD	= "\u001b[1m";

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

      logo = returnCentredTxt(logo, 85, 7, 1);

      //On affiche le résultat en ConsoleOut
      System.out.print (logo);
      System.out.println (" MazeSolver - Terminal based maze solver");
      System.out.println (" Entrez "+GREEN+".help"+CLOSE+" pour obtenir de l'aide");
    }

    public static ArrayList<Completer> getCompleter() {
      ArrayList<Completer> autocmp_list = new ArrayList();
      autocmp_list.addAll(Arrays.asList(
        new StringsCompleter("load"),
        new StringsCompleter("display"),
        new StringsCompleter("save >>"),
        new StringsCompleter("solve --"),
        new StringsCompleter("setpath"),
        new StringsCompleter(".help"),
        new StringsCompleter(".exit"),
        new StringsCompleter(".about"),
        new StringsCompleter(".exit"),
        new StringsCompleter(".clear"),
        new FileNameCompleter()));

      return autocmp_list;
    }

    public static void afficheMap (MazeMap map) {
      /* Diverses variables de traitement */
      int[] dim = map.getDim();
      int[] dep_arr;

      NodeType type;
      String map2string = "";
      boolean notSignaled = true;

      System.out.println (" "+GREEN_BG+"RENDU :"+CLOSE+" "+outTxtFile(map.img_adr));

      for (int y = 0; y<dim[1]; y++){ //hauteur
        for (int x = 0; x<dim[0]; x++){ //largeur
          type = map.getCase(x, y).getType();

          switch (type) {
            case NULL:
              map2string += RED+"88"+CLOSE;
              if (notSignaled) {
                outError ("display", "une ou plusieurs nodes indéfinies ont été trouvées : le parsing de l'image comporte des erreurs.");
                notSignaled = false;
              }
              break;
            case EMPTY:
              map2string += "::"; break;
            case START:
              map2string += GREEN_BG+"::"+CLOSE; break;
            case END:
              map2string += RED_BG+"::"+CLOSE; break;
            case OBSTACLE:
              map2string += "██"; break;
            case PATH:
              map2string += YELLOW_BG+"::"+CLOSE; break;
          }

        }
        map2string+="\n";
      }

      map2string = returnCentredTxt (map2string, dim[0]*2, dim[1], 0);
      System.out.print (map2string);
    }

    public static String outTxtFile (String filen) {
      return (WHITE_BG+BLACK+"{"+filen+"}"+CLOSE);
    }

    public static void outNotice (String txt) {
      System.out.println (" "+YELLOW_BG+"NOTICE :"+CLOSE+" "+txt);
    }

    public static void outNotice (String cmd, String msg) {
      System.out.println (" "+YELLOW_BG+"<<"+cmd+" :"+CLOSE+" "+msg);
    }

    public static void outError (String cmd, String msg) {
      System.out.println (" "+RED_BG+"<<"+cmd+" :"+CLOSE+" "+msg);
    }

    public static void outVersion() {
      //on a le droit de rêver
      System.out.println ("\n     "+GREEN+"GitHub Project - "+BLUE+"http://github.com/TheJuror8/MazeSolver"+CLOSE
                          +GREEN+"\n     MazeSolver Copyright (C) 2017 - Habib Slim\n"+CLOSE);
    }

    public static void outHelp () {
      String helptxt = String.join ("\n", "\n"
      , "     "+YELLOW+"load <file>"+CLOSE+"     : Permet de charger un labyrinthe BMP/JPG en mémoire"
      , "     "+YELLOW+"display"+CLOSE+"         : Affiche en console le dernier labyrinthe chargé"
      , "     "+YELLOW+"solve --alg"+CLOSE+"     : Résout le labyrinthe avec la méthode spécifiée en paramètre"
      , "                       => algorithmes implémentés : --a*, TODO"
      , "     "+YELLOW+"setpath a:b,c:d"+CLOSE+" : Définit les points de départ et d'arrivée du labyrinthe"
      , "                       => exemple : 1:1,20:20"
      , "     "+  GREEN+".help"+CLOSE+"           : Affiche cette fenêtre d'aide"
      , "     "+  GREEN+".about"+CLOSE+"          : Affiche des infos complémentaires sur le logiciel"
      , "     "+  GREEN+".exit"+CLOSE+"           : Ferme MazeSolver"
      , "     "+  GREEN+".clear"+CLOSE+"          : Vide le terminal\n\n");
      System.out.println (helptxt);
    }

    //Nettoyage de la console
    public static void flushScreen () {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //Activation de l'écho console
    public static void enableEcho () {
        try {
            UnixTerminal console = new UnixTerminal();
            console.setEchoEnabled(true);
        } catch (Exception e) {
            outError ("global", "Erreur lors de l'activation de l'echo console.");
        }
    }

    //Méthodes internes à la classse
    //==================================

    //Renvoie les dimensions de la console
    private static int[] getConsoleDim () {
      int[] dim = new int[2];
      dim[0] = TerminalFactory.get().getWidth();
      dim[1] = TerminalFactory.get().getHeight();

      //On réactive l'echo console
      //enableEcho();
      return dim;
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
