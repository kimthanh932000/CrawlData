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
import java.util.ArrayList;
import java.util.Iterator;
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
import sample.jaxb.category.Category;
import sample.jaxb.product.ListProduct;

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
//                if (event != null) {
//                    if (event.isStartElement()) {
//                        System.out.println(event);
//                    }
//                }
            } catch (XMLStreamException exception) {
                String msg = exception.getMessage();
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
//        while(IEvents.iterator().hasNext()){
//            XMLEvent event = IEvents.iterator().next();
//            if(event.isStartElement()){
//                System.out.println(event.asStartElement().getName().getLocalPart());
//            }
//        }
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
    public static Category parseCategory(String content) throws XMLStreamException {
        XMLEventReader reader = getReader(content);
        Iterator<XMLEvent> iterator = autoAddMissingEndtag(reader);
        while (iterator.hasNext()) {
            XMLEvent event = iterator.next();
            if (event != null) {
                if (event.isStartElement()) {
                    StartElement se = event.asStartElement();
                    if (se.getName().getLocalPart().equals("h2")) {
                        Iterator<Attribute> attrs = se.getAttributes();
                        while(attrs.hasNext()){
                            Attribute attr = attrs.next();
                            if(attr.getName().getLocalPart().equals("page_title")){
                                Characters chars = se.asCharacters();
                                String category = chars.getData().trim();
                                System.out.println(category);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //get list of product
    public static ListProduct parseListProduct(String content) {
        return null;
    }

}
