package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Pomme extends ObjetPlateau{

  public char afficher(){
    return '+';
  }
  
  public boolean estMarchable(){
    return true;
  }

  public void visiterPlateauCalculEtatSuivant(Niveau plateau, int x, int y) {
    plateau.etatSuivantVisiteur(this, x, y);
  }
}