package by.black_pearl.vica.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import by.black_pearl.vica.R;
import by.black_pearl.vica.parsers.ColorModelSizeParser;
import by.black_pearl.vica.parsers.ModelIrrhParser;
import by.black_pearl.vica.realm_db.ColorModelSizeDb;

/**
 * Created by BLACK_Pearl.
 */

public class SaveModelIrrh extends AsyncTask <Void, String, Void> {

    private final TextView mTextView;
    private final Resources mResources;
    private SaveOstTask mSaveOstTask;
    private int mCount = 0;

    public SaveModelIrrh(Resources resources, TextView updateTextView) {
        this.mResources = resources;
        this.mTextView = updateTextView;
    }

    @Override
    protected void onPreExecute() {
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("Подготовка обработки параметров моделей...");
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        //ModelIrrhParser.IrrhCallback parserCallback = getParserCalback();
        //ModelIrrhParser modelIrrhParser = new ModelIrrhParser(parserCallback);
        ColorModelSizeParser.ColorModelSizeCallback parserCallback = getParserCalback();
        ColorModelSizeParser colorModelSizeParser = new ColorModelSizeParser(parserCallback);
        //InputSource source = new InputSource(mResources.openRawResource(R.raw.modelrrh1));
        InputSource source = new InputSource(mResources.openRawResource(R.raw.colormodelsize));
        //parseXml(source, modelIrrhParser);
        parseXml(source, colorModelSizeParser);
        //source = new InputSource(mResources.openRawResource(R.raw.modelrrh2));
        //parseXml(source, modelIrrhParser);
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        final String txt = values[0];
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(txt);
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("Обработка параметров моделей завершена.");
            }
        });
        mSaveOstTask = new SaveOstTask(mTextView);
        mSaveOstTask.execute();
    }

    public void cancel() {
        if(this.mSaveOstTask != null && this.mSaveOstTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.mSaveOstTask.cancel(true);
        }
        cancel(true);
    }

    private void parseXml(InputSource source, DefaultHandler defaultHandler) {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(defaultHandler);
            reader.parse(source);
        }
        catch (Exception e) {
            publishProgress("Ошибка обработки параметров моделей!");
            e.printStackTrace();
        }
    }

    public ColorModelSizeParser.ColorModelSizeCallback getParserCalback() {
        return new ColorModelSizeParser.ColorModelSizeCallback() {
            @Override
            public void onStartDocument() {
            }

            @Override
            public void onStartElement(String qName) {
            }

            @Override
            public void onAttributes(Attributes attributes) {
            }

            @Override
            public void onCharacters(String chars) {
            }

            @Override
            public void onEndElement(String qName) {
                if (qName.equals("data")) {
                    mCount++;
                    publishProgress("Обработано параметров: " + String.valueOf(mCount));
                }
            }

            @Override
            public void onEndDocument() {
            }
        };
    }
}
