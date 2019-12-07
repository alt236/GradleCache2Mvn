package uk.co.alt236.gradlecache2mvn.core.exporter;

import org.apache.commons.io.FileUtils;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.CopyJobFactory;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.CopyJobs;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.FileToCopy;
import uk.co.alt236.gradlecache2mvn.util.Hasher;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Exporter {
    private static final String LOG_TEMPLATE = "For artifact: %s";

    public Result export(final List<GradleMavenArtifactGroup> artifacts,
                         final String exportPath,
                         final boolean dryRun) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        int errors = 0;
        int copied = 0;
        int skipped = 0;

        final List<GradleMavenArtifactGroup> sortedArtifacts = new ArrayList<>(artifacts);
        sortedArtifacts.sort(Comparator.comparing(GradleMavenArtifactGroup::getGradleDeclaration));

        for (final GradleMavenArtifactGroup artifactGroup : sortedArtifacts) {
            final CopyJobs copyJobs = copyJobFactory.createJobs(artifactGroup, exportPath);
            if (!copyJobs.hasError()) {
                Logger.log(LOG_TEMPLATE, artifactGroup.getGradleDeclaration());
                Logger.log("Files to copy: " + copyJobs.getFilesToCopy().size());
                final Result result = copy(copyJobs.getFilesToCopy(), dryRun);
                errors += result.getErrors();
                copied += result.getCopied();
                skipped += result.getSkipped();
            } else {
                errors += artifactGroup.getFiles().size();
            }
        }

        return new Result(copied, skipped, errors);
    }

    private Result copy(List<FileToCopy> filesToCopy, boolean dryRun) {
        final List<FileToCopy> sortedFiles = new ArrayList<>(filesToCopy);
        sortedFiles.sort(Comparator.comparing(t -> t.getSource().getFileName()));

        int copied = 0;
        int skipped = 0;

        for (final FileToCopy fileToCopy : sortedFiles) {
            final File destination = fileToCopy.getDestination();
            final File source = fileToCopy.getSource().getFile();

            final String gradleDeclaration = fileToCopy.getSource().getGradleDeclaration();
            final String fileName = fileToCopy.getSource().getFileName();

            if (shouldCopy(fileToCopy)) {
                copied++;
                Logger.logImportant("Copying...  %s, file: %s", gradleDeclaration, fileName);
//                Logger.logImportant("Copying...  %s, file: %s. From %s to %s", gradleDeclaration, fileName, source, destination);

                if (dryRun) {
                    Logger.logImportant("---DRY RUN---");
                } else {
                    try {
                        FileUtils.copyFile(source, destination, true);
                        writeMd5(fileToCopy);
                        writeSha1(fileToCopy);
                    } catch (IOException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            } else {
                skipped++;
                Logger.log("Skipping...  %s, file %s, as it exists and is the same", gradleDeclaration, fileName);
            }
        }

        return new Result(copied, skipped, 0);
    }

    private boolean shouldCopy(FileToCopy fileToCopy) {
        final boolean retVal;
        final boolean destinationExists = fileToCopy.getDestination().exists();
        if (destinationExists) {
            final String destMd5 = Hasher.getMd5(fileToCopy.getDestination());
            final String sourceMd5 = fileToCopy.getSource().getMd5();
            retVal = !destMd5.equals(sourceMd5);
        } else {
            retVal = true;
        }

        return retVal;
    }

    private void writeMd5(FileToCopy fileToCopy) throws IOException {
        final File destination = fileToCopy.getDestination();
        final File destinationMd5 = new File(fileToCopy.getDestination().getAbsolutePath() + ".md5");

        // It has to be 2 spaces between the fields;
        final String content = fileToCopy.getSource().getMd5() + "  " + destination.getName();

        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8");
    }

    private void writeSha1(FileToCopy fileToCopy) throws IOException {
        final File destination = fileToCopy.getDestination();
        final File destinationMd5 = new File(fileToCopy.getDestination().getAbsolutePath() + ".sha1");

        // It has to be 2 spaces between the fields;
        final String content = fileToCopy.getSource().getSha1() + "  " + destination.getName();

        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8");
    }
}
