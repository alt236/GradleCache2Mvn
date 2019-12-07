package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory;

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;

import java.io.File;

public class FileToCopy {
    private final ArtifactFile file;
    private final File newPath;

    FileToCopy(final ArtifactFile file,
               final File newPath) {
        this.file = file;
        this.newPath = newPath;
    }

    public ArtifactFile getSource() {
        return file;
    }

    public File getDestination() {
        return newPath;
    }
}
