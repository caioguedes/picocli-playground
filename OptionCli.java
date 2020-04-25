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
@Command(description = "Top-level command, it do nothing, don't worry", name = "toplevel")
public class OptionCli implements Callable<Void> {

    @Option(
        names = {"-m", "--message"},
        defaultValue = "Hello",
        showDefaultValue = Visibility.ALWAYS,
        order = 1)
    private String message;

    @Option(
        names = {"-s", "--second-message"},
        defaultValue = "World",
        showDefaultValue = Visibility.ALWAYS,
        order = 0)
    private String secondMessage;

    public static void main(String[] args) {
        CommandLine cli = new CommandLine(new OptionCli());
        
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

    @Command(description = "This subcommand use the same options from top-level...", name = "same")
    public void sameCommand() {
        // jbang OptionCli.java same
        // print: Hello World

        // jbang OptionCli.java -m Hey -s Caio same
        // print: Hey Caio
        System.out.println(this.message + " " + this.secondMessage);
    }

    @Command(
        description = "This subcommand use the same options from top-level, but you can set options after subcommand.",
        name = "same2")
    public void same2Command(
        @Option(names = {"-m", "--message"}) String message,
        @Option(names = {"-s", "--second-message"}) String secondMessage
    ) {
        // option are optional, I want use default values from top-level
        this.message = message == null ? this.message : message;
        this.secondMessage = secondMessage == null ? this.secondMessage : secondMessage;

        // jbang OptionCli.java -m Hoi same2 -m Hey
        // print: Hey World
        // jbang OptionCli.java -m Hoi -s Caio same2 -m Hey
        // print: Hey Caio
        System.out.println(this.message + " " + this.secondMessage);
    }

    @Command(
        description = "Same, but now with a option with same name, but diferetent meaning...",
        name = "same3")
    public void same3Command(
        @Option(
            names = {"-m", "--message"}) String message,
        @Option(
            names = {"-s", "--second-message"}) String secondMessage, 
        @Option(
            names = {"-ss", "--other-message"}) String otherMessage
    ) {
        // if you change --other-message to -s
        // BOOM, THIS NOT WORK, OF COURSE!
        System.out.println(message);
        System.out.println(secondMessage);
        System.out.println(otherMessage);
    }
}