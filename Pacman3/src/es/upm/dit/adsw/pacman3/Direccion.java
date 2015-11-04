package es.upm.dit.adsw.pacman3;

public enum Direccion {
	NORTE, SUR, ESTE, OESTE;
	
	/**
	 * @return direccion opuesta
	 */
	public Direccion opuesta () {
		switch(this) {
		case NORTE:
			return SUR;
		case SUR:
			return NORTE;
		case ESTE:
			return OESTE;
		case OESTE:
			return ESTE;
		default: return null;
		}
	}	
}
