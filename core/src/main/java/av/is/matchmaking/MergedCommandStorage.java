package av.is.matchmaking;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface MergedCommandStorage extends CommandStorage {
    
    @Override
    default <C extends Command> Class<C> getCommand() {
        return (Class<C>) mergedCommand().getClass();
    }
    
    @Override
    default <C extends CommandResponse> C getCommandResponse() {
        return (C) mergedCommand();
    }
    
    <C extends Command & CommandResponse> C mergedCommand();
}
