package uk.co.alt236.gradlecache2mvn;

import uk.co.alt236.gradlecache2mvn.cli.CommandLineWrapper;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.exporter.Exporter;
import uk.co.alt236.gradlecache2mvn.core.reader.GradleCacheReader;
import uk.co.alt236.gradlecache2mvn.resources.Strings;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*package*/ class GradleCache2mvn {

    private final CommandLineWrapper commandLine;
    private final Strings strings;

    GradleCache2mvn(final Strings strings, final CommandLineWrapper commandLine) {
        this.strings = strings;
        this.commandLine = commandLine;
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

    void doWork() {
        final String input = commandLine.getInputDirectory();
        final String output = commandLine.getOutputDirectory();
        final boolean verbose = commandLine.isVerbose();
        final boolean dryRun = commandLine.isDryRun();
        Logger.setMode(verbose ? Logger.Mode.ALL : Logger.Mode.IMPORTANT);

        final String saneInput = sanePath(
                input == null || input.isEmpty()
                        ? strings.getString("default_gradle_cache_location")
                        : input);

        final String saneOutput = sanePath(output);

        Logger.logImportant("Input (Gradle cache location): " + saneInput);
        Logger.logImportant("Output (Maven repo location): " + saneOutput);
        Logger.logImportant("Dry run: " + dryRun);
        final List<GradleMavenArtifactGroup> artifacts =
                new GradleCacheReader(sanePath(saneInput)).getDependencies();
        final Exporter.Result result = new Exporter().export(artifacts, saneOutput, dryRun);

        final int artifactCount = artifacts.size();
        final int errors = result.getErrors();
        final int copied = result.getCopied();
        final int skipped = result.getSkipped();
        final int fileCount = artifacts.stream()
                .map(GradleMavenArtifactGroup::getFiles)
                .filter(Objects::nonNull)
                .mapToInt(Collection::size)
                .sum();


        Logger.logImportant("Done! Artifacts: %d, Files %d, Copied %d, Skipped %d, Errors %d", artifactCount, fileCount, copied, skipped, errors);
        if ((skipped + copied + errors) != fileCount) {
            throw new IllegalStateException("The sum of skipped, copied and errors is not the same as the total file count!");
        }
    }
}
