package interfaces;

import om.EntityMagazzino;
import om.EntityRegistro;

import java.util.List;

public interface BancoAlimentareController
{
    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param prodotto:    prodotto da cercare nel magazzino
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da prelevare dal magazzino
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta e se la transazione viene correttamente registrata, false altrimenti
     */
    boolean preleva(EntityMagazzino prodotto, EntityRegistro transazione, int quantita);

    /**
     * Metodo che permette di prelevare una quantità specificata di un certo prodotto dal magazzino e registrare la transazione
     * @param nomeProdotto: nome del prodotto oggetto della transazione
     * @param quantita:     quantità di prodotto da prelevare
     * @param destinatario: destinatario del prodotto prelevato
     * @return true se il prodotto viene trovato nel magazzino, se la sua giacenza è sufficiente per coprire la richiesta e se la transazione viene correttamente registrata, false altrimenti
     */
    boolean preleva(String nomeProdotto, int quantita, String destinatario);

    /**
     * Metodo che permette di depositare una quantità specificata di un certo prodotto nel magazzino e registrare la transazione
     * @param prodotto:    prodotto da cercare nel magazzino
     * @param transazione: transazione da registrare
     * @param quantita:    quantità di prodotto da depositare nel magazzino
     * @return true se il prodotto viene trovato nel magazzino e se la transazione viene correttamente registrata, false altrimenti
     */
    boolean deposita(EntityMagazzino prodotto, EntityRegistro transazione, int quantita);

    /**
     * Metodo che permette di depositare una quantità specificata di un certo prodotto nel magazzino e registrare la transazione
     * @param nomeProdotto: nome del prodotto da depositare
     * @param quantita:     quantità di prodotto da depositare
     * @return true se il prodotto viene correttamente depositato in magazzino e la sua giacenza viene aggiornata, false altrimenti
     */
    boolean deposita(String nomeProdotto, int quantita);

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto);

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    EntityMagazzino cancellaProdotto(EntityMagazzino prodotto);

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     */
    EntityMagazzino cancellaProdotto(String nomeProdotto);

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     */
    EntityMagazzino modificaProdotto(EntityMagazzino prodotto);

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     */
    boolean exists(EntityMagazzino prodotto);

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se il rpodotto esiste in magazzino, false altrimenti
     */
    boolean exists(String nomeProdotto);

    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     */
    EntityRegistro aggiungiTransazione(EntityRegistro transazione);

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    EntityRegistro cancellaTransazione(EntityRegistro transazione);

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     */
    EntityRegistro cancellaTransazione(Long id);

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     */
    EntityRegistro modificaTransazione(EntityRegistro transazione);

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return true se la transazione è stata registrata, false altrimenti
     */
    boolean exists(EntityRegistro transazione);

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return true se la transazione è stata registrata, false altrimenti
     */
    boolean exists(Long id);

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     */
    List<String> getNomiProdotti();

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     */
    List<EntityMagazzino> getProdotti();

    /**
     * Metodo che restituisce la lista delle transazioni registrate nel registro
     * @return la lista delle transazioni presenti nel registro
     */
    List<EntityRegistro> getTransazioni();
}
