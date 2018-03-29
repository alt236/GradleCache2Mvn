## GradleCache2Mvn

Will convert a gradle cache directory structure to the equivelent Maven one.

### Usage

    Convert a Gradle cache directory structure to a Maven repository one
    
     -d,--dryrun         Dry run. No writing will take place.
     -i,--input <arg>    Gradle cache directory. If not provided, it will
                         default to '~/.gradle/caches/modules-2/files-2.1/'
     -o,--output <arg>   Directory where the Maven artifacts will be written
                         to.
     -v,--verbose        Show more information in console.
    

### Example Usage

	$ gradlecache2mvn-X.X.X.jar --input ~/.gradle/caches/modules-2/files-2.1/ --output ~/gradle2mvn/
	$ gradlecache2mvn-X.X.X.jar -o ~/gradle2mvn/

### Binaries
Binaries can be found here: [https://github.com/alt236/GradleCache2Mvn/releases]()

### Notes
- This will not create POM files if the gradle cache has none. Essentially, if the cached gradle dependency was not a Maven one, it will not be converted.
- If the artifact has slashes `"/"` in its groupId or artifactId, the conversion will fail. This is because this app is using the file path to extract artifacts from the Gradle cache.
- Use the `--dryrun` switch to make sure this works the way you expect it to.
- Not sure how well this works on Windows :)

### Changelog
- 1.0.0: First release
- 2.0.0: We now Detect duplicate files and skip exporting if found. Changed output to make it less verbose. This will break any parsers build for v1.0.0.

### Build Instructions
Linux/Mac:

	mvn clean package && chmod +x target/gradlecache2mvn-X.X.X.jar


### Links
* Github: [https://github.com/alt236/GradleCache2Mvn]()

### Credits
Author: [Alexandros Schillings](https://github.com/alt236).

The code in this project is licensed under the [Apache Software License 2.0](LICENSE-2.0.html).

Copyright (c) 2017 Alexandros Schillings.
