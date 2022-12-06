package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Vide extends ObjetPlateau {

  public char afficher(){
    return " ";
  }

  @Override
  public estMarchable(){
    return false;
  }

  // @Override
  public boolean estVide(){
    return true;
  }
}