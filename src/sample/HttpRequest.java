package sample;

import com.oracle.javafx.jmx.json.JSONFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private final String BASIC_URL = "http://www.onlinesjtu.com/SCEPlayer/Default.aspx";
    private String studentid;
    private String cookie;
    private String term;

    public void requestCourse(String courseid) {
        try {
            String addr = String.format("%s?studentid=%s&courseid=%s&termidentify=%s", this.BASIC_URL, this.studentid, courseid, this.term);
            URL url = new URL(addr);

            InputStream in = url.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader buff = new BufferedReader(isr);
            String str;
            StringBuilder body = new StringBuilder();
            while ((str = buff.readLine()) != null) {
                body.append(str);
                body.append("\n");
            }
            System.out.println("request over");
            in.close();
            isr.close();
            buff.close();
            System.out.println(url.toString());
            String reg = "id=\"list-item-(\\d+)\"\\sclass=\"status-no\"";
            Pattern p = Pattern.compile(reg);

            Matcher m = p.matcher(body);
            while (m.find()) {
                String resouce = m.group(1);
                this.attendCourse(resouce);
            }
            System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attendCourse (String resouce) {
        try {
            URL url = new URL(this.BASIC_URL + "/AddDacLog");
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);// 使用 URL 连接进行输出
            conn.setDoInput(true);// 使用 URL 连接进行输入
            conn.setUseCaches(false);// 忽略缓存
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Cookie", this.cookie);

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            String jsonPayload = String.format("{\"resourceId\":\"%s\",\"studentId\":\"%s\"}", resouce, this.studentid);
            System.out.println("START POST: " + jsonPayload);
            out.print(jsonPayload);
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder body = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                body.append(line + "\n");
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    HttpRequest(String studentid, String cookie, String term) {
        this.studentid = studentid;
        this.cookie = cookie;
        this.term = term;
    }

}
