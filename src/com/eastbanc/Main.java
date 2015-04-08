package com.eastbanc;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        Option option = new Option("f", "file", true, "File with numbers");
        option.setArgs(1);
        option.setOptionalArg(true);
        option.setArgName("path to file");
        options.addOption(option);
        option = new Option("b", "benchmark", false, "Benchmark");
        options.addOption(option);
        option = new Option("g", "generate", true, "Generate numbers");
        option.setArgs(2);
        option.setOptionalArg(true);
        option.setArgName("path to file> <numbers amount");
        options.addOption(option);
        CommandLine commandLine = null;
        try {
            commandLine = new PosixParser().parse(options, args);
        } catch (ParseException e) {
            return;
        }

        if(!commandLine.hasOption("f") && !commandLine.hasOption("g")) {
            HelpFormatter formater = new HelpFormatter();
            formater.printHelp(" ", options);
            return;
        }

        if(commandLine.hasOption("f") && commandLine.getOptionValue("f") == null) {
            HelpFormatter formater = new HelpFormatter();
            formater.printHelp(" ", options);
            return;
        }

        if(commandLine.hasOption("g") && (commandLine.getOptionValues("g") == null || commandLine.getOptionValues("g").length < 2)) {
            HelpFormatter formater = new HelpFormatter();
            formater.printHelp(" ", options);
            return;
        }


        Searcher searcher = new Searcher();
        if(commandLine.hasOption("g")) {
            try {
                String[] gValues = commandLine.getOptionValues("g");
                searcher.generate(gValues[0], new BigInteger(gValues[1]));
            } catch (FileNotFoundException e) {
                System.out.println("Error creating file");
                return;
            } catch (NumberFormatException e) {
                HelpFormatter formater = new HelpFormatter();
                formater.printHelp(" ", options);
                return;
            }
        }

        long time = System.currentTimeMillis();

        if(commandLine.hasOption("f")) {
            Integer number = null;
            try {
                number = searcher.find(commandLine.getOptionValue("f"), 3);
            } catch (Exception e) {
                if(e instanceof FileNotFoundException) {
                    System.out.println("File not found");
                } else if(e instanceof IllegalArgumentException) {
                    System.out.println("File contains not only numbers and spaces");
                } else {
                    System.out.println("Error");
                }
                return;
            }
            if(number == null) {
                System.out.println("no result");
            } else {
                System.out.println(number);
            }
        }

        if(commandLine.hasOption("b")) {
            System.out.print("Execution time: " + (System.currentTimeMillis() - time) + "millis");
        }
    }
}
