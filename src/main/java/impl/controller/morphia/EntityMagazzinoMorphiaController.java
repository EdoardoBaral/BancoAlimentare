package impl.controller.morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateOperations;
import interfaces.EntityMagazzinoController;
import om.EntityMagazzino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static utils.ConnectionParams.DATABASE;
import static utils.ConnectionParams.URI;

/**
 * EntityMagazzinoMorphiaController. Classe che implementa l'interfaccia EntityMagazzinoController e realizza un connettore
 * verso il database MongoDB, sfruttando l'Object Document Mapper (ODM) Morphia.
 *
 * @author Edoardo Baral
 */
public class EntityMagazzinoMorphiaController implements EntityMagazzinoController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMagazzinoMorphiaController.class);

    private Datastore datastore;

    /**
     * Costruttore della classe EntityMagazzinoMorphiaController che stabilisce una connessione verso MongoDB
     */
    public EntityMagazzinoMorphiaController()
    {
        Morphia morphia = new Morphia();
        morphia.mapPackage("om");
        MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
        datastore = morphia.createDatastore(mongoClient, DATABASE);
        datastore.ensureIndexes();

        LOGGER.info("EntityMagazzinoMorphiaController - Nuova istanza creata");
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    @Override
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        if(!exists(prodotto))
        {
            Key<EntityMagazzino> key = datastore.save(prodotto);
            LOGGER.info("EntityMagazzinoMorphiaController - Aggiunto nuovo prodotto: "+ prodotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMorphiaController - Prodotto già esistente: "+ prodotto);
            return null;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto)
    {
        Query<EntityMagazzino> selectQuery = datastore.createQuery(EntityMagazzino.class).field("nome").equalIgnoreCase(prodotto.getNome());
        EntityMagazzino result = datastore.findAndDelete(selectQuery);
        LOGGER.info("EntityMagazzinoMorphiaController - Cancellazione prodotto: "+ result);
        return result;
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    @Override
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        Query<EntityMagazzino> selectQuery = datastore.createQuery(EntityMagazzino.class).field("nome").equalIgnoreCase(nomeProdotto);
        EntityMagazzino result = datastore.findAndDelete(selectQuery);
        LOGGER.info("EntityMagazzinoMorphiaController - Cancellazione prodotto: "+ result);
        return result;
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    @Override
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        Query<EntityMagazzino> selectQuery = datastore.createQuery(EntityMagazzino.class).field("nome").equalIgnoreCase(prodotto.getNome());
        UpdateOperations<EntityMagazzino> updates = datastore.createUpdateOperations(EntityMagazzino.class).set("giacenza", prodotto.getGiacenza());
        EntityMagazzino result = datastore.findAndModify(selectQuery, updates);
        LOGGER.info("EntityMagazzinoMorphiaController - Aggiornamento prodotto: "+ result);
        return result;
    }
    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se l'elemento esiste, false altrimenti
     */
    public boolean exists(EntityMagazzino prodotto)
    {
        LOGGER.info("EntityMagazzinoMorphiaController - Verifica esistenza prodotto: "+ prodotto);
        List<EntityMagazzino> list = datastore.find(EntityMagazzino.class).field("nome").equalIgnoreCase(prodotto.getNome()).find().toList();
        return (list == null || list.isEmpty()) ? false : true;
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se l'elemento esiste, false altrimenti
     */
    public boolean exists(String nomeProdotto)
    {
        LOGGER.info("EntityMagazzinoMorphiaController - Verifica esistenza prodotto: "+ nomeProdotto);
        List<EntityMagazzino> list = datastore.find(EntityMagazzino.class).field("nome").equalIgnoreCase(nomeProdotto).find().toList();
        return (list == null || list.isEmpty()) ? false : true;
    }

    /**
     * Metodo che permette di incrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da aggiungere alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene incrementata, false altrimenti
     */
    @Override
    public boolean incrementaGiacenza(EntityMagazzino prodotto, int quantita)
    {
        Query<EntityMagazzino> selectQuery = datastore.createQuery(EntityMagazzino.class).field("nome").equalIgnoreCase(prodotto.getNome());
        UpdateOperations<EntityMagazzino> updates = datastore.createUpdateOperations(EntityMagazzino.class).inc("giacenza", quantita);
        EntityMagazzino result = datastore.findAndModify(selectQuery, updates);

        if(result != null)
        {
            LOGGER.info("EntityMagazzinoMorphiaController - Incremento giacenza prodotto: "+ result);
            return true;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMorphiaController - Incremento giacenza prodotto inesistente: "+ result);
            return false;
        }
    }

    /**
     * Metodo che permette di decrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da sottrarre alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene decrementata, false altrimenti
     */
    @Override
    public boolean decrementaGiacenza(EntityMagazzino prodotto, int quantita)
    {
        Query<EntityMagazzino> selectQuery = datastore.createQuery(EntityMagazzino.class).field("nome").equalIgnoreCase(prodotto.getNome());
        UpdateOperations<EntityMagazzino> updates = datastore.createUpdateOperations(EntityMagazzino.class).dec("giacenza", quantita);
        EntityMagazzino result = datastore.findAndModify(selectQuery, updates);

        if(result != null)
        {
            LOGGER.info("EntityMagazzinoMorphiaController - Decremento giacenza prodotto: "+ result);
            return true;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMorphiaController - Decremento giacenza prodotto inesistente: "+ result);
            return false;
        }
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     */
    @Override
    public List<String> getNomiProdotti()
    {
        List<EntityMagazzino> list = datastore.find(EntityMagazzino.class).order(Sort.ascending("nome")).find().toList();
        List<String> listNomi = new ArrayList<>();

        for(EntityMagazzino prodotto : list)
        {
            listNomi.add(prodotto.getNome());
        }

        LOGGER.info("EntityMagazzinoMorphiaController - Recuperata lista nomi prodotti");
        return listNomi;
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     */
    @Override
    public List<EntityMagazzino> getProdotti()
    {
        List<EntityMagazzino> list = datastore.find(EntityMagazzino.class).order(Sort.ascending("nome")).find().toList();
        LOGGER.info("EntityMagazzinoMorphiaController - Recuperata lista prodotti");
        return list;
    }

    /**
     * Metodo che cerca sul database un determinato prodotto in base al nome passato come argomento
     * @param nome: nome del prodotto da cercare
     * @return il prodotto indicato, se esiste, altrimenti null;
     */
    public EntityMagazzino getProdotto(String nome)
    {
        List<EntityMagazzino> result = datastore.find(EntityMagazzino.class).field("nome").equalIgnoreCase(nome).find().toList();
        return result.isEmpty() ? null : result.get(0);
    }
}
