package uk.co.alt236.gradlecache2mvn.artifacts;

import java.io.File;

public class ArtifactFile implements MavenArtifact {
    private final File file;
    private final String hash;
    private final String groupId;
    private final String artifactId;
    private final String version;

    public ArtifactFile(final String groupId,
                        final String artifactId,
                        final String version,
                        final File file,
                        final String hash) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.file = file;
        this.hash = hash;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public File getFile() {
        return file;
    }

    public String getMd5() {
        return hash;
    }

    public String getFileName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return "ArtifactFile{" +
                "file=" + file.getName() +
                ", hash='" + hash + '\'' +
                '}';
    }
}
