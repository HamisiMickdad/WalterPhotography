package com.zitano.walter.photography;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.textview.MaterialTextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

    }

    //set details to recyclerview row
    public void setDetails(Context ctx, String title, String description, String image) {
        //views
        MaterialTextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        MaterialTextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        AppCompatImageView mImageIv = mView.findViewById(R.id.rImageView);

        //set data to views
        mTitleTv.setText(title);
        mDetailTv.setText(description);
        mTitleTv.setVisibility(View.GONE);
        mDetailTv.setVisibility(View.GONE);
        //Picasso.get().load(image).into(mImageIv);

        Glide.with(ctx).load(image)
                .thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageIv);
    }
    private ViewHolder.ClickListener mClickListener;

    // Interface to handle callbacks
    public interface  ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener (ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

