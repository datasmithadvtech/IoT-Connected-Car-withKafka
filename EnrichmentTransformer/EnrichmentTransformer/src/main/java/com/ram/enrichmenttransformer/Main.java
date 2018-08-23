package com.ram.enrichmenttransformer;

import java.util.concurrent.ExecutionException;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.ram.constants.IKafkaConstants;
;
import com.ram.producer.ProducerCreator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;



public class Main {
    private static int counter = 0;
	
    public static void main(String[] args) {

	// get the filtered json from data filter service and publish it to kafka topic 
        String jsonString = getPostedJSON();
        while (true) {
            if (jsonString != null && counter < IKafkaConstants.MAX_NO_POSTED_JSON) {
                runProducer(jsonString);
                jsonString = getPostedJSON();
                counter++;
            } else {
                System.out.println("Exception : There is No DATA (Data Filter is Empty)");
                try {
                    counter = 0;
                    Thread.sleep(5000);
                    jsonString = getPostedJSON();
                } catch (InterruptedException ex) {
                    System.out.println("Exception : " + ex.getMessage());
                    //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

	// get the filtered jsonobject from data filter service and add time stamp to it and uppercase the vin 
    static String getPostedJSON() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet("http://data-filter.azurewebsites.net/api/Home/1");
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            if (result.getStatusLine().getStatusCode() != 404) {
                String json = EntityUtils.toString(result.getEntity(), "UTF-8");
                map = mapper.readValue(json, Map.class);
                if (!map.containsKey("timestamp")) {
                    map.put("timestamp", System.currentTimeMillis());
                }
				 if (map.containsKey("vin")) {
                        map.put("vin", ((String) map.get("vin")).toUpperCase());
                    }

                JSONObject jsonobj = new JSONObject();
                jsonobj.putAll(map);
                System.out.println(jsonobj.toJSONString());
                return jsonobj.toJSONString();
            }

        } catch (IOException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
        return null;
    }
     // create kafka producer with topic and publish the json string to that topic
    static void runProducer(String postedJson) {
        Producer<Long, String> producer = ProducerCreator.createProducer();

        final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
                postedJson);
        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("to partition " + metadata.partition()
                    + " with offset " + metadata.offset());
        } catch (ExecutionException e) {
            System.out.println("Error in sending record");
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println("Error in sending record");
            System.out.println(e);
        }

    }
}
