package impl.controller.mysql;

import interfaces.BancoAlimentareController;
import interfaces.EntityMagazzinoController;
import interfaces.EntityRegistroController;
import om.EntityMagazzino;
import om.EntityRegistro;
import om.TipoTransazione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BancoAlimentareDerbyController implements BancoAlimentareController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BancoAlimentareDerbyController.class);

    private EntityMagazzinoController magazzino;
    private EntityRegistroController registro;

    public BancoAlimentareDerbyController() throws SQLException
    {
        magazzino = new EntityMagazzinoDerbyController();
        registro = new EntityRegistroDerbyController();

        LOGGER.info("BancoAlimentareDerbyController - Nuova istanza creata");
    }

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da prelevare dal magazzino
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta
     * e se la transazione viene correttamente registrata, false altrimenti
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    private boolean preleva(EntityRegistro transazione, int quantita) throws SQLException
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
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public boolean preleva(String nomeProdotto, int quantita, String destinatario) throws SQLException
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
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    private boolean deposita(EntityRegistro transazione, int quantita) throws SQLException
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
    public boolean deposita(String nomeProdotto, int quantita) throws SQLException
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
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto) throws SQLException
    {
        return magazzino.aggiungiProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto) throws SQLException
    {
        return magazzino.cancellaProdotto(prodotto);
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityMagazzino cancellaProdotto(String nomeProdotto) throws SQLException
    {
        return magazzino.cancellaProdotto(nomeProdotto);
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto) throws SQLException
    {
        return magazzino.modificaProdotto(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public boolean exists(EntityMagazzino prodotto) throws SQLException
    {
        return magazzino.exists(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public boolean exists(String nomeProdotto) throws SQLException
    {
        return magazzino.exists(nomeProdotto);
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione) throws SQLException
    {
        return registro.aggiungiTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro cancellaTransazione(EntityRegistro transazione) throws SQLException
    {
        return registro.cancellaTransazione(transazione);
    }

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro cancellaTransazione(Long id) throws SQLException
    {
        return registro.cancellaTransazione(id);
    }

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public EntityRegistro modificaTransazione(EntityRegistro transazione) throws SQLException
    {
        return registro.modificaTransazione(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public boolean exists(EntityRegistro transazione) throws SQLException
    {
        return registro.exists(transazione);
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public boolean exists(Long id) throws SQLException
    {
        return registro.exists(id);
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public List<String> getNomiProdotti() throws SQLException
    {
        return magazzino.getNomiProdotti();
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public List<EntityMagazzino> getProdotti() throws SQLException
    {
        return magazzino.getProdotti();
    }

    /**
     * Metodo che restituisce la lista delle transazioni registrate nel registro
     * @return la lista delle transazioni presenti nel registro
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    @Override
    public List<EntityRegistro> getTransazioni() throws SQLException
    {
        return registro.getListaTransazioni();
    }

    @Override
    public int backupDatabase(int exitFunctionCode) throws SQLException
    {
        List<EntityMagazzino> listaProdotti = magazzino.getProdotti();
        List<EntityRegistro> listaTransazioni = registro.getListaTransazioni();

        try
        {
            LOGGER.info("BancoAlimentareDerbyController - Inizio backup database su file");
            scriviFileBackupDatabase(listaProdotti, listaTransazioni);
            LOGGER.info("BancoAlimentareDerbyController - Backup database su file completato");
        }
        catch(IOException ex)
        {
            throw new SQLException("Errore nella scrittura del file di backup del database\n"+ ex.getMessage());
        }

        return JFrame.EXIT_ON_CLOSE;
    }

    private void scriviFileBackupDatabase(List<EntityMagazzino> listaProdotti, List<EntityRegistro> listaTransazioni) throws IOException
    {
        String tabellaProdotti = "PRODOTTI";
        String tabellaTransazioni = "TRANSAZIONI";
        String colonneProdotti = " (NOME, GIACENZA) ";
        String colonneTransazioni = " (PRODOTTO, QUANTITA, DESTINATARIO, DATA, TIPO) ";

        File backupFile = new File("BackupDatabase.sql");
        BufferedWriter bw = new BufferedWriter(new FileWriter(backupFile));

        if(!backupFile.exists())
            backupFile.createNewFile();
        else
        {
            backupFile.delete();
            backupFile.createNewFile();
        }

        for(EntityMagazzino prodotto : listaProdotti)
        {
            String query = "insert into "+ tabellaProdotti + colonneProdotti +"values ('"+ prodotto.getNome() +"', "+ prodotto.getGiacenza() +");\n";
            bw.append(query);
        }
        bw.append("\n");
        bw.flush();

        for(EntityRegistro transazione : listaTransazioni)
        {
            String query = "insert into "+ tabellaTransazioni + colonneTransazioni +"values ('"+ transazione.getProdotto().getNome() +"', "+ transazione.getQuantita() +
                           ", '"+ transazione.getDestinatario() +"', '"+ transazione.getDataTransazione().toString() +"', '"+ transazione.getTipoTransazione() +"');\n";
            bw.append(query);
        }
        bw.flush();

        bw.close();
    }
}
