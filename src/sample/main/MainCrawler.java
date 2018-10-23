/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.Crawler;
import sample.dao.CategoryDAO;
import sample.jaxb.category.Category;
import sample.jaxb.product.ListProduct;
import sample.jaxb.product.Product;
import sample.parser.StAXParser;
import sample.utils.CrawlHelper;

/**
 *
 * @author Administrator
 */
public class MainCrawler {

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException {
        String vppahUrl = "http://vanphongphamanhhang.com/van-phong-pham/But-bi-But-gel-But-de-ban/";
        String beginSign = "id=\"category\"";
        String endSign = "id=\"msgshow\"";

        //get html content
        Crawler.parseHTML_getPageCount(vppahUrl, beginSign, endSign);

        //get page count
        int pageCount = Crawler.pageCount;

        String cleanHTML = "";
        Category category = new Category();
        List<Product> listProduct = new ArrayList<>();
        Product product = new Product();

        //crawl product from all pages
        for (int i = 1; i <= pageCount; i++) {

            //uri of each page
            String uri = vppahUrl + "page-" + i + "/";

            //crawl product page
            Crawler.parseHTML(uri, beginSign, endSign);

            //clean html
            cleanHTML = CrawlHelper.cleanHTMLContent(Crawler.htmlContent);
            
            //get category
//            category = StAXParser.parseCategory(cleanHTML);
//            if (category != null) {
//                String categoryName = category.getCategoryName();
//                if (!categoryName.isEmpty() || categoryName != null) {
//                    CategoryDAO.addNewCategory(category);   //save category to DB
//                }
//            }

            listProduct = StAXParser.parseListProduct(cleanHTML);
        }
    }
}
