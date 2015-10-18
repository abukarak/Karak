package org.consultant.kubby.karak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import org.consultant.kubby.karak.model.Symptom;
import org.consultant.kubby.karak.model.Topic;
import org.consultant.kubby.karak.persistence.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String DEFAULT_SYMPT_TEXT = "ادخل الاعراض...";

    private List topicsFoundList = new ArrayList<>(10);

    /***********************************************************************************************
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InitRepositoryTask().execute();

        setupSymptomsAutocompleter();
        setupTopicsListView();
        setupFindButton();

    }


    private void findTopics() {
        Log.e("FIND_TOPICS", " Finding topics...");
        try {
            List<Symptom> selectedSymptoms = getSelectedSymptoms();
            Log.e("FIND_TOPICS"," Selected symptomcs: " + selectedSymptoms);
            List foundTopics = Repository.getInstance().getTopicsByAllSymptoms(selectedSymptoms);
            topicsFoundList.clear();
            topicsFoundList.addAll(foundTopics);
            ListView listView = (ListView)findViewById(R.id.topicListView);
            ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
        }catch (Exception ex) {
            Log.e("MAIN_ACTIVITY", ex.getMessage(), ex);
        }

    }

    private List<Symptom> getSelectedSymptoms() throws Exception {
        MultiAutoCompleteTextView symptomsAutocompleter =
                (MultiAutoCompleteTextView)findViewById(R.id.symptomsAutoCompleter);
        String selection = symptomsAutocompleter.getText().toString().trim();

        if( selection.isEmpty() ) {
            return new ArrayList<>();
        }

        String[] symptomPartials = selection.split(",");
        return Repository.getInstance().symptomsFromNamePartials(Arrays.asList(symptomPartials));
    }

    /***********************************************************************************************
     *
     */

    private void setupFindButton() {
        Button findButton = (Button)findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findTopics();
            }
        });
    }
    /***********************************************************************************************
     *
     */
    private void setupTopicsListView() {
        final ListView listView = (ListView)findViewById(R.id.topicListView);
        final ArrayAdapter<String> topicsFoundAdaptor = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                topicsFoundList);

        listView.setAdapter(topicsFoundAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = (Topic) parent.getAdapter().getItem(position);
                Log.i("MAIN_ACTIVITY", "TOPIC CLICKED:" + topic);
                showTopic(topic);

            }
        });
    }

    private void showTopic(Topic topic) {
        Intent myIntent = new Intent(MainActivity.this, ShowTopicActivity.class);
        myIntent.putExtra("topicTitle", topic.getTitle());
        myIntent.putExtra("topicSymptoms", topic.getSymptomsPrettyString());
        MainActivity.this.startActivity(myIntent);
    }

    /***********************************************************************************************
     *
     */
    private void setupSymptomsAutocompleter() {
        final MultiAutoCompleteTextView symptomsAutocompleter =
                (MultiAutoCompleteTextView)findViewById(R.id.symptomsAutoCompleter);

        symptomsAutocompleter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        symptomsAutocompleter.setThreshold(1);


     }


    /***********************************************************************************************
     *
     */
    private class InitRepositoryTask extends AsyncTask<String, Void, String> {

        ProgressDialog progress = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... params) {
            try {

                Repository repository = Repository.getInstance();
                repository.setApplicationContext(getApplicationContext());
                repository.initDatabase();

            } catch (Exception ex ) {
                Log.e("MAIN_ACTIVITY", ex.getMessage(), ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                MultiAutoCompleteTextView symptomsAutocompleter =
                        (MultiAutoCompleteTextView)findViewById(R.id.symptomsAutoCompleter);

                Log.i("MAIN_ACTIVITY", "Looking up symptoms...");
                List symptoms = Repository.getInstance().listSymptoms();
                Log.i("MAIN_ACTIVITY", "Found symptoms:\n" + symptoms);

                symptomsAutocompleter.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        R.layout.support_simple_spinner_dropdown_item, symptoms));

                progress.dismiss();
            }catch( Exception ex) {
                Log.e("MAIN_ACTIVITY", ex.getMessage(), ex);
            }

        }

        @Override
        protected void onPreExecute() {

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
            // To dismiss the dialog


        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
