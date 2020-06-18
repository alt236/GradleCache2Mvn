package uk.co.alt236.gradlecache2mvn.core.artifacts;

import java.io.File;

public class ArtifactFile implements MavenArtifact {
    private final File file;
    private final DeferredHash md5;
    private final DeferredHash sha1;
    private final String groupId;
    private final String artifactId;
    private final String version;

    public ArtifactFile(final File file,
                        final String groupId,
                        final String artifactId,
                        final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.file = file;
        this.md5 = DeferredHash.createForMd5(file);
        this.sha1 = DeferredHash.createForSha1(file);
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
        return md5.getHash();
    }

    public String getSha1() {
        return sha1.getHash();
    }

    public String getFileName() {
        return file.getName();
    }

    @Override
    public String getGradleDeclaration() {
        return "'" + groupId + ":" + artifactId + ":" + version + "'";
    }

    @Override
    public String toString() {
        return "ArtifactFile{" +
                "file=" + file.getName() +
                ", md5='" + md5 + '\'' +
                ", sha1='" + sha1 + '\'' +
                '}';
    }
}
