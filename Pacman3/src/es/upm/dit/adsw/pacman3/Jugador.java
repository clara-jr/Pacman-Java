package es.upm.dit.adsw.pacman3;

import java.awt.Image;

import es.upm.dit.adsw.pacman3.Direccion;

public class Jugador extends Movil {
	private Image imagen = loadImage("Dormilon.png");
	private Casilla casilla;
	private boolean devorado=false;
	private Terreno terreno;
	
	/**
	 * @param terreno
	 */
	public Jugador(Terreno terreno) {
		this.terreno = terreno;
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#getImagen()
	 */
	@Override
	public Image getImagen() {
		// TODO Auto-generated method stub
		return imagen;
	}

	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#getCasilla()
	 */
	@Override
	public Casilla getCasilla() {
		// TODO Auto-generated method stub
		return casilla;
	}
	
	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#setCasilla(es.upm.dit.adsw.pacman3.Casilla)
	 */
	@Override
	public void setCasilla(Casilla casilla) {
		this.casilla = casilla;
		if (casilla.isObjetivo()) terreno.setEstado(EstadoJuego.GANA_JUGADOR);
		// si es llave, se quitan trampas y llaves del terreno
		if (casilla.isLlave()) terreno.quitaTrampasLlaves();
	}
	
	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#muere(boolean)
	 */
	@Override
	public void muere(boolean devorado) {
		if (devorado)
	    	  terreno.setEstado(EstadoJuego.PIERDE_JUGADOR);
	}

	/* (non-Javadoc)
     * @see es.upm.dit.adsw.pacman3.Movil#puedoMoverme(es.upm.dit.adsw.pacman3.Direccion)
     */
	@Override
	public int puedoMoverme(Direccion direccion){
		if (direccion!=null && getCasilla()!=null && !getCasilla().hayPared(direccion) && 
				(terreno.getCasilla(this.getCasilla().getX(),this.getCasilla().getY(), direccion) != null) && 
				(!devorado) && terreno.getEstado()==EstadoJuego.JUGANDO &&
				!(terreno.getCasilla(this.getCasilla().getX(),this.getCasilla().getY(), direccion).isTrampa() &&
				terreno.getCasilla(this.getCasilla().getX(),this.getCasilla().getY(), direccion).getMovil() != null))
			return 0;
		else return 2;
	}
	
}
