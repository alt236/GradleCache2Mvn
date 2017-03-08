package uk.co.alt236.gradlecache2mvn.util;

import java.io.Closeable;
import java.io.IOException;

public final class StreamUtil {

    private StreamUtil() {
        // NOOP
    }

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
