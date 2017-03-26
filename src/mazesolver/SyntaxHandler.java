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

      switch (split[0]) {
        /* Misc : */
        case ".help":
          ConsoleOut.outHelp();
          break;

        case ".about":
          ConsoleOut.outVersion();
          break;

        case ".clear":
          ConsoleOut.flushScreen();
          break;

        case ".exit":
          ConsoleOut.enableEcho();
          System.exit(0);
          break;

        /* Instructions principales */
        case "load":
          String fpath;

          //On modifie le prompt pour signaler à l'utilisateur d'entrer un nom de fichier
          MazeSolver.xterm.setPrompt(" :msolv>load>> ");

          //On lit la ligne suivante pour récupérer le nom de fichier
          try {
            fpath = MazeSolver.xterm.readLine();
          } catch (IOException e) {
            e.printStackTrace();
            break;
          }

          fpath = fpath.trim(); //on nettoie les espaces dûs à l'auto-complétion

          File imgsrc = new File (fpath);

          if (imgsrc.exists() &&  !imgsrc.isDirectory()) {
            try {
              /* On parse le labyrinthe donné en paramètre */
              MazeSolver.current = ImageLecture.chargerImage(imgsrc);

              /* Création préemptive de la GUI pour le JFrame
                  si la précédente a été détruite ou n'a jamais été créée */
              if (!ImagePop.isDisplayable()) {
                ImagePop.createGUI(fpath);
              }

              /* Si le popup est affiché à l'écran alors le contenu est affiché */
              if (ImagePop.isVisible()) {
                loadImg();
              }

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
          break; //Les paramètres supplémentaires sont ignorés

        case "imgpop":
          if (MazeSolver.current != null) {
            /* On calcule le ratio par défaut */
            int ratio = ImagePop.getDefaultRatio(MazeSolver.current.getDim());
            int n_ratio;

            /* On récupère le paramètre de ratio s'il y en a un */
            if (split.length>1) {
              try {
                n_ratio = Integer.valueOf(split[1]);
              } catch (NumberFormatException e) {
                  ConsoleOut.outError("imgpop", "impossible d'interpréter le ratio passé en paramètres. \n Chaîne : "+split[1]);
                  break;
              }
              if (n_ratio<1) {
                ConsoleOut.outError ("imgpop", "ratio entré négatif. Veuillez entrer une valeur positive, supérieure à 1.");
                break;
              /* Si le ratio va créer une fenêtre trop grande on le plafonne à la valeur par défaut. */
            } else if (ImagePop.ratio2Big(MazeSolver.current.getDim(), n_ratio)) {
                /* Si l'instance ImagePop est déjà au ratio par défaut, on ne fait rien */
                if (ImagePop.getRatio()!=ratio) {
                    n_ratio = ratio;
                    ConsoleOut.outWarning ("imgpop", "ratio entré trop grand, ratio par défaut défini à "+ratio+":1");
                } else {
                    ConsoleOut.outError ("imgpop", "ratio entré trop grand.");
                    break;
                }
              }
            } else {
                n_ratio = ratio;
                ConsoleOut.outWarning ("imgpop", "ratio par défaut : "+ratio+":1");
            }

            ratio = n_ratio;

            /* Si la GUI a été détruite, alors elle est recréée */
            try {
              if (!ImagePop.isDisplayable()) {
                ImagePop.createGUI(MazeSolver.current.img_adr);
                ImagePop.imgPop (MazeSolver.current, ratio);
                ConsoleOut.outNotice ("imgpop", "fenêtre de rendu créée avec succès. ratio : "+ratio+":1");
              } else if (ImagePop.getRatio()!=ratio) {
                ImagePop.setBuffer(MazeSolver.current, ratio);
                ConsoleOut.outNotice ("imgpop", "ratio du rendu défini à "+ratio+":1");
              } else {
                ConsoleOut.outError ("imgpop", "le rendu fenêtré a déjà été créé pour ce ratio.");
              }
            } catch (Exception e) {
              ConsoleOut.outError("imgpop", "impossible de charger l'image source.\n Exception : "+e.getMessage());
            }
          } else {
              ConsoleOut.outError ("imgpop", "le buffer actif est vide.");
          }

          break;

        case "initmap":
          if (MazeSolver.current != null) {
            if (MazeSolver.current.getSE()!= null) {
              MazeSolver.current.clearMaze();
              refreshImg("initmap");
              ConsoleOut.outError ("initmap", "contenu du labyrinthe vidé.");
            } else {
                ConsoleOut.outError ("initmap", "aucun chemin ou case de départ ou d'arrivée n'ont été définis.");
            }
          } else {
            ConsoleOut.outError ("initmap", "aucun labyrinthe n'a été chargé en mémoire.");
          }

          break;

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
              break;
            }

            fout = fout.trim(); //on nettoie les espaces dûs à l'auto-complétion

            File imgout = new File (fout);

            if (new File(MazeSolver.current.img_adr).exists()) {
              try {
                ImageLecture.saveBuffer (MazeSolver.current, imgout);
                ConsoleOut.outNotice ("save", "le buffer actif a été enregistré dans le fichier "+ConsoleOut.outTxtFile(fout)+" avec succès !");
              } catch (IOException e) {
                ConsoleOut.outError ("save", "impossible d'enregistrer le fichier à l'adresse spécifiée, vérifiez que le fichier source du labyrinthe n'a pas été modifié.");
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
          break; //Les paramètres supplémentaires sont ignorés

        case "solve":
          if (MazeSolver.current != null) {
            if (MazeSolver.current.getSE() != null) {
              if (split.length>1) {
                switch (split[1]) {
                  case "-a*":
                    ConsoleOut.outNotice("solve::A*", MazeSolver.current.solveAStar() ? "solution trouvée !" : "aucune solution trouvée");
                    break;
                  // more to come...
                  default:
                    ConsoleOut.outError ("solve", "mode de résolution « "+split[1]+" » non reconnu.");
                    break;
                }
                refreshImg("solve");
              } else {
                ConsoleOut.outError ("solve", "aucun mode de résolution n'a été spécifié. Saisir "+ConsoleOut.BLUE+".help"+ConsoleOut.CLOSE+" pour plus d'infos");
              }
            } else {
              ConsoleOut.outError ("solve", "les nodes de départ et d'arrivée n'ont pas été définies pour ce labyrinthe. ");
            }
          } else {
            ConsoleOut.outError ("solve", "aucun labyrinthe n'a été chargé en mémoire.");
          }
          break;

        case "display":
          if (MazeSolver.current != null) {
            /* Si le labyrinthe ne tient pas en largeur dans la console, l'affichage n'est pas effectué */
            if (MazeSolver.current.getDim()[0]*2<ConsoleOut.getConsoleDim()[0]) {
              ConsoleOut.afficheMap (MazeSolver.current);
              refreshImg("display");
            } else {
              ConsoleOut.outError ("display", "le labyrinthe chargé est trop large pour être affiché en console.\n Préférez imgpop [ratio] pour des labyrinthes de cette dimension");
            }
            if (split.length>1) {
              ConsoleOut.outWarning ("display", "paramètres supplémentaires « "+split[1]+" » ignorés.");
            }
          } else {
            ConsoleOut.outError ("display", "aucun labyrinthe n'est chargé en mémoire.");
          }
          break;

        case "setpath":
          if (MazeSolver.current != null) {
            if (split.length>1) {
              //On découpe la chaîne d'instruction par (:) et (,)
              String[] coord = split[1].split("(:)|(,)");

              //L'expression régulière ici vérifie que les coordonnées ont été données dans le bon format
              //   => http://regexr.com/ = goldmine
              if ((coord.length < 4) || !split[1].matches("(\\d+):(\\d+),(\\d+):(\\d+)"))  {
                ConsoleOut.outError ("setpath", "erreur de syntaxe : « "+split[1]+" » ne correspond pas aux coordonnées de deux points.");
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
                    refreshImg("setpath");
                    ConsoleOut.outNotice ("setpath", "coordonnées de départ et d'arrivée définies avec succès.");
                }
              }
            } else {
              ConsoleOut.outError ("setpath", "aucune coordonnée n'a été saisie. Saisir "+ConsoleOut.BLUE+".help"+ConsoleOut.CLOSE+" pour plus d'infos");
            }
          } else {
            ConsoleOut.outError ("setpath", "aucun labyrinthe n'est chargé en mémoire.");
          }
          break;

        case "autopath":
          /* Définit automatiquement les cases de départ et de fin
              => pour les labyrinthes générés sous Daedalus         */
          if (MazeSolver.current != null) {
              int[] dim = MazeSolver.current.getDim();

              //positions de départ et d'arrivée
              int[] start = new int[] {-1, -1};
              int[] end = new int[] {-1, -1};;

              /* On balaye horizontalement les première et dernière lignes
                        de la map pour récupérer les points d'entrée     */
              for (int i = 0; i < dim[0]; i++) {
                if (MazeSolver.current.getCase(i,0).getType()==NodeType.EMPTY) {
                  start = new int[] {i, 0};
                  break;
                }
              }

              for (int j = 0; j < dim[0]; j++) {
                if (MazeSolver.current.getCase(j,dim[1]-1).getType()==NodeType.EMPTY) {
                  end = new int[] {j, dim[1]-1};
                  break;
                }
              }

              if (start[0] == -1 || end[0] == -1) {
                ConsoleOut.outError ("autopath", "le labyrinthe passé en paramètres ne possède pas de points d'entrée.");
              } else {
                MazeSolver.current.setSE(start, end);
                refreshImg("autopath");
                ConsoleOut.outNotice ("autopath", "points d'entrées ajoutés avec succès. coordonnées : S("+start[0]+":"+start[1]+"), E("+end[0]+":"+end[1]+") .");
              }

          } else {
              ConsoleOut.outError ("autopath", "aucun labyrinthe n'est chargé en mémoire.");
          }

          break;

        default:
          if (split[0].length()>0) {
              ConsoleOut.outError ("syntax", "erreur de syntaxe, aucune commande ne correspond à : « "+split[0]+" ».");
          }
          break;
      }
    }

    /* Affiche une image pour la première fois */
    private static void loadImg() {
      if (ImagePop.isDisplayable()) {
        try {
            ImagePop.setBuffer(MazeSolver.current, ImagePop.getDefaultRatio(MazeSolver.current.getDim()));
        } catch (Exception e) {
            ConsoleOut.outError ("load", "impossible d'afficher le labyrinthe chargé.\n Exception : "+e.getMessage());
        }
      }
    }

    /* Rafraîchit le rendu image */
    private static void refreshImg(String instr) {
      if (ImagePop.isDisplayable()) {
        try {
            ImagePop.setBuffer();
        } catch (Exception e) {
            ConsoleOut.outError (instr, "erreur lors de la tentative d'actualisation du rendu fenêtré.");
        }
      }
    }
}
