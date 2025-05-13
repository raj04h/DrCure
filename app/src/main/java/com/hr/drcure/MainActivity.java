package com.hr.drcure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // In MainActivity's onCreate method:
    TextView dr_id;
    TextView dr_nme;
    TextView dr_eff;
    TableLayout drugTable;
    FloatingActionButton fabsearch;
    Button btnentrydrug;
    Button btnentrylung;

    TableLayout diseaseTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_l), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dr_id=findViewById(R.id.drug_id);
        dr_nme=findViewById(R.id.drug_name);
        dr_eff=findViewById(R.id.drug_efficiency);
        fabsearch = findViewById(R.id.fab_search);
        btnentrydrug=findViewById(R.id.btn_entry_drug);
        drugTable = findViewById(R.id.table_drug);
        diseaseTable=findViewById(R.id.table_disease);
        btnentrylung=findViewById(R.id.btn_entry_lung);



        // Fetch drug information from the Intent passed by SearchActivity
        String drugId = getIntent().getStringExtra("drug_id");
        String drugName = getIntent().getStringExtra("drug_name");
        String targetPathway = getIntent().getStringExtra("target_pathway");
        String ic50 = getIntent().getStringExtra("ic50");

        dr_id.setText("ID: "+ drugId);
        dr_nme.setText("Drug: "+drugName);

        if (drugId != null && drugName != null) {
            // Create a new TableRow programmatically to display the drug info
            TableRow tableRow = new TableRow(this);

            TextView idView = new TextView(this);
            idView.setText(drugId);
            tableRow.addView(idView);

            TextView nameView = new TextView(this);
            nameView.setText(drugName);
            tableRow.addView(nameView);

            TextView pathwayView = new TextView(this);
            pathwayView.setText(targetPathway);
            tableRow.addView(pathwayView);

            TextView rView = new TextView(this);

            int rsqured=DrugML.getr(this, drugName);
            rView.setText( String.valueOf(rsqured));
            tableRow.addView(rView);

            String effici=DrugML.geteff(this,drugName);
            dr_eff.setText("Efficiency= "+ effici);

            TextView ic50View = new TextView(this);
            ic50View.setText(ic50);
            tableRow.addView(ic50View);

            // Add the TableRow to the TableLayout
            drugTable.addView(tableRow);
        }

        String age=getIntent().getStringExtra("Age");
        String bill=getIntent().getStringExtra("Bilirubin");
        String pro=getIntent().getStringExtra("Protein");
        String ag=getIntent().getStringExtra("A/G");
        String dis=getIntent().getStringExtra("Disease");


        if(bill!=null && ag!=null){
            TableRow tableRow=new TableRow(this);

            TextView agev = new TextView(this);
            agev.setText(age);
            tableRow.addView(agev);

            TextView billv = new TextView(this);
            billv.setText(bill);
            tableRow.addView(billv);

            TextView prov = new TextView(this);
            prov.setText(pro);
            tableRow.addView(prov);

            TextView Agv = new TextView(this);
            Agv.setText(ag);
            tableRow.addView(Agv);

            TextView disv = new TextView(this);
            disv.setText(dis);
            tableRow.addView(disv);

            diseaseTable.addView(tableRow);
        }

        btnentrydrug.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, PredictorModelsACtivity.class);
            startActivity(intent);
        });
        btnentrylung.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, LungModelActivity.class);
            startActivity(intent);
        });


        // Search button action
        fabsearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });


    }
}
