package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductSeriesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class ProductsParser extends MilavitsaXmlParser {
    private Realm mRealm;
    private RealmList<ProductDb> mProductDbs;
    private ProductDb mProductDb;
    private boolean mIsDisabled = false;
    private boolean mIsReading = false;
    private String mString;

    public ProductsParser(ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mProductDbs = new RealmList<>();
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
        mRealm.copyToRealm(mProductDbs);
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
            case "cat_new_products":
                mProductDb = new ProductDb();
                break;
            case "id":
                mIsReading = true;
                break;
            case "id_rubric":
                mIsReading = true;
                break;
            case "article":
                mIsReading = true;
                break;
            case "image":
                mIsReading = true;
                break;
            case "image_backward":
                mIsReading = true;
                break;
            case "description":
                mIsReading = true;
                break;
            case "sizes":
                mIsReading = true;
                break;
            case "type":
                mIsReading = true;
                break;
            case "sort":
                mIsReading = true;
                break;
            case "custom_matherial":
                mIsReading = true;
                break;
        }
    }

    private void parseCloseTag(String qName) {
        if (mIsDisabled) {
            if (qName.equals("cat_new_products")) {
                mIsDisabled = false;
            }
            return;
        }
        switch (qName) {
            case "cat_new_products":
                if (mProductDb != null)
                    mProductDbs.add(mProductDb);
                break;
            case "id":
                mProductDb.setId(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "id_rubric":
                if (mRealm.where(ProductSeriesDb.class)
                        .equalTo(ProductSeriesDb.COLUMN_ID, Integer.parseInt(mString)).findFirst() == null) {
                    mIsDisabled = true;
                    return;
                }
                mProductDb.setId_rubric(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "article":
                mProductDb.setArticle(mString);
                mIsReading = false;
                break;
            case "image":
                mProductDb.setImage(mString);
                mIsReading = false;
                break;
            case "image_backward":
                mProductDb.setImage_backward(mString);
                mIsReading = false;
                break;
            case "description":
                mProductDb.setDescription(mString);
                mIsReading = false;
                break;
            case "sizes":
                mProductDb.setSizes(mString);
                mIsReading = false;
                break;
            case "type":
                mProductDb.setType(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "sort":
                mProductDb.setSort(Integer.parseInt(mString));
                mIsReading = false;
                break;
            case "custom_matherial":
                mProductDb.setCustom_matherial(mString);
                mIsReading = false;
                break;
        }
        mString = "";
    }
}
