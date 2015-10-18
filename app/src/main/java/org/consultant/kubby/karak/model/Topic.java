package org.consultant.kubby.karak.model;

import org.consultant.kubby.karak.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 10/14/15.
 */
public class Topic extends DocumentObject {

    public String getTitle() {
        return (String)getProperty("title");
    }

    public void setTitle(String title) {
        setProperty("title", title);
    }

    public List<Symptom> getSymptoms() throws Exception{
        List<String> symptomIds = getSymptomIds();
        return Repository.getInstance().symptomsFromIds(symptomIds);
    }

    public String getSymptomsPrettyString(){
        try {
            List<Symptom> symptoms = getSymptoms();
            StringBuilder sb = new StringBuilder();
            for(Symptom symptom: symptoms) {
                sb.append(symptom.getName());
                sb.append("\n");
            }

            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public List<String> getSymptomIds() {
        return (List)getArrayProperty("symptoms");
    }

    public void setSymptoms(List<Symptom> symptoms) {
        List<String> symptomIds = new ArrayList<>();
        for( Symptom s : symptoms) {
            symptomIds.add(s.getId());
        }

        setProperty("symptoms", symptomIds);
    }

    public boolean hasAllSymptoms(List<Symptom> symptoms) {
        List<String> mySymptomIds = getSymptomIds();
        List<String> symptomIds = new ArrayList<>();
        for( Symptom s : symptoms) {
            symptomIds.add(s.getId());
        }

        return mySymptomIds.containsAll(symptomIds);
    }

    public boolean hasAnySymptoms(List<Symptom> symptoms) {
        List<String> mySymptomIds = getSymptomIds();
        for( Symptom s: symptoms) {
            if( mySymptomIds.contains(s.getId())) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        return getTitle();
    }

}
