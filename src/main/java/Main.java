import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import util.Bot;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            Tesseract tesseract = new Tesseract();
            String dayString;
            TimeUnit.SECONDS.sleep(2);
            do {
                dayString = tesseract.doOCR(bot.getDay()).trim();
            } while (!dayString.matches("[1-9][0-9]*"));
        
            bot.setJbDay(Integer.parseInt(dayString));

            
//            char[] alphanum = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
//            char[] symbols = {'!','@','$','^','&','*','(',')','_','+','=','-','[',']','{','}','\\','|',',','\"','.',',','/'};
//            bot.train(alphanum,"E:/Screenshots/alphanum.png");
//            bot.train(symbols,"E:/Screenshots/symbols.png");

            File file = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");

            if(file.delete())
            {
                System.out.println("File deleted successfully");
            }

            TimeUnit.SECONDS.sleep(3);
            int line = 5;
            String lastLine = null;
            
            while (true) {
                String processedLine = bot.processText(bot.getChat(line));
                
                if (processedLine.equals(lastLine))
                    continue;
                
                if (processedLine.isEmpty()) {
                    line--;
                    continue;
                } else {
                    lastLine = processedLine;
                    line++;
                }
                
                bot.act(processedLine);
            }

//            ImageIO.read(new File("testpng1.png"));
//            System.out.println(bot.processText(ImageIO.read(new File("testpng1.png"))));
        } catch (IOException | AWTException | InterruptedException | TesseractException e) {
            e.printStackTrace();
        }
    }
}
