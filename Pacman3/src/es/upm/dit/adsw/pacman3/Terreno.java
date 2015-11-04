package es.upm.dit.adsw.pacman3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.upm.dit.adsw.pacman3.Casilla;
import es.upm.dit.adsw.pacman3.Direccion;
import es.upm.dit.adsw.pacman3.Movil;

public class Terreno extends java.lang.Object {
	
	private Casilla[][] casillas;
	private EstadoJuego estado;
	private View view;

	/**
	 * @param n número de filas o columnas del terreno
	 */
	public Terreno(int n) {
		casillas = new Casilla[n][n];
		for (int x=0; x<n ;x++) {
			for (int y=0; y<n; y++) {
				casillas[x][y] = new Casilla(x, y);
			}
		}
	}
	
	/**
	 * Setter.
	 * @param estado	estado del juego
	 */
	public synchronized void setEstado(EstadoJuego estado) {
		this.estado = estado;
	}
	
	/**
	 * Getter
	 * @return estado del juego
	 */
	public synchronized EstadoJuego getEstado(){
    	return estado;   
    }
	
	/**
	 * Quita todas las paredes del terreno
	 */
	public synchronized void quitaParedes(){
		for (int i=0;i<getN();i++){
			for (int j= 0; j <getN(); j++){
		        casillas[i][j].quitaParedes();
			}
		}	
	}
	
	/**
	 * Pone las paredes del terreno
	 */
	public void ponParedes() { 
		 int N = getN(); 
		 Random random=new Random();
		 Direccion[] direcciones = Direccion.values(); 
		 // pongo todas las paredes 
		 for (int x=0; x<N; x++) { 
		 for (int y=0; y<N; y++) { 
		 Casilla casilla = getCasilla(x,y); 
		 for (Direccion direccion: direcciones) { 
		 ponPared(casilla, direccion); 
		 } 
		 } 
		 } 
		 // quito el mínimo para interconectar todo 
		 List<Casilla> conectadas = new ArrayList<Casilla>(); 
		 List<Pared> paredes = new ArrayList<Pared>(); 
		 int x0 = random.nextInt(N); 
		 int y0 = random.nextInt(N); 
		 Casilla casilla0 = getCasilla(x0, y0); 
		 conectadas.add(casilla0); 
		 for (Direccion direccion : direcciones) 
		 paredes.add(new Pared(casilla0, direccion)); 
		 while (paredes.size() > 0) { 
		 int i = random.nextInt(paredes.size()); 
		 Pared pared = paredes.remove(i); 
		 Casilla origen = pared.getCasilla(); 
		 Direccion direccion = pared.getDireccion(); 
		 Casilla destino = 
		 getCasilla(origen.getX(),origen.getY(),direccion); 
		 if (destino != null && !conectadas.contains(destino)) { 
		 quitaPared(origen, direccion); 
		 conectadas.add(destino); 
		 for (Direccion d : direcciones) { 
		 if (destino.hayPared(d)) 
		 paredes.add(new Pared(destino,d)); 
		
		 } 
		 } 
		 } 
		 // quito unas cuantas paredes más para abrir rutas alternativas 
		 for (int q = 0; q < 2 * N; q++) { 
		 int x = random.nextInt(N); 
		 int y = random.nextInt(N); 
		 Casilla casilla = getCasilla(x,y); 
		 Direccion direccion = 
		 direcciones[random.nextInt(direcciones.length)]; 
		 quitaPared(casilla, direccion); 
		 } 
	}
	
	/**
	 * @return dimensión del terreno, es decir, el número de filas o columnas que tiene
	 */
	public synchronized int getN() {
		return casillas.length;
	}
	
	/**
	 * @param x	coordenada x
	 * @param y	coordenada y
	 * @return	casilla cuyas coordenadas son x e y
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public synchronized Casilla getCasilla(int x, int y) throws ArrayIndexOutOfBoundsException {
		if (x>(getN()-1) || y>(getN()-1) || x<0 || y<0) throw new java.lang.ArrayIndexOutOfBoundsException ("coordenadas fuera del tablero");
		else if (casillas == null) return null;
		return casillas[x][y];
	}
	
	/**
	 * @param movil		movil que quiere moverse
	 * @param direccion	direccion en la que quiere moverse
	 * @return casilla a la que se ha movido el movil
	 */
	public synchronized Casilla move(Movil movil, Direccion direccion) {
		view.pintame();
		if (movil!=null && movil.getCasilla()!=null && direccion!=null) {
			int puedomoverme = movil.puedoMoverme(direccion);
			Casilla casilla = movil.getCasilla();
			while (!(casilla.getMovil() instanceof Jugador) && casilla.isTrampa()) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			notifyAll();
			Casilla casillasiguiente = getCasilla(casilla.getX(),casilla.getY(),direccion);
			 
			if(casillasiguiente!=null && casillasiguiente.getMovil()!=null && !casilla.hayPared(direccion) &&
					!casillasiguiente.isTrampa() &&
					((casilla.getMovil() instanceof Jugador) ||
					(casillasiguiente.getMovil() instanceof Jugador))) {
				movil.muere(true);
				paraMoviles();
				view.muestra("¡Ouch! ¡Te has despertado!");
			}
			
			if (puedomoverme==0) {
				casilla.setMovil(null);
				movil.setCasilla(casillasiguiente);
				casillasiguiente.setMovil(movil);
				if(casillasiguiente.isObjetivo() && casillasiguiente.getMovil() instanceof Jugador) {
					estado=EstadoJuego.GANA_JUGADOR;
					paraMoviles();
					view.muestra("¡Enhorabuena! ¡Has conseguido llegar a la cama y seguir durmiendo!");
				}
				return casillasiguiente;
			}
			return casilla;
		}
		return movil.getCasilla();
	}
	
