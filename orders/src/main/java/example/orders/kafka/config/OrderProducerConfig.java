package example.orders.kafka.config;


import example.orders.kafka.model.OrderMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.topic.orders}")
    private String ordersTopic;

    @Bean
    public ProducerFactory<String, OrderMessage> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");//подтверждение от всех реплик
        props.put(ProducerConfig.RETRIES_CONFIG, 3);//3 попытки отправки
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);//задержка между повторными отправками 1 сек
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);// Настройки идемпотентности
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OrderMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name(ordersTopic)
                .partitions(6)
                .replicas(3)
                .configs(Map.of(
                        "retention.ms", "604800000", // 7 дней
                        "cleanup.policy", "delete",
                        "min.insync.replicas", "2"  // Гарантия записи при потере 1 реплики
                ))
                .build();
    }
}
