package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public interface TypeCommandResponse<C extends Command> extends CommandResponse {
    
    @Override
    default void respond(Command command) {
        typeRespond((C) command);
    }
    
    void typeRespond(C command);
}