	/**
	 * @param x	coordenada x
	 * @param y	coordenada y
	 * @param direccion	direccion en la que estoy mirando
	 * @return casilla adyacente en una direccion dada
	 */
	public synchronized Casilla getCasilla(int x, int y, Direccion direccion) {
		if (casillas == null || (x > casillas[0].length - 1) || (y > casillas[0].length - 1)) return null;
		if (y!=0 && direccion==Direccion.SUR) { y = y-1; return getCasilla(x,y); }
		else if (y!=(casillas.length-1) && direccion==Direccion.NORTE) { y = y+1; return getCasilla(x,y); }
		else if (x!=(casillas.length-1) && direccion==Direccion.ESTE) { x = x+1; return getCasilla(x,y); }
		else if (x!=0 && direccion==Direccion.OESTE) { x = x-1; return getCasilla(x,y); }
		else return null; 
	}
	
	/**
	 * @param casilla	casilla de la cual quiero devolver la adyacente
	 * @param direccion	direccion en la que estoy mirando
	 * @return
	 */
	public synchronized Casilla getCasilla(Casilla casilla, Direccion direccion) {
		return getCasilla(casilla.getX(), casilla.getY(), direccion);
	}
	
	/**
	 * Pone una pared en una casilla según la direccion dada, y en la casilla adyacente en la direccion opuesta
	 * @param casilla
	 * @param direccion
	 */
	public synchronized void ponPared(Casilla casilla, Direccion direccion) {
		casilla.ponPared(direccion);
		if (getCasilla(casilla.getX(), casilla.getY(), direccion)!=null)
				getCasilla(casilla.getX(), casilla.getY(), direccion).ponPared(direccion.opuesta());
	}
	
	/**
	 * Quita una pared en una casilla según la direccion dada, y en la casilla adyacente en la direccion opuesta
	 * @param casilla
	 * @param direccion
	 */
	public synchronized void quitaPared(Casilla casilla, Direccion direccion) {
		casilla.quitaPared(direccion);
		if (getCasilla(casilla.getX(), casilla.getY(), direccion)!=null)
				getCasilla(casilla.getX(), casilla.getY(), direccion).quitaPared(direccion.opuesta());
	}
	
	/**
	 * @param casilla
	 * @param direccion
	 * @return	true si hay pared en la casilla en esa dirección; de lo contrario, devuelve false
	 */
	public synchronized boolean hayPared(Casilla casilla, Direccion direccion) {
		if ((direccion==Direccion.SUR && casilla.getY()==0) ||
			(direccion==Direccion.NORTE && casilla.getY()==(getN()-1)) ||
			(direccion==Direccion.ESTE && casilla.getX()==(getN()-1)) ||
			(direccion==Direccion.OESTE && casilla.getX()==0)) return true;
		return casilla.hayPared(direccion);
	}
	
	/**
	 * Pone un movil en el terreno
	 * @param x	coordenada x
	 * @param y	coordenada y
	 * @param movil
	 * @return Pone un movil en el terreno y devuelve true si lo consigue
	 */
	public synchronized boolean put(int x, int y, Movil movil) {
		try {
			Casilla casilla = getCasilla(x,y);
			if (casilla.getMovil() != null)
				return false;
			casilla.setMovil(movil);
			movil.setCasilla(casilla);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Establece la casilla objetivo según las coordenadas dadas
	 * @param x	coordenada x
	 * @param y	coordenada y
	 * @return establece el objetivo y devuelve true si lo consigue
	 */
	public synchronized boolean setObjetivo(int x, int y) {
		if (x>(getN()-1) || y>(getN()-1) || x<0 || y<0) return false;
		casillas[x][y].setObjetivo(true);
		return true;
	}
	
	/**
	 * Reinicia el juego.
	 * Quita todas las trampas y llaves del terreno.
	 * Elimina todos los moviles del terreno.
	 * Pone las paredes al laberinto.
	 * Establece el objetivo.
	 * Coloca al jugador en el origen.
	 */
	public synchronized void ponSituacionInicial() {
		estado=EstadoJuego.JUGANDO;
		Thread.interrupted();
		quitaTrampasLlaves();
		limpiaTerreno();
		ponParedes();
		setObjetivo(getN()-1,getN()-1);
		Jugador	jugador	=	new	Jugador(this);	
		put(0,	0,	jugador);
		view.setJugador(jugador);
	}
	
	/**
	 * Elimina todos los moviles del terreno
	 */
	public synchronized void limpiaTerreno() {
		quitaParedes();
		for (int i=0; i<getN(); i++) {
			for (int j=0; j<getN(); j++) {
				if(casillas[i][j].getMovil()!=null) casillas[i][j].getMovil().muere(true);
				casillas[i][j].setMovil(null);
			}
		}
	}
	
	/**
	 * Para todos los moviles del terreno
	 */
	public synchronized void paraMoviles() {
		for (int i=0; i<getN(); i++) {
			for (int j=0; j<getN(); j++) {
				if(casillas[i][j].getMovil()!=null) 
					casillas[i][j].getMovil().muere(true);
			}
		}
	}

	/**
	 * Setter.
	 * @param view
	 */
	public synchronized void setView(View view) {
		this.view = view;
	}
	
	/**
	 * Quita todas las trampas y llaves del terreno
	 */
	public synchronized void quitaTrampasLlaves() {
		// recorre todas las casillas quitando trampas y llaves
		for (int i=0; i<getN(); i++) {
			for (int j=0; j<getN(); j++) {
				if (casillas[i][j].isTrampa()) casillas[i][j].setTrampa(false);
				if (casillas[i][j].isLlave()) casillas[i][j].setLlave(false);
			}
		}
	}
}
