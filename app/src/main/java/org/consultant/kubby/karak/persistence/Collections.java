package org.consultant.kubby.karak.persistence;



import org.consultant.kubby.karak.model.DocumentObject;
import org.consultant.kubby.karak.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abdul on 10/14/15.
 */
public class Collections {

    /***********************************************************************************************
     *
     */
    private static final String API_KEY = "XXXXXXXXX";

    /***********************************************************************************************
     *
     */
    private static final String DB_API_URL = "https://api.mongolab.com/api/1/databases/kobby";

    /***********************************************************************************************
     *
     * @param collectionName
     * @param clazz
     * @return
     * @throws Exception
     */
    protected static List listCollection(String collectionName, Class clazz) throws Exception{
        String url = DB_API_URL + "/collections/" + collectionName + "?apiKey=" + API_KEY;

        String jsonStr = Utils.fetchUrl(url);
        return Utils.parseDocObjList(jsonStr, clazz);
    }
}
