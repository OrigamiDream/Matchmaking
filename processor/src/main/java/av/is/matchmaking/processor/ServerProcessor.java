package av.is.matchmaking.processor;

public class ServerProcessor {

    private final Process process;

    public ServerProcessor(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }
}
