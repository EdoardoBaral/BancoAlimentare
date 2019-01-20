package om;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntityMagazzinoTest
{

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