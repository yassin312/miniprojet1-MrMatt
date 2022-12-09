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
   * @author Yassin Farassi
   */
  public Niveau(String chemin) {
    nbPomme = 0;
    String[] fichier = Utils.lireFichier(chemin).split("\n");

    // Récupère le nombre de colonne et le nombre de ligne sous le format INTEGER
    nombreColonne = Integer.parseInt(fichier[0]);
    nombreLigne = Integer.parseInt(fichier[1]);

    plateau = new ObjetPlateau[nombreLigne][nombreColonne];

    // Permet d'afficher le plateau
    for (int i = 2; i < nombreLigne + 2; i++) {
      for (int j = 0; j < nombreColonne; j++) {
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
      case QUITTER:
        partie = false;
        break;
    }
    if (deplacementPossible(deltaX,deltaY)) {
      deplacer(deltaX, deltaY);
      return true;
    }
    return false;
  } 

  /**
   * Permet d'échanger la position de deux objets du plateau
   * 
   * @param
   * @author Yassin Farassi
   */
  private void echanger(int sourceX, int sourceY, int destinationX, int destinationY) {
    ObjetPlateau tempo = plateau[destinationX][destinationY];
    plateau[destinationX][destinationY] = plateau[sourceX][sourceY];
    plateau[sourceX][sourceY] = tempo;
  }

  /**
   * Produit une sortie du niveau sur la sortie standard.
   * @param
   * @author
   */
  public void afficher() {

    for (int i = 0; i < plateau.length; ++i) {
      for (int j = 0; j < plateau[i].length; ++j) {
        System.out.print(plateau[i][j].afficher());      
      }
      System.out.println();
    }
    System.out.println("Pommes restantes : " + nbPomme);
    System.out.println("Nombre déplacements : " + nbDeplacement);
    
  }

    // Patron visiteur du Rocher
  public void etatSuivantVisiteur(Rocher r, int x, int y) {

    // Verifier si le rocher est fixe et est dans le plateau 
    if (r.getEtat() == EtatRocher.FIXE && x+1<nombreLigne) {
      // Si le rocher n'a rien en dessou de lui
      if(plateau[x+1][y].estVide()){
        // Passe l'objet Rocher en etat de chute      
        r.setEtat(EtatRocher.CHUTE);
      }     
    }
      // Verifie si le rocher à encore du vide en dessous de lui pendant sa chute
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
        // Si le joueur est sur le chemin d'un rocher en train de glisser vers la gauche, il perd
        if(x+1 == joueurX && y-1 == joueurY){
          echanger(x, y, x, y - 1);        
          partie = false;
        }           
        // Il se place à gauche si c'est possible
        else if (plateau[x][y-1].estVide() && plateau[x + 1][y - 1].estVide()) {                
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
      chuteRocher = true;
    } else
        chuteRocher = false;
  }

  /**
   * Calcule l'état suivant du niveau.
   * 
   * 
   * @author
   */
  public void etatSuivant() {
    chuteRocher = true;
    System.out.println(plateau.length);
    for (int i = plateau.length - 1; i >= 0; i--) {
      for (int j = plateau[i].length - 1; j >= 0; j--) {
        plateau[i][j].visiterPlateauCalculEtatSuivant(this, i, j);
      }
    }
    if(nbPomme == 0)
      partie = false;
  }

  /**
  * Verifie si le déplacement est possible avant de l'effectuer
  */
 private boolean deplacementPossible(int dx, int dy) {

    if (0 <= dx && 0 <= dy){
      if(plateau[dx][dy].estMarchable())
        return true;
    }
    return false;
  }

  /**
   * Effectue le déplacement
   */

  public void deplacer(int deltaX, int deltaY) {
    if(plateau[deltaX][deltaY].estPomme() == true)
      nbPomme--;
      // Déplace le joueur à la destination choisit
    echanger(joueurX, joueurY, deltaX, deltaY);
    plateau[joueurX][joueurY] = new Vide();
    nbDeplacement++;
    joueurX = deltaX;
    joueurY = deltaY;
  }

  /**
   * Affiche l'état final (gagné ou perdu) une fois le jeu terminé.
   */
  public void afficherEtatFinal() {
    if (nbPomme == 0)
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
