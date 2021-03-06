package av.is.matchmaking.processor;

import av.is.matchmaking.Matchmaking;
import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.match.MatchProcessLoader;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.processor.command.CommandException;
import av.is.matchmaking.processor.command.CommandRegistry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static av.is.matchmaking.processor.Utils.rethrowRunnable;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public final class MatchmakingProcessor {
    
    public static void main(String args[]) throws IOException {
        Matchmaking matchmaking = Matchmaking.getInstance();
    
        MatchmakingManager manager = matchmaking.getManager("processor");
        MatchRegistry registry = new MatchRegistry(manager);
    
        manager.registerRedis(MatchProcessRequest.MATCH_PROCESS_REQUEST_KEY, MatchProcessRequest.class, new MatchProcessorResponse(registry));
        manager.registerRedis(MatchProcessResult.MATCH_PROCESS_RESPONSE_KEY, MatchProcessResult.class, null);
        manager.registerRedis(MatchProcessLoader.MATCH_PROCESS_LOADER_KEY, MatchProcessLoader.class, new MatchProcessorLoaderResponse(registry));
    
        File root = new File(".");
        
        File servers = new File(root, "matchmakings.json");
        if(!servers.exists()) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(servers))) {
                writer.write("[]");
                writer.flush();
            } catch(IOException e) {
                e.printStackTrace();
            }
            System.out.println("Created a new matchmakings json");
        }
        try {
            JSONArray array = (JSONArray) new JSONParser().parse(new FileReader(servers));
            
            System.out.println("Ready to run " + array.size() + " matchmaking servers");
    
            for(int i = 0; i < array.size(); i++) {
                JSONObject object = (JSONObject) array.get(i);
        
                registry.addServer((String) object.get("matchType"),
                                   new File((String) object.get("directory")),
                                   (String) object.get("command"));
            }
    
            if(array.size() == 0) {
                System.out.println("How to configure:");
                System.out.println("[");
                System.out.println("    {");
                System.out.println("        \"matchType\": \"Your match type\",");
                System.out.println("        \"directory\": \"your/server/directory\",");
                System.out.println("        \"command\": \"sh launcher.sh\",");
                System.out.println("    },");
                System.out.println("    {");
                System.out.println("        \"matchType\": \"Your match type\",");
                System.out.println("        \"directory\": \"your/server/directory\",");
                System.out.println("        \"command\": \"sh launcher.sh\",");
                System.out.println("    }");
                System.out.println("]");
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }

        Thread console = new Thread(new ConsoleThread(registry));
        console.setName("Matchmaking Console");
        console.setDaemon(true);
        console.start();
        
        Thread adapter = new Thread(rethrowRunnable(() -> {
            CommandRegistry commandRegistry = registry.getCommandRegistry();
            commandRegistry.dispatch(commandRegistry.getCommand("perform"), new String[] { "-a", "stop" });
            System.out.println("Processor will be shutdown as soon as possible...");
            while(registry.getUsableServers().size() > 0 && registry.getUsableServers().stream().anyMatch(ServerInfo::isAvailable)) {
                Thread.sleep(50L);
            }
            System.out.println("All servers are disabled.");
            System.out.println("Shutdown in 5 seconds...");
            Thread.sleep(5000L);
        }));
        adapter.setName("Matchmaking ShutdownHook");
        adapter.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(adapter);
    }
}