package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Herbe extends ObjetPlateau{

  public char afficher(){
    return "-";
  }
  @Override
  public estMarchable(){
    return true;
  }
  
}