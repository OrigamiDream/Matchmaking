package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.match.MatchProcessRequest;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchProcessorResponse implements CommandResponse {
    
    private final MatchRegistry registry;
    
    public MatchProcessorResponse(MatchRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public void respond(Command command) {
        if(command instanceof MatchProcessRequest) {
            Thread thread = new Thread(new MatchmakingThread((MatchProcessRequest) command, registry));
            thread.setDaemon(true);
            thread.start();
        }
    }
}
