package com.rmuti.cpe.rangsiman.busatkorat.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rmuti.cpe.rangsiman.busatkorat.Database.DatabaseHelper;
import com.rmuti.cpe.rangsiman.busatkorat.R;
import com.rmuti.cpe.rangsiman.busatkorat.adapter.DataListAdapter;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataBusStop;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataList;
import com.rmuti.cpe.rangsiman.busatkorat.utill.DataRoute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SearchFragment extends Fragment implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final int PLAY_SERVICES_RESOLUTION_REQUEST = 90000;


    private Calendar fromTime;
    private Calendar toTime;
    private Calendar currentTime;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Button btnSearch;
    private ImageButton btnStart;
    private ImageButton btnEnd;
    private AutoCompleteTextView completeTextViewStart;
    private Marker myMarker;
    private Marker EndMarker;

    private ListView listView_nieghborhood;
    private DataListAdapter listAdapter_nieghborhood;
    private ListView listView_result;
    private DataListAdapter listAdapter_result;

    private DatabaseHelper mDBHelper;
    private AutoCompleteTextView completeTextViewEnd;
    private String[] strBusStop_Thai;
    private List<DataBusStop> mDataListBusStop;
    private List<DataRoute> mDataRoute;
    private List<DataList> mDataBusList;
    private ArrayAdapter<String> adapter;

    private int position_adapter_start;
    private int position_adapter_end;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private double latitude = 0;
    private double longitude = 0;
    private double latitudeStart = 0;
    private double longitudeStart = 0;
    private double latitudeEnd = 0;
    private double longitudeEnd = 0;

    private GoogleApiClient googleApiClient;
    private Marker StartMarker;

    public SearchFragment() {
        super();
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean copyDatabase(Context context){
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBName);
            String outFileName = DatabaseHelper.DBLocation + DatabaseHelper.DBName;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff =  new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            Log.v("DataFragment","DB Copied");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        mGeoDataClient = Places.getGeoDataClient(getContext(),null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(),null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_search, container, false);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(final View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState

        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mDBHelper = new DatabaseHelper(getContext());

        File database = getActivity().getDatabasePath(DatabaseHelper.DBName);
        if(false == database.exists()){
            mDBHelper.getReadableDatabase();
            if (copyDatabase(getContext())){
                Toast.makeText(getContext(), "Copy Success", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Copy Fail", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mDataListBusStop = mDBHelper.getBusStopListData();
        mDataBusList = mDBHelper.getBusListData();
        mDataRoute = mDBHelper.getRouteListData();

        strBusStop_Thai = mDBHelper.getBusstopNameData();
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,strBusStop_Thai);

        completeTextViewEnd = (AutoCompleteTextView) rootView.findViewById(R.id.autoText_End);
        completeTextViewEnd.setAdapter(adapter);


        completeTextViewStart = (AutoCompleteTextView) rootView.findViewById(R.id.editText_Start);
        completeTextViewStart.setAdapter(adapter);



/////////////////////////////////////////////// Start Button //////////////////////////////////////////////////////
        btnStart = (ImageButton) rootView.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save Position Bus Stop


                try {
                    // Check in
                    if(completeTextViewStart.getText().toString().matches("")){
                        Location currentLocation = new Location("Current Location");
                        currentLocation.setLatitude(latitude);
                        currentLocation.setLongitude(longitude);
                        Location busstopLocation = new Location("Bus Stop");
                        double min_dist = 999999999;
                        double dist = 0;
                        int index_min_dist = 0;
                        for(int i=1; i<mDataListBusStop.size();i++){
                            busstopLocation.setLatitude(mDataListBusStop.get(i).getLatitude_busstop());
                            busstopLocation.setLongitude(mDataListBusStop.get(i).getLongtitude_busstop());
                            dist = currentLocation.distanceTo(busstopLocation);
                            if(dist < min_dist){
                                min_dist = dist;
                                index_min_dist = i;
                            }
                        }
                        completeTextViewStart.setText(mDataListBusStop.get(index_min_dist).getName_busstop());
                    }



                    position_adapter_start = 0;
                    if (!completeTextViewStart.getText().equals(null) && !completeTextViewEnd.getText().equals(null)) {

                        if (!completeTextViewStart.getText().equals(null)) {
                            String strCopy = completeTextViewStart.getText().toString();
                            int index = 1;
                            for (int i = 0; i < strBusStop_Thai.length; i++) {
                                if (strCopy.equals(strBusStop_Thai[i].toString())) {
                                    position_adapter_start = i + 1;
                                } else {

                                }
                                index++;
                            }
                            AddStartMarker();
                        }
                        // Coding Rout Search Neighborhoods
                        String arr_neighborhoods = "";
                        for (int i = 0; i < mDataRoute.size(); i++) {
                            if (position_adapter_start ==
                                    mDataRoute.get(i).getId_busstop()) {

                                arr_neighborhoods += mDataRoute.get(i).getId_bus() + ",";
                         //       Toast.makeText(getContext(), arr_neighborhoods, Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                        // Set Data on ViewList and Dialog Show
                        arr_neighborhoods = arr_neighborhoods.substring(0, arr_neighborhoods.length() - 1);
                        listAdapter_nieghborhood = new DataListAdapter(getContext(), mDBHelper.getBusListDataOnIndex(arr_neighborhoods));

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.popup_neighborhood_list, null);

                        listView_nieghborhood = (ListView) mView.findViewById(R.id.listView);
                        TextView txt_station = (TextView) mView.findViewById(R.id.txt_station);
                        listView_nieghborhood.setAdapter(listAdapter_nieghborhood);
                        txt_station.setText(mDataListBusStop.get(position_adapter_start - 1).getName_busstop());


                        mBuilder.setView(mView);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    } else {
                        //          Toast.makeText(getContext(), "โปรดตรวจสอบข้อมูล", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), getContext().getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }
            }
        });

//////////////////////////////////////////// End Button ////////////////////////////////////////////////
        btnEnd = (ImageButton) rootView.findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Position Bus Stop
                try {
                    position_adapter_end = 0;
                    if (!completeTextViewEnd.getText().equals(null)) {
                        String strCopy = completeTextViewEnd.getText().toString();
                        for (int i = 0; i < strBusStop_Thai.length; i++) {
                            if (strCopy.equals(strBusStop_Thai[i].toString())) {
                                position_adapter_end = i + 1;
                            } else {

                            }
                        }
                        AddEndMarker();
                    } else {
                        //    Toast.makeText(getContext(), "โปรดตรวจสอบข้อมูล", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), getContext().getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }
            }
        });


//////////////////////////////////////// Search Button //////////////////////////////////////////////////////
        btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ArrayStart = new String[500];
                try {

                    position_adapter_start = 0;
                    position_adapter_end = 0;
                    if (!completeTextViewStart.getText().equals(null) && !completeTextViewEnd.getText().equals(null)) {

                        if (!completeTextViewStart.getText().equals(null)) {
                            String strCopy = completeTextViewStart.getText().toString();
                            for (int i = 0; i < strBusStop_Thai.length; i++) {
                                if (strCopy.equals(strBusStop_Thai[i].toString())) {
                                    position_adapter_start = i + 1;
                                    } else {

                                }
                            }
                            AddStartMarker();
                        }

                        if (!completeTextViewEnd.getText().equals(null)) {
                            String strCopy = completeTextViewEnd.getText().toString();
                            for (int i = 0; i < strBusStop_Thai.length; i++) {
                                if (strCopy.equals(strBusStop_Thai[i].toString())) {
                                    position_adapter_end = i + 1;
                                } else {

                                }
                            }
                            AddEndMarker();
                        }
                        ArrayList<Integer> arrayList_start = new ArrayList<Integer>();
                        ArrayList<Integer> arrayList_end = new ArrayList<Integer>();

                        int index_start = 0;
                        int index_end = 0;

                        for (int i = 0; i < mDataRoute.size(); i++) {
                            if (position_adapter_start ==
                                    mDataRoute.get(i).getId_busstop()) {
                                arrayList_start.add(mDataRoute.get(i).getId_bus());
                                //     Toast.makeText(getContext(), "Route Start " + arrayList_start.get(index_start), Toast.LENGTH_SHORT).show();
                                index_start++;
                            }
                            if (position_adapter_end ==
                                    mDataRoute.get(i).getId_busstop()) {
                                arrayList_end.add(mDataRoute.get(i).getId_bus());
                                //     Toast.makeText(getContext(), "Route End " + arrayList_end.get(index_end), Toast.LENGTH_SHORT).show();
                                index_end++;
                            }
                        }

                        Collections.sort(arrayList_start);
                        Collections.sort(arrayList_end);

                        String strOneList = "";
                        boolean CheckOneList = false;


                        // Check One Point Bus
                        for(int i = 0; i<mDataRoute.size();i++) {
                            for (int j = 0; j < arrayList_start.size(); j++) {
                                if (position_adapter_end == mDataRoute.get(i).getId_busstop()) {
                                    if (arrayList_start.get(j) == mDataRoute.get(i).getId_bus()){
                                        CheckOneList = true;
                                        strOneList = arrayList_start.get(j).toString();
                        //                Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                        String strdebug1 = "";
                        String strdebug2 = "";

                        String CoopPoint = "";
                        for(int i = 0 ; i < arrayList_start.size();i++){
                            String[] strPointStart = mDBHelper.getBusstopFromRoute(arrayList_start.get(i));
                            String debug1 = "";
                            String debug2 = "";
                            for(int j = 0 ; j<arrayList_end.size();j++){
                                String[] strPointEnd = mDBHelper.getBusstopFromRoute(arrayList_end.get(j));
                                for(int k = 0 ; k < strPointStart.length ; k++){
                                    for(int l = 0; l <strPointEnd.length ; l++){
                                        debug1 = strPointStart[k];
                                        debug2 = strPointEnd[l];
                                        strdebug1 += debug1  + ",";
                                        strdebug2 += debug2  + ",";
                                        if(debug1.equals(debug2)) {
                                            CoopPoint += debug1 + ",";
                                           // Toast.makeText(getContext(), debug1 + "," + debug2, Toast.LENGTH_SHORT).show();
                                        }
                                        if(CoopPoint != ""){
                                            //break;
                                        }
                                    }
                                }
                            }
                        }
                        String[] arrName_cooppoint = CoopPoint.split(",");
                        Toast.makeText(getContext(),arrName_cooppoint[0] + " " + CoopPoint , Toast.LENGTH_SHORT).show();
                        String name_CocpPoint = mDataListBusStop.get(Integer.parseInt(arrName_cooppoint[0])).getName_busstop();


                        String matchingData = "";

                        if(CheckOneList){
                            matchingData = strOneList;
                        }
                        else {
                            matchingData = arrayList_start.get(0) + "," + arrayList_end.get(0);
                        }
                       // Toast.makeText(getContext(), "Data " + matchingData, Toast.LENGTH_SHORT).show();
                        listAdapter_result = new DataListAdapter(getContext(), mDBHelper.getBusListDataOnIndex(matchingData));

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.popup_result_list, null);

                        listView_result = (ListView) mView.findViewById(R.id.listView);
                        TextView txt_station_from = (TextView) mView.findViewById(R.id.txt_station_from);
                        TextView txt_station_to = (TextView) mView.findViewById(R.id.txt_station_to);
                        TextView txt_station_connect = (TextView) mView.findViewById(R.id.txt_station_connect);
                        listView_result.setAdapter(listAdapter_result);

                        txt_station_from.setText(getContext().getString(R.string.from) + " " +
                        mDataListBusStop.get(position_adapter_start - 1).getName_busstop());

                        txt_station_to.setText(getContext().getString(R.string.to) + " " +
                                mDataListBusStop.get(position_adapter_end - 1).getName_busstop());

                        if(!CheckOneList)
                            txt_station_connect.setText(getContext().getString(R.string.station_core) + " " + name_CocpPoint);
                        else
                            txt_station_connect.setText("");

                        mBuilder.setView(mView);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();



                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), getContext().getString(R.string.check_data), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void AddStartMarker(){
        latitudeStart = mDataListBusStop.get(position_adapter_start-1).getLatitude_busstop();
        longitudeStart = mDataListBusStop.get(position_adapter_start-1).getLongtitude_busstop();
        LatLng latLng = new LatLng(latitudeStart, longitudeStart);
//        Toast.makeText(getContext(),"" + position_adapter_end , Toast.LENGTH_SHORT).show();

        if (null != StartMarker) {
            StartMarker.remove();
            mGoogleMap.clear();
        }

        StartMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        StartMarker.showInfoWindow();


        mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    public void AddEndMarker(){
        latitudeEnd = mDataListBusStop.get(position_adapter_end-1).getLatitude_busstop();
        longitudeEnd = mDataListBusStop.get(position_adapter_end-1).getLongtitude_busstop();
        LatLng latLng = new LatLng(latitudeEnd, longitudeEnd);
//        Toast.makeText(getContext(),"" + position_adapter_end , Toast.LENGTH_SHORT).show();

        if (null != EndMarker) {
            EndMarker.remove();
            mGoogleMap.clear();
        }

        EndMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        EndMarker.showInfoWindow();


        mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap = googleMap;

        try {
            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 200, 0, this);
        }catch (Exception ex){

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
/*
        if (null != myMarker) {
            myMarker.remove();
            mGoogleMap.clear();
        }

        myMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        myMarker.showInfoWindow();

        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        */
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public String getAddress(Context context, double lat, double lng) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
/*
            add = add + obj.getCountryCode();
            add = add + obj.getAdminArea();
            add = add + obj.getPostalCode();
            add = add + obj.getSubAdminArea();
            add = add + obj.getLocality();
            add = add + obj.getSubThoroughfare();
*/

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}
