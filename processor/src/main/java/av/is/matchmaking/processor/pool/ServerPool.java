package av.is.matchmaking.processor.pool;

import av.is.matchmaking.processor.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ServerPool implements Runnable {

    private static final Object LOCK = new Object();

    public static class QueueServer {
        private final ServerInfo serverInfo;
        private final String matchType;
        private final String senderId;

        public QueueServer(ServerInfo serverInfo, String matchType, String senderId) {
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

    public void addQueue(QueueServer server) {
        synchronized (LOCK) {
            queuedServers.add(server);
            System.out.println("Queue added. size: " + queuedServers.size());
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
