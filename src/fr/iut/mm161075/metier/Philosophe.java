package fr.iut.mm161075.metier;

import fr.iut.mm161075.Consts;

import java.util.concurrent.Semaphore;

/**
 * Représente un philosophe, côté métier.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class Philosophe extends Semaphore implements Runnable {

	/**
	 * Classe métier
	 */
	private Metier metier;

	/**
	 * Thread utilisé pour gérer les actions du philosophe
	 */
	private Thread th;

	/**
	 * Etat courant du philosophe
	 */
	private Etat etat;

	/**
	 * Identifiant du philosophe
	 */
	private int i;

	Philosophe(Metier metier, int i) {
		super(1, true);

		this.metier = metier;
		this.etat = Etat.PENSE;
		this.i = i;

		this.th = new Thread(this);
	}

	public int getI() {
		return this.i;
	}

	public Etat getEtat() {
		return this.etat;
	}

	/**
	 * On demande au philosophe de démarrer ses actions
	 */
	void demarrer() {
		this.th.start();
	}

	/**
	 * On demande au philosophe de stopper ses actions
	 */
	void stopper() {
		this.th.stop();
	}

	/**
	 * On demande au philosophe de mettre en pause ses actions
	 */
	void pause() {
		this.th.suspend();
	}

	/**
	 * On demande au philosophe de redémarrer ses actions
	 */
	void resume() {
		this.th.resume();
	}

	private void maJ() {
		this.metier.getControleur().majIHM(this);
	}

	/**
	 * Le philosophe pense...
	 * @throws InterruptedException Exception si une erreur a lieu pendant sa pause
	 */
	private void penser() throws InterruptedException {
		if (this.etat != Etat.PENSE) return;

		this.maJ();
		Thread.sleep(Consts.DELAI_MIN_PENSER + Metier.ALEA.nextInt(Consts.DELAI_MAX_PENSER - Consts.DELAI_MIN_PENSER));
	}

	/**
	 * Le philosophe mange...
	 * @throws InterruptedException Exception si une erreur a lieu pendant son repas
	 */
	private void manger() throws InterruptedException {
		if (this.etat != Etat.MANGE) return;

		this.maJ();
		Thread.sleep(Consts.DELAI_MIN_MANGER + Metier.ALEA.nextInt(Consts.DELAI_MAX_MANGER - Consts.DELAI_MIN_MANGER));
	}

	/**
	 * Test si le philosophe peut manger ou non.
	 */
	private void testManger() {
		Philosophe[] voisins = this.metier.getVoisins(this);

		if (this.etat == Etat.A_FAIM && voisins[0].etat != Etat.MANGE && voisins[1].etat != Etat.MANGE) {
			this.etat = Etat.MANGE;

			this.metier.setBaguette(this.i, false);
			this.metier.setBaguette((this.i + 1) % this.metier.getNb(), false);

			this.maJ();

			this.release();
		}
	}

	/**
	 * Essaie de prendre les baguettes pour commencer à manger
	 * @throws InterruptedException Exception si une erreur a lieu pendant son action
	 */
	private void prendreBaguettes() throws InterruptedException {
		synchronized (this) {
			this.etat = Etat.A_FAIM;
			this.maJ();

			this.testManger();
		}

		this.acquire();
	}

	/**
	 * Poser les baguettes après avoir mangé
	 */
	private synchronized void poserBaguettes() {
		this.etat = Etat.PENSE;

		this.metier.setBaguette(this.i, true);
		this.metier.setBaguette((this.i + 1) % this.metier.getNb(), true);

		this.maJ();

		for (Philosophe voisin : this.metier.getVoisins(this))
			voisin.testManger();
	}

	/**
	 * Méthode de lancement des actions du philosophe
	 */
	@Override
	public void run() {
		while (!this.metier.isSimulationPause()) {
			try {

				this.penser();
				this.prendreBaguettes();
				this.manger();
				this.poserBaguettes();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
