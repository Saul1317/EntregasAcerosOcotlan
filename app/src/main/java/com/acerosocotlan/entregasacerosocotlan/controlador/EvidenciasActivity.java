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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class EvidenciasActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private LinearLayout linear_layout_descripcion, linear_layout_descripcion1, linear_layout_descripcion2, linear_layout_descripcion3;
    private FrameLayout foto_acuse_recibo,  foto_evidencia1, foto_evidencia2, foto_evidencia3;
    private ImageView img_acuse_recibo, img_evidecia1, img_evidecia2, img_evidecia3,
    img_recargar_foto1, img_recargar_foto2, img_recargar_foto3, img_recargar_foto4;
    private TextView txt_evidencia1,txt_evidencia2,txt_evidencia3;
    private TextInputEditText text_comentario_evidencia;
    private Button btn_mandar_fotos_finalizar_entrega;
    private SharedPreferences prs;
    private String fotoPathTemp = "";
    private String pathAcuseRecibo = "";
    private String pathEvidencia1 = "";
    private String pathEvidencia2 = "";
    private String pathEvidencia3 = "";
    private ProgressDialog progressDoalog;
    private int numEvidencia;
    private Calendar calendar;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Localizacion localizacion;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidencias);
        InicializadorView();

        foto_acuse_recibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                numEvidencia=1;
                EjecutarPermisosCamara();
            }
        });

        foto_evidencia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                numEvidencia=2;
                EjecutarPermisosCamara();

            }
        });

        foto_evidencia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                numEvidencia=3;
                EjecutarPermisosCamara();
            }
        });

        foto_evidencia3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                numEvidencia=4;
                EjecutarPermisosCamara();
            }
        });

        btn_mandar_fotos_finalizar_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                if(!pathAcuseRecibo.isEmpty()){
                    DialogoConfirmacion();
                }else{
                    Toast.makeText(EvidenciasActivity.this, "Necesita tomar la foto del recibo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void InicializadorView() {
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        foto_acuse_recibo = (FrameLayout) findViewById(R.id.foto_acuse_recibo);
        text_comentario_evidencia= (TextInputEditText) findViewById(R.id.text_comentario_evidencia);
        linear_layout_descripcion = (LinearLayout) findViewById(R.id.linear_layout_descripcion);
        linear_layout_descripcion1 = (LinearLayout) findViewById(R.id.linear_layout_descripcion1);
        linear_layout_descripcion2 = (LinearLayout) findViewById(R.id.linear_layout_descripcion2);
        linear_layout_descripcion3 = (LinearLayout) findViewById(R.id.linear_layout_descripcion3);
        foto_evidencia1 = (FrameLayout) findViewById(R.id.foto_evidencia1);
        foto_evidencia2 = (FrameLayout) findViewById(R.id.foto_evidencia2);
        foto_evidencia3 = (FrameLayout) findViewById(R.id.foto_evidencia3);
        img_acuse_recibo= (ImageView) findViewById(R.id.img_acuse_recibo);
        img_evidecia1 = (ImageView) findViewById(R.id.img_evidecia1);
        img_evidecia2 = (ImageView) findViewById(R.id.img_evidecia2);
        img_evidecia3 = (ImageView) findViewById(R.id.img_evidecia3);
        img_recargar_foto1 =(ImageView) findViewById(R.id.img_recargar_foto1);
        img_recargar_foto2 =(ImageView) findViewById(R.id.img_recargar_foto2);
        img_recargar_foto3 =(ImageView) findViewById(R.id.img_recargar_foto3);
        img_recargar_foto4 =(ImageView) findViewById(R.id.img_recargar_foto4);
        txt_evidencia1 = (TextView) findViewById(R.id.txt_evidencia1);
        txt_evidencia2 = (TextView) findViewById(R.id.txt_evidencia2);
        txt_evidencia3 = (TextView) findViewById(R.id.txt_evidencia3);
        btn_mandar_fotos_finalizar_entrega =(Button) findViewById(R.id.btn_mandar_fotos_finalizar_entrega);
        foto_evidencia1.setEnabled(false);
        foto_evidencia1.setVisibility(View.INVISIBLE);
        foto_evidencia2.setEnabled(false);
        foto_evidencia2.setVisibility(View.INVISIBLE);
        foto_evidencia3.setEnabled(false);
        foto_evidencia3.setVisibility(View.INVISIBLE);
        progressDoalog = new ProgressDialog(EvidenciasActivity.this);
        progressDoalog.setMessage("Mandando los datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
    private void EjecutarPermisosCamara(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EvidenciasActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
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
        int targetW = img_acuse_recibo.getWidth();
        int targetH = img_acuse_recibo.getHeight();

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
            switch (numEvidencia)
            {
                case 1:
                    img_acuse_recibo.setImageBitmap(bitmap);
                    pathAcuseRecibo = fotoPathTemp;
                    foto_evidencia1.setEnabled(true);
                    foto_evidencia1.setVisibility(View.VISIBLE);
                    img_recargar_foto1.setVisibility(View.VISIBLE);
                    linear_layout_descripcion.setVisibility(View.GONE);
                    break;
                case 2:
                    img_evidecia1.setImageBitmap(bitmap);
                    pathEvidencia1 = fotoPathTemp;
                    foto_evidencia2.setEnabled(true);
                    foto_evidencia2.setVisibility(View.VISIBLE);
                    img_recargar_foto2.setVisibility(View.VISIBLE);
                    linear_layout_descripcion1.setVisibility(View.GONE);
                    break;
                case 3:
                    img_evidecia2.setImageBitmap(bitmap);
                    pathEvidencia2 = fotoPathTemp;
                    foto_evidencia3.setEnabled(true);
                    foto_evidencia3.setVisibility(View.VISIBLE);
                    img_recargar_foto3.setVisibility(View.VISIBLE);
                    linear_layout_descripcion2.setVisibility(View.GONE);
                    break;
                case 4:
                    img_evidecia3.setImageBitmap(bitmap);
                    pathEvidencia3 = fotoPathTemp;
                    img_recargar_foto4.setVisibility(View.VISIBLE);
                    linear_layout_descripcion3.setVisibility(View.GONE);
                    break;
            }
        }else{
            return;
        }
    }
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de finalizar la entrega ¿Desea continuar?");

        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                localizacion = new Localizacion(getApplicationContext());
                localizacion.ObtenerMejorLocalizacion();
                progressDoalog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InsertarSalidaCamion();
                        localizacion.cancelarLocalizacion();
                    }
                },4000);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    private void InsertarSalidaCamion(){
        String comentarios = text_comentario_evidencia.getText().toString();
        if (comentarios.isEmpty()){
            comentarios = "-";
        }

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).SalidaEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_salida/"+MetodosSharedPreference.getSociedadPref(prs),
                ObtenerFecha(), String.valueOf(localizacion.getLatitude()), String.valueOf(localizacion.getLongitud()),comentarios);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("correcto")){
                        MandarFotos();
                    }else{
                        progressDoalog.dismiss();
                    }
                }else{
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(EvidenciasActivity.this, "Ocurrio un error intente de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void MandarFotos() {
        if(!pathAcuseRecibo.isEmpty()){
            InsertarFotoRecibo();
            if(!pathEvidencia1.isEmpty()){
                InsertarFotoEvidencia1();
            }

            if(!pathEvidencia2.isEmpty()){
                InsertarFotoEvidencia2();
            }

            if(!pathEvidencia3.isEmpty()){
                InsertarFotoEvidencia3();
            }
        }else{
            Toast.makeText(EvidenciasActivity.this, "Necesitas tomar la foto del recibo", Toast.LENGTH_SHORT).show();
        }
    }
    private void InsertarFotoRecibo(){
        File foto_AcuseRecibo = new File(pathAcuseRecibo);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(foto_AcuseRecibo));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", foto_AcuseRecibo.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), foto_AcuseRecibo.getName());

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_recibo_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    progressDoalog.dismiss();
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("fotoguardada")){
                        Toast.makeText(getApplicationContext(),"Se completo la entrega", Toast.LENGTH_LONG).show();
                        EliminarFoto();
                        Intent i = new Intent(EvidenciasActivity.this, ActivityEntregas.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }else{
                        progressDoalog.dismiss();
                    }
                }else{
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
                progressDoalog.dismiss();
                Toast.makeText(EvidenciasActivity.this, "Ocurrio un error intente de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void InsertarFotoEvidencia1(){
        File foto_evidencia1 = new File(pathEvidencia1);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(foto_evidencia1));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", foto_evidencia1.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), foto_evidencia1.getName());

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_evidencia1_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("fotoguardada")){
                        Toast.makeText(getApplicationContext(),"Se completo la entrega", Toast.LENGTH_LONG).show();
                    }else{

                    }
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
            }
        });
    }
    private void InsertarFotoEvidencia2(){
        File foto_evidencia2 = new File(pathEvidencia2);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(foto_evidencia2));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", foto_evidencia2.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), foto_evidencia2.getName());

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_evidencia2_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("fotoguardada")){
                        Toast.makeText(getApplicationContext(),"Se completo la entrega", Toast.LENGTH_LONG).show();
                    }else{

                    }
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
            }
        });
    }
    private void InsertarFotoEvidencia3(){
        File foto_evidencia3 = new File(pathEvidencia3);

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(foto_evidencia3));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", foto_evidencia3.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), foto_evidencia3.getName());

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFoto(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_evidencia3_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    if (valor.equals("fotoguardada")){
                        Toast.makeText(getApplicationContext(),"Se completo la entrega", Toast.LENGTH_LONG).show();
                    }else{

                    }
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
            }
        });
    }
    private String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
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
                        Toast.makeText(EvidenciasActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EvidenciasActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
