package av.is.matchmaking.processor;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerInfo implements Runnable {

    private final File directory;
    private final String runCommand;

    private AtomicBoolean using = new AtomicBoolean(false);
    private ServerProcessor processor = null;

    public ServerInfo(File directory, String runCommand) {
        this.directory = directory;
        this.runCommand = runCommand;
    }

    public ServerProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(ServerProcessor processor) {
        this.processor = processor;
    }

    public void setUsing(boolean using) {
        this.using.set(using);
    }

    public boolean isUsing() {
        return using.get();
    }

    public File getDirectory() {
        return directory;
    }

    public String getRunCommand() {
        return runCommand;
    }

    @Override
    public void run() {

    }
}
