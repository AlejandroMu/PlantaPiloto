package icesi.plantapiloto.enityManager;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import icesi.plantapiloto.model.EntityWrapper;
import icesi.plantapiloto.controller.entityManager.*;
import com.zeroc.Ice.Current;

import icesi.plantapiloto.model.*;

/**
 * ChannelManager
 */
public class Manager implements ManagerI {

    public static final EntityManager manager = Persistence.createEntityManagerFactory("planta").createEntityManager();
    private long idValue = 20;

    public void begin(Current current) {
        manager.getTransaction().begin();
    }

    public void commit(Current current) {
        manager.getTransaction().commit();
    }

    public void save(String registry, Current current) {
        String reg[] = registry.split(":");
        String sourceName = reg[0].split(" ")[0];
        // System.out.println(Arrays.toString(reg));
        Query moduleQ = manager.createQuery("FROM IOModule m WHERE m.name ='" + sourceName + "'");
        List<IOModule> modules = moduleQ.getResultList();
        long idM = modules.get(0).getId();
        Query chanelQ = manager.createQuery("FROM Channel c WHERE c.module =" + idM + " and c.name ='" + reg[4] + "'");
        List<?> channels = chanelQ.getResultList();

        // ystem.out.println("size: " + channels.size());

        Float f = Float.parseFloat(reg[2]);
        if (channels.size() > 0) {
            Value valueO = new Value(idValue, new Date(Long.parseLong(reg[3])), f, (Channel) channels.get(0));
            manager.getTransaction().begin();
            manager.persist(valueO);
            manager.getTransaction().commit();

            idValue++;
        } else {
            System.out.println("no found");
        }
    }

    public EntityWrapper getById(Current current) {
        return null;
    }

}