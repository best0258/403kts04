package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainAdapter extends FirebaseRecyclerAdapter<Notfood,MainAdapter.myViewHolaer>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Notfood> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolaer holder, int position, @NonNull Notfood model) {
        holder.nametext.setText( model.getName());

    }

    @NonNull
    @Override
    public myViewHolaer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_iten,parent,false);
        return new myViewHolaer(view);
    }

    class myViewHolaer extends RecyclerView.ViewHolder{
        TextView nametext;

        public myViewHolaer(@NonNull View itemView) {
            super(itemView);

            nametext = (TextView)itemView.findViewById(R.id.coursetext);
        }
    }

}