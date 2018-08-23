package av.is.matchmaking.processor;

import av.is.matchmaking.match.MatchIdentifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

class ServerRunner implements Runnable {

    private final Process process;
    private final MatchIdentifiers identifiers;
    private final AtomicBoolean running = new AtomicBoolean(true);

    ServerRunner(Process process, File directory) {
        this.process = process;
        this.identifiers = new MatchIdentifiers(directory, new File(directory, "matchmaking.properties"), false);
    }
    
    MatchIdentifiers getIdentifiers() {
        return identifiers;
    }
    
    void setRunning(boolean running) {
        this.running.set(running);
    }
    
    @Override
    public void run() {
        try {
            while(running.get()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                String line;
                if((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
