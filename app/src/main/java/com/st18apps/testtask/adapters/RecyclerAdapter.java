package com.st18apps.testtask.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.st18apps.testtask.R;
import com.st18apps.testtask.model.User;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<User> userArrayList;

    public RecyclerAdapter(List<User> articleArrayList) {
        this.userArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(userArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        if (userArrayList == null)
            return 0;
        return userArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView info;
        CircleImageView userImage;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewDetailName);
            description = itemView.findViewById(R.id.textViewDescription);
            info = itemView.findViewById(R.id.textViewInfo);
            userImage = itemView.findViewById(R.id.imageView);
        }

        void bind(User user) {

            name.setText(user.getName());
            description.setText(user.getEmail());
            info.setText(user.getCompany().getCatchPhrase());

            getAvatar(user.getId());
        }

        void getAvatar(int id){
            Uri uri = Uri.parse("https://avatars.io/twitter/" + id);
                Glide.with(itemView.getContext())
                        .load(uri)
                        .fitCenter()
                        .thumbnail(0.5f)
                        .priority(Priority.IMMEDIATE)
                        .placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(userImage);
        }
    }
}
