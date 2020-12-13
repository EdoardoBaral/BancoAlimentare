package interfaces;

import om.EntityMagazzino;

import java.sql.SQLException;
import java.util.List;

public interface EntityMagazzinoController
{
    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityMagazzino nella lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da aggiungere alla lista
     * @return l'elemento appena aggiunto
     * @throws SQLException in caso di errore nell'inserimento nel database
     */
    EntityMagazzino aggiungiProdotto(EntityMagazzino prodotto) throws SQLException;

    /**
     * Metodo che permette la cancellazione del prodotto passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param prodotto: prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    EntityMagazzino cancellaProdotto(EntityMagazzino prodotto) throws SQLException;

    /**
     * Metodo che permette la cancellazione del prodotto il cui nome viene passato come argomento dalla lista dei prodotti presenti in magazzino
     * @param nomeProdotto: nome del prodotto da cancellare
     * @return l'elemento EntityMagazzino appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    EntityMagazzino cancellaProdotto(String nomeProdotto) throws SQLException;

    /**
     * Metodo che permette di modificare uno dei prodotti presenti in magazzino (non il nome, che è la chiave di ricerca)
     * @param prodotto: prodotto aggiornato
     * @return il prodotto appena aggiornato, se esiste nella lista, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    EntityMagazzino modificaProdotto(EntityMagazzino prodotto) throws SQLException;

    /**
     * Metodo che permette di incrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da aggiungere alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene incrementata, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    boolean incrementaGiacenza(EntityMagazzino prodotto, int quantita) throws SQLException;

    /**
     * Metodo che permette di decrementare la giacenza di un prodotto in magazzino della quantità specificata
     * @param prodotto: prodotto da cercare in magazzino
     * @param quantita: quantità da sottrarre alla giacenza
     * @return true se il prodotto indicato viene trovato e la giacenza viene decrementata, false altrimenti
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    boolean decrementaGiacenza(EntityMagazzino prodotto, int quantita) throws SQLException;

    /**
     * Metodo che restituisce la lista dei nomi dei prodotti presenti in magazzino
     * @return la lista dei nomi dei prodotti presenti in magazzino
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    List<String> getNomiProdotti() throws SQLException;

    /**
     * Metodo che restituisce la lista dei prodotti presenti in magazzino
     * @return la lista dei prodotti presenti in magazzino
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    List<EntityMagazzino> getProdotti() throws SQLException;

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto uguale a quello passato come argomento
     * @param prodotto: prodotto da cercare nel magazzino
     * @return true se il prodotto è presente nel magazzino, false altrimenti
     * @throws SQLException se si verifica un errore nella comunicazione con il database
     */
    boolean exists(EntityMagazzino prodotto) throws SQLException;

    /**
     * Metodo che verifica l'esistenza nel magazzino di un prodotto il cui nome è uguale a quello passato come argomento
     * @param nomeProdotto: nome del prodotto da cercare nel magazzino
     * @return true se il prodotto è presente nel magazzino, false altrimenti
     * @throws SQLException se si verifica un errore nella comunicazione con il database
     */
    boolean exists(String nomeProdotto) throws SQLException;

    /**
     * Metodo che cerca sul database un determinato prodotto in base al nome passato come argomento
     * @param nome: nome del prodotto da cercare
     * @return il prodotto indicato, se esiste, altrimenti null
     * @throws SQLException in caso di errore nella comunicazione con il database
     */
    EntityMagazzino getProdotto(String nome) throws SQLException;
}
