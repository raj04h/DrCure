package com.hr.drcure;
import android.content.Context;
import com.hr.drcure.ml.DrugEffectiveness;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


class MLfile{
    protected float[] ML(Context context, String drugName ){
        float[] outputArray=new float[0];

        try {
            DrugEffectiveness model = DrugEffectiveness.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 17}, DataType.FLOAT32);

            ByteBuffer byteBuffer=prepareInputBuffer(drugName);
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            DrugEffectiveness.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            outputArray = outputFeature0.getFloatArray();

            // Release model resources
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputArray;

    }
    protected ByteBuffer prepareInputBuffer(String drgn) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 17);
        byteBuffer.order(ByteOrder.nativeOrder());

        float normalizedHash = drgn.hashCode() % 1000 / 1000.0f;
        for (int i = 0; i < 17; i++) {
            byteBuffer.putFloat(normalizedHash);
        }
        return byteBuffer;
    }
}


public class DrugML extends MLfile {

    public  static  String geteff(Context context, String drugName){
        String eff="";
        MLfile mLfile=new MLfile();
        float[] outputArray=mLfile.ML(context,drugName);

        if(outputArray.length>1){
            eff=String.format("%.2f%%", outputArray[0] * 100);
            }
        return eff;
    }

    public  static  int getr(Context context, String drugName){
        int rsqr=0;
        MLfile mLfile=new MLfile();

        float[] outputArray=mLfile.ML(context,drugName);
            if(outputArray.length>1){
                rsqr=Math.round(outputArray[0]);
            }
        return  rsqr;
    }
}
