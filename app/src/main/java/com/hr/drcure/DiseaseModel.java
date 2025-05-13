package com.hr.drcure;

public class DiseaseModel {
    private String age;
    private String Billrubin;
    private String Proteins;
    private String Ag;
    private String Disease;

    public  DiseaseModel(String age_n, String bill, String pro, String ag, String dis){
        this.age=age_n;
        this.Billrubin=bill;
        this.Proteins=pro;
        this.Ag=ag;
        this.Disease=dis;
    }

    public String getage(){
        return  age;
    }
    public String getbill(){
        return Billrubin;
    }
    public  String getpro(){
        return  Proteins;
    }
    public  String getAg(){
        return  Ag;
    }
    public String getdis(){
        return  Disease;
    }
}
