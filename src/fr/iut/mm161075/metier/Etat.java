package fr.iut.mm161075.metier;

/**
 * Représente un état d'un philosophe.
 *
 * @date 21/02/2018
 * @author Maxime MALGORN
 * @version 1.0
 */
public enum Etat {

	PENSE(0, "Pense"),
	A_FAIM(1, "À faim"),
	MANGE(2, "Mange");

	/**
	 * Indice lié à l'état
	 */
	private int indice;

	/**
	 * Chaîne pour identifier l'état
	 */
	private String chaine;

	Etat(int indice, String chaine) {
		this.indice = indice;
		this.chaine = chaine;
	}

	public int getIndice() {
		return indice;
	}

	@Override
	public String toString() {
		return this.chaine;
	}

	/**
	 * Récupération d'un état à partir d'un indice
	 * @param indice Indice de l'état à chercher
	 * @return Etat trouvé avec l'indice
	 */
	public static Etat getEtat(int indice) {
		for (Etat etat : Etat.values())
			if (etat.getIndice() == indice)
				return etat;

		return null;
	}

}
