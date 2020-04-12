package com.cat.bots.util;

import com.cat.bots.robot.Bot;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AutoSpammer extends Thread {
    public void run(){
        Bot bot;
        try {
            bot = Bot.getInstance();
            System.out.println("Begin crying");
            while (bot.isCrying()) {
                bot.cryInChat();
                TimeUnit.SECONDS.sleep(1);
                bot.reloadSpamButton();
                TimeUnit.SECONDS.sleep(1);
                bot.fireSpamButton();
                TimeUnit.SECONDS.sleep(4);
            }
            System.out.println("End crying");
        } catch (AWTException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
