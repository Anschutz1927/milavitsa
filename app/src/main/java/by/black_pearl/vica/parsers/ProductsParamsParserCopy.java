package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import by.black_pearl.vica.realm_db.ColorsDb;
import by.black_pearl.vica.realm_db.ConstructionTypesDb;
import by.black_pearl.vica.realm_db.ConstructionsDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductsParamsDb;
import by.black_pearl.vica.realm_db.SizesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BlackPearl-net.
 */

public class ProductsParamsParserCopy extends MilavitsaXmlParser {
    private Realm mRealm;
    private RealmList<ProductsParamsDb> mProductsParamsDbs;
    private int mSizeNextId = 0;
    private int mColorNextId = 0;
    private int mConstructionNextId = 0;
    private int mConstructionTypeNextId = 0;
    private ProductsParamsDb mProductParam;

    public ProductsParamsParserCopy(MilavitsaXmlParser.ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.mRealm = Realm.getDefaultInstance();
        this.mRealm.beginTransaction();
        mProductsParamsDbs = new RealmList<>();
        calculateNewIds();
        setTagEnabled();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        parseOpenTag(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        parseCloseTag(qName);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        mRealm.copyToRealm(mProductsParamsDbs);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    protected void processAttributes(String qName, Attributes attributes) {
        super.processAttributes(qName, attributes);
        if (qName.equals("data") && attributes.getValue("Model") != null) {
            if (mRealm.where(ProductDb.class).equalTo(ProductDb.COLUMN_ARTICLE, attributes.getValue("Model")).findFirst() == null) {
                setTagDisabled();
                return;
            }
        }
        else {
            return;
        }
        mProductParam = new ProductsParamsDb();
        for (int i = 0; i < attributes.getLength(); i++) {
            int id;
            switch (attributes.getLocalName(i)) {
                case "Size":
                    if (mRealm.where(SizesDb.class)
                            .equalTo(SizesDb.COLUMN_SIZE, attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(SizesDb.class)
                                .equalTo(SizesDb.COLUMN_SIZE, attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        SizesDb db = mRealm.createObject(SizesDb.class);
                        db.setId(mSizeNextId++);
                        db.setSizeParam(attributes.getValue(i));
                        id = db.getId();
                    }
                    mProductParam.setSizeId(id);
                    break;
                case "Color":
                    if (mRealm.where(ColorsDb.class)
                            .equalTo(ColorsDb.COLUMN_COLOR, attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ColorsDb.class)
                                .equalTo(ColorsDb.COLUMN_COLOR, attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ColorsDb db = mRealm.createObject(ColorsDb.class);
                        db.setId(mColorNextId++);
                        db.setColor(attributes.getValue(i));
                        id = db.getId();
                    }
                    mProductParam.setColorId(id);
                    break;
                case "Model":
                    mProductParam.setModel(Integer.parseInt(attributes.getValue(i)));
                    break;
                case "Konstrukcia":
                    if (mRealm.where(ConstructionsDb.class)
                            .equalTo(ConstructionsDb.COLUMN_CONSTRUCTION, attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ConstructionsDb.class)
                                .equalTo(ConstructionsDb.COLUMN_CONSTRUCTION, attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ConstructionsDb db = mRealm.createObject(ConstructionsDb.class);
                        db.setId(mConstructionNextId++);
                        db.setConstructionParam(attributes.getValue(i));
                        id = db.getId();
                    }
                    mProductParam.setConstruction_id(id);
                    break;
                case "Vid_konstrukcii":
                    if (mRealm.where(ConstructionTypesDb.class)
                            .equalTo(ConstructionTypesDb.COLUMN_CONSTRUCTION_TYPE, attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ConstructionTypesDb.class)
                                .equalTo(ConstructionTypesDb.COLUMN_CONSTRUCTION_TYPE, attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ConstructionTypesDb db = mRealm.createObject(ConstructionTypesDb.class);
                        db.setId(mConstructionTypeNextId++);
                        db.setConstructionTypeParam(attributes.getValue(i));
                        id = db.getId();
                    }
                    mProductParam.setConstructionTypeId(id);
                    break;
            }
        }
    }

    private void parseOpenTag(String qName) {

    }

    private void parseCloseTag(String qName) {
        if (!qName.equals("data")) {
            return;
        }
        if (!isTagDisabled()) {
            this.mProductsParamsDbs.add(mProductParam);
        }
        else {
            setTagEnabled();
        }
    }

    private void calculateNewIds() {
        if(mRealm.where(SizesDb.class).count() != 0) {
            mSizeNextId = mRealm.where(SizesDb.class).max(SizesDb.COLUMN_ID).intValue() + 1;
        }
        if(mRealm.where(SizesDb.class).count() != 0) {
            mColorNextId = mRealm.where(ColorsDb.class).max(ColorsDb.COLUMN_ID).intValue() + 1;
        }
        if(mRealm.where(SizesDb.class).count() != 0) {
            mConstructionNextId = mRealm.where(ConstructionsDb.class).max(ConstructionsDb.COLUMN_ID).intValue() + 1;
        }
        if(mRealm.where(SizesDb.class).count() != 0) {
            mConstructionTypeNextId = mRealm.where(ConstructionTypesDb.class).max(ConstructionTypesDb.COLUMN_ID).intValue() + 1;
        }
    }
}
