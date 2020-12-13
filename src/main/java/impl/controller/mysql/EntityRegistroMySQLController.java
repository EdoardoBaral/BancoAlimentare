package impl.controller.mysql;

import interfaces.EntityRegistroController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConnectionParams;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * EntityRegistroMySQLController. Classe che permette di realizzare un controller per gestire le transazioni del Banco
 * Alimentare salvandole su un database MySQL.
 *
 * @author Edoardo Baral
 */
public class EntityRegistroMySQLController implements EntityRegistroController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRegistroMySQLController.class);

    private final String url = ConnectionParams.URL;
    private final String username = ConnectionParams.USERNAME;
    private final String password = ConnectionParams.PASSWORD;
    private Connection conn;
    private Statement st;

    /**
     * Metodo costruttore della classe EntityRegistroMySQLController
     * @throws SQLException in caso di errori nel tentativo di connessione al database
     */
    public EntityRegistroMySQLController() throws SQLException
    {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        conn = DriverManager.getConnection(url, username, password);
        st = conn.createStatement();
        LOGGER.info("EntityRegistroMySQLController - Nuova istanza creata");
    }

    /**
     * Metodo che permette di aggiungere una transazione al registro del Banco Alimentare
     * @param transazione: transazione da aggiungere al registro
     * @return la transazione aggiunta al registro del Banco Alimentare
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione) throws SQLException
    {
        String queryColonne = "insert into TRANSAZIONI (PRODOTTO, QUANTITA, DESTINATARIO, DATA, TIPO) ";
        String queryValori;
        if(transazione.getDestinatario() == null)
            queryValori = "values ('"+ transazione.getProdotto().getNome() +"', "+ transazione.getQuantita() +", null, '"+ transazione.getDataTransazione().toString() +"', '"+ transazione.getTipoTransazione() +"')";
        else
            queryValori = "values ('"+ transazione.getProdotto().getNome() +"', "+ transazione.getQuantita() +", '"+ transazione.getDestinatario() +"', '"+ transazione.getDataTransazione().toString() +"', '"+ transazione.getTipoTransazione() +"')";

        String query = queryColonne + queryValori;
        st.executeUpdate(query);
        LOGGER.info("EntityRegistroMySQLController - Aggiunta nuova transazione: "+ transazione);
        return transazione;
    }

    /**
     * Metodo che permette di cancellare una transazione dal registro del Banco Alimentare
     * @param transazione: transazione da cancellare
     * @return la transazione cancellata, null in caso la transazione non sia presente nel registro
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro cancellaTransazione(EntityRegistro transazione) throws SQLException
    {
        if(exists(transazione))
        {
            String query = "delete from TRANSAZIONI where ID = "+ transazione.getId();
            st.executeUpdate(query);
            LOGGER.info("EntityRegistroMySQLController - Cancellazione transazione: "+ transazione);
            return transazione;
        }
        else
        {
            LOGGER.info("EntityRegistroMySQLController - Tentativo di cancellazione transazione inesistente: "+ transazione);
            return null;
        }
    }

    /**
     * Metodo che permette di cancellare una transazione dal registro del Banco Alimentare
     * @param id: id della transazione da cancellare
     * @return la transazione cancellata, null in caso la transazione non sia presente nel registro
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro cancellaTransazione(Long id) throws SQLException
    {
        if(exists(id))
        {
            EntityRegistro transazione = getTransazione(id);

            String query = "delete from TRANSAZIONI where ID = "+ id;
            st.executeUpdate(query);
            LOGGER.info("EntityRegistroMySQLController - Cancellazione transazione: "+ id);
            return transazione;
        }
        else
        {
            LOGGER.info("EntityRegistroMySQLController - Tentativo di cancellazione transazione inesistente: "+ id);
            return null;
        }
    }

    /**
     * Metodo che permette di modificare una transazione nel registro del Banco Alimentare
     * @param transazione: transazione modificata
     * @return la transazione modificata, null in caso non sia presente nel registro
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro modificaTransazione(EntityRegistro transazione) throws SQLException
    {
        if(exists(transazione))
        {
            String queryHeader = "update TRANSAZIONI ";
            String queryValues = "set PRODOTTO = '"+ transazione.getProdotto().getNome() +"', ";

            if(transazione.getDestinatario() == null)
                queryValues += "DESTINATARIO = null, ";
            else
                queryValues += "DESTINATARIO = '"+ transazione.getDestinatario() +"', ";

            queryValues += "DATA = '"+ transazione.getDataTransazione().toString() +"', TIPO = '"+ transazione.getTipoTransazione() +"' ";
            String queryCondition = "where ID = "+ transazione.getId();
            String query = queryHeader + queryValues + queryCondition;

            st.executeUpdate(query);
            LOGGER.info("EntityRegistroMySQLController - Aggiornamento transazione: "+ transazione);
            return transazione;
        }
        else
        {
            LOGGER.info("EntityRegistroMySQLController - Tentativo di aggiornamento transazione inesistente: "+ transazione);
            return null;
        }
    }

    /**
     * Metodo che verifica l'esistenza nel registro del Banco Alimentare della transazione passata come argomento
     * @param transazione: transazione da controllare
     * @return true se la transazione è stata registrata, false altrimenti
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    public boolean exists(EntityRegistro transazione) throws SQLException
    {
        String query = "select count(*) as NUM_RIGHE from TRANSAZIONI where ID = "+ transazione.getId();
        ResultSet rs = st.executeQuery(query);

        if(rs.next())
        {
            int contatore = rs.getInt("NUM_RIGHE");
            return contatore == 0 ? false : true;
        }

        return false;
    }

    /**
     * Metodo che verifica l'esistenza nel registro del Banco Alimentare della transazione il cui id viene passato come argomento
     * @param id: identificativo della transazione da controllare
     * @return true se la transazione è stata registrata, false altrimenti
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    public boolean exists(Long id) throws SQLException
    {
        String query = "select count(*) as NUM_RIGHE from TRANSAZIONI where ID = "+ id;
        ResultSet rs = st.executeQuery(query);

        if(rs.next())
        {
            int contatore = rs.getInt("NUM_RIGHE");
            return contatore == 0 ? false : true;
        }

        return false;
    }

    /**
     * Metodo che restituisce la lista delle transazioni registrate
     * @return la lista delle transazioni registrate
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public List<EntityRegistro> getListaTransazioni() throws SQLException
    {
        String query = "select * from TRANSAZIONI t inner join PRODOTTI p on (t.PRODOTTO = p.NOME) order by ID asc";
        ResultSet rs = st.executeQuery(query);
        List<EntityRegistro> listaTransazioni = new ArrayList<>();

        while(rs.next())
        {
            EntityRegistro transazione = new EntityRegistro();
            transazione.setId(Long.valueOf(rs.getInt("ID")));

            EntityMagazzino prodotto = new EntityMagazzino();
            prodotto.setNome(rs.getString("NOME"));
            prodotto.setGiacenza(rs.getInt("GIACENZA"));

            transazione.setProdotto(prodotto);
            transazione.setQuantita(rs.getInt("QUANTITA"));
            transazione.setDestinatario(rs.getString("DESTINATARIO"));

            Date data = rs.getDate("DATA");
            LocalDate dataConvertita = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            transazione.setDataTransazione(dataConvertita);

            transazione.setTipoTransazione(TipoTransazione.valueOf(rs.getString("TIPO")));

            listaTransazioni.add(transazione);
        }

        LOGGER.info("EntityRegistroMySQLController - Recuperata lista transazioni");
        return listaTransazioni;
    }

    /**
     * Metodo che restituisce la transazione registrata il cui identificativo corrisponde a quello passato come argomento
     * @param id: identificativo della transazione da cercare
     * @return la transazione con identificativo id, se presente, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    public EntityRegistro getTransazione(Long id) throws SQLException
    {
        String query = "select * from TRANSAZIONI t inner join PRODOTTI p on (t.PRODOTTO = p.NOME) where ID = "+ id +" order by ID asc";
        ResultSet rs = st.executeQuery(query);

        if(rs.next())
        {
            EntityRegistro transazione = new EntityRegistro();
            transazione.setId(Long.valueOf(rs.getInt("ID")));

            EntityMagazzino prodotto = new EntityMagazzino();
            prodotto.setNome(rs.getString("NOME"));
            prodotto.setGiacenza(rs.getInt("GIACENZA"));

            transazione.setProdotto(prodotto);
            transazione.setQuantita(rs.getInt("QUANTITA"));
            transazione.setDestinatario(rs.getString("DESTINATARIO"));

            Date data = rs.getDate("DATA");
            LocalDate dataConvertita = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            transazione.setDataTransazione(dataConvertita);

            transazione.setTipoTransazione(TipoTransazione.valueOf(rs.getString("TIPO")));

            LOGGER.info("EntityRegistroMySQLController - Recuperata transazione");
            return transazione;
        }

        return null;
    }
}
