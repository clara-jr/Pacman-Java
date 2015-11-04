package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.util.Random;

public class Fantasma00 extends Movil implements Runnable {
	
	private final Image imagen;
	private final Terreno terreno;
	private Casilla casilla;
	private boolean devorado;
	private boolean vivo;
	
	/**
	 * @param terreno
	 */
	public Fantasma00(Terreno terreno){
		this.terreno=terreno;
		imagen=loadImage("Cafe.png");
		devorado=false;
		vivo=true;
	}
	
	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#setCasilla(es.upm.dit.adsw.pacman3.Casilla)
	 */
	@Override
	public void setCasilla(Casilla casilla) {
		// TODO Auto-generated method stub
		this.casilla = casilla;
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
	 * @see es.upm.dit.adsw.pacman3.Movil#getImagen()
	 */
	@Override
	public Image getImagen() {
		// TODO Auto-generated method stub
		return imagen;
	}
	
	/* (non-Javadoc)
	 * @see es.upm.dit.adsw.pacman3.Movil#muere(boolean)
	 */
	@Override
	public void muere(boolean devorado) {
		if (devorado || terreno.getEstado() != EstadoJuego.JUGANDO) vivo=false;
		else vivo=true;
	}
	
	/* (non-Javadoc)
     * @see es.upm.dit.adsw.pacman3.Movil#puedoMoverme(es.upm.dit.adsw.pacman3.Direccion)
     */
	@Override
	public int puedoMoverme(Direccion direccion){
		// si estoy en una trampa, no puedo moverme temporalmente
	    Casilla siguiente = terreno.getCasilla(getCasilla().getX(), getCasilla().getY(), direccion);
    	if(this!=null && vivo && !getCasilla().isTrampa() && (siguiente!=null) && (!getCasilla().hayPared(direccion))) {
    		Movil movilcerca = siguiente.getMovil();
    		boolean esjugador = movilcerca instanceof Jugador;
    		if (movilcerca==null || (esjugador))
    			return 0;
    		if (!esjugador)
    			return 1;
    	}
    	return 2;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() { // coge una dirección aleatoria, pero sólo tiene el cuenta las direcciones a las que sabe que puede moverse
		while(vivo){
			Direccion[] direcciones = Direccion.values();
			
			int posibilidades = 0;
			for (int i=0; i<direcciones.length; i++) {
				if (puedoMoverme(direcciones[i])==0) {
					posibilidades++;
				}
			}
			int j = 0;
			Direccion direccionesposibles[] = new Direccion[posibilidades];
			for (int i=0; i<direcciones.length; i++) {
				if (puedoMoverme(direcciones[i])==0) {
					direccionesposibles[j] = direcciones[i];
					j++;
				}
			}
			
			Random random = new Random();
			
			if (posibilidades > 0) {
				int d = random.nextInt(direccionesposibles.length);
				Direccion direccion = direccionesposibles[d];
				terreno.move(this, direccion);
			}
			
			else {
				int d = random.nextInt(direcciones.length);
				Direccion direccion = direcciones[d];
				terreno.move(this, direccion);
			}
			
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException ignored){
			}
		}	
	}
}
