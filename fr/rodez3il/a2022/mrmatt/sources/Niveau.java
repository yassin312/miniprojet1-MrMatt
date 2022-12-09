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
   * @param chemin est le chemin afin de trouver le nom du plateau à générer
   * @author Yassin Farassi
   */
  public Niveau(String chemin) {
    nbPomme = 0;
    String[] fichier = Utils.lireFichier(chemin).split("\n");

    // Récupère le nombre de colonnes et le nombre de lignes sous le format INTEGER
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
  /* @param c est la commande récupérée afin de savoir quel déplacement est demandé par le joueur.
   * return true si le plateau subit un changement
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
   * @param SourceX est la position sur l'axe vertical de base de l'échange
   * @param SourceY est la position sur l'axe horizontal de base de l'échange
   * @param destinationX est la position sur l'axe vertical de fin de l'échange
   * @param destinationY est la position sur l'axe horizontal de fin de l'échange
   * @author Yassin Farassi
   */
  private void echanger(int sourceX, int sourceY, int destinationX, int destinationY) {
    // Création d'un objet plateau temporaire pour stocker la position de fin de l'objet échangé
    ObjetPlateau tempo = plateau[destinationX][destinationY];
    plateau[destinationX][destinationY] = plateau[sourceX][sourceY];
    plateau[sourceX][sourceY] = tempo;
  }

  /**
   * Produit une sortie du niveau sur la sortie standard.
   * @author Yassin Farassi
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
  /**
    * @param r est un objet de type rocher
    * @param x représente la position verticale du rocher
    * @param y représente la position horizontale du rocher
    */
  public void etatSuivantVisiteur(Rocher r, int x, int y) {

    // Vérifie si le rocher est fixe et est dans le plateau 
    if (r.getEtat() == EtatRocher.FIXE && x+1<nombreLigne) {
      // Si le rocher n'a rien en dessous de lui
      if(plateau[x+1][y].estVide()){
        // Passe l'objet Rocher en état de chute      
        r.setEtat(EtatRocher.CHUTE);
      }     
    }
      // Vérifie si le rocher à encore du vide en dessous de lui pendant sa chute
      else if (plateau[x+1][y].estVide()){
        echanger(x, y, x+1, y);
      }
    // Si le rocher est en état de chute
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
   * @author Yassin Farassi
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
    * Vérifie si le déplacement est possible avant de l'effectuer
    * @param dx représente la position verticale du mouvement demandée
    * @param dy représente la position horizontale du mouvement demandée
    * @author Yassin Farassi
    */
 private boolean deplacementPossible(int dx, int dy) {

   // Vérifie si le déplacement entré se trouve bien dans le plateau
    if (0 <= dx && 0 <= dy){
      // Vérifie si le déplacement entré est dans un objet marchable
      if(plateau[dx][dy].estMarchable())
        return true;
    }
    return false;
  }

  /**
    * Effectue le déplacement
    * @param deltaX représente la position verticale du mouvement
    * @param deltaY représente la position horizontale du mouvement
    * @author Yassin Farassi
    */

  public void deplacer(int deltaX, int deltaY) {
    if(plateau[deltaX][deltaY].estPomme() == true)
      nbPomme--;
      // Déplace le joueur à la destination choisit
    echanger(joueurX, joueurY, deltaX, deltaY);
    // Remplace la case où se situait le joueur par du vide
    plateau[joueurX][joueurY] = new Vide();
    nbDeplacement++;
    // Met à jour les coordonnées du joueur
    joueurX = deltaX;
    joueurY = deltaY;
  }

  /**
    * Affiche l'état final (gagné ou perdu) une fois le jeu terminé.
    * @author Yassin Farassi
    */
  public void afficherEtatFinal() {
    if (nbPomme == 0)
      System.out.println("Vous avez gagné !");
    else
      System.out.println("Vous avez perdu...");
  }


  /**
    * Vérifie si la partie est encore en cours ou non
    * @author Yassin Farassi
    */
  public boolean enCours() {

    return partie && nbPomme > 0;
  }

  /**
   * Vérifie que tous les rochers du plateau soit fixe avant de permettre au joueur de se déplacer
   */
  public boolean estIntermediaire() {
    return chuteRocher;
  }
}
