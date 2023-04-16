package ru.spbstu.kafka.base;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerWrapperTest {

    private static final String TOPIC = "testTopic";
    private static final int PARTITION = 10;
    private static final long OFFSET = 0;
    private static final Integer TOPIC_KEY = 123;
    private static final String TOPIC_VALUE = "test_value";

    private static final String CONSUMER_GROUP_ID = "consumer_group_id";

    @Mock
    private KafkaConsumer<Integer, String> consumer;

    @Mock
    private KafkaJobConfiguration kafkaJobConfiguration;

    @Test(expected = NullPointerException.class)
    public void constructorConsumerIdIsNullTest() {
        new ConsumerWrapper(null, consumer, kafkaJobConfiguration);
    }

    @Test(expected = NullPointerException.class)
    public void constructorConsumerIsNullTest() {
        new ConsumerWrapper(CONSUMER_GROUP_ID, null, kafkaJobConfiguration);
    }

    @Test(expected = NullPointerException.class)
    public void constructorConfigurationIsNullTest() {
        new ConsumerWrapper(CONSUMER_GROUP_ID, consumer, null);
    }

    @Test(expected = NullPointerException.class)
    public void commitOffsetsNullInputTest() {
        ConsumerWrapper wrapper = createConsumerWrapper();
        wrapper.commitOffsets(null);
    }

    @Test
    public void commitOffsetsEmptyInputTest() {
        ConsumerWrapper wrapper = createConsumerWrapper();
        ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(Collections.emptyMap());
        wrapper.commitOffsets(consumerRecords);
        Mockito.verify(consumer, Mockito.times(0)).commitSync(Mockito.anyMap());
    }

    @Test
    public void commitOffsetsNotEmptyInputTest() {
        Mockito.doNothing().when(consumer).commitSync(Mockito.anyMap());
        ConsumerWrapper wrapper = createConsumerWrapper();
        ConsumerRecord<Integer, String> consumerRecord
                = new ConsumerRecord<>(TOPIC, PARTITION, OFFSET, TOPIC_KEY, TOPIC_VALUE);
        TopicPartition topicPartition = new TopicPartition(TOPIC, PARTITION);
        Map<TopicPartition, List<ConsumerRecord<Integer, String>>> consumerRecordMap
                = ImmutableMap.of(topicPartition, Collections.singletonList(consumerRecord));
        ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(consumerRecordMap);
        wrapper.commitOffsets(consumerRecords);

        Map<TopicPartition, OffsetAndMetadata> expectedParameter
                = ImmutableMap.of(topicPartition, new OffsetAndMetadata(OFFSET));
        Mockito.verify(consumer, Mockito.times(1))
                .commitSync(expectedParameter);
    }

    @Test
    public void getPollTimeoutTest() {
        ConsumerWrapper wrapper = createConsumerWrapper();
        Assert.assertEquals(0, wrapper.getPollTimeout());
        Mockito.doReturn(-1L).when(kafkaJobConfiguration).getPollTimeout();
        Assert.assertEquals(0, wrapper.getPollTimeout());
        Mockito.doReturn(60L).when(kafkaJobConfiguration).getPollTimeout();
        Assert.assertEquals(60, wrapper.getPollTimeout());
    }

    @Test
    public void closeTest() {
        ConsumerWrapper wrapper = createConsumerWrapper();
        wrapper.shutdown();
        Mockito.verify(consumer, Mockito.times(1)).close();
    }

    @Test
    public void pollTest() {
        Mockito.doReturn(null).when(consumer).poll(Mockito.anyLong());
        ConsumerWrapper wrapper = createConsumerWrapper();
        wrapper.poll();
        Mockito.verify(consumer, Mockito.times(1)).poll(Mockito.anyLong());
    }

    @Test(expected = NullPointerException.class)
    public void createConsumerWrapperComponentIdIsNullTest() {
        ConsumerWrapper.createConsumerWrapper(null, kafkaJobConfiguration, new Properties());
    }

    @Test(expected = NullPointerException.class)
    public void createConsumerWrapperConfigurationIsNullTest() {
        ConsumerWrapper.createConsumerWrapper(CONSUMER_GROUP_ID, null, new Properties());
    }

    @Test(expected = NullPointerException.class)
    public void createConsumerWrapperPropertiesIsNullTest() {
        ConsumerWrapper.createConsumerWrapper(CONSUMER_GROUP_ID, kafkaJobConfiguration, null);
    }

    @Test
    public void createConsumerWrapperTest() {
        Mockito.doReturn(CONSUMER_GROUP_ID).when(kafkaJobConfiguration).getCustomGroupId();
        Mockito.doReturn(10).when(kafkaJobConfiguration).getMaxPoolRecords();
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9020");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        ConsumerWrapper consumerWrapper
                = ConsumerWrapper.createConsumerWrapper(CONSUMER_GROUP_ID, kafkaJobConfiguration, properties);
        Assert.assertNotNull(consumerWrapper);
    }

    private ConsumerWrapper createConsumerWrapper() {
        return new ConsumerWrapper(CONSUMER_GROUP_ID, consumer, kafkaJobConfiguration);
    }

}
