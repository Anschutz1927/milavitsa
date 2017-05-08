package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import by.black_pearl.vica.realm_db.ShopsCountriesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class ShopsCountryParser extends MilavitsaXmlParser {
    private Realm mRealm;
    private RealmList<ShopsCountriesDb> mShopsCountriesDbs;
    private ShopsCountriesDb mShopsCountriesDb;
    private String mString = "";

    public ShopsCountryParser(ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        this.mShopsCountriesDbs = new RealmList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        parseOpenTag(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(!isTagDisabled() && isTagReadable()) {
            mString = new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        parseCloseTag(qName);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        mRealm.copyToRealm(mShopsCountriesDbs);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    protected void processAttributes(String qName, Attributes attributes) {
        super.processAttributes(qName, attributes);
    }

    private void parseOpenTag(String qName) {
        if (isTagDisabled()) {
            return;
        }
        switch (qName) {
            case "shops_rubric":
                mShopsCountriesDb = new ShopsCountriesDb();
                break;
            case "id":
                setTagReadable();
                break;
            case "parent_id":
                setTagReadable();
                break;
            case "name":
                setTagReadable();
                break;
        }
    }

    private void parseCloseTag(String qName) {
        if (isTagDisabled()) {
            if (qName.equals("shops_rubric")) {
                setTagEnabled();
            }
            return;
        }
        switch (qName) {
            case "shops_rubric":
                if (mShopsCountriesDb != null) {
                    mShopsCountriesDbs.add(mShopsCountriesDb);
                }
                break;
            case "id":
                mShopsCountriesDb.setId(Integer.parseInt(mString));
                setTagUnreadable();
                break;
            case "parent_id":
                mShopsCountriesDb.setParentId(Integer.parseInt(mString));
                setTagUnreadable();
                break;
            case "name":
                mShopsCountriesDb.setCountryName(mString);
                setTagUnreadable();
                break;
        }
    }

    @Override
    protected void setTagUnreadable() {
        super.setTagUnreadable();
        mString = "";
    }
}
