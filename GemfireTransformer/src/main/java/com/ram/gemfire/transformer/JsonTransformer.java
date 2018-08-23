/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ram.gemfire.transformer;

import java.util.HashMap;
import java.util.Map;
import com.ram.domain.CarInfo;
import com.ram.repository.CarInfoRepository;
import com.ram.kafka.consumer.ConsumerCreator;
import com.ram.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ram
 */
public class JsonTransformer {

    private static final Logger logger
            = LoggerFactory.getLogger(JsonTransformer.class);

    private final ObjectMapper mapper;
// JsonTransformer is the actual transfomer which transform the json string into pojo (plain old java object)
// to be serialized into Gemfire

    public JsonTransformer() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

    }

    // first consume the json string from Kafka then transform it into pojo
    // finally save the pojo into Gemfire
    public void run(CarInfoRepository carRepository) {

        Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

        int noMessageToFetch = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(IKafkaConstants.MAX_NO_POSTED_JSON);
            if (consumerRecords.count() == 0) {
                noMessageToFetch++;
                if (noMessageToFetch > IKafkaConstants.NO_FAILED_POLL_COUNT) {
                    System.out.println("Exception : There is No DATA (Data Filter is Empty)");
                    try {
                        noMessageToFetch = 0;
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        consumer.close();
                        System.out.println("Exception : " + ex.getMessage());

                    }
                } else {
                    continue;
                }
            }

            consumerRecords.forEach(record -> {
                CarInfo field = this.transform(record.value());
                if (field != null) {
                    carRepository.save(field);
                    System.out.println("the number of entities available : " + carRepository.count());
                    try {

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {

                        System.out.println("Exception : " + ex.getMessage());

                    }
                }

                System.out.println("Record Key " + record.key());
                System.out.println("Record value " + record.value());
                System.out.println("Record offset " + record.offset());
                System.out.println("-------------------------------------");
            });
            consumer.commitAsync();
        }

    }

    @SuppressWarnings("unchecked")
    public CarInfo transform(String payload) {

        CarInfo carInfo = null;

        try {
            if (payload != null) {
                Map<String, Object> map = mapper.readValue(payload,
                        new TypeReference<HashMap<String, Object>>() {
                        });
                carInfo = new CarInfo(map);
            }
        } catch (final Exception e) {
            System.out.println("Error converting to a CarPosition object :" + e.getMessage());

            if (logger.isDebugEnabled()) {
                System.out.println("Error payload=[" + payload + "]");
            }
        }

        return carInfo;
    }
}
