package av.is.matchmaking.api;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface MergedCommandStorage extends CommandStorage {
    
    @Override
    default <C extends Command> Class<C> getCommand() {
        return mergedCommand();
    }
    
    @Override
    default <C extends CommandResponse> C getCommandResponse() {
        try {
            ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
            Constructor objDef = Object.class.getDeclaredConstructor();
            Constructor initConstructor = factory.newConstructorForSerialization(mergedCommand(), objDef);
            
            return (C) mergedCommand().cast(initConstructor.newInstance());
        } catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Failed to serialize command response with no constructor");
    }
    
    /**
     *
     * Put class which implements both {@link Command} and {@link CommandResponse}.
     *
     * @param <C> This implements {@link Command} and {@link CommandResponse}.
     * @return Class
     */
    <C extends Command & CommandResponse> Class<C> mergedCommand();
}
