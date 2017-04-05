package by.black_pearl.vica.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import by.black_pearl.vica.realm_db.ConstructionTypesDb;
import by.black_pearl.vica.realm_db.ConstructionsDb;
import by.black_pearl.vica.realm_db.ModelIrrhDb;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.SizesDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class ModelIrrhParser extends DefaultHandler {

    private final IrrhCallback mCallback;
    private RealmList<ModelIrrhDb> mModelIrrhs;
    private ModelIrrhDb mModelIrrh;
    private Realm mRealm;
    private boolean mItemIsExist = true;
    private RealmList<SizesDb> mSizes;
    private RealmList<ConstructionsDb> mConstructions;
    private RealmList<ConstructionTypesDb> mConstructionsType;

    public ModelIrrhParser(IrrhCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void startDocument() throws SAXException {
        mCallback.onStartDocument();
        this.mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        this.mModelIrrhs = new RealmList<>();
        this.mSizes = new RealmList<>();
        this.mConstructions = new RealmList<>();
        this.mConstructionsType = new RealmList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        mCallback.onStartElement(qName);
        if (!qName.equals("data")) {
            return;
        }
        this.mModelIrrh = mRealm.createObject(ModelIrrhDb.class);
        mItemIsExist = true;
        if(attributes != null && attributes.getLength() != 0) {
            mCallback.onAttributes(attributes);
            processAttributes(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mCallback.onEndElement(qName);
        if (!qName.equals("data")) {
            return;
        }
        if (mItemIsExist) {
            this.mModelIrrhs.add(mModelIrrh);
        }
        else {
            mItemIsExist = true;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        mCallback.onEndDocument();
        mRealm.copyToRealm(mModelIrrhs);
        mRealm.copyToRealm(mSizes);
        mRealm.copyToRealm(mConstructions);
        mRealm.copyToRealm(mConstructionsType);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length);
        mCallback.onCharacters(chars);
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
            switch (attributes.getLocalName(i)) {
                case "Size":
                    mModelIrrh.setSize(attributes.getValue(i));
                    if (mRealm.where(SizesDb.class).equalTo("size", attributes.getValue(i)).findFirst() == null) {
                        mSizes.add(mRealm.createObject(SizesDb.class).setSizeParam(attributes.getValue(i)));
                    }
                    break;
                case "Model":
                    mModelIrrh.setModel(Integer.parseInt(attributes.getValue(i)));
                    break;
                case "Konstrukcia":
                    mModelIrrh.setConstruction(attributes.getValue(i));
                    if (mRealm.where(ConstructionsDb.class).equalTo("construction", attributes.getValue(i)).findFirst() == null) {
                        mConstructions.add(mRealm.createObject(ConstructionsDb.class).setConstructionParam(attributes.getValue(i)));
                    }
                    break;
                case "Vid_konstrukcii":
                    mModelIrrh.setConstructionType(attributes.getValue(i));
                    if (mRealm.where(ConstructionTypesDb.class)
                            .equalTo("constructionType", attributes.getValue(i)).findFirst() == null) {
                        mConstructionsType.add(mRealm.createObject(ConstructionTypesDb.class)
                                .setConstructionTypeParam(attributes.getValue(i)));
                    }
                    break;
            }
        }
    }

    public interface IrrhCallback {
        void onStartDocument();
        void onStartElement(String qName);
        void onAttributes(Attributes attributes);
        void onCharacters(String chars);
        void onEndElement(String qName);
        void onEndDocument();
    }
}
