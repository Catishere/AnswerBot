package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Bot {
    private static Robot robot_instance;

    public Bot() throws AWTException {
        robot_instance = new Robot();
    }

    public void click(Point mouse) throws InterruptedException {
        robot_instance.mouseMove(mouse.x, mouse.y);
        robot_instance.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        TimeUnit.MILLISECONDS.sleep(50);
        robot_instance.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void pressEscape() throws InterruptedException {
        robot_instance.keyPress(KeyEvent.VK_ESCAPE);
        TimeUnit.MILLISECONDS.sleep(50);
        robot_instance.keyRelease(KeyEvent.VK_ESCAPE);
    }


    public void move(Point mouse) {
        robot_instance.mouseMove(mouse.x, mouse.y);
    }

    public void awaitColorChange(Point colorPos, Color color, boolean fromColor) throws AWTException, InterruptedException {
        while (true) {
            Color realColor = getColor(colorPos);
            if (fromColor ^ color.equals(realColor)) return;
            TimeUnit.SECONDS.sleep(5);
        }
    }

    public void antiAFK() throws InterruptedException {
        robot_instance.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        TimeUnit.MILLISECONDS.sleep(50);
        robot_instance.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void clickUntilColorChange(Point mouse, Point colorPos, Color color, int delay, boolean fromColor) throws AWTException, InterruptedException {
        while (true) {
            Color realColor = getColor(colorPos);
            if (fromColor ^ color.equals(realColor)) return;
            click(mouse);
            TimeUnit.SECONDS.sleep(delay);
        }
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

    public Color getColor(Point pixel) throws AWTException {
        Rectangle screenRect = new Rectangle(pixel.x, pixel.y, pixel.x, pixel.y + 1);
        BufferedImage capture = robot_instance.createScreenCapture(screenRect);
        return new Color(capture.getRGB(0,0), false);
    }

    public BufferedImage getChat(int line) {
        if (line < 0)
            line = 0;
        else if (line > 5)
            line = 5;

        return robot_instance.createScreenCapture(new Rectangle(10, 899 + line*21, 800, 18));
    }
    
    private String getAnswer(String question) {
        if (question.matches("[0-9x*^/+\\- =]+")) {
            return Integer.toString((int) MathParser.eval(question));
        }
        else if (question.startsWith("Stolicata na "))
            return "Sofia";
        else
            return "eh ne go znam twar";
    }
    
    public void act(String line) throws FileNotFoundException {
        if (line.startsWith("[Quest]"))
        {
            File file = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");
            PrintWriter pw = new PrintWriter(file);
            pw.print("say " + getAnswer(line.substring(line.lastIndexOf(':') + 1)) + "; alias quest;");
            pw.close();
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
