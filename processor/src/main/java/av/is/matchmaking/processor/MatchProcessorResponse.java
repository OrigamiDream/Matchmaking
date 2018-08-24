package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.match.ProcessResult;

import java.util.List;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
final class MatchProcessorResponse implements CommandResponse {

    private static final Object LOCK = new Object();

    private final MatchRegistry registry;
    
    MatchProcessorResponse(MatchRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public void respond(Command command) {
        if(command instanceof MatchProcessRequest) {
            ServerInfo serverInfo;
            synchronized (LOCK) {
                List<ServerInfo> serverInfos = registry.getUsableServers(((MatchProcessRequest) command).getMatchType());
                if(serverInfos.size() == 0) {
                    serverInfo = null;
                } else {
                    serverInfo = serverInfos.get(0);
                    serverInfo.setUsing(true);
                }
            }

            MatchProcessRequest processor = (MatchProcessRequest) command;

            System.out.println("Requested a new server: " + processor.getMatchType() + " from '" + processor.getSenderId() + "'");
            if(serverInfo != null) {
                registry.getPool().addQueue(new ServerPool.QueueServer(serverInfo, processor.getMatchType(), processor.getSenderId()));
            } else {
                System.out.println("Not available servers for match: " + processor.getMatchType());
                Command resultCommand = new MatchProcessResult(null, null, null, -1, ProcessResult.NOT_AVAILABLE);
                resultCommand.setDestinations(processor.getSenderId());

                registry.getMatchmakingManager().publishRedis(resultCommand);
            }
        }
    }
}
