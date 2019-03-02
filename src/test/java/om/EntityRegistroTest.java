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
        er.setProdotto("Spaghetti 1 kg");
        er.setQuantita(10);
        er.setDestinatario("Mario Rossi");
        er.setDataTransazione(new DateTime());
        er.setTipoTransazione(TipoTransazione.USCITA);
        assertEquals("Spaghetti 1 kg", er.getProdotto());
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
        er1.setProdotto("Spaghetti 1 kg");
        er1.setQuantita(10);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er2 = new EntityRegistro();
        er2.setId(2L);
        er2.setProdotto("Tonno 1 kg");
        er2.setQuantita(10);
        er2.setDestinatario("Mario Rossi");
        er2.setDataTransazione(new DateTime());
        er2.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er3 = new EntityRegistro();
        er3.setId(3L);
        er3.setProdotto("Acciughe 1 kg");
        er3.setQuantita(10);
        er3.setDestinatario("Mario Rossi");
        er3.setDataTransazione(new DateTime());
        er3.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er4 = new EntityRegistro();
        er4.setId(1L);
        er4.setProdotto("Spaghetti 1 kg");
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
        er1.setProdotto("Fusilli");
        er1.setQuantita(5);
        er1.setDestinatario("Mario Rossi");
        er1.setDataTransazione(new DateTime());
        er1.setTipoTransazione(TipoTransazione.USCITA);

        EntityRegistro er2 = new EntityRegistro();
        DateTime d = new DateTime();
        d = d.plus(100);
        er2.setProdotto("Aceto");
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
        er.setProdotto("Spaghetti 1 kg");
        er.setQuantita(10);
        er.setDestinatario("Mario Rossi");
        er.setDataTransazione(new DateTime());
        er.setTipoTransazione(TipoTransazione.USCITA);

        String result = er.toString();
        assertNotNull(result);
    }
}