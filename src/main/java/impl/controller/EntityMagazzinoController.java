package impl.controller;

import om.EntityMagazzino;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityMagazzinoController. Classe che permette di realizzare un controller per la gestione dei prodotti presenti in magazzino.
 * Lo stato del magazzino viene scritto su un apposito file CSV.
 * La classe contiene metodi che permettono la lettura del file per recuperarne l'intero contenuto e mapparlo all'interno di una lista e metodi che permettono la scrittura del
 * contenuto della lista sul medesimo file, in caso di aggiornamenti.
 *
 * @author Edoardo Baral
 */
public class EntityMagazzinoController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMagazzinoController.class);

    private static final String PATH = "archivio/magazzino.csv";
    private static final String INTESTAZIONE = "NOME;GIACENZA";
    private static final String SEPARATORE = ";";

    private List<EntityMagazzino> listaProdotti;

    /**
     * Costruttore della classe EntityMagazzinoController
     * @throws IOException in caso di problemi di lettura/scrittura del file CSV
     */
    public EntityMagazzinoController() throws IOException
    {
        File fileMagazzino = new File(PATH);
        fileMagazzino.createNewFile();
        listaProdotti = new ArrayList<>();

        LOGGER.info("Istanziato un nuovo oggetto EntityMagazzinoController - "+ this.toString());
    }

    /**
     * Metodo che permette la lettura del file CSV e il mapping del suo contenuto all'interno di una lista ordinata
     * @throws IOException in caso di problemi di lettura del file CSV
     */
    public void mappingDaFile() throws IOException
    {
        File fileMagazzino = new File(PATH);
        BufferedReader br = new BufferedReader(new FileReader(fileMagazzino));
        String riga;

        LOGGER.info("Metodo mappingDaFile() - Inizio lettura da file");

        while( (riga = br.readLine()) != null )
        {
            if(!riga.equals(INTESTAZIONE))
            {
                String[] valori = riga.split(SEPARATORE);
                EntityMagazzino prodotto = new EntityMagazzino();
                prodotto.setNome(valori[0]);
                prodotto.setGiacenza(Integer.parseInt(valori[1]));
                aggiungiProdotto(prodotto);
            }
        }

        LOGGER.info("Metodo mappingDaFile() - Fine lettura dal file");
    }

    /**
     * Metodo che permette la scrittura del contenuto della lista dei prodotti nell'apposito file CSV, per archiviare il contenuto del magazzino
     * @throws IOException in caso di problemi di scrittura sul file CSV
     */
    public void scriviProdottiSuFile() throws IOException
    {
        File fileMagazzino = new File(PATH);
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileMagazzino));

        LOGGER.info("Metodo scriviProdottiSuFile() - Inizio cancellazione contenuto obsoleto dal file");
        bw.write("");
        bw.flush();
        LOGGER.info("Metodo scriviProdottiSuFile() - Fine cancellazione contenuto obsoleto dal file");

        LOGGER.info("Metodo scriviProdottiSuFile() - Inizio scrittura su file");
        bw.append(INTESTAZIONE +"\n");
        for(EntityMagazzino prodotto : listaProdotti)
        {
            bw.append(prodotto.getNome() + ";" + prodotto.getGiacenza() + "\n");
            bw.flush();
        }
        LOGGER.info("Metodo scriviProdottiSuFile() - Fine scrittura su file");
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        LOGGER.info("Metodo aggiungiProdotto() - Inizio");
        int indice = exists(prodotto);
        if(indice >= 0)
        {
            LOGGER.warn("Metodo aggiungiProdotto() - Fine - Prodotto già presente in magazzino");
            return null;
        }
        else
        {
            indice = -(indice + 1);
            listaProdotti.add(indice, prodotto);
            ordinaListaProdotti();
            LOGGER.info("Metodo aggiungiProdotto() - Fine - Prodotto aggiunto al magazzino - "+ prodotto);
            return prodotto;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(EntityMagazzino prodotto)
    {
        LOGGER.info("Metodo cancellaProdotto() - Inizio");

        int indice = exists(prodotto);
        if(indice < 0)
        {
            LOGGER.warn("Metodo cancellaProdotto() - Fine - Prodotto non trovato in magazzino");
            return null;
        }
        else
        {
            // In caso di rimozione di un elemento della lista, non è necessario riordinare i rimanenti in quanto l'ordine rimane inalterato
            EntityMagazzino result = listaProdotti.remove(indice);

            LOGGER.info("Metodo cancellaProdotto() - Fine - Prodotto rimosso dal magazzino - "+ result);
            return result;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        LOGGER.info("Metodo cancellaProdotto() - Inizio");
        int indice = exists(nomeProdotto);
        if(indice < 0)
        {
            LOGGER.warn("Metodo cancellaProdotto() - Fine - Prodotto non trovato in magazzino");
            return null;
        }
        else
        {
            EntityMagazzino result = listaProdotti.remove(indice);

            LOGGER.info("Metodo cancellaProdotto() - Fine - Prodotto rimosso dal magazzino - "+ result);
            return result;
        }
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        LOGGER.info("Metodo modificaProdotto() - Inizio");
        int indice = exists(prodotto);
        if(indice < 0)
        {
            LOGGER.warn("Metodo modificaProdotto() - Fine - Prodotto non trovato in magazzino");
            return null;
        }
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            p.setGiacenza(prodotto.getGiacenza());
            ordinaListaProdotti();

            LOGGER.info("Metodo modificaProdotto() - Fine - Prodotto trovato in magazzino e modificato - "+ p);
            return p;
        }
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param prodotto: prodotto da cercare nel magazzino
     * @return l'indice del prodotto all'interno della lista, negativo nel caso non esista
     */
    public int exists(EntityMagazzino prodotto)
    {
        return exists(prodotto.getNome());
    }

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return l'indice del prodotto all'interno della lista, negativo nel caso non esista
     */
    public int exists(String nomeProdotto)
    {
        LOGGER.info("Metodo exists() - Inizio");

        int start = 0;
        int end = listaProdotti.size() - 1;

        if(listaProdotti.isEmpty())
        {
            LOGGER.warn("Metodo exists() - Fine - Prodotto non trovato: magazzino vuoto");
            return -1;
        }
        if(nomeProdotto.compareTo(listaProdotti.get(0).getNome()) < 0)
        {
            LOGGER.warn("Metodo exists() - Fine - Prodotto non trovato: inserimento in testa alla lista");
            return -1;
        }
        if(nomeProdotto.compareTo(listaProdotti.get(end).getNome()) > 0)
        {
            LOGGER.warn("Metodo exists() - Fine - Prodotto non trovato: inserimento in coda alla lista");
            return -(end + 1);
        }

        while(start <= end)
        {
            int middle = (start + end) >>> 1;
            String middleName = listaProdotti.get(middle).getNome();
            if(nomeProdotto.compareTo(middleName) < 0)
                end = middle - 1;
            else if(nomeProdotto.compareTo(middleName) > 0)
                start = middle + 1;
            else
            {
                LOGGER.info("Metodo exists() - Fine - Prodotto trovato alla posizione "+ middle);
                return middle;
            }
        }

        LOGGER.warn("Metodo exists() - Fine - Prodotto non trovato");
        return -(end + 1);
    }

    /**
     * Metodo che permette di incrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da aggiungere alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene incrementata, false altrimenti
     */
    public boolean incrementaGiacenza(EntityMagazzino prodotto, int quantita)
    {
        LOGGER.info("Metodo incrementaGiacenza() - Inizio");

        int indice = exists(prodotto);
        if(indice < 0)
        {
            prodotto.setGiacenza(quantita);

            LOGGER.info("Metodo exists() - Fine - Prodotto non trovato, aggiunto nel magazzino");
            return aggiungiProdotto(prodotto) != null ? true : false;
        }
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            int tot = p.getGiacenza() + quantita;
            p.setGiacenza(tot);

            LOGGER.info("Metodo exists() - Fine - Prodotto trovato in magazzino e giacenza incrementata");
            return true;
        }
    }

    /**
     * Metodo che permette di decrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da sottrarre alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene decrementata, false altrimenti
     */
    public boolean decrementaGiacenza(EntityMagazzino prodotto, int quantita)
    {
        LOGGER.info("Metodo decrementaGiacenza() - Inizio");

        int indice = exists(prodotto);
        if(indice < 0)
        {
            LOGGER.warn("Metodo decrementaGiacenza() - Fine - Prodotto non trovato in magazzino");
            return false;
        }
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            int tot = p.getGiacenza() - quantita;
            if(tot < 0)
                return false;
            p.setGiacenza(tot);

            LOGGER.info("Metodo decrementaGiacenza() - Fine - Prodotto trovato in magazzino e giacenza decrementata");
            return true;
        }
    }

    /**
     * Metodo che permette di ordinare la lista dei prodotti presenti in magazzino
     */
    private void ordinaListaProdotti()
    {
        LOGGER.info("Metodo ordinaListaProdotti() - Inizio");

        if(!listaProdotti.isEmpty() && listaProdotti.size() > 1)
            Collections.sort(listaProdotti);

        LOGGER.info("Metodo ordinaListaProdotti() - Fine");
    }

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     */
    public List<String> getNomiProdotti()
    {
        LOGGER.info("Metodo getNomiProdotti() - Inizio");
        List<String> listaNomiProdotti = new ArrayList<>();
        for(EntityMagazzino prodotto : this.listaProdotti)
            listaNomiProdotti.add(prodotto.getNome());

        LOGGER.info("Metodo getNomiProdotti() - Fine");
        return listaNomiProdotti;
    }

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     */
    public List<EntityMagazzino> getProdotti()
    {
        LOGGER.info("Metodo getProdotti() - Inizio");

        List<EntityMagazzino> result = this.listaProdotti;

        LOGGER.info("Metodo getProdotti() - Fine");
        return result;
    }

    /**
     * Metodo che restituisce una rappresentazione testuale dello stato del magazzino
     * @return una stringa che rappresenta lo stato del magazzino
     */
    @Override
    public String toString()
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }
        catch(JsonProcessingException e)
        {
            return null;
        }
    }
}
