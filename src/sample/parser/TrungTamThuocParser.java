/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.parser;

import sample.utils.ParserUtils;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import sample.jaxb.category.Category;
import sample.jaxb.product.Product;

/**
 *
 * @author Administrator
 */
public class TrungTamThuocParser {

    public static Set<String> productURLs = null;
//    public static int emptyProductCount = 0;

    public static Category getCategory(String content) throws XMLStreamException {
        XMLEvent event = null;

        XMLEventReader reader = ParserUtils.getReader(content);
        Iterator<XMLEvent> iterator = ParserUtils.fixWellForm(reader);

        Category category = null;
        while (iterator.hasNext()) {

            event = iterator.next();

            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();

                if (seQName.equals("span")) {

                }
            }
        }
        return category;
    }

    public static Set<String> getProductLinks(String content)
            throws SQLException, NamingException, XMLStreamException {
        TrungTamThuocParser.productURLs = null;
        XMLEvent event = null;

        XMLEventReader reader = ParserUtils.getReader(content);
        Iterator<XMLEvent> iterator = ParserUtils.fixWellForm(reader);
        String url = "";

        while (iterator.hasNext()) {

            event = iterator.next();

            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();

                if (seQName.equals("a")) {
                    Iterator<Attribute> attributes = se.getAttributes();
                    Attribute attr = null;

                    while (attributes.hasNext()) {
                        attr = attributes.next();
                        String name = attr.getName().getLocalPart().trim();
                        String value = attr.getValue().trim();

                        if (name.equals("href") && value.contains("/p/")) {
                            url = value;
                            if (productURLs == null) {
                                productURLs = new HashSet<>();
                            }
                            productURLs.add(url);
                            break;
                        }
                    }
                }
            }
        }
        return productURLs;
    }

    public static Product getProductDetails(String content) throws XMLStreamException {
        XMLEvent event = null;

        XMLEventReader reader = ParserUtils.getReader(content);
        Iterator<XMLEvent> iterator = ParserUtils.fixWellForm(reader);

        String imgURL = "";
        String productName = "";
        String price = "";
        String description = "";
        String productCode = "";

        while (iterator.hasNext()) {

            event = iterator.next();

            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();

                if (seQName.equals("img")) {    //get img url
                    Iterator<Attribute> attributes = se.getAttributes();
                    Attribute attr = null;

                    while (attributes.hasNext()) {
                        attr = attributes.next();
                        String name = attr.getName().getLocalPart().trim();
                        String value = attr.getValue().trim();

                        if (name.equals("src") && value.contains("/upload/image/san-pham/")) {
                            imgURL = "https://trungtamthuoc.com" + value;
                        }
                    }
                }

                if (seQName.equals("h1")) {    //get product name
                    Iterator<Attribute> attributes = se.getAttributes();
                    Attribute attr = null;

                    while (attributes.hasNext()) {
                        attr = attributes.next();
                        String name = attr.getName().getLocalPart().trim();
                        String value = attr.getValue().trim();

                        if (name.equals("id") && value.contains("ten-san-pham")) {
                            event = iterator.next();
                            productName = event.asCharacters().getData().trim();
                        }
                    }
                }

                if (seQName.equals("h2")) {    //get product code
                    Iterator<Attribute> attributes = se.getAttributes();
                    Attribute attr = null;

                    while (attributes.hasNext()) {
                        attr = attributes.next();
                        String name = attr.getName().getLocalPart().trim();
                        String value = attr.getValue().trim();

                        if (name.equals("class") && value.contains("product-code")) {
                            event = iterator.next();
                            if (event.asCharacters().getData().trim().contains("Mã")) {
                                productCode = event.asCharacters().getData().trim();
                                productCode = productCode.replace("Mã :", "").trim();
                            }
                        }
                    }
                }

                if (seQName.equals("ins")) {    //get product price
                    event = iterator.next();
                    price = event.asCharacters().getData().trim();
                    price = price.replace(",", "")
                            .replace("vnđ", "")
                            .replace("Giá :", "").trim();
                    if (price.contains("Liên hệ")) {
                        price = "0";
                    }
                    break;
                }
            }            
        }

        Product product = null;
        if (!productName.isEmpty() && !productCode.isEmpty() && !imgURL.isEmpty()) {
            product = new Product();
            product.setCode(productCode);
            product.setPrice(Integer.parseInt(price));
            product.setDescription(description);
            product.setImageURL(imgURL);
            product.setName(productName);
        }

        return product;
    }
}
