package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class SeriesParser extends MilavitsaXmlParser {
    private Realm mRealm;
    private RealmList<ProductSeriesDb> mProductSeriesDbs;
    private ProductSeriesDb mProductSeriesDb;
    private boolean mIsDisabled = false;
    private boolean mIsReading = false;
    private String mString;

    public SeriesParser(ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mProductSeriesDbs = new RealmList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        parseOpenTag(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(!mIsDisabled && mIsReading) {
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
        mRealm.copyToRealm(mProductSeriesDbs);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    protected void processAttributes(String qName, Attributes attributes) {
        super.processAttributes(qName, attributes);
    }

    private void parseOpenTag(String qName) {
        if (mIsDisabled) {
            return;
        }
        switch (qName) {
            case "cat_new":
                //mProductSeriesDb = mRealm.createObject(ProductSeriesDb.class);
                mProductSeriesDb = new ProductSeriesDb();
                break;
            case "disable":
                mIsDisabled = true;
                mIsReading = false;
                break;
            case "id":
                mIsReading = true;
                break;
            case "name":
                mIsReading = true;
                break;
            case "image":
                mIsReading = true;
                break;
            case "description":
                mIsReading = true;
                break;
            case "matherial":
                mIsReading = true;
                break;
            case "menu_id":
                mIsReading = true;
                break;
            case "sort":
                mIsReading = true;
                break;
            case "image_menu":
                mIsReading = true;
                break;
            case "image_2":
                mIsReading = true;
                break;
            case "image_3":
                mIsReading = true;
                break;

        }
    }

    private void parseCloseTag(String qName) {
        if (mIsDisabled) {
            if (qName.equals("cat_new")) {
                mIsDisabled = false;
            }
            return;
        }
        switch (qName) {
            case "cat_new":
                if (mProductSeriesDb != null)
                    mProductSeriesDbs.add(mProductSeriesDb);
                break;
            case "id":
                mProductSeriesDb.setId(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "name":
                mProductSeriesDb.setName(mString);
                mIsReading = false;
                break;
            case "image":
                mProductSeriesDb.setImage(mString);
                mIsReading = false;
                break;
            case "description":
                mProductSeriesDb.setDescription(mString);
                mIsReading = false;
                break;
            case "matherial":
                mProductSeriesDb.setMatherial(mString);
                mIsReading = false;
                break;
            case "menu_id":
                mProductSeriesDb.setId_menu(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "sort":
                mProductSeriesDb.setSort(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "image_menu":
                mProductSeriesDb.setImage_menu(mString);
                mIsReading = false;
                break;
            case "image_2":
                mProductSeriesDb.setImage2(mString);
                mIsReading = false;
                break;
            case "image_3":
                mProductSeriesDb.setImage3(mString);
                mIsReading = false;
                break;
        }
        mString = "";
    }
}
