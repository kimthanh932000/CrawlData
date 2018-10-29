/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Administrator
 */
public class Crawler {

    public static String htmlSource = "";
    public static int pageCount = 0;

    public static void getHTMLSource(String uri, String beginSign, String endSign)
            throws MalformedURLException, IOException {
        htmlSource = "";
        boolean isInside = false;
        int count = 0;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {
//                System.out.println(inputLine);
                if (inputLine.contains(beginSign)) {
                    if (count == 0) {
                        isInside = true;
                    }
                    count++;
                }
                if (inputLine.contains(endSign)) {
                    isInside = false;
//                    break;
                }
                if (isInside) {
                    htmlSource = htmlSource + inputLine.trim() + "\n";
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public static void getPageCount(String content, String key) {
        pageCount = 0;
        if (content.contains(key)) {

            int beginPage = 0;
            int endPage = -1;
            int index = 0;

            while (index != -1) {
                index = content.indexOf(key, index);
                if (index != -1) {
                    beginPage = index + key.length();
                    for (int i = beginPage; i < content.length(); i++) {
                        if (content.charAt(i) == '\'' || content.charAt(i) == '"') {
                            endPage = i;
                            break;
                        }
                    }
                    if (endPage != -1) {
                        int num = Integer.parseInt(content.substring(beginPage, endPage));
                        pageCount = Math.max(num, pageCount);
                    }
                    index += key.length();
                }
            }
        }
    }

    public static void getHTMLSource_getPageCount(String uri, String beginSign, String endSign, String key)
            throws MalformedURLException, IOException {
        htmlSource = "";
        boolean isInside = false;
        int count = 0;
        InputStream is = null;
        BufferedReader br = null;
        
        try {
            URL url = new URL(uri);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {

                if (inputLine.contains(key)) {
                    getPageCount(inputLine, key);
                }
                if (inputLine.contains(beginSign)) {
                    if (count == 0) {
                        isInside = true;
                    }
                    count++;
                }
                if (inputLine.contains(endSign)) {
                    isInside = false;
                }
                if (isInside) {
                    htmlSource = htmlSource + inputLine.trim() + "\n";
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
    public static String cleanHTMLContent(String content) {
        content = content.replace("<br />", "")
                .replace("<br>", "")
                .replace("itemscope", "")
                .replace("null", "")
                .replace("&#38;&nbsp;", "")
                .replace("&nbsp;&nbsp;", "")
                .replace("\t", "")
                .replace("&", "&#38;")
                .replace("/>", ">")
                .replace("LANBELLE LAN'STAMANU CREAM-kem dưỡng da chiết xuất từ tự nhiên", "")
                .replace("< ", "")
                .replace("fb:share:layout=\"button_count\"", "")
                .replace("g:plusone:size=\"medium\"", "")
                .replace("<col style=\"text-align: justify;\" width=\"406\" >", "")
                .replaceAll("(?m)^\\s", "");
        content = "<root>" + "\n" + content + "</root>";
        return content;
    }
}
