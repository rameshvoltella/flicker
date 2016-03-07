package xmpp.extensions.com.ua.max.oliynick.util;

/**
 * <p>
 * Simple tuple implementation
 * </p>
 * @author Max Oliynick
 * */
public class Tuple<First, Second> {

    private First first;

    private Second second;

    public Tuple() {}

    public Tuple(First f, Second s) {
        this.first = f;
        this.second = s;
    }

    public First getFirst() {
        return first;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public Second getSecond() {
        return second;
    }

    public void setSecond(Second second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "first=" + first + ", second=" + second;
    }

}
