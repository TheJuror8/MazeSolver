/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

/**
 *
 * @author slimh
 */
public class Node {
    private NodeType type; //-1 si indéfini, 0 si vide, 1 si obstacle, 2 si chemin
    private int h_cout, g_cout;
    private Node parent;

    public int[] coord; //utilisé uniquement pour la résolution pour des raisons d'optimisation
    // => moyen de faire plus propre que ça

    Node() {
        this.type = NodeType.NULL;
    }

    Node(NodeType type) {
        this.type = type;
    }

    Node(NodeType type, Node parent) {
        this.type = type;
        this.parent = parent;
    }

    /* Setters
    =============================*/

    public void setType (NodeType type) {
        this.type = type;
    }

    public void setParent (Node parent) {
        this.parent = parent;
    }

    public void setHCout (int h_cout) {
        this.h_cout = h_cout;
    }

    public void setGCout (int g_cout) {
        this.g_cout = g_cout;
    }

    /* Getters
    =============================*/

    public int getGCout () {
        return this.g_cout;
    }

    public int getFCout () {
      return this.h_cout + this.g_cout;
    }

    public NodeType getType () {
        return this.type;
    }

    public Node getParent () {
       return this.parent;
    }

    /* Autres méthodes utiles
    =============================*/
    @Override
    public String toString() {
      return "Node "+getType()+"|| Cout H : "+this.h_cout+" Cout G : "+getGCout();
    }
}
