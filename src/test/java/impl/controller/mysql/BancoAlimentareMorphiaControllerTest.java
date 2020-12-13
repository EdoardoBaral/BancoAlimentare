package impl.controller.mysql;

import interfaces.BancoAlimentareController;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class BancoAlimentareMorphiaControllerTest
{
    @Test
    @Ignore
    public void costruttoreTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertNotNull(controller);
    }

    @Test
    @Ignore
    public void depositaProdottoInesistenteTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertNotNull(controller.deposita("Spaghetti 1 Kg", 5));
        assertNotNull(controller.deposita("Passata di pomodoro 1 L", 3));
        assertNotNull(controller.deposita("Biscotti 500 g", 7));
        assertNotNull(controller.deposita("Fagioli 300 g", 3));
        assertNotNull(controller.deposita("Olio extravergine di oliva 1 L", 9));
    }

    @Test
    @Ignore
    public void depositaProdottoEsistenteTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertNotNull(controller.deposita("Spaghetti 1 Kg", 5));
    }

    @Test
    @Ignore
    public void prelevaProdottoInesistenteTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertFalse(controller.preleva("Chewing gum", 1, "Mario Rossi"));
    }

    @Test
    @Ignore
    public void prelevaProdottoQuantitaNonValidaTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertFalse(controller.preleva("Spaghetti 1 Kg", 0, "Mario Rossi"));
    }

    @Test
    @Ignore
    public void prelevaProdottoTest() throws SQLException
    {
        BancoAlimentareController controller = new BancoAlimentareMySQLController();
        assertTrue(controller.preleva("Spaghetti 1 Kg", 1, "Mario Rossi"));
    }
}