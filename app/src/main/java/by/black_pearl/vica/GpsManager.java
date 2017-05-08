package by.black_pearl.vica;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;

/**
 * Created by BLACK_Pearl.
 */

public class GpsManager implements LocationListener {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    private final LocationManager mLocationManager;
    private final Fragment mFragment;

    private MapView mMapView;
    private boolean mIsGpsStatusOk = false;
    private Marker mMyLocationMarker;

    public GpsManager(Fragment fragment) {
        mFragment = fragment;
        mLocationManager = (LocationManager) mFragment.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    public void enableMap(MapView mapView) {
        if (ActivityCompat.checkSelfPermission(mFragment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mFragment.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mFragment.getContext(), "Нет доступа к местоположению или права на использование " +
                    "местоположения не предоставлены!", Toast.LENGTH_SHORT).show();
            mFragment.requestPermissions(new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    PERMISSIONS_REQUEST_CODE);
            return;
        }
        this.mMapView = mapView;
        addMapCompass();
        mMyLocationMarker = new Marker(mMapView);
        Drawable myDrawable = ContextCompat.getDrawable(mFragment.getContext().getApplicationContext(), R.drawable.person);
        mMyLocationMarker.setIcon(myDrawable);
        mMyLocationMarker.setTitle("Я здесь!");
        if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            this.mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 0, this);
        }
        if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 15, this);
        }
        if (mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            GeoPoint geoPoint = new GeoPoint(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            mMyLocationMarker.setPosition(geoPoint);
            this.mMapView.getController().setCenter(geoPoint);
        }
        else if (mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            GeoPoint geoPoint = new GeoPoint(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            mMyLocationMarker.setPosition(geoPoint);
            this.mMapView.getController().setCenter(geoPoint);
        }
        else {
            this.mMapView.getController().setCenter(new GeoPoint(53.902563, 27.563162));
        }
        this.mMapView.getOverlays().add(mMyLocationMarker);
        this.mMapView.getController().setZoom(11);
    }

    public void disableMap() {
        this.mLocationManager.removeUpdates(this);
        this.mMapView = null;
    }

    private void addMapCompass() {
        CompassOverlay compassOverlay =
                new CompassOverlay(mFragment.getContext(), new InternalCompassOrientationProvider(mFragment.getContext()), mMapView);
        compassOverlay.enableCompass();
        mMapView.getOverlays().add(compassOverlay);
    }

    private void setMyLocationOnMap(Location location) {
        GeoPoint myGeoPoint = new GeoPoint(location);
        mMyLocationMarker.setPosition(myGeoPoint);
        mMapView.invalidate();
    }

    private void putShopsOnMap(Location location) {
        ArrayList<OverlayItem> shopsOverlayItems = new ArrayList<>();
        getThreeNearestShopsCoordinates(location);

/*
        String description = nearestShop.getAddress() + "\n" + nearestShop.getTimeWork() + "\n" + nearestShop.getPhone();
        shopsOverlayItems.add(new OverlayItem(nearestShop.getName(), description,
                new GeoPoint(nearestcCoordinates.getLatitude(), nearestcCoordinates.getLongitude())));
        ItemizedOverlayWithFocus<OverlayItem> overlay = new ItemizedOverlayWithFocus<>(
                getContext(), shopsOverlayItems,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });
        overlay.setFocusItemsOnTap(true);
        mMap.getOverlays().add(overlay);*/
    }

    private void getThreeNearestShopsCoordinates(Location location) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER) && mIsGpsStatusOk) {
            return;
        }
        setMyLocationOnMap(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            this.mIsGpsStatusOk = status == LocationProvider.AVAILABLE;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            this.mIsGpsStatusOk = false;
        }
    }
}
