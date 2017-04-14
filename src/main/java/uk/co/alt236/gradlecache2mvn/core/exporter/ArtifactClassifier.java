package uk.co.alt236.gradlecache2mvn.core.exporter;

import org.apache.commons.io.FilenameUtils;
import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;

import java.util.ArrayList;
import java.util.List;

/*package*/ final class ArtifactClassifier {


    public static ClassifiedFiles classify(final GradleMavenArtifactGroup artifactGroup) {

        final List<ArtifactFile> pomFiles = new ArrayList<>();
        final List<ArtifactFile> primaryArtifactFiles = new ArrayList<>();
        final List<ArtifactFile> secondaryArtifactFiles = new ArrayList<>();
        final List<ArtifactFile> otherFiles = new ArrayList<>();

        for (final ArtifactFile file : artifactGroup.getFiles()) {
            final String fileName = file.getFileName();
            if (FilenameUtils.isExtension(fileName, "pom")) {
                pomFiles.add(file);
            } else if (FilenameUtils.isExtension(fileName, "md5")) {
                otherFiles.add(file);
            } else {
                //$artifactId-$version.$extension
                final String expectedPrimaryName = file.getArtifactId() + "-" + file.getVersion() + ".";
                if (fileName.startsWith(expectedPrimaryName)) {
                    primaryArtifactFiles.add(file);
                } else {
                    secondaryArtifactFiles.add(file);
                }
            }
        }

        return new ClassifiedFiles(pomFiles, primaryArtifactFiles, secondaryArtifactFiles, otherFiles);
    }

    public static class ClassifiedFiles {
        private final List<ArtifactFile> pomFiles;
        private final List<ArtifactFile> primaryArtifactFiles;
        private final List<ArtifactFile> secondaryArtifactFiles;
        private final List<ArtifactFile> otherFiles;


        private ClassifiedFiles(List<ArtifactFile> pomFiles,
                                List<ArtifactFile> primaryArtifactFiles,
                                List<ArtifactFile> secondaryArtifactFiles,
                                List<ArtifactFile> otherFiles) {
            this.pomFiles = pomFiles;
            this.primaryArtifactFiles = primaryArtifactFiles;
            this.secondaryArtifactFiles = secondaryArtifactFiles;
            this.otherFiles = otherFiles;
        }

        public List<ArtifactFile> getPomFiles() {
            return pomFiles;
        }

        public List<ArtifactFile> getPrimaryArtifactFiles() {
            return primaryArtifactFiles;
        }

        public List<ArtifactFile> getSecondaryArtifactFiles() {
            return secondaryArtifactFiles;
        }

        public List<ArtifactFile> getOtherFiles() {
            return otherFiles;
        }
    }

}
