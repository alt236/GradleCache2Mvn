package uk.co.alt236.gradlecache2mvn.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import uk.co.alt236.gradlecache2mvn.resources.Strings;

import java.util.Locale;

public class OptionsBuilder {

    /*package*/ static final String ARG_MVN_DIR = "o";
    /*package*/ static final String ARG_MVN_DIR_LONG = "output";

    /*package*/ static final String ARG_GRADLE_CACHE = "i";
    /*package*/ static final String ARG_GRADLE_CACHE_LONG = "input";

    /*package*/ static final String ARG_DRY_RUN = "d";
    /*package*/ static final String ARG_DRY_RUN_LONG = "dryrun";

    private final Strings strings;

    public OptionsBuilder() {
        strings = new Strings();
    }

    private static <E extends Enum<?>> String toLower(final E enm) {
        return enm.name().toLowerCase(Locale.US);
    }

    public Options compileOptions() {
        final Options options = new Options();

        options.addOption(createOptionInputLocation());
        options.addOption(createOptionOutputLocation());
        options.addOption(createOptionDryRun());

        return options;
    }

    private Option createOptionOutputLocation() {
        final String desc = strings.getString("cli_cmd_output_location");
        return Option.builder(ARG_MVN_DIR)
                .longOpt(ARG_MVN_DIR_LONG)
                .required(true)
                .desc(desc)
                .build();
    }

    private Option createOptionInputLocation() {
        final String desc = strings.getString("cli_cmd_input_location");
        return Option.builder(ARG_GRADLE_CACHE)
                .longOpt(ARG_GRADLE_CACHE_LONG)
                .required(false)
                .desc(desc)
                .build();
    }

    private Option createOptionDryRun() {
        final String desc = strings.getString("cli_cmd_desc_dry_run");
        return Option.builder(ARG_DRY_RUN)
                .longOpt(ARG_DRY_RUN_LONG)
                .hasArg(false)
                .required(false)
                .desc(desc)
                .build();
    }
}
