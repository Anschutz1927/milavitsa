package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import by.black_pearl.vica.realm_db.ColorModelSizeDb;
import by.black_pearl.vica.realm_db.ColorsDb;
import by.black_pearl.vica.realm_db.ConstructionTypesDb;
import by.black_pearl.vica.realm_db.ConstructionsDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.SizesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BlackPearl-net.
 */

public class ColorModelSizeParser extends DefaultHandler {
    private final ColorModelSizeCallback mCallback;
    private RealmList<ColorModelSizeDb> mColorModelSizes;
    private RealmList<SizesDb> mSizesDbs;
    private RealmList<ConstructionsDb> mConstructionsDbs;
    private RealmList<ConstructionTypesDb> mConstructionTypesDbs;
    private RealmList<ColorsDb> mColorsDbs;
    private Realm mRealm;
    private ColorModelSizeDb mColorModelSize;
    private boolean mItemIsExist = true;

    public ColorModelSizeParser(ColorModelSizeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void startDocument() throws SAXException {
        mCallback.onStartDocument();
        this.mRealm = Realm.getDefaultInstance();
        this.mRealm.beginTransaction();
        mColorModelSizes = new RealmList<>();
        mSizesDbs = new RealmList<>();
        mConstructionsDbs = new RealmList<>();
        mConstructionTypesDbs = new RealmList<>();
        mColorsDbs = new RealmList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        mCallback.onStartElement(qName);
        if (!qName.equals("data")) {
            return;
        }
        mColorModelSize = mRealm.createObject(ColorModelSizeDb.class);
        mItemIsExist = true;
        if(attributes != null && attributes.getLength() != 0) {
            mCallback.onAttributes(attributes);
            processAttributes(attributes);
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
        if (!qName.equals("data")) {
            return;
        }
        if (mItemIsExist) {
            this.mColorModelSizes.add(mColorModelSize);
        }
        else {
            mItemIsExist = true;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        mCallback.onEndDocument();
        mRealm.copyToRealm(mColorModelSizes);
        mRealm.copyToRealm(mSizesDbs);
        mRealm.copyToRealm(mColorsDbs);
        mRealm.copyToRealm(mConstructionsDbs);
        mRealm.copyToRealm(mConstructionTypesDbs);
        mRealm.commitTransaction();
        mRealm.close();
    }

    private void processAttributes(Attributes attributes) {
        if (attributes.getValue("Model") != null) {
            if (mRealm.where(ProductDb.class).equalTo("Article", attributes.getValue("Model")).findFirst() == null) {
                mItemIsExist = false;
                return;
            }
        }
        else {
            return;
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            int id;
            switch (attributes.getLocalName(i)) {
                case "Size":
                    if (mRealm.where(SizesDb.class)
                            .equalTo("size", attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(SizesDb.class)
                                .equalTo("size", attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        SizesDb db = mRealm.createObject(SizesDb.class);
                        db.setSizeParam(attributes.getValue(i));
                        mSizesDbs.add(db);
                        id = db.getId();
                    }
                    mColorModelSize.setSizeId(id);
                    break;
                case "Color":
                    if (mRealm.where(ColorsDb.class)
                            .equalTo("color", attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ColorsDb.class)
                                .equalTo("color", attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ColorsDb db = mRealm.createObject(ColorsDb.class);
                        db.setColor(attributes.getValue(i));
                        mColorsDbs.add(db);
                        id = db.getId();
                    }
                    mColorModelSize.setColorId(id);
                    break;
                case "Model":
                    mColorModelSize.setModel(Integer.parseInt(attributes.getValue(i)));
                    break;
                case "Konstrukcia":
                    if (mRealm.where(ConstructionsDb.class)
                            .equalTo("construction", attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ConstructionsDb.class)
                                .equalTo("construction", attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ConstructionsDb db = mRealm.createObject(ConstructionsDb.class);
                        db.setConstructionParam(attributes.getValue(i));
                        mConstructionsDbs.add(db);
                        id = db.getId();
                    }
                    mColorModelSize.setConstructionId(id);
                    break;
                case "Vid_konstrukcii":
                    if (mRealm.where(ConstructionTypesDb.class)
                            .equalTo("constructionType", attributes.getValue(i)).findFirst() != null) {
                        id = mRealm.where(ConstructionTypesDb.class)
                                .equalTo("constructionType", attributes.getValue(i)).findFirst().getId();
                    }
                    else  {
                        ConstructionTypesDb db = mRealm.createObject(ConstructionTypesDb.class);
                        db.setConstructionTypeParam(attributes.getValue(i));
                        mConstructionTypesDbs.add(db);
                        id = db.getId();
                    }
                    mColorModelSize.setConstructionTypeId(id);
                    break;
            }
        }
    }

    public interface ColorModelSizeCallback {
        void onStartDocument();
        void onStartElement(String qName);
        void onAttributes(Attributes attributes);
        void onCharacters(String chars);
        void onEndElement(String qName);
        void onEndDocument();
    }
}
