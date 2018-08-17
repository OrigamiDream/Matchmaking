package av.is.matchmaking.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
interface CommandDestination {
    
    void setDestinations(String... destinations);
    
    String[] getDestinations();
    
    default List<String> getDestinationList() {
        return new ArrayList<>(Arrays.asList(getDestinations()));
    }
}
