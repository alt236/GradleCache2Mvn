package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory;

import java.util.List;

public class CopyJobs {
    private final List<FileToCopy> filesToCopy;
    private final boolean error;

    CopyJobs(List<FileToCopy> filesToCopy, boolean error) {
        this.filesToCopy = filesToCopy;
        this.error = error;
    }

    public List<FileToCopy> getFilesToCopy() {
        return filesToCopy;
    }

    public boolean hasError() {
        return error;
    }
}
