/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author habib
 */
public class MazeSolver {
    public static String VERS = "0.1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //fichier à ouvrir en dur pour l'instant
        String img_adr = "maze_img/maze_01.bmp", hex;
        Scanner input = new Scanner(System.in);
        MazeMap result;

        //Affichage console
        ConsoleOut.ShowLoader();

        String klek;

        //Traitements à gérer dans un analyseur de prompt
        do {
            System.out.print ("::msolv=> ");
            klek = input.nextLine();
        } while (!klek.equals("load"));

        try {
            result = ImageLecture.chargerImage(img_adr);
            ConsoleOut.afficheMap (result);

            //Traitements à gérer dans un analyseur de prompt
            do {
                System.out.print ("::msolv=> ");
                klek = input.nextLine();
            } while (!klek.equals("setstart"));

            //On définit la case départ et la case d'arrivée selon l'input de l'utilisateur (en dur pour l'instant)
            //  => on affiche la map après définition
            result.setSE(new int[] {1,1}, new int[] {25,28});
            ConsoleOut.afficheMap (result);

            //Traitements à gérer dans un analyseur de prompt
            do {
                System.out.print ("::msolv=> ");
                klek = input.nextLine();
            } while (!klek.equals("solve"));

            //On résout le labyrinthe avec l'algorithme A*
            ConsoleOut.outNotice(result.solveAStar() ? "Solution trouvée !" : "Aucune solution trouvée");
            ConsoleOut.afficheMap (result);

        } catch (IOException e) {
            ConsoleOut.outError ("Le fichier "+ConsoleOut.outTxtFile(img_adr)+" n'est pas disponible en lecture.");
        }
    }
}
