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

    /**
     * Metodo che permette di testare il corretto funzionamento del metodo compareTo() implementato nella classe EntityMagazzino
     */
    @Test
    public void testCompareTo()
    {
        EntityMagazzino em1 = new EntityMagazzino();
        em1.setNome("Spaghetti 1 kg");
        em1.setGiacenza(10);

        EntityMagazzino em2 = new EntityMagazzino();
        em2.setNome("Tonno in scatola 200 g");
        em2.setGiacenza(5);

        EntityMagazzino em3 = new EntityMagazzino();
        em3.setNome("Olio d'oliva 1 L");
        em3.setGiacenza(7);

        EntityMagazzino em4 = new EntityMagazzino();
        em4.setNome("Spaghetti 1 kg");
        em4.setGiacenza(10);

        assertTrue(em1.compareTo(em2) < 0);
        assertTrue(em1.compareTo(em3) > 0);
        assertTrue(em1.compareTo(em4) == 0);
    }
}