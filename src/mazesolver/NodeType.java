package mazesolver;

public enum NodeType {
  NULL,     /* Case indéfinie */
  EMPTY,    /* Case vide */
  OBSTACLE, /* Obstacle */
  PATH,     /* Chemin */
  START,    /* Case départ => uniquement en affichage */
  END       /* Case d'arrivée => uniquement en affichage */
}
