package impl.controller.csv;

import interfaces.BancoAlimentareController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * BancoAlimentareCSVController. Classe che permette di mettere in relazione i due controller che gestiscono il magazzino e il registro del banco alimentare
 * (EntityMagazzinoCSVController ed EntityRegistroCSVController) per gestire in modo centralizzato le operazioni di gestione del magazzino e registro delle transazioni
 * @author Edoardo Baral
 */
public class BancoAlimentareCSVController implements BancoAlimentareController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BancoAlimentareCSVController.class);

    private EntityMagazzinoCSVController magazzino;
    private EntityRegistroCSVController registro;

    /**
     * Metodo costruttore della classe BancoAlimentareCSVController
     * @throws IOException in caso di errori nella lettura/scrittura dei file CSV
     */
    public BancoAlimentareCSVController() throws IOException
    {
        File cartella = new File("archivio");
        if(!cartella.exists())
        {
            try
            {
                cartella.mkdir();
            }
            catch(SecurityException e)
            {
                throw new IOException(e.getMessage());
            }
        }
        magazzino = new EntityMagazzinoCSVController();
        registro = new EntityRegistroCSVController();

        LOGGER.info("Istanziato un nuovo oggetto BancoAlimentareCSVController - " + this.toString());
    }

    /**
     * Metodo che permette di leggere il contenuto dei file CSV e inizializzare opportunamente le strutture dati dei controller per magazzino e registro
     * @throws IOException in caso di errori nella lettura/scrittura dei file CSV
     */
    public void inizializza() throws IOException
    {
        LOGGER.info("Metodo inizializza() - Inizio");

        magazzino.mappingDaFile();
        registro.mappingDaFile();

        LOGGER.info("Metodo inizializza() - Fine");
        LOGGER.info("Stato del controller: " + this.toString());
    }

    /**
     * Metodo che permette di riversare su file CSV il contenuto delle strutture dati del magazzino e del registro
     * @throws IOException in caso di errori nella lettura/scrittura dei file CSV
     */
    public void caricaSuFile() throws IOException
    {
        LOGGER.info("Metodo caricaSuFile() - Inizio");

        magazzino.scriviProdottiSuFile();
        registro.scriviProdottiSuFile();

        LOGGER.info("Metodo caricaSuFile() - Fine");
        LOGGER.info("Stato del controller: " + this.toString());
    }

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param prodotto:    prodotto da cercare nel magazzino
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da prelevare dal magazzino
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta e se la transazione viene correttamente registrata, false altrimenti
     */
    public boolean preleva(EntityMagazzino prodotto, EntityRegistro transazione, int quantita)
    {
        if(!magazzino.decrementaGiacenza(prodotto, quantita))
        {
            LOGGER.warn("Metodo preleva() - Fine - Prodotto non presente nel magazzino");
            return false;
        }

        transazione.setTipoTransazione(TipoTransazione.USCITA);

        if(registro.aggiungiTransazione(transazione) != null)
        {
            LOGGER.info("Metoto preleva() - Fine - Transazione in uscita registrata - " + transazione);
            return true;
        }
        else
        {
            LOGGER.warn("Metoto preleva() - Fine - Transazione già registrata");
            return false;
        }
    }

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param nomeProdotto: nome del prodotto oggetto della transazione
     * @param quantita:     quantità di prodotto da prelevare
     * @param destinatario: destinatario del prodotto prelevato
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta e se la transazione viene correttamente registrata, false altrimenti
     */
    public boolean preleva(String nomeProdotto, int quantita, String destinatario)
    {
        LOGGER.info("Metodo preleva() - Inizio");

        int indice = magazzino.exists(nomeProdotto);
        if(indice >= 0)
        {
            EntityMagazzino prodotto = magazzino.getProdotti().get(indice);
            EntityRegistro transazione = new EntityRegistro();
            //transazione.setProdotto(nomeProdotto);
            transazione.setQuantita(quantita);
            transazione.setDestinatario(destinatario);
            transazione.setDataTransazione(new DateTime());
            transazione.setTipoTransazione(TipoTransazione.USCITA);

            boolean result = preleva(prodotto, transazione, quantita);
            LOGGER.info("Stato del controller: " + this.toString());
            return result;
        }
        else
        {
            LOGGER.info("Stato del controller: " + this.toString());
            return false;
        }
    }

    /**
     * Metodo che permette di depositare una quantità specificata di un certo prodotto nel magazzino e registrare la transazione
     * @param prodotto:    prodotto da cercare nel magazzino
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da depositare nel magazzino
     * @return true se il prodotto viene trovato nel magazzino e se la transazione viene correttamente registrata, false altrimenti
     */
    public boolean deposita(EntityMagazzino prodotto, EntityRegistro transazione, int quantita)
    {
        if(!magazzino.incrementaGiacenza(prodotto, quantita))
        {
            LOGGER.warn("Metodo deposita() - Fine - Prodotto non trovato nel magazzino");
            return false;
        }

        transazione.setTipoTransazione(TipoTransazione.INGRESSO);

        if(registro.aggiungiTransazione(transazione) != null)
        {
            LOGGER.info("Metodo deposita() - Fine - Transazione in entrata registrata - " + transazione);
            return true;
        }
        else
        {
            LOGGER.warn("Metodo deposita() - Fine - Transazione già registrata");
            return false;
        }
    }

    /**
     * Metodo che permette di depositare una quantità specificata di un certo prodotto nel magazzino e registrare la transazione
     * @param nomeProdotto: nome del prodotto da depositare
     * @param quantita:     quantità di prodotto da depositare
     * @return true se il prodotto viene correttamente depositato in magazzino e la sua giacenza viene aggiornata, false altrimenti
     */
    public boolean deposita(String nomeProdotto, int quantita)
    {
        LOGGER.info("Metodo deposita() - Inizio");

        int indice = magazzino.exists(nomeProdotto);
        if(indice >= 0)
        {
            EntityMagazzino prodotto = magazzino.getProdotti().get(indice);
            EntityRegistro transazione = new EntityRegistro();
            //transazione.setProdotto(nomeProdotto);
            transazione.setQuantita(quantita);
            transazione.setDataTransazione(new DateTime());
            transazione.setTipoTransazione(TipoTransazione.INGRESSO);

            LOGGER.info("Stato del controller: " + this.toString());
            return deposita(prodotto, transazione, quantita);
        }
        else
        {
            EntityMagazzino prodotto = new EntityMagazzino();
            prodotto.setNome(nomeProdotto);
            prodotto.setGiacenza(quantita);

            EntityRegistro transazione = new EntityRegistro();
            //transazione.setProdotto(nomeProdotto);
            transazione.setQuantita(quantita);
            transazione.setDataTransazione(new DateTime());
            transazione.setTipoTransazione(TipoTransazione.INGRESSO);

            LOGGER.info("Stato del controller: " + this.toString());
            return deposita(prodotto, transazione, quantita);
        }
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        return magazzino.aggiungiProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto)
    {
        return magazzino.cancellaProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        return magazzino.cancellaProdotto(nomeProdotto);
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        return magazzino.modificaProdotto(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param prodotto: prodotto da cercare nel magazzino
     * @return l'indice del prodotto all'interno della lista, negativo nel caso non esista
     */
    public int exists(EntityMagazzino prodotto)
    {
        return magazzino.exists(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return l'indice del prodotto all'interno della lista, negativo nel caso non esista
     */
    public int exists(String nomeProdotto)
    {
        return magazzino.exists(nomeProdotto);
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione)
    {
        return registro.aggiungiTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityRegistro cancellaTransazione(EntityRegistro transazione)
    {
        return registro.cancellaTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityRegistro cancellaTransazione(Long id)
    {
        return registro.cancellaTransazione(id);
    }

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     */
    public EntityRegistro modificaTransazione(EntityRegistro transazione)
    {
        return registro.modificaTransazione(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    public int exists(EntityRegistro transazione)
    {
        return registro.exists(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    public int exists(Long id)
    {
        return registro.exists(id);
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     */
    public List<String> getNomiProdotti()
    {
        return magazzino.getNomiProdotti();
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     */
    public List<EntityMagazzino> getProdotti()
    {
        return magazzino.getProdotti();
    }

    /**
     * Metodo che restituisce la lista delle transazioni registrate nel registro
     * @return la lista delle transazioni presenti nel registro
     */
    public List<EntityRegistro> getTransazioni()
    {
        return registro.getListaTransazioni();
    }

    /**
     * Metodo che restituisce una rappresentazione testuale dello stato del banco alimentare
     * @return una stringa che rappresenta lo stato del banco alimenatre
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("{");
        result.append("\"magazzino\":[");
        int i;

        if(magazzino.getProdotti().isEmpty()) result.append("]");
        else
        {
            for(i = 0; i < magazzino.getProdotti().size() - 1; i++)
            {
                result.append(magazzino.getProdotti().get(i).toString());
                result.append(",");
            }
            result.append(magazzino.getProdotti().get(i).toString());
            result.append("]");
        }

        result.append(",");

        result.append("\"registro\":[");
        if(registro.getListaTransazioni().isEmpty()) result.append("]");
        else
        {
            for(i = 0; i < registro.getListaTransazioni().size() - 1; i++)
            {
                result.append(registro.getListaTransazioni().get(i).toString());
                result.append(",");
            }
            result.append(registro.getListaTransazioni().get(i));
            result.append("]");
        }

        result.append("}");
        return result.toString();
    }
}
