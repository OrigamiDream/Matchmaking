package av.is.matchmaking.example;

import av.is.matchmaking.Matchmaking;
import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class ProcessHandlerRegistration {
    
    public static void main(String args[]) {
        Matchmaking matchmaking = Matchmaking.getInstance();
        MatchmakingManager manager = matchmaking.getManager("matchmaking");
        
        manager.registerRedis(MatchProcessRequest.MATCH_PROCESS_REQUEST_KEY, MatchProcessRequest.class, null);
        manager.registerRedis(MatchProcessResult.MATCH_PROCESS_RESPONSE_KEY, MatchProcessResult.class, new ProcessHandlerResponse());
    
        System.out.println("Sending new Skywars server request");
        
        for(int i = 0; i < 3; i++) {
            Command command = new MatchProcessRequest(manager.getMatchmakingId(), "Skywars");
            command.setDestinations("processor");
            manager.publishRedis(command);
        }
    }
}
