package om;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;

/**
 * EntityRegistro. Classe che rappresenta un record nel registro dei prodotti ceduti dal Banco Alimentare.
 *
 * @author Edoardo Baral
 */
public class EntityRegistro
{
    private Long id;
    private String nome;
    private int quantita;
    private String destinatario;
    private DateTime dataCessione;

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
     * Metodo che restituisce il nome del prodotto ceduto
     * @return il nome del prodotto
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * Metodo che permette di impostare un nuovo nome per il prodotto ceduto
     * @param nome: nuovo nome del prodotto
     */
    public void setNome(String nome)
    {
        this.nome = nome;
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
     * Metodo che restituisce il nome del destinatario del prodotto ceduto
     * @return il nome del destinatario del prodotto indicato sul registro
     */
    public String getDestinatario()
    {
        return destinatario;
    }

    /**
     * Metodo che permette di indicare un nuovo nome per il destinatario del prodotto
     * @param destinatario: nome del destinatario da indicare per il prodotto ceduto
     */
    public void setDestinatario(String destinatario)
    {
        this.destinatario = destinatario;
    }

    /**
     * Metodo che restituisce la data di cessione del prodotto
     * @return la data di cessione del prodotto
     */
    public DateTime getDataCessione()
    {
        return dataCessione;
    }

    /**
     * Metodo che permette di indicare una data di cessione per il prodotto
     * @param dataCessione: data di cessione da indicare per il prodotto
     */
    public void setDataCessione(DateTime dataCessione)
    {
        this.dataCessione = dataCessione;
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
