/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.parser;

import com.sun.xml.internal.stream.events.EndElementEvent;
import com.sun.xml.internal.stream.events.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import sample.dao.CategoryDAO;
import sample.jaxb.category.Category;
import sample.jaxb.product.ListProduct;
import sample.jaxb.product.Product;

/**
 *
 * @author Administrator
 */
public class StAXParser {

    protected static Iterator<XMLEvent> autoAddMissingEndtag(XMLEventReader reader) {
        ArrayList<XMLEvent> IEvents = new ArrayList<>();
        int endTagMarker = 0;
        while (endTagMarker >= 0) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();

            } catch (XMLStreamException exception) {
                String msg = exception.getMessage();
//                System.out.println(msg);
                String msgErrorString = "The element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be terminated"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));
                    event = missingTag;
                }
                msgErrorString = "Element type \"";

                if (msg.contains(msgErrorString)) {
                    String missingTagName = msg.substring(msg.indexOf(msgErrorString) + msgErrorString.length(), msg.indexOf("\" must be followed"));
                    EndElement missingTag = new EndElementEvent(new QName(missingTagName));

                    event = missingTag;
                }

            } catch (NullPointerException exception) {
                break;
            }
            if (event != null) {
                if (event.isStartElement()) {
                    endTagMarker++;
                } else if (event.isEndElement()) {
                    endTagMarker--;

                }
                if (endTagMarker >= 0) {
                    IEvents.add(event);
                }
            }
        }
        return IEvents.iterator();
    }

    //get reader
    public static XMLEventReader getReader(String content) throws XMLStreamException {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        StreamSource streamsource = new StreamSource(is);
        XMLEventReader reader = null;
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setEventAllocator(new XMLEventAllocatorImpl());
        reader = xif.createXMLEventReader(streamsource.getInputStream());
        return reader;
    }

    //get category
    public static Category parseCategory(String content)
            throws XMLStreamException, SQLException, NamingException {
        XMLEventReader reader = getReader(content);
        Iterator<XMLEvent> iterator = autoAddMissingEndtag(reader);
        XMLEvent event = null;

//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
        while (iterator.hasNext()) {
            event = iterator.next();
            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();
                if (seQName.equals("h2")) {
                    QName attrName = new QName("class");
                    Attribute attr = se.getAttributeByName(attrName);
                    if (attr != null) {
                        if (attr.getValue().equals("page_title")) {
                            event = iterator.next();
                            String category = event.asCharacters().getData().trim();
                            Category cate = new Category();
                            cate.setCategoryName(category);
                            return cate;
                        }
                    }
                }
            }
        }
        return null;
    }

    //get list of product
    public static List<Product> parseListProduct(String content)
            throws XMLStreamException {
        XMLEventReader reader = getReader(content);
        Iterator<XMLEvent> iterator = autoAddMissingEndtag(reader);
        XMLEvent event = null;
        
        String productName = "";
        String description = "";
        String imgURL = "";
        double price = 0;
        int categoryID = 0;

        while (iterator.hasNext()) {
            event = iterator.next();
            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
                String seQName = se.getName().getLocalPart();
                if (seQName.equals("a") && (productName == null || productName.isEmpty())) {
                    Iterator<Attribute> attrIterator = se.getAttributes();
                    while (attrIterator.hasNext()) {
                        Attribute attribute = attrIterator.next();
                        if (attribute.getName().getLocalPart().equals("title")) {  //get product name
                            productName = attribute.getValue().trim();
                            System.out.println(productName);
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

}
