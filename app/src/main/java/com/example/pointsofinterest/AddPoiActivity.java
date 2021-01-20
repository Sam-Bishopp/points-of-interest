package com.example.pointsofinterest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPoiActivity extends AppCompatActivity implements View.OnClickListener {


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poientry);

        EditText name = findViewById(R.id.name);
        EditText type = findViewById(R.id.type);
        EditText desc = findViewById(R.id.desc);
        Button buttonPoi = findViewById(R.id.btnPoi);
        buttonPoi.setOnClickListener(this);
    }

    public void onClick (View v)
    {
        EditText nameText = findViewById(R.id.name);
        String name = nameText.getText().toString();

        EditText typeText = findViewById(R.id.type);
        String type = typeText.getText().toString();

        EditText descText = findViewById(R.id.desc);
        String desc = descText.getText().toString();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("poiName1", name);
        bundle.putString("poiType2", type);
        bundle.putString("poiDesc3", desc);
        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);

        finish();
    }
}
