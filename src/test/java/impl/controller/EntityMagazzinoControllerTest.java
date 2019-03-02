package impl.controller;

import static org.junit.Assert.*;

import om.EntityMagazzino;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * EntityMagazzinoControllerTest. Classe di test che permette di verificare il corretto funzionamento dei metodi della classe EntityMagazzinoController.
 *
 * @author Edoardo Baral
 */
@Ignore
public class EntityMagazzinoControllerTest
{
    /**
     * Metodo che permette di testare il costruttore della classe
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCostruttore() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        assertNotNull(controller);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo aggiungiProdotto()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testAggiungiProdotto() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Gallette 400 g");
        em4.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em4));

        // Caso prodotto già presente in magazzino
        EntityMagazzino result = controller.aggiungiProdotto(em3);
        assertNull(result);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo cancellaProdotto()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCancellaProdotto() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino result = controller.cancellaProdotto(em2);
        assertNotNull(result);

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Olio");
        em4.setGiacenza(18);

        EntityMagazzino resultNull = controller.cancellaProdotto(em4);
        assertNull(resultNull);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo cancellaProdotto(String nomeProdotto)
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCancellaProdottoPerNome() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino result = controller.cancellaProdotto("Asparagi 300 g");
        assertNotNull(result);

        EntityMagazzino resultNull = controller.cancellaProdotto("Olio");
        assertNull(resultNull);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo modificaProdotto()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testModificaProdotto() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Tonno in scatola 200 g");
        em4.setGiacenza(3);

        EntityMagazzino em5 = new EntityMagazzino();
        em5.setNome("Pomodori");
        em5.setGiacenza(9);

        EntityMagazzino result = controller.modificaProdotto(em4);
        assertNotNull(result);
        assertEquals(3, result.getGiacenza());

        EntityMagazzino resultNull = controller.modificaProdotto(em5);
        assertNull(resultNull);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo exists()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testExists() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Frollini 500 g");
        em4.setGiacenza(3);

        assertTrue(controller.exists(em2) >= 0);
        assertTrue(controller.exists(em4) < 0);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo scriviProdottiSuFile()
     * @throws IOException in caso di errori nella scrittura sul file CSV
     */
    @Test
    public void testScriviProdottiSuFile() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        controller.scriviProdottiSuFile();
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo mappingDaFile()
     * @throws IOException in caso di errori nella lettura del file CSV
     */
    @Test
    public void testMappingDaFile() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        controller.scriviProdottiSuFile();

        EntityMagazzinoController controller2 = new EntityMagazzinoController();
        controller2.mappingDaFile();
        assertEquals(0, controller2.exists("Asparagi 300 g"));
    }

    /**
     * Metodo che permette di testare incrementaGiacenza() e decrementaGiacenza()
     * @throws IOException in caso di errori nella lettura del file CSV
     */
    @Test
    public void testIncrementaDecrementaGiacenza() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        assertTrue(controller.incrementaGiacenza(em1, 2));
        assertTrue(controller.decrementaGiacenza(em3, 1));
    }

    /**
     * Metodo che permette di testare il metodo toString()
     * @throws IOException in caso di errori nella lettura del file CSV
     */
    @Test
    public void testToString() throws IOException
    {
        EntityMagazzinoController controller = new EntityMagazzinoController();
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);
        assertNotNull(controller.aggiungiProdotto(em1));

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);
        assertNotNull(controller.aggiungiProdotto(em2));

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em3));

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Gallette 400 g");
        em4.setGiacenza(3);
        assertNotNull(controller.aggiungiProdotto(em4));

        // Caso prodotto già presente in magazzino
        EntityMagazzino result = controller.aggiungiProdotto(em3);
        assertNull(result);

        String controllerToString = controller.toString();
        assertNotNull(controllerToString);
    }
}