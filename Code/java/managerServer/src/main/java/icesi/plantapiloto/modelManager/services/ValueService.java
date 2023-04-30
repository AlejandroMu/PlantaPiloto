package icesi.plantapiloto.modelManager.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import icesi.plantapiloto.common.entities.Measure;
import icesi.plantapiloto.common.entities.Message;
import icesi.plantapiloto.modelManager.dtos.ValueQuery;
import icesi.plantapiloto.modelManager.model.Channel;
import icesi.plantapiloto.modelManager.model.Value;
import icesi.plantapiloto.modelManager.repositories.ChannelRepository;
import icesi.plantapiloto.modelManager.repositories.ValueRepository;

public class ValueService {
    private ValueRepository valueRepository;
    private ChannelRepository channelRepository;

    public void setValueRepository(ValueRepository repository) {
        this.valueRepository = repository;
    }

    public void setChannelRepository(ChannelRepository repository) {
        this.channelRepository = repository;
    }

    public void addValues(Message message) {
        List<Measure> measures = message.getMeasures();
        Date date = message.getTime();
        for (Measure measure : measures) {
            Optional<Channel> channel = channelRepository.findByNameAndModuleName(measure.getName(),
                    message.getTopic());
            if (channel.isPresent()) {
                Value value = new Value(date, Float.parseFloat(measure.getValue()), channel.get());
                valueRepository.save(value);

            } else {
                System.out.println("Channel not found: channel name: " + measure.getName() + " module name: "
                        + message.getTopic());
            }
        }
    }

    public List<Value> getValues(ValueQuery query) {
        return valueRepository.findAll(Value.class);
    }

}
