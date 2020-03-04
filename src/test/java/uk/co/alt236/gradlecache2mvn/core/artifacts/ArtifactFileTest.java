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
        final String md5 = "qux1";
        final String sha1 = "qux2";
        final File file = new File("quux");

        final ArtifactFile artifact = new ArtifactFile(file, groupId, artifactId, version);
        assertEquals(groupId, artifact.getGroupId());
        assertEquals(artifactId, artifact.getArtifactId());
        assertEquals(version, artifact.getVersion());

        assertSame(file, artifact.getFile());
        assertEquals("quux", artifact.getFileName());
    }
}