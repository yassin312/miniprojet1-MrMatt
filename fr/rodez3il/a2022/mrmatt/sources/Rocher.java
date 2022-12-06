package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Rocher extends ObjetPlateau{

  public char afficher(){
    return "*";
  }
  @Override
  public estMarchable(){
    return false;
  }

  @Override
  public estGlissant(){
    return true;
  }

  @Override
  public estPoussable(){
    return true;
  }
}