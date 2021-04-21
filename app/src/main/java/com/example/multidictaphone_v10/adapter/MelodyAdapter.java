package com.example.multidictaphone_v10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.models.Melody;

import java.util.List;

public class MelodyAdapter extends RecyclerView.Adapter<MelodyAdapter.MelodyViewHolder> {

    Context context;
    List<Melody> melodyList;

    public MelodyAdapter(Context context, List<Melody> melodyList) {
        this.context = context;
        this.melodyList = melodyList;
    }

    @NonNull
    @Override
    public MelodyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.melodyitem, parent, false);

        return new MelodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MelodyViewHolder holder, int position) {

        holder.melodyName.setText(melodyList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return melodyList.size();
    }

    public static final class MelodyViewHolder extends RecyclerView.ViewHolder{

        TextView melodyName;

        public MelodyViewHolder(@NonNull View itemView) {

            super(itemView);

            melodyName = itemView.findViewById(R.id.melodyName_textView);
        }
    }
}
