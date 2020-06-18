package uk.co.alt236.gradlecache2mvn.util;

import java.util.concurrent.atomic.AtomicReference;

public final class Logger {

    private static AtomicReference<Mode> currentMode = new AtomicReference<>(Mode.IMPORTANT);
    private static final Colorizer coloriser = new Colorizer(true);

    public static void log(final String template, Object... params) {
        log(String.format(template, params));
    }

    public static void log(final String message) {
        if (isLogEverything()) {
            out(message);
        }
    }

    public static void logImportant(final String template, Object... params) {
        logImportant(String.format(template, params));
    }

    public static boolean isLogEverything() {
        return currentMode.get() == Mode.ALL;
    }

    public static void logImportant(final String message) {
        out(coloriser.important(message));
    }

    public static void logError(final String message) {
        err(coloriser.error("ERROR: " + message));
    }

    public static void setMode(final Mode mode) {
        currentMode.set(mode);
    }

    private static void out(final String message) {
        System.out.println(message);
    }

    private static void err(final String message) {
        System.err.println(message);
    }

    public enum Mode {
        ALL,
        IMPORTANT
    }
}
