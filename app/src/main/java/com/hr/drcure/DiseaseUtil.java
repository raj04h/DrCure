package com.hr.drcure;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DiseaseUtil {
    public  static ArrayList<DiseaseModel> readDisease(Context context, String filename){
        ArrayList<DiseaseModel> diseaselist=new ArrayList<>();

        try{ // read the drug details from the drug file and then add name in the druglist using drugmodel which contains the methods
            BufferedReader reader=new BufferedReader(new InputStreamReader(context.getAssets().open("Liver.csv")));
            String line;
            while ((line =reader.readLine())!=null){
                String[] columns=line.split(",");
                if(columns.length>=5){
                    DiseaseModel disase=new DiseaseModel(columns[0], columns[2],columns[7],columns[8], columns[9]); // Incapulation DrugModel
                    diseaselist.add(disase);
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return diseaselist;
    }
}
