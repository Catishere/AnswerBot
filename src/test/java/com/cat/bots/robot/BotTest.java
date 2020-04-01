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
        assertEquals("[SIMON] diviq zapad ;]: az li trqq natiskam", bot.processText(chat));
    }

    @Test
    void getFromGoogle() throws IOException, AWTException {
        assertEquals("51", bot.getFromGoogle("Kolko e visok Niagarskiyat vodopad", true));
    }


    @Test
    void translateQuestion() throws AWTException, IOException {
        assertEquals("Shinzo Abe", bot.getFromGoogle("Prezidenta na Qponiq", true));
        assertEquals("dog in japanese", bot.translateQuestion("kuche+na+qponski"));
    }

    @Test
    void getAnswer() throws IOException {
        assertEquals("kiev", bot.getAnswer("koq e stolicata na ukraina"));
        assertEquals("-1", bot.getAnswer("vremeto navun"));
        assertEquals("Wednesday", bot.getAnswer("Koi den ot sedmicata e dnes"));
        assertEquals("4", bot.getFromGoogle("log10 10000", false).trim());
        assertEquals("Bulgaria", bot.getAnswer("Koi zavurshi na chetvurto mqsto na svetovnoto purvenstvo po futbol prez 1994 godina"));
    }
}