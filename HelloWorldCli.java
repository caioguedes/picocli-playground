//DEPS info.picocli:picocli:3.9.6

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.RunAll;
import picocli.CommandLine.Option;
import picocli.CommandLine.Help.Visibility;

/*
██████  ██  ██████  ██████   ██████ ██      ██     ██████     ██   ██ 
██   ██ ██ ██      ██    ██ ██      ██      ██          ██     ██ ██  
██████  ██ ██      ██    ██ ██      ██      ██      █████       ███   
██      ██ ██      ██    ██ ██      ██      ██          ██     ██ ██  
██      ██  ██████  ██████   ██████ ███████ ██     ██████  ██ ██   ██ 
*/
@Command(description = "Prints a nice and beautilful inspiring message.", name = "message")
public class HelloWorldCli implements Callable<Void> {

    @Option(names = {"-m", "--message"}, defaultValue = "Hello", showDefaultValue = Visibility.ALWAYS)
    private String message;
    public static void main(String[] args) {
        CommandLine cli = new CommandLine(new HelloWorldCli());
        
        if (args.length == 0) {
            cli.usage(System.out);
            System.exit(0);;
        }

        cli.parseWithHandler(new RunAll(), args);
    }

    @Override
    public Void call() {
        return null;
    }

    @Command(description = "Prints a nice and beautilful inspiring message.", name = "say")
    public void sayCommand(
        @Option(names = {"-m", "--message"}) String message
    ) {

        // merge
        System.out.println(this.message + message + " I'm a subcommand!");
    }
}