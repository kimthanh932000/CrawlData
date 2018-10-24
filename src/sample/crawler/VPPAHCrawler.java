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
import java.util.ArrayList;
import sample.utils.CrawlUtils;

/**
 *
 * @author Administrator
 */
public class VPPAHCrawler {

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
                if (inputLine.contains(beginSign)) {
                    if (count == 0) {
                        isInside = true;
                    }
                    count++;
                }
                if (inputLine.contains(endSign)) {
//                    isInside = false;
                    break;
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

    public static void getPageCount(String content) {
        if (content.contains("<a title=\"")) {
            String key = "- Trang ";    //keyword contains page count
            ArrayList<Integer> pos = new ArrayList<>();
            int num = 0;
            if (content.contains(key)) {
                int index = 0;
                while (index != -1) {
                    index = content.indexOf(key, index);
                    if (index != -1) {
                        pos.add(index);
                        index += key.length();
                    }
                }
            }
            for (Integer p : pos) {
                num = Integer.parseInt(content.substring(p + key.length(), p + key.length() + 1));
                pageCount = Math.max(num, pageCount);
//                System.out.println(pageCount);
            }
        }
    }

    public static void getHTMLSource_getPageCount(String uri, String beginSign, String endSign)
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
//                getPageCount(inputLine);

                if (inputLine.contains(beginSign)) {
                    if (count == 0) {
                        isInside = true;
                    }
                    count++;
                }
                if (inputLine.contains(endSign)) {
//                    isInside = false;
                    break;  //stop when finish getting needed data block
                }
                if (isInside) {
                    getPageCount(inputLine);
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
        
}
