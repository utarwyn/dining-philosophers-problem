package fr.iut.mm161075.ihm;

import fr.iut.mm161075.Consts;
import fr.iut.mm161075.metier.Etat;
import fr.iut.mm161075.metier.EtatBaguette;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Cette classe permet de gérer l'espace graphique de la table
 * des philosophes. Elle prépare et dessine la table, les baguettes
 * et les philosophes suivant leur état à un moment "t".
 * <p>
 * Elle se met à jour toute seule quand il le faut via un Thread que
 * l'on peut démarrer, stopper et mettre en pause.
 * </p>
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class GraphicTable extends JPanel implements Runnable {

	/**
	 * Forme de la table où sont posées les assiettes
	 */
	private final Ellipse2D.Double formeTable = new Ellipse2D.Double(-2.5, -2.5, 5, 5);

	/**
	 * Thread pour mettre à jour l'IHM
	 */
	private Thread t;

	/**
	 * Nombre de philosophes à gérer
	 */
	private int n;

	/**
	 * Etat des différents philosophes, mis à jour
	 * depuis la partie métier.
	 */
	private Etat[] etats;

	/**
	 * Dimension de l'affichage de la table en entier
	 * (table, philosophes, baguettes)
	 */
	private Dimension dimTable = new Dimension(0, 0);

	/**
	 * Objet de transformation 2d pour centrer la table
	 * au milieu de la fenêtre.
	 */
	private AffineTransform at;

	/**
	 * Construction de l'environnement graphique de la table
	 * @param n Nombre de philosophes à gérer
	 */
	GraphicTable(int n) {
		this.n = n;

		this.setBackground(Consts.COULEUR_FOND);

		this.etats = new Etat[n];
	}

	/**
	 * Retourne les états des philosophes
	 * @return Etats des philosophes
	 */
	public Etat[] getState() {
		return this.etats;
	}

	private void dessinerPhilosophe(int i, Graphics2D g2d) {
		// Couleur du philosophe en fonction de son état
		if (this.etats[i] != null)
			g2d.setPaint(Consts.COULEUR_ETATS[this.etats[i].getIndice()]);
		else
			g2d.setPaint(getBackground());

		// Définition de la taille et de la forme
		double size = Math.min(6 / Math.pow(n, .8), 1.5);
		Ellipse2D.Double forme = new Ellipse2D.Double(3, -size/2, size, size);

		// Couleur de base du philosophe
		if (t == null || !t.isAlive())
			g2d.setColor(new Color(25, 25, 25));

		// Et enfin on dessine !
		g2d.fill(forme);
		g2d.setPaint(Color.black);
		g2d.draw(forme);
		g2d.rotate(2 * Math.PI / n);
	}

	private EtatBaguette getEtatBaguettePour(int i) {
		if (this.etats[(i - 1 + n) % n] == Etat.MANGE)
			return EtatBaguette.MANGE_GAUCHE;

		if (this.etats[i] == Etat.MANGE)
			return EtatBaguette.MANGE_DROIT;

		return EtatBaguette.SUR_LA_TABLE;
	}

	private void dessinerBaguette(int i, Graphics2D g2d) {
		// Détermination de la positin avec un peu de maths
		double phi = (2 * i - 1) * Math.PI / n;

		EtatBaguette etat = this.getEtatBaguettePour(i);
		float facteurPhi = 0;

		if (etat == EtatBaguette.MANGE_GAUCHE)
			facteurPhi = -.8f;
		if (etat == EtatBaguette.MANGE_DROIT)
			facteurPhi =  .8f;

		phi = phi + facteurPhi * (Math.PI / (2 * n));

		// Détermination de la longueur en fonction
		// du nombre de philosophes autour de la table.
		double length = (2.0 * 3) / n;

		// On passe au dessin !
		g2d.setPaint(Consts.COULEUR_BAGUETTE);
		g2d.rotate(phi);
		g2d.setStroke(new BasicStroke((float) (.3 / Math.pow(n, .55))));

		if (etat == EtatBaguette.MANGE_GAUCHE || etat == EtatBaguette.MANGE_DROIT)
			g2d.draw(new Line2D.Double(3, 0, 3 + length, 0));
		else
			g2d.draw(new Line2D.Double(2.3 - length, 0, 2.3, 0));

		g2d.rotate(-phi);
	}

	private void majTailleTable() {
		// Taille de zone différente... on met à jour la table !
		if (!dimTable.equals(this.getSize())) {
			this.dimTable = this.getSize();

			// Détermination du zoom de la table
			double scale = Math.min(dimTable.width, dimTable.height) / 10.0;

			// Transformation au milieu de la fenêtre
			this.at = new AffineTransform();
			this.at.translate(dimTable.width / 2.0, dimTable.height / 2.0);
			this.at.scale(scale, scale);
		}
	}

	/**
	 * Démarrer la mise à jour de l'IHM
	 */
	public void demarrer() {
		this.t = new Thread(this);
		this.t.start();
	}

	/**
	 * Stopper la mise à jour de l'IHM
	 */
	public void stopper() {
		if (this.t != null)
			this.t.interrupt();
	}

	/**
	 * Mettre en pause l'IHM
	 */
	public void pause() {
		this.t.suspend();
	}

	/**
	 * Relancer l'IHM
	 */
	public void resume() {
		this.t.resume();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(Consts.MAJ_IHM_DELAI);
			} catch (InterruptedException e) {
				break;
			}

			repaint();
		}

		for (int i = 0; i < n; i++)
			this.etats[i] = null;

		repaint();
	}

	/**
	 * Méthode lancée pour "peindre" la table et son environnement.
	 * @param g Environnement graphique AWT
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform saveAT = g2d.getTransform();

		// Lissage des formes ...
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setRenderingHints(rh);

		this.majTailleTable();

		// Application de la transformation (centre de la fenêtre)
		g2d.transform(at);

		// On dessine la table ...
		g2d.setStroke(new BasicStroke(.15f));
		g2d.setPaint(Consts.COULEUR_TABLE);
		g2d.fill(this.formeTable);
		g2d.setPaint(Consts.COULEUR_BORDURE_TABLE);
		g2d.draw(this.formeTable);

		// ... les philosophes aussi ...
		g2d.setStroke(new BasicStroke(.04f));

		for (int i = 0; i < n; i++)
			this.dessinerPhilosophe(i, g2d);

		// ... et les baguettes !
		for (int i = 0; i < n; i++)
			this.dessinerBaguette(i, g2d);

		g2d.setTransform(saveAT);
	}

	/**
	 * Retourne la taille prédéfinie de l'espace de dessin de la table
	 * @return Dimension de l'espace de dessin
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}

}
