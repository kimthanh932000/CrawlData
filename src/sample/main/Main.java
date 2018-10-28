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
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.Crawler;
import sample.dao.CategoryDAO;
import sample.dao.ProductDAO;
import sample.jaxb.product.Product;
import sample.parser.TrungTamThuocParser;
import sample.utils.CrawlUtils;

/**
 *
 * @author Administrator
 */
public class Main {

    public static int count = 0;   //count product

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException {
        crawlTrungTamThuoc();

    }

    public static void crawlTrungTamThuoc()
            throws IOException, XMLStreamException, SQLException, NamingException {
        String urlMyPham = "https://trungtamthuoc.com/pr/my-pham-i17/";  
        String urlHuongLieu = "https://trungtamthuoc.com/pr/huong-lieu-i19/";
//        String urlVitamin = "https://trungtamthuoc.com/pr/vitamin-va-khoang-chat-i51/";  
        String beginSign = "class=\"product-breadcroumb\"";
        String endSign = "class=\"phantrang\"";

        ArrayList<String> listURL = new ArrayList<>();
        listURL.add(urlMyPham);
        listURL.add(urlHuongLieu);
//        listURL.add(urlVitamin);

        for (String url : listURL) {
            
            //get html content
            Crawler.getHTMLSource_getPageCount(url, beginSign, endSign, "?page=");

            //clean html
            String cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

            //get page count
            int pageCount = Crawler.pageCount;
            System.out.println("Page count " + pageCount);

            String category = TrungTamThuocParser.getCategory(cleanHTML);
            System.out.println("Category: " + category);

            boolean result = CategoryDAO.addNewCategory(category);  //save category to DB

            if (result) {

                //get category ID 
                int categoryID = CategoryDAO.getCategoryID(category);

                //crawl product details from all pages
                for (int i = 1; i <= pageCount; i++) {
                    //uri of each page
                    String uriPage = url + "?page=" + i;

                    //crawl product page
                    Crawler.getHTMLSource(uriPage, beginSign, endSign);

                    //clean html source
                    cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

                    //get all products urls of a single page
                    Set<String> productURLs = new HashSet<>();
                    productURLs = TrungTamThuocParser.getProductURLs(cleanHTML);

                    if (productURLs.size() > 0) {

                        ArrayList<Product> productList = new ArrayList<>();

                        for (String productURL : productURLs) {
                            //get a product detail url
                            String productDetailsUrl = "https://trungtamthuoc.com" + productURL;

                            //get html source of the product detail 
                            Crawler.getHTMLSource(productDetailsUrl, "class=\"product-breadcroumb\"", "class=\"row contentPro\"");

                            //clean html content
                            cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

                            //create an instance of Product
                            Product product = TrungTamThuocParser.getProductDetails(cleanHTML);

                            if (product != null) {
                                productList.add(product);
                            }
                        }

                        //save list of products to DB
                        if (productList.size() > 0) {
                            count += ProductDAO.addNewProduct(productList, categoryID);
                            System.out.println("Saved " + count + " products to DB");
                        }
                    }
//                break;
                }
            }
        }
    }
}
