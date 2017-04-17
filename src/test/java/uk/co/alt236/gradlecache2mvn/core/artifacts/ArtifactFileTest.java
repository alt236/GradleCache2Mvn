package uk.co.alt236.gradlecache2mvn.core.artifacts;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ArtifactFileTest {

    @Test
    public void testValid() {
        final String groupId = "foo";
        final String artifactId = "bar";
        final String version = "baz";
        final String hash = "qux";
        final File file = new File("quux");

        final ArtifactFile artifact = new ArtifactFile(groupId, artifactId, version, file, hash);
        assertEquals(groupId, artifact.getGroupId());
        assertEquals(artifactId, artifact.getArtifactId());
        assertEquals(version, artifact.getVersion());
        assertEquals(hash, artifact.getMd5());

        assertSame(file, artifact.getFile());
        assertEquals("quux", artifact.getFileName());
    }
}