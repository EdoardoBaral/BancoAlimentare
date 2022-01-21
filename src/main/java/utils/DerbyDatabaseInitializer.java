package utils;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyDatabaseInitializer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DerbyDatabaseInitializer.class);

    private Connection conn;
    private Statement st;

    public DerbyDatabaseInitializer() throws SQLException
    {
        DriverManager.registerDriver(new EmbeddedDriver());
        conn = DriverManager.getConnection(ConnectionParams.URL);
        st = conn.createStatement();
        LOGGER.info("DerbyDatabaseInitializer - Nuova istanza creata");
    }

    public boolean verificaEsistenzaTabelle()
    {
        return verificaPresenzaTabellaProdotti() && verificaPresenzaTabellaTransazioni();
    }

    private boolean verificaPresenzaTabellaProdotti()
    {
        try
        {
            String queryMagazzino = "select * from PRODOTTI";
            st.executeQuery(queryMagazzino);
            return true;
        }
        catch(SQLException ex)
        {
            return false;
        }
    }

    private boolean verificaPresenzaTabellaTransazioni()
    {
        try
        {
            String queryRegistro = "select * from TRANSAZIONI";
            st.executeQuery(queryRegistro);
            return true;
        }
        catch(SQLException ex) {
            return false;
        }
    }

    public void initDatabaseDerby() throws SQLException
    {
        if(verificaEsistenzaTabelle())
            LOGGER.info("DerbyDatabaseInitializer - Database gia' presente, nessuna inizializzazione necessaria");
        else
        {
            creaTabellaProdotti();
            creaTabellaTransazioni();
            LOGGER.info("DerbyDatabaseInitializer - Tabelle create, database inizializzato");
        }
    }

    private void creaTabellaProdotti() throws SQLException
    {
        StringBuilder sb = new StringBuilder("create table PRODOTTI ");
        sb.append("( ");
        sb.append("NOME varchar(50) primary key, ");
        sb.append("GIACENZA smallint not null, ");
        sb.append("check (GIACENZA >= 0) ");
        sb.append(")");

        st.execute(sb.toString());
        LOGGER.info("DerbyDatabaseInitializer - Tabella PRODOTTI creata");
    }

    private void creaTabellaTransazioni() throws SQLException
    {
        StringBuilder sb = new StringBuilder("create table TRANSAZIONI ");
        sb.append("( ");
        sb.append("ID int primary key generated always as identity(start with 1, increment by 1), ");
        sb.append("PRODOTTO varchar(50) not null, ");
        sb.append("QUANTITA int not null,");
        sb.append("DESTINATARIO varchar(50), ");
        sb.append("DATA date not null, ");
        sb.append("TIPO varchar(8) not null, ");
        sb.append("foreign key (PRODOTTO) references PRODOTTI(NOME) on delete cascade, ");
        sb.append("check (QUANTITA > 0), ");
        sb.append("check (TIPO = 'INGRESSO' or TIPO = 'USCITA')");
        sb.append(")");

        st.execute(sb.toString());
        LOGGER.info("DerbyDatabaseInitializer - Tabella TRANSAZIONI creata");
    }

    public void resetDatabase() throws SQLException
    {
        String queryTransazioni = "drop table TRANSAZIONI";
        String queryProdotti = "drop table PRODOTTI";

        if(verificaPresenzaTabellaTransazioni())
        {
            st.execute(queryTransazioni);
            LOGGER.info("DerbyDatabaseInitializer - Tabella TRANSAZIONI cancellata");
        }

        if(verificaPresenzaTabellaProdotti())
        {
            st.execute(queryProdotti);
            LOGGER.info("DerbyDatabaseInitializer - Tabella PRODOTTI cancellata");
        }
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Inizio programma");
        DerbyDatabaseInitializer dbInit = new DerbyDatabaseInitializer();
        dbInit.resetDatabase();
        dbInit.initDatabaseDerby();
        System.out.println("Fine programma");
    }
}
