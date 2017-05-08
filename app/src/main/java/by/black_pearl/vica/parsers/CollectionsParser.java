package by.black_pearl.vica.parsers;

import android.support.annotation.Nullable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import by.black_pearl.vica.realm_db.CollectionsDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class CollectionsParser extends MilavitsaXmlParser {

    private Realm mRealm;
    private RealmList<CollectionsDb> mCollections;
    private int mCollectionsNextId = 0;

    public CollectionsParser(MilavitsaXmlParser.ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.mRealm = Realm.getDefaultInstance();
        this.mRealm.beginTransaction();
        this.mCollections = new RealmList<>();
        calculateNewIds();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        mRealm.copyToRealm(mCollections);
        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    @Override
    protected void processAttributes(String qName, @Nullable Attributes attributes) {
        super.processAttributes(qName, attributes);
        if(attributes.getValue("name") != null) {
            if (mRealm.where(CollectionsDb.class).equalTo(CollectionsDb.COLUMN_NAME, attributes.getValue("name"))
                    .findFirst() != null) {
                return;
            }
        }
        else {
            return;
        }
        CollectionsDb db = mRealm.createObject(CollectionsDb.class);
        db.setId(mCollectionsNextId++);
        for(int i = 0; i < attributes.getLength(); i++) {
            switch (attributes.getLocalName(i)) {
                case "name":
                    db.setName(attributes.getValue(i));
                    break;
                case "image":
                    db.setImage(attributes.getValue(i));
                    break;
                case "menuId":
                    db.setMneuId(Integer.parseInt(attributes.getValue(i)));
                    break;
            }
        }
        this.mCollections.add(db);
    }

    private void calculateNewIds() {
        if(mRealm.where(CollectionsDb.class).count() != 0) {
            this.mCollectionsNextId = mRealm.where(CollectionsDb.class).max(CollectionsDb.COLUMN_ID).intValue() + 1;
        }
    }
}
