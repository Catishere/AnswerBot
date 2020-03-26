package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Bot {
    private static Robot robot_instance;
    private HashMap<String, String> countries = new HashMap<>();
    private int jbDay;
    
    public Bot() throws AWTException {
        robot_instance = new Robot();
        countries.put("Bulgariq", "Sofiq");
        countries.put("Bulgaria", "Sofia");
        countries.put("Turciq", "Ankara");
        countries.put("Ispaniq", "Madrid");
        countries.put("Franciq", "Parish");
    }

    public int getJbDay() {
        return jbDay;
    }

    public void setJbDay(int jbDay) {
        System.out.println(jbDay);
        this.jbDay = jbDay;
    }
    
    private int processVerticalLine(int x, StringBuilder sb, BufferedImage chat) {
        int seriesIncrement = 1;
        boolean isLastHit = Pallette.hasColor(new Color(chat.getRGB(x, 0)));
        for (int y = 1; y < chat.getHeight(); y++) {
            Color color = new Color(chat.getRGB(x, y));
            boolean isHit = Pallette.hasColor(color);

            if (isLastHit == isHit)
                seriesIncrement++;
            else {
                sb.append(seriesIncrement);
                sb.append((isLastHit)? 'x': 'n');
                seriesIncrement = 1;
            }
            isLastHit = isHit;
        }
        return seriesIncrement;
    }
    
    public BufferedImage getDay() { 
        return robot_instance.createScreenCapture(new Rectangle(1264, 108, 20, 20));
    }

    public String processText(BufferedImage chat) throws IOException {
        File file = new File("training.txt");
        Scanner sc = new Scanner(file);

        StringBuilder sb = new StringBuilder();
        StringBuilder output = new StringBuilder();

        for (int x = 0; x < chat.getWidth(); x++) {
            int seriesIncrement = processVerticalLine(x, sb, chat);
            if ((x+1) % 10 == 0) {
                while (sc.hasNextLine()) {
                    if (x == 9 && sb.toString().equals("18n18n18n18n18n18n18n18n18n"))
                        return "";
                    String line = sc.nextLine();
                    if (line.startsWith(sb.toString())) {
                        output.append(line.charAt(line.length() - 1));
                        break;
                    }
                }

                sc = new Scanner(file);
                sb.setLength(0);
            } else {
                sb.append(seriesIncrement);
                sb.append('n');
            }
        }
        sc.close();
        return output.toString();
    }
    
    public BufferedImage getChat(int line) {
        if (line < 0)
            line = 0;
        else if (line > 5)
            line = 5;

        return robot_instance.createScreenCapture(new Rectangle(10, 899 + line*21, 800, 18));
    }
    
    private String getAnswer(String line) {
        String question = line.substring(line.lastIndexOf(':') + 1).replace("?","");
        question = question.replace("koren ot", "sqrt");
        if (question.matches("[0-9x*^/+\\- =sqrt]+")) 
            return Integer.toString((int) MathParser.eval(question.replace('x', '*')));
        else if (question.startsWith("Stolicata na "))
            return countries.get(question.substring(13));
        else if (question.startsWith("tochen") || question.startsWith("to4en"))
            return line.substring(8, line.indexOf("Zadade"));
        else {
            int jbIndex = question.indexOf("jb");
            int jbIndex2 = question.indexOf("jail");
            if (jbIndex >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex + 2)));
            else if (jbIndex2 >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex + 4)));
            else
                return "eh ne go znam twa";
        }
    }
    
    public void act(String line) throws FileNotFoundException {
        if (line.startsWith("[Quest]"))
        {
            File file = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");
            PrintWriter pw = new PrintWriter(file);
            pw.print("say " + getAnswer(line) + "; alias quest;");
            pw.close();
            System.out.println("Received Answer");
        }
    }

    public void train(char[] alphabet, String filepath) throws IOException {
        BufferedImage chat = ImageIO.read(new File(filepath));
        PrintWriter writer = new PrintWriter(filepath + ".txt", "UTF-8");
        StringBuilder sb = new StringBuilder();
        int charactersCount = 0;

        for (int x = 0; x < chat.getWidth(); x++) {
            int seriesIncrement = processVerticalLine(x, sb, chat);
            if ((x+1) % 10 == 0) {
                sb.append(" - ").append(alphabet[charactersCount]);
                charactersCount++;
                sb.append('\n');
            } else {
                sb.append(seriesIncrement);
                sb.append('n');
            }
        }

        writer.println(sb);
        System.out.println(charactersCount);
        writer.close();
    }
}
