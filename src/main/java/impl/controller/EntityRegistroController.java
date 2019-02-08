package impl.controller;

import om.EntityRegistro;
import om.TipoTransazione;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EntityRegistroController. Classe che permette di realizzare un controller per la gestione delle transazioni del banco alimentare.
 * Il registro delle transazioni viene scritto su un apposito file CSV.
 * La classe contiene metodi che permettono la lettura del file per recuperarne l'intero contenuto e mapparlo all'interno di una lista e metodi che permettono la scrittura del
 * contenuto della lista sul medesimo file, in caso di aggiornamenti.
 *
 * @author Edoardo Baral
 */
public class EntityRegistroController
{
    private static final String PATH = "archivio/registro.csv";
    private static final String INTESTAZIONE = "ID;PRODOTTO;QUANTITA';NOME_DESTINATARIO;DATA_TRANSAZIONE;TIPO_TRANSAZIONE";
    private static final String SEPARATORE = ";";

    private List<EntityRegistro> listaTransazioni;
    private Long nextId;

    /**
     * Costruttore della classe EntityRegistroController
     * @throws IOException in caso di problemi di lettura/scrittura del file CSV
     */
    public EntityRegistroController() throws IOException
    {
        File fileRegistro = new File(PATH);
        fileRegistro.createNewFile();
        listaTransazioni = new ArrayList<>();
        nextId = 1L;
        mappingDaFile();
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
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");

        while( (riga = br.readLine()) != null )
        {
            if(!riga.equals(INTESTAZIONE))
            {
                String[] valori = riga.split(SEPARATORE);
                EntityRegistro transazione = new EntityRegistro();
                transazione.setId(Long.parseLong(valori[0]));
                transazione.setProdotto(valori[1]);
                transazione.setQuantita(Integer.parseInt(valori[2]));
                transazione.setDestinatario(valori[3]);
                transazione.setDataTransazione(dtf.parseDateTime(valori[4]));
                if(valori[5].equals(TipoTransazione.INGRESSO))
                    transazione.setTipoTransazione(TipoTransazione.INGRESSO);
                else
                    transazione.setTipoTransazione(TipoTransazione.USCITA);
                aggiungiTransazione(transazione);
            }
        }
    }

    /**
     * Metodo che permette la scrittura del contenuto della lista delle transazioni nell'apposito file CSV, per archiviare il contenuto del registro
     * @throws IOException in caso di problemi di scrittura sul file CSV
     */
    public void scriviProdottiSuFile() throws IOException
    {
        File fileMagazzino = new File(PATH);
        BufferedReader br = new BufferedReader(new FileReader(fileMagazzino));
        String checkIntestazione = br.readLine();
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileMagazzino, true));

        if(checkIntestazione == null) //controllo se il file contiene già delle transazioni registrate o meno
            bw.append(INTESTAZIONE +"\n");

        for(EntityRegistro transazione : listaTransazioni)
        {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
            bw.append(transazione.getId() +";"+ transazione.getProdotto() +";"+ transazione.getQuantita() +";");
            bw.append(transazione.getDestinatario() +";"+ dtf.print(transazione.getDataTransazione()) +";"+ transazione.getTipoTransazione() +"\n");
            bw.flush();
        }
    }

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    public EntityRegistro aggiungiTransazione(EntityRegistro transazione)
    {
        transazione.setId(nextId);
        nextId++;
        transazione.setDataTransazione(new DateTime());
        listaTransazioni.add(transazione);
        ordinaListaTransazioni();
        return transazione;
    }

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityRegistro cancellaTransazione(EntityRegistro transazione)
    {
        int indice = exists(transazione);
        if(indice < 0)
            return null;
        else
        {
            // In caso di rimozione di un elemento della lista, non è necessario riordinare i rimanenti in quanto l'ordine rimane inalterato
            return listaTransazioni.remove(indice);
        }
    }

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    public EntityRegistro cancellaTransazione(Long id)
    {
        int indice = exists(id);
        if(indice < 0)
            return null;
        else
            return listaTransazioni.remove(indice);
    }

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     */
    public EntityRegistro modificaTransazione(EntityRegistro transazione)
    {
        int indice = exists(transazione);
        if(indice < 0)
            return null;
        else
        {
            EntityRegistro t = listaTransazioni.get(indice);
            t.setProdotto(transazione.getProdotto());
            t.setQuantita(transazione.getQuantita());
            t.setDestinatario(transazione.getDestinatario());
            t.setDataTransazione(transazione.getDataTransazione());
            t.setTipoTransazione(transazione.getTipoTransazione());
            ordinaListaTransazioni();
            return t;
        }
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    public int exists(EntityRegistro transazione) //TODO valutarne la cancellazione
    {
        return exists(transazione.getId());
    }

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     */
    public int exists(Long id)
    {
        int start = 0;
        int end = listaTransazioni.size() - 1;

        if(listaTransazioni.isEmpty())
            return -1;
        if(id < listaTransazioni.get(0).getId())
            return -1;
        if(id > listaTransazioni.get(end).getId())
            return -(end + 1);

        while(start <= end)
        {
            int middle = (start + end) >>> 1;
            Long middleId = listaTransazioni.get(middle).getId();
            if(id < middleId)
                end = middle - 1;
            else if(id > middleId)
                start = middle + 1;
            else
                return middle;
        }

        return -(end + 1);
    }

    /**
     * Metodo che permette di ordinare la lista delle transazioni presenti nel registro
     */
    private void ordinaListaTransazioni()
    {
        if(!listaTransazioni.isEmpty() && listaTransazioni.size() > 1)
            Collections.sort(listaTransazioni);
    }
}
