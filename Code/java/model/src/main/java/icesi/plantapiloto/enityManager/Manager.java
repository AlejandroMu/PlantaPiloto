package icesi.plantapiloto.enityManager;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;



/**
 * ChannelManager
 */
public class Manager<T> {

    public static final EntityManager manager = Persistence.createEntityManagerFactory("planta").createEntityManager();
    public static void begin(){
        manager.getTransaction().begin();
    }
    public static void commit(){
        manager.getTransaction().commit();
    }
    public void save(T elemnt){
        manager.persist(elemnt);
    }


}