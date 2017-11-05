package com.example.jia.classcircle.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jia.classcircle.R;
import com.example.jia.classcircle.activity.util.SharePreferenceUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Hashtable;

public class QRcodeActivity extends AppCompatActivity {
    private ImageView img_QRcode;
   // private Button btn_return;
    private String getClassName="";
    private Toolbar toolbar_QRCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ercode);
        initView();
        onclickEvent();
        produceQRcode();
    }

    private void produceQRcode() {
        if(getClassName.length()>0){
            Bitmap bitmap=encodeAsBitmap(getClassName);
            img_QRcode.setImageBitmap(bitmap);
        }else {
            Toast.makeText(QRcodeActivity.this,"二维码生成失败，请返回重新创建",Toast.LENGTH_SHORT).show();
        }
    }

    private void onclickEvent() {
        toolbar_QRCode.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


     /*   btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });*/
    }

    private void initView() {

        Intent intent=getIntent();
        getClassName=  intent.getStringExtra("theClassNameID");//此时取得的是objectID,用它来查询班级名

        img_QRcode= (ImageView) findViewById(R.id.img_QRcode);
        toolbar_QRCode= (Toolbar) findViewById(R.id.toolbar_QRCode);

        setSupportActionBar(toolbar_QRCode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ToolBar：根据是否开启夜间模式设置其控件颜色
        if (SharePreferenceUtil.getNightMode(this)){
            toolbar_QRCode.setBackgroundColor(Color.parseColor("#534f4f"));
        }
       // btn_return= (Button) findViewById(btn_return);

    }

    private Bitmap encodeAsBitmap(String str){//将字符串转为二维码
        Bitmap bitmap = null;
        BitMatrix result = null;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter() ;
        try {
    /*        try { //,
                str=new String(str.getBytes("ISO-8859-1"),"UTF-8");//zxing乱码解决方法

            } catch (UnsupportedEncodingException e) {

            }*/
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200,hints);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){ //
            return null;
        }
        return bitmap;
    }
}
