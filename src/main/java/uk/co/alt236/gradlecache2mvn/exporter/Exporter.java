package uk.co.alt236.gradlecache2mvn.exporter;

import org.apache.commons.io.FileUtils;
import uk.co.alt236.gradlecache2mvn.artifacts.gradle.GradleMavenArtifactGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Exporter {
    public void export(final List<GradleMavenArtifactGroup> artifacts,
                       final String exportPath) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        for (final GradleMavenArtifactGroup artifactGroup : artifacts) {
            final List<CopyJobFactory.FileToCopy> filesToCopy = copyJobFactory.createJobs(artifactGroup, exportPath);
            copy(filesToCopy);
        }
    }

    private void copy(List<CopyJobFactory.FileToCopy> filesToCopy) {
        System.out.println("Files to copy: " + filesToCopy.size());

        for (final CopyJobFactory.FileToCopy fileToCopy : filesToCopy) {
            final File destination = fileToCopy.getNewPath();
            System.out.println("\tWriting: " + destination);

            try {
                FileUtils.copyFile(fileToCopy.getFile().getFile(), destination, true);
                writeMd5(fileToCopy);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private void writeMd5(CopyJobFactory.FileToCopy fileToCopy) throws IOException {
        final File destination = fileToCopy.getNewPath();
        final File destinationMd5 = new File(fileToCopy.getNewPath().getAbsolutePath() + ".md5");

        // It has to be 2 spaces between the fields;
        final String content = fileToCopy.getFile().getMd5() + "  " + destination.getName();

        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8");
    }
}
