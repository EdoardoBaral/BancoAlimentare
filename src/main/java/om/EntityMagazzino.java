package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * EntityMagazzino. Classe che rappresenta un record nel registro di magazzino. Le istanze di questa classe descrivono un determinato dipo di prodotto presente in magazzino e la relativa giacenza
 * (ovvero la quantità residua)
 *
 * @author Edoardo Baral
 */
public class EntityMagazzino implements Comparable<EntityMagazzino>
{
    private String nome;
    private int giacenza;

    /**
     * Costruttore di default della classe EntityMagazzino
     */
    public EntityMagazzino()
    {
        giacenza = 0;
    }

    /**
     * Metodo che restituisce il nome del prodotto
     * @return il nome del prodotto
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * Metodo che permette di indicare un nuovo nome per il prodotto
     * @param nome: nuovo nome da associare al prodotto
     */
    public void setNome(String nome)
    {
        this.nome = nome;
    }

    /**
     * Metodo che restituisce la quantità residua di prodotto presente in magazzino (giacenza)
     * @return la giacenza del prodotto in magazzino
     */
    public int getGiacenza()
    {
        return giacenza;
    }

    /**
     * Metodo che permette di impostare un nuovo valore per la giacenza del prodotto
     * @param giacenza: nuova giacenza da associare al prodotto
     */
    public void setGiacenza(int giacenza)
    {
        this.giacenza = giacenza;
    }

    /**
     * Metodo che mermette di confrontare l'istanza this di EntityMagazzino con un'altra per determinare l'ordine tra le due
     * @param other: istanza di EntityMagazzino da confrontare con quella chiamante
     * @return una valore negativo che il nome dell'istanza chiamante (this) precede alfabeticamente quello dell'istanza other, 0 se sono uguali, una valore positivo altrimenti
     */
    public int compareTo(EntityMagazzino other)
    {
        return this.nome.compareTo(other.getNome());
    }

    /**
     * Metodo che restituisce una rappresentazione JSON dell'oggetto
     * @return una stringa rappresentante l'oggetto in formato JSON, null in caso d'errore
     */
    @Override
    public String toString()
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }
        catch(JsonProcessingException e)
        {
            return null;
        }
    }
}
