package impl.controller;

import om.EntityRegistro;
import om.TipoTransazione;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * EntityRegistroControllerTest. Classe di test che permette di verificare il corretto funzionamento dei metodi della classe EntityRegistroController.
 *
 * @author Edoardo Baral
 */
public class EntityRegistroControllerTest
{
    /**
     * Metodo che permette di testare il costruttore della classe
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCostruttore() throws IOException
    {
        EntityRegistroController controller = new EntityRegistroController();
        assertNotNull(controller);
    }

    /**
     * Metodo che permette di testare il corretto funzionamento di exists()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testExists() throws IOException
    {
        EntityRegistroController controller = new EntityRegistroController();
        EntityRegistro er1 = new EntityRegistro();
        er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        int result = controller.exists(1L);
        assertTrue(result == 0);

        result = controller.exists(10L);
        assertTrue(result < 0);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo aggiungiProdotto()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testAggiungiTransazione() throws IOException
    {
        EntityRegistroController controller = new EntityRegistroController();
        EntityRegistro er1 = new EntityRegistro();
        er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro er4 = new EntityRegistro();
        er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(new DateTime());
        er4.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er4));
    }

    /**
     * Metodo che permette di verificare il corretto funzionamento del metodo cancellaTransazione()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCancellaTransazione() throws IOException
    {
        EntityRegistroController controller = new EntityRegistroController();
        EntityRegistro er1 = new EntityRegistro();
        er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro result = controller.cancellaTransazione(1L);
        assertNotNull(result);

        EntityRegistro er4 = new EntityRegistro();
        er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(new DateTime());
        er4.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro resultNull = controller.cancellaTransazione(10L);
        assertNull(resultNull);
    }

    @Test
    public void testScritturaLetturaFile() throws IOException
    {
        EntityRegistroController controller = new EntityRegistroController();
        EntityRegistro er1 = new EntityRegistro();
        er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        controller.scriviProdottiSuFile();

        EntityRegistroController controller2 = new EntityRegistroController();
        int result = controller2.exists(1L);
        assertEquals(0, result);
    }

}