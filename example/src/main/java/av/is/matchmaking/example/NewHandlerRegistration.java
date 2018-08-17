package av.is.matchmaking.example;

import av.is.matchmaking.Matchmaking;
import av.is.matchmaking.api.MatchmakingManager;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class NewHandlerRegistration {
    
    public static void main(String args[]) {
        sendRedis();
    }
    
    public static void sendRedis() {
        System.out.println("new-matchmaking-id");
        Matchmaking newMatchmaking = Matchmaking.getInstance();
        MatchmakingManager newManager = newMatchmaking.getManager("new-matchmaking-id");
        newManager.registerRedis("myHandlerKey", NewHandler.class);
        
        System.out.println("other-matchmaking-id");
        Matchmaking otherMatchmaking = Matchmaking.getInstance();
        MatchmakingManager otherManager = otherMatchmaking.getManager("other-matchmaking-id");
        otherManager.registerRedis("myHandlerKey", NewHandler.class);
    
        new Thread(() -> {
            try {
                Thread.sleep(200L);
                NewHandler newHandler = new NewHandler("my handler name", "my message");
                newHandler.setDestinations("other-matchmaking-id");
                newManager.publishRedis(newHandler);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
