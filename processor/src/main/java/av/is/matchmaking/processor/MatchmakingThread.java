package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.match.ProcessResult;

import java.util.UUID;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchmakingThread implements Runnable {

    private final MatchProcessRequest processor;
    private final MatchRegistry registry;
    private final ServerInfo serverInfo;

    MatchmakingThread(MatchProcessRequest processor, MatchRegistry registry, ServerInfo serverInfo) {
        this.processor = processor;
        this.registry = registry;
        this.serverInfo = serverInfo;
    }

    @Override
    public void run() {
        System.out.println("Requested a new server: " + processor.getMatchType() + " from '" + processor.getSenderId() + "'");
        if(serverInfo == null) {
            System.out.println("Not available servers for match: " + processor.getMatchType());
            Command command = new MatchProcessResult(null, null, null, -1, ProcessResult.NOT_AVAILABLE);
            command.setDestinations(processor.getSenderId());
            
            registry.getMatchmakingManager().publishRedis(command);
        } else {
            serverInfo.setUniqueId(UUID.randomUUID().toString());
            serverInfo.setMatchType(processor.getMatchType());
            serverInfo.setSenderId(processor.getSenderId());
            serverInfo.run();
        }
    }
}
