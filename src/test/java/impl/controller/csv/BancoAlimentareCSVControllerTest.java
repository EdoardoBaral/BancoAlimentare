package impl.controller.csv;

import impl.controller.csv.BancoAlimentareCSVController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.apache.log4j.BasicConfigurator;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * BancoAlimentareCSVControllerTest. Classe che permette di testare i metodi della classe BancoAlimentareCSVController.
 *
 * @author Edoardo Baral
 */
@Ignore
public class BancoAlimentareCSVControllerTest
{
    /**
     * Metodo che permette di testare il metodo inizializza()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void inizializzaTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        controller.inizializza();
        assertFalse(controller.getNomiProdotti().isEmpty());
    }

    /**
     * Metodo che permette di testare il metodo caricaSuFile()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void caricaSuFileTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        controller.inizializza();
        controller.caricaSuFile();
    }

    /**
     * Metodo che permette di testare il metodo preleva()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void prelevaTest() throws IOException, InterruptedException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        controller.inizializza();
        boolean result = controller.preleva("Asparagi 300 g", 1, "Mario Rossi");
        assertTrue(result);
        controller.caricaSuFile();
    }

    /**
     * Metodo che permette di testare il metodo deposita()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void depositaTest() throws IOException, InterruptedException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        controller.inizializza();
        boolean result = controller.deposita("Asparagi 300 g", 1);
        assertTrue(result);
        controller.caricaSuFile();
    }

    /**
     * Metodo che permette di testare il metodo aggiungiProdotto()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void aggiungiProdottoTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();

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
    }

    /**
     * Metodo che permette di testare il metodo cancellaProdotto()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void cancellaProdottoTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        initControllerMagazzino(controller);

        EntityMagazzino result = controller.cancellaProdotto("Asparagi 300 g");
        assertNotNull(result);
    }

    /**
     * Metodo che permette di testare il metodo cancella prodotto()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void cancellaProdottoTest_2() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        initControllerMagazzino(controller);

        EntityMagazzino result = controller.cancellaProdotto("Olio");
        assertNull(result);
    }

    /**
     * Metodo che permette di testare il metodo modificaProdotto()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void modificaProdottoTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        initControllerMagazzino(controller);

        EntityMagazzino em = new EntityMagazzino();
        em.setNome("Tonno in scatola 200 g");
        em.setGiacenza(3);

        EntityMagazzino result = controller.modificaProdotto(em);
        assertNotNull(result);
        assertEquals(3, result.getGiacenza());
    }

    /**
     * Metodo che permette di testare il metodo exists()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void existsTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        initControllerMagazzino(controller);

        EntityMagazzino em = new EntityMagazzino();
        em.setNome("Frollini 500 g");
        em.setGiacenza(3);

        assertTrue(controller.exists("Asparagi 300 g") >= 0);
        assertTrue(controller.exists(em) < 0);
    }

    /**
     * Metodo che permette di testare il metodo aggiungiTransazione()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void aggiungiTransazioneTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();

        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er1));

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);
        assertNotNull(controller.aggiungiTransazione(er2));

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er3));

        EntityRegistro er4 = new EntityRegistro();
        //er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(new DateTime());
        er4.setTipoTransazione(TipoTransazione.USCITA);
        assertNotNull(controller.aggiungiTransazione(er4));
    }

    /**
     * Metodo che permette di testare il metodo cancellaTransazione()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void cancellaTransazioneTest() throws IOException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        initControllerRegistro(controller);

        EntityRegistro result = controller.cancellaTransazione(1L);
        assertNotNull(result);
    }

    /**
     * Metodo privato che permette di inizializzare il magazzino all'interno del controller del banco alimentare
     * @param controller: oggetto BancoAlimentareCSVController da inizializzare
     */
    private void initControllerMagazzino(BancoAlimentareCSVController controller)
    {
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Tonno in scatola 200 g");
        em1.setGiacenza(7);

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Asparagi 300 g");
        em2.setGiacenza(6);

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Zucchine 500 g");
        em3.setGiacenza(3);

        controller.aggiungiProdotto(em1);
        controller.aggiungiProdotto(em2);
        controller.aggiungiProdotto(em3);
    }

    /**
     * Metodo privato che permette di inizializzare il registro all'interno del controller del banco alimentare
     * @param controller: oggetto BancoAlimentareCSVController da inizializzare
     */
    private void initControllerRegistro(BancoAlimentareCSVController controller)
    {
        EntityRegistro er1 = new EntityRegistro();
        //er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er2 = new EntityRegistro();
        //er2.setProdotto("Aceto");
        er2.setQuantita(2);
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.INGRESSO);

        EntityRegistro er3 = new EntityRegistro();
        //er3.setProdotto("Mele");
        er3.setQuantita(10);
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er4 = new EntityRegistro();
        //er4.setProdotto("Mele");
        er4.setQuantita(4);
        er4.setDataTransazione(new DateTime());
        er4.setTipoTransazione(TipoTransazione.USCITA);

        controller.aggiungiTransazione(er1);
        controller.aggiungiTransazione(er2);
        controller.aggiungiTransazione(er3);
        controller.aggiungiTransazione(er4);
    }

    /**
     * Metodo che permette di testare il metodo toString()
     * @throws IOException in caso di problemi di accesso ai file CSV
     */
    @Test
    public void toStringTest() throws IOException, InterruptedException
    {
        BasicConfigurator.configure();
        BancoAlimentareCSVController controller = new BancoAlimentareCSVController();
        controller.inizializza();
        boolean result = controller.deposita("Asparagi 300 g", 1);
        assertTrue(result);
        controller.caricaSuFile();

        String toStringResult = controller.toString();
        assertNotNull(toStringResult);
    }
}