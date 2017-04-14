package uk.co.alt236.gradlecache2mvn;

import org.apache.commons.cli.*;
import uk.co.alt236.gradlecache2mvn.cli.CommandHelpPrinter;
import uk.co.alt236.gradlecache2mvn.cli.CommandLineWrapper;
import uk.co.alt236.gradlecache2mvn.cli.OptionsBuilder;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.reader.GradleCacheReader;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        final String input = "~/.gradle/caches/modules-2/files-2.1";
//        final String ouput = "~/tmp/fakemvn2";
//        final boolean dryRun = false;

        final CommandLineParser parser = new DefaultParser();
        final Options options = new OptionsBuilder().compileOptions();

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

            new Core(new CommandLineWrapper(line)).doWork();
        }

    }

    private static String getJarName() {
        final File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString());
        return f.getName();
    }

    private static String sanePath(final String path) {
        return path.replaceFirst("^~", System.getProperty("user.home"));
    }

    private static class Core {

        private final CommandLineWrapper commandLine;

        Core(final CommandLineWrapper commandLine) {
            this.commandLine = commandLine;
        }

        void doWork() {
            final String input = commandLine.getInputDirectory();
            final String output = commandLine.getOutputDirectory();
            final boolean dryRun = commandLine.isDryRun();

            final List<GradleMavenArtifactGroup> artifacts = new GradleCacheReader(sanePath(input))
                    .getDependencies();

            System.out.println("--------------------------");

//            new Exporter().export(artifacts, sanePath(output), dryRun);
        }
    }


}
