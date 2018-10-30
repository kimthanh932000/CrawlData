/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import sample.crawler.NhaThuoc365;
import sample.crawler.TrungTamThuoc;
import sample.dao.ProductDAO;
import sample.jaxb.product.Product;
import sample.jaxb.product.Products;

/**
 *
 * @author Administrator
 */
public class Main {

    public static int count = 0;   //count product

    public static void main(String[] args)
            throws IOException, XMLStreamException, SQLException, NamingException, JAXBException {

        Products allProducts = new Products();

        List<Product> listTrungTamThuoc = TrungTamThuoc.Crawler();

        allProducts.getProduct().addAll(listTrungTamThuoc);

        List<Product> listNhaThuoc365 = NhaThuoc365.Crawler();

        allProducts.getProduct().addAll(listNhaThuoc365);

        int begin = 0;
        int pos = 0;
        int end = allProducts.getProduct().size() - 1;

        while (true) {

            if (pos >= end) {
                pos = end;
            } else {
                pos = begin + 39;
            }

//            pos = begin + 3;
            int count = ProductDAO.addNewProduct(allProducts.getProduct().subList(begin, pos));
            System.out.println("Saved " + count + " products to DB");
            begin = pos + 1;
            
            if (pos >= end) {
                break;
            }
        }
//        int count = ProductDAO.addNewProduct(allProducts.getProduct());

//        System.out.println("Saved " + count + " products to DB");
    }
}
