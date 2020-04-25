//DEPS info.picocli:picocli:3.9.6

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.RunAll;
import picocli.CommandLine.Option;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Help.Visibility;

/*
██████  ██  ██████  ██████   ██████ ██      ██     ██████     ██   ██ 
██   ██ ██ ██      ██    ██ ██      ██      ██          ██     ██ ██  
██████  ██ ██      ██    ██ ██      ██      ██      █████       ███   
██      ██ ██      ██    ██ ██      ██      ██          ██     ██ ██  
██      ██  ██████  ██████   ██████ ███████ ██     ██████  ██ ██   ██ 
*/
@Command(description = "Top-level command, it do nothing, don't worry", name = "toplevel", mixinStandardHelpOptions = false)
public class MixinCli implements Callable<Void> {

    @Mixin(name = "messages") ReusableOptions messages;

    public static void main(String[] args) {
        CommandLine cli = new CommandLine(new MixinCli());
        
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
    public void sameCommand(@Mixin(name = "messages") ReusableOptions messages) {
        // jbang MixinCli.java same -m Hei -s Caio
        // print: Hei Caio
        System.out.println(messages.message + " " + messages.secondMessage);
    }

    public static class ReusableOptions {
        @Option(
            names = {"-m", "--message"},
            defaultValue = "Hello",
            showDefaultValue = Visibility.ALWAYS,
            order = 1)
        public String message;
    
        @Option(
            names = {"-s", "--second-message"},
            defaultValue = "World",
            showDefaultValue = Visibility.ALWAYS,
            order = 0)
        public String secondMessage;
    }
}