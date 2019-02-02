package impl.controller;

import om.EntityMagazzino;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    }

    /**
     * Metodo che permette la scrittura del contenuto della lista dei prodotti nell'apposito file CSV, per archiviare il contenuto del magazzino
     * @throws IOException in caso di problemi di scrittura sul file CSV
     */
    public void scriviProdottiSuFile() throws IOException
    {
        File fileMagazzino = new File(PATH);
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileMagazzino));
        // Cancellazione del contenuto ormai obsoleto del file
        bw.write("");
        bw.flush();

        // Scrittura del contenuto aggiornato a partire da listaProdotti
        bw.append(INTESTAZIONE +"\n");
        for(EntityMagazzino prodotto : listaProdotti)
        {
            bw.append(prodotto.getNome() + ";" + prodotto.getGiacenza() + "\n");
            bw.flush();
        }
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    public EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto)
    {
        int indice = exists(prodotto);
        if(indice >= 0)
            return null;
        else
        {
            indice = -(indice + 1);
            listaProdotti.add(indice, prodotto);
            ordinaListaProdotti();
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
        int indice = exists(prodotto);
        if(indice < 0)
            return null;
        else
        {
            // In caso di rimozione di un elemento della lista, non è necessario riordinare i rimanenti in quanto l'ordine rimane inalterato
            return listaProdotti.remove(indice);
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        int indice = exists(nomeProdotto);
        if(indice < 0)
            return null;
        else
            return listaProdotti.remove(indice);
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        int indice = exists(prodotto);
        if(indice < 0)
            return null;
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            p.setGiacenza(prodotto.getGiacenza());
            ordinaListaProdotti();
            return p;
        }
    }

    /**
     * Metodo che verifica l'esistenza nelmagazzino di un prodotto uguale a quello passato come argomento, sfruttando l'algoritmo di ricerca binaria
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
        int start = 0;
        int end = listaProdotti.size() - 1;

        if(listaProdotti.isEmpty())
            return -1;
        if(nomeProdotto.compareTo(listaProdotti.get(0).getNome()) < 0)
            return -1;
        if(nomeProdotto.compareTo(listaProdotti.get(end).getNome()) > 0)
            return -(end + 1);

        while(start <= end)
        {
            int middle = (start + end) >>> 1;
            String middleName = listaProdotti.get(middle).getNome();
            if(nomeProdotto.compareTo(middleName) < 0)
                end = middle - 1;
            else if(nomeProdotto.compareTo(middleName) > 0)
                start = middle + 1;
            else
                return middle;
        }

        return -(end + 1);
    }

    /**
     * Metodo che permette di ordinare la lista dei prodotti presenti in magazzino
     */
    private void ordinaListaProdotti()
    {
        if(!listaProdotti.isEmpty() && listaProdotti.size() > 1)
            Collections.sort(listaProdotti);
    }
}
