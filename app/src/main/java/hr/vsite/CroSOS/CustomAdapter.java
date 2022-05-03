package hr.vsite.CroSOS;

import static hr.vsite.CroSOS.EditUserActivity.ARG_PERSON_ID;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    public final String[] mDataSet;
    public final String[] idDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String id;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(v1 -> {
                Intent intent = new Intent(v1.getContext(), EditUserActivity.class);
                intent.putExtra(ARG_PERSON_ID, id);
                v1.getContext().startActivity(intent);

            });
            textView = v.findViewById(R.id.txtUser);
        }

        public TextView getTextView() {
            return textView;
        }

        public String getId() {
            return id;
        }

        public void setId(String idP) {
            id = idP;
        }
    }

    public CustomAdapter(String[] dataSet, String[] dataSetId) {
        mDataSet = dataSet;
        idDataSet = dataSetId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(mDataSet[position]);
        viewHolder.setId(idDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}