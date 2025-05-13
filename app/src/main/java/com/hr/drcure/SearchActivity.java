package com.hr.drcure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView searchauto;
    TableLayout drugTable;
    ArrayList<DrugModel> druglist;

    AutoCompleteTextView diseaseauto;
    TableLayout diseaseTable;
    ArrayList<DiseaseModel> diseaselist;

    DrugModel selectedDrug = null; // Track selected drug
    DiseaseModel selectedDisease = null; // Track selected disease

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_lay), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchauto = findViewById(R.id.search_autocomplete);
        drugTable = findViewById(R.id.drug_stable);
        druglist = DrugUtil.readDrug(this, "ANOVA_drugstructure.csv");

        searchauto.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchauto.getText().toString();
            loadDrugData(query);
            return true;
        });

        diseaseauto = findViewById(R.id.disease_auto);
        diseaseTable = findViewById(R.id.disease_stable);
        diseaselist = DiseaseUtil.readDisease(this, "Liver.csv");

        diseaseauto.setOnEditorActionListener((v, actionId, event) -> {
            String query = diseaseauto.getText().toString();
            loadDiseaseData(query);
            return true;
        });
    }

    private void loadDrugData(String query) {
        drugTable.removeAllViews();
        for (DrugModel drug : druglist) {
            if (drug.getName().equalsIgnoreCase(query)) {
                TableRow tableRow = new TableRow(this);

                TextView drugId = new TextView(this);
                drugId.setText(drug.getId());
                tableRow.addView(drugId);

                TextView drugname = new TextView(this);
                drugname.setText(drug.getName());
                tableRow.addView(drugname);

                TextView pathway = new TextView(this);
                pathway.setText(drug.getPathway());
                tableRow.addView(pathway);

                TextView ic = new TextView(this);
                ic.setText(drug.getIc50());
                tableRow.addView(ic);

                tableRow.setOnClickListener(v -> {selectedDrug = drug;
                    navigateToMainActivity(v);
                }); // Save selected drug
                drugTable.addView(tableRow);
            }
        }
    }

    private void loadDiseaseData(String query) {
        diseaseTable.removeAllViews();
        for (DiseaseModel disease : diseaselist) {
            if (disease.getbill().equalsIgnoreCase(query)) {
                TableRow tableRow = new TableRow(this);

                TextView age = new TextView(this);
                age.setText(disease.getage());
                tableRow.addView(age);

                TextView bill = new TextView(this);
                bill.setText(disease.getbill());
                tableRow.addView(bill);

                TextView pro = new TextView(this);
                pro.setText(disease.getpro());
                tableRow.addView(pro);

                TextView Ag = new TextView(this);
                Ag.setText(disease.getAg());
                tableRow.addView(Ag);

                TextView dis = new TextView(this);
                dis.setText(disease.getdis());
                tableRow.addView(dis);

                tableRow.setOnClickListener(v -> {selectedDisease = disease;
                    navigateToMainActivity(v);
                });// Save selected disease

                diseaseTable.addView(tableRow);
            }
        }
    }

    public void navigateToMainActivity(View view) {
        if (selectedDrug != null && selectedDisease != null) {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);

            // Add drug data
            intent.putExtra("drug_id", selectedDrug.getId());
            intent.putExtra("drug_name", selectedDrug.getName());
            intent.putExtra("target_pathway", selectedDrug.getPathway());
            intent.putExtra("ic50", selectedDrug.getIc50());

            // Add disease data
            intent.putExtra("Age", selectedDisease.getage());
            intent.putExtra("Bilirubin", selectedDisease.getbill());
            intent.putExtra("Protein", selectedDisease.getpro());
            intent.putExtra("A/G", selectedDisease.getAg());
            intent.putExtra("Disease", selectedDisease.getdis());

            startActivity(intent);
        }
    }
}
