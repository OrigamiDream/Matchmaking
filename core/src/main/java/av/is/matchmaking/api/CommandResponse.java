package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface CommandResponse extends CommandValidation, CommandDestination {
    
    void respond(Command command);
    
    @Override
    default boolean validate(String destination) {
        if(getDestinations().length == 0) {
            return true;
        }
    
        for(String cur : getDestinations()) {
            if(cur.equalsIgnoreCase(destination)) {
                return true;
            }
        }
        return false;
    }
}