package net.monsterdev.heimdallr.utils;

import net.monsterdev.heimdallr.exceptions.XmlException;
import net.monsterdev.heimdallr.model.FilterRule;
import org.w3c.dom.Document;
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

public class FilterReader {
    private Document document;

    public FilterReader(String xml) throws XmlException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlException(e.getMessage());
        }
    }

    public List<FilterRule> getFilterRules() throws XmlException {
        List<FilterRule> rules = new ArrayList<>();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile("rules/rule");
            NodeList ruleNodes = (NodeList)xPathExpression.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new XmlException("Internal xml parser error");
        }
        return rules;
    }
}
