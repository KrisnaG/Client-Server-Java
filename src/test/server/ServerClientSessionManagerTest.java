package a2.src.test.server;

import a2.src.main.server.ServerClientSessionManager;

public class ServerClientSessionManagerTest {
    public static void main(String[] args) {
        addClientTest();
        disconnectClientTest();
        getClientDataTest();
        putClientDataTest();
        deleteClientDataTest();
    }

    private static void addClientTest() {
        ServerClientSessionManager sessionManager = new ServerClientSessionManager();
        String clientId = "client1";
        if (sessionManager.addClient(clientId)) {
            System.out.println("addClientTest passed");
        } else {
            System.out.println("addClientTest failed");
        }
    }

    private static void disconnectClientTest() {
        ServerClientSessionManager sessionManager = new ServerClientSessionManager();
        String clientId = "client1";
        sessionManager.addClient(clientId);
        sessionManager.disconnectClient(clientId);
        if (sessionManager.getClientData(clientId, "key") == null) {
            System.out.println("disconnectClientTest passed");
        } else {
            System.out.println("disconnectClientTest failed");
        }
    }

    private static void getClientDataTest() {
        ServerClientSessionManager sessionManager = new ServerClientSessionManager();
        String clientId = "client1";
        String key = "key";
        String value = "value";
        sessionManager.addClient(clientId);
        sessionManager.putClientData(clientId, key, value);
        String retrievedValue = sessionManager.getClientData(clientId, key);
        if (retrievedValue != null && retrievedValue.equals(value)) {
            System.out.println("getClientDataTest passed");
        } else {
            System.out.println("getClientDataTest failed");
        }
    }

    private static void putClientDataTest() {
        ServerClientSessionManager sessionManager = new ServerClientSessionManager();
        String clientId = "client1";
        String key = "key";
        String value = "value";
        sessionManager.addClient(clientId);
        if (sessionManager.putClientData(clientId, key, value)) {
            System.out.println("putClientDataTest passed");
        } else {
            System.out.println("putClientDataTest failed");
        }
    }

    private static void deleteClientDataTest() {
        ServerClientSessionManager sessionManager = new ServerClientSessionManager();
        String clientId = "client1";
        String key = "key";
        String value = "value";
        sessionManager.addClient(clientId);
        sessionManager.putClientData(clientId, key, value);
        if (sessionManager.deleteClientData(clientId, key)) {
            System.out.println("deleteClientDataTest passed");
        } else {
            System.out.println("deleteClientDataTest failed");
        }
    }
}