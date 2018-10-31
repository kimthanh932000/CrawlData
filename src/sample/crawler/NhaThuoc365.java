/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.crawler;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import sample.dao.CategoryDAO;
import sample.dao.ProductDAO;
import sample.jaxb.product.Product;
import sample.jaxb.product.Products;
import static sample.main.Main.count;
import sample.parser.NhaThuoc365Parser;

/**
 *
 * @author Administrator
 */
public class NhaThuoc365 {

    public static void Crawler()
            throws IOException, XMLStreamException, SQLException, NamingException {

        String urlThucPhamChucNang = "https://nhathuoc365.vn/thuc-pham-chuc-nang-p1";
        String urlTinhDau = "https://nhathuoc365.vn/tinh-dau-p2";

        ArrayList<String> listURL = new ArrayList<>();
        listURL.add(urlThucPhamChucNang);
        listURL.add(urlTinhDau);

        //list of all product from 2 urls
        Products listAllProducts = new Products();

        for (String url : listURL) {
            System.out.println("Crawling from " + url);
            String cleanHTML = "";

            //get html content contains page count
            HTMLCrawler.getHTMLSource_getPageCount(url, "class=\"breadcrumbs\"", "class=\"clearfix\"", "/trang-");

            //get html content contains category
            HTMLCrawler.getHTMLSource(url, "class=\"breadcrumbs\"", "id=\"aside\"");
            cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

            //get page count
            int pageCount = HTMLCrawler.pageCount;
//            System.out.println("Page count " + pageCount);

            //get category
            String category = NhaThuoc365Parser.getCategory(cleanHTML);
//            System.out.println("Category: " + category);

            boolean result = CategoryDAO.addNewCategory(category);  //save category to DB

            if (result) {

                //get category ID 
                int categoryID = CategoryDAO.getCategoryID(category);

                //crawl product details from all pages
                for (int i = 1; i <= pageCount; i++) {
                    System.out.println("Crawling products from page " + i + "...");
                    //uri of each page
                    String uriPage = url + "/trang-" + i;

                    //crawl product page
                    HTMLCrawler.getHTMLSource(uriPage, "id=\"content\"", "<div class=\"clearfix\">");

                    //clean html source
                    cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

                    //get all products urls of a single page
                    Set<String> productURLs = new HashSet<>();
                    productURLs = NhaThuoc365Parser.getProductURLs(cleanHTML);

                    if (productURLs.size() > 0) {

                        ArrayList<Product> listProductPerPage = new ArrayList<>();

                        for (String productURL : productURLs) {

                            //get html source of the product detail 
                            HTMLCrawler.getHTMLSource(productURL, "id=\"detail-product\"", "id=\"same_products\"");

                            //clean html content
                            cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

                            //create an instance of Product
                            Product product = NhaThuoc365Parser.getProductDetails(cleanHTML);

                            if (product != null) {
                                listProductPerPage.add(product);
                                product.setCategoryID(BigInteger.valueOf(categoryID));
//                                listAllProducts.getProduct().add(product);
                            }
                        }

                        //save list of products to DB
                        if (listProductPerPage.size() > 0) {
                            count += ProductDAO.addNewProduct(listProductPerPage);
                            System.out.println("Saved " + count + " products to DB");
                        }
                    }
                }
            }
        }
//        return listAllProducts.getProduct();
    }
}
