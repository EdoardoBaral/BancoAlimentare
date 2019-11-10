package impl.controller.morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import interfaces.BancoAlimentareController;
import interfaces.EntityMagazzinoController;
import interfaces.EntityRegistroController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

import static utils.ConnectionParams.DATABASE;
import static utils.ConnectionParams.URI;

public class BancoAlimentareMorphiaController implements BancoAlimentareController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BancoAlimentareMorphiaController.class);

    private EntityMagazzinoController magazzino;
    private EntityRegistroController registro;
    private Datastore datastore;

    public BancoAlimentareMorphiaController()
    {
        Morphia morphia = new Morphia();
        morphia.mapPackage("om");
        MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
        datastore = morphia.createDatastore(mongoClient, DATABASE);
        datastore.ensureIndexes();

        magazzino = new EntityMagazzinoMorphiaController();
        registro = new EntityRegistroMorphiaController();

        LOGGER.info("BancoAlimentareMorphiaController - Nuova istanza creata");
    }

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da prelevare dal magazzino
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta
     * e se la transazione viene correttamente registrata, false altrimenti
     */
    private boolean preleva(EntityRegistro transazione, int quantita)
    {
        boolean result = magazzino.decrementaGiacenza(transazione.getProdotto(), quantita);

        if(!result)
        {
            LOGGER.warn("Metodo preleva() - Fine - Impossibile decrementare la giacenza del prodotto in magazzino");
            return false;
        }

        EntityRegistro t = registro.aggiungiTransazione(transazione);
        result = result && (t != null);

        if(result) LOGGER.info("Metoto preleva() - Fine - Transazione in uscita registrata - " + transazione);
        else LOGGER.warn("Metoto preleva() - Fine - Transazione già registrata");

        return result;
    }

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param nomeProdotto: nome del prodotto oggetto della transazione
     * @param quantita:     quantità di prodotto da prelevare
     * @param destinatario: destinatario del prodotto prelevato
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta
     * e se la transazione viene correttamente registrata, false altrimenti
     */
    @Override
    public boolean preleva(String nomeProdotto, int quantita, String destinatario)
    {
        LOGGER.info("Metodo preleva() - Inizio");

        EntityMagazzino prodotto = magazzino.getProdotto(nomeProdotto);

        if(prodotto == null)
        {
            LOGGER.warn("Metodo preleva() - Prodotto non trovato nel magazzino");
            return false;
        }

        EntityRegistro transazione = new EntityRegistro();
        transazione.setProdotto(prodotto);
        transazione.setQuantita(quantita);
        transazione.setDestinatario(destinatario);
        transazione.setDataTransazione(LocalDate.now());
        transazione.setTipoTransazione(TipoTransazione.USCITA);

        boolean result = preleva(transazione, quantita);
        return result;
    }

    /**
     * Metodo che permette di depositare una quantità specificata di un certo prodotto nel magazzino e registrare la transazione.
     * Precondizione del metodo è che il prodotto sia presente nel magazzino.
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da depositare nel magazzino
     * @return true se il prodotto viene trovato nel magazzino e se la transazione viene correttamente registrata, false altrimenti
     */
    private boolean deposita(EntityRegistro transazione, int quantita)
    {
        EntityMagazzino prodotto = magazzino.getProdotto(transazione.getProdotto().getNome());
        boolean result = true;

        if(prodotto == null)
        {
            EntityMagazzino p = magazzino.aggiungiProdotto(transazione.getProdotto());
            result = result && (p != null);
        }
        else result = result && magazzino.incrementaGiacenza(transazione.getProdotto(), quantita);

        EntityRegistro t = registro.aggiungiTransazione(transazione);
        result = result && (t != null);

        if(result) LOGGER.info("Metodo deposita() - Transazione registrata correttamente");
        else LOGGER.warn("Metodo deposita() - Errore durante la registrazione della transazione");

        return result;
    }

    @Override
    public boolean deposita(String nomeProdotto, int quantita)
    {
        LOGGER.info("Metodo deposita() - Inizio");

        EntityMagazzino prodotto = magazzino.getProdotto(nomeProdotto);

        if(prodotto == null)
        {
            prodotto = new EntityMagazzino();
            prodotto.setNome(nomeProdotto);
            prodotto.setGiacenza(quantita);
            //            magazzino.aggiungiProdotto(prodotto);
        }

        EntityRegistro transazione = new EntityRegistro();
        transazione.setProdotto(prodotto);
        transazione.setQuantita(quantita);
        transazione.setDataTransazione(LocalDate.now());
        transazione.setTipoTransazione(TipoTransazione.INGRESSO);
        transazione.setDestinatario("Magazzino");

        return deposita(transazione, quantita);
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    @Override
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        return magazzino.aggiungiProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto)
    {
        return magazzino.cancellaProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        return magazzino.cancellaProdotto(nomeProdotto);
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    @Override
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        return magazzino.modificaProdotto(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     */
    @Override
    public boolean exists(EntityMagazzino prodotto)
    {
        return magazzino.exists(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     */
    @Override
    public boolean exists(String nomeProdotto)
    {
        return magazzino.exists(nomeProdotto);
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    @Override
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione)
    {
        return registro.aggiungiTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityRegistro cancellaTransazione(EntityRegistro transazione)
    {
        return registro.cancellaTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityRegistro cancellaTransazione(Long id)
    {
        return registro.cancellaTransazione(id);
    }

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     */
    @Override
    public EntityRegistro modificaTransazione(EntityRegistro transazione)
    {
        return registro.modificaTransazione(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    @Override
    public boolean exists(EntityRegistro transazione)
    {
        return registro.exists(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    @Override
    public boolean exists(Long id)
    {
        return registro.exists(id);
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     */
    @Override
    public List<String> getNomiProdotti()
    {
        return magazzino.getNomiProdotti();
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     */
    @Override
    public List<EntityMagazzino> getProdotti()
    {
        return magazzino.getProdotti();
    }

    /**
     * Metodo che restituisce la lista delle transazioni registrate nel registro
     * @return la lista delle transazioni presenti nel registro
     */
    @Override
    public List<EntityRegistro> getTransazioni()
    {
        return registro.getListaTransazioni();
    }
}
