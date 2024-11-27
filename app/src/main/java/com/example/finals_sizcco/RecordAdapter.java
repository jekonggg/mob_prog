package com.example.finals_sizcco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> recordList;

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.price.setText(record.getPrice());
        holder.date.setText(record.getDate());
        holder.time.setText(record.getTime());
        holder.notes.setText(record.getNotes());
        holder.category.setText(record.getCategory());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    // ViewHolder class to bind views
    public class RecordViewHolder extends RecyclerView.ViewHolder {
        public TextView price, date, time, notes, category;

        public RecordViewHolder(View view) {
            super(view);
            price = view.findViewById(R.id.tvPrice);
            date = view.findViewById(R.id.tvDate);
            time = view.findViewById(R.id.tvTime);
            notes = view.findViewById(R.id.tvNotes);
            category = view.findViewById(R.id.tvCategory);
        }
    }
}
