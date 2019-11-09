package om;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

/**
 * TransactionID. Classe che serve a gestire l'autoincremento dell'identificativo delle transazioni su MongoDB e Morphia.
 * L'identificativo dell'entità è dato dalla stringa className, che indica la classe per cui si sta tenendo traccia del
 * contatore dell'id.
 * Il campo id invece tiene traccia del prossimo valore disponibile e utilizzabile per l'identificativo.
 *
 * @author Edoardo Baral
 */

@Entity("TransactionID")
public class TransactionID
{
    @Id
    private String className;
    private Long id;

    /**
     * Metodo costruttore della classe TransactionID
     */
    public TransactionID()
    {
        className = "EntityRegistro";
        id = 1L;
    }

    /**
     * Metodo che restituisce il valore di className
     * @return il nome della classe associata al contatore
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Metodo che permette di indicare un nome di una classe da associare ad un contatore id
     * @param className: nome della classe da associare ad un id
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * Metodo che restituisce il valore del prossimo id disponibile
     * @return il valore del prossimo id disponibile
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Metodo che permette di settare un valore per l'id associato ad una classe
     * @param id: valore dell'id
     */
    public void setId(Long id)
    {
        this.id = id;
    }
}
