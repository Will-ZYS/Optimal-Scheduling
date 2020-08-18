# SOFTENG 306 Project 1

## Project Setup in IDE
1. Clone the repository
    ```
    https://github.com/SoftEng306-2020/project-1-the-lucky-1.git         
    ```
2. Import the project as Gradle project in IntelliJ

## Building the Project
To build an executable jar of the project, run the following on the command line:
```
./gradlew build
```
The generated jar file will be placed under the `build/libs` directory

## Running the Project
To run the executable jar file, run
```
java -jar <jarfilename.jar> [OPTIONS...]
```
The detailed usage of the application is the following:
```
Usage: java -jar scheduler.jar INPUT.dot P [OPTIONS]
INPUT.dot (A task graph with integer weights in dot format, need to be placed in the same directory as the jar file or the project root)
P (The number of processors to schedule the INPUT graph on

Optional:
-p N (Use N core for execution in parallel, default is sequential, not implemented for Milestone 1)
-v (Visualise the search, not implemented yet)
-o OUTPUT (Output file is named OUTPUT, default is INPUT-output.dot)
```

The project can also be run in Gradle, just run
```
./gradlew run --args='OPTIONS...'
```
in the project root folder

Or, run the build.gradle script in IntelliJ with the arguments:
```
run --args='OPTIONS...'
```
