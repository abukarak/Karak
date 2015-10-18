package org.consultant.kubby.karak.persistence;

import android.content.Context;
import android.util.Log;

import org.consultant.kubby.karak.model.DocumentObject;
import org.consultant.kubby.karak.model.Symptom;
import org.consultant.kubby.karak.model.Topic;
import org.consultant.kubby.karak.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abdul on 10/14/15.
 */
public class Repository {

    /***********************************************************************************************
     * CONSTANTS
     */
    private static final String SYMPTOMS_FILE = "symptoms.json";

    private static final String TOPICS_FILE = "topics.json";

    /***********************************************************************************************
     *
     */

    private Context context;
    private Repository() throws Exception{

    }

    /***********************************************************************************************
     *
     */
    private static  Repository REPOSITORY = null;

    /***********************************************************************************************
     *
     * @return
     */
    public static Repository getInstance() throws Exception{
        if( REPOSITORY == null ) {
            REPOSITORY = new Repository();
        }

        return REPOSITORY;
    }

    public void setApplicationContext(Context context) {
        this.context = context;
    }

    /***********************************************************************************************
     *
     */

    /***********************************************************************************************
     * symptoms
     */
    private Map<String, Symptom> symptomMap = new HashMap<>();

    /***********************************************************************************************
     * topics
     */

    private Map<String, Topic> topicMap = new HashMap<>();


    /***********************************************************************************************
     *
     */
    public void initDatabase() throws Exception {
        try {
            loadDatabaseFromCollections();
            saveFileDatabase();
        } catch (Exception ex) {
            Log.e("REPOSITORY", "Failed to load entities from collections: " + ex.getMessage(), ex);
            Log.i("REPOSITORY", "Fall back to file repo");
            loadDatabaseFromFile();
        }
    }

    private void loadDatabaseFromCollections() throws Exception {
        Log.i("REPOSITORY", "Load database from collections...");
        List<Symptom> symptoms = Collections.listCollection("symptoms", Symptom.class);
        setSymptoms(symptoms);

        List<Topic> topics = Collections.listCollection("topics", Topic.class);
        setTopics(topics);


    }

    private void saveFileDatabase() throws Exception {

        Log.i("REPOSITORY", "Saving file database...");

        File symptomsFile = new File(context.getFilesDir(), SYMPTOMS_FILE);

        Utils.writeDocObjList(new ArrayList<DocumentObject>(symptomMap.values()), symptomsFile);

        File topicsFile = new File(context.getFilesDir(), TOPICS_FILE);
        Utils.writeDocObjList(new ArrayList<DocumentObject>(topicMap.values()), topicsFile);
    }

    private void loadDatabaseFromFile() throws Exception {

        Log.i("REPOSITORY", "Load database from files...");
        File symptomsFile = new File(context.getFilesDir(), SYMPTOMS_FILE);
        if (!symptomsFile.exists()) {
            throw new Exception("Local symptoms file repository files does not exist");
        }

        File topicsFile = new File(context.getFilesDir(), TOPICS_FILE);
        if (!topicsFile.exists()) {
            throw new Exception("Local topics file repository files does not exist");
        }

        List<Symptom> symptoms = Utils.readDocObjList(symptomsFile, Symptom.class);
        setSymptoms(symptoms);

        List<Topic> topics = Utils.readDocObjList(topicsFile, Topic.class);
        setTopics(topics);


    }

    private void setSymptoms(List<Symptom> symptoms) {
        symptomMap.clear();
        for( Symptom symptom : symptoms) {
            symptomMap.put(symptom.getId(), symptom);
        }
    }

    private void setTopics(List<Topic> topics) {
        topicMap.clear();
        for( Topic topic : topics) {
            topicMap.put(topic.getId(), topic);
        }
    }
    /***********************************************************************************************
     *
     * @return
     */
    public List<Symptom> listSymptoms() {

        return new ArrayList<>(symptomMap.values());
    }

    /***********************************************************************************************
     *
     * @param id
     * @return
     */
    public Symptom getSymptomById(String id) {
        return symptomMap.get(id);
    }


    /***********************************************************************************************
     *
     * @param symptomIds
     * @return
     */
    public List<Symptom> symptomsFromIds(List<String> symptomIds) {
        List<Symptom> symptoms = new ArrayList<>(symptomIds.size());
        for( String id: symptomIds) {
            symptoms.add(getSymptomById(id));
        }
        return symptoms;
    }


    /***********************************************************************************************
     *
     * @param namePartials
     * @return
     */
    public List<Symptom> symptomsFromNamePartials(List<String> namePartials) {
        List<Symptom> result = new ArrayList<>();

        for (Symptom symptom : listSymptoms()) {
            for(String partial : namePartials) {
                if( symptom.getName().contains(partial)) {
                    result.add(symptom);
                }
            }
        }

        return result;
    }




    /***********************************************************************************************
     *
     * @param symptoms
     * @return
     */
    public List<Topic> getTopicsByAnySymptoms(List<Symptom> symptoms) {
        List<Topic> result = new ArrayList<>();
        for (Topic topic : topicMap.values()) {
            if( topic.hasAnySymptoms(symptoms)) {
                result.add(topic);
            }
        }

        return result;

    }

    public List<Topic> getTopicsByAllSymptoms(List<Symptom> symptoms) {
        List<Topic> result = new ArrayList<>();
        for (Topic topic : topicMap.values()) {
            if( topic.hasAllSymptoms(symptoms)) {
                result.add(topic);
            }
        }

        return result;

    }


}
