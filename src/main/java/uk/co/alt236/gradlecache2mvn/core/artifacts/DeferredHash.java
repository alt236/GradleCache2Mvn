package uk.co.alt236.gradlecache2mvn.core.artifacts;

import uk.co.alt236.gradlecache2mvn.util.Hasher;

import java.io.File;

final class DeferredHash {
    private static final String ALGO_SHA1 = "sha1";
    private static final String ALGO_MD5 = "md5";

    private final File file;
    private final String algorithm;

    private String value;

    private DeferredHash(File file, String algorithm) {
        this.file = file;
        this.algorithm = algorithm;
    }

    public static DeferredHash createForSha1(File file) {
        return new DeferredHash(file, ALGO_SHA1);
    }

    public static DeferredHash createForMd5(File file) {
        return new DeferredHash(file, ALGO_MD5);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public synchronized String getHash() {
        if (value == null) {
            switch (algorithm) {
                case ALGO_SHA1:
                    value = Hasher.getSha1(file);
                    break;
                case ALGO_MD5:
                    value = Hasher.getMd5(file);
                    break;
                default:
                    throw new IllegalStateException("Don't know how to hash with " + algorithm);
            }
        }

        return value;
    }
}
