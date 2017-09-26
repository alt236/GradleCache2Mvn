package uk.co.alt236.gradlecache2mvn.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class DuplicateFinderTest {

    private static <T> Collection<T> toCollection(T... items) {
        return Arrays.asList(items);
    }

    @Test(expected = NullPointerException.class)
    public void shouldBlowOnNull() {
        DuplicateFinder.findDuplicates(null);
    }

    @Test
    public void singleItemNotDuplicate() {
        final Collection items = toCollection("a");

        assertEquals(0, DuplicateFinder.findDuplicates(items).size());
    }

    @Test
    public void differentItemNotDuplicate() {
        final Collection items = toCollection("a", "b", "c", "d");

        assertEquals(0, DuplicateFinder.findDuplicates(items).size());
    }

    @Test
    public void sameItemsDuplicated() {
        final Collection items = toCollection("a", "a", "b", "b", "d");

        assertEquals(2, DuplicateFinder.findDuplicates(items).size());
    }

}