package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morphia.annotations.*;
import org.joda.time.DateTime;

/**
 * EntityRegistro. Classe che rappresenta un record (transazione) nel registro dei prodotti ceduti/acquisiti dal Banco Alimentare.
 *
 * @author Edoardo Baral
 */

@Entity("Transazione")
@Validation("{ quantita: { $gt: 0 } }")
public class EntityRegistro implements Comparable<EntityRegistro>
{
    @Id
    private Long id;
    @Reference
    private EntityMagazzino prodotto;
    private int quantita;
    private String destinatario;
    private DateTime dataTransazione;
    private TipoTransazione tipoTransazione;

    /**
     * Costruttore di default della classe EntityRegistro
     */
    public EntityRegistro()
    {
        quantita = 0;
    }

    /**
     * Metodo che restituisce l'identificativo della transazione registrata
     * @return l'identificativo della transazione registrata
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Metodo che permette di indicare un identificativo per la transazione di cessione di un prodotto
     * @param id: identificativo da impostare per la transazione
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Metodo che restituisce il prodotto oggetto della transazione
     * @return il prodotto oggetto della transazione
     */
    public EntityMagazzino getProdotto()
    {
        return prodotto;
    }

    /**
     * Metodo che permette di impostare un nuovo prodotto la transazione
     * @param prodotto: prodotto della transazione
     */
    public void setProdotto(EntityMagazzino prodotto)
    {
        this.prodotto = prodotto;
    }

    /**
     * Metodo che restituisce la quantità di prodotto ceduto
     * @return la quantità del prodotto ceduto
     */
    public int getQuantita()
    {
        return quantita;
    }

    /**
     * Metodo che permette di indicare una nuova quantità di prodotto ceduto
     * @param quantita: quantità di prodotto da indicare sul registro
     */
    public void setQuantita(int quantita)
    {
        this.quantita = quantita;
    }

    /**
     * Metodo che restituisce il prodotto del destinatario del prodotto ceduto
     * @return il prodotto del destinatario del prodotto indicato sul registro
     */
    public String getDestinatario()
    {
        return destinatario;
    }

    /**
     * Metodo che permette di indicare un nuovo prodotto per il destinatario del prodotto
     * @param destinatario: prodotto del destinatario da indicare per il prodotto ceduto
     */
    public void setDestinatario(String destinatario)
    {
        this.destinatario = destinatario;
    }

    /**
     * Metodo che restituisce la data di cessione del prodotto
     * @return la data di cessione del prodotto
     */
    public DateTime getDataTransazione()
    {
        return dataTransazione;
    }

    /**
     * Metodo che permette di indicare una data di cessione per il prodotto
     * @param dataTransazione: data di cessione da indicare per il prodotto
     */
    public void setDataTransazione(DateTime dataTransazione)
    {
        this.dataTransazione = dataTransazione;
    }

    /**
     * Metodo che restituisce il tipo di transazione rappresentata dall'istanza di EntityRegistro
     * @return il tipo della transazione
     */
    public TipoTransazione getTipoTransazione()
    {
        return tipoTransazione;
    }

    /**
     * Metodo che permette di impostare un tipo di transazione per un oggetto EntityRegistro
     * @param tipoTransazione: tipo di transazione da impostare sull'oggetto EntityRegistro
     */
    public void setTipoTransazione(TipoTransazione tipoTransazione)
    {
        this.tipoTransazione = tipoTransazione;
    }

    /**
     * Metodo che mermette di confrontare l'istanza this di EntityRegistro con un'altra per determinare l'ordine tra le due
     * @param other: istanza di EntityRegistro da confrontare con quella chiamante
     * @return una valore negativo se l'istanza chiamante (this) precede l'istanza other, un valore positivo in caso contrario.
     */
    public int compareTo(EntityRegistro other)
    {
        if(this.id == null || other.getId() == null)
        {
            if (this.dataTransazione.getMillis() < other.getDataTransazione().getMillis())
                return -1;
            else if(this.dataTransazione.getMillis() == other.getDataTransazione().getMillis())
                return 0;
            else
                return 1;
        }
        else
        {
            if(this.id < other.getId())
                return -1;
            else if(this.id == other.getId())
                return 0;
            else
                return 1;
        }
    }

    /**
     * Metodo che verifica se due istanze di EntityRegistro sono uguali
     * @param other: istanza da comparare con this
     * @return true se le due istanze sono uguali, false altrimenti
     */
    public boolean equals(EntityRegistro other)
    {
        if(this.id != other.getId())
            return false;
        else if(!this.getProdotto().equals(other.getProdotto()))
            return false;
        else if(this.quantita != other.getQuantita())
            return false;
        else if(!this.destinatario.equals(other.getDestinatario()))
            return false;
        else if(this.dataTransazione.getMillis() != other.getDataTransazione().getMillis())
            return false;
        else if(!this.getTipoTransazione().equals(other.getTipoTransazione()))
            return false;
        else
            return true;
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
