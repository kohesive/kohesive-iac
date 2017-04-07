## Building

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

The DLL list is something like:

```
IKVM.OpenJDK.Beans.dll
IKVM.OpenJDK.Charsets.dll
IKVM.OpenJDK.Cldrdata.dll
IKVM.OpenJDK.Core.dll
IKVM.OpenJDK.Jdbc.dll
IKVM.OpenJDK.Localedata.dll
IKVM.OpenJDK.Management.dll
IKVM.OpenJDK.Media.dll
IKVM.OpenJDK.Misc.dll
IKVM.OpenJDK.Naming.dll
IKVM.OpenJDK.Security.dll
IKVM.OpenJDK.Text.dll
IKVM.OpenJDK.Tools.dll
IKVM.OpenJDK.Util.dll
IKVM.OpenJDK.XML.API.dll
IKVM.OpenJDK.XML.Bind.dll
IKVM.OpenJDK.XML.Crypto.dll
IKVM.OpenJDK.XML.Parse.dll
IKVM.OpenJDK.XML.Transform.dll
IKVM.OpenJDK.XML.WebServices.dll
IKVM.OpenJDK.XML.XPath.dll
IKVM.Reflection.dll
IKVM.Runtime.JNI.dll
IKVM.Runtime.dll
ikvm-native-win32-x64.dll
ikvm-native-win32-x86.dll
```
             
You can include all the DLL's from the IKVM distribution to be safe.                                                                                             
