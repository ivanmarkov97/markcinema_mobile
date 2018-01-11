package com.example.ivan.markcinema.PayActivityRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Ticket;
import com.example.ivan.markcinema.R;

import java.util.ArrayList;

/**
 * Created by Ivan on 05.01.2018.
 */

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    ArrayList<Ticket> tickets;
    Context context;

    public TicketAdapter(ArrayList<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_view, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.filmName.setText(ticket.getSeance().getFilm());
        holder.hallName.setText(ticket.getSeance().getHall());
        holder.row.setText("Row: " + String.valueOf(ticket.getRow()));
        holder.place.setText("Place: " + String.valueOf(ticket.getPlace()));
        holder.cost.setText("Cost: " + String.valueOf(ticket.getSeance().getCost()) + " P");
        holder.time.setText(parseTime(ticket.getSeance().getTime()));
        //holder.time.setText(ticket.getSeance().getTime());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder{

        TextView filmName;
        TextView hallName;
        TextView cost;
        TextView row;
        TextView place;
        TextView age;
        TextView time;

        public TicketViewHolder(View itemView) {
            super(itemView);

            filmName = (TextView) itemView.findViewById(R.id.ticket_view_film);
            hallName = (TextView) itemView.findViewById(R.id.ticket_view_hall);
            cost = (TextView) itemView.findViewById(R.id.ticket_view_cost);
            row = (TextView) itemView.findViewById(R.id.ticket_view_row);
            place = (TextView) itemView.findViewById(R.id.ticket_view_place);
            age = (TextView) itemView.findViewById(R.id.ticket_view_age);
            time = (TextView) itemView.findViewById(R.id.ticket_view_time);
        }
    }

    public String parseTime(String t){
        String mtime = "";
        boolean flag = false;
        int index = 0;
        int countDots = 0;
        for(int i = 0; i < t.length(); i++) {
            if (String.valueOf(t.charAt(i)).equals(" ")) {
                flag = true;
                index = i;
                break;
            }
        }
        for(int i = index + 1; i < t.length(); i++){
            if (String.valueOf(t.charAt(i)).equals(":")) {
                countDots++;
                if(countDots == 2){
                    break;
                }
            }
            if(flag) {
                mtime += String.valueOf(t.charAt(i));
            }
        }
        return mtime;
    }
}
