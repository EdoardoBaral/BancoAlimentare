package interfaces;

import om.EntityRegistro;

import java.util.List;

public interface EntityRegistroController
{
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
     * Metodo che restituisce la lista delle transazioni presenti sul registro
     * @return la lista delle transazioni presenti sul registro
     */
    List<EntityRegistro> getListaTransazioni();
}
