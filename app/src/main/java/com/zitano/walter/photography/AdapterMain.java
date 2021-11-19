package com.zitano.walter.photography;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMain {
    Context c;
    ArrayList<Model_Main> models;

    public AdapterMain(Context c, ArrayList<Model_Main> models) {
        this.c = c;
        this.models = models;
    }

  /*  @NonNull
    @Override
    public Holder_Main onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //convert xml to view obj
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_main, null);
        return new Holder_Main(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_Main holder, int position) {
        //bind data to our views
        holder.mTitleTv.setText(models.get(position).getTitle());
        holder.mDescrTv.setText(models.get(position).getDescription());
        holder.mImageIv.setImageResource(models.get(position).getImg());

        //animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (models.get(pos).getTitle().equals("Portraits")){
                   Intent intent = new Intent(c, Portraits.class);
                   c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Outdoor")){
                    Intent intent = new Intent(c, Outdoor.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Studio/Indoors")){
                    Intent intent = new Intent(c, Studio_Indoors.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Fashion Photography")){
                    Intent intent = new Intent(c, Fashion_Photography.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Baby Bump")){
                    Intent intent = new Intent(c, Baby_Bump.class);
                    c.startActivity(intent);
                }
                if (models.get(pos).getTitle().equals("Traditional")){
                    Intent intent = new Intent(c, Traditional.class);
                    c.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }*/
}
