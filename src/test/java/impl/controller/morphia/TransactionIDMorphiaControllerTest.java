package impl.controller.morphia;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionIDMorphiaControllerTest
{
    @Test
    @Ignore
    public void testGetNextIdInesistente()
    {
        TransactionIDMorphiaController controller = TransactionIDMorphiaController.getInstance();
        assertEquals(Long.valueOf(1L), controller.getNextId());
    }

    @Test
    @Ignore
    public void testGetNextIdEsistente()
    {
        TransactionIDMorphiaController controller = TransactionIDMorphiaController.getInstance();
        assertEquals(Long.valueOf(1L), controller.getNextId());
    }

    @Test
    @Ignore
    public void incrementaId()
    {
        TransactionIDMorphiaController controller = TransactionIDMorphiaController.getInstance();

        assertTrue(controller.incrementaId());
        assertEquals(Long.valueOf(2L), controller.getNextId());

        assertTrue(controller.incrementaId());
        assertEquals(Long.valueOf(3L), controller.getNextId());

        assertTrue(controller.incrementaId());
        assertEquals(Long.valueOf(4L), controller.getNextId());
    }
}