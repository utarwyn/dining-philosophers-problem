package fr.iut.mm161075;

import java.awt.*;

/**
 * Classe de constantes.
 * Ne fait rien de plus que de déclarer des constantes
 * pour simplifier la configuration & personnalisation du programme.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class Consts {

	/**
	 * Classe utilitaire.
	 * Aucune instance possible.
	 */
	private Consts() {

	}

	/*  Métier  */

	public static final int DELAI_MIN_PENSER = 3000;

	public static final int DELAI_MAX_PENSER = 6000;

	public static final int DELAI_MIN_MANGER = 5000;

	public static final int DELAI_MAX_MANGER = 8000;


	/*  IHM  */

	public static final int MAJ_IHM_DELAI = 500;

	public static final Color[] COULEUR_ETATS = {
			new Color(68, 189, 50),  // Etat 1 : je pense
			new Color(255, 157, 34), // Etat 2 : j'ai faim
			new Color(194, 54, 22)   // Etat 3 : je mange
	};

	public static final Color COULEUR_FOND = new Color(53, 59, 72);

	public static final Color COULEUR_FOND_2 = new Color(47, 54, 64);

	public static final Color COULEUR_TABLE = new Color(72, 126, 176);

	public static final Color COULEUR_BORDURE_TABLE = new Color(64, 115, 158);

	public static final Color COULEUR_BAGUETTE = new Color(255, 255, 255);

}
