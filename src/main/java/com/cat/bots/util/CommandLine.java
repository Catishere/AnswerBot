package com.cat.bots.util;

import java.util.HashMap;

public class CommandLine {
    private HashMap<String, String> arguments = new HashMap<>();
    
    public CommandLine() {
        initDefault();
    }
    
    public CommandLine(String[] args) {
        
        initDefault();
        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "--nickname":
                case "-n":
                    arguments.replace("nickname", args[i +1 ]);
                    break;
                case "--config":
                case "-c":
                    arguments.replace("config", args[i + 1]);
                    break;
                case "--output":
                case "-o":
                    arguments.replace("output", args[i + 1]);
                    break;
                case "--test":
                case "-t":
                    arguments.replace("test", "true");
                    break;
                case "--test-input":
                case "-I":
                    arguments.replace("test-input", args[i + 1]);
                    break;
                case "--test-output":
                case "-O":
                    arguments.replace("test-output", args[i + 1]);
                    break;
            }
        }
    }
    
    private void initDefault() {
        arguments.put("nickname", "Cat ");
        arguments.put("config", "training.txt");
        arguments.put("test", "false");
        arguments.put("test-input", "testimage.png");
        arguments.put("test-output", "testoutput.txt");
        arguments.put("output", "D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");
    }
    
    public String getArgument(String name) {
        return arguments.get(name);
    }
    
    public boolean hasArgument(String name) {
        return arguments.containsKey(name);
    }
}
