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
  private boolean chuteRocher = false;
  private int nbDeplacement;
  private boolean partie = true;

  /**
   * Constructeur public : crée un niveau depuis un fichier.
   * 
   * @param chemin .....
   * @author .............
   */
  public Niveau(String chemin) {
    // chargerNiveau(chemin);
    // this.plateau = new ObjetPlateau[0][0];
    nbPomme = 0;
    String[] fichier = Utils.lireFichier(chemin).split("\n");

    // Récupère le nombre de colonne et le nombre de ligne sous le format INTEGER
    this.nombreColonne = Integer.parseInt(fichier[0]);
    this.nombreLigne = Integer.parseInt(fichier[1]);

    this.plateau = new ObjetPlateau[this.nombreLigne][this.nombreColonne];

    // Permet d'afficher le plateau
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
    ObjetPlateau tempo = this.plateau[destinationX][destinationY];
    this.plateau[destinationX][destinationY] = this.plateau[sourceX][sourceY];
    this.plateau[sourceX][sourceY] = tempo;
  }

  /**
   * Produit une sortie du niveau sur la sortie standard.
   * ................
   */

  public void afficher() {

    for (int i = 0; i < plateau.length; ++i) {
      for (int j = 0; j < plateau[i].length; ++j) {
        System.out.print(this.plateau[i][j].afficher());      
      }
      System.out.println();
    }
    System.out.println("Pommes restantes : " + this.nbPomme);
    System.out.println("Nombre déplacements : " + this.nbDeplacement);
    
  }

  // TODO : patron visiteur du Rocher...
  public void etatSuivantVisiteur(Rocher r, int x, int y) {

    // Verifier si le rocher et fixe et n'a rien en dessous de lui
    if (r.getEtat() == EtatRocher.FIXE && x+1<nombreLigne) {
      if(plateau[x+1][y].estVide()){
        // Passe l'objet Rocher en etat de chute      
        r.setEtat(EtatRocher.CHUTE);
      }
     
    }
      else if (plateau[x+1][y].estVide()){
        echanger(x, y, x+1, y);
      }
    // Si le rocher est en etat de chute
    else if (r.getEtat() == EtatRocher.CHUTE && x+1<nombreLigne) {
      // Si le joueur se trouve en dessus d'un rocher qui chute la partie s'arrête
      if (x+1 == joueurX && y == joueurY) {
        r.setEtat(EtatRocher.FIXE);
        partie = false;
      }
    }
      // Si le rocher chute sur un autre rocher
      else if (plateau[x+1][y].estGlissant()) {
        // Il se place à gauche si c'est possible
        if (plateau[x][y-1].estVide() && plateau[x + 1][y - 1].estVide()) {
          echanger(x, y, x + 1, y - 1);
          r.setEtat(EtatRocher.FIXE);
        }
        // Sinon il se place à droite
        else {
          echanger(x, y, x + 1, y + 1);
          plateau[x][y] = new Vide();
          r.setEtat(EtatRocher.FIXE);
        }
      }
     else {
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
    chuteRocher = false;
    System.out.println(plateau.length);
    for (int i = plateau.length - 1; i >= 0; i--) {
      for (int j = plateau[i].length - 1; j >= 0; j--) {
        plateau[i][j].visiterPlateauCalculEtatSuivant(this, i, j);
      }
    }
    if(nbPomme == 0)
      partie = false;
  }

  // Joue la commande C passée en paramètres
  /*
   * return true si le plateau subi un changement
   */
  public boolean jouer(Commande c) {
    int deltaX = joueurX;
    int deltaY = joueurY;
    switch (c) {
      case HAUT:
        deltaX--;
        break;
      case GAUCHE:
        deltaY--;
        break;
      case BAS:
        deltaX++;
        break;
      case DROITE:
        deltaY++;
        break;
    }
    if (deplacementPossible(deltaX,deltaY)) {
      deplacer(deltaX, deltaY);
      return true;
    }
    return false;
  }

  private boolean deplacementPossible(int dx, int dy) {

    if (0 <= dx && 0 <= dy){
      if(plateau[dx][dy].estMarchable())
        return true;
    }
    return false;
  }

  /**
   * Methode deplacer
   */

  public void deplacer(int deltaX, int deltaY) {
    if(plateau[deltaX][deltaY].estPomme() == true)
      nbPomme--;
      // Déplace le joueur à la destination choisit
    echanger(this.joueurX, this.joueurY, deltaX, deltaY);
    plateau[this.joueurX][this.joueurY] = new Vide();
    nbDeplacement++;
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

    return partie && nbPomme > 0;
  }

  /**
   * 
   */
  public boolean estIntermediaire() {
    return chuteRocher;
  }
}
