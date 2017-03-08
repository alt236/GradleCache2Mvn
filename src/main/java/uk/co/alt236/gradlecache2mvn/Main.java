package uk.co.alt236.gradlecache2mvn;

import uk.co.alt236.gradlecache2mvn.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.exporter.Exporter;
import uk.co.alt236.gradlecache2mvn.reader.GradleCacheReader;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String input = "~/.gradle/caches/modules-2/files-2.1";
        final String ouput = "~/tmp/fakemvn";

        final List<GradleMavenArtifactGroup> artifacts = new GradleCacheReader(sanePath(input))
                .getDependencies();

        System.out.println("--------------------------");

        new Exporter().export(artifacts, sanePath(ouput));
    }

    private static String sanePath(final String path) {
        return path.replaceFirst("^~", System.getProperty("user.home"));
    }
}
