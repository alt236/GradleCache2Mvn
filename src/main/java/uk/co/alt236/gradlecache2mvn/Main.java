package uk.co.alt236.gradlecache2mvn;

import org.apache.commons.cli.*;
import uk.co.alt236.gradlecache2mvn.cli.CommandHelpPrinter;
import uk.co.alt236.gradlecache2mvn.cli.CommandLineWrapper;
import uk.co.alt236.gradlecache2mvn.cli.OptionsBuilder;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.exporter.Exporter;
import uk.co.alt236.gradlecache2mvn.core.reader.GradleCacheReader;
import uk.co.alt236.gradlecache2mvn.resources.Strings;

import java.io.File;
import java.util.List;

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
                System.err.println(message);
                System.exit(1);
            }

            new Core(strings, new CommandLineWrapper(line)).doWork();
        }

    }

    private static String getJarName() {
        final File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString());
        return f.getName();
    }

    private static String sanePath(final String path) {
        final String retVal;

        if (path == null) {
            retVal = "";
        } else {
            retVal = path.replaceFirst("^~", System.getProperty("user.home"));
        }

        return retVal;
    }

    private static class Core {

        private final CommandLineWrapper commandLine;
        private final Strings strings;

        Core(final Strings strings, final CommandLineWrapper commandLine) {
            this.strings = strings;
            this.commandLine = commandLine;
        }

        void doWork() {
            final String input = commandLine.getInputDirectory();
            final String output = commandLine.getOutputDirectory();
            final boolean dryRun = commandLine.isDryRun();


            final String saneInput = sanePath(
                    input == null || input.isEmpty()
                            ? strings.getString("default_gradle_cache_location")
                            : input);

            final String saneOutput = sanePath(output);

            System.out.println("Input (Gradle cache location): " + saneInput);
            System.out.println("Output (Maven repo location): " + saneOutput);
            System.out.println("Dry run: " + dryRun);
            System.out.println("--------------------------");

            final List<GradleMavenArtifactGroup> artifacts =
                    new GradleCacheReader(sanePath(saneInput)).getDependencies();

            System.out.println("--------------------------");

            new Exporter().export(artifacts, saneOutput, dryRun);
        }
    }


}
