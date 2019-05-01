package cl.ubiobio.medidoriot;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MradiacionUV extends AppCompatActivity {

    Button volver;
    //creamos la consulta
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mradiacion_uv);
        //inicializamos la consulta
        queue = Volley.newRequestQueue(this);
        SetValorGauge();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetValorGauge();
            }
        });

        volver = (Button)findViewById(R.id.almenu);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iralmenu = new Intent(MradiacionUV.this,MainActivity.class);
                startActivity(iralmenu);
            }
        });
    }
    private void SetValorGauge(){
        //buscamos la fecha actual
        Calendar rightNow = Calendar.getInstance();
        Date fechaActual = rightNow.getTime();
        Integer dia = fechaActual.getDay();
        Integer mes = fechaActual.getMonth();
        Integer anio = fechaActual.getYear();
        String fechaConcatenada = "" + dia + mes + anio;
        //url de consulta
        String url = "http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/lWTXt6CeLP/8IvrZCP3qa/" + fechaConcatenada;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String valor;
                try {
                    JSONArray array = response.getJSONArray("data");
                    for (int i =0; i<array.length();i++) {
                        JSONObject object = array.getJSONObject(i);
                        valor = object.getString("valor");
                    }
                    //falta setear valor al gaugue


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        queue.add(request);
    }
}
