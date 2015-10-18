package org.consultant.kubby.karak.utils;

import android.util.JsonReader;
import android.util.Log;

import org.consultant.kubby.karak.model.DocumentObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 10/14/15.
 */
public class Utils {


    public static List readDocObjList(File file, Class clazz) throws Exception{

        Log.i("UTILS", "Reading from file:" + file.getName());
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        String str = sb.toString();
        Log.i("UTILS", "READ SUCCESS File:" + file.getName());
        return parseDocObjList(str, clazz);

    }

    public static void writeDocObjList(List<? extends DocumentObject> docList, File file) throws Exception{
        FileOutputStream out = new FileOutputStream(file);
        OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
        JSONArray array = new JSONArray();
        int i = 0;
        for( DocumentObject doc : docList) {
            array.put(i, doc);
            i++;

        }
        String str = array.toString();
        Log.i("UTILS", "Writing to file:" + file.getName());
        ow.write(str);
        ow.flush();
        ow.close();

        Log.i("UTILS", "WRITE SUCCESS File:" + file.getName());



    }

    public static List parseDocObjList(String jsonStr, Class clazz) throws Exception{
        JSONArray jsonArray = parseJsonArray(jsonStr);

        List items = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            DocumentObject documentObject = (DocumentObject)clazz.newInstance();
            documentObject.putAll(jsonObject);
            items.add(documentObject);
        }

        return items;
    }
    /***********************************************************************************************
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static JSONArray parseJsonArray(String json) throws JSONException{
        return new JSONArray(json);
    }
    /***********************************************************************************************
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String fetchUrl(String url) throws Exception{
        URL urlObj = new URL(url);
        URLConnection urlConnection = urlObj.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        return readFully(in, "UTF-8");
    }

    /***********************************************************************************************
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws IOException
     */
    public static  String readFully(InputStream inputStream, String encoding)
            throws IOException {
        return new String(readFully(inputStream), encoding);
    }

    /***********************************************************************************************
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] readFully(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toByteArray();
    }
}
