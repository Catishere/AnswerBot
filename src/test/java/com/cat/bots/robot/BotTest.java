package com.cat.bots.robot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    
    private static Bot bot;
    
    @BeforeAll
    public static void initializeBot() throws AWTException {
        bot = new Bot();
    }

    @Test
    void processText() throws IOException, AWTException {
        BufferedImage chat = ImageIO.read(new File("testimage.png"));
        assertEquals(bot.processText(chat), "[SIMON] diviq zapad ;]: az li trqq natiskam");
    }

    @Test
    void getFromGoogle() throws IOException, AWTException {
        assertEquals(bot.getFromGoogle("Kolko e visok Niagarskiyat vodopad", true), "51");
    }


    @Test
    void translateQuestion() throws AWTException, IOException {
        assertEquals(bot.getFromGoogle("Prezidenta na Qponiq", true), "Shinzo Abe");
    }
}