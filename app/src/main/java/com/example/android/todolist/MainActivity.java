package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.android.todolist.dagger.EventComponent;
import com.example.android.todolist.dagger.EventModule;
import com.example.android.todolist.dagger.DaggerEventComponent;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ToDoListAdapter.RecyclerViewItemClickLister{
    private ToDoListAdapter adapter;
    private AppDatabase mDb;
    private String LOG_TAG = MainActivity.class.getName();
    @Inject
    public Repository mRepository;
    private EventComponent mEventComponent;
    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.getDao().deleteEvent(adapter.getEvents().get(viewHolder.getAdapterPosition()));
                }
            });
            //adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventComponent = DaggerEventComponent.builder()
                .eventModule(new EventModule(getApplication())).build();
        mEventComponent.inject(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        mDb = AppDatabase.getsInstance(this);
        Log.d(LOG_TAG,"on create method is called and database is initialised");


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ToDoListAdapter(this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });
        loadData();
    }

    private void loadData(){
        Log.d(LOG_TAG,"load data method is called");
        //this will run out of ui thread
        MainViewModelFactory factory = new MainViewModelFactory(mRepository);
        MainViewModel mainViewModel = ViewModelProviders.of(this,factory).get(MainViewModel.class);
        LiveData<List<Event>> events= mainViewModel.getEvents();
        events.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                //this will run on ui thread
                adapter.setEvents(events);
                Log.d(LOG_TAG,"data is loaded to adapter");
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG,"on resume method is called");
        super.onResume();

    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("selectedPosition",position);
        startActivity(intent);
    }
}
