package fr.rodez3il.a2022.mrmatt.sources.objets;

import fr.rodez3il.a2022.mrmatt.sources.*;


public class Rocher extends ObjetPlateau {
  
  private EtatRocher etat;

  public EtatRocher getEtat(){
    return etat;
  }

  public void setEtat(EtatRocher etat){
    this.etat = etat;
  }

  public char afficher() {
    return '*';
  }

  public boolean estMarchable() {
    return false;
  }

  public boolean estGlissant() {
    return true;
  }

  public boolean estPoussable() {
    return true;
  }

  public void visiterPlateauCalculEtatSuivant(Niveau plateau, int x, int y) {
    plateau.etatSuivantVisiteur(this, x, y);
  }
}