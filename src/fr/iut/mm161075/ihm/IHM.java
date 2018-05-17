package fr.iut.mm161075.ihm;

import fr.iut.mm161075.metier.Philosophe;

/**
 * Interface qui représente une IHM.
 * Peut être utile pour créer une IHM en console par exemple.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public interface IHM {

	/**
	 * Méthode lancée pour démarrer l'IHM si besoin.
	 * @param nbPhilosophes Nombre de philosophes gérés
	 */
	void demarrer(int nbPhilosophes);

	/**
	 * Méthode lancée pour mettre à jour un philosophe.
	 * @param philosophe Philosophe à mettre à jour dans l'IHM
	 */
	void miseAJour(Philosophe philosophe);

}
