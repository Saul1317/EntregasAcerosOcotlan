package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.ComprimidorArchivo;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormularioPosponerEntrega extends AppCompatActivity {

    private Button btn_enviar_formulario;
    private ImageView img_evidencia_posponer,circulo_evidencia_posponer;
    private Animation circulo_animacion;
    private String CARPETA_RAIZ="acerosOcotlan/";
    private String RUTA_IMAGEN = CARPETA_RAIZ+"evidencia";
    private String path;
    private final int COD_TOMAR_FOTO=20;
    private final int COD_SELECCIONA_FOTO=10;
    private String valor;
    private SharedPreferences prs;
    private File imagen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_posponer_entrega);
        IniciadorViews();
        img_evidencia_posponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EjecutarPermisosCamara();
            }
        });

        btn_enviar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagen==null){
                    Toast.makeText(FormularioPosponerEntrega.this, "Ingresa la foto de evidencia", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FormularioPosponerEntrega.this, "UUUY ya se pospuso por razones: "+  valor, Toast.LENGTH_SHORT).show();
                    PosponerEntrega();
                }
            }
        });
    }
    //CAMARA
    private void EjecutarPermisosCamara(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FormularioPosponerEntrega.this, new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
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
        int targetW = img_evidencia_posponer.getWidth();
        int targetH = img_evidencia_posponer.getHeight();

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
            img_evidencia_posponer.setImageBitmap(bitmap);
        }else{
            return;
        }
    }
    private void PosponerEntrega(){
        Log.i("URL posponer", "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_posponer/"+MetodosSharedPreference.getSociedadPref(prs));

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarEvidencia(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_posponer/"+MetodosSharedPreference.getSociedadPref(prs),
                valor,
                MetodosSharedPreference.ObtenerFolioRutaPref(prs),
                "-",
                "-",
                "-");
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    Log.i("POSPONER",respuesta.get(0));
                    if (respuesta.get(0).equals("correcto")){
                        GuardarEvidencia();
                    }else{
                        Toast.makeText(FormularioPosponerEntrega.this, "Ocurrio un error, intente de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("POSPONER","Respuesta desconocida");
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
            }
        });
    }
    private void GuardarEvidencia(){
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), ComprimidorArchivo.getCompressedImageFile(imagen));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", imagen.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), imagen.getName());
        Log.i("URL posponer", "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_posponer/"+MetodosSharedPreference.getSociedadPref(prs));

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).InsertarFotoEvidencia(
                "foto_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"_"+valor+"_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs),
                fileToUpload,
                filename);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    Log.i("POSPONER_FOTO",respuesta.get(0));
                    if(respuesta.get(0).equals("fotoguardada")){
                        AbrirEntregas();
                    }
                }else{
                    Log.i("POSPONER","Respuesta desconocida");
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("FOTO",t.getMessage());
            }
        });
    }
    private void AbrirEntregas() {
        Intent i = new Intent(FormularioPosponerEntrega.this, ActivityEntregas.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    private void IniciadorViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_enviar_formulario= (Button) findViewById(R.id.btn_posponer_entrega);
        img_evidencia_posponer =(ImageView) findViewById(R.id.img_evidencia_posponer);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        circulo_evidencia_posponer =(ImageView) findViewById(R.id.circulo_evidencia_posponer);
        circulo_animacion = AnimationUtils.loadAnimation(this, R.anim.circulo_animacion);
        circulo_evidencia_posponer.setVisibility(View.VISIBLE);
        circulo_evidencia_posponer.startAnimation(circulo_animacion);
        circulo_evidencia_posponer.setVisibility(View.INVISIBLE);
        valor = getIntent().getExtras().getString("posponer_razon");
        Log.i("TIPO POSPOPNER",valor);
    }
}
