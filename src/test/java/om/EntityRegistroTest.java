package om;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;

/**
 * EntityRegistroTest. Classe di test che permette di verificare la correttezza dei metodi scritti nella classe EntityRegistro
 *
 * @author Edoardo Baral
 */
@Ignore
public class EntityRegistroTest
{
    /**
     * Metodo che permette di testare i metodi setter e getter della classe EntityMagazzino
     */
    @Test
    public void testSetterGetterMethods()
    {
        EntityRegistro er = new EntityRegistro();

        EntityMagazzino prodotto = new EntityMagazzino();
        prodotto.setNome("Spaghetti 1 kg");
        prodotto.setGiacenza(5);

        er.setProdotto(prodotto);
        er.setQuantita(10);
        er.setDestinatario("Mario Rossi");
        er.setDataTransazione(new DateTime());
        er.setTipoTransazione(TipoTransazione.USCITA);
        assertEquals("Spaghetti 1 kg", er.getProdotto().getNome());
        assertEquals(10, er.getQuantita());
        assertEquals("Mario Rossi", er.getDestinatario());
        assertEquals(TipoTransazione.USCITA, er.getTipoTransazione());
        assertNotNull(er.getDataTransazione());
    }

    /**
     * Metodo che permette di testare il corretto funzionamento del metodo compareTo() implementato nella classe EntityRegistro
     */
    @Test
    public void testCompareTo()
    {
        EntityRegistro er1 = new EntityRegistro();
        er1.setId(1L);

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Spaghetti 1 kg");
        p1.setGiacenza(2);

        er1.setProdotto(p1);
        er1.setQuantita(10);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er2 = new EntityRegistro();
        er2.setId(2L);

        EntityMagazzino p2 = new EntityMagazzino();
        p2.setNome("Tonno 1 kg");
        p2.setGiacenza(5);

        er2.setProdotto(p2);
        er2.setQuantita(10);
        er2.setDestinatario("Mario Rossi");
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er3 = new EntityRegistro();
        er3.setId(3L);

        EntityMagazzino p3 = new EntityMagazzino();
        p3.setNome("Acciughe 1 kg");
        p3.setGiacenza(4);

        er3.setProdotto(p3);
        er3.setQuantita(10);
        er3.setDestinatario("Mario Rossi");
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er4 = new EntityRegistro();
        er4.setId(1L);

        EntityMagazzino p4 = new EntityMagazzino();
        p4.setNome("Spaghetti 1 kg");
        p4.setGiacenza(8);

        er4.setProdotto(p4);
        er4.setQuantita(10);
        er4.setDestinatario("Mario Rossi");
        er4.setDataTransazione(er1.getDataTransazione());
        er4.setTipoTransazione(TipoTransazione.USCITA);

        assertTrue(er1.compareTo(er2) < 0);
        assertTrue(er2.compareTo(er1) > 0);
        assertTrue(er1.compareTo(er3) < 0);
        assertEquals(0, er1.compareTo(er4));
    }

    /**
     * Metodo che permette di testare il corretto funzionamento del metodo compareTo()
     */
    @Test
    public void testCompareToBis()
    {
        EntityRegistro er1 = new EntityRegistro();

        EntityMagazzino p1 = new EntityMagazzino();
        p1.setNome("Fusilli");

        er1.setProdotto(p1);
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er2 = new EntityRegistro();
        DateTime d = new DateTime();
        d = d.plus(100);

        EntityMagazzino p2 = new EntityMagazzino();
        p2.setNome("Aceto");

        er2.setProdotto(p2);
        er2.setQuantita(2);
        er2.setDataTransazione(d);
        er2.setTipoTransazione(TipoTransazione.INGRESSO);

        assertEquals(-1, er1.compareTo(er2));

        er1.setId(1L);
        er2.setId(2L);
        assertTrue(er1.compareTo(er2) < 0);
        assertTrue(er2.compareTo(er1) > 0);
        assertTrue(er1.compareTo(er1) == 0);
    }

    /**
     * Metodo che permette di testare il metodo toString() della classe EntityMagazzino
     */
    @Test
    public void testToString()
    {
        EntityRegistro er = new EntityRegistro();
        EntityMagazzino prodotto = new EntityMagazzino();
        prodotto.setNome("Spaghetti 1 kg");
        er.setProdotto(prodotto);
        er.setQuantita(10);
        er.setDestinatario("Mario Rossi");
        er.setDataTransazione(new DateTime());
        er.setTipoTransazione(TipoTransazione.USCITA);

        String result = er.toString();
        assertNotNull(result);
    }
}