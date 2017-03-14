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

    public void setType (NodeType type) {
        this.type = type;
    }

    public void setParent (Node parent) {
        this.parent = parent;
    }

    public void setHCout (int h_cout) {
        this.h_cout = h_cout;
    }

    public NodeType getType () {
        return this.type;
    }
}
