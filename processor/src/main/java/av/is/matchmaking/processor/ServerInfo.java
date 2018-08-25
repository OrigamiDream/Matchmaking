package av.is.matchmaking.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ServerInfo implements Runnable {

    private final MatchRegistry matchRegistry;
    private final File directory;
    private final String command;

    private AtomicBoolean using = new AtomicBoolean(false);
    private ServerRunner processor = null;
    private String uniqueId;
    private String matchType;
    private String senderId;
    private String serverId;

    ServerInfo(MatchRegistry matchRegistry, File directory, String command) {
        this.matchRegistry = matchRegistry;
        this.directory = directory;
        this.command = command;
    }
    
    void clearDynamicData() {
        setUsing(false);
        setProcessor(null);
        setUniqueId(null);
        setMatchType(null);
        setSenderId(null);
    }

    ServerRunner getProcessor() {
        return processor;
    }

    private void setProcessor(ServerRunner processor) {
        this.processor = processor;
    }

    void setUsing(boolean using) {
        this.using.set(using);
    }

    public boolean isUsing() {
        return using.get();
    }
    
    public boolean isNotUsing() {
        return !isUsing();
    }

    public File getDirectory() {
        return directory;
    }

    public String getCommand() {
        return command;
    }
    
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
        System.out.println("Set UniqueId: " + uniqueId);
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
    
    public String getMatchType() {
        return matchType;
    }
    
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    public String getSenderId() {
        return senderId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public void performCommand(String command) {
        try {
            getProcessor().performCommand(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            List<File> remove = new ArrayList<>();
            for(File file : getDirectory().listFiles()) {
                if(file.getName().endsWith(".matchmaking-id")) {
                    remove.add(file);
                }
            }
            remove.forEach(File::delete);
            
            File file = new File(getDirectory(), getUniqueId() + ".matchmaking-id");
            file.createNewFile();
            
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", getCommand());
            builder.directory(new File(getDirectory(), "/"));

            Process process = builder.start();

            ServerRunner processor = new ServerRunner(matchRegistry, process, getDirectory());
            setProcessor(processor);
            processor.run();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
