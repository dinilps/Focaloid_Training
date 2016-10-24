package com.focaloid.focaloid_training;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {


    Button addevent;
    EditText edtitle,edcomment;
    TextView txtDate,txtTime;
    double houre,mini;
    Spinner spinplace;
    String location;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int REQUEST_CODE=001;
    Calendar cal;

    private OnFragmentInteractionListener mListener;

    public EventFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.event, container, false);


        ((MainActivity) getActivity()).setActionBarTitle("Event");

        //getActivity().getActionBar().setTitle("Event");

        edtitle=(EditText)view.findViewById(R.id.edtxtEventName);
        txtDate=(TextView) view.findViewById(R.id.edtxtDate);
        txtTime=(TextView) view.findViewById(R.id.edtxtTime);
        edcomment=(EditText)view.findViewById(R.id.edtxtComment);
        spinplace=(Spinner)view.findViewById(R.id.spinnerPlace) ;
        addevent = (Button) view.findViewById(R.id.btnAddEvent);

        spinplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                location=String.valueOf(spinplace.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCalendarEvent();

            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            String time="A.M";
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay>=12) {
                                    if(hourOfDay>12)
                                        hourOfDay = hourOfDay - 12;
                                    if(hourOfDay==12)
                                        hourOfDay=12;
                                    time = "P.M";
                                }


                                txtTime.setText(hourOfDay + ":" + minute+" "+time);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addCalendarEvent(){


        String title=edtitle.getText().toString();
        String comment=edcomment.getText().toString();

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", title);
        intent.putExtra("description", comment);
        intent.putExtra("eventLocation", location);
        edtitle.clearComposingText();
        edcomment.clearComposingText();
        startActivity(intent);



    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
