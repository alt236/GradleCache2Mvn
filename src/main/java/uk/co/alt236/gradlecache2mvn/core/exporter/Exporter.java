package uk.co.alt236.gradlecache2mvn.core.exporter;

import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.CopyJobFactory;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.CopyJobs;
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.FileToCopy;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Exporter {
    private static final String LOG_TEMPLATE = "For artifact: %s";
    private static final FileWriter fileWriter = new FileWriter();

    public Result export(final List<GradleMavenArtifactGroup> artifacts,
                         final String exportPath,
                         final boolean dryRun,
                         final boolean overwriteDifferentFiles) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        int errors = 0;
        int copied = 0;
        int skipped = 0;

        final List<GradleMavenArtifactGroup> sortedArtifacts = new ArrayList<>(artifacts);
        sortedArtifacts.sort(Comparator.comparing(GradleMavenArtifactGroup::getGradleDeclaration));

        final CopyEvaluator evaluator = new CopyEvaluator(overwriteDifferentFiles);

        for (final GradleMavenArtifactGroup artifactGroup : sortedArtifacts) {
            final CopyJobs copyJobs = copyJobFactory.createJobs(artifactGroup, exportPath);
            if (!copyJobs.hasError()) {
                Logger.log(LOG_TEMPLATE, artifactGroup.getGradleDeclaration());
                Logger.log("Files to copy: " + copyJobs.getFilesToCopy().size());
                final Result result = copy(evaluator, copyJobs.getFilesToCopy(), dryRun);
                errors += result.getErrors();
                copied += result.getCopied();
                skipped += result.getSkipped();
            } else {
                errors += artifactGroup.getArtifacts().size();
            }
        }

        return new Result(copied, skipped, errors);
    }

    private Result copy(CopyEvaluator evaluator, List<FileToCopy> filesToCopy, boolean dryRun) {
        final List<FileToCopy> sortedFiles = new ArrayList<>(filesToCopy);
        sortedFiles.sort(Comparator.comparing(t -> t.getSource().getFileName()));


        int copied = 0;
        int skipped = 0;

        for (final FileToCopy fileToCopy : sortedFiles) {
            final String gradleDeclaration = fileToCopy.getSource().getGradleDeclaration();
            final String fileName = fileToCopy.getSource().getFileName();

            switch (evaluator.evaluate(fileToCopy)) {
                case COPY:
                    copied++;
                    Logger.logImportant("Copying...  %s, file: %s", gradleDeclaration, fileName);
                    if (dryRun) {
                        Logger.logImportant("---DRY RUN---");
                    } else {
                        fileWriter.write(fileToCopy);
                    }
                    break;
                case SKIP_SAME_HASH:
                    skipped++;
                    Logger.log("Skipping...  %s, file %s, as it already exists and is identical.", gradleDeclaration, fileName);
                    break;
                case SKIP_FILE_EXISTS:
                    skipped++;
                    Logger.log("Skipping...  %s, file %s, as it already exists.", gradleDeclaration, fileName);
                    break;
            }
        }

        return new Result(copied, skipped, 0);
    }


}
