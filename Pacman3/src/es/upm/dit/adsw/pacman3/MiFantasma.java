package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.util.Random;

public class MiFantasma extends Movil implements Runnable {
	
	/* Este nuevo Fantasma evita las trampas para no caer en ellas.
	 * Además, este nuevo Fantasma se mueve aleatoriamente, como el Fantasma00,
	 * pero una vez se encuentre en una casilla adyacente a la del jugador,
	 * si se puede mover hacia ella, se mueve y le come.
	 * Cuando este fantasma te come, se transforma en la calavera de la muerte.
	 */
	
	private Image imagen;
	private final Terreno terreno;
	private Casilla casilla;
	private boolean devorado;
	private boolean vivo;
	
	/**
	 * @param terreno
	 */
	public MiFantasma (Terreno terreno){
		this.terreno=terreno;
		imagen=loadImage("Ducha.png");
		devorado=false;
		vivo=true;
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
		// este nuevo Fantasma evita las trampas para no caer en ellas
	    Casilla siguiente = terreno.getCasilla(getCasilla().getX(), getCasilla().getY(), direccion);
    	if(this!=null && vivo && (siguiente!=null) && !siguiente.isTrampa() && !siguiente.isObjetivo() && (!terreno.hayPared(getCasilla(), direccion))) {
    		Movil movilcerca = siguiente.getMovil();
    		boolean esjugador = movilcerca instanceof Jugador;
    		if (movilcerca==null || (esjugador))
    			return 0;
    		if (!esjugador)
    			return 1;
    	}
    	return 2;
	}
	
	/**
	 * @return casilla en la que está el jugador
	 */
	private Casilla getCasillaJugador() { // busca en todas las casillas y devuelve donde esta el jugador
		Casilla casillajugador = null;
		for (int i=0; i<terreno.getN(); i++) {
			for (int j=0; j<terreno.getN(); j++) {
				if (terreno.getCasilla(i,j).getMovil() != null && terreno.getCasilla(i,j).getMovil() instanceof Jugador)
					casillajugador = terreno.getCasilla(i,j);
			}
		}
		return casillajugador;
	}
	
	/**
	 * @param adyacente	casilla de la cual quiero comprobar si es adyacente o no a casilla
	 * @param casilla	casilla desde la que observo a las posibles adyacentes
	 * @return true si la casilla "adyacente" es adyacente a la casilla "casilla", y además, si el fantasma puede moverse de dicha casilla a la adyacente en cuestión.
	 */
	private boolean adyacente (Casilla adyacente, Casilla casilla) {
		/* este método devuelve true si la casilla "adyacente" es adyacente a la casilla "casilla",
		 * y además, si el fantasma puede moverse de dicha casilla a la adyacente en cuestión.
		 */
		if (adyacente.getX() == casilla.getX() - 1 && adyacente.getY() == casilla.getY() &&
			puedoMoverme(Direccion.OESTE)==0) return true;
		else if (adyacente.getX() == casilla.getX() + 1 && adyacente.getY() == casilla.getY() &&
			puedoMoverme(Direccion.ESTE)==0) return true;
		else if (adyacente.getX() == casilla.getX() && adyacente.getY() == casilla.getY() - 1 &&
			puedoMoverme(Direccion.SUR)==0) return true;
		else if (adyacente.getX() == casilla.getX() && adyacente.getY() == casilla.getY() + 1 &&
			puedoMoverme(Direccion.NORTE)==0) return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() { 
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
				if (adyacente(getCasillaJugador(), this.casilla)) { // si está al lado del jugador, le come
					imagen=loadImage("Calavera.png");
					Direccion d = casilla.getDireccion(getCasillaJugador());
					terreno.move(this,d);
				}
				else { // coge una dirección aleatoria, pero sólo tiene el cuenta las direcciones a las que sabe que puede moverse
					int d = random.nextInt(direccionesposibles.length);
					Direccion direccion = direccionesposibles[d];
					terreno.move(this, direccion);	
				}
			}
			
			else {
				int d = random.nextInt(direcciones.length);
				Direccion direccion = direcciones[d];
				terreno.move(this, direccion);
			}
			
			try{
				Thread.sleep(300);
			}
			catch(InterruptedException ignored){
			}
		}	
	}
}
