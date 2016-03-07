package xmpp.extensions.com.ua.max.oliynick.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.ISODateTimeFormat;

public final class UTCUtils {

    /**
     * UTC date format
     * */
    public static final DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss[.sss]'Z'");

    /**
     * Parser for the "fraction" part of a date-time value.
     */
    private static final DateTimeParser fractParser =
            new DateTimeFormatterBuilder()
                    .appendLiteral('.')
                    .appendFractionOfSecond(3, 9)
                    .toParser();

    private static final DateTimePrinter fractPrinter =
            new DateTimeFormatterBuilder()
                    .appendLiteral('.')
                    .appendFractionOfSecond(3, 9)
                    .toPrinter();

    private static final DateTimeParser baseParser =
            new DateTimeFormatterBuilder()
                    .append(ISODateTimeFormat.date())
                    .appendLiteral('T')
                    .append(ISODateTimeFormat.hourMinuteSecond())
                    .appendOptional(fractParser)
                    .appendLiteral('Z')
                    .toParser();

    private static final DateTimePrinter basePrinter =
            new DateTimeFormatterBuilder()
                    .append(ISODateTimeFormat.date())
                    .appendLiteral('T')
                    .append(ISODateTimeFormat.hourMinuteSecond())
                    .append(fractPrinter)
                    .appendLiteral('Z')
                    .toPrinter();

    /**
     * A formatter for a "local" date/time without time zone offset
     * (in the format "yyyy-dd-mmThh:mm:ss[.fff]Z").
     */
    private static final DateTimeFormatter formatter =
            new DateTimeFormatterBuilder()
                    .append(basePrinter, baseParser)
                    .toFormatter()
                    .withZone(DateTimeZone.UTC);

    /**
     * Tries to parse input UTC string into {@linkplain Date}.
     * If operation is successful, than {@linkplain Date} will be
     * returned, in another case - <b>null</b>
     * @param utc UTC source string
     * */
    public static DateTime fromString(final String utc) {

        try {
            return formatter.parseDateTime(utc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Tries to parse input UTC string into {@linkplain Date}
     * @param utc UTC source string
     * */
    public static String toUTCString(final DateTime date) {
        return formatter.print(date);
    }

    public static void main(String[] args) {
        //UTCUtils.fromString("2016-02-21T01:00:59.629Z");
        //UTCUtils.fromString("2011-04-15T20:08:18.228Z");

        String startDate = "2016-02-21T01:00:59.629Z";

        DateTime time = UTCUtils.fromString(startDate);

        System.out.println(time);
        System.out.println(UTCUtils.toUTCString(time));

    }

}
