package com.cat.bots.robot;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {

    @Test
    void processText() throws IOException, AWTException {
        BufferedImage chat = ImageIO.read(new File("asdasd.png"));
        Bot bot = new Bot();
        assertEquals(bot.processText(chat), "[SIMON] diviq zapad ;]: az li trqq natiskam");
    }

    @Test
    void getFromGoogle() throws IOException, AWTException {
        Bot bot = new Bot();
        assertEquals(bot.getFromGoogle("Kolko e visok Niagarskiyat vodopad"), "51");
    }
}