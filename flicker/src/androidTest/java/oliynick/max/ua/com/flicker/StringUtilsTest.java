package oliynick.max.ua.com.flicker;

import android.test.InstrumentationTestCase;

import com.ua.max.oliynick.flicker.util.StringUtils;

/**
 * Created by Максим on 18.02.2016.
 */
public class StringUtilsTest extends InstrumentationTestCase {

    public void testParseBareJID() throws Exception {
        String bareJid = "user@example";

        assertEquals("Wrong result", bareJid, StringUtils.parseBareJid(bareJid));
    }

    public void testParseJID() throws Exception {
        String bareJid = "user@example/Flicker";

        assertEquals("Wrong result", "user@example", StringUtils.parseBareJid(bareJid));
    }
}
