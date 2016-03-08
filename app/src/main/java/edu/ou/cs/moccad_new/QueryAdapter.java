package edu.ou.cs.moccad_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 3/8/16.
 */
public class QueryAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public QueryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(QueryDetail object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        QueryHolder queryHolder;
        View row = convertView;

        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.query_list_layout, parent, false);
            queryHolder = new QueryHolder();
            queryHolder.tx_logid        = ( TextView ) row.findViewById(R.id.tx_log_id);
            queryHolder.tx_patid        = ( TextView ) row.findViewById(R.id.tx_pat_id);
            queryHolder.tx_docid        = ( TextView ) row.findViewById(R.id.tx_doc_id);
            queryHolder.tx_date         = ( TextView ) row.findViewById(R.id.tx_log_date);
            queryHolder.tx_diagnosis    = ( TextView ) row.findViewById(R.id.tx_diagnosis);

            row.setTag(queryHolder);
        }
        else {
            queryHolder = (QueryHolder) row.getTag();
        }

        QueryDetail queryDetail = (QueryDetail) this.getItem(position);
        queryHolder.tx_logid.setText(queryDetail.getLog_id());
        queryHolder.tx_patid.setText("Pat ID\n" + queryDetail.getPat_id());
        queryHolder.tx_docid.setText("Doc ID\n" + queryDetail.getDoc_id());
        queryHolder.tx_date.setText("Date: " + queryDetail.getLog_date());
        queryHolder.tx_diagnosis.setText("Diagnosis:\n" + queryDetail.getDiagnosis());


        return row;
    }

    static class QueryHolder {

        TextView tx_logid, tx_patid, tx_docid, tx_date, tx_diagnosis;

    }

}
