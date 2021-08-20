package com.istl.mimaps.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.istl.mimaps.MapsActivity;
import com.istl.mimaps.MiPosicion;
import com.istl.mimaps.R;
import com.istl.mimaps.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUb, btnMapa;
    private EditText txtLat;
    private EditText txtLong;
    private EditText txtAlt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnUb = root.findViewById(R.id.btnGPS);
        btnUb.setOnClickListener(this);
        btnMapa = root.findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(this);
        txtLat = root.findViewById(R.id.txtLatitud);
        txtLong = root.findViewById(R.id.txtLongitud);
        txtAlt = root.findViewById(R.id.txtAltitud);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void verificar(){
        if (!txtAlt.getText().toString().equals("")|| !txtLong.getText().toString().equals("") || !txtLat.getText().toString().equals("")){
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("latitud", txtLat.getText().toString());
            intent.putExtra("longitud", txtLong.getText().toString());
            intent.putExtra("altitud", txtAlt.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Los campo no estan llenos", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v){
        if(v==btnUb){
            miPosicion();
            //Toast.makeText((Activity) getContext(), "click button", Toast.LENGTH_LONG).show();
        }
        if(v==btnMapa){
            verificar();
        }
    }

    public void miPosicion(){
        if(ContextCompat.checkSelfPermission((Activity) getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        LocationManager objLocation = null;
        LocationListener objLocListener;

        objLocation = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        objLocListener = new MiPosicion();
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, objLocListener);

        if(objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            txtLong.setText(MiPosicion.longitud+"");
            txtLat.setText(MiPosicion.latitud+"");
            txtAlt.setText(MiPosicion.altitud+"");
        }else{
            Toast.makeText((Activity) getContext(), "GPS deshabilitado", Toast.LENGTH_LONG).show();
        }
    }
}