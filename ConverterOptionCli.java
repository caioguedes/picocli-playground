//DEPS info.picocli:picocli:3.9.6

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
        paramLabel = "<cluster-name@<hostname>:<port>[,<hostname>:<port>]")
    private Targets targets;

    public static class Targets {
        private String clusterName;
        private List<String> addresses;

        @Override
        public String toString() {
            return "[clusterName = " + clusterName + "; addresses = " + addresses + "]";
        }

        public static class Converter implements ITypeConverter<Targets> {
            public Targets convert(String value) throws Exception {
                Targets targets = new Targets();
                String[] values = value.split("@");
                targets.clusterName = values[0];
                targets.addresses = List.of(values[1].split(","));
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