package net.monsterdev.heimdallr;

import net.monsterdev.heimdallr.model.NetworkSlot;
import net.monsterdev.heimdallr.exceptions.CryptException;
import net.monsterdev.heimdallr.exceptions.XmlException;
import net.monsterdev.heimdallr.model.FilterRule;
import net.monsterdev.heimdallr.services.FilterTCPService;
import net.monsterdev.heimdallr.services.FilterUDPService;
import net.monsterdev.heimdallr.utils.ConfigReader;
import net.monsterdev.heimdallr.utils.CryptUtil;
import net.monsterdev.heimdallr.utils.FilterReader;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HeimdallrApplication {

    private ExecutorService executorService;
    /**
     * Список сетевых "слотов", на которые нужно повесить фильтры
     */
    List<NetworkSlot> inSlots;
    /**
     * Список сетевых "слотов" на которые нужно отправлять пакеты, прошедшие фильтрацию
     */
    List<NetworkSlot> outSlots;
    /**
     * Список правил, по которым нужно выполнять фильтрацию
     */
    List<FilterRule> filterRules;

    private HeimdallrApplication(List<NetworkSlot> inSlots, List<NetworkSlot> outSlots, List<FilterRule> filterRules) {
        this.inSlots = inSlots;
        this.outSlots = outSlots;
        this.filterRules = filterRules;
    }

    private void run() {
        executorService = Executors.newFixedThreadPool(inSlots.size());
        // Создание и запуск потоков для всех "слотов", по которым будет выполняться фильрация
        for (NetworkSlot slot : inSlots) {
            if (slot.getProtocol() == NetworkSlot.Protocol.TCP)
                executorService.submit(new FilterTCPService(slot));
            else if (slot.getProtocol() == NetworkSlot.Protocol.UDP)
                executorService.submit(new FilterUDPService(slot));
        }
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option cfgOption = new Option("cfg", true, "Path to config file");
        cfgOption.setRequired(true);
        options.addOption(cfgOption);

        Option filtersOption = new Option("filter", true, "Path to filter rules file");
        filtersOption.setRequired(true);
        options.addOption(filtersOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            help.printHelp("heimdallr -cfg <arg> -filter <arg>", options);
            System.exit(1);
            return;
        }

        String pathToCfg = cmd.getOptionValue("cfg");
        String pathToFilter = cmd.getOptionValue("filter");

        try {
            ConfigReader configReader = new ConfigReader(new String(Files.readAllBytes(Paths.get(pathToCfg)), "UTF-8"));
            FilterReader filterReader = new FilterReader(CryptUtil.decrypt(new String(Files.readAllBytes(Paths.get(pathToFilter)), "UTF-8")));
            List<NetworkSlot> networkInSlots = configReader.getInNetworkSlots();
            List<NetworkSlot> networkOutSlots = configReader.getOutNetworkSlots();
            List<FilterRule> filterRules = filterReader.getFilterRules();

            HeimdallrApplication app = new HeimdallrApplication(networkInSlots, networkOutSlots, filterRules);
            app.run();
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
            System.exit(1);
        } catch (XmlException e) {
            System.out.println("Error while parsing xml: " + e.getMessage());
            System.exit(1);
        } catch (CryptException e) {
            System.out.println("File encrypt error: " + e.getMessage());
            System.exit(1);
        }
    }
}
