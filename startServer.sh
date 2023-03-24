#!/bin/bash

# Navigate to the directory containing the script
cd "$(dirname "$0")"

# Compile the Java class
javac -cp ./temp -d temp src/utility/Constants.java src/utility/Validation.java 
javac -cp ./temp -d temp src/utility/Command.java src/server/Server.java

# Execute the Server program with the given port number
java -cp ./temp a2.src.server.Server $1

# Clean up and remove compiled files
rm -rf ./temp