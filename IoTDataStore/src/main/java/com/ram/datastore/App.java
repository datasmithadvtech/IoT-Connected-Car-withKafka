package com.ram.datastore;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.ram.kafka.constants.IKafkaConstants;
import com.ram.kafka.consumer.ConsumerCreator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.util.logging.Logger;
import org.apache.hadoop.fs.FSDataOutputStream;

// Here is our Data Store 
//first we consume the json object from Kafka thee write it to hdfs (Hadoop distributed File System)
public class App {

    public static FSDataOutputStream outputStream = null;
    public static FileSystem fs = null;
  
    public static void main(String[] args) throws Exception {

        String hdfsuri = "hdfs://localhost:9000";

        String path = "/user/ram/Test/";

        String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String fileName = "Data_" + timeLog + ".json";
        // ====== Init HDFS File System Object
        Configuration conf = new Configuration();
        // Set FileSystem URI
        conf.set("fs.defaultFS", hdfsuri);
        // Because of Maven
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        // Set HADOOP user
        System.setProperty("HADOOP_USER_NAME", "hema");
        System.setProperty("hadoop.home.dir", "/");

        //Get the filesystem - HDFS
        fs = FileSystem.get(URI.create(hdfsuri), conf);

        //==== Create folder if not exists
        Path workingDir = fs.getWorkingDirectory();
        Path newFolderPath = new Path(path);
        if (!fs.exists(newFolderPath)) {
            // Create new Directory
            fs.mkdirs(newFolderPath);

        }
        //Create a path
        Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
        //Init output stream
        outputStream = fs.create(hdfswritepath);
        runConsumer();
    }

    static void runConsumer() throws IOException {

        Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

        int noMessageToFetch = 0;

        while (true) {
            ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count() == 0) {
                noMessageToFetch++;
                if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT) {
                    break;
                } else {
                    continue;
                }
            }

            consumerRecords.forEach(record -> {
                try {
                    outputStream.writeBytes(record.value());
                    outputStream.hflush();
                    outputStream.writeBytes("\n");
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                // save(record.value());
                System.out.println("Record Key " + record.key());
                System.out.println("Record value " + record.value());
                System.out.println("Record partition " + record.partition());
                System.out.println("Record offset " + record.offset());

            });

            consumer.commitAsync();
        }

        outputStream.close();
        fs.close();
        consumer.close();
    }

}
