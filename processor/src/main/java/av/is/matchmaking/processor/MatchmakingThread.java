package av.is.matchmaking.processor;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.match.MatchProcessRequest;
import av.is.matchmaking.match.MatchProcessResult;
import av.is.matchmaking.match.ProcessResult;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchmakingThread implements Runnable {
    
    private final MatchProcessRequest processor;
    private final MatchRegistry registry;
    
    public MatchmakingThread(MatchProcessRequest processor, MatchRegistry registry) {
        this.processor = processor;
        this.registry = registry;
    }
    
    @Override
    public void run() {
        System.out.println("Launch a new matchmaking...");
        System.out.println("Process Data:");
        System.out.println("Match Type: " + processor.getMatchType());
        System.out.println("Requested From: " + processor.getSenderId());
    
        try {
            Thread.sleep(2000L);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    
        System.out.println("Done");
        Command command = new MatchProcessResult(null, null, null, -1, ProcessResult.FAILURE);
        command.setDestinations(processor.getSenderId());
        registry.getMatchmakingManager().publishRedis(command);
    }
}
