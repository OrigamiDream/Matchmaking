package av.is.matchmaking.loader;

import av.is.matchmaking.Matchmaking;
import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.match.MatchIdentifiers;
import av.is.matchmaking.match.MatchProcessLoader;
import av.is.matchmaking.match.MatchSwitch;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

import static av.is.matchmaking.match.MatchProcessLoader.MATCH_PROCESS_LOADER_KEY;

public class MatchmakingLoader extends JavaPlugin {
    
    private static final String PROPERTIES_FILE = "matchmaking.properties";
    
    private MatchmakingManager matchmakingManager;
    private MatchIdentifiers identifiers;
    
    @Override
    public void onEnable() {
        File serverFolder = new File(".");
        
        // After server initialized.
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            File file = new File(serverFolder, PROPERTIES_FILE);
            if(!file.exists()) {
                System.out.println("No matchmaking properties found. Generating a new dummy matchmaking configuration...");
                MatchIdentifiers.initializeProperties(serverFolder, PROPERTIES_FILE);
            }
            identifiers = new MatchIdentifiers(new File(serverFolder, "/"), new File(serverFolder, PROPERTIES_FILE));
    
            matchmakingManager = Matchmaking.getInstance().getManager(identifiers.getServerName());
            matchmakingManager.registerRedis(MATCH_PROCESS_LOADER_KEY, MatchProcessLoader.class, null);
    
            System.out.println("Publishing server enabled.");
            if(identifiers.getUniqueId() != null) {
                Command command = new MatchProcessLoader(identifiers.getUniqueId(), identifiers.getMatchType(), identifiers.getServerName(), identifiers.getAddress(), Integer.parseInt(identifiers.getPort()), MatchSwitch.ENABLE);
                command.setDestinations("processor");
                matchmakingManager.publishRedis(command);
            }
        }, 20L);
    }
    
    @Override
    public void onDisable() {
        if(matchmakingManager != null && identifiers != null) {
            System.out.println("Publishing server disabled.");
            if(identifiers.getUniqueId() != null) {
                Command command = new MatchProcessLoader(identifiers.getUniqueId(), identifiers.getMatchType(), identifiers.getServerName(), identifiers.getAddress(), Integer.parseInt(identifiers.getPort()), MatchSwitch.DISABLE);
                command.setDestinations("processor");
                matchmakingManager.publishRedis(command);
            }
        }
    }
}
