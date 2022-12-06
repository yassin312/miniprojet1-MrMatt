package fr.rodez3il.a2022.mrmatt.sources.objets;

public abstract class ObjetPlateau{

  public abstract char afficher();

  // Renvoie si l'objet est vide
  public boolean estVide(){
    return false;
  }
  // Renvoie si l'objet est marchable
  public boolean estMarchable(){
    return false;
  }
  // qui renvoie si l'objet est poussable (c'est à dire que le joueur peut le pousser horizontalement en se déplaçant dans sa direction).
  public boolean estPoussable(){
    return false;
  }
  // qui renvoie si l'objet est glissant (c'est à dire qu'un rocher tombant dessus glissera à gauche ou à droite pour tomber).
  public boolean estGlissant(){
    return false;
  }

  public void visiterPlateauCalculEtatSuivant(Niveau niveau, int x, int y) {
    
  }
  
}