package com.cat.bots.robot;

import com.cat.bots.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.KeyEvent;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot {
    private static final String NOT_FOUND_RESPONSE = "Ne go znam";
    private static final String BLANK_CHARACTER = "18n18n18n18n18n18n18n18n18n";
    private static final String QUEST_PREFIX = "[Quest]";
    private static final String SPEC_TRANSFER_SUFFIX = "transferira Cat kam SPECTATOR";
    private static final String TERRORIST_TRANSFER_SUFFIX = "transferira Cat kam TERRORIST";
    private static final String GOOGLE_REQUEST_STRING = "Google kaji mi ";
    private static final String ANSWER_REQUEST_STRING = "!pitam ";
    private static final String LYRICS_REQUEST_STRING = "!pusni ";
    private static final String LYRICS_FILE_PATH = "D:\\Program Files (x86)\\Steam\\steamapps\\common\\Half-Life\\cstrike\\output.cfg";

    private static Bot bot;
    private static Robot robot_instance;
    private Pallette pallette;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private Capitals capitals = new Capitals();
    private String[] googleClasses;
    private AutoSpammer spammerThread;
    private List<String> specTransferResponses;
    private int jbDay;
    private boolean resolved = false;
    private boolean crying = false;
    private String lastQuery;
    
    private String nickname;
    private File config;
    private File outputConfig;

    public static Bot getInstance(CommandLine clp) throws AWTException {
        if (bot == null)
            bot = new Bot(clp);
        return bot;
    }
    
    public static Bot getInstance() throws AWTException {
        return bot;
    }
    
    private Bot(CommandLine clp) throws AWTException {
        
        nickname = clp.getArgument("nickname");
        config = new File(clp.getArgument("config"));
        outputConfig = new File(clp.getArgument("output"));
        pallette = new Pallette();
        if(outputConfig.delete())
            System.out.println("File deleted successfully");

        specTransferResponses = Arrays.asList(
                "ne me barai",
                "ne sum afk we",
                "pak li shte me mestite spec",
                "pak li spec",
                "ashdoigfashdoigoi",
                "stiga spec we doklad",
                "afk si ti dosadnik",
                "wrushtaite me obratno we",
                "wuzkresete me",
                "ehoooooooooooooooooo",
                "tuka sum be",
                "sprete se",
                "koi pak me mesti we",
                "nqma li da prestanete",
                "prastani we");
        robot_instance = new Robot();
        googleClasses = new String[] {
                "FLP8od",
                "Z0LcW",
                "gsrt",
                "vk_bk dDoNo",
                "title",
                "qv3Wpe",
                "vk_bk sol-tmp\" style=\"float:left;margin-top:-3px;font-size:64px\"><span class=\"wob_t\" id=\"wob_tm\" style=\"display:inline\"", // translation
                "tw-data-text tw-text-large tw-ta\" data-placeholder=\"Translation\" id=\"tw-target-text\" style=\"text-align:left\"><span",
                "ztXv9",
                "ayqGOc",
                //"o3Kv0", //coronavirus
                "e24Kjd" //tables
        };
    }
    
    public int getJbDay() {
        return jbDay;
    }

    public void setJbDay(int jbDay) {
        System.out.println(jbDay);
        this.jbDay = jbDay;
    }

    public boolean isCrying() {
        return crying;
    }

    public void setCrying(boolean crying) {
        this.crying = crying;
    }


    private int processVerticalLine(int x, StringBuilder sb, BufferedImage chat) {
        int seriesIncrement = 1;
        boolean isLastHit = pallette.hasColor(new Color(chat.getRGB(x, 0)));
        for (int y = 1; y < chat.getHeight(); y++) {
            Color color = new Color(chat.getRGB(x, y));
            boolean isHit = pallette.hasColor(color);

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
        Scanner sc = new Scanner(config);

        StringBuilder sb = new StringBuilder();
        StringBuilder output = new StringBuilder();

        for (int x = 0; x < chat.getWidth(); x++) {
            int seriesIncrement = processVerticalLine(x, sb, chat);
            if ((x+1) % 10 == 0) {
                while (sc.hasNextLine()) {
                    if (x == 9 && sb.toString().equals(BLANK_CHARACTER))
                        return "";
                    String line = sc.nextLine();
                    if (line.startsWith(sb.toString())) {
                        output.append(line.charAt(line.length() - 1));
                        break;
                    }
                }

                sc = new Scanner(config);
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
        int deadOffset = isDead() ? 80 : 0;
        return robot_instance.createScreenCapture(new Rectangle(10, 899 + line*21 - deadOffset, 1350, 18));
    }
    
    public String getAnswer(String line) throws IOException {
        String question = line.substring(line.lastIndexOf(':') + 1)
                .replace("?","")
                .replace("=","")
                .trim();
        question = question
                .replace("koren ot", "sqrt")
                .replaceAll("\\([A-Za-z :]+?\\)", "");
        
        String jbDayReturn;
        String questionLowerCase = question.toLowerCase();
        
        int indexCapital;

        if (question.matches("[0-9x*^/+\\- =sqrt]+"))
            return Long.toString((long) MathParser.eval(question.replace('x', '*')));
        else if (question.contains(" li "))
            return "da; say ne";
        else if (question.matches("[0-9]+do[0-9]+"))
            return question.replace("do", "");
        else if ((indexCapital = questionLowerCase.indexOf("stolica")) >= 0) {
            if (question.indexOf(" e") < indexCapital) {
                String countryAnswer = capitals
                        .getCountries()
                        .get(question.substring(question
                            .indexOf(" na ") + 4)
                            .toLowerCase());
                if (countryAnswer == null)
                    return getFromGoogle(question, true).trim();
                else
                    return countryAnswer;
            }
        
            String capitalAnswer = capitals
                    .getCapitals()
                    .get(question.substring(0, question
                            .indexOf(' '))
                            .toLowerCase());
            if (capitalAnswer == null)
                return getFromGoogle(question, true).trim();
            else
                return capitalAnswer;
        }
        else if (questionLowerCase.contains("simon"))
            return line.substring(8, line.indexOf("Zadade"));
        else if (question.contains(" jivi ") || question.contains(" jiwi "))
            return getAliveTerrorists();
        else if (question.contains(" murtvi ") || question.contains(" murtwi "))
            return getDeadTerrorists();
        else if (questionLowerCase.contains(" t ") || questionLowerCase.contains(" teroristi "))
            return getAllTerrorists();
        else if ((jbDayReturn = tryJailbreakDay(question)) != null)
            return jbDayReturn;
        else if (question.contains("sekundi") && question.contains("komanda"))
            return "5 sekundi; say 10 sekundi";
        else if (questionLowerCase.contains(" cat"))
            return "cat";
        else
            return getFromGoogle(question, true).trim();
    }
    
    private String tryJailbreakDay(String question) {
        
        String jb1;
        String jb2;
        String jb3;
        String jb4;
        
        if ((jb1 = tryJailbreakDayWithString(question, "jb")) != null)
            return jb1;
        else if ((jb2 = tryJailbreakDayWithString(question, "jail")) != null)
            return jb2;
        else if ((jb3 = tryJailbreakDayWithString(question, "jailbreak")) != null)
            return jb3;
        else if ((jb4 = tryJailbreakDayWithString(question, "jail break")) != null)
            return jb4;
        else
            return null;
    }
    
    private String tryJailbreakDayWithString(String question, String target) {
        int jbDayIndex = question.indexOf(target);
        if (jbDayIndex >= 0)
            return Long.toString((long)MathParser.eval(jbDay + question.substring(jbDayIndex + target.length())));
        else
            return null;
    }

    public String getDeadTerrorists() throws IOException {
        String[] message = getTerroristsMessageArray();
        return Long.toString(Long.parseLong(message[4]) - Long.parseLong(message[1]));
    }

    public String getAllTerrorists() throws IOException {
        return getTerroristsMessageArray()[4];
    }

    public String getAliveTerrorists() throws IOException {
        return getTerroristsMessageArray()[1];
    }

    private String[] getTerroristsMessageArray() throws IOException {
        BufferedImage image = robot_instance.createScreenCapture(new Rectangle(10, 1014, 300, 18));
        String text = processText(image);
        return text.split(" ");
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    
    private List<String> parseResultTable(String result) {
        List<String> records = new ArrayList<>();
        String recordRegex = "<td.*?>(.+?)\\s*<\\/td>";
        if (!result.contains("<table>") || !result.contains("</table>"))
            return null;
        Pattern pattern = Pattern.compile(recordRegex);
        Matcher matcher = pattern.matcher(result).region(result.indexOf("<table>"), result.indexOf("</table>"));
        
        while (matcher.find()) {
            String record = matcher.group();
            record = record.replaceAll("<.+?>", "").replaceAll("\\(.+?\\)", "");
            records.add(record.toLowerCase());
        }
        return records;
    }

    public String getFromGoogle(String question, boolean translate) throws IOException {
        
        question = question.trim()
                .replace(" ", "%20")
                .replace("\"", "%22");
        if (translate)
            question = question
                    .replace("c", "ts")
                    .replace("tsh", "ch");
        
        String query = question;

        System.out.println("From google with translation: " + translate + " query: " + query);
        
        if (query.contains("^") 
                || query.contains("+")
                || query.contains("\""))
            query = encodeValue(query);
        
        HttpGet request = new HttpGet("https://www.google.com/search?q=" + query + "&hl=en&aqs=chrome..69i57j69i59l2.517j0j9&sourceid=chrome&ie=UTF-8");
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

        String answer = "";
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            
            String result = EntityUtils.toString(entity);
            String startString = ">";
            String endString = "<";
            int answerIndex;
            
            
            if (result.contains("Our systems have detected unusual traffic from your computer network"))
                return "Google is not working";
            
            for (String tag : googleClasses) {
                if ((answerIndex = result.indexOf("class=\"" + tag)) >= 0) {
                    if (tag.equals("ztXv9") || tag.equals("e24Kjd")) {
                        String[] questionTokens = question.split("%20");
                        List<String> resultTableRecords = parseResultTable(result);
                        if (resultTableRecords == null) {
                            addLyrics(getLongInfoBox(result));
                            return "Result is ready!";
                        }
                        for (String questionToken : questionTokens) {
                            for (int j = 0; j < resultTableRecords.size(); j++) {
                                if (j % 2 == 0 & resultTableRecords.get(j).contains(questionToken))
                                    return resultTableRecords.get(j + 1);
                            }
                        }
                    }
                    answer = result
                            .substring(result.indexOf(startString, answerIndex + tag.length() + 7) +  + startString.length(), result.indexOf(endString, answerIndex + tag.length() + 7));
                    break;
                }
            }
            
            if (answer.isEmpty()) {
                if (translate)
                    return getFromGoogle(translateQuestion(question), false);
                else
                    return NOT_FOUND_RESPONSE;
            }
                
            answer = answer.replace('\u00A0', ' ').replace(",","");
            if (answer.matches("^[0-9].+"))
                if (answer.indexOf(' ') > 0)
                    return answer.substring(0, answer.indexOf(' '));
                else
                    return answer;
            else
                return answer.replace(",","");
        }
    }

    private void addLyrics(List<String> lyrics) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append("alias spamycs lyrics0\n");
        int i;
        for (i = 0; i < lyrics.size(); i++) {
            sb
                    .append("alias lyrics")
                    .append(i)
                    .append(" \"say ")
                    .append(lyrics.get(i))
                    .append(" ;alias spamycs lyrics")
                    .append(i + 1)
                    .append("\"\n");
        }
        sb.append(";alias lyrics").append(i).append(" say ---;");
        saveLyrics(sb.toString());
    }

    private List<String> getLongInfoBox(String result) {
        int start = result.indexOf("<span class=\"e24Kjd\">");
        result = result
                .substring(start, result
                        .indexOf("</span>", start))
                .replaceAll("<.+?>", "")
                .replaceAll("\\(.+?\\)", "")
                .replace("&quot;", "\'");
        int lineStart = 0;
        int lineEnd = 0;
        List<String> lines = new ArrayList<>();
        while ((lineEnd = result.indexOf(' ', lineStart + 70)) > 0) {
            lines.add(result.substring(lineStart, Math.min(lineEnd, lineStart + 80)));
            lineStart = lineEnd + 1;
        }
        return lines;
    }

    public String translateQuestion(String question) {
        HttpGet request = new HttpGet("https://translate.google.bg/translate_a/single?client=gtx&sl=bg&tl=en&hl=en&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=1&ssel=0&tsel=0&xid=1791807&kc=3&tk=738940.917225&q="
                + question);
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            int start = result.indexOf("[\"") + 2;
            int end = result.indexOf("\",", start + 1);
            System.out.println("From translation with result: "  + result.substring(start, end));
            return result.substring(start, end).replace("\\", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void writeConfig(String cnf) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(outputConfig), cnf.length());
        out.write(cnf);
        out.close(); 
    }
    
    public void reloadSpamButton() throws InterruptedException {
        robot_instance.keyPress(KeyEvent.VK_NUMPAD8);
        TimeUnit.MILLISECONDS.sleep(50);
        robot_instance.keyRelease(KeyEvent.VK_NUMPAD8);
    }
    
    public void fireSpamButton() throws InterruptedException {
        robot_instance.keyPress(KeyEvent.VK_NUMPAD5);
        TimeUnit.MILLISECONDS.sleep(50);
        robot_instance.keyRelease(KeyEvent.VK_NUMPAD5);
    }
    
    public void cryInChat() throws IOException {
        int randomIndex = new Random().nextInt(specTransferResponses.size());
        writeConfig("say " + specTransferResponses.get(randomIndex) + "; alias quest;");
    }

    public void act(String line) throws IOException, InterruptedException {
        String nicknameRegex = "([~*]DEAD[~*] )?(\\[.+?])?( )?" + nickname + "( )?: ";
        
        if ((line.equals(lastQuery) || !line.contains(nickname)) && !line.contains("Quest"))
            return;
        
        if (line.startsWith(QUEST_PREFIX))
        {
            if (line.trim().contains("otgovori pravilno na vaprosa"))
                return;
            
            if (!resolved) {
                String answer = getAnswer(line);
                writeConfig("say " + answer + "; alias quest;");
                System.out.println(answer);
                resolved = true;
            }
        } else if (line.trim().endsWith(SPEC_TRANSFER_SUFFIX)) {
            if (!crying) {
                crying = true;
                spammerThread = new AutoSpammer();
                spammerThread.start();
            }
        } else if (line.trim().endsWith(TERRORIST_TRANSFER_SUFFIX)) {
            crying = false;
        } else if (line.matches(nicknameRegex + GOOGLE_REQUEST_STRING + ".+")) {
            if (!resolved) {
                lastQuery = line;
                String answer = getFromGoogle(line.substring(line.indexOf(": ") + + GOOGLE_REQUEST_STRING.length() + 2), true).trim();
                writeConfig("say " + answer + "; alias quest;");
                System.out.println(answer);
                resolved = true;
            }
        } else if (line.matches(nicknameRegex + ANSWER_REQUEST_STRING + ".+")) {
            if (!resolved) {
                lastQuery = line;
                String answer = getAnswer(line.substring(line.indexOf(": ") + ANSWER_REQUEST_STRING.length() + 2)).trim();
                writeConfig("say " + answer + "; alias quest;");
                System.out.println(answer);
                resolved = true;
            }
        }
        else if (line.matches(nicknameRegex + LYRICS_REQUEST_STRING + ".+")) {
            if (!resolved) {
                lastQuery = line;
                String result = loadLyrics(line.substring(line.indexOf(": ") + LYRICS_REQUEST_STRING.length() + 2)).trim();
                writeConfig("say " + result + "; alias quest;");
                System.out.println(result);
                resolved = true;
            }
        }
        else if (resolved && line.matches(nicknameRegex + ".+")) {
            if(outputConfig.delete())
                System.out.println("File deleted successfully");
            resolved = false;
        }
    }

    private String loadLyrics(String question) {
        question = question.trim()
                .replace(" ", "%20")
                .replace("\"", "%22");

        HttpGet request = new HttpGet("https://www.google.com/search?q=" + question + "%20lyrics&hl=en&aqs=chrome..69i57j69i59l2.517j0j9&sourceid=chrome&ie=UTF-8");
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        
        boolean found = false;
        
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            List<String> lyrics = new ArrayList<>();
            int indexOfTag = 0;
            while ((indexOfTag = result.indexOf("jsname=\"YS01Ge\"", indexOfTag + 1)) >= 0) {
                lyrics.add(result.substring(indexOfTag + 16, result.indexOf('<', indexOfTag)).replace("\"", ""));
                found = true;
            }
            addLyrics(lyrics);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found ? "Loaded" : "Nqma q";
    }

    private void saveLyrics(String lyrics) throws FileNotFoundException {
        File file = new File(LYRICS_FILE_PATH);
        PrintWriter pw = new PrintWriter(file);
        pw.print(lyrics);
        pw.close();
    }

    public void executeCommand(String command) {
        int commandEnd = command.indexOf(' ');
        if (commandEnd < 0)
            return;
        
        String commandName = command.substring(0, commandEnd);
        String commandArgument = command.substring(commandEnd + 1);
        switch (commandName) {
            case "changename":
                nickname = commandArgument;
                break;
            case "changeday":
                jbDay = Integer.parseInt(commandArgument);
                break;
            case "testline":
                try {
                    act(commandArgument);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "pitam":
                try {
                    System.out.println(getAnswer(commandArgument));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        System.out.println("Executed \"" + commandName + "\" with argument \"" + commandArgument + "\"");
    }
    
    public boolean isDead() {
        Color col = robot_instance.getPixelColor(1799,43);
        return col.equals(pallette.getDeadYellow());
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
