package icesi.plantapiloto.modelManager.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;

import icesi.plantapiloto.modelManager.enityManager.Manager;
import icesi.plantapiloto.modelManager.model.Channel;

public class ChannelRepository implements Repository<Channel, Long> {

        public Optional<Channel> findByNameAndModuleName(String channelName, String moduleName) {
                EntityManager manager = Manager.managerFactory.createEntityManager();
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
