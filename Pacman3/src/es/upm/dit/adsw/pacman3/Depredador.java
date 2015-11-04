package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Depredador extends Movil implements Runnable {
	// Cabe destacar que si el fantasma encuentra un fantasma atrapado en su camino,
	// espera a que éste se libere para poder avanzar,
	// pero no cambia su ruta ni devuelve una casilla null por no encontrar el camino
	private final java.awt.Image imagen;
	private final Terreno terreno;
	private Casilla casilla;
	private boolean devorado;
	private boolean vivo;
	List<Casilla> pendientes;
	Map<Casilla, Casilla> visitadas;
	
	/**
	 * @param terreno
	 */
	public Depredador(Terreno terreno) {
		this.terreno = terreno;
		imagen=loadImage("Despertador.png");
		devorado=false;
		vivo=true;
	}
    
    /**
     * @return casilla en la que está el jugador
     */
    private Casilla getCasillaDestino() { // busca en todas las casillas y devuelve donde esta el jugador
		Casilla casilladestino = null;
		for (int i=0; i<terreno.getN(); i++) {
			for (int j=0; j<terreno.getN(); j++) {
				if (terreno.getCasilla(i,j).getMovil() != null && terreno.getCasilla(i,j).getMovil() instanceof Jugador) {
					casilladestino = terreno.getCasilla(i,j);
				}
			}
		}
		return casilladestino;
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
	 * @see es.upm.dit.adsw.pacman3.Movil#muere(boolean)
	 */
	@Override
	public void muere(boolean devorado) {
		// TODO Auto-generated method stub
		if (devorado || terreno.getEstado() != EstadoJuego.JUGANDO) vivo=false;
		else vivo=true;
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

    /**
     * @param origen	casilla de partida
     * @param destino	casilla a la que pretendo ir
     * @return primera celda de la ruta más corta al destino. Devuelve null si no hay ruta posible.
     */
    public Casilla bfs(Casilla origen, Casilla destino) {
    	// primera celda de la ruta mas corta al destino. Devuelve NULL si no hay ruta posible.
    	pendientes = new ArrayList<Casilla>();
		visitadas = new HashMap<Casilla, Casilla>();
		pendientes.add(origen);
		boolean ruta = bfs(destino, pendientes, visitadas);
		if (ruta) {
			Casilla c = destino;
			while ((c != null) && (origen != null) && (visitadas.containsKey(c)) && !(visitadas.get(c).equals(origen)))
					c = visitadas.get(c);	
			return c;
		}
		else return null;
    }


    /**
     * @param destino		casilla a la que pretendo ir
     * @param pendientes	lista con las casillas pendientes de visitar
     * @param visitadas		mapa con las casillas que ya han sido visitadas
     * @return	true si se puede llegar al destino; de lo contrario, false.
     */
    private boolean bfs(Casilla destino, List<Casilla> pendientes, Map<Casilla, Casilla> visitadas) {
    	/* si no hay pendientes, devuelve FALSE
		 * saca la primera casilla pendiente: c1
		 * si c1 es el destino, devuelve TRUE
		 */
    	if (pendientes.isEmpty()) return false;
		while (!pendientes.isEmpty()) {
			Casilla c1 = pendientes.get(0);
			if (c1 != null && destino!=null) {
				if (c1.equals(destino)) return true;
				if (visitadas.containsKey(destino)) return true;
				for (Direccion direccion : Direccion.values()) {
					Casilla casilla = terreno.getCasilla(c1, direccion);
					// si hay casilla en esa direccion y no esta en las visitadas
					if (casilla!=null && !c1.hayPared(direccion) && !casilla.hayPared(direccion.opuesta()) && !visitadas.containsKey(casilla)) {
						visitadas.put(casilla, c1);
						pendientes.add(casilla);
						if (casilla.equals(destino)) return true;
					}
				}
				pendientes.remove(0);
			}
			else return false;
		}
		return false;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
	public void run() {
		// TODO Auto-generated method stub
		/* El fantasma se activa.
		 * Sugerencia: mientras esta vivo, elige moverse a la primera casilla de la ruta mas corta para alcanzar al jugador en la posicion actual.
		 * Luego duerme DELAY secs y vuelve a moverse.
		 */
		while (vivo) {
			Casilla destino = getCasillaDestino();
			Casilla optima = bfs(casilla, destino);
			Direccion d = casilla.getDireccion(optima);
			terreno.move(this,d);
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException ignored){
				
			}
		}
	}
}
