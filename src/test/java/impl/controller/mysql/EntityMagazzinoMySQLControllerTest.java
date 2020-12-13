package impl.controller.mysql;

import om.EntityMagazzino;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class EntityMagazzinoMySQLControllerTest
{
    @Test
    @Ignore
    public void costruttoreTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        assertNotNull(controller);
    }

    @Test
    @Ignore
    public void existsFalseTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Succo di pesca 1 L");
        p1.setGiacenza(5);

        assertFalse(controller.exists(p1));
    }

    @Test
    @Ignore
    public void existsTrueTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertTrue(controller.exists(p1));
    }

    @Test
    @Ignore
    public void aggiungiProdottiNuoviTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

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
    public void aggiungiProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertNull(controller.aggiungiProdotto(p1));
    }

    @Test
    @Ignore
    public void cancellaProdottoInesistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertNull(controller.cancellaProdotto(p1));
    }

    @Test
    @Ignore
    public void cancellaProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(5);

        assertNotNull(controller.cancellaProdotto(p1));
    }

    @Test
    @Ignore
    public void modificaProdottoInesistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertNull(controller.modificaProdotto(p1));
    }

    @Test
    @Ignore
    public void modificaProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Fagioli in scatola");
        p1.setGiacenza(10);

        assertNotNull(controller.modificaProdotto(p1));
    }

    @Test
    @Ignore
    public void incrementaGiacenzaProdottoInesistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.incrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void incrementaGiacenzaProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Fagioli in scatola");
        p1.setGiacenza(10);

        assertTrue(controller.incrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaNonValidaTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.decrementaGiacenza(p1, 0));
        assertFalse(controller.decrementaGiacenza(p1, -1));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaProdottoInesistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Pomodori");
        p1.setGiacenza(5);

        assertFalse(controller.decrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void decrementaGiacenzaProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Fagioli in scatola");
        p1.setGiacenza(10);

        assertTrue(controller.decrementaGiacenza(p1, 2));
    }

    @Test
    @Ignore
    public void getProdottoInesistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        assertNull(controller.getProdotto("Sapone"));
    }

    @Test
    @Ignore
    public void getProdottoEsistenteTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        assertNotNull(controller.getProdotto("Fagioli in scatola"));
    }

    @Test
    @Ignore
    public void getNomiProdottiTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        List<String> list = controller.getNomiProdotti();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Ignore
    public void getProdottiTest() throws SQLException
    {
        EntityMagazzinoMySQLController controller = new EntityMagazzinoMySQLController();
        List<EntityMagazzino> list = controller.getProdotti();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}