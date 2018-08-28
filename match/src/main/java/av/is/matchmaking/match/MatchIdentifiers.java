package av.is.matchmaking.match;

import java.io.*;
import java.util.Optional;
import java.util.Properties;

public final class MatchIdentifiers {
    
    private String uniqueId;
    
    private String matchType;
    private String address;
    private String port;
    private String serverName;
    
    public MatchIdentifiers(File folder, File idFile) {
        this(folder, idFile, true);
    }
    
    public MatchIdentifiers(File folder, File idFile, boolean deleteFile) {
        try {
            File delete = null;
            for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])) {
                if(file.getName().endsWith(".matchmaking-id")) {
                    uniqueId = file.getName().substring(0, file.getName().indexOf("."));
                    delete = file;
                }
            }
            if(delete != null && delete.exists() && deleteFile) {
                delete.delete();
            }
        
            System.out.println("Identified UUID: " + uniqueId);
        
            Properties properties = new Properties();
            properties.load(new FileInputStream(idFile));
        
            matchType = properties.getProperty("matchType");
            address = properties.getProperty("address");
            port = properties.getProperty("port");
            serverName = properties.getProperty("serverName");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void initializeProperties(File dataFolder, String fileName) {
        try(FileOutputStream outputStream = new FileOutputStream(new File(dataFolder, fileName).toString())) {
            Properties properties = new Properties();
            
            properties.setProperty("matchType", "Unknown");
            properties.setProperty("address", "127.0.0.1");
            properties.setProperty("port", "25565");
            properties.setProperty("serverName", "TEST_01");
    
            properties.store(outputStream, "Auto-generated default matchmaking properties");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void synchronizeServerProperties(File serverFile) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(serverFile));

            properties.setProperty("server-ip", getAddress());
            properties.setProperty("server-port", getPort());

            properties.store(new FileOutputStream(serverFile), "Synchronized by matchmaking");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public String getMatchType() {
        return matchType;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getPort() {
        return port;
    }
    
    public String getServerName() {
        return serverName;
    }
}
