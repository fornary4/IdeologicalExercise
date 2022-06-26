package com.fornary4.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fornary4.exercise.adapter.ListItemAdapter;
import com.fornary4.exercise.database.UserDBHelper;
import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.ErrorInfo;
import com.fornary4.exercise.entity.Judge;
import com.fornary4.exercise.util.FileUtil;

import java.util.List;

public class ErrorActivity extends AppCompatActivity {
    private ImageView iv_progress;
    private TextView tv_title;
    private ListView lv;
    private List<ErrorInfo> errorList;
    private UserDBHelper mDB;
    private List<Choose> singleList;
    private List<Choose> multipleList;
    private List<Judge> judgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        initData();
        initView();
    }

    private void initData() {
        mDB = UserDBHelper.getInstance(this);
        int type = getIntent().getIntExtra("type", -1);
        errorList = mDB.queryByType(type);
        singleList = FileUtil.getSingleList(this);
        multipleList = FileUtil.getMultipleList(this);
        judgeList = FileUtil.getJudgeList(this);
    }

    private void initView() {
        iv_progress = findViewById(R.id.iv_progress);
        tv_title = findViewById(R.id.tv_title);
        lv = findViewById(R.id.lv_single);

        iv_progress.setVisibility(View.GONE);
        tv_title.setText(R.string.error_statistic);
        ListItemAdapter adapter = new ListItemAdapter(this, errorList);
        lv.setAdapter(adapter);
    }

}