/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.Crawler;
import sample.dao.CategoryDAO;
import sample.jaxb.category.Category;
import sample.parser.StAXParser;
import sample.utils.CrawlHelper;

/**
 *
 * @author Administrator
 */
public class MainCrawler {

    public static void main(String[] args) 
            throws IOException, XMLStreamException, SQLException, NamingException {
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
//        System.out.println(cleanHTML);
        Category cate = StAXParser.parseCategory(cleanHTML);
        if(cate != null){
            CategoryDAO.addNewCategory(cate);
        }
        
    }
}
