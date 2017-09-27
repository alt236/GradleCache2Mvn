package uk.co.alt236.gradlecache2mvn;

import org.apache.commons.cli.*;
import uk.co.alt236.gradlecache2mvn.cli.CommandHelpPrinter;
import uk.co.alt236.gradlecache2mvn.cli.CommandLineWrapper;
import uk.co.alt236.gradlecache2mvn.cli.OptionsBuilder;
import uk.co.alt236.gradlecache2mvn.resources.Strings;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        final Strings strings = new Strings();
        final CommandLineParser parser = new DefaultParser();
        final Options options = new OptionsBuilder(strings).compileOptions();

        if (args.length == 0) {
            new CommandHelpPrinter(options, getJarName()).printHelp();
        } else {
            CommandLine line = null;

            try {
                line = parser.parse(options, args);
            } catch (final ParseException exp) {
                final String message = exp.getMessage();
                Logger.logError(message);
                System.exit(1);
            }

            new GradleCache2mvn(strings, new CommandLineWrapper(line)).doWork();
        }

    }

    private static String getJarName() {
        final File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString());
        return f.getName();
    }
}
