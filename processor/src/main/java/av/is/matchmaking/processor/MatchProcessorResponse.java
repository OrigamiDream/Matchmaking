package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.match.MatchProcessRequest;

import java.util.List;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
class MatchProcessorResponse implements CommandResponse {
    
    private final MatchRegistry registry;
    
    MatchProcessorResponse(MatchRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public void respond(Command command) {
        if(command instanceof MatchProcessRequest) {
            ServerInfo serverInfo;
            List<ServerInfo> serverInfos = registry.getUsableServers(((MatchProcessRequest) command).getMatchType());
            if(serverInfos.size() == 0) {
                serverInfo = null;
            } else {
                serverInfo = serverInfos.get(0);
                serverInfo.setUsing(true);
            }
            Thread thread = new Thread(new MatchmakingThread((MatchProcessRequest) command, registry, serverInfo));
            thread.setDaemon(true);
            thread.start();
        }
    }
}
