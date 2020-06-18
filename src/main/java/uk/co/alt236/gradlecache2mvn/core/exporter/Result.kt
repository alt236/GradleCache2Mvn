package uk.co.alt236.gradlecache2mvn.core.exporter;

public class Result {
    private final int copied;
    private final int skipped;
    private final int errors;


    public Result(int copied, int skipped, int errors) {
        this.copied = copied;
        this.skipped = skipped;
        this.errors = errors;
    }

    public int getCopied() {
        return copied;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getErrors() {
        return errors;
    }
}
