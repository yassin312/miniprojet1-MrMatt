package fr.rodez3il.a2022.mrmatt.sources;

import java.awt.geom.Ellipse2D;

public class Niveau {

  // Les objets sur le plateau du niveau
  private ObjetPlateau[][] plateau;
  // Position du joueur
  private int joueurX;
  private int joueurY;

  // Autres attributs que vous jugerez nécessaires...

  /**
   * Constructeur public : crée un niveau depuis un fichier.
   * 
   * @param chemin .....
   * @author .............
   */
  public Niveau(String chemin) {
    this();
    chargerNiveau(chemin);
    Utils.lireFichier(chemin);
  }

  /**
   * Javadoc à réaliser...
   * 
   * @param
   * @author
   */
  private void echanger(int sourceX, int sourceY, int destinationX, int destinationY) {
    // Ajout d'un objet vide à l'emplacement de base du joueur
    plateau[this.joueurX][this.joueurY] = new Vide();
    joueurX = destinationX;
    joueurY = destinationY;
  }

  /**
   * Produit une sortie du niveau sur la sortie standard.
   * ................
   */
  public void afficher() {
    // TODO

  }

  // TODO : patron visiteur du Rocher...
  public void etatSuivantVisiteur(Rocher r, int x, int y) {

  }

  /**
   * Calcule l'état suivant du niveau.
   * ........
   * 
   * @author
   */
  public void etatSuivant() {
    // TODO
  }

  // Illustrez les Javadocs manquantes lorsque vous coderez ces méthodes !

  public boolean enCours() {
  }

  // Joue la commande C passée en paramètres
  /*
   * return true si le plateau subi un changement
   */
  public boolean jouer(Commande c) {
  //   int x, y;

  //   if (x + 1) {
  //     return true;
  //   } else if (x - 1) {
  //     return true;
  //   } else if (y + 1) {
  //     return true;
  //   } else if (y - 1)
  //     return true;
  //   else
  //     return false;
  // }
    switch (c){
      case HAUT:
        deplacer(this.joueurX,joueurY-1);
        break;
      case GAUCHE:
        deplacer(this.joueurX-1,joueurY);
        break;
      case BAS:
        deplacer(this.joueurX,joueurY+1);
        break;
      case DROITE:
        deplacer(this.joueurX+1,joueurY);
        break;
      
    }
    return true;
  }

  private boolean deplacementPossible(int dx, int dy){
    int toggleX = this.joueurX + dx;
    int toggleY = this.joueurY + dy;
    
    if (plateau[toggleX][toggleY].estMarchable())
      return true;
    
    return false;
    
  }

  /**
   * Methode deplacer
   */

  public void deplacer(int deltaX, int deltaY) {
    
    if (deplacementPossible(deltaX, deltaY) == true){    
      echanger(this.joueurX, this.joueurY, deltaX, deltaY);
      // int toggleX = this.joueurX + deltaX;
      // int toggleY = this.joueurY + deltaY;
      this.joueurX = deltaX;
      this.joueurY = deltaY;
    }
    else
      System.out.println("Déplacement impossible");   
  }

  /**
   * Affiche l'état final (gagné ou perdu) une fois le jeu terminé.
   */
  public void afficherEtatFinal() {
  }

  /**
   */
  public boolean estIntermediaire() {
  }

}
