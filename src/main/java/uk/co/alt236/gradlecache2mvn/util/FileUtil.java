package uk.co.alt236.gradlecache2mvn.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {

    private FileUtil() {
        // NOOP
    }

    public static File[] getSubfolders(final File folder) {
        final List<File> subfolders = new ArrayList<>();

        final File[] contents = folder.listFiles();
        if (contents != null) {
            for (final File file : contents) {
                if (file.isDirectory()) {
                    subfolders.add(file);
                }
            }
        }

        return subfolders.toArray(new File[subfolders.size()]);
    }
}
