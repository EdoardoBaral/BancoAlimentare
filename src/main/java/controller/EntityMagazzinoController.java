package controller;

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
    private static final String PATH = "magazzino.csv";
    private static final String INTESTAZIONE = "NOME;GIACENZA";
    private static final String SEPARATORE = ";";

    private BufferedWriter bw;
    private BufferedReader br;
    private List<EntityMagazzino> listaProdotti;

    /**
     * Costruttore della classe EntityMagazzinoController
     * @throws IOException in caso di problemi di lettura/scrittura del file CSV
     */
    public EntityMagazzinoController() throws IOException
    {
        File fileMagazzino = new File(PATH);
        fileMagazzino.createNewFile();
        bw = new BufferedWriter(new FileWriter(fileMagazzino));
        br = new BufferedReader(new FileReader(fileMagazzino));
        listaProdotti = new ArrayList<>();
    }

    /**
     * Metodo che permette la lettura del file CSV e il mapping del suo contenuto all'interno di una lista ordinata
     * @throws IOException in caso di problemi di lettura del file CSV
     */
    public void mappingDaFile() throws IOException
    {
        String riga = null;
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
        if(listaProdotti.contains(prodotto))
            return null;
        else
        {
            listaProdotti.add(prodotto);
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
        int indice = listaProdotti.indexOf(prodotto);
        if(indice == -1)
            return null;
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            listaProdotti.remove(p);
            // In caso di rimozione di un elemento della lista, non è necessario riordinare i rimanenti in quanto l'ordine rimane inalterato
            return p;
        }
    }

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityMagazzino cancellaProdotto(String nomeProdotto)
    {
        EntityMagazzino p = new EntityMagazzino();
        p.setNome(nomeProdotto);
        int indice = listaProdotti.indexOf(p);
        if(indice == -1)
            return null;
        else
            return listaProdotti.remove(indice);
    }

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    public EntityMagazzino modificaProdotto(EntityMagazzino prodotto)
    {
        int indice = listaProdotti.indexOf(prodotto);
        if(indice == -1)
            return null;
        else
        {
            EntityMagazzino p = listaProdotti.get(indice);
            p.setNome(prodotto.getNome());
            p.setGiacenza(prodotto.getGiacenza());
            ordinaListaProdotti();
            return p;
        }
    }

    /**
     * Metodo che verifica l'esistenza nelmagazzino di un prodotto uguale a quello passato come argomento
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se prodotto è presente nel magazzino, false altrimenti
     */
    public boolean exists(EntityMagazzino prodotto)
    {
        return listaProdotti.contains(prodotto);
    }

    /**
     * Metodo che verifica l'esistenza nelmagazzino di un prodotto il cui nome è uguale a quello passato come argomento
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se prodotto è presente nel magazzino, false altrimenti
     */
    public boolean exists(String nomeProdotto)
    {
        EntityMagazzino p = new EntityMagazzino();
        p.setNome(nomeProdotto);
        return exists(p);
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
