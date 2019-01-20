package om;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * EntityRegistroTest. Classe di test che permette di verificare la correttezza dei metodi scritti nella classe EntityRegistro
 *
 * @author Edoardo Baral
 */
public class EntityRegistroTest
{
    /**
     * Metodo che permette di testare i metodi setter e getter della classe EntityMagazzino
     */
    @Test
    public void testSetterGetterMethods()
    {
        EntityRegistro er = new EntityRegistro();
        er.setNome("Spaghetti 1 kg");
        er.setQuantita(10);
        er.setDestinatario("Mario Rossi");
        er.setDataCessione(new DateTime());
        assertEquals("Spaghetti 1 kg", er.getNome());
        assertEquals(10, er.getQuantita());
        assertEquals("Mario Rossi", er.getDestinatario());
        assertNotNull(er.getDataCessione());
    }
}