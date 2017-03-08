package by.black_pearl.vica.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import by.black_pearl.vica.R;
import by.black_pearl.vica.fragments.UpgradeFragment;
import by.black_pearl.vica.objects.ProductSeries;

/**
 * Created by BLACK_Pearl.
 */

public class UpdateMenuTask extends AsyncTask<Void, Void, ArrayList<ProductSeries>> {
    private final TextView mParentTextView;
    private final Context mContext;
    private final UpgradeFragment.TaskListener mTaskListener;

    //homerelax

    public UpdateMenuTask(Context context, TextView textView, UpgradeFragment.TaskListener taskListener) {
        this.mParentTextView = textView;
        this.mContext = context;
        this.mTaskListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mParentTextView.setText("Загрузка серий...");
    }

    @Override
    protected ArrayList<ProductSeries> doInBackground(Void... params) {
        ArrayList<ProductSeries> menus = null;
        try {
            XmlPullParser parser = this.mContext.getResources().getXml(R.xml.cat_new);
            menus = new ArrayList<>();
            ProductSeries menu = null;
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_TAG:
                        menu = parseTag(parser, menu);
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("cat_new") && menu != null) {
                            menus.add(menu);
                        }
                        parser.next();
                        break;
                    default:
                        parser.next();
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return menus;
    }

    @Override
    protected void onPostExecute(ArrayList<ProductSeries> menus) {
        super.onPostExecute(menus);
        this.mParentTextView.setText("Серии загружены.");
        saveProductMenu(menus);
    }

    private void saveProductMenu(ArrayList<ProductSeries> menus) {
        if(menus.size() >0) {
            new SaveProductMenu(this.mContext, menus, mParentTextView, mTaskListener).execute();
        }
        else {
            this.mTaskListener.replaceToCatalogFragment();
        }
    }

    private ProductSeries parseTag(XmlPullParser parser, ProductSeries menu) {
        try {
            String parseName = parser.getName();
            parser.next();
            switch (parseName) {
                case "cat_new":
                    menu = new ProductSeries();
                    break;
                case "id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setId(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "name":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setName(parser.getText());
                    }
                    break;
                case "image":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage(parser.getText());
                    }
                    break;
                case "description":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setDescription(parser.getText());
                    }
                    break;
                case "matherial":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setMatherial(parser.getText());
                    }
                    break;
                case "menu_id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setIdMenu(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "sort":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setSort(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "image_menu":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImageMenu(parser.getText());
                    }
                    break;
                case "image_2":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage2(parser.getText());
                    }
                    break;
                case "image_3":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        menu.setImage3(parser.getText());
                    }
                    break;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return menu;
    }
}
