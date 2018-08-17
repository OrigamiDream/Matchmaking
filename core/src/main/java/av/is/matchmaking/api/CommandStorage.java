package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface CommandStorage {
    
    /**
     *
     * Get a type of command.
     *
     * @param <C>
     * @return
     */
    <C extends Command> Class<C> getCommand();
    
    /**
     *
     * An instance to respond.
     *
     * @param <C>
     * @return
     */
    <C extends CommandResponse> C getCommandResponse();
    
}
