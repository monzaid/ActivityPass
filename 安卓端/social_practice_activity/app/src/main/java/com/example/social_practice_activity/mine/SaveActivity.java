package com.example.social_practice_activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.social_practice_activity.R;

public class SaveActivity extends AppCompatActivity  {
    private String title;
    private String text;
    private String tips;

    private EditText et_text;
    private TextView tv_tips;
    private TextView tv_title;
    private Button btn_save;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Bundle bundle = this.getIntent().getExtras();
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.save_title_bar, null);
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        title = bundle.getString("title");
        text = bundle.getString("text");
        tips = bundle.getString("tips");
        et_text = findViewById(R.id.et_text);
        tv_tips = findViewById(R.id.tv_tips);
        tv_title = mActionBarView.findViewById(R.id.tv_title);
        et_text.setText(text);
        tv_tips.setText(tips);
        tv_title.setText(title);
        btn_save = mActionBarView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                String result = String.valueOf(et_text.getText());
                bundle1.putString("text", result);
                Intent intent = new Intent();
                intent.putExtras(bundle1);
                if (result.equals(text)){
                    setResult(RESULT_CANCELED, intent);
                }else{
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
        btn_back = mActionBarView.findViewById(R.id.ib_title_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}