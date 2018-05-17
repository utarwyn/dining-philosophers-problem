package fr.iut.mm161075.ihm;

import fr.iut.mm161075.Consts;
import fr.iut.mm161075.Controleur;
import fr.iut.mm161075.metier.Etat;
import fr.iut.mm161075.metier.Philosophe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe de gestion de l'IHM.
 * Gère la fenêtre Swing et les boutons.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public class Frame extends JFrame implements IHM, ActionListener {

	/**
	 * Boutons de la fenêtre
	 */
	private JButton demarrer, stopper, pause, changerNbPhilo, aide;

	/**
	 * Classe de gestion de l'espace de la table
	 */
	private GraphicTable table;

	/**
	 * Classe Controleur principale
	 */
	private Controleur controleur;

	public Frame(Controleur controleur) {
		this.controleur = controleur;

		this.setTitle("Problème des philosophes");
		this.setMinimumSize(new Dimension(600, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialisation des composants
		this.demarrer = new JButton("Démarrer la simulation");
		this.stopper = new JButton("Arrêter la simulation");
		this.pause = new JButton("Mettre en pause");
		this.changerNbPhilo = new JButton("Changer Nb philosophes");
		this.aide = new JButton(new ImageIcon(getClass().getResource("/help.png")));

		this.aide.setBounds(6, 6, 24, 24);
		this.aide.setBackground(Consts.COULEUR_FOND_2);
		this.aide.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		this.stopper.setEnabled(false);
		this.pause.setEnabled(false);

		this.demarrer.addActionListener(this);
		this.stopper.addActionListener(this);
		this.pause.addActionListener(this);
		this.changerNbPhilo.addActionListener(this);
		this.aide.addActionListener(this);

		// Composants d'informations
		JLabel auteur = new JLabel("  Maxime MALGORN");
		auteur.setForeground(Color.white);

		JPanel legende = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		for (int i = 0; i < Consts.COULEUR_ETATS.length; i++)
			this.preparerLegende(legende, Etat.getEtat(i).toString(), Consts.COULEUR_ETATS[i]);

		legende.setOpaque(false);

		// Placement des composants dans l'interface
		Container c = new Container();
		c.setLayout(new FlowLayout());
		c.add(this.demarrer);
		c.add(this.stopper);
		c.add(this.pause);
		c.add(this.aide);

		Container c2 = new Container();
		c2.setLayout(new GridLayout(1, 3));
		c2.add(auteur);
		c2.add(this.changerNbPhilo);
		c2.add(legende);

		this.add(c, BorderLayout.NORTH);
		this.add(c2, BorderLayout.SOUTH);

		this.getContentPane().setBackground(Consts.COULEUR_FOND_2);
	}

	/**
	 * Démarrage de l'IHM
	 * @param n Nombre de philosophes
	 */
	@Override
	public void demarrer(int n) {
		if (this.table != null) {
			this.table.stopper();
			this.remove(this.table);
		}

		this.table = new GraphicTable(n);
		this.add(this.table);

		this.pack();
		this.setVisible(true);
	}

	/**
	 * Appelée pour mettre à jour l'IHM
	 * @param philo Philosophe à mettre à jour
	 */
	@Override
	public void miseAJour(Philosophe philo) {
		// On applique le changement d'état du philosophe ...
		this.table.getState()[philo.getI()] = philo.getEtat();

		// ... et on redessine la table !
		repaint();
	}

	/**
	 * Un clic a été fait sur un des boutons de l'interface
	 * @param e Evènement du clic
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.demarrer) {
			this.demarrer.setEnabled(false);
			this.stopper.setEnabled(true);
			this.pause.setEnabled(true);

			this.table.demarrer();
			this.controleur.demarrerSimulation();
		} else if (e.getSource() == this.pause) {
			boolean pause = this.controleur.isSimulationPause();

			if (pause) {
				this.controleur.resumeSimulation();
				this.table.resume();

				this.pause.setText("Mettre en pause");
			} else {
				this.controleur.pauseSimulation();
				this.table.pause();

				this.pause.setText("Reprendre");
			}
		} else if (e.getSource() == this.changerNbPhilo) {
			String message = "Combien de philosophes ?";
			String text = JOptionPane.showInputDialog(this, message);
			int n;

			try {
				n = Integer.parseInt(text);
			} catch (Exception ex) {
				return;
			}

			n = Math.max(Math.min(n, 30), 3);

			// Réinitialisation des boutons
			this.demarrer.setEnabled(true);
			this.stopper.setEnabled(false);
			this.pause.setEnabled(false);

			// Réinitialisation du métier et de l'IHM
			this.controleur.stopperSimulation();
			this.controleur.demarrer(n);
		} else if (e.getSource() == this.stopper) {
			this.demarrer.setEnabled(true);
			this.stopper.setEnabled(false);
			this.pause.setEnabled(false);

			this.table.stopper();
			this.controleur.stopperSimulation();
		} else if (e.getSource() == this.aide) {
			JOptionPane.showMessageDialog(
					this,
					"Cinq philosophes se trouvent autour d’une table et chacun\n" +
							"a devant lui un plat de nourriture. Étant en pleine pensée\n" +
							"philosophique, les philosophes ont besoin d’énergie,\n" +
							"et ils ont faim. Pour manger, ils ont besoin de deux baguettes,\n" +
							"une à gauche et une à droite de chacun d’entre eux :\n" +
							"elles sont donc partagées (chacune pour deux philosophes).",
					"Explications du problème",
					JOptionPane.INFORMATION_MESSAGE
			);
		}
	}

	/**
	 * Prépare une légende d'état en bas à droite de la fenêtre
	 * @param panLegende Panel Swing qui va contenir les légendes
	 * @param texte Texte associé à la légende
	 * @param couleur Couleur associée à la légende
	 */
	private void preparerLegende(JPanel panLegende, String texte, Color couleur) {
		JPanel carre = new JPanel();
		JLabel label = new JLabel(texte + "  ");

		carre.setBackground(couleur);
		label.setForeground(couleur);

		panLegende.add(carre);
		panLegende.add(label);
	}

}
