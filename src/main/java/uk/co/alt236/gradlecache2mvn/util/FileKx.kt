package uk.co.alt236.gradlecache2mvn.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class Hasher {

    private Hasher() {
        // NOOP
    }

    public static String getMd5(final File file) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            return DigestUtils.md5Hex(bis);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            if (bis != null) {
                StreamUtil.close(bis);
            }
        }
    }

    public static String getSha1(final File file) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            return DigestUtils.sha1Hex(bis);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            if (bis != null) {
                StreamUtil.close(bis);
            }
        }
    }
}
