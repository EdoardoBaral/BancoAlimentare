package impl.controller.morphia;

import om.EntityMagazzino;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EntityMagazzinoMorphiaControllerTest
{
    @Test
    @Ignore
    public void costruttoreTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        assertNotNull(controller);
    }

    @Test
    @Ignore
    public void existsFalseTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Succo di pesca 1 L");
        p1.setGiacenza(5);

        assertFalse(controller.exists(p1));
    }

    @Test
    @Ignore
    public void existsTrueTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertTrue(controller.exists(p1));
    }

    @Test
    @Ignore
    public void aggiungiProdottiNuoviTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        EntityMagazzino p2 = new EntityMagazzino();
        p2.setNome("Passata di pomodoro 1 L");
        p2.setGiacenza(4);

        EntityMagazzino p3 = new EntityMagazzino();
        p3.setNome("Fagioli in scatola");
        p3.setGiacenza(2);

        assertNotNull(controller.aggiungiProdotto(p1));
        assertNotNull(controller.aggiungiProdotto(p2));
        assertNotNull(controller.aggiungiProdotto(p3));
    }

    @Test
    @Ignore
    public void aggiungiProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertNull(controller.aggiungiProdotto(p1));
    }

    @Test
    @Ignore
    public void cancellaProdottoInesistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertNull(controller.cancellaProdotto(p1));
    }

    @Test
    @Ignore
    public void cancellaProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertNotNull(controller.cancellaProdotto(p1));
    }

    @Test
    @Ignore
    public void modificaProdottoInesistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertNull(controller.modificaProdotto(p1));
    }

    @Test
    @Ignore
    public void modificaProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(10);

        assertNotNull(controller.modificaProdotto(p1));
    }

    @Test
    @Ignore
    public void incrementaGiacenzaProdottoInesistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.incrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void incrementaGiacenzaProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(10);

        assertTrue(controller.incrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaNonValidaTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.decrementaGiacenza(p1, 0));
        assertFalse(controller.decrementaGiacenza(p1, -1));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaProdottoInesistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.decrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(10);

        assertTrue(controller.decrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void getProdottoInesistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        assertNull(controller.getProdotto("Sapone"));
    }

    @Test
    @Ignore
    public void getProdottoEsistenteTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        assertNotNull(controller.getProdotto("Spaghetti 1 kg"));
    }

    @Test
    @Ignore
    public void getNomiProdottiTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        List<String> list = controller.getNomiProdotti();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Ignore
    public void getProdottiTest()
    {
        EntityMagazzinoMorphiaController controller = new EntityMagazzinoMorphiaController();
        List<EntityMagazzino> list = controller.getProdotti();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}