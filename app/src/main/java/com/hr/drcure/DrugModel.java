package com.hr.drcure;

public class DrugModel {
    //Encapsulation- private instance variables

    private  String id;
    private  String name;
    private  String pathway;
    private  String ic50;

    // Constructor- create instence of original class
    public DrugModel(String id_nw, String name_nw, String pathway_nw, String ic50_nw){
        this.id=id_nw;
        this.name=name_nw;
        this.pathway=pathway_nw;
        this.ic50=ic50_nw;
    }

    // Getter Methods- access the private instance variables
    public String getId(){
        return  id;
    }
    public  String getName(){
        return  name;
    }
    public  String getPathway(){
        return  pathway;
    }
    public  String getIc50(){
        return  ic50;
    }
}
