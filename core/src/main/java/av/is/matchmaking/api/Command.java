package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public abstract class Command implements DefaultCommandResponse, CommandValidation, CommandDestination {
    
    private String[] destinations;
    
    public Command(String... destinations) {
        this.destinations = destinations;
    }
    
    @Override
    public void setDestinations(String... destinations) {
        this.destinations = destinations;
    }
    
    @Override
    public String[] getDestinations() {
        if(destinations == null) {
            destinations = new String[0];
        }
        return destinations;
    }
    
    @Override
    public boolean validate(String destination) {
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