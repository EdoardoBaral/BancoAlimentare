package interfaces;

import om.EntityRegistro;

import java.sql.SQLException;
import java.util.List;

public interface EntityRegistroController
{
    /**
     * Metodo che permette l'aggiunta di un nuovo elemento EntityRegistro nella lista delle transazioni presenti in registro
     * @param transazione: transazione da aggiungere alla lista
     * @return l'elemento appena aggiunto, null se questo è già presente nella lista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    EntityRegistro aggiungiTransazione(EntityRegistro transazione) throws SQLException;

    /**
     * Metodo che permette la cancellazione della transazione passata come argomento dal registro
     * @param transazione: transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    EntityRegistro cancellaTransazione(EntityRegistro transazione) throws SQLException;

    /**
     * Metodo che permette la cancellazione della transazione il cui id viene passato come argomento dal registro
     * @param id: id della transazione da cancellare
     * @return l'elemento EntityRegistro appena rimosso dalla lista, se esiste, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    EntityRegistro cancellaTransazione(Long id) throws SQLException;

    /**
     * Metodo che permette di modificare una delle transazioni presenti in registro (non l'id, che è la chiave di ricerca)
     * @param transazione: transazione aggiornata
     * @return la transazione appena aggiornata, se esiste nella lista, altrimenti null
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    EntityRegistro modificaTransazione(EntityRegistro transazione) throws SQLException;

    /**
     * Metodo che restituisce la lista delle transazioni presenti sul registro
     * @return la lista delle transazioni presenti sul registro
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    List<EntityRegistro> getListaTransazioni() throws SQLException;

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione uguale a quella passata come argomento, sfruttando l'algoritmo di ricerca binaria
     * @param transazione: transazione da cercare nel registro
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    boolean exists(EntityRegistro transazione) throws SQLException;

    /**
     * Metodo che verifica l'esistenza nel registro di una transazione il cui id è uguale a quello passato come argomento, sfruttando l'algoritmo della ricerca binaria
     * @param id: id della transazione da cercare nel magazzino
     * @return l'indice della transazione all'interno della lista, negativo nel caso non esista
     * @throws SQLException in caso di errori nella comunicazione con il database
     */
    boolean exists(Long id) throws SQLException;
}
