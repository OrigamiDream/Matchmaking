package av.is.matchmaking.processor;

public class Utils {
    
    private Utils() {
    }
    
    @FunctionalInterface
    public static interface DelegatedRunnable {
        
        void run() throws Exception;
        
    }
    
    public static Runnable rethrowRunnable(DelegatedRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch(Exception e) {
                e.printStackTrace();
            }
        };
    }
}
