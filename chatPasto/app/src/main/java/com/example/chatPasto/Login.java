package com.example.chatPasto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.example.chatPasto.Mensajes.Mensajeria;

public class Login extends AppCompatActivity {

    private EditText eTusuario;
    private EditText eTcontraseña;
    private Button bTingresar;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String IP = "https://androidchatpastuso.000webhostapp.com/ArchivosPHP/Login_GETID.php?id=";
    private static final String IP_TOKEN = "https://androidchatpastuso.000webhostapp.com/ArchivosPHP/Token_INSERTandUPDATE.php";

    private String USER = "";
    private String PASSWORD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        eTusuario = (EditText) findViewById(R.id.eTusuario);
        eTcontraseña = (EditText) findViewById(R.id.eTcontraseña);

        bTingresar = (Button) findViewById(R.id.bTingresar);

        bTingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarLogin(eTusuario.getText().toString().toLowerCase(),eTcontraseña.getText().toString().toLowerCase());
            }
        });
    }

    public void VerificarLogin(String user, String password){
        USER = user;
        PASSWORD = password;
        SolicitudJSON(IP+user);
    }

    public void SolicitudJSON(String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                VerificarPassword(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"Ocurrio un error, por favor contactese con el administrador",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void VerificarPassword(JSONObject datos){
        try {
            String estado = datos.getString("resultado");
            if(estado.equals("CC")){
                JSONObject Jsondatos = new JSONObject(datos.getString("datos"));
                String usuario = Jsondatos.getString("id");
                String contraseña = Jsondatos.getString("Password");
                if(usuario.equals(USER) && contraseña.equals(PASSWORD)){
                    String Token =FirebaseInstanceId.getInstance().getToken();
                    if(Token!=null){
                        JSONObject js = new JSONObject(Token);//SOLO SI LES APARECE {"token":"...."}
                        String tokenRecortado = js.getString("token");
                        SubirToken(tokenRecortado);
                    }
                    else Toast.makeText(this,"El token es nulo",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this,"La contraseña es incorrecta",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,estado,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {}
    }

    private void SubirToken(String token){
        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("id",USER);
        hashMapToken.put("token",token);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_TOKEN,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    Toast.makeText(Login.this,datos.getString("resultado"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e){}
                Intent i = new Intent(Login.this,Mensajeria.class);
                i.putExtra("key_emisor",USER);
                startActivity(i);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"El token no se pudo subir a la base de datos",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

}
