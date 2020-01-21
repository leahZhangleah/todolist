package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.todolist.dagger.EventComponent;
import com.example.android.todolist.dagger.EventModule;
import com.example.android.todolist.dagger.DaggerEventComponent;

import java.util.Date;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity {
    EditText noteTv;
    RadioButton highPriorityBtn, mediumPriorityBtn, lowPriorityBtn;
    RadioGroup priorityGroup;
    Button submitBtn;
    int DEFAULT_SELECT_EVENT_ID = -1;
    int selectedEventId = DEFAULT_SELECT_EVENT_ID;
    int selectedId;
    private int LOW_PRIORITY_ID = 1;
    private int MEDIUM_PRIORITY_ID = 2;
    private int HIGH_PRIORITY_ID = 3;
    private AppDatabase mDb;
    private String LOG_TAG = DetailActivity.class.getName();
    @Inject
    public Repository mRepository;
    private EventComponent mEventComponent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mEventComponent = DaggerEventComponent.builder().eventModule(new EventModule(getApplication())).build();
        mEventComponent.inject(this);
        initViews();
        mDb = AppDatabase.getsInstance(this);
        if (savedInstanceState!=null && savedInstanceState.containsKey("selectedEventId")){
            selectedEventId = savedInstanceState.getInt("selectedEventId");
        }
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("selectedPosition")){
            if (selectedEventId == DEFAULT_SELECT_EVENT_ID){
                selectedEventId = intent.getIntExtra("selectedPosition",DEFAULT_SELECT_EVENT_ID);
                setTitle("Edit");
                loadEventById(selectedEventId);
            }
        }else{
            setTitle("Add");
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedEventId",selectedEventId);
    }

    private void initViews(){
        noteTv = findViewById(R.id.event_description);
        priorityGroup = findViewById(R.id.priority_group);
        submitBtn = findViewById(R.id.submit_button);
        highPriorityBtn = findViewById(R.id.high_priority);
        mediumPriorityBtn = findViewById(R.id.medium_priority);
        lowPriorityBtn = findViewById(R.id.low_priority);

    }

    private int getPriorityLevel(){
        int priorityId = LOW_PRIORITY_ID;
        if (priorityGroup.getCheckedRadioButtonId()!=-1){
            selectedId = priorityGroup.getCheckedRadioButtonId();
            switch (selectedId){
                case R.id.high_priority:
                    priorityId = HIGH_PRIORITY_ID;
                    break;
                case R.id.medium_priority:
                    priorityId = MEDIUM_PRIORITY_ID;
                    break;
                case R.id.low_priority:
                    priorityId = LOW_PRIORITY_ID;
                    break;
            }
        }
        return priorityId;
    }

    private void setPriorityLevel(int level){
        if (level == HIGH_PRIORITY_ID){
            highPriorityBtn.setChecked(true);
        }else if(level == MEDIUM_PRIORITY_ID){
            mediumPriorityBtn.setChecked(true);
        }else if (level == LOW_PRIORITY_ID){
            lowPriorityBtn.setChecked(true);
        }
    }

    private void loadEventById(final int selectedEventId){
        Log.d(LOG_TAG,"load event by id is called");
        DetailViewModelFactory factory = new DetailViewModelFactory(mRepository,selectedEventId);
        DetailViewModel detailViewModel = ViewModelProviders.of(this,factory).get(DetailViewModel.class);
        final LiveData<Event> eventLiveData = detailViewModel.loadEventById();
        eventLiveData.observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
                //because detail activity is one time load
                eventLiveData.removeObserver(this);
                populateUI(event);
            }
        });
    }

    private void addEvent(){
        Log.d(LOG_TAG,"add event method is called");
        final String note = noteTv.getText().toString();
        final int priorityId = getPriorityLevel();
        final Date date = new Date();
        AppExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (selectedEventId == DEFAULT_SELECT_EVENT_ID){
                    Event event = new Event(priorityId,note,date);
                    mDb.getDao().insertEvent(event);
                    Log.d(LOG_TAG," the event is added to the database");
                }else{
                    final Event event = new Event(selectedEventId,priorityId,note,date);
                    mDb.getDao().updateEvent(event);
                    Log.d(LOG_TAG," the event is updated in the database");
                }
            }
        });
    }

    private void populateUI(Event event){
        noteTv.setText(event.getEvent());
        setPriorityLevel(event.getPriorityLevel());
    }

}
