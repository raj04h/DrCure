package com.hr.drcure;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LungModelActivity extends AppCompatActivity {

    private EditText etBilirubin, etAlkPhos, etSgot, etProteins, etAlbumin, etAGRatio;
    private Button btnPredict;
    private TextView tvResult;
    private final String apiUrl = "https://liver-disease-ex53.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lungmodel);

        etBilirubin = findViewById(R.id.et_bilirubin);
        etAlkPhos = findViewById(R.id.et_alk_phosphotase);
        etSgot = findViewById(R.id.et_sgot);
        etProteins = findViewById(R.id.et_proteins);
        etAlbumin = findViewById(R.id.et_albumin);
        etAGRatio = findViewById(R.id.et_ag_ratio);
        btnPredict = findViewById(R.id.btn_predict);
        tvResult = findViewById(R.id.tv_result);

        btnPredict.setOnClickListener(v -> predictLiverHealth());
    }

    private void predictLiverHealth() {
        try {
            JSONObject json = new JSONObject();
            json.put("Total Bilirubin", Double.parseDouble(etBilirubin.getText().toString()));
            json.put(" Alkphos Alkaline Phosphotase", Double.parseDouble(etAlkPhos.getText().toString()));
            json.put("Sgot Aspartate Aminotransferase", Double.parseDouble(etSgot.getText().toString()));
            json.put("Total Protiens", Double.parseDouble(etProteins.getText().toString()));
            json.put("Albumin", Double.parseDouble(etAlbumin.getText().toString()));
            json.put("A/G Ratio Albumin and Globulin Ratio", Double.parseDouble(etAGRatio.getText().toString()));

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    apiUrl,
                    json,
                    response -> {
                        try {
                            String prediction = response.getString("prediction");
                            tvResult.setText(prediction);
                        } catch (JSONException e) {
                            tvResult.setText("JSON Parsing Error");
                        }
                    },
                    error -> tvResult.setText("Error: " + error.getMessage())
            );

            queue.add(request);

        } catch (JSONException | NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please fill all fields correctly.", Toast.LENGTH_SHORT).show();
        }
    }
}
