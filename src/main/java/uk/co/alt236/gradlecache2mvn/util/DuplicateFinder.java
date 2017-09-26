package uk.co.alt236.gradlecache2mvn.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class DuplicateFinder {

    private DuplicateFinder() {
        // NOOP
    }

    public static <T> Set<T> findDuplicates(Collection<T> items) {
        final Set<T> duplicates = new TreeSet<>();
        if (items.size() > 1) {
            final Set<T> uniques = new HashSet<>();

            for (T t : items) {
                if (!uniques.add(t)) {
                    duplicates.add(t);
                }
            }
        }
        return duplicates;
    }
}
