
Building on Linux / Mac OSX:

```
./gradlew build -x test
```

Building on Windows:

```
gradlew.bat build -x test
```

Then to turn into a .NET DLL:

on Linux / Mac OSX:

```
mono /Users/jminard/.gradle/ikvm/ikvm-8.1.5717.0/bin/ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/kohesive-iac-aws-crawl.dll -version:0.1.0 -removeassertions -compressresources -target:library /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

on Windows:

```
ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/kohesive-iac-aws-crawl.dll -version:0.1.0 -removeassertions -compressresources -target:library /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

To make the console test program (Java version):


on Linux / Mac OSX:

```
mono /Users/jminard/.gradle/ikvm/ikvm-8.1.5717.0/bin/ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector.exe -version:0.1.0 -removeassertions -compressresources -target:exe -main:uy.kohesive.iac.util.aws.Ec2CrawlKt /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```

on Windows:

```
ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector.exe -version:0.1.0 -removeassertions -compressresources -target:exe -main:uy.kohesive.iac.util.aws.Ec2CrawlKt /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
```


# make DLL
                                                                                                                                                                                                                                                                                                                                    
                                                                                                                                                                                                                                                                                                                                    # make EXE
                                                                                                                                                                                                                                                                                                                                    mono /Users/jminard/.gradle/ikvm/ikvm-8.1.5717.0/bin/ikvmc.exe -out:/Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector.exe -version:0.1.0 -removeassertions -compressresources -target:exe -main:uy.kohesive.iac.util.aws.Ec2CrawlKt /Users/jminard/DEV/OSS/repos/kohesive-iac/aws-data-collector/build/libs/aws-data-collector-all-0.1.0-SNAPSHOT.jar
                                                                                                                                                                                                                                                                                                                                    
                                                                                                                                                                                                                                                                                                                                    # make DLL
                                                                                                                                                                                                                                                                                                                                    mono /Users/jminard/.gradle/ik