package av.is.matchmaking;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public abstract class Command implements DefaultCommandResponse {
    
    private String[] destinations;
    
    public Command(String[] destinations) {
        this.destinations = destinations;
    }
    
    public void setDestinations(String... destinations) {
        this.destinations = destinations;
    }
    
    public String[] getDestinations() {
        if(destinations == null) {
            destinations = new String[0];
        }
        return destinations;
    }
    
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
