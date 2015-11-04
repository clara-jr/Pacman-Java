package es.upm.dit.adsw.pacman3;

public class Pared {
	private final Casilla casilla;
	private final Direccion direccion;
	
	/**
	 * @param casilla
	 * @param direccion
	 */
	public Pared(Casilla casilla, Direccion direccion) {
		this.casilla = casilla;
		this.direccion = direccion;
	}
	
	/** Getter.
	 * @return casilla
	 */
	public Casilla getCasilla() {
		return casilla;
	}

	/** Getter.
	 * @return direccion
	 */
	public Direccion getDireccion() {
		return direccion;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		Pared pared = (Pared) o;
		return casilla.equals(pared.casilla)&& direccion == pared.direccion; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return casilla.hashCode();
	}

}
