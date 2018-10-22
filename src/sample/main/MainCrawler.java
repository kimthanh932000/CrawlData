/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import sample.crawler.Crawler;
import sample.utils.CrawlHelper;

/**
 *
 * @author Administrator
 */
public class MainCrawler {
    public static void main(String[] args) throws IOException{
        String uri = "http://vanphongphamanhhang.com/van-phong-pham/";
        String beginSign = "id=\"products\"";
        String endSign = "id=\"msgshow\"";
        
        //get html content
        Crawler.parseHTML_getPageCount(uri, beginSign, endSign); 
        //get page count
        int pageCount = Crawler.pageCount;
        //clean html content
        Crawler.htmlContent = CrawlHelper.cleanHTMLContent(Crawler.htmlContent);
        
    }
}
