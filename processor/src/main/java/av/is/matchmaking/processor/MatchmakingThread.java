package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.match.ProcessResult;

import java.util.List;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchmakingThread implements Runnable {

    private final MatchProcessRequest processor;
    private final MatchRegistry registry;

    MatchmakingThread(MatchProcessRequest processor, MatchRegistry registry) {
        this.processor = processor;
        this.registry = registry;
    }

    @Override
    public void run() {
        System.out.println("Requested a new server: " + processor.getMatchType() + " from " + processor.getSenderId());
        List<ServerInfo> available = registry.getUsableServers(processor.getMatchType());
        if(available.size() == 0) {
            System.out.println("Not available servers for match: " + processor.getMatchType());
            respond(null, null, null, -1, null, ProcessResult.NO_AVAILABLE);
        } else {
            ServerInfo serverInfo = available.get(0);
            serverInfo.setUsing(true);
            serverInfo.run();
        }
    }

    private void respond(String matchId, String serverId, String address, int port, String senderId, ProcessResult result) {
        Command command = new MatchProcessResult(matchId, serverId, address, port, result);
        command.setDestinations(senderId);
        registry.getMatchmakingManager().publishRedis(command);
    }
}
