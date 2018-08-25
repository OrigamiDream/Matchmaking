package av.is.matchmaking.processor.command;

public class CommandContext {

    private final String[] arguments;

    public CommandContext(String[] arguments) {
        this.arguments = arguments;
    }

    public String getString(int index) throws CommandException {
        if(index < 0) {
            throw new CommandException("Index cannot be lower than zero.");
        }
        if(index >= arguments.length) {
            throw new CommandException("Index cannot be higher than argument size!");
        }
        return arguments[index];
    }

    public int getInt(int index) throws CommandException {
        try {
            return Integer.parseInt(getString(index));
        } catch (NumberFormatException e) {
            throw new CommandFormatException("Index '" + index + "' context of '" + getString(index) + "' is not parsable as Integer.");
        }
    }

    public double getDouble(int index) throws CommandException {
        try {
            return Double.parseDouble(getString(index));
        } catch (NumberFormatException e) {
            throw new CommandFormatException("Index '" + index + "' context of '" + getString(index) + "' is not parsable as Double.");
        }
    }

    public String getJoinedString(int beginIndex) throws CommandException {
        if(beginIndex >= arguments.length) {
            throw new CommandException("Index cannot be higher than argument size!");
        }
        StringBuilder builder = new StringBuilder();
        for(int i = beginIndex; i < arguments.length; i++) {
            builder.append(arguments[i]);
            if(i != arguments.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public int argsLength() {
        return arguments.length;
    }
}
