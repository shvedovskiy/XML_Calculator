package ru.shvedov.calculator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSchemaFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reading, parsing and writing data to XML files.
 */
public class Parser {
    private final String xmlPath;

    /**
     * Initialization XML file path from user's input.
     * @param path
     *        The full path to executing directory.
     * @param xmlName
     *        The name of the XML source file, received from user.
     */
    public Parser(String path, String xmlName) {
        xmlPath = path + File.separatorChar + xmlName;
        validate(xmlPath, path + File.separatorChar + "Calculator.xsd");
    }

    /**
     * Check the source XML file correctness.
     * @param xmlPath
     *        The full path to the XML file.
     * @param xsdPath
     *        The full path to the XML Schema file.
     * @return Boolean value is showing validating process success.
     */
    private boolean validate(String xmlPath, String xsdPath) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdPath));
            XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
            SAXBuilder sb = new SAXBuilder(factory);
            Document doc = sb.build(new File(xmlPath));
        } catch (IOException e) {
            e.getMessage();
        } catch(SAXException | JDOMException e) {
            System.err.println("XML file does not correspond the Schema");
            e.getMessage();
            System.exit(-1);
        }
        return true;
    }

    /**
     * Recursive pass on all the children of the root element and generation
     * of expression string.
     * @param elt
     *        A subtree root node.
     * @param prefixExpr
     *        Current prefix expression string.
     * @return Complete prefix expression string.
     */
    private String checkChildren(Element elt, String prefixExpr) {
        List<Element> children = elt.getChildren();
        for (Element child : children) {
            String childName = child.getName();
            if (childName.equals("operation") ||
                    childName.equals("operation1") ||
                    childName.equals("operation2")) {
                String operator = child.getAttributeValue("OperationType");
                prefixExpr = checkChildren(child, prefixExpr + operator + " ");
            } else if (childName.equals("arg") ||
                    childName.equals("arg1") ||
                    childName.equals("arg2")) {
                prefixExpr += (child.getText() + " ");
            }
        }
        return prefixExpr;
    }

    /**
     * Obtaining prefix expression from an XML source file.
     * @return List of expressions in the file as strings.
     */
    public List<String> parse() {
        SAXBuilder jdomBuilder = new SAXBuilder();
        File xmlFile = new File(xmlPath);
        List<String> expressionsLst = new LinkedList<>();

        try {
            Document document = jdomBuilder.build(xmlFile);
            Element root = document.getRootElement();
            Element expressions = root.getChild("expressions");
            List<Element> expressionLst = expressions.getChildren("expression");

            for (Element expression : expressionLst) {
                String prefixExpr = checkChildren(expression, "");
                expressionsLst.add(prefixExpr);
            }
        } catch (JDOMException | IOException e) {
            System.err.println("Problems while parsing XML file!");
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
            e.getMessage();
            System.exit(-1);
        }
        return expressionsLst;
    }

    /**
     * Write results of all expressions to output XML file.
     * @param results
     *        A list with results for any expression in source file.
     * @param path
     *        Full path to output XML file named Result.xml
     */
    public void write(List<Double> results, String path){
            Element root = new Element("simpleCalculator");
            Element expressionResultsElem = new Element("expressionResults");
            for (double result : results) {
                Element resultElem = new Element("result").setText(result + "");
                Element expressionResultElem = new Element("expressionResult");
                expressionResultElem.addContent(resultElem);
                expressionResultsElem.addContent(expressionResultElem);
            }
            root.addContent(expressionResultsElem);
        try {
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            Document document = new Document(root);
            xmlOutput.output(document, new FileWriter(path + File.separatorChar + "Result.xml"));
        } catch(IOException e) {
            System.err.println("Cannot write to output file!");
            e.getMessage();
            System.exit(-1);
        }
        validate(path + File.separatorChar + "Result.xml", path + File.separatorChar + "Calculator.xsd");
    }
}
