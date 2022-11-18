package fr.rodez3il.a2022.mrmatt.sources.objets;

public abstract class ObjetPlateau{

  public abstract char afficher();
  
  public boolean estVide(){
    boolean vide = true;
    return vide;
  };
  public boolean estMarchable(){
    boolean marchable = true;
    return marchable;
  };
  public boolean estPoussable(){
    boolean poussable = false;
    return poussable;
  };
  public boolean estGlissant(){
    boolean glissant = false;
    return glissant;
  }

  public void visiterPlateauCalculEtatSuivant(Niveau niveau, int x, int y) {
    
  }
  
}