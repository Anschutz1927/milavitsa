package by.black_pearl.vica.parsers;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

import by.black_pearl.vica.realm_db.ShopsParamsDb;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by BLACK_Pearl.
 */

public class ShopsParamsParser extends MilavitsaXmlParser {
    private static final String LOG_TAG = "ShopsParamsParser::";

    private Realm mRealm;
    private RealmList<ShopsParamsDb> mShopsParamsDbs;
    private ShopsParamsDb mShopsParamsDb;
    private String mString = "";
    private ParserState mState = ParserState.TAG_CLOSED;

    public ShopsParamsParser(ParserCallback callback) {
        super(callback);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mShopsParamsDbs = new RealmList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        parseOpenTag(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (!isTagDisabled() && isTagReadable() && mState == ParserState.TAG_OPENED) {
            mString = new String(ch, start, length);
            mState = ParserState.TAG_PARSED;
        }
        else if (mState == ParserState.TAG_PARSED) {
            mString += new String(ch, start, length);
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
        mRealm.copyToRealm(mShopsParamsDbs);
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
        mState = ParserState.TAG_OPENED;
        switch (qName) {
            case "shops":
                mShopsParamsDb = new ShopsParamsDb();
                break;
            case "id":
                setTagReadable();
                break;
            case "rubric_id":
                setTagReadable();
                break;
            case "name":
                setTagReadable();
                break;
            case "address":
                setTagReadable();
                break;
            case "time_work":
                setTagReadable();
                break;
            case "phone":
                setTagReadable();
                break;
        }
    }

    private void parseCloseTag(String qName) {
        if (isTagDisabled()) {
            if (qName.equals("shops")) {
                setTagEnabled();
            }
            return;
        }
        switch (qName) {
            case "shops":
                mShopsParamsDbs.add(mShopsParamsDb);
                //uploadToBackend(mShopsParamsDb);
                setTagUnreadable();
                break;
            case "id":
                mShopsParamsDb.setId(Integer.parseInt(mString));
                setTagUnreadable();
                break;
            case "rubric_id":
                mShopsParamsDb.setRubricId(Integer.parseInt(mString));
                setTagUnreadable();
                break;
            case "name":
                mShopsParamsDb.setName(mString);
                setTagUnreadable();
                break;
            case "address":
                mShopsParamsDb.setAddress(mString);
                setTagUnreadable();
                break;
            case "time_work":
                mShopsParamsDb.setTimeWork(mString);
                setTagUnreadable();
                break;
            case "phone":
                mShopsParamsDb.setPhone(mString);
                setTagUnreadable();
                break;
        }
        mState = ParserState.TAG_CLOSED;
    }

    @Override
    protected void setTagUnreadable() {
        super.setTagUnreadable();
        mString = "";
    }

    private void uploadToBackend(final ShopsParamsDb shopsParamsDb) {
        try {
            Backendless.Persistence.of("shop_params").save(createShopsParamsHashMap(shopsParamsDb), new AsyncCallback<Map>() {
                @Override
                public void handleResponse(Map response) {
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.i(LOG_TAG, "fault to save shop params: " + fault.getCode()+ " -> " + fault.getMessage() + ":");
                    Log.i(LOG_TAG, "Shop id " + shopsParamsDb.getId() + " was not uploaded.");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap createShopsParamsHashMap(ShopsParamsDb shopsParamsDb) {
        HashMap shop_params = new HashMap();
        shop_params.put("id", shopsParamsDb.getId());
        shop_params.put("rubric_id", shopsParamsDb.getRubricId());
        shop_params.put("name", shopsParamsDb.getName());
        shop_params.put("address", shopsParamsDb.getAddress());
        shop_params.put("phone", shopsParamsDb.getPhone());
        shop_params.put("time_work", shopsParamsDb.getTimeWork());
        return shop_params;
    }

    private enum ParserState {
        TAG_OPENED, TAG_PARSED, TAG_CLOSED
    }
}
