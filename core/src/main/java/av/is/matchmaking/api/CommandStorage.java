package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface CommandStorage {
    
    <C extends Command> Class<C> getCommand();
    
    <C extends CommandResponse> C getCommandResponse();
    
}
