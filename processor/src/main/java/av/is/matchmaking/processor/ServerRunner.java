package av.is.matchmaking.processor;

import av.is.matchmaking.match.MatchIdentifiers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicBoolean;

final class ServerRunner implements Runnable {

    private final MatchRegistry matchRegistry;
    private final String expectedServerName;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final BufferedWriter writer;
    private final BufferedReader reader;

    ServerRunner(MatchRegistry matchRegistry, Process process, File directory) {
        this.matchRegistry = matchRegistry;
        
        MatchIdentifiers identifiers = new MatchIdentifiers(directory, new File(directory, "matchmaking.properties"), false);
        try {
            identifiers.synchronizeServerProperties(new File(directory, "server.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.expectedServerName = identifiers.getServerName();
        this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
    
    void setRunning(boolean running) {
        this.running.set(running);
    }
    
    @Override
    public void run() {
        try {
            while(running.get()) {
                String line;
                if((line = reader.readLine()) != null) {
                    if(matchRegistry.isViewable(expectedServerName)) {
                        System.out.println("[" + expectedServerName + "]: " + line);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    void performCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }
}
