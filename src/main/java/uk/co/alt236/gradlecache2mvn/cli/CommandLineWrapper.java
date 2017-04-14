package uk.co.alt236.gradlecache2mvn.cli;

import org.apache.commons.cli.CommandLine;

public class CommandLineWrapper {

    private final CommandLine commandLine;

    public CommandLineWrapper(final CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public boolean isDryRun() {
        return commandLine.hasOption(OptionsBuilder.ARG_DRY_RUN);
    }

    public String getOutputDirectory() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_MVN_DIR);
    }

    public String getInputDirectory() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_GRADLE_CACHE);
    }
}
