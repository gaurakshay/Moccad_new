package edu.ou.cs.moccad_new;

/**
 * Created by akshay on 3/7/16.
 */
public class QueryDetail {

    String log_id;
    String pat_id;
    String doc_id;
    String log_date;
    String diagnosis;
    String field;

    public QueryDetail ( String logid, String patid, String docid, String date, String diag ){
        setLog_id(logid);
        setPat_id(patid);
        setDoc_id(docid);
        setLog_date(date);
        setDiagnosis(diag);
    }

    public QueryDetail(String field){
        setField(field);
    }

    public String getField(){
        return this.field;
    }

    public void setField(String field){
        this.field = field;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

}
