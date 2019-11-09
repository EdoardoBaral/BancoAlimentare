package impl.controller.morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import om.TransactionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static utils.ConnectionParams.DATABASE;
import static utils.ConnectionParams.URI;

public class TransactionIDMorphiaController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionIDMorphiaController.class);

    private static TransactionIDMorphiaController instance = null;
    private Datastore datastore;

    private TransactionIDMorphiaController()
    {
        Morphia morphia = new Morphia();
        morphia.mapPackage("om");
        MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
        datastore = morphia.createDatastore(mongoClient, DATABASE);
        datastore.ensureIndexes();

        LOGGER.info("TransactionIDMorphiaController - Nuova istanza creata");
    }

    public static TransactionIDMorphiaController getInstance()
    {
        if(instance == null)
            instance = new TransactionIDMorphiaController();
        return instance;
    }

    public Long getNextId()
    {
        List<TransactionID> result = datastore.find(TransactionID.class).field("className").equalIgnoreCase("EntityRegistro").find().toList();
        if(result.isEmpty())
        {
            //Se non è presente un TransactionID nella collection, ne crea uno nuovo con id = 1 e ce lo salva
            TransactionID idNew = new TransactionID();
            datastore.save(idNew);
            return idNew.getId();
        }
        else
        {
            TransactionID idDb = result.get(0);
            return idDb.getId();
        }
    }

    public boolean incrementaId()
    {
        Query<TransactionID> selectQuery = datastore.find(TransactionID.class).field("className").equalIgnoreCase("EntityRegistro");
        UpdateOperations<TransactionID> updates = datastore.createUpdateOperations(TransactionID.class).inc("id");
        TransactionID id = datastore.findAndModify(selectQuery, updates);

        if(id != null)
        {
            LOGGER.info("TransactionIDMorphiaController - Incremento TransactionID avvenuto");
            return true;
        }
        else
        {
            LOGGER.warn("TransactionIDMorphiaController - Incremento TransactionID non avvenuto");
            return false;
        }
    }
}