package com.cat.bots.robot;

import com.cat.bots.util.CommandLine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BotSingleTest {
    
    private static Bot bot;
    
    @BeforeAll
    public static void initializeBot() throws AWTException {
        CommandLine cmd = new CommandLine();
        bot = new Bot(cmd);
    }
    
    @Test
    void getAnswer() throws IOException {
        assertEquals("蝙蝠吃病毒死亡", bot.getAnswer("\"Koito prilep qde ot virus umira\" na kitaiski"));
    }
    
    
}