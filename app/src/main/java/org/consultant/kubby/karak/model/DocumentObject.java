package org.consultant.kubby.karak.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by abdul on 10/14/15.
 */
public class DocumentObject extends JSONObject{

    /***********************************************************************************************
     *
     * @return
     */
    public String getId() {
        return (String)getProperty("_id");
    }

    /***********************************************************************************************
     *
     * @param id
     */
    public void setId(String id) {
        setProperty("_id", id);
    }

    /***********************************************************************************************
     *
     * @param name
     * @param val
     */
    public void setProperty(String name, Object val) {
        try {
            put(name, val);
        } catch (JSONException je) {
            throw new RuntimeException(je);
        }
    }

    /***********************************************************************************************
     *
     * @param name
     * @return
     */
    public Object getProperty(String name) {
        try {
            return get(name);
        } catch (JSONException je) {
            throw new RuntimeException(je);
        }

    }

    /***********************************************************************************************
     *
     * @param name
     * @return
     */
    public List getArrayProperty(String name) {

        try {
            JSONArray jsonArray = (JSONArray)getProperty(name);
            List items = new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                items.add(jsonArray.get(i));
            }
            return items;

        }catch (JSONException je) {
            throw new RuntimeException(je);
        }

    }

    /***********************************************************************************************
     *
     * @param other
     * @throws JSONException
     */
    public void putAll(JSONObject other) throws JSONException{
        for( Iterator<String> it = other.keys() ; it.hasNext();) {
            String key = it.next();
            setProperty(key, other.get(key));
        }
    }
}
