package es.upm.dit.adsw.pacman3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Bateria de pruebas sobre el algoritmo de busqueda.
 */
public class TestDepredador {
    public static final int N = 5;

    private Terreno terreno;
    private Depredador depredador;

    @Before
    public void setup() {
        terreno = new Terreno(N);
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                Casilla casilla = terreno.getCasilla(x, y);
                terreno.ponPared(casilla, Direccion.NORTE);
                terreno.ponPared(casilla, Direccion.SUR);
                terreno.ponPared(casilla, Direccion.ESTE);
                terreno.ponPared(casilla, Direccion.OESTE);
            }
        }
        terreno.quitaPared(terreno.getCasilla(0, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(1, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(1, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(3, 0), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(4, 0), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(0, 1), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(0, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 1), Direccion.NORTE);
        terreno.quitaPared(terreno.getCasilla(2, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(3, 1), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(0, 2), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(1, 2), Direccion.ESTE);
        terreno.quitaPared(terreno.getCasilla(2, 2), Direccion.NORTE);

        depredador = new Depredador(terreno);
    }

	@Test // todo recto hacia el NORTE/SUR, sin paredes y sin casillas de por medio
    public void testBFS01() {
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 1);
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 0), depredador.bfs(b, a));
    }
	
	@Test // todo recto hacia el ESTE/OESTE, sin paredes y sin casillas de por medio
    public void testBFS02() {
        Casilla a = terreno.getCasilla(1, 1);
        Casilla b = terreno.getCasilla(0, 1);
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(1, 1), depredador.bfs(b, a));
    }

	@Test // todo recto hacia el NORTE/SUR sin paredes, con una casilla de por medio
    public void testBFS03() {
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 2);
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(b, a));
    }
	
	@Test // todo recto hacia el ESTE/OESTE sin paredes, con una casilla de por medio
    public void testBFS04() {
        Casilla a = terreno.getCasilla(2, 2);
        Casilla b = terreno.getCasilla(0, 2);
        assertEquals(terreno.getCasilla(1, 2), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(1, 2), depredador.bfs(b, a));
    }

	@Test // no se puede ir a la casilla destino porque ésta esta inhabilitada por paredes
    public void testBFS05() {
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 3);
        assertNull(depredador.bfs(a, b));
        assertNull(depredador.bfs(b, a));
    }
	
	@Test // no se puede ir a la casilla destino, pero tampoco a sus adyacentes
    public void testBFS06() {
        Casilla a = terreno.getCasilla(0, 0);
        Casilla b = terreno.getCasilla(0, 4);
        assertNull(depredador.bfs(a, b));
        assertNull(depredador.bfs(b, a));
    }
	
	@Test // no se puede ir a la casilla destino ni a ninguna otra casilla porque tanto origen como destino están inhabilitadas por paredes
    public void testBFS07() {
        Casilla a = terreno.getCasilla(0, 4);
        Casilla b = terreno.getCasilla(4, 4);
        assertNull(depredador.bfs(a, b));
        assertNull(depredador.bfs(b, a));
    }
	
	@Test // camino separado por paredes donde la primera direccion a la que hay que moverse es al OESTE o al SUR
    public void testBFS08() {
        Casilla a = terreno.getCasilla(2, 3);
        Casilla b = terreno.getCasilla(1, 1);
        assertEquals(terreno.getCasilla(2, 2), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(0, 1), depredador.bfs(b, a));
    }
	
	@Test // camino separado por paredes donde la primera direccion a la que hay que moverse es al ESTE o al NORTE
    public void testBFS09() {
        Casilla a = terreno.getCasilla(1, 2);
        Casilla b = terreno.getCasilla(4, 0);
        assertEquals(terreno.getCasilla(2, 2), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(4, 1), depredador.bfs(b, a));
    }
	
	@Test // camino separado por paredes, donde dos caminos son igual de óptimos
    public void testBFS10() {
        Casilla a = terreno.getCasilla(1, 0);
        Casilla b = terreno.getCasilla(2, 1);
        assertEquals(terreno.getCasilla(1, 1), depredador.bfs(a, b));
        assertEquals(terreno.getCasilla(2, 2), depredador.bfs(b, a));
    }
}
