package ru.spbstu.kafka.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbstu.antispam.UserLogin;

public class UserLoginEventParserTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final long USER_ID = 1L;
    private static final String IP_ADDRESS = "1.1.1.1";
    private static final long LOGIN_TIME = System.currentTimeMillis() - 1000 * 60 * 60;

    private UserLoginEventParser parser;

    @Before
    public void setUp() {
        this.parser = new UserLoginEventParser(OBJECT_MAPPER);
    }

    @Test(expected = NullPointerException.class)
    public void parseNullInputTest() {
        parser.parse(null);
    }

    @Test(expected = RuntimeException.class)
    public void parseNotParseableInputTest() {
        parser.parse("fklanlfjaskf");
    }

    @Test
    public void parseParseableInputTest() {
        UserLogin userLogin = parser.parse(
                "{\"user_id\" : " + USER_ID + "," +
                        " \"login_ip_address\" : \"" + IP_ADDRESS + "\"," +
                        " \"login_time\" : " + LOGIN_TIME +
                        "}");
        Assert.assertNotNull(userLogin);
        Assert.assertEquals(USER_ID, userLogin.getUserId());
        Assert.assertEquals(IP_ADDRESS, userLogin.getLoginIpAddress());
        Assert.assertEquals(LOGIN_TIME, userLogin.getLoginTime());
    }

}
