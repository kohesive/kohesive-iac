## Building

### Prerequisites

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) Installed, `JAVA_HOME` set to the install directory, with `JAVA_HOME/bin` added to the system path.
* [Install IKVM](http://www.frijters.net/ikvmsrc-8.1.5717.0.zip), with its install directory added to the system path. 

### Building on Linux / Mac OSX:

```
./gradlew build -x test
```

### Building on Windows:

```
gradlew.bat build -x test
```

## Making .NET artifacts

You will need to isntall IKVM (I am using `ikvm-8.1.5717.0`) to convert the Java JAR files into .NET DLL

### To make the .NET DLL:

on Linux / Mac OSX:

```
mono /Users/jminard/.gradle/ikvm/ikvm-8.1.5717.0/bin/ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/kohesive-iac-aws-crawl.dll -version:0.1.0 -removeassertions -compressresources -target:library /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

on Windows:  (paths may need to change, this is just an example)

```
ikvmc.exe -out:aws-data-collector\build\libs\kohesive-iac-aws-crawl.dll -version:0.1.0 -removeassertions -compressresources -target:library aws-data-collector\build\libs\aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

### To make the console test program (Java version):


on Linux / Mac OSX:

```
mono /Users/jminard/.gradle/ikvm/ikvm-8.1.5717.0/bin/ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector.exe -version:0.1.0 -removeassertions -compressresources -target:exe -main:uy.kohesive.iac.util.aws.collector.Ec2CrawlKt /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

on Windows:  (paths may need to change, this is just an example)

```
ikvmc.exe -out:aws-data-collector\build\libs\aws-data-collector.exe -version:0.1.0 -removeassertions -compressresources -target:exe -main:uy.kohesive.iac.util.aws.collector.Ec2CrawlKt aws-data-collector\build\libs\aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

### Running
  
To use the DLL or EXE you need to have the IKVM runtime DLL's added to the project first as
a dependency, or from the command-line available in the assembly cache or current directory.

You can add IKVM to a .NET projet via NuGET:  https://www.nuget.org/packages/IKVM/

Or manually use the all of the DLL's from the IKVM install.
