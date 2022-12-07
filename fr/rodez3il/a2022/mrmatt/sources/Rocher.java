package fr.rodez3il.a2022.mrmatt.sources.objets;

public class Rocher extends ObjetPlateau {
  private EtatRocher etat;

  public enum EtatRocher {
    // Etat du rocher
    CHUTE, // le rocher est en train de tomber
    FIXE; // le rocher est sur un sol
  }

  public getEtat(){
    return etat;
  }

  public setEtat(){
    this.etat
  }

  public char afficher() {
    return "*";
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

  @Override
  public void visiterPlateauCalculEtatSuivant(Niveau plateau, int x, int y) {
    plateau.etatSuivantVisiteur(this, x, y);
  }
}