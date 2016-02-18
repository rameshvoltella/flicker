package oliynick.max.ua.com.test;

import com.ua.max.oliynick.flicker.util.StringUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p>Tests {@linkplain StringUtils} class</p>
 * Created by Максим on 18.02.2016.
 */
public class StringUtilsTest {

    @Test
    public void testParseBareJID() {
        String bareJid = "user@example";
        Assert.assertEquals("Wrong result", bareJid, StringUtils.parseBareJid(bareJid));
    }

    @Test
    public void testParseJID() {
        String bareJid = "user@example/Flicker/App";
        Assert.assertEquals("Wrong result", "user@example", StringUtils.parseBareJid(bareJid));
    }

    @Test
    public void testNullJID() {
        Assert.assertEquals("Wrong result", null, StringUtils.parseBareJid(null));
    }
}