package org.consultant.kubby.karak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ShowTopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        String topicTitle = extras.getString("topicTitle");
        String topicSymptoms = extras.getString("topicSymptoms");

        TextView topicTitleView =  (TextView)findViewById(R.id.topicTitle);
        topicTitleView.setText(topicTitle);

        TextView topicSymptomsView =  (TextView)findViewById(R.id.topicSymptoms);
        topicSymptomsView.setText(topicSymptoms);

    }

}
