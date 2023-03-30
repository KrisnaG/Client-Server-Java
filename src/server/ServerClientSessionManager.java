/**
 * 
 */
package a2.src.server;

import java.util.Map;
import java.util.HashMap;

/**
 * 
 */
public class ServerClientSessionManager {
    private static final Map<String, Map<String, String>> clientSession = new HashMap<>();

    /**
     * 
     * @param clientId
     * @return
     */
    public synchronized boolean addClient(String clientId) {
        if (clientId == null || clientSession.containsKey(clientId)) {
            return false;
        }
        clientSession.put(clientId, new HashMap<>());
        return true;
    }

    /**
     * 
     * @param clientId
     */
    public synchronized void disconnectClient(String clientId) {
        if (clientId != null && clientSession.containsKey(clientId)) {
            clientSession.remove(clientId);
        }
    }

    /**
     * 
     * @param clientId
     * @param key
     * @return
     */
    public String getClientData(String clientId, String key) {
        return clientSession.get(clientId).get(key);
    }

    /**
     * 
     * @param clientId
     * @param key
     * @param value
     */
    public void putClientData(String clientId, String key, String value) {
        clientSession.get(clientId).put(key, value);
    }
}
