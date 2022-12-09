# miniprojet1-MrMatt

# Ce que je n'ai pas réalisé : 
 - La méthode estPoussable ainsi que l'annulation d'un mouvement dans une partie.
 - La chute de certains rochers ne tue pas le joueur.
 - Le joueur peut bouger alors qu'un rocher est encore en train de chuter

# Questions :

  * Question : Pourquoi cette classe est-elle abstraite ?
    - Réponse : La classe objet plateau est abstraite car il y à plusieurs type d'objet avec des           caractéristique différent. Cette classe va servir de patron pour ces objets plus spécifique.
      
  * Question : Pourquoi la méthode echanger est-elle privée ? 
    - Réponse : Elle est privée car cette méthode n'est utilisée qu'uniquement dans la classe Niveau.
      
  * Question : Pourquoi les deux méthodes deplacer et deplacementPossible sont-elles privées ?
    - Réponse : Ces deux méthodes ne sont utilisées que dans la classe Niveau, la visibilité la plus faible leur est donc attribuée afin de respecter l'intégrité des données.
      
  * Question : Quel est le problème d'une telle implémentation, d'après le cours ?
    - Réponse : L'utilisation de instanceof est fortement déconseillée car faire une instanceof de Rocher suivi d'une instanceof de Pomme violerait le principe de Liskov.
      
  * Question : Pourquoi la méthode etatSuivant() est-elle publique ?
    - Réponse : La méthode etatSuivant est utilisée dans la classe Jeu pour mettre à jour le plateau, si sa visibilité était en privé la classe jeu n'aurait pas accès à cette méthode et donc ne pourrait pas mettre à jour l'état du plateau.


# Points difficiles :

  - La compréhension de certaines méthodes, j'ai eu du mal à comprendre ce qui était demandé ou comment le réaliser, notamment pour la méthode patron visiteur du rocher
  - La classe Jeu.java était un peu flou au début du projet et ne possède pas d'explications, ce qui m'a fait refaire certaines méthodes.