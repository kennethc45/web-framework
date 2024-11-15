# web-framework
Building a Java web-framework from scratch

# Testing 
For testing, if the test file is changed or intialized for the first time, make sure to compile it before running.

For Server file
 - Compile `javac -d out -cp "lib/*" src/Server.java tests/Server_Integration_Test.java`
 - Run `java -cp out tests.Server_Integration_Test`

For Request file
 - Compile `javac -cp "lib/*" -d out src/Request.java tests/Request_Test.java`
 - Run `java -ea -cp "out:lib/*" tests.Request_Test`
 

If the test command does not work, then the compiled files might be outdated
 - Run this to remove out folder `rm -rf out/`
 - Recompile files you want to test and then run test commands

