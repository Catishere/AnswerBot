package robot;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import util.Capitals;
import util.MathParser;
import util.Pallette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Bot {
    private static Robot robot_instance;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private Capitals capitals = new Capitals();
    private int jbDay;
    
    public Bot() throws AWTException {
        robot_instance = new Robot();
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
        return robot_instance.createScreenCapture(new Rectangle(10, 899 + line*21, 800, 18));
    }
    
    private String getAnswer(String line) throws IOException {
        String question = line.substring(line.lastIndexOf(':') + 1)
                .replace("?","")
                .replace("=","")
                .trim();
        question = question.replace("koren ot", "sqrt");
        int indexCapital;
        if (question.matches("[0-9x*^/+\\- =sqrt]+")) 
            return Integer.toString((int) MathParser.eval(question.replace('x', '*')));
        else if (question.contains(" li "))
            return "da; say ne";
        else if ((indexCapital = question.toUpperCase().indexOf("STOLICA")) >= 0) {

            if (indexCapital == 0) {
                String countryAnswer = capitals.getCountries().get(question.substring(13).toUpperCase());
                if (countryAnswer == null)
                    return getFromGoogle(question);
                else
                    return countryAnswer;
            }
            
            String capitalAnswer = capitals.getCapitals().get(question.substring(0, question.indexOf(' ')).toUpperCase());
            if (capitalAnswer == null)
                return getFromGoogle(question);
            else
                return capitalAnswer;
        }
        else if (question.toUpperCase().contains("SIMON"))
            return line.substring(8, line.indexOf("Zadade"));
        else {
            int jbIndex = question.indexOf("jb");
            int jbIndex2 = question.indexOf("jail");
            int jbIndex3 = question.indexOf("jailbreak");
            int jbIndex4 = question.indexOf("jail break");
            if (jbIndex >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex + 2)));
            else if (jbIndex2 >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex2 + 4)));
            else if (jbIndex3 >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex3 + 9)));
            else if (jbIndex4 >= 0)
                return Integer.toString((int) MathParser.eval(jbDay + question.substring(jbIndex4 + 10)));
            else if (question.contains(" cat") || question.contains(" Cat"))
                return "cat";
            else
                return getFromGoogle(question);
        }
    }

    public String getFromGoogle(String query) throws IOException {
        HttpGet request = new HttpGet("https://www.google.com/search?q=" + query
        .replace(' ', '+')
        .replace("c", "ts")
        .replace("j", "dzh") + "&hl=en");
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

        String answer;
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            int answerIndex;
            int answerCapitalIndex;
            if ((answerCapitalIndex = result.indexOf("class=\"FLP8od\"")) >= 0)
                answer = result
                        .substring(result.indexOf('>', answerCapitalIndex) + 1, result.indexOf('<', answerCapitalIndex));
            else if ((answerIndex = result.indexOf("class=\"Z0LcW\">")) >= 0)
                answer = result
                        .substring(answerIndex + 14, result.indexOf('<', answerIndex));
            else
                answer = "No answer";
            
            answer = answer.replace('\u00A0', ' ').replace(",","");
            if (answer.matches("^[0-9].+"))
                return answer.substring(0, answer.indexOf(' '));
            else
                return answer.replace(",","");
        }
    }

    public void act(String line) throws IOException {
        if (line.startsWith("[Quest]"))
        {
            File file = new File("D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\quest.cfg");
            PrintWriter pw = new PrintWriter(file);
            String answer = getAnswer(line);
            pw.print("say " + answer + "; alias quest;");
            pw.close();
            System.out.println(answer);
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
