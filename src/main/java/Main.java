import robot.Bot;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            
//            char[] alphanum = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
//            char[] symbols = {'!','@','$','^','&','*','(',')','_','+','=','-','[',']','{','}','\\','|',',','\"','.',',','/'};
//            bot.train(alphanum,"E:/Screenshots/alphanum.png");
//            bot.train(symbols,"E:/Screenshots/symbols.png");

            File file = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");
            if(file.delete())
            {
                System.out.println("File deleted successfully");
            }

            Scanner scan = new Scanner(System.in);
            System.out.println("Select day: ");
            bot.setJbDay(scan.nextInt());


            TimeUnit.SECONDS.sleep(3);
            int line = 0;
            String lastLine = null;
            
            while (true) {
                String processedLine = bot.processText(bot.getChat(line));
                if (processedLine.equals(lastLine)) {
                    line++;
                    continue;
                }
                
                if (processedLine.isEmpty()) {
                    line--;
                } else {
                    lastLine = processedLine;
                    line++;
                }
                
                if (line < 0)
                    line = 0;
                else if (line > 3)
                    line = 3;
                
                if (processedLine.isEmpty())
                    continue;
                
                System.out.println(line + ": " + processedLine);
                bot.act(processedLine);
            }

        } catch (IOException | AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
