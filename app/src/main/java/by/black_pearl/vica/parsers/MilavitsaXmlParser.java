package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by BLACK_Pearl.
 */

public abstract class MilavitsaXmlParser extends DefaultHandler {
    private final ParserCallback mCallback;
    private boolean mIsDisabled = false;
    private boolean mIsReading = false;

    MilavitsaXmlParser(ParserCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void startDocument() throws SAXException {
        mCallback.onStartDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        mCallback.onStartElement(qName);
        if(attributes != null && attributes.getLength() != 0) {
            processAttributes(qName, attributes);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length);
        mCallback.onCharacters(chars);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mCallback.onEndElement(qName);
    }

    @Override
    public void endDocument() throws SAXException {
        mCallback.onEndDocument();
    }

    protected void processAttributes(String qName, Attributes attributes) {
        mCallback.onAttributes(attributes);
    }

    public static void parseXml(InputSource source, DefaultHandler defaultHandler) {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(defaultHandler);
            reader.parse(source);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disable = true;
     */
    protected void setTagDisabled() {
        this.mIsDisabled = true;
    }

    /**
     * Disabled = false;
     */
    protected void setTagEnabled() {
        this.mIsDisabled = false;
    }

    protected boolean isTagDisabled() {
        return this.mIsDisabled;
    }

    /**
     * isReading = false;
     */
    protected void setTagUnreadable() {
        this.mIsReading = false;
    }

    /**
     * isReading = true;
     */
    protected void setTagReadable() {
        this.mIsReading = true;
    }

    protected boolean isTagReadable() {
        return this.mIsReading;
    }

    public interface ParserCallback {
        void onStartDocument();
        void onStartElement(String qName);
        void onAttributes(Attributes attributes);
        void onCharacters(String chars);
        void onEndElement(String qName);
        void onEndDocument();
    }
}
