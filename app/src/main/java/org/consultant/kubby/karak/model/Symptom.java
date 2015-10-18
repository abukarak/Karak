package org.consultant.kubby.karak.model;

/**
 * Created by abdul on 10/14/15.
 */
public class Symptom extends DocumentObject {

    /***********************************************************************************************
     *
     * @return
     */
    public String getName() {
        return (String)getProperty("name");
    }


    /***********************************************************************************************
     *
     * @return
     */
    public String getDescription() {
        return (String)getProperty("description");
    }


    /***********************************************************************************************
     *
     * @return
     */
    public String toString() {
        return getName();
    }

}
