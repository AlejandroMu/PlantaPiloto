package icesi.plantapiloto.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import icesi.plantapiloto.enityManager.Manager;
import icesi.plantapiloto.model.Channel;

public class ChannelRepository implements Repository<Channel, Long> {

    public Optional<Channel> findByNameAndModuleName(String channelName, String moduleName) {
        EntityManager manager = Manager.managerFactory.createEntityManager();

        String subQuery = "(Select m.id From IOModule m Where m.name = ':modName' Limit 1)";
        // IOModule moduleQ = manager.createQuery("FROM IOModule m WHERE m.name
        // =':name'", IOModule.class)
        // .setParameter("modName", moduleName)
        // .getSingleResult();
        // long idM = moduleQ.getId();
        Channel chanelQ = manager
                .createQuery(
                        "FROM Channel c WHERE c.name =:channelName and " +
                                "c.module = (Select m.id From IOModule m Where m.name = :modName)",
                        Channel.class)
                .setParameter("channelName", channelName)
                .setParameter("modName", moduleName)
                .getSingleResult();
        manager.close();
        return Optional.ofNullable(chanelQ);
    }

}
