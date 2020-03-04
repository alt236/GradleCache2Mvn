package uk.co.alt236.gradlecache2mvn.core.exporter.classifier;

import org.junit.Test;
import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArtifactClassifierTest {
    private static final String groupId = "foo";
    private static final String artifactId = "bar";
    private static final String version = "baz";

    private static GradleMavenArtifactGroup createArtifactGroup(final String... filenames) {
        final List<ArtifactFile> artifacts = new ArrayList<>();
        for (final String filename : filenames) {
            artifacts.add(createArtifactFile(filename));
        }

        return new GradleMavenArtifactGroup(groupId, artifactId, version, artifacts);
    }

    private static ArtifactFile createArtifactFile(final String filename) {
        return new ArtifactFile(new File(filename), groupId, artifactId, version);
    }

    private static void assertListSize(final int expected, final List<?> list) {
        final String message = "Contents: " + list.toString();
        assertEquals(message, expected, list.size());
    }

    @Test
    public void testClassify_valid() throws Exception {
        final String validName = artifactId + "-" + version;
        final String primaryItem = validName + ".jar";
        final String secondaryItem = validName + "-other.jar";
        final String pomFile = "blah.pom";
        final String otherFile = "blah.md5";

        final GradleMavenArtifactGroup group = createArtifactGroup(
                primaryItem,
                secondaryItem,
                pomFile,
                otherFile);

        final ArtifactClassifier.ClassifiedFiles classifiedFiles = ArtifactClassifier.classify(group);

        assertNotNull(classifiedFiles.getPrimaryArtifactFiles());
        assertNotNull(classifiedFiles.getSecondaryArtifactFiles());
        assertNotNull(classifiedFiles.getPomFiles());
        assertNotNull(classifiedFiles.getOtherFiles());

        assertListSize(1, classifiedFiles.getPrimaryArtifactFiles());
        assertListSize(1, classifiedFiles.getSecondaryArtifactFiles());
        assertListSize(1, classifiedFiles.getPomFiles());
        assertListSize(1, classifiedFiles.getOtherFiles());

        assertEquals(primaryItem, classifiedFiles.getPrimaryArtifactFiles().get(0).getFileName());
        assertEquals(secondaryItem, classifiedFiles.getSecondaryArtifactFiles().get(0).getFileName());
        assertEquals(pomFile, classifiedFiles.getPomFiles().get(0).getFileName());
        assertEquals(otherFile, classifiedFiles.getOtherFiles().get(0).getFileName());
    }

    @Test
    public void testClassify_other_files() throws Exception {
        final String[] files = {"blah.md5", "blah.sha1", "blah", "blah.MD5", "blah.SHA1"};

        final GradleMavenArtifactGroup group = createArtifactGroup(files);

        final ArtifactClassifier.ClassifiedFiles classifiedFiles = ArtifactClassifier.classify(group);

        assertListSize(0, classifiedFiles.getPrimaryArtifactFiles());
        assertListSize(0, classifiedFiles.getSecondaryArtifactFiles());
        assertListSize(0, classifiedFiles.getPomFiles());
        assertListSize(files.length, classifiedFiles.getOtherFiles());
    }

    @Test
    public void testClassify_empty() throws Exception {
        final GradleMavenArtifactGroup group = createArtifactGroup(new String[0]);

        final ArtifactClassifier.ClassifiedFiles classifiedFiles = ArtifactClassifier.classify(group);

        assertNotNull(classifiedFiles.getPrimaryArtifactFiles());
        assertNotNull(classifiedFiles.getSecondaryArtifactFiles());
        assertNotNull(classifiedFiles.getPomFiles());
        assertNotNull(classifiedFiles.getOtherFiles());
    }
}