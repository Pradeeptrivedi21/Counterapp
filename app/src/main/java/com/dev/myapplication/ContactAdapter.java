package com.dev.myapplication;

import static com.dev.myapplication.appconfig.BIRTHDAY_DATE;
import static com.dev.myapplication.appconfig.BIRTHDAY_EVENT_NOTES;
import static com.dev.myapplication.appconfig.BIRTHDAY_EVENT_TIME;
import static com.dev.myapplication.appconfig.FRIEND_NAME;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder>
        implements Filterable {
    private Context context;
    private ArrayList<Contacts> listContacts;
    private ArrayList<Contacts> mArrayList;
    private SqliteDatabase mDatabase;

    CountDownTimer timer;
    onRefresh refresh;

    ContactAdapter(Context context, ArrayList<Contacts> listContacts) {
        this.context = context;
        this.listContacts = listContacts;
        this.mArrayList = listContacts;
        mDatabase = new SqliteDatabase(context);
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout, parent, false);
        return new ContactViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Contacts contacts = listContacts.get(position);
        holder.tvName.setText(contacts.getName());
        holder.tvdesc.setText(contacts.getDescription());
//        holder.tvPhoneNum.setText(contacts.getDuratiom());
        if (holder.timer != null) {
            holder.timer.cancel();
        }
        Date curDate = new Date();
        long curMillis = curDate.getTime();
        long oldTimes = Long.parseLong(listContacts.get(position).getDuratiom());

        long remianingtime=oldTimes-curMillis;
        long timer = TimeUnit.MILLISECONDS.toSeconds(remianingtime);


        Log.e("oldTimes",String.valueOf(oldTimes));
        Log.e("curMillis",String.valueOf(curMillis));
        Log.e("remianingtime",String.valueOf(remianingtime));
        Log.e("oldMiles",String.valueOf(timer));
        Log.e("date",listContacts.get(position).getDate());
        Log.e("time",listContacts.get(position).getTime());

        timer = timer*1000;
        holder.timer = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {

                long uptime = millisUntilFinished;

                long days = TimeUnit.MILLISECONDS
                        .toDays(uptime);
                uptime -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS
                        .toHours(uptime);
                uptime -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS
                        .toMinutes(uptime);
                uptime -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS
                        .toSeconds(uptime);

                Log.e("remaining","days :"+days+" minutes :"+minutes+" seconds:"+seconds);
                holder.tv_remaining.setText("Remain: "+"days :"+days+"Hour :"+hours+" minutes :"+minutes+" seconds:"+seconds);
            }

            public void onFinish() {
                holder.tv_remaining.setText("Remaining : "+"00:00:00");
                if(listContacts.size()>0){
                    mDatabase.deleteContact(contacts.getId());
                    refresh.onClick(listContacts.get(position));
                }


            }
        }.start();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listContacts = mArrayList;
                }
                else {
                    ArrayList<Contacts> filteredList = new ArrayList<>();
                    for (Contacts contacts : mArrayList) {
                        if (contacts.getName().toLowerCase().contains(charString)) {
                            filteredList.add(contacts);
                        }
                    }
                    listContacts = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listContacts;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listContacts = (ArrayList<Contacts>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    public void setRefresh(onRefresh refresh) {
        this.refresh = refresh;
    }

    public interface onRefresh{
        public void onClick(Contacts contacts);
    }
}