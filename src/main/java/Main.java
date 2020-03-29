import util.Bot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            Scanner scan = new Scanner(System.in);
            bot.setJbDay(scan.nextInt());

            
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

        } catch (IOException | AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
