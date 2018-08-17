package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
interface CommandValidation {
    
    /**
     *
     * Validate Redis destination when responding.
     *
     * @param destination
     * @return
     */
    boolean validate(String destination);
    
}
