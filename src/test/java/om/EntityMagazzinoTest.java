package om;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * EntityMagazzinoTest. Classe di test che permette di verificare la correttezza dei metodi scritti nella classe EntityMagazzino
 *
 * @author Edoardo Baral
 */
public class EntityMagazzinoTest
{
    /**
     * Metodo che permette di testare i metodi setter e getter della classe EntityMagazzino
     */
    @Test
    public void testSetterGetterMethods()
    {
        EntityMagazzino em = new EntityMagazzino();
        em.setNome("Spaghetti 1 kg");
        em.setGiacenza(10);
        assertEquals("Spaghetti 1 kg", em.getNome());
        assertEquals(10, em.getGiacenza());
    }
}