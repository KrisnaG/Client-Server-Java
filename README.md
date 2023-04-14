# Java Client-Server Application

This is a Java client-server application for sending messages over a network. It consists of a server program and a client program that communicate with each other using sockets.

---

## Requirements

    Java JDK 8 or higher
    Bash shell

---

## Usage

### ***Starting the Server***

1. Navigate to the root directory of the project (a2).
2. Run the following command to start the server:

```bash
    ./startServer.sh <port_number>
```

>Replace <port_number> with the port number you want to use for the server.

### ***Starting the Client***

1. Navigate to the root directory of the project (a2).
2. Run the following command to start the client:

```bash
    ./startClient.sh <host_name> <port_number>
```

>Replace <host_name> with the name of the host where the server is running, and <port_number> with the port number used by the server.

### ***Cleaning the compiled files***

1. Navigate to the root directory of the project (a2).
2. Run the following command to clean all compiled files in the temp directory:

```bash
    ./clean.sh
```

---

## Constraints

### Timeout
If an error occurs from the server or a message is not received by the client within 10 seconds, the client connection will timeout. 
This means the client would disconnect from the server.