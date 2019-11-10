package impl.controller.morphia;

import interfaces.BancoAlimentareController;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class BancoAlimentareMorphiaControllerTest
{
    @Test
    @Ignore
    public void costruttoreTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertNotNull(controller);
    }

    @Test
    @Ignore
    public void depositaProdottoInesistenteTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertNotNull(controller.deposita("Spaghetti 1 Kg", 5));
        assertNotNull(controller.deposita("Passata di pomodoro 1 L", 3));
        assertNotNull(controller.deposita("Biscotti 500 g", 7));
        assertNotNull(controller.deposita("Fagioli 300 g", 3));
        assertNotNull(controller.deposita("Olio extravergine di oliva 1 L", 9));
    }

    @Test
    @Ignore
    public void depositaProdottoEsistenteTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertNotNull(controller.deposita("Spaghetti 1 Kg", 5));
    }

    @Test
    @Ignore
    public void prelevaProdottoInesistenteTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertFalse(controller.preleva("Chewing gum", 1, "Mario Rossi"));
    }

    @Test
    @Ignore
    public void prelevaProdottoQuantitaNonValidaTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertFalse(controller.preleva("Spaghetti 1 Kg", 0, "Mario Rossi"));
    }

    @Test
    @Ignore
    public void prelevaProdottoTest()
    {
        BancoAlimentareController controller = new BancoAlimentareMorphiaController();
        assertTrue(controller.preleva("Spaghetti 1 Kg", 1, "Mario Rossi"));
    }
}