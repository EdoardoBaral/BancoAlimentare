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
        MongoClientURI uri;
        MongoClient mongoClient = new MongoClient(URI);
        datastore = morphia.createDatastore(mongoClient, DATABASE);
        datastore.ensureIndexes();
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    @Override
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        Key<EntityMagazzino> key = datastore.save(prodotto);
        return key == null ? null : prodotto;
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
        return datastore.findAndDelete(selectQuery);
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
        return datastore.findAndDelete(selectQuery);
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
        return datastore.findAndModify(selectQuery, updates);
    }
    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se l'elemento esiste, false altrimenti
     */
    public boolean exists(EntityMagazzino prodotto)
    {
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
        int nuovaGiacenza = prodotto.getGiacenza() + quantita;
        prodotto.setGiacenza(nuovaGiacenza);
        Key<EntityMagazzino> key = datastore.merge(prodotto);
        return key == null ? false : true;
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
        int nuovaGiacenza = prodotto.getGiacenza() - quantita;
        if(nuovaGiacenza <= 0) return false;
        prodotto.setGiacenza(nuovaGiacenza);
        Key<EntityMagazzino> key = datastore.merge(prodotto);
        return key == null ? false : true;
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
        return list;
    }
}
