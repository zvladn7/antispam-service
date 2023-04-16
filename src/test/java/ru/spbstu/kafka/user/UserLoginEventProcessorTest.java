package ru.spbstu.kafka.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.spbstu.antispam.UserLogin;
import ru.spbstu.ip.CheckLoginIpService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class UserLoginEventProcessorTest {

    private static final String TOPIC = "testTopic";

    @Mock
    private UserLoginConfiguration userLoginConfiguration;

    @Mock
    private CheckLoginIpService checkLoginIpService;

    private UserLoginEventProcessor userLoginEventProcessor;

    @Before
    public void setUp() {
        Mockito.doNothing().when(checkLoginIpService).processLogin(Mockito.any());
        userLoginEventProcessor = new UserLoginEventProcessor(checkLoginIpService);
    }

    @Test(expected = NullPointerException.class)
    public void processNullInputTest() {
        userLoginEventProcessor.process(null);
    }

    @Test
    public void processEmptyMapInputTest() {
        userLoginEventProcessor.process(Collections.emptyMap());
        Mockito.verify(checkLoginIpService, Mockito.times(0)).processLogin(Mockito.any());
    }

    @Test
    public void processNotEmptyMapInputTest() {
        UserLogin firstUserLogin = new UserLogin(
                new UserLoginDTO(1L, "1.1.1.1", System.currentTimeMillis() - 60 * 60 * 1000));
        UserLogin secondUserLogin = new UserLogin(
                new UserLoginDTO(2L, "2.2.2.2", System.currentTimeMillis()));
        List<UserLogin> userLogins = ImmutableList.of(firstUserLogin, secondUserLogin);
        Map<String, List<UserLogin>> messages = ImmutableMap.of(TOPIC, userLogins);
        userLoginEventProcessor.process(messages);
        Mockito.verify(checkLoginIpService, Mockito.times(2)).processLogin(Mockito.any());
        Mockito.verify(checkLoginIpService, Mockito.times(1))
                .processLogin(Mockito.refEq(firstUserLogin));
        Mockito.verify(checkLoginIpService, Mockito.times(1))
                .processLogin(Mockito.refEq(secondUserLogin));
    }


}
