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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    
    private static Bot bot;
    
    @BeforeAll
    public static void initializeBot() throws AWTException {
        CommandLine cmd = new CommandLine();
        bot = Bot.getInstance(cmd);
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
        LocalDate ld = LocalDate.now();
        assertEquals("kiev", bot.getAnswer("koq e stolicata na ukraina"));
        assertEquals("4", bot.getAnswer("vremeto navun"));
        assertEquals(ld.getDayOfWeek().toString().toLowerCase(), bot.getAnswer("Koi den ot sedmicata e dnes").toLowerCase());
        assertEquals("4", bot.getFromGoogle("log10 10000", false).trim());
        assertEquals("Santa Cruz de Tenerife", bot.getAnswer("koq e stolicata na tenerife"));
        assertEquals("bulgaria", bot.getAnswer("Koi zavurshi na chetvurto mqsto na svetovnoto purvenstvo po futbol prez 1994 godina"));
    }
    
    
}