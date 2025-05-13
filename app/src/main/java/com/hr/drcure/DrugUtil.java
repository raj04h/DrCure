package com.hr.drcure;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrugUtil {
    public  static ArrayList<DrugModel> readDrug(Context context, String filename){
        ArrayList<DrugModel> druglist=new ArrayList<>();

        try{ // read the drug details from the drug file and then add name in the druglist using drugmodel which contains the methods
            BufferedReader reader=new BufferedReader(new InputStreamReader(context.getAssets().open("ANOVA_drugstructure.csv")));
            String line;
            while ((line =reader.readLine())!=null){
                String[] columns=line.split(",");
                if(columns.length>=4){
                    DrugModel drug=new DrugModel(columns[1], columns[0],columns[3],columns[7]); // Incapulation DrugModel
                    druglist.add(drug);
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return druglist;
    }
}
