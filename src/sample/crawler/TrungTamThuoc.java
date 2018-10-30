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
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import sample.dao.CategoryDAO;
import sample.dao.ProductDAO;
import sample.jaxb.product.Product;
import sample.jaxb.product.Products;
import static sample.main.Main.count;
import sample.parser.TrungTamThuocParser;

/**
 *
 * @author Administrator
 */
public class TrungTamThuoc {

    public static List<Product> Crawler()
            throws IOException, XMLStreamException, SQLException, NamingException, JAXBException {
        String urlMyPham = "https://trungtamthuoc.com/pr/my-pham-i17/";
        String urlHuongLieu = "https://trungtamthuoc.com/pr/huong-lieu-i19/";
        String beginSign = "class=\"product-breadcroumb\"";
        String endSign = "class=\"phantrang\"";

        //list of 2 urls to crawl from
        ArrayList<String> listURL = new ArrayList<>();
        listURL.add(urlMyPham);
        listURL.add(urlHuongLieu);

        //list of all product from 2 urls
        Products listAllProducts = new Products();

        //loop through 2 urls 
        for (String url : listURL) {

            //get html content
            HTMLCrawler.getHTMLSource_getPageCount(url, beginSign, endSign, "?page=");

            //clean html
            String cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

            //get page count
            int pageCount = HTMLCrawler.pageCount;
//            System.out.println("Page count " + pageCount);

            String category = TrungTamThuocParser.getCategory(cleanHTML);
//            System.out.println("Category: " + category);

            boolean result = CategoryDAO.addNewCategory(category);  //save category to DB

            if (result) {

                //get category ID 
                int categoryID = CategoryDAO.getCategoryID(category);

                //crawl product details from all pages
                for (int i = 1; i <= pageCount; i++) {
                    //uri of each page
                    String uriPage = url + "?page=" + i;

                    //crawl product page
                    HTMLCrawler.getHTMLSource(uriPage, beginSign, endSign);

                    //clean html source
                    cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

                    //get all products urls of a single page
                    Set<String> productURLs = new HashSet<>();
                    productURLs = TrungTamThuocParser.getProductURLs(cleanHTML);

                    if (productURLs.size() > 0) {

                        ArrayList<Product> listProductPerPage = new ArrayList<>();

                        for (String productURL : productURLs) {
                            //get a product detail url
                            String productDetailsUrl = "https://trungtamthuoc.com" + productURL;

                            //get html source of the product detail 
                            HTMLCrawler.getHTMLSource(productDetailsUrl, "class=\"product-breadcroumb\"", "class=\"row contentPro\"");

                            //clean html content
                            cleanHTML = HTMLCrawler.cleanHTMLContent(HTMLCrawler.htmlSource);

                            //create an instance of Product
                            Product product = TrungTamThuocParser.getProductDetails(cleanHTML);

                            if (product != null) {
                                product.setCategoryID(BigInteger.valueOf(categoryID));
//                                listProductPerPage.add(product);
                                listAllProducts.getProduct().add(product);
                            }
                        }

                        //save all products per page to DB
//                        if (listProductPerPage.size() > 0) {
//                            count += ProductDAO.addNewProduct(listProductPerPage);
//                            System.out.println("Saved " + count + " products to DB");
//                        }
                    }
                }
            }
        }
        return listAllProducts.getProduct();
    }
}
