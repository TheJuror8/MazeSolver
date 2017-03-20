/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazesolver;

/**
 *
 * @author habib
 */
public class justatest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        int a = 255;
        int r = 22;
        int g = 225;
        int b = 25;

        //set the pixel value
        int p = (a<<24) | (r<<16) | (g<<8) | b;

        System.out.println ("VERT : "+p);// TODO code application logic here

        a = 255;
        r = 242;
        g = 44;
        b = 26;

        //set the pixel value
        p = (a<<24) | (r<<16) | (g<<8) | b;

        System.out.println ("ROUGE : "+p);

        a = 255;
        r = 255;
        g = 215;
        b = 0;

        //set the pixel value
        p = (a<<24) | (r<<16) | (g<<8) | b;

        System.out.println ("CHEMIN : "+p);
    }

}
