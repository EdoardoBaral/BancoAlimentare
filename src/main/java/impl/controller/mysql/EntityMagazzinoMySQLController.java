package impl.controller.mysql;

import interfaces.EntityMagazzinoController;
import om.EntityMagazzino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConnectionParams;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityMagazzinoMySQLController. Classe che implementa l'interfaccia EntityMagazzinoController e realizza un connettore
 * verso il database MySQL.
 *
 * @author Edoardo Baral
 */
public class EntityMagazzinoMySQLController implements EntityMagazzinoController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMagazzinoMySQLController.class);

    private final String url = ConnectionParams.URL;
    private final String username = ConnectionParams.USERNAME;
    private final String password = ConnectionParams.PASSWORD;
    private Connection conn;
    private Statement st;

    /**
     * Costruttore della classe EntityMagazzinoMySQLController che stabilisce una connessione verso il database MySQL
     * @throws SQLException in caso di errori nel tentativo di connessione al database
     */
    public EntityMagazzinoMySQLController() throws SQLException
    {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        conn = DriverManager.getConnection(url, username, password);
        st = conn.createStatement();
        LOGGER.info("EntityMagazzinoMySQLController - Nuova istanza creata");
    }

    /**
     * Metodo che permette di chiudere la connessione verso il database MySQL
     * @throws SQLException nel caso n cui si verifichi un errore nel chiudere la connessione
     */
    public void close() throws SQLException
    {
        if(st != null)
            st.close();
        if(conn != null)
            conn.close();
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se era già stato inserito nel database
     * @throws SQLException in caso di errore nell'inserimento del record nel database
     */
    @Override
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto) throws SQLException
    {
        if(!exists(prodotto.getNome()))
        {
            String query = "insert into PRODOTTI (NOME, GIACENZA) values ('"+ prodotto.getNome() +"', "+ prodotto.getGiacenza() +")";
            st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            LOGGER.info("EntityMagazzinoMySQLController - Inserimento prodotto: "+ prodotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMySQLController - Tentativo di inserimento prodotto esistente: "+ prodotto);
            return null;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto) throws SQLException
    {
        if(exists(prodotto.getNome()))
        {
            String query = "delete from PRODOTTI where NOME = '"+ prodotto.getNome() +"'";
            st.executeUpdate(query);
            LOGGER.info("EntityMagazzinoMySQLController - Cancellazione prodotto: "+ prodotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMySQLController - Tentativo di cancellazione di un prodotto inesistente: "+ prodotto);
            return null;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public EntityMagazzino cancellaProdotto(String nomeProdotto) throws SQLException
    {
        EntityMagazzino prodotto = getProdotto(nomeProdotto);

        if(exists(nomeProdotto))
        {
            String query = "delete from PRODOTTI where NOME = '"+ nomeProdotto +"'";
            st.executeUpdate(query);
            LOGGER.info("EntityMagazzinoMySQLController - Cancellazione prodotto: "+ nomeProdotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMySQLController - Tentatico di cancellazione di un prodotto inesistente: "+ nomeProdotto);
            return null;
        }
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto) throws SQLException
    {
        if(exists(prodotto))
        {
            String query = "update PRODOTTI set GIACENZA = "+ prodotto.getGiacenza() +" where NOME = '"+ prodotto.getNome() +"'";
            st.executeUpdate(query);
            LOGGER.info("EntityMagazzinoMySQLController - Aggiornamento prodotto: "+ prodotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMySQLController - Tentativo aggiornamento prodotto inesistente: "+ prodotto);
            return null;
        }
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se l'elemento esiste, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    public boolean exists(EntityMagazzino prodotto) throws SQLException
    {
        String queryCheck = "select count(*) as NUM_RIGHE from PRODOTTI where NOME = '"+ prodotto.getNome() +"'";
        ResultSet rs = st.executeQuery(queryCheck);
        if(rs.next())
        {
            int contatore = rs.getInt("NUM_RIGHE");
            return contatore == 0 ? false : true;
        }
        return false;
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se l'elemento esiste, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    public boolean exists(String nomeProdotto) throws SQLException
    {
        String queryCheck = "select count(*) as NUM_RIGHE from PRODOTTI where NOME = '"+ nomeProdotto +"'";
        ResultSet rs = st.executeQuery(queryCheck);
        if(rs.next())
        {
            int contatore = rs.getInt("NUM_RIGHE");
            return contatore == 0 ? false : true;
        }
        return false;
    }

    /**
     * Metodo che permette di incrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da aggiungere alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene incrementata, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public boolean incrementaGiacenza(EntityMagazzino prodotto, int quantita) throws SQLException
    {
        if(quantita <= 0)
        {
            LOGGER.warn("EntityMagazzinoMySQLController - Incremento giacenza prodotto non eseguito causa quantità non valida");
            return false;
        }

        if(exists(prodotto))
        {
            String query = "update PRODOTTI set GIACENZA = GIACENZA + "+ quantita +" where NOME = '"+ prodotto.getNome() +"'";
            st.executeUpdate(query);
            LOGGER.info("EntityMagazzinoMySQLController - Incremento della giacenza del prodotto "+ prodotto.getNome());
            return true;
        }
        else
        {
            LOGGER.warn("EntityMagazzinoMySQLController - Incremento giacenza prodotto non eseguito causa prodotto non trovato");
            return false;
        }
    }

    /**
     * Metodo che permette di decrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da sottrarre alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene decrementata, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public boolean decrementaGiacenza(EntityMagazzino prodotto, int quantita) throws SQLException
    {
        if(quantita <= 0)
        {
            LOGGER.warn("EntityMagazzinoMySQLController - Decremento giacenza prodotto non eseguito causa quantità non valida");
            return false;
        }

        if(exists(prodotto))
        {
            String query = "update PRODOTTI set GIACENZA = GIACENZA - "+ quantita +" where NOME = '"+ prodotto.getNome() +"'";
            st.executeUpdate(query);
            LOGGER.info("EntityMagazzinoMySQLController - Decremento della giacenza del prodotto "+ prodotto.getNome());
            return true;
        }
        else
        {
            LOGGER.warn("EntityMagazzinoMySQLController - Decremento giacenza prodotto non eseguito causa prodotto non trovato");
            return false;
        }
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public List<String> getNomiProdotti() throws SQLException
    {
        String query = "select NOME from PRODOTTI";
        ResultSet rs = st.executeQuery(query);
        List<String> nomiProdotti = new ArrayList<>();

        while(rs.next())
        {
            String nome = rs.getString("NOME");
            nomiProdotti.add(nome);
        }

        LOGGER.info("EntityMagazzinoMySQLController - Recuperata lista nomi prodotti");
        return nomiProdotti;
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public List<EntityMagazzino> getProdotti() throws SQLException
    {
        String query = "select * from PRODOTTI order by NOME asc";
        ResultSet rs = st.executeQuery(query);
        List<EntityMagazzino> listaProdotti = new ArrayList<>();

        while(rs.next())
        {
            EntityMagazzino prodotto = new EntityMagazzino();
            prodotto.setNome(rs.getString("NOME"));
            prodotto.setGiacenza(rs.getInt("GIACENZA"));
            listaProdotti.add(prodotto);
        }

        LOGGER.info("EntityMagazzinoMySQLController - Recuperata lista prodotti");
        return listaProdotti;
    }

    /**
     * Metodo che cerca sul database un determinato prodotto in base al nome passato come argomento
     * @param nome: nome del prodotto da cercare
     * @return il prodotto indicato, se esiste, altrimenti null;
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    @Override
    public EntityMagazzino getProdotto(String nome) throws SQLException
    {
        String query = "select * from PRODOTTI where NOME = '"+ nome +"' order by NOME asc";
        ResultSet rs = st.executeQuery(query);

        if(rs.next())
        {
            EntityMagazzino prodotto = new EntityMagazzino();
            prodotto.setNome(rs.getString("NOME"));
            prodotto.setGiacenza(rs.getInt("GIACENZA"));

            LOGGER.info("EntityMagazzinoMySQLController - Recupero prodotto - "+ prodotto);
            return prodotto;
        }
        else
        {
            LOGGER.info("EntityMagazzinoMySQLController - Tentativo di recupero prodotto inesistente - "+ nome);
            return null;
        }
    }
}
