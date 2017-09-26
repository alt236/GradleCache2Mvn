package uk.co.alt236.gradlecache2mvn.core.exporter;

import org.apache.commons.io.FileUtils;
import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.DuplicateFinder;
import uk.co.alt236.gradlecache2mvn.util.Hasher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Exporter {
    private static final String LOG_TEMPLATE = "For artifactId: %s, groupId: %s, version: %s";
    private static final String FILES_WITH_SAME_NAME_TEMPLATE = "ERROR: artifactId: %s, groupId: %s, version: %s has files with same name! %s";

    public void export(final List<GradleMavenArtifactGroup> artifacts,
                       final String exportPath,
                       final boolean dryRun) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        for (final GradleMavenArtifactGroup artifactGroup : artifacts) {
            if (validateArtifactGroup(artifactGroup)) {
                System.out.println(String.format(Locale.US, LOG_TEMPLATE,
                        artifactGroup.getArtifactId(), artifactGroup.getGroupId(), artifactGroup.getVersion()));

                final List<CopyJobFactory.FileToCopy> filesToCopy = copyJobFactory.createJobs(artifactGroup, exportPath);
                System.out.println("\tfiles to copy: " + filesToCopy.size());
                copy(filesToCopy, dryRun);
            }
        }
    }

    private boolean validateArtifactGroup(GradleMavenArtifactGroup artifactGroup) {
        boolean retVal = true;
        final List<String> fileNames = new ArrayList<>();
        for (final ArtifactFile file : artifactGroup.getFiles()) {
            fileNames.add(file.getFileName());
        }

        final Set<String> duplicates = DuplicateFinder.findDuplicates(fileNames);

        if (!duplicates.isEmpty()) {
            System.err.println(String.format(Locale.US, FILES_WITH_SAME_NAME_TEMPLATE,
                    artifactGroup.getArtifactId(), artifactGroup.getGroupId(),
                    artifactGroup.getVersion(), duplicates));
            retVal = false;
        }

        return retVal;
    }

    private void copy(List<CopyJobFactory.FileToCopy> filesToCopy,
                      boolean dryRun) {
        for (final CopyJobFactory.FileToCopy fileToCopy : filesToCopy) {
            final File destination = fileToCopy.getDestination();
            final File source = fileToCopy.getSource().getFile();
            if (shouldCopy(fileToCopy)) {
                System.out.printf("\tCopying %s to %s%n", source, destination);
                if (!dryRun) {
                    try {
                        FileUtils.copyFile(source, destination, true);
                        writeMd5(fileToCopy);
                    } catch (IOException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                } else {
                    System.out.println("\t---DRY RUN---");
                }
            } else {
                System.out.printf("\tSkipping as it exists and is the same: Copying %s to %s%n", source, destination);
            }
        }
    }

    private boolean shouldCopy(CopyJobFactory.FileToCopy fileToCopy) {
        final boolean retVal;
        final boolean destinationExists = fileToCopy.getDestination().exists();
        if (destinationExists) {
            final String destMd5 = Hasher.getMd5(fileToCopy.getDestination());
            retVal = !destMd5.equals(fileToCopy.getSource().getMd5());
        } else {
            retVal = true;
        }

        return retVal;
    }

    private void writeMd5(CopyJobFactory.FileToCopy fileToCopy) throws IOException {
        final File destination = fileToCopy.getDestination();
        final File destinationMd5 = new File(fileToCopy.getDestination().getAbsolutePath() + ".md5");

        // It has to be 2 spaces between the fields;
        final String content = fileToCopy.getSource().getMd5() + "  " + destination.getName();

        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8");
    }
}
