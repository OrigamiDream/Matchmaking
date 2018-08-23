package av.is.matchmaking.example;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.match.MatchProcessResult;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class ProcessHandlerResponse implements CommandResponse {
    
    @Override
    public void respond(Command command) {
        if(command instanceof MatchProcessResult) {
            MatchProcessResult result = (MatchProcessResult) command;
            
            switch(result.getResult()) {
                case SUCCESS:
                    System.out.println("Result: SUCCESS");
                    System.out.println("MatchID: " + result.getMatchId());
                    System.out.println("ServerID: " + result.getServerId());
                    System.out.println("Address: " + result.getAddress());
                    System.out.println("Port: " + result.getPort());
                    break;
                    
                case FAILURE:
                    System.out.println("Result: FAILURE");
                    break;
    
                case NOT_AVAILABLE:
                    System.out.println("Result: NOT_AVAILABLE");
                    break;
            }
        }
    }
}
