package com.example.pointsofinterest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    MapView mv;
    double lat;
    double lon;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);

        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(51.43, -0.93));
        mv.getTileProvider().setTileSource(TileSourceFactory.MAPNIK);

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        //Setup for Location Manager
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Default Co-ordinates
        lat=51.43;
        lon=-0.93;

        Location location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location!=null)
        {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        //Center Location
        mv.getController().setCenter(new GeoPoint(lat, lon));
        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);

        OverlayItem mylocation = new OverlayItem("My Location", "Your Geographical Location", new GeoPoint(lat, lon));
        mylocation.setMarker(getResources().getDrawable(R.drawable.ulocation));
        items.addItem(mylocation);

        mv.getOverlays().add(items);

        //Setup Location Listener
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void onLocationChanged(Location newLoc)
    {
        lat = newLoc.getLatitude();
        lon = newLoc.getLongitude();

        //Center Location
        mv.getController().setCenter(new GeoPoint(lat, lon));

        OverlayItem mylocation = new OverlayItem("My Location", "Your Geographical Location", new GeoPoint(lat, lon));
        mylocation.setMarker(getResources().getDrawable(R.drawable.ulocation));

        //RemoveAllItems here deletes POI markers when placed --items.removeAllItems();--
        items.removeItem(mylocation);
        items.addItem(mylocation);

        Toast.makeText(this, "Location=" + newLoc.getLatitude() + " " + newLoc.getLongitude(), Toast.LENGTH_LONG).show();
    }

    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider + " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider + " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Toast.makeText(this, "Status Changed: " + status, Toast.LENGTH_LONG).show();
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.poientry)
        {
            Intent intent = new Intent(this, AddPoiActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        else if (item.getItemId() == R.id.poi_save)
        {
            FileIO.poiSave();
            Toast.makeText(this, "POI was saved to file", Toast.LENGTH_LONG).show();
            return true;
        }

        else if (item.getItemId() == R.id.poi_load)
        {
            FileIO.poiLoad();
            items.removeAllItems();

            for(PoiValues poi : FileIO.getPOIList())
            {
                String name_load = poi.getName();
                String type_load = poi.getType();
                String desc_load = poi.getDesc();

                double long_load = poi.getLongitude();
                double lat_load = poi.getLatitude();

                items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                OverlayItem newPoi = new OverlayItem(name_load, ("Type: "+ type_load +" | Description:"+ desc_load), new GeoPoint(lat_load, long_load));
                items.addItem(newPoi);
            }

            Toast.makeText(this, "POIs Loaded", Toast.LENGTH_LONG).show();
            return true;
        }

        else if (item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String poiName = extras.getString("poiName1");
                String poiType = extras.getString("poiType2");
                String poiDesc = extras.getString("poiDesc3");
                double poiLat = lat;
                double poiLon = lon;

                OverlayItem newPOI = new OverlayItem(poiName, "Type: " + poiType + " | Description:" + poiDesc, new GeoPoint(poiLat, poiLon));
                items.addItem(newPOI);
                mv.getOverlays().add(items);
                PoiValues newPoi = new PoiValues(poiName, poiType, poiDesc, poiLat, poiLon);
                FileIO.getPOIList().add(newPoi);
                FileIO.poiSave();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean poi_auto_upload = prefs.getBoolean("poi_auto_upload", true);

                if (poi_auto_upload)
                {

                }

            }
        }
    }
}
