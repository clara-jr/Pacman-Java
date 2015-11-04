package es.upm.dit.adsw.pacman3;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public abstract class Movil {

	/**
	 * @param direccion	direccion en la que pretendo moverme
	 * @return un número entero de valor 0, 1, 2 según pueda moverme, o deba esperar, o no pueda moverme, respectivamente
	 */
	public abstract int puedoMoverme(Direccion direccion);

	/**
	 * Este método pone vivo a false si devorado es true
	 * @param devorado
	 */
	public abstract void muere(boolean devorado);

	/**
	 * Setter.
	 * @param casilla
	 */
	public abstract void setCasilla(Casilla casilla);

	/**
	 * Getter.
	 * @return casilla
	 */
	public abstract Casilla getCasilla();

	/**
	 * Getter.
	 * @return imagen
	 */
	public abstract Image getImagen();
	
	/**
	 * @param fichero
	 * @return imagen cuyo nombre es el dado por el String fichero
	 */
	protected Image loadImage(String fichero) {
		String path = "imgs/" + fichero;
		Class<Juego> root = Juego.class;
		try {
			URL url = root.getResource(path);
			ImageIcon icon = new ImageIcon(url);
			return icon.getImage();
		} catch (Exception e) {
			System.err.println("no se puede cargar "
					   +root.getPackage().getName()
					   +System.getProperty("file.separator")
					   +path);
			return null;
		}
	}

}
