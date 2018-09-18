package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.Activity;
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
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.ComprimidorArchivo;
import com.acerosocotlan.entregasacerosocotlan.modelo.InformacionAvisos_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.Prueba_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormularioActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private ImageView imagenEvidencia,circulo_iniciar_ruta, img_formulario_recargar_foto1;
    private EditText txt_kilometraje;
    private Button botonEnviar;
    private String fotoPathTemp = "";
    private String pathKilometraje = "";
    private Animation circulo_animacion;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    private SharedPreferences prs;
    private ProgressDialog progressDoalog;
    private Localizacion localizacion;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Inicializador();

        imagenEvidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(50);
                EjecutarPermisosCamara();
            }
        });
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(50);
                if(pathKilometraje.isEmpty()){
                    DialogoValidacionFoto();
                }else{
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
            }
        });
    }
    public void Inicializador(){
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imagenEvidencia =(ImageView) findViewById(R.id.imagen_formulario);
        img_formulario_recargar_foto1 =(ImageView) findViewById(R.id.img_formulario_recargar_foto1);
        botonEnviar= (Button) findViewById(R.id.btn_enviar_formulario);
        txt_kilometraje = (EditText)findViewById(R.id.text_input_layout_kilometraje);
        circulo_iniciar_ruta = (ImageView) findViewById(R.id.circulo_iniciar_ruta);
        circulo_animacion = AnimationUtils.loadAnimation(this,R.anim.circulo_animacion);
        circulo_iniciar_ruta.setVisibility(View.VISIBLE);
        circulo_iniciar_ruta.startAnimation(circulo_animacion);
        circulo_iniciar_ruta.setVisibility(View.INVISIBLE);
    }
    //Retrofit2
    private void InsertarFormulario(){
        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).MandarFormularioPost(
                "iniciarruta_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                ObtenerFecha(),
                String.valueOf(localizacion.getLatitude()),
                String.valueOf(localizacion.getLongitud()),
                txt_kilometraje.getText().toString());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("iniciada")){
                        InsertarFotoInicioRuta();

                    }
                }else{
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                MostrarDialogCustomNoConfiguracion();
            }
        });
    }
    private void InsertarFotoInicioRuta(){
        File foto_kilometraje = new File(pathKilometraje);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(foto_kilometraje));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", foto_kilometraje.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), foto_kilometraje.getName());

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_inicio_0/"+MetodosSharedPreference.getSociedadPref(prs),
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
                        Toast.makeText(getApplicationContext(),"Información guardada", Toast.LENGTH_LONG).show();
                        AbrirEntregas();
                        ObtenerListaAvisos();
                        EliminarFoto();

                    }else{
                        Toast.makeText(FormularioActivity.this, valor, Toast.LENGTH_SHORT).show();
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
    public void ObtenerListaAvisos(){
        Call<List<InformacionAvisos_retrofit>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).ObtenerInformacionParaAviso(
                "avisogeneral_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs) +"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<InformacionAvisos_retrofit>>() {
            @Override
            public void onResponse(Call<List<InformacionAvisos_retrofit>> call, Response<List<InformacionAvisos_retrofit>> response) {
                if(response.isSuccessful()){
                    Log.i("FORMULARIO",response.body().toString());
                    //MandarCodigoRastreo();
                }else{
                    Log.i("AVISO PERSONAL","Mensaje no reconocido");
                }
            }

            @Override
            public void onFailure(Call<List<InformacionAvisos_retrofit>> call, Throwable t) {
                Log.i("AVISO PERSONAL","Fallo la conexion "+ t.getMessage());
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
                progressDoalog.setMessage("Mandando los datos");
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
    public void DialogoValidacionFoto(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Falta ingresar la fotografia del kilometraje del camión");
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
                        Toast.makeText(FormularioActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FormularioActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AbrirEntregas() {
        Intent i = new Intent(FormularioActivity.this, ActivityEntregas.class);
        startActivity(i);
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

    private void EjecutarPermisosCamara(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FormularioActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }else{
                TomarFoto();
            }
        }else {
            TomarFoto();
        }
    }
    private void TomarFoto() {
        Intent intentTomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentTomarFoto.resolveActivity(getApplicationContext().getPackageManager()) != null){

            File archivoFoto = null;
            try {
                archivoFoto = crearImagen();

            }catch (Exception e){
                e.printStackTrace();
            }
            if (archivoFoto!= null){
                String authorities=getApplicationContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(this,authorities,archivoFoto);
                intentTomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentTomarFoto, REQUEST_CAMERA);
            }
        }
    }
    private File crearImagen() throws IOException {
        String timeStamp =  new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
        String archivoNombreImagen = "JPG_" + timeStamp +"_";
        File storageDir = getApplicationContext().getExternalFilesDir("FotosEvidencias");

        File foto = File.createTempFile(archivoNombreImagen, ".jpg", storageDir);
        fotoPathTemp = foto.getAbsolutePath();
        Log.i("PathTemp", fotoPathTemp);
        return foto;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            MostrarFoto();
        }
    }
    private void MostrarFoto() {
        int targetW = imagenEvidencia.getWidth();
        int targetH = imagenEvidencia.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fotoPathTemp, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fotoPathTemp, bmOptions);
        if (bitmap!=null) {
            imagenEvidencia.setImageBitmap(bitmap);
            pathKilometraje = fotoPathTemp;
            img_formulario_recargar_foto1.setVisibility(View.VISIBLE);
        }else{
            return;
        }
    }
    public void EliminarFoto(){
        File dir = getApplicationContext().getExternalFilesDir("FotosEvidencias");
        //comprueba si es directorio.
        if (dir.isDirectory())
        {
            //obtiene un listado de los archivos contenidos en el directorio.
            String[] hijos = dir.list();
            //Elimina los archivos contenidos.
            for (int i = 0; i < hijos.length; i++)
            {
                new File(dir, hijos[i]).delete();
            }
        }
    }
}
