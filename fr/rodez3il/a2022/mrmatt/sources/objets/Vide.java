package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Vide extends ObjetPlateau {

  public char afficher(){
    return ' ';
  }


  public boolean estMarchable(){
    return false;
  }

  
  public boolean estVide(){
    return true;
  }
}