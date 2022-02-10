package com.android.mp3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private ArrayList<String> list;
    private Context context;

    MusicAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        final String filePath = list.get(position);
        final int index = position;
        Log.i("MusicAdapter", "onBindViewHolder: " + filePath);
        final String title = filePath.substring(filePath.lastIndexOf("/") + 1);
        holder.tvFileName.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicActivity.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("FILE_PATH", filePath);
                intent.putExtra("POSITION", index);
                intent.putExtra("LIST", list);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;

        MusicViewHolder(View itemView) {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tv_file_name);

        }


    }
}
