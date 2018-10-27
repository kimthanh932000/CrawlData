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
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.Crawler;
import sample.crawler.VPPAHCrawler;
import sample.dao.CategoryDAO;
import sample.dao.ProductDAO;
import sample.jaxb.category.Category;
import sample.jaxb.product.Product;
import sample.parser.TrungTamThuocParser;
import sample.utils.ParserUtils;
import sample.utils.CrawlUtils;

/**
 *
 * @author Administrator
 */
public class Main {

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException {
        crawlTrungTamThuoc();

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

    public static void crawlTrungTamThuoc() throws IOException, XMLStreamException, SQLException, NamingException {
        String url = "https://trungtamthuoc.com/pr/my-pham-i17/";     //loss of some products while crawling
        //String url = "https://trungtamthuoc.com/pr/vitamin-va-khoang-chat-i51/";  //loss of some products while crawling
        String beginSign = "class=\"product-breadcroumb\"";
        String endSign = "class=\"phantrang\"";

        //get html content
        Crawler.getHTMLSource_getPageCount(url, beginSign, endSign, "?page=");

        //get page count
        int pageCount = Crawler.pageCount;
        System.out.println("Page count " + pageCount);

        Category category = new Category();
        category.setName("Mỹ Phẩm");
        
        boolean result = CategoryDAO.addNewCategory(category);

        if (result) {
            
            int categoryID = CategoryDAO.getCategoryID(category, category.getName());
                    
            int count = 1;  //count product per category
            
            //crawl product details from all pages
            for (int i = 1; i <= pageCount; i++) {
                //uri of each page
                String uri = url + "?page=" + i;

                //crawl product page
                Crawler.getHTMLSource(uri, beginSign, endSign);

                //clean html source
                String cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

                //get all products urls of a single page
                Set<String> productLinks = new HashSet<>();
                productLinks = TrungTamThuocParser.getProductLinks(cleanHTML);

                if (productLinks.size() > 0) {

                    ArrayList<Product> productList = new ArrayList<>();

                    for (String productLink : productLinks) {
                        //get a product detail url
                        String productDetailsUrl = "https://trungtamthuoc.com" + productLink;

                        //get html source of the product detail 
                        Crawler.getHTMLSource(productDetailsUrl, "class=\"product-breadcroumb\"", "class=\"row contentPro\"");

                        //clean html content
                        cleanHTML = CrawlUtils.cleanHTMLContent(Crawler.htmlSource);

                        //create an instance of Product
                        Product product = TrungTamThuocParser.getProductDetails(cleanHTML);

                        if (product != null) {
                            productList.add(product);   //add product to list
//                            System.out.println(count + ". ");
//                            System.out.println("Name: " + product.getName());
//                            System.out.println("Price: " + product.getPrice());
//                            System.out.println("Description: " + product.getDescription());
//                            System.out.println("Code: " + product.getCode());
//                            System.out.println("ImgURL: " + product.getImageURL());
//                            count++;
                        }
                        if(productLinks.size() > 0){
                            for (Product p : productList) {
//                                ProductDAO.addNewProduct(product);
                            }
                        }
                    }
                }
            }
        }
    }
}
