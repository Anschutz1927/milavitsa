package by.black_pearl.vica.fragments.addresses;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import by.black_pearl.vica.GpsManager;
import by.black_pearl.vica.MapView;
import by.black_pearl.vica.R;
import by.black_pearl.vica.realm_db.ShopsCoordinatesDb;
import by.black_pearl.vica.realm_db.ShopsParamsDb;
import io.realm.Realm;

public class ShopsMapFragment extends Fragment {

    private static final double ZOOM_DELTA = 0.0007;

    private FrameLayout mProgressFl;
    private MapView mMap;
    private GpsManager mGpsManager;

    public ShopsMapFragment() {
    }

    public static ShopsMapFragment newInstance() {
        return new ShopsMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mGpsManager = new GpsManager(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops_map, container, false);
        mProgressFl = (FrameLayout) view.findViewById(R.id.fl_progress);
        mMap = (MapView) view.findViewById(R.id.mv_map);
        mProgressFl.setVisibility(View.VISIBLE);
        mProgressFl.setVisibility(View.GONE);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setBuiltInZoomControls(true);
        mMap.setMultiTouchControls(true);
        mGpsManager.enableMap(mMap);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getActivity().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getActivity().getApplicationContext(),
               PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
    }

    @Override
    public void onDestroyView() {
        mGpsManager.disableMap();
        super.onDestroyView();
    }

    private void setupMap() {
        IMapController mapController = mMap.getController();
        mapController.setZoom(5);
        GeoPoint startPoint = new GeoPoint(53.902257, 27.561831);
        mapController.setCenter(startPoint);
        mMap.setMaxZoomLevel(11);
    }

    private void addMapMyLocation() {
        GpsMyLocationProvider myGpsProvider = new GpsMyLocationProvider(getContext());
        myGpsProvider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(myGpsProvider, mMap);
        locationOverlay.enableMyLocation();
        mMap.getOverlays().add(locationOverlay);
    }

    private ShopsCoordinatesDb getNearestCoordinates(Location location) {
        Realm realm = Realm.getDefaultInstance();
        ShopsCoordinatesDb nearestCoordinates = null;
        GeoPoint mylocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        float distance = Float.MAX_VALUE;
        for (ShopsCoordinatesDb shopsCoordinateDb : realm.where(ShopsCoordinatesDb.class).findAll()) {
            GeoPoint shopLocation = new GeoPoint(shopsCoordinateDb.getLatitude(), shopsCoordinateDb.getLongitude());
            if (distance > mylocation.distanceTo(shopLocation)) {
                distance = mylocation.distanceTo(shopLocation);
                nearestCoordinates = shopsCoordinateDb;
            }
        }
        if (nearestCoordinates != null) {
            return nearestCoordinates;
        }
        return null;
    }

    private ShopsParamsDb getNearestShop(int id) {
        return Realm.getDefaultInstance().where(ShopsParamsDb.class).equalTo(ShopsParamsDb.COLUMN_ID, id).findFirst();
    }

    private void setAutoZoom(Location location, ShopsCoordinatesDb nearestcCoordinates) {
        double north = getNorth(location, nearestcCoordinates) * (1 + ZOOM_DELTA);
        double south = getSouth(location, nearestcCoordinates) * (1 - ZOOM_DELTA);
        double west = getWest(location, nearestcCoordinates) * (1 - ZOOM_DELTA);
        double east = getEast(location, nearestcCoordinates) * (1 + ZOOM_DELTA);
        BoundingBox box = new BoundingBox(north, east, south, west);
        mMap.zoomToBoundingBox(box, true);
    }

    public double getNorth(Location location, ShopsCoordinatesDb nearestcCoordinates) {
        if (location.getLatitude() > nearestcCoordinates.getLatitude()) {
            return location.getLatitude();
        }
        return nearestcCoordinates.getLatitude();
    }

    public double getSouth(Location location, ShopsCoordinatesDb nearestcCoordinates) {
        if (location.getLatitude() < nearestcCoordinates.getLatitude()) {
            return location.getLatitude();
        }
        return nearestcCoordinates.getLatitude();
    }

    public double getWest(Location location, ShopsCoordinatesDb nearestcCoordinates) {
        if (location.getLongitude() < nearestcCoordinates.getLongitude()) {
            return location.getLongitude();
        }
        return nearestcCoordinates.getLongitude();
    }

    public double getEast(Location location, ShopsCoordinatesDb nearestcCoordinates) {
        if (location.getLongitude() > nearestcCoordinates.getLongitude()) {
            return location.getLongitude();
        }
        return nearestcCoordinates.getLongitude();
    }
}
