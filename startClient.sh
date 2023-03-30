#!/bin/bash

# Navigate to the directory containing the script
cd "$(dirname "$0")"

# Compile the Java classes
javac -d temp src/utility/Validation.java
javac -d temp src/utility/Commands.java
javac -d temp src/client/ClientConstants.java
javac -cp ./temp -d temp src/client/Client.java

# Execute the Client program with the specified host name and port number
java -cp ./temp a2.src.client.Client $1 $2

# Clean up and remove compiled files
# rm -rf ./temp