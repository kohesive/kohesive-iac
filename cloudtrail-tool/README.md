# Kohesive CloudTrail Tool

This tool generates Java (Kotlin) AWS API calls from Amazon CloudTrail event logs. It may either fetch the events using the CloudTrail API or use local events directory (e.g., downloaded from S3) as its source.

The range of events to codegen can be defined by their IDs and/or timestamp. If events filter is not defined, all the events from the specified source would be processed.
  
## Building
  
1. Check out the `kohesive-iac` project sources
2. Run the distribution generation task `./gradlew :cloudtrail-tool:distZip`
3. The distribution zip should now be available at `kohesive-iac/cloudtrail-tool/build/distributions`

## Running

1. Unzip the distribution zip
2. Run either `./cloudtrail-tool` or `cloudtrail-tool.bat`, specifying the command and its parameters:

`cloud-trail-tool <command> [<args>]`

## Commands

### list-events

This command prints a list of events with applied ID and/or timestamp filters if specified. Here's a list of available options:

```
--endId <endId>
    Event ID range end

--endTime <endTime>
    Event date range end

--events-dir <eventsDir>
    Events directory path

--local
    Local filesystem mode

--startId <startId>
    Event ID range start

--startTime <startTime>
    Event date range start
```

Use this command as a preview of events available within a range specified. 

### codegen

This command generates Kotlin code that executes the AWS API calls to reproduce the actions which lead to CloudTrail events, having those events as an input.
 
It uses the same arguments as `list-events` to define the events range and source (API or local filesystem) plus code generation parameters:
 
```
--outputDir <outputDir>
    Output base directory path

--packageName <packageName>
    Generated classes package name
``` 