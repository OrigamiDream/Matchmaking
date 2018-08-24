package av.is.matchmaking.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ServerPool implements Runnable {

    private static final Object LOCK = new Object();

    static class QueueServer {
        private final ServerInfo serverInfo;
        private final String matchType;
        private final String senderId;

        QueueServer(ServerInfo serverInfo, String matchType, String senderId) {
            this.serverInfo = serverInfo;
            this.matchType = matchType;
            this.senderId = senderId;
        }

        ServerInfo getServerInfo() {
            return serverInfo;
        }

        String getMatchType() {
            return matchType;
        }

        String getSenderId() {
            return senderId;
        }
    }

    void addQueue(QueueServer server) {
        synchronized (LOCK) {
            queuedServers.add(server);
        }
    }

    private List<QueueServer> queuedServers = new ArrayList<>();

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (LOCK) {
                if(queuedServers.size() > 0) {
                    QueueServer queueServer = queuedServers.remove(0);

                    new Thread(() -> {
                        queueServer.getServerInfo().setUniqueId(UUID.randomUUID().toString());
                        queueServer.getServerInfo().setMatchType(queueServer.getMatchType());
                        queueServer.getServerInfo().setSenderId(queueServer.getSenderId());
                        queueServer.getServerInfo().run();
                    }).start();
                }
            }
        }
    }
}
