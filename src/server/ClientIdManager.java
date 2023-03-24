/**
 * 
 */
package a2.src.server;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class ClientIdManager {
    private static final Set<String> usedClientIds = new HashSet<>();

    /**
     * 
     * @param clientId
     * @return
     */
    public synchronized boolean addClient(String clientId) {
        if (clientId == null || usedClientIds.contains(clientId)) {
            return false;
        }
        usedClientIds.add(clientId);
        return true;
    }

    /**
     * 
     * @param clientId
     */
    public synchronized void removeClient(String clientId) {
        if (clientId != null && usedClientIds.contains(clientId)) {
            usedClientIds.remove(clientId);
        }
    }

    /**
     * 
     * @param clientId
     * @return
     */
    public synchronized boolean isClientConnected(String clientId) {
        return clientId != null && usedClientIds.contains(clientId);
    }

    /**
     * 
     * @param clientId
     */
    public synchronized void disconnectClient(String clientId) {
        if (clientId != null && usedClientIds.contains(clientId)) {
            removeClient(clientId);
        }
    }


}
