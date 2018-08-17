package av.is.matchmaking.processor;

import av.is.matchmaking.Matchmaking;
import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchmakingProcessor {
    
    public MatchmakingProcessor() {
        Matchmaking matchmaking = Matchmaking.getInstance();
    
        MatchmakingManager manager = matchmaking.getManager("processor");
        MatchRegistry registry = new MatchRegistry(manager);
    
        manager.registerRedis(MatchProcessRequest.MATCH_PROCESS_REQUEST_KEY, MatchProcessRequest.class, new MatchProcessorResponse(registry));
        manager.registerRedis(MatchProcessResult.MATCH_PROCESS_RESPONSE_KEY, MatchProcessResult.class, null);
        
        Thread thread = new Thread("Infinite server keeper") {
            @Override
            public void run() {
                while(true);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    
    public static void main(String args[]) {
        new MatchmakingProcessor();
    }
}