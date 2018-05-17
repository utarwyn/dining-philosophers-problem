package fr.iut.mm161075;

import fr.iut.mm161075.ihm.Frame;
import fr.iut.mm161075.ihm.IHM;
import fr.iut.mm161075.metier.Metier;
import fr.iut.mm161075.metier.Philosophe;

/**
 * Classe principale du programme.
 * Etablie une liaison entre les parties métier et IHM.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class Controleur {

	/**
	 * Partie IHM
	 */
	private IHM ihm;

	/**
	 * Partie Métier
	 */
	private Metier metier;

	private Controleur() {
		this.ihm = new Frame(this);
		this.metier = new Metier(this);
	}

	public void demarrerSimulation() {
		this.metier.demarrerSimulation();
	}

	public void stopperSimulation() {
		this.metier.stopperSimulation();
	}

	public boolean isSimulationPause() {
		return this.metier.isSimulationPause();
	}

	public void pauseSimulation() {
		this.metier.pauseSimulation();
	}

	public void resumeSimulation() {
		this.metier.resumeSimulation();
	}

	/**
	 * Méthode appellée quand le métier indique à l'IHM de se mettre à jour.
	 * @param philosophe Philosophe à mettre à jour dans l'IHM
	 */
	public void majIHM(Philosophe philosophe) {
		this.ihm.miseAJour(philosophe);
	}

	/**
	 * Démarre l'application avec un nombre de philosophes définis.
	 * @param n Nombre de philosophes à gérer
	 */
	public void demarrer(int n) {
		this.metier.initialiser(n);
		this.ihm.demarrer(n);
	}

	public static void main(String[] args) {
		int n;

		try {
			n = Integer.parseInt(args[0]);
		} catch (Exception e) {
			n = 5;
		}

		new Controleur().demarrer(n);
	}

}
