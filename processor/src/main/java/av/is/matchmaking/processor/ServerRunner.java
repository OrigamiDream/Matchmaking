package av.is.matchmaking.processor;

import av.is.matchmaking.match.MatchIdentifiers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

final class ServerRunner implements Runnable {

    private final AtomicBoolean running = new AtomicBoolean(true);

    private final BufferedWriter writer;
    private final BufferedReader reader;

    ServerRunner(Process process, File directory) {
        new MatchIdentifiers(directory, new File(directory, "matchmaking.properties"), false);

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
