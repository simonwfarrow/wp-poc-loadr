import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.UUID;

public class Loadr implements Runnable {

    private final KafkaProducer<String, String> producer;
    private final String topicName;
    private String json;
    private final int numEvents;

    Loadr(KafkaProducer<String, String> producer, String topicName, String json, int numEvents) {
        this.producer = producer;
        this.topicName = topicName;
        this.json = json;
        this.numEvents = numEvents;
    }


    @Override
    public void run() {

        try {

            System.out.println("Thread id " + Thread.currentThread().getId() + " sending " + this.numEvents + " events");
            for (int i = 1; i <= numEvents; i++) {

                UUID id = UUID.randomUUID();
                String payload = json.replaceAll("ID", id.toString());
                producer.send(new ProducerRecord<String, String>(topicName, id.toString(), payload));
            }

            producer.flush();
            System.out.println("Thread id " + Thread.currentThread().getId() +  " finished publishing");

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
