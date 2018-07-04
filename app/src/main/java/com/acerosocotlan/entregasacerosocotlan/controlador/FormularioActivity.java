package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.InformacionAvisos_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormularioActivity extends AppCompatActivity {
    //VIEWS
    private ImageView imagenEvidencia;
    private Button botonEnviar;
    private EditText txt_kilometraje;
    //DATOS EXTERNOS
    private Location location;
    private LocationManager locationManager;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    //RUTAS DE LA CAMARA
    private String CARPETA_RAIZ="acerosOcotlan/";
    private String RUTA_IMAGEN = CARPETA_RAIZ+"evidencia";
    private String path;
    private final int COD_TOMAR_FOTO=20;
    private final int COD_SELECCIONA_FOTO=10;
    File imagen;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    //LOCATION
    private double longitudeBest =0, latitudeBest=0;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Inicializador();
        imagenEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EjecutarPermisosCamara();
            }
        });
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_kilometraje.getText().toString().isEmpty()){
                    DialogoValidacion();
                }else{
                    if (ValidarPermisosGPS()==true){
                        DialogoConfirmacion();
                    }else {
                        ActivityCompat.requestPermissions(FormularioActivity.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
                    }
                }
            }
        });
    }
    public void Inicializador(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        imagenEvidencia =(ImageView) findViewById(R.id.imagen_formulario);
        botonEnviar= (Button) findViewById(R.id.btn_enviar_formulario);
        txt_kilometraje = (EditText)findViewById(R.id.text_input_layout_kilometraje);

    }
    //OBTENER DATOS
    public boolean ValidarPermisosGPS(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }else{
                return true;
            }
        }else {
            return true;
        }
    }
    public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
    //CAMARA
    private void EjecutarPermisosCamara(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FormularioActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }else{
                TomarImagen();
            }
        }else {
            TomarImagen();
        }
    }
    private void TomarImagen(){
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean existencia = fileImagen.exists();
        String nombreImagen="";
        if (existencia==false){
            existencia = fileImagen.mkdirs();
        }
        if (existencia==true){
            nombreImagen= (System.currentTimeMillis()/1000)+".jpg";
        }
        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        imagen = new File(path);
        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri=FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_TOMAR_FOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){}
        switch (requestCode){
            case COD_TOMAR_FOTO:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.i("Ruta de almacenamiento","Path: "+path);
                    }
                });
                setPic();
                break;
            case COD_SELECCIONA_FOTO:
                Uri miPath = data.getData();
                path= miPath.getPath();
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void setPic() {
            int targetW = imagenEvidencia.getWidth();
            int targetH = imagenEvidencia.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
            if (bitmap!=null) {
                imagenEvidencia.setImageBitmap(bitmap);
            }else{
                return;
            }
    }
    //Retrofit2
    public void InsertarFormulario(){
        Call<List<String>> call = NetworkAdapter.getApiService().MandarFormularioPost(
                "iniciarruta_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                ObtenerFecha(), String.valueOf(latitudeBest), String.valueOf(longitudeBest), txt_kilometraje.getText().toString());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0).toString();
                   if (valor.equals("iniciada")){
                        Toast.makeText(getApplicationContext(),"Información guardada", Toast.LENGTH_LONG).show();
                        ObtenerListaAvisos();
                        NuevaActividad();
                    }
                }else{
                    progressDoalog.dismiss();
                    Toast.makeText(getApplicationContext(), "No manches", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("LOL", "onFailure: ERROR"+t.getMessage());
            }
        });
    }
    public void ObtenerListaAvisos(){
        Call<List<InformacionAvisos_retrofit>> call = NetworkAdapter.getApiService().ObtenerInformacionParaAviso(
                "avisogeneral_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs) +"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<InformacionAvisos_retrofit>>() {
            @Override
            public void onResponse(Call<List<InformacionAvisos_retrofit>> call, Response<List<InformacionAvisos_retrofit>> response) {
                if(response.isSuccessful()){
                    Log.i("FORMULARIO",response.body().toString());
                }else{
                    Log.i("FORMULARIO","Mensaje no reconocido");
                }
            }

            @Override
            public void onFailure(Call<List<InformacionAvisos_retrofit>> call, Throwable t) {
                Log.i("FORMULARIO","Fallo la conexion");
                Toast.makeText(FormularioActivity.this, "Problema con el socket", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //ACTIVITY
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de comenzar una ruta, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(FormularioActivity.this);
                progressDoalog.setMessage("Preparando los datos");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                ObtenerMejorLocalizacion();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    public void DialogoValidacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Falta ingresar el kilometraje actual del camión");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    public void NuevaActividad(){
        Intent i = new Intent(FormularioActivity.this, ActivityEntregas.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //LOCALIZACION
    private void ObtenerMejorLocalizacion(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 5, LocalizacionListener);
        }
    }
    private final LocationListener LocalizacionListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            Log.i("LOCALIZACION",String.valueOf(longitudeBest)+" "+String.valueOf(latitudeBest));
            locationManager.removeUpdates(LocalizacionListener);
            InsertarFormulario();
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
    };
}
