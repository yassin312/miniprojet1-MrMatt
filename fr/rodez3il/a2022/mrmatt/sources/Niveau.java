package fr.rodez3il.a2022.mrmatt.sources;

import fr.rodez3il.a2022.mrmatt.sources.objets.ObjetPlateau;
import fr.rodez3il.a2022.mrmatt.sources.objets.Herbe;
import fr.rodez3il.a2022.mrmatt.sources.objets.Pomme;
import fr.rodez3il.a2022.mrmatt.sources.objets.Vide;
import fr.rodez3il.a2022.mrmatt.sources.objets.Mur;
import fr.rodez3il.a2022.mrmatt.sources.objets.Rocher;
import fr.rodez3il.a2022.mrmatt.sources.objets.EtatRocher;

import java.awt.geom.Ellipse2D;

public class Niveau {

  // Les objets sur le plateau du niveau
  private ObjetPlateau[][] plateau;
  // Position du joueur
  private int joueurX;
  private int joueurY;

  // Autres attributs que vous jugerez nécessaires...
  private int nombreLigne;
  private int nombreColonne;
  private int nbPomme;
  private boolean chuteRocher;

  /**
   * Constructeur public : crée un niveau depuis un fichier.
   * 
   * @param chemin .....
   * @author .............
   */
  public Niveau(String chemin) {
    chargerNiveau(chemin);
    // this.plateau = new ObjetPlateau[0][0];
  }

  private void chargerNiveau(String chemin) {
    this.nbPomme = 0;
    String[] fichier = Utils.lireFichier(chemin).split("\n");
    this.nombreColonne = Integer.parseInt(fichier[0]);
    this.nombreLigne = Integer.parseInt(fichier[1]);

    this.plateau = new ObjetPlateau[this.nombreLigne][this.nombreColonne];

    for (int i = 2; i < this.nombreLigne + 2; i++) {
      for (int j = 0; j < this.nombreColonne; j++) {
        plateau[i - 2][j] = ObjetPlateau.depuisCaractere(fichier[i].charAt(j));
        if (fichier[i].charAt(j) == 'H') {
          joueurX = i - 2;
          joueurY = j;
        }
        if (fichier[i].charAt(j) == '+') {
          nbPomme++;
        }
      }
    }
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

    for (int i = 0; i < this.plateau.length; ++i) {
      for (int j = 0; j < this.plateau[i].length; ++j) {
        System.out.print(this.plateau[i][j].afficher());
      }
      System.out.println();
    }
    System.out.println("Pommes restantes : " + this.nbPomme);
  }

  // TODO : patron visiteur du Rocher...
  public void etatSuivantVisiteur(Rocher r, int x, int y) {

    // Verifier si le rocher et fixe et n'a rien en dessous de lui
    if (r.getEtat() == EtatRocher.FIXE && this.plateau[x][y + 1].estVide()) {
      // Passe l'objet Rocher en etat de chute
      r.setEtat(EtatRocher.CHUTE);
    }
    // Si le rocher est en etat de chute
    else if (r.getEtat() == EtatRocher.CHUTE) {
      // Si le joueur se trouve en dessus d'un rocher qui chute
      if (x == joueurX && y + 1 == joueurY) {
        System.out.println("Défaite");
      }
      // Si le rocher chute sur un autre rocher
      else if (plateau[x][y + 1].estGlissant()) {
        // Il se place à gauche si c'est possible
        if (plateau[x - 1][y].estVide() && plateau[x - 1][y + 1].estVide()) {
          echanger(x, y, x - 1, y + 1);
          r.setEtat(EtatRocher.FIXE);
        }
        // Sinon il se place à droite
        else {
          echanger(x, y, x + 1, y + 1);
          r.setEtat(EtatRocher.FIXE);
        }
      }
    } else {
      r.setEtat(EtatRocher.FIXE);
    }
    if (r.getEtat() == EtatRocher.CHUTE) {
      this.chuteRocher = true;
    } else
      this.chuteRocher = false;
  }

  /**
   * Calcule l'état suivant du niveau.
   * 
   * 
   * @author
   */
  public void etatSuivant() {
    // Si auncun rocher ne chute
    if (chuteRocher == false) {
      for (int i = plateau.length - 1; i >= 0; i--) {
        for (int j = plateau[i].length - 1; j >= 0; j--) {
          plateau[i][j].visiterPlateauCalculEtatSuivant(this, i, j);
        }
      }
    }
  }

  // Joue la commande C passée en paramètres
  /*
   * return true si le plateau subi un changement
   */
  public boolean jouer(Commande c) {

    switch (c) {
      case HAUT:
        deplacer(this.joueurX, joueurY - 1);
        break;
      case GAUCHE:
        deplacer(this.joueurX - 1, joueurY);
        break;
      case BAS:
        deplacer(this.joueurX, joueurY + 1);
        break;
      case DROITE:
        deplacer(this.joueurX + 1, joueurY);
        break;

    }
    return true;
  }

  private boolean deplacementPossible(int dx, int dy) {
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
    // Si le déplacement est possible
    if (plateau[deltaX][deltaY].estMarchable()) {
      // Déplace le joueur à la destination choisit
      echanger(this.joueurX, this.joueurY, deltaX, deltaY);
      this.plateau[this.joueurX][this.joueurY] = new Vide();
    } else
      System.out.println("Déplacement impossible");
    this.joueurX = deltaX;
    this.joueurY = deltaY;
  }

  /**
   * Affiche l'état final (gagné ou perdu) une fois le jeu terminé.
   */
  public void afficherEtatFinal() {
    if (this.nbPomme == 0)
      System.out.println("Vous avez gagné !");
    else
      System.out.println("Vous avez perdu...");
  }

  // Illustrez les Javadocs manquantes lorsque vous coderez ces méthodes !

  public boolean enCours() {
    // S'il reste des pommes sur le plateau, la partie est encore en cours
    if (nbPomme != 0)
      return true;
    else
      return false;
  }

  /**
   * 
   */
  public boolean estIntermediaire() {
    return chuteRocher;
  }
}
