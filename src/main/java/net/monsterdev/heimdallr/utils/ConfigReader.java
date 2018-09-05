package net.monsterdev.heimdallr.utils;

import net.monsterdev.heimdallr.model.TCPSlot;
import net.monsterdev.heimdallr.model.UDPSlot;
import net.monsterdev.heimdallr.exceptions.XmlException;
import net.monsterdev.heimdallr.model.NetworkSlot;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private Document document;

    public ConfigReader(String xml) throws XmlException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlException(e.getMessage());
        }
    }

    private List<NetworkSlot> getNetworkSlots(NodeList slotNodes) throws XmlException {
        List<NetworkSlot> slots = new ArrayList<>();

        try {
            for (int nItem = 0; nItem < slotNodes.getLength(); nItem++) {
                Node node = slotNodes.item(nItem);
                NamedNodeMap attributes = node.getAttributes();
                String addr = attributes.getNamedItem("ip").getNodeValue();
                Integer port = Integer.parseInt(attributes.getNamedItem("port").getNodeValue());
                String protocol = attributes.getNamedItem("protocol").getNodeValue();
                switch (protocol.toUpperCase()) {
                    case "TCP":
                        slots.add(new TCPSlot(addr, port));
                        break;
                    case "UDP":
                        slots.add(new UDPSlot(addr, port));
                        break;
                    default:
                        throw new Exception("Unknown protocol type");
                }
            }
        } catch (Exception e) {
            throw new XmlException(e.getMessage());
        }
        return slots;
    }

    public List<NetworkSlot> getInNetworkSlots() throws XmlException {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile("Settings/in-slots/slot");
            NodeList slotNodes = (NodeList)xPathExpression.evaluate(document, XPathConstants.NODESET);
            return getNetworkSlots(slotNodes);
        } catch (Exception e) {
            throw new XmlException("Internal xml parser error: " + e.getMessage());
        }
    }

    public List<NetworkSlot> getOutNetworkSlots() throws XmlException {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile("Settings/out-slots/slot");
            NodeList slotNodes = (NodeList)xPathExpression.evaluate(document, XPathConstants.NODESET);
            return getNetworkSlots(slotNodes);
        } catch (XPathExpressionException e) {
            throw new XmlException("Internal xml parser error: " + e.getMessage());
        }
    }
}
