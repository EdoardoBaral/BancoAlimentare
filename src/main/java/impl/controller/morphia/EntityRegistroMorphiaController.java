package impl.controller.morphia;

import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateOperations;
import interfaces.EntityRegistroController;
import om.EntityRegistro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static utils.ConnectionParams.DATABASE;
import static utils.ConnectionParams.URI;

/**
 * EntityRegistroMorphiaController. Classe che permette di realizzare un controller per gestire le transazioni del Banco
 * Alimentare salvandole su MongoDB, utilizzando il framework Morphia.
 * La classe fa uso di un'istanza di TransactionIDMorphiaController per controllare la generazione degli identificativi da
 * assegnare alle transazioni.
 *
 * @author Edoardo Baral
 */
public class EntityRegistroMorphiaController implements EntityRegistroController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRegistroMorphiaController.class);

    private Datastore datastore;
    private TransactionIDMorphiaController idGen;

    /**
     * Metodo costruttore della classe EntityRegistroMorphiaController
     */
    public EntityRegistroMorphiaController()
    {
        Morphia morphia = new Morphia();
        morphia.mapPackage("om");
        MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
        datastore = morphia.createDatastore(mongoClient, DATABASE);
        datastore.ensureIndexes();

        idGen = TransactionIDMorphiaController.getInstance();

        LOGGER.info("EntityRegistroMorphiaController - Nuova istanza creata");
    }

    /**
     * Metodo che permette di aggiungere una transazione al registro del Banco Alimentare
     * @param transazione: transazione da aggiungere al registro
     * @return la transazione aggiunta al registro del Banco Alimentare, null in caso d'errore
     */
    @Override
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione)
    {
        Long id = idGen.getNextId();
        transazione.setId(id);

        if(!exists(transazione))
        {
            Key<EntityRegistro> key = datastore.save(transazione);
            LOGGER.info("EntityRegistroMorphiaController - Aggiunta nuova transazione: "+ transazione);
            idGen.incrementaId();
            return transazione;
        }
        else
        {
            LOGGER.info("EntityRegistroMorphiaController - Transazione già presente: "+ transazione);
            return null;
        }
    }

    /**
     * Metodo che permette di cancellare una transazione dal registro del Banco Alimentare
     * @param transazione: transazione da cancellare
     * @return la transazione cancellata, null in caso la transazione non sia presente nel registro
     */
    @Override
    public EntityRegistro cancellaTransazione(EntityRegistro transazione)
    {
        Query<EntityRegistro> selectQuery = datastore.createQuery(EntityRegistro.class).field("id").equal(transazione.getId());
        EntityRegistro result = datastore.findAndDelete(selectQuery);
        LOGGER.info("EntityRegistroMorphiaController - Cancellazione transazione: "+ transazione);
        return result;
    }

    /**
     * Metodo che permette di cancellare una transazione dal registro del Banco Alimentare
     * @param id: id della transazione da cancellare
     * @return la transazione cancellata, null in caso la transazione non sia presente nel registro
     */
    @Override
    public EntityRegistro cancellaTransazione(Long id)
    {
        Query<EntityRegistro> selectQuery = datastore.createQuery(EntityRegistro.class).field("id").equal(id);
        EntityRegistro result = datastore.findAndDelete(selectQuery);
        LOGGER.info("EntityRegistroMorphiaController - Cancellazione transazione: "+ id);
        return result;
    }

    /**
     * Metodo che permette di modificare una transazione nel registro del Banco Alimentare
     * @param transazione: transazione modificata
     * @return la transazione modificata, null in caso non sia presente nel registro
     */
    @Override
    public EntityRegistro modificaTransazione(EntityRegistro transazione)
    {
        Query<EntityRegistro> selectQuery = datastore.createQuery(EntityRegistro.class).field("id").equal(transazione.getId());
        UpdateOperations<EntityRegistro> updates = datastore.createUpdateOperations(EntityRegistro.class)
                .set("prodotto", transazione.getProdotto())
                .set("quantita", transazione.getQuantita())
                .set("destinatario", transazione.getDestinatario())
                .set("data", transazione.getDataTransazione())
                .set("tipo", transazione.getTipoTransazione());
        EntityRegistro result = datastore.findAndModify(selectQuery, updates);
        LOGGER.info("EntityRegistroMorphiaController - Aggiornamento transazione: "+ transazione);
        return result;
    }

    /**
     * Metodo che verifica l'esistenza nel registro del Banco Alimentare della transazione passata come argomento
     * @param transazione: transazione da controllare
     * @return true se la transazione è stata registrata, false altrimenti
     */
    public boolean exists(EntityRegistro transazione)
    {
        LOGGER.info("EntityRegistroMorphiaController - Verifica esistenza transazione: "+ transazione);
        List<EntityRegistro> list = datastore.find(EntityRegistro.class).field("id").equal(transazione.getId()).find().toList();
        return (list == null || list.isEmpty()) ? false : true;
    }

    /**
     * Metodo che verifica l'esistenza nel registro del Banco Alimentare della transazione il cui id viene passato come argomento
     * @param id: identificativo della transazione da controllare
     * @return true se la transazione è stata registrata, false altrimenti
     */
    public boolean exists(Long id)
    {
        LOGGER.info("EntityRegistroMorphiaController - Verifica esistenza transazione: "+ id);
        List<EntityRegistro> list = datastore.find(EntityRegistro.class).field("id").equal(id).find().toList();
        return (list == null || list.isEmpty()) ? false : true;
    }

    /**
     * Metodo che restituisce la lista delle transazioni registrate
     * @return la lista delle transazioni registrate
     */
    @Override
    public List<EntityRegistro> getListaTransazioni()
    {
        List<EntityRegistro> list = datastore.find(EntityRegistro.class).order(Sort.ascending("id")).find().toList();
        LOGGER.info("EntityRegistroMorphiaController - Recuperata lista transazioni");
        return list;
    }

    /**
     * Metodo che restituisce la transazione registrata il cui identificativo corrisponde a quello passato come argomento
     * @param id: identificativo della transazione da cercare
     * @return la transazione con identificativo id, se presente, altrimenti null
     */
    public EntityRegistro getTransazione(Long id)
    {
        List<EntityRegistro> list = datastore.find(EntityRegistro.class).field("id").equal(id).find().toList();
        return list.isEmpty() ? null : list.get(0);
    }
}
