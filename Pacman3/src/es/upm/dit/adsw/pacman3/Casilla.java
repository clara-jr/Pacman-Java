package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

public class Casilla {
	private int x, y;
	private Movil movil;
	private boolean objetivo = false;
	private Set<Direccion> paredes;
	private boolean trampa;
	private boolean llave;
	private Image imagen;
	
	/**
	 * @param x	coordenada x
	 * @param y	coordenada y
	 */
	public Casilla(int x, int y) {
		paredes = new HashSet<Direccion>();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter.
	 * @return imagen
	 */
	public Image getImagen() {
		return imagen;
	}

	/**
	 * Setter.
	 * @param imagen
	 */
	public void setImagen(Image imagen) {
		this.imagen = imagen;
	}
	
	/**
	 * Getter.
	 * @return true o false si la casilla es una trampa o no, respectivamente
	 */
	public boolean isTrampa() {
		return trampa;
	}

	/**
	 * Setter.
	 * @param trampa
	 */
	public void setTrampa(boolean trampa) {
		this.trampa = trampa;
	}

	/**
	 * Getter.
	 * @return true o false si la casilla es una llave o no, respectivamente
	 */
	public boolean isLlave() {
		return llave;
	}
	
	/**
	 * Setter.
	 * @param llave
	 */
	public void setLlave(boolean llave) {
		this.llave = llave;
	}
	
	/**
	 * Setter.
	 * @param x	coordenada x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Setter.
	 * @param y	coordenada y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Getter.
	 * @return coordenada x de la casilla
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter.
	 * @return coordenada y de la casilla
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Getter.
	 * @return movil presente en una casilla
	 */
	public Movil getMovil() {
		return movil;
	}

	/**
	 * Setter.
	 * @param movil
	 */
	public void setMovil(Movil movil) {
		this.movil = movil;
	}
	
	/**
	 * Pone una pared en la direccion determinada
	 * @param direccion
	 */
	public void ponPared(Direccion direccion) {
		paredes.add(direccion);
	}
	
	/**
	 * Quita una pared en la direccion determinada
	 * @param direccion
	 */
	public void quitaPared(Direccion direccion) {
		paredes.remove(direccion);
	}
	
	/**
	 * @param direccion
	 * @return true o false si hay pared o no en esa direccion
	 */
	public boolean hayPared(Direccion direccion) {
		return paredes.contains(direccion);
	}
	
	/**
	 * Quita todas las paredes de la casilla
	 */
	public void quitaParedes() {
		paredes.clear();
	}

	/**
	 * Getter.
	 * @return true o false si la casilla es una casilla objetivo o no, respectivamente
	 */
	public boolean isObjetivo() {
		return objetivo;
	}

	/**
	 * Setter.
	 * @param objetivo
	 */
	public void setObjetivo(boolean objetivo) {
		this.objetivo = objetivo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(java.lang.Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Casilla casilla = (Casilla) o;
		return (x == casilla.getX() && y == casilla.getY());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return paredes.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("(%d,  %d)", x, y);
	}
	
	/**
	 * @param destino	casilla a la que pretendo moverme
	 * @return direccion en la que hay que moverse para llegar a la casilla destino
	 */
	public Direccion getDireccion(Casilla destino) {
		if (destino!=null) {
			if (destino.getX() == this.getX()-1 && destino.getY() == this.getY()) return Direccion.OESTE;
			else if (destino.getX() == this.getX()+1 && destino.getY() == this.getY()) return Direccion.ESTE;
			else if (destino.getX() == this.getX() && destino.getY() == this.getY()-1) return Direccion.SUR;
			else if (destino.getX() == this.getX() && destino.getY() == this.getY()+1) return Direccion.NORTE;
		}
		return null;
	}
}
