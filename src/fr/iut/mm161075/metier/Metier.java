package fr.iut.mm161075.metier;

import fr.iut.mm161075.Controleur;

import java.util.Random;

/**
 * Classe métier.
 * Gère les différents philosophes et la simulation du problème.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class Metier {

	/**
	 * Classe JAVA de gestion de l'aléatoire
	 */
	final static Random ALEA = new Random();

	/**
	 * Contrôleur du programme
	 */
	private Controleur controleur;

	private boolean simulationDemarree;

	private boolean simulationPause;

	/**
	 * Liste des baguettes
	 */
	private boolean[] baguettes = null;

	/**
	 * Liste des philosophes gérés
	 */
	private Philosophe[] philosophes = null;

	/**
	 * Nombre de philosophes à gérer
	 */
	private int n;

	/**
	 * Construction du métier
	 * @param controleur Controleur du programme
	 */
	public Metier(Controleur controleur) {
		this.controleur = controleur;
	}

	Controleur getControleur() {
		return this.controleur;
	}

	public int getNb() {
		return this.n;
	}

	public boolean getBaguette(int n) {
		return this.baguettes[n];
	}

	public Philosophe[] getVoisins(Philosophe philo) {
		int mIdx = (philo.getI() - 1) % this.getNb();
		int pIdx = (philo.getI() + 1) % this.getNb();

		if (mIdx < 0) mIdx += this.getNb();

		return new Philosophe[] {
				this.philosophes[mIdx],
				this.philosophes[pIdx]
		};
	}

	public void setBaguette(int n, boolean presence) {
		this.baguettes[n] = presence;
	}

	public void initialiser(int n) {
		this.n = n;
	}

	public void demarrerSimulation() {
		if (this.simulationDemarree)
			this.stopperSimulation();

		// On initialise les champs ...
		this.baguettes = new boolean[n];
		this.philosophes = new Philosophe[n];

		// ... puis, de base toutes baguettes sont disponibles ...
		for (int i = 0; i < n; i++)
			this.baguettes[i] = true;

		// ... et enfin on créé les philosophes et on les démarre.
		Philosophe philo;
		for (int i = 0; i < n; i++) {
			philo = new Philosophe(this, i);
			philo.demarrer();

			this.philosophes[i] = philo;
		}

		this.simulationDemarree = true;
	}

	public void stopperSimulation() {
		if (this.simulationDemarree) {
			// On stoppe tous les philosophes ...
			for (int i = 0; i < n; i++)
				this.philosophes[i].stopper();

			// ... puis on enregistre en mémoire l'arrêt.
			this.simulationDemarree = false;
			this.simulationPause = false;
		}
	}

	public boolean isSimulationPause() {
		return this.simulationPause;
	}

	public void pauseSimulation() {
		this.simulationPause = true;

		for (Philosophe philosophe : this.philosophes)
			philosophe.pause();
	}

	public void resumeSimulation() {
		this.simulationPause = false;

		for (Philosophe philosophe : this.philosophes)
			philosophe.resume();
	}

}


