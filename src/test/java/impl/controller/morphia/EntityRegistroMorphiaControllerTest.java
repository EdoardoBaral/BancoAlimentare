package impl.controller.morphia;

import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class EntityRegistroMorphiaControllerTest
{
    @Test
    @Ignore
    public void costruttoreTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        assertNotNull(controller);
    }

    @Test
    @Ignore
    public void existsProdottoInesistenteTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();

        EntityRegistro t1 = new EntityRegistro();
        t1.setId(1L);

        assertFalse(controller.exists(t1));
    }

    @Test
    @Ignore
    public void aggiungiTransazioneInesistente()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();

        /*---------- Transazione 1 ----------*/
        EntityRegistro t1 = new EntityRegistro();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");

        t1.setProdotto(p1);
        t1.setQuantita(5);
        t1.setDestinatario("Magazzino");
        t1.setDataTransazione(LocalDate.now());
        t1.setTipoTransazione(TipoTransazione.INGRESSO);

        assertNotNull(controller.aggiungiTransazione(t1));

        /*---------- Transazione 2 ----------*/
        EntityRegistro t2 = new EntityRegistro();

        EntityMagazzino p2 = new EntityMagazzino();
        p2.setNome("Passata di pomodoro 1 L");

        t2.setProdotto(p2);
        t2.setQuantita(4);
        t2.setDestinatario("Magazzino");
        t2.setDataTransazione(LocalDate.now());
        t2.setTipoTransazione(TipoTransazione.INGRESSO);

        assertNotNull(controller.aggiungiTransazione(t2));

        /*---------- Transazione 3 ----------*/
        EntityRegistro t3 = new EntityRegistro();

        t3.setProdotto(p1);
        t3.setQuantita(2);
        t3.setDestinatario("Magazzino");
        t3.setDataTransazione(LocalDate.now());
        t3.setTipoTransazione(TipoTransazione.USCITA);

        assertNotNull(controller.aggiungiTransazione(t3));
    }

    @Test
    @Ignore
    public void cancellaTransazioneInesistenteTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        assertNull(controller.cancellaTransazione(0L));
    }

    @Test
    @Ignore
    public void cancellaTransazioneEsistenteTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        assertNotNull(controller.cancellaTransazione(4L));
    }

    @Test
    @Ignore
    public void getListaTransazioniTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        List<EntityRegistro> list = controller.getListaTransazioni();
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }

    @Test
    @Ignore
    public void getTransazioneInesistenteTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        assertNull(controller.getTransazione(0L));
    }

    @Test
    @Ignore
    public void getTransazioneEsistenteTest()
    {
        EntityRegistroMorphiaController controller = new EntityRegistroMorphiaController();
        assertNotNull(controller.getTransazione(3L));
    }
}