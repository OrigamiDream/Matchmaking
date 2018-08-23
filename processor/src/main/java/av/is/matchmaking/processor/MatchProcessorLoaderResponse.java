package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.match.MatchProcessLoader;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.match.ProcessResult;

import java.util.List;
import java.util.Optional;

public class MatchProcessorLoaderResponse implements CommandResponse {
    
    private final MatchRegistry matchRegistry;
    
    MatchProcessorLoaderResponse(MatchRegistry matchRegistry) {
        this.matchRegistry = matchRegistry;
    }
    
    @Override
    public void respond(Command command) {
        if(command instanceof MatchProcessLoader) {
            MatchProcessLoader loader = (MatchProcessLoader) command;
    
            List<ServerInfo> servers = matchRegistry.getServers(loader.getMatchType());
            Optional<ServerInfo> nullable = servers.stream().filter(server -> server.getUniqueId().equals(loader.getUniqueId())).findFirst();
            
            if(nullable.isPresent()) {
                ServerInfo serverInfo = nullable.get();
                switch(loader.getMatchSwitch()) {
                    case ENABLE:
                        serverInfo.getProcessor().setRunning(true);
            
                        Command result = new MatchProcessResult(serverInfo.getUniqueId(),
                                                                loader.getServerId(),
                                                                loader.getAddress(),
                                                                loader.getPort(),
                                                                ProcessResult.SUCCESS);
                        result.setDestinations(serverInfo.getSenderId());
                        matchRegistry.getMatchmakingManager().publishRedis(result);
                        break;
        
                    case DISABLE:
                        serverInfo.getProcessor().setRunning(false);
                        serverInfo.clearDynamicData();
                        break;
                }
            } else {
                System.out.println("Failed to find loaded ServerInfo which has UUID: " + loader.getUniqueId());
            }
        }
    }
}
