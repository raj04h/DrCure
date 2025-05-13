package com.hr.drcure;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PredictorModelsACtivity extends AppCompatActivity {

    private EditText etDrugName, etDrugTarget, etTargetPath, etFeatureName, etNPos, etNNeg;
    private Button sendButton;
    private TextView tvResult;

    private static final String PREDICT_URL = "https://project-drug.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predictor_models);

        // bind views
        etDrugName    = findViewById(R.id.drug_name);
        etDrugTarget  = findViewById(R.id.drug_target);
        etTargetPath  = findViewById(R.id.target_path);
        etFeatureName = findViewById(R.id.feature_name);
        etNPos        = findViewById(R.id.n_pos);
        etNNeg        = findViewById(R.id.n_neg);
        sendButton    = findViewById(R.id.send_button);
        tvResult      = findViewById(R.id.IC_result);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drugName    = etDrugName.getText().toString().trim();
                String drugTarget  = etDrugTarget.getText().toString().trim();
                String targetPath  = etTargetPath.getText().toString().trim();
                String featureName = etFeatureName.getText().toString().trim();
                String nPosStr     = etNPos.getText().toString().trim();
                String nNegStr     = etNNeg.getText().toString().trim();

                if (drugName.isEmpty() || drugTarget.isEmpty() || targetPath.isEmpty()
                        || featureName.isEmpty() || nPosStr.isEmpty() || nNegStr.isEmpty()) {
                    tvResult.setText("Please fill in all fields.");
                    return;
                }

                int nPos = Integer.parseInt(nPosStr);
                int nNeg = Integer.parseInt(nNegStr);

                // fire off the network task
                new PredictTask(drugName, drugTarget, targetPath, featureName, nPos, nNeg).execute();
            }
        });
    }

    private class PredictTask extends AsyncTask<Void, Void, Double> {
        private final String drugName, drugTarget, targetPath, featureName;
        private final int nPos, nNeg;
        private String errorMessage = null;

        PredictTask(String drugName, String drugTarget, String targetPath,
                    String featureName, int nPos, int nNeg) {
            this.drugName    = drugName;
            this.drugTarget  = drugTarget;
            this.targetPath  = targetPath;
            this.featureName = featureName;
            this.nPos        = nPos;
            this.nNeg        = nNeg;
        }

        @Override
        protected Double doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            try {
                // build JSON payload
                JSONObject payload = new JSONObject();
                payload.put("Drug name",         drugName);
                payload.put("Drug target",       drugTarget);
                payload.put("Target Pathway",    targetPath);
                payload.put("Feature Name",      featureName);
                payload.put("n_feature_pos",     nPos);
                payload.put("n_feature_neg",     nNeg);

                // open connection
                URL url = new URL(PREDICT_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // write JSON
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.toString().getBytes("UTF-8"));
                }

                // read response
                int code = conn.getResponseCode();
                BufferedReader reader;
                if (code >= 200 && code < 300) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder responseSb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseSb.append(line);
                }
                reader.close();

                if (code >= 200 && code < 300) {
                    JSONObject respJson = new JSONObject(responseSb.toString());
                    return respJson.getDouble("ic50_effect_size");
                } else {
                    errorMessage = "Server error: " + responseSb.toString();
                    return null;
                }

            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Double result) {
            if (result != null) {
                tvResult.setText(String.format("Result: %.10f", result));
            } else {
                tvResult.setText("Error: " + (errorMessage != null ? errorMessage : "Unknown"));
            }
        }
    }
}
