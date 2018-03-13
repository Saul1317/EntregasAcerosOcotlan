package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.InformacionAvisos_retrofit;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormularioActivity extends AppCompatActivity {
    private Location location;
    private LocationManager locationManager;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private ImageView imagenEvidencia;
    private Button botonTomarFoto;
    private double latitude;
    private double longitud;
    private Calendar calendar;
    String CARPETA_RAIZ="acerosOcotlan/";
    String RUTA_IMAGEN = CARPETA_RAIZ+"evidencia";
    String path;
    private final int COD_TOMAR_FOTO=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Inicializador();
        botonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //InsertarFormulario()
                MenuCamara();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"LOL", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Inicializador de componentes
    public void Inicializador(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        imagenEvidencia =(ImageView) findViewById(R.id.imagen_formulario);
        botonTomarFoto=(Button) findViewById(R.id.btn_tomar_foto_formulario);
    }

    //OBTENER DATOS
    public void ObtenerLocalizacionCamion(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }else{
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude = location.getLatitude();
                longitud = location.getLongitude();
            }
        }else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitud = location.getLongitude();
        }
    }
    public void ObtenerFecha(){
        calendar = Calendar.getInstance();
        //simpleDateFormat.format(calendar.getTime())
    }

    //CAMARA
    private void MenuCamara(){
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen","Cancelar"};
        AlertDialog.Builder alertOpciones = new AlertDialog.Builder(FormularioActivity.this);
        alertOpciones.setTitle("Selecciona una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    Toast.makeText(FormularioActivity.this, "Tomar foto", Toast.LENGTH_SHORT).show();
                    if(EjecutarPermisosCamara()==true){
                        TomarImagen();
                    }else{
                        Toast.makeText(FormularioActivity.this, "No hay permisos", Toast.LENGTH_SHORT).show();
                    }
                }else if (opciones[i].equals("Cargar Imagen")){
                    Toast.makeText(FormularioActivity.this, "Cargar Foto", Toast.LENGTH_SHORT).show();
                }else if (opciones[i].equals("Cancelar")){
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    private boolean EjecutarPermisosCamara(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                solicitarPermisosManual();
            }else{
            }
        }
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(FormularioActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(FormularioActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
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
        File imagen = new File(path);
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
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(FormularioActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            case 10:
                break;
        }
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imagenEvidencia.getWidth();
        int targetH = imagenEvidencia.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imagenEvidencia.setImageBitmap(bitmap);
    }

    //Retrofit2
    public void InsertarFormulario(){
        Call<List<String>> call = NetworkAdapter.getApiService().MandarFormularioPost(
                "http://192.168.0.226/web/entregas/iniciarruta_43/gao",
                "2018-08-03 09:59:00",
                "0",
                "0",
                "0");
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0).toString();
                   if (valor.equals("iniciada")){
                        Toast.makeText(getApplicationContext(),valor, Toast.LENGTH_LONG).show();
                        ObtenerListaAvisos();
                    }
                }else{
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
                "http://192.168.0.226/web/entregas/avisogeneral_43/gao");
        call.enqueue(new Callback<List<InformacionAvisos_retrofit>>() {
            @Override
            public void onResponse(Call<List<InformacionAvisos_retrofit>> call, Response<List<InformacionAvisos_retrofit>> response) {
                if(response.isSuccessful()){
                    List<InformacionAvisos_retrofit> respuesta = response.body();
                    Toast.makeText(getApplicationContext(),"Obtuvimos los datos correctamente para mandar un mensaje: "+respuesta.get(0).getTelefono(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "No manches", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<InformacionAvisos_retrofit>> call, Throwable t) {

            }
        });
    }
}
