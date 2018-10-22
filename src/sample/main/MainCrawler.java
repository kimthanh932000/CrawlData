/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.Crawler;
import sample.jaxb.category.Category;
import sample.parser.StAXParser;
import sample.utils.CrawlHelper;

/**
 *
 * @author Administrator
 */
public class MainCrawler {

    public static void main(String[] args) throws IOException, XMLStreamException {
        String uri = "http://vanphongphamanhhang.com/van-phong-pham/But-bi-But-gel-But-de-ban/";
        String beginSign = "id=\"category\"";
        String endSign = "id=\"msgshow\"";
        
        String sample = "<div><a><img><h></img></div>";
        //get html content
        Crawler.parseHTML_getPageCount(uri, beginSign, endSign);
        //get page count
        int pageCount = Crawler.pageCount;
        //clean html content
        String cleanHTML = CrawlHelper.cleanHTMLContent(Crawler.htmlContent);
//        String newContent = CrawlHelper.fixString(cleanHTML);
//        Category cate = StAXParser.parseCategory(newContent);
        StAXParser.parseCategory(cleanHTML);
    }
}
