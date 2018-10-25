/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import sample.crawler.Crawler;
import sample.crawler.VPPAHCrawler;
import sample.jaxb.category.Category;
import sample.jaxb.product.Product;
import sample.parser.TTTParser;
import sample.utils.ParserUtils;
import sample.utils.CrawlUtils;

/**
 *
 * @author Administrator
 */
public class Main {

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException {
        parseTTT();

    }

    public static void parseVPPAH() throws IOException, XMLStreamException {
        String vppahUrl = "http://vanphongphamanhhang.com/van-phong-pham/But-bi-But-gel-But-de-ban/";
        String beginSign = "id=\"category\"";
        String endSign = "id=\"msgshow\"";

        //get html content
        VPPAHCrawler.getHTMLSource_getPageCount(vppahUrl, beginSign, endSign);

        //get page count
        int pageCount = VPPAHCrawler.pageCount;

        String cleanHTML = "";
        Category category = new Category();
        List<Product> listProduct = new ArrayList<>();
        Product product = new Product();

        //crawl product from all pages
        for (int i = 1; i <= pageCount; i++) {

            //uri of each page
            String uri = vppahUrl + "page-" + i + "/";

            //crawl product page
            VPPAHCrawler.getHTMLSource(uri, beginSign, endSign);

            //clean html
            cleanHTML = CrawlUtils.cleanHTMLContent(VPPAHCrawler.htmlSource);

            //get category
//            category = StAXParser.parseCategory(cleanHTML);
//            if (category != null) {
//                String categoryName = category.getCategoryName();
//                if (!categoryName.isEmpty() || categoryName != null) {
//                    CategoryDAO.addNewCategory(category);   //save category to DB
//                }
//            }
            listProduct = ParserUtils.parseListProduct(cleanHTML);
        }
    }

    public static void parseTTT() throws IOException, XMLStreamException, SQLException, NamingException {
        String url = "https://trungtamthuoc.com/pr/my-pham-i17/";
        String beginSign = "class=\"product-breadcroumb\"";
        String endSign = "class=\"phantrang\"";

        //get html content
        Crawler.getHTMLSource_getPageCount(url, beginSign, endSign, "?page=");

        //get page count
        int pageCount = Crawler.pageCount;
        System.out.println("Page count " + pageCount);

        int count = 1;
//        crawl product details from all pages
        for (int i = 1; i <= pageCount; i++) {
            //uri of each page
            String uri = url + "?page=" + i;

            //crawl product page
            Crawler.getHTMLSource(uri, beginSign, endSign);

            //clean html source
            String cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

//        get all products urls of a single page
            Set<String> productLinks = new HashSet<>();
            productLinks = TTTParser.getProductLinks(cleanHTML);

            if (productLinks != null && productLinks.size() > 0) {
                for (String productLink : productLinks) {
                    System.out.println(count + ". " + productLink);
                    count++;
                }
                TTTParser.productURLs = null;   //set productURLs = null each time get enough 40 products url per page
            }

            //clean html
//            cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);
//            XMLEventReader reader = ParserUtils.getReader(cleanHTML);
//            Iterator<XMLEvent> iterator = ParserUtils.fixWellForm(reader);
        }
    }
}
