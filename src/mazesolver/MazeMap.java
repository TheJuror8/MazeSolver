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

    /* Setters
    =============================*/

    public void setCase(Node obj, int x, int y) {
        this.map[y][x] = obj;
    }

    /* Définit les coordonnées des cases de départ et d'arrivée = {x,y}
       Calcule les poids heuristiques de chaque case en conséquence */
    public void setSE (int[] start, int [] end) {
        this.start = start;
        this.end = end;

        //Calcul du poids heuristique (H) de chaque case
        int hcout;
        int[] end_c = new int[] {getSE()[2], getSE()[3]};

        for (int y = 0; y<getDim()[1]; y++){ //hauteur
          for (int x = 0; x<getDim()[0]; x++){ //largeur
            hcout = Math.abs(end_c[0] - x) + Math.abs(end_c[1] - y);
            this.getCase(x,y).setHCout (hcout);
          }
        }

        // On remplace les cases départ et arrivée par des type départ et arrivée
        this.getCase(getSE()[0], getSE()[1]).setType(NodeType.START); // case départ
        this.getCase(getSE()[2], getSE()[3]).setType(NodeType.END); // case arrivée
    }

    /* Getters
    =============================*/

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


    /* Autres méthodes publiques
    =============================*/

    /* Méthode A* de résolution du labyrinthe */
    public boolean solveAStar () {
      //Liste ouverte et liste fermée
      ArrayList<Node> openlist = new ArrayList();
      ArrayList<Node> closedlist = new ArrayList();

      /* Array utile pour donner les coordonnées des cases adjacentes
        verticalement et horizontalement */
      int[][] dep = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

      int[] stend = getSE();

      /* On récupère les objets Node associés à la case de départ et d'arrivée
         => la case active est la case de départ */
      Node active_node = this.getCase (stend[0], stend[1]);
      Node end_node = this.getCase (stend[2], stend[3]);

      Node adj_node;

      //On attribue un poids G de zéro, et ses coordonnées, à la case départ
      active_node.setGCout(0);
      active_node.coord = new int[] {stend[0], stend[1]};

      //On ajoute la case de départ à la liste fermée et on commence une boucle
      closedlist.add (active_node);

      /* Si la case d'arrivée est ajoutée à la liste fermée
         ou la liste ouverte est vide on sort de la boucle */
      do {
        // On ajoute les cases adjacentes à la case active
        // qui ne sont pas dans la liste fermée et qui ne sont pas obstacles
        //    => interdire les mouvements diagonaux
        for (int[] i : dep) {
          adj_node = this.getCase (active_node.coord[0]+i[0], active_node.coord[1]+i[1]);
          adj_node.coord = new int[] {active_node.coord[0]+i[0], active_node.coord[1]+i[1]};

          /* Si la case n'est ni dans la liste ouverte, ni dans la liste fermée
            et qu'elle n'est pas obstacle, alors : */
          if (!((closedlist.contains(adj_node) || openlist.contains(adj_node))
                                               || (adj_node.getType() == NodeType.OBSTACLE))) {

            adj_node.setGCout(10 + active_node.getGCout()); // déplacement vertical\horizontal additionné du cout en déplacement de la case active
            adj_node.setParent(active_node); //on définit comme parent pour cette node la case active
            openlist.add (adj_node); // on ajoute la case à la liste ouverte
          }
        }

        /* On cherche dans toute la liste ouverte une node adjacente ayant le poids minimum qui devient la case active
           => on l'enlève de la liste ouverte
           => on l'ajoute à la liste fermée */
        active_node = openlist.get (0); // On lit la première case de la liste ouverte pour comparaison

        for (int i = 1; i < openlist.size(); i++) {
          if (openlist.get(i).getFCout() < active_node.getFCout()) {
            active_node = openlist.get(i);
          }
        }

        openlist.remove(active_node);
        closedlist.add(active_node);


      } while (!(openlist.isEmpty() || closedlist.contains(end_node)));

      /* On reconstitue le chemin a partir des parents de la dernière case_active
           => on écrit dans les cases chemin le type PATH
           => la boucle s'arrête quand elle arrive à la case départ, qui est orpheline */
      // => pré-lecture pour ne pas écraser le type de la case d'arrivée
      active_node = active_node.getParent();

      while (active_node.getParent() != null) {
        active_node.setType(NodeType.PATH);
        active_node = active_node.getParent();
      }

      /* On retourne vrai si un chemin jusqu'à la case d'arrivée a été trouvée et faux sinon */
      return (closedlist.contains(end_node));
    }

}
