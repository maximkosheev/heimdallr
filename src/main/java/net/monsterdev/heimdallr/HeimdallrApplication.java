package net.monsterdev.heimdallr;

import net.monsterdev.heimdallr.exceptions.CryptException;
import net.monsterdev.heimdallr.utils.ConfigReader;
import net.monsterdev.heimdallr.utils.CryptUtil;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class HeimdallrApplication {

    public static void main(String[] args) {
        Options options = new Options();

        Option cfgOption = new Option("cfg", true, "Path to config file");
        cfgOption.setRequired(true);
        options.addOption(cfgOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            help.printHelp("heimdallr -cfg <arg>", options);
            System.exit(1);
            return;
        }

        String pathToCfg = cmd.getOptionValue("cfg");

        try {
            String encryptedConfig = new String(Files.readAllBytes(Paths.get(pathToCfg)), "UTF-8");
            String config = CryptUtil.decrypt(encryptedConfig);
            ConfigReader cr = new ConfigReader();
            cr.read(config);
        } catch (IOException e) {
            System.out.println("Config file not found.");
            System.exit(1);
        } catch (CryptException e) {
            System.out.println("Config file encrypt error");
            System.exit(1);
        }
        SpringApplication.run(HeimdallrApplication.class, args);
    }
}
