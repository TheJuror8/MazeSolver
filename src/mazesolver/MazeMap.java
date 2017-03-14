/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

import java.util.*;

/**
 *
 * @author slimh
 */
public class MazeMap {
    //Tableau 2D contenant toutes les nodes
    private Node[][] map;
    private int[] start, end; //positions de départ et d'arrivée

    //Dimensions de la map
    private final int[] dim = new int[2];

    //Nom du fichier associé
    public String img_adr;

    MazeMap (int hauteur, int largeur, String img_adr) {
        this.dim[0] = largeur;
        this.dim[1] = hauteur;
        this.map = new Node[hauteur][largeur];
        this.img_adr = img_adr;

        //On intialise le tableau map avec des nodes vides
        for (int i = 0; i<hauteur; i++){
          for (int j = 0; j<largeur; j++){
            this.setCase (new Node(), j, i);
          }
        }
    }

    public void setCase(Node obj, int x, int y) {
        this.map[y][x] = obj;
    }

    /* Définit les coordonnées des cases de départ et d'arrivée = {x,y}
       Calcule les poids heuristiques de chaque case en conséquence */
    public void setSE (int[] start, int [] end) {
        this.start = start;
        this.end = end;

        //Calcul du poids heuristique de chaque case
        int hcout;
        int[] end_c = new int[] {getSE()[2], getSE()[3]};

        for (int y = 0; y<getDim()[1]; y++){ //hauteur
          for (int x = 0; x<getDim()[0]; x++){ //largeur
            hcout = Math.abs(strt[0] - x) + Math.abs(strt[1] - y);
            this.getCase(x,y).setHCout (hcout);
          }
        }
    }

    /* Renvoie les coordonnées des cases départ et arrivée */
    public int[] getSE () {
      if (this.start != null)
        return (new int[] {this.start[0], this.start[1], this.end[0], this.end[1]});
      else return null;
    }

    public Node getCase (int x, int y) {
        return this.map[y][x];
    }

    public int[] getDim() {
        return this.dim;
    }

    /* Méthode A* de résolution du labyrinthe */
    public boolean solveAStar () {
      //Liste ouverte et liste fermée
      private ArrayList<Node> openlist;
      private ArrayList<Node> closedlist;

      /* Note : ORDONNER LES LISTES PAR POIDS OPTIMISE L'ALGO*/

      //On récupère les objets Node associés à la case de départ et d'arrivée
      Node start_node = this.getCase (getSE()[0], getSE[1]);
      Node end_node = this.getCase (getSE()[2], getSE[3]);

      /* La case active est la case de départ
         => variable inutile en soit, question de clarté du code */
      Node active_node = start_node;

      //On ajoute la case de départ à la liste fermée et on commence une boucle
      closedList.add (start_node);

      /* Si la case d'arrivée est ajoutée à la liste fermée
         ou la liste ouverte est vide on sort de la boucle */
      do {
        // On ajoute les cases adjacentes qui ne sont pas dans la liste fermée
        //    => interdire les mouvements diagonaux


      } while (!openList.isEmpty() && !closedList.contains(end_node))



    }



}
