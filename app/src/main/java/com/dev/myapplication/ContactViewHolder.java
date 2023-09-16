package com.dev.myapplication;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
class ContactViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvdesc,tv_remaining;
    CountDownTimer timer;

    ContactViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.title);
        tvdesc = itemView.findViewById(R.id.description);
        tv_remaining = itemView.findViewById(R.id.remainingtime);
    }
}