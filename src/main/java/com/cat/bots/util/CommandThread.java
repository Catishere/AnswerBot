package com.cat.bots.util;

import com.cat.bots.robot.Bot;

import java.util.Scanner;

public class CommandThread extends Thread {
    private Bot bot;
    
    public void run(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Commandline input thread is running...");
        while (sc.hasNextLine())
            bot.executeCommand(sc.nextLine());
    }
    
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
