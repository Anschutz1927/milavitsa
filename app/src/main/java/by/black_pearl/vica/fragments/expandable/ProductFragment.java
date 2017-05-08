package by.black_pearl.vica.fragments.expandable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.black_pearl.vica.R;
import by.black_pearl.vica.activities.FullscreenImageActivity;
import by.black_pearl.vica.activities.TempSettingsActivity;
import by.black_pearl.vica.realm_db.ProductDb;
import by.black_pearl.vica.realm_db.ProductsParamsDb;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    private final static String sNotificSizes = "Размеры";
    private final static String sNotificColors = "Цвет";

    private static final String ID = "mId";
    private ProductDb mProduct;

    public ProductFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragment.
     */
    public static ProductFragment newInstance(int id) {
        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        int mId = 0;
        if(getArguments() != null) {
            mId = getArguments().getInt(ID);
        }
        this.mProduct = realm.where(ProductDb.class).equalTo(ProductDb.COLUMN_ID, mId).findFirst();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_product_view, container, false);
        if (mProduct == null) {
            return view;
        }
        ((TextView) view.findViewById(R.id.view_product_view_descTextView))
                .setText("Описание: \n" + mProduct.getDescription());
        ImageView imageView = (ImageView) view.findViewById(R.id.view_product_view_imageView);
        final String image_url = "http://www.milavitsa.com/i/photo/" + mProduct.getImage();
        Glide.with(getContext()).load(image_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into(imageView);
        ImageView colorsView = (ImageView) view.findViewById(R.id.view_colors_view_imageView);
        String colors_url = "http://www.milavitsa.com/i/photo/" + mProduct.getCustom_matherial();
        Glide.with(getContext()).load(colors_url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .fitCenter().placeholder(android.R.drawable.ic_menu_camera)
                .crossFade().into(colorsView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FullscreenImageActivity.class)
                        .putExtra(FullscreenImageActivity.IMAGE_URL, image_url));
            }
        });
        ArrayAdapter sizeSpnrAdapter = getSizesAdapter(mProduct.getArticle());
        final ArrayAdapter colorSpnrAdapter = getColorAdapter(mProduct.getArticle());
        Spinner sizesSpnr = (Spinner) view.findViewById(R.id.spnr_size);
        sizesSpnr.setAdapter(sizeSpnrAdapter);
        Spinner colorSpnr = (Spinner) view.findViewById(R.id.spnr_color);
        colorSpnr.setAdapter(colorSpnrAdapter);
        String modelOrder = "Model: " + mProduct.getArticle() + "\n";
        /*Callback callback = new Callback() {
            @Override
            public void onCallback() {
                colorSpnr.setEnabled(false);
                colorSpnr.post(new Runnable() {
                    @Override
                    public void run() {
                        colorSpnrAdapter.set;
                        colorSpnr.setEnabled(true);
                    }
                });
            }
        };*/
        view.findViewById(R.id.fab_order).setOnClickListener(getOrderOnClockListener(sizesSpnr, colorSpnr, modelOrder));
        return view;
    }


    public ArrayAdapter getSizesAdapter(String article) {
        ArrayList<String> sizesList = ProductsParamsDb.getItemSizesList(Realm.getDefaultInstance(), Integer.parseInt(article));
        if (sizesList == null) {
            sizesList = new ArrayList<>();
        }
        sizesList.add(0, sNotificSizes);
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, sizesList);
    }

    private ArrayAdapter getColorAdapter(String article) {
        ArrayList<String> colorsList = ProductsParamsDb
                .getItemColorsList(Realm.getDefaultInstance(), Integer.parseInt(article));
        if (colorsList == null) {
            colorsList = new ArrayList<>();
        }
        colorsList.add(0, sNotificColors);
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, colorsList);
    }

    public View.OnClickListener getOrderOnClockListener(final Spinner sizesSpnr,
                                                        final Spinner colorSpnr, final String modelOrder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                String mailTo = prefs.getString(TempSettingsActivity.APP_EMAIL_KEY, null);
                if (mailTo == null) {
                    Toast.makeText(getContext().getApplicationContext(), "Не задан e-mail приема...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), TempSettingsActivity.class));
                    return;
                }
                if (sizesSpnr.getSelectedItem().toString().equals(sNotificSizes)) {
                    Toast.makeText(getContext().getApplicationContext(), "Не задан размер.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (colorSpnr.getSelectedItem().toString().equals(sNotificColors)) {
                    Toast.makeText(getContext().getApplicationContext(), "Не задан цвет.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String order = modelOrder + "Size: " + sizesSpnr.getSelectedItem().toString() + "\n" +
                        "Color: " + colorSpnr.getSelectedItem().toString() + "\n";
                emailDialog(mailTo, order);
            }
        };
    }

    private void emailDialog(String mailTo, String order) {
        AlertDialog.Builder orderDialogBuilder = new AlertDialog.Builder(getContext());
        View orderDialogView = LayoutInflater.from(getContext()).inflate(R.layout.view_order, null, false);
        orderDialogBuilder.setTitle("Заказ")
                .setView(orderDialogView)
                .setPositiveButton("Отправить", getPosBtnListener(
                        (EditText) orderDialogView.findViewById(R.id.et_name),
                        (EditText) orderDialogView.findViewById(R.id.et_phone),
                        mailTo, order))
                .setNegativeButton("Отмена", getNegBtnListener())
                .setCancelable(true);
        AlertDialog orderDialog = orderDialogBuilder.create();
        orderDialog.show();
    }

    public DialogInterface.OnClickListener getPosBtnListener(final EditText etName, final EditText etPhone,
                                                             final String mailTo, final String order) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                String message =
                        "Time: " + simpleDate.format(new Date(System.currentTimeMillis())) + "\n" +
                        "Name: " + etName.getText() + "\n" +
                        "Phone: " + etPhone.getText() + "\n" + order;
                Backendless.Messaging.sendTextEmail("Milavitsa new order" + System.currentTimeMillis(),
                        message, mailTo, getResponderCallback());
            }
        };
    }

    private DialogInterface.OnClickListener getNegBtnListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
    }

    private AsyncCallback<MessageStatus> getResponderCallback() {
        return new AsyncCallback<MessageStatus>() {
            @Override
            public void handleResponse(MessageStatus response) {
                Toast.makeText(getContext(), "Заявка отправлена!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getContext(), "Ошибка, заявка не отправлена!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private class SpinnerDropDownAdapter extends ArrayAdapter<String> {

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        public SpinnerDropDownAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }
    }

    public interface Callback {
        void onCallback();
    }
}
