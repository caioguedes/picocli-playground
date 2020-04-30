//DEPS info.picocli:picocli:3.9.6

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.RunAll;
import picocli.CommandLine.Option;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.ITypeConverter;

@Command(description = "Multi Valued Options", name = "main")
public class ConverterOptionCli implements Callable<Void> {
    
    @Option(
        names = {"-t", "--targets"},
        converter = Targets.Converter.class,
        paramLabel = "<cluster-name>@<hostname>:<port>[,<hostname>:<port>]")
    private Targets targets;

    public static class Targets {
        private String clusterName;
        private List<String> addresses;

        public static class Converter implements ITypeConverter<Targets> {
            public Targets convert(String value) throws Exception {
                Matcher matcher = Pattern.compile("(.*)@(.*)").matcher(value);
                if (!matcher.matches()) throw new Exception("Ops!");
                Targets targets = new Targets();
                targets.clusterName = matcher.group(1);
                targets.addresses = List.of(matcher.group(2).split(","));
                return targets;
            }
        }
    } 

    public static void main(String[] args) {
        CommandLine cli = new CommandLine(new ConverterOptionCli());
        
        if (args.length == 0) {
            cli.usage(System.out);
            System.exit(0);;
        }

        cli.parseWithHandler(new RunAll(), args);
    }

    public Void call() {
        System.out.println("Cluster Name: " + targets.clusterName);
        System.out.println("Addresses: " + targets.addresses);
        return null;
    }
}