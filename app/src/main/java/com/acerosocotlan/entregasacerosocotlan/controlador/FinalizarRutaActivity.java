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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.ComprimidorArchivo;
import com.acerosocotlan.entregasacerosocotlan.modelo.InformacionAvisos_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FinalizarRutaActivity extends AppCompatActivity {

    //VIEWS
    private ImageView imagenEvidencia,circulo_finalizar_ruta;
    private Animation circulo_animacion;
    private Button botonEnviar;
    private EditText txt_kilometraje;
    //DATOS EXTERNOS
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    //RUTAS DE LA CAMARA
    private String CARPETA_RAIZ="acerosOcotlan/";
    private String RUTA_IMAGEN = CARPETA_RAIZ+"evidencia";
    private String path;
    private final int COD_TOMAR_FOTO=20;
    private final int COD_SELECCIONA_FOTO=10;
    private File imagen;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    //INSTANCIA
    private Localizacion localizacion;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_ruta);
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
                if (imagen==null){
                    DialogoValidacion();
                }else{
                    if(txt_kilometraje.getText().toString().isEmpty()){
                        DialogoValidacion();
                    }else{
                        if (ValidarPermisosGPS()==true){
                            DialogoConfirmacion();
                        }else {
                            ActivityCompat.requestPermissions(FinalizarRutaActivity.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
                        }
                    }
                }
            }
        });
    }
    //Inicializador de componentes
    public void Inicializador(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        imagenEvidencia =(ImageView) findViewById(R.id.imagen_formulario_finalizar);
        botonEnviar= (Button) findViewById(R.id.btn_enviar_formulario_finalizar);
        txt_kilometraje = (EditText)findViewById(R.id.text_input_layout_kilometraje_finalizar);
        circulo_finalizar_ruta = (ImageView) findViewById(R.id.circulo_finalizar_ruta);
        circulo_animacion = AnimationUtils.loadAnimation(this,R.anim.circulo_animacion);
        circulo_finalizar_ruta.setVisibility(View.VISIBLE);
        circulo_finalizar_ruta.startAnimation(circulo_animacion);
        circulo_finalizar_ruta.setVisibility(View.INVISIBLE);
    }
    private void DialogoValidacionFoto() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Falta ingresar la fotografia del acuse de recibo");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    //OBTENER DATOS
   public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
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
    //CAMARA
    private void EjecutarPermisosCamara(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FinalizarRutaActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
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
            nombreImagen= (System.currentTimeMillis()/1000)+".png";
        }
        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        imagen = new File(path);
        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
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
        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).LlegadaRuta(
                "finalizarruta_"+ MetodosSharedPreference.ObtenerFolioRutaPref(prs) +"/"+MetodosSharedPreference.getSociedadPref(prs),
                ObtenerFecha(),
                String.valueOf(localizacion.getLatitude()),
                String.valueOf(localizacion.getLongitud()),
                txt_kilometraje.getText().toString());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("finalizada")){
                        InsertarFotoFinalizarRuta();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No manches", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                MostrarDialogCustomNoConfiguracion();
            }
        });
    }
    private void InsertarFotoFinalizarRuta(){
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(imagen));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", imagen.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), imagen.getName());
        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_finalizada_0/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("fotoguardada")){
                        Toast.makeText(getApplicationContext(),"Se completo la ruta", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(FinalizarRutaActivity.this, ScrollingRutasActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }else{
                        Toast.makeText(FinalizarRutaActivity.this, valor, Toast.LENGTH_SHORT).show();
                    }
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                Log.i("FOTO",t.getMessage());
                MostrarDialogCustomNoConfiguracion();
            }
        });
    }
    //ACTIVITY
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de finalizar esta ruta, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(FinalizarRutaActivity.this);
                progressDoalog.setMessage("Preparando los datos");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                localizacion = new Localizacion(getApplicationContext());
                localizacion.ObtenerMejorLocalizacion();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localizacion.cancelarLocalizacion();
                        InsertarFormulario();
                    }
                },4000);
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
    private void MostrarDialogCustomNoConfiguracion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.DialogErrorConexion);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_error_conexion, null);
        alert.setCancelable(false);
        alert.setView(dialoglayout);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogErrorConexion;
        alertDialog.show();
        final FloatingActionButton botonEntendido = (FloatingActionButton) dialoglayout.findViewById(R.id.fab_recargar_app);
        botonEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidacionConexion.isConnectedWifi(getApplicationContext())||ValidacionConexion.isConnectedMobile(getApplicationContext())){
                    if(ValidacionConexion.isOnline(getApplicationContext())){
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(FinalizarRutaActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FinalizarRutaActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
