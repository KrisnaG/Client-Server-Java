#!/bin/bash

# Navigate to the directory containing the script
cd "$(dirname "$0")"

# Compile the Java classes
javac -d temp src/utility/Validation.java
javac -d temp src/utility/Commands.java
javac -d temp src/server/ServerConstants.java
javac -d temp src/server/ServerClientSessionManager.java
javac -cp ./temp -d temp src/server/Server.java src/server/ServerClientHandler.java

# Execute the Server program with the given port number
java -cp ./temp a2.src.server.Server $1

# Clean up and remove compiled files
# rm -rf ./temp