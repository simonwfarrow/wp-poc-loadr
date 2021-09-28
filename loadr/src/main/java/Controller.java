import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Controller {

    /* expects the following env vars
        topicName
        concurrency
        jsonString  - inc. string replacement placeholders i.e. { payload : { id : <ID> }}
        numEvents
     */
    public static void main(String[] args) {

        String topicName = System.getenv("TOPIC");
        int concurrency = Integer.valueOf(System.getenv("CONCURRENCY"));
        String json = System.getenv("PAYLOAD");
        int numEvents = Integer.valueOf(System.getenv("NUMEVENTS"));
        int total = Math.round(numEvents/concurrency);
        String bootstrapServers = System.getenv("KAFKA_HOST") + ":" + System.getenv("KAFKA_PORT");

        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);

        System.out.println("Connecting to " + bootstrapServers);
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);

        System.out.println("Creating " + concurrency + " threads, each with " + total + " events");
        long start = System.currentTimeMillis();

        Thread[] loadrs = new Thread[concurrency];
        for (int i=0;i<concurrency;i++){
            loadrs[i] = new Thread(new Loadr(producer,topicName,json,total));
            loadrs[i].start();
        }

        try {
            for (Thread t : loadrs) {
                t.join();
            }
        } catch (InterruptedException e){
            System.out.println("Thread was interrupted");
        } finally {
            producer.close();
            long end = System.currentTimeMillis();
            long totalTime = end-start;
            System.out.println("Completed in " + totalTime/1000 + " seconds");
        }


    }
}
