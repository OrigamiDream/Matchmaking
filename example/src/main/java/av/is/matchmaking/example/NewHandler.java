package av.is.matchmaking.example;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class NewHandler extends Command implements CommandResponse {
    
    private final String myHandlerName;
    private final String myMessage;
    
    public NewHandler(String myHandlerName, String myMessage) {
        this.myHandlerName = myHandlerName;
        this.myMessage = myMessage;
    }
    
    public String getMyHandlerName() {
        return myHandlerName;
    }
    
    public String getMyMessage() {
        return myMessage;
    }
    
    @Override
    public void respond(Command command) {
        if(command instanceof NewHandler) {
            NewHandler newHandler = (NewHandler) command;
            System.out.println(newHandler.getMyHandlerName());
            System.out.println(newHandler.getMyMessage());
        }
    }
}
