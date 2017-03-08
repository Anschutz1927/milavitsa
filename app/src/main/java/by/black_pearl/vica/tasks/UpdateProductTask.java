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
import by.black_pearl.vica.objects.Product;

/**
 * Created by BLACK_Pearl.
 */

public class UpdateProductTask extends AsyncTask<Void, Void, ArrayList<Product>> {
    private final TextView mParentTextView;
    private final Context mContext;
    private final UpgradeFragment.TaskListener mTaskListener;

    //homerelax

    public UpdateProductTask(Context context, TextView textView, UpgradeFragment.TaskListener taskListener) {
        this.mParentTextView = textView;
        this.mContext = context;
        this.mTaskListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mParentTextView.setText("Загрузка продуктов...");
    }

    @Override
    protected ArrayList<Product> doInBackground(Void... params) {
        ArrayList<Product> productes = null;
        try {
            XmlPullParser parser = this.mContext.getResources().getXml(R.xml.cat_new_products);
            productes = new ArrayList<>();
            Product product = null;
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_TAG:
                        product = parseTag(parser, product);
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("cat_new_products") && product != null) {
                            productes.add(product);
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
        return productes;
    }

    @Override
    protected void onPostExecute(ArrayList<Product> products) {
        super.onPostExecute(products);
        this.mParentTextView.setText("Загрузка продуктов завершена.");
        if(products.size() > 0) {
            saveProducts(products);
        }
        else {
            startUpdateMenu();
        }
    }

    private void saveProducts(ArrayList<Product> productes) {
        new SaveProducts(mContext, productes, mParentTextView, mTaskListener).execute();
    }

    private void startUpdateMenu() {
        new UpdateMenuTask(this.mContext, this.mParentTextView, mTaskListener).execute();
    }

    private Product parseTag(XmlPullParser parser, Product product) {
        try {
            String parseName = parser.getName();
            parser.next();
            switch (parseName) {
                case "cat_new_products":
                    product = new Product();
                    break;
                case "id":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setIdProduct(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "id_rubric":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setIdRubric(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "article":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setArticle(parser.getText());
                    }
                    break;
                case "image":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setImage(parser.getText());
                    }
                    break;
                case "image_backward":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setImageBackward(parser.getText());
                    }
                    break;
                case "description":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setDescription(parser.getText());
                    }
                    break;
                case "sizes":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setSizes(parser.getText());
                    }
                    break;
                case "type":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setType(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "sort":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setSort(Integer.valueOf(parser.getText()));
                    }
                    break;
                case "custom_matherial":
                    if(parser.getEventType() == XmlPullParser.TEXT) {
                        product.setCustomMatherial(parser.getText());
                    }
                    break;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return product;
    }
}
