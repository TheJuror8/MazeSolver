/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.io.*;

/**
 *
 * @author habib
 */
public class SyntaxHandler {
    public static void instrRead (String instr) {
      //On découpe la chaîne d'instruction par un espace
      String[] split = instr.split("\\s+");

      syntaxcheck:
      for (int i = 0; i<split.length; i++) {
        switch (split[i]) {
          /* Misc : */
          case ".help":
            ConsoleOut.outHelp();
            break syntaxcheck;

          case ".about":
            ConsoleOut.outVersion();
            break syntaxcheck;

          case ".clear":
            ConsoleOut.flushScreen();
            break syntaxcheck;

          case ".exit":
            ConsoleOut.enableEcho();
            System.exit(0);
            break;

          /* Commandes principales */
          case "load":
            String fpath;

            //On modifie le prompt pour signaler à l'utilisateur d'entrer un nom de fichier
            MazeSolver.xterm.setPrompt(" :msolv>load>> ");

            //On lit la ligne suivante pour récupérer le nom de fichier
            try {
              fpath = MazeSolver.xterm.readLine();
            } catch (IOException e) {
              e.printStackTrace();
              break syntaxcheck;
            }

            fpath = fpath.trim(); //on nettoie les espaces dûs à l'auto-complétion

            File imgsrc = new File (fpath);

            if (imgsrc.exists() &&  !imgsrc.isDirectory()) {
              try {
                MazeSolver.current = ImageLecture.chargerImage(imgsrc);
                ConsoleOut.outNotice ("load", "le fichier "+ConsoleOut.outTxtFile(fpath)+" a été chargé et parsé avec succès.");
              } catch (Exception e) {
                ConsoleOut.outError ("load", "mauvais format de fichier, impossible de lire l'image fournie en paramètre.\n Exception : "+e.getMessage());
              }
            }
            else {
              ConsoleOut.outError ("load", "le fichier "+ConsoleOut.outTxtFile(fpath)+" n'est pas accessible en lecture.");
            }
            // On reset le prompt
            MazeSolver.xterm.setPrompt(" :msolv> ");
            break syntaxcheck; //Les paramètres supplémentaires sont ignorés

          case "save" :
            if (MazeSolver.current != null) {
              String fout;

              //On modifie le prompt pour signaler à l'utilisateur d'entrer un nom de fichier
              MazeSolver.xterm.setPrompt(" :msolv>save>> ");

              //On lit la ligne suivante pour récupérer le nom de fichier
              try {
                fout = MazeSolver.xterm.readLine();
              } catch (IOException e) {
                e.printStackTrace();
                break syntaxcheck;
              }

              fout = fout.trim(); //on nettoie les espaces dûs à l'auto-complétion

              File imgin = new File (MazeSolver.current.img_adr);
              File imgout = new File (fout);

              if (imgin.exists() &&  !imgin.isDirectory()) {
                try {
                  ImageLecture.saveBuffer (imgin, imgout, MazeSolver.current);
                  ConsoleOut.outNotice ("save", "le buffer actif a été enregistré dans le fichier "+ConsoleOut.outTxtFile(fout)+" avec succès !");
                } catch (IOException e) {
                  ConsoleOut.outError ("save", "impossible d'enregistrer le fichier à l'adresse spécifiée, vérifiez que vous disposez des autorisations en écriture pour ce répertoire.");
                }
              }
              else {
                ConsoleOut.outError ("save", "l'image précédemment chargée en buffer n'est plus lisible.");
              }
              // On reset le prompt
              MazeSolver.xterm.setPrompt(" :msolv> ");
            } else {
                ConsoleOut.outError ("save", "aucun labyrinthe n'a été chargé en mémoire.");
            }
            break syntaxcheck; //Les paramètres supplémentaires sont ignorés

          case "solve":
            if (MazeSolver.current != null) {
              if (MazeSolver.current.getSE() != null) {
                if (i!=split.length-1) {
                  switch (split[i+1]) {
                    case "--a*":
                      ConsoleOut.outNotice("solve::A*", MazeSolver.current.solveAStar() ? "solution trouvée !" : "aucune solution trouvée");
                      break;
                    // more to come...
                    default:
                      ConsoleOut.outError ("solve", "mode de résolution « "+split[i+1]+" » non reconnu.");
                      break;
                  }
                } else {
                  ConsoleOut.outError ("solve", "aucun mode de résolution n'a été spécifié pour la résolution. Saisir "+ConsoleOut.BLUE+".help"+ConsoleOut.CLOSE+" pour plus d'infos");
                }
              } else {
                ConsoleOut.outError ("solve", "les nodes de départ et d'arrivée n'ont pas été définies pour ce labyrinthe. ");
              }
            } else {
              ConsoleOut.outError ("solve", "aucun labyrinthe n'a été chargé en mémoire.");
            }
            break syntaxcheck;

          case "display":
            if (MazeSolver.current != null) {
              ConsoleOut.afficheMap (MazeSolver.current);
              if (i!=split.length-1) {
                ConsoleOut.outNotice ("display", "paramètres supplémentaires « "+split[i+1]+" » ignorés.");
              }
            } else {
              ConsoleOut.outError ("display", "aucun labyrinthe n'est chargé en mémoire.");
            }
            break syntaxcheck;

          case "setpath":
            if (MazeSolver.current != null) {
              if (i!=split.length-1) {
                //On découpe la chaîne d'instruction par (:) et (,)
                String[] coord = split[i+1].split("(:)|(,)");

                //L'expression régulière ici vérifie que les coordonnées ont été données dans le bon format
                //   => http://regexr.com/ = goldmine
                if ((coord.length < 4) || !split[i+1].matches("(\\d+):(\\d+),(\\d+):(\\d+)"))  {
                  ConsoleOut.outError ("setpath", "erreur de syntaxe : « "+split[i+1]+" » ne correspond pas aux coordonnées de deux points.");
                } else {
                  /* On récupère les coordonnées et on vérifie qu'elles rentrent dans le MazeMap */
                  boolean inbound = true;
                  int[] strtend = new int[4];
                  int[] mapdim = MazeSolver.current.getDim();
                  int n = 0;

                  // => on convertit les chaînes en integer
                  // => on vérifie que les coordonnées rentrent dans la map
                  //   0 2 : X ? dim[0]-1, 1 3 : Y ? dim[1]-1
                  for (String val : coord) {
                    strtend[n] = Integer.valueOf(val);
                    if ((strtend[n]<0) || (((n%2 == 0)&&(strtend[n]>mapdim[0]-1)) || ((n%2 == 1)&&(strtend[n]>mapdim[1]-1)))) {
                      inbound = false;
                      break;
                    }
                    n++;
                  }

                  if (!inbound) {
                      ConsoleOut.outError ("setpath", "les coordonnées données sortent de l'image.");
                  } else if ((MazeSolver.current.getCase(strtend[0], strtend[1]).getType() == NodeType.OBSTACLE) ||
                                    (MazeSolver.current.getCase(strtend[2], strtend[3]).getType() == NodeType.OBSTACLE)) {
                      ConsoleOut.outError ("setpath", "les coordonnées données sont celles de cases parsées comme obstacles.");
                  } else {
                      MazeSolver.current.setSE(new int[] {strtend[0], strtend[1]}, new int[] {strtend[2], strtend[3]});
                      ConsoleOut.outNotice ("setpath", "coordonnées de départ et d'arrivée définies avec succès.");
                  }
                }
              } else {
                ConsoleOut.outError ("setpath", "aucune coordonnée n'a été saisie. Saisir "+ConsoleOut.BLUE+".help"+ConsoleOut.CLOSE+" pour plus d'infos");
              }
            } else {
              ConsoleOut.outError ("setpath", "aucun labyrinthe n'est chargé en mémoire.");
            }
            break syntaxcheck;

          default:
            ConsoleOut.outError ("syntax", "erreur de syntaxe, aucune commande ne correspond à : « "+split[i]+" ».");
            break syntaxcheck;
        }
      }
    }
}
