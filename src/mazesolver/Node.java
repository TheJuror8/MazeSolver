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
    private int type; //-1 si indéfini, 0 si vide, 1 si obstacle, 2 si chemin
    private int h_cout, g_cout;
    private Node parent;

    Node() {
        this.type = -1;
    }
    
    Node(int type) {
        this.type = type;
    }

    Node(int type , Node parent) {
        this.type = type;
        this.parent = parent;
    }

    public void setType (int tval) {
        this.type = tval;
    }

    public void setParent (Node parent) {
        this.parent = parent;
    }

    public int getType () {
        return this.type;
    }
}
