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
    //Variables utiles à la résolution, à déplacer en local après
    private ArrayList<Node> openlist;
    private ArrayList<Node> closedlist;

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
}
