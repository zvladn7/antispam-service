package ru.spbstu.kafka.utils;

import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Properties;

public class PropertiesProviderTest {

    private static final String FILE_NOT_EXIST_PATH = "/tmp/fileNotExistPath.properties";
    private static final String SUCCESS_FILE = "messages-consumer-properties.properties";
    private static final String BOOTSTRAP_SERVERS_KEY = "bootstrap.servers";
    private static final String BOOTSTRAP_SERVERS_VALUE = "localhost:9092";
    private static final String VALUE_DESERIALIZER_KEY = "value.deserializer";
    private static final String VALUE_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final String KEY_DESERIALIZER_KEY = "key.deserializer";
    private static final String KEY_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";

    private static final String AUTO_OFFSET_RESET_KEY = "auto.offset.reset";
    private static final String AUTO_OFFSET_RESET_VALUE = "latest";

    private static final String GROUP_ID_KEY = "group.id";
    private static final String GROUP_ID_VALUE = "antispam-messages";
    private static final String AUTO_COMMIT_INTERVAL_MS_KEY = "auto.commit.interval.ms";
    private static final String AUTO_COMMIT_INTERVAL_MS_VALUE = "60000";


    @Test
    public void fileNotExistTest() {
        Properties properties = PropertiesProvider.provideProperties(FILE_NOT_EXIST_PATH);
        Assert.assertNotNull(properties);
        Assert.assertTrue(properties.isEmpty());
    }

    @Test
    public void fileSuccessReadTest() {
        URL resource = Resources.getResource(SUCCESS_FILE);
        Assert.assertNotNull(resource);
        Properties properties = PropertiesProvider.provideProperties(resource.getFile().replaceAll("%20", " "));
        Assert.assertEquals(BOOTSTRAP_SERVERS_VALUE, properties.getProperty(BOOTSTRAP_SERVERS_KEY));
        Assert.assertEquals(VALUE_DESERIALIZER_VALUE, properties.getProperty(VALUE_DESERIALIZER_KEY));
        Assert.assertEquals(KEY_DESERIALIZER_VALUE, properties.getProperty(KEY_DESERIALIZER_KEY));
        Assert.assertEquals(AUTO_COMMIT_INTERVAL_MS_VALUE, properties.getProperty(AUTO_COMMIT_INTERVAL_MS_KEY));
        Assert.assertEquals(GROUP_ID_VALUE, properties.getProperty(GROUP_ID_KEY));
        Assert.assertEquals(AUTO_COMMIT_INTERVAL_MS_VALUE, properties.getProperty(AUTO_COMMIT_INTERVAL_MS_KEY));
    }

}
