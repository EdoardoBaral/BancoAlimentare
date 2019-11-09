package impl.controller.csv;

import impl.controller.csv.EntityRegistroCSVController;
import om.EntityRegistro;
import om.TipoTransazione;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * EntityRegistroCSVControllerTest. Classe di test che permette di verificare il corretto funzionamento dei metodi della classe EntityRegistroCSVController.
 *
 * @author Edoardo Baral
 */
@Ignore
public class EntityRegistroCSVControllerTest
{
    /**
     * Metodo che permette di testare il costruttore della classe
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testCostruttore() throws IOException
    {
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        assertNotNull(controller);
    }

    /**
     * Metodo che permette di testare il corretto funzionamento di exists()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testExists() throws IOException
    {
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(LocalDate.now());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(LocalDate.now());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(LocalDate.now());
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
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(LocalDate.now());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(LocalDate.now());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(LocalDate.now());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro er4 = new EntityRegistro();
        //er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(LocalDate.now());
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
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(LocalDate.now());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(LocalDate.now());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(LocalDate.now());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro result = controller.cancellaTransazione(1L);
        assertNotNull(result);

        EntityRegistro er4 = new EntityRegistro();
        //er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(LocalDate.now());
        er4.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro resultNull = controller.cancellaTransazione(10L);
        assertNull(resultNull);
    }

    @Test
    public void testScritturaLetturaFile() throws IOException
    {
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(LocalDate.now());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(LocalDate.now());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(LocalDate.now());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        controller.scriviProdottiSuFile();

        EntityRegistroCSVController controller2 = new EntityRegistroCSVController();
        controller2.mappingDaFile();
        int result = controller2.exists(1L);
        assertEquals(0, result);
    }

    /**
     * Metodo che permette di testare il corretto comportamento del metodo toString()
     * @throws IOException in caso di errori nell'apertura/creazione del file CSV
     */
    @Test
    public void testToString() throws IOException
    {
        EntityRegistroCSVController controller = new EntityRegistroCSVController();
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(LocalDate.now());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(LocalDate.now());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(LocalDate.now());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro er4 = new EntityRegistro();
        //er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(LocalDate.now());
        er4.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er4));

        String controllerToString = controller.toString();
        assertNotNull(controllerToString);
    }
}