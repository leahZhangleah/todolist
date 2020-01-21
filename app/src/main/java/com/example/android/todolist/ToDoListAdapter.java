package com.example.android.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> implements ItemTouchHelperAdapter{
    List<Event> events;
    private RecyclerViewItemClickLister mListener;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    private String LOG_TAG = ToDoListAdapter.class.getName();
    public interface RecyclerViewItemClickLister{
        void onClick(int position);
    }

    public ToDoListAdapter(RecyclerViewItemClickLister listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item,parent,false);
        ToDoViewHolder toDoViewHolder = new ToDoViewHolder(rootView);
        Log.d(LOG_TAG,"on create view holder method is called");
        return toDoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.onBind(events.get(position));
        Log.d(LOG_TAG,"on bind view holder method is called");
    }

    @Override
    public int getItemCount() {
        if (events!=null){
            return events.size();
        }
        return 0;
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView descriptionTv, priorityTv,timeStampTv;
        public ToDoViewHolder(View itemView) {
            super(itemView);
            descriptionTv = itemView.findViewById(R.id.to_do_description);
            priorityTv = itemView.findViewById(R.id.priority_symbol);
            timeStampTv = itemView.findViewById(R.id.time_stamp);
            itemView.setOnClickListener(this);
        }
        public void onClick(View view){
            int position = events.get(getAdapterPosition()).getId();
            mListener.onClick(position);
        }
        public void onBind(Event event){
            if (event!=null){
                descriptionTv.setText(event.getEvent());
                priorityTv.setText(String.valueOf(event.getPriorityLevel()));
                String time = formatter.format(event.getDate());
                timeStampTv.setText(time);
                Log.d(LOG_TAG,"on bind method is called");

            }
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition){
            for (int i= fromPosition; i < toPosition; i++){
                Collections.swap(events,i,i+1);
            }
        }else{
            for (int i = fromPosition; i>toPosition; i--){
                Collections.swap(events,i,i-1);
            }
        }
        notifyItemMoved(fromPosition,toPosition);
        //todo
    }

    @Override
    public void onItemDismiss(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
        Log.d(LOG_TAG,"set event method is called");
    }

    public List<Event> getEvents() {
        Log.i(LOG_TAG,"the new events are: "+events);
        return events;
    }
}
