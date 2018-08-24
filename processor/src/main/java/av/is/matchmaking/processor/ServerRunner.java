package av.is.matchmaking.processor;

import av.is.matchmaking.match.MatchIdentifiers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

class ServerRunner implements Runnable {

    private final Process process;
    private final MatchIdentifiers identifiers;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final BufferedWriter writer;
    private final BufferedReader reader;

    ServerRunner(Process process, File directory) {
        this.process = process;
        this.identifiers = new MatchIdentifiers(directory, new File(directory, "matchmaking.properties"), false);

        this.writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
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
                String line;
                if((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void performCommand(String command) throws IOException {
        writer.write(command);
        writer.flush();
    }
}
