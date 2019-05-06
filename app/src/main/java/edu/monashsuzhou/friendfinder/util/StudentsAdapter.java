package edu.monashsuzhou.friendfinder.util;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.activity.MyFriends;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    List<MyFriends.Student> students;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv_student;
        TextView tv_fName;
        TextView tv_fSuburb;
        TextView tv_fMovie;
        ImageView iv_fGender;
        ImageButton iv_detail;
        ImageButton iv_add;


        public MyViewHolder(View v) {
            super(v);
            cv_student = (CardView) v.findViewById(R.id.cv_student);
            tv_fName = (TextView) v.findViewById(R.id.tv_fName);
            tv_fSuburb = (TextView) v.findViewById(R.id.tv_fSuburb);
            tv_fMovie = (TextView) v.findViewById(R.id.tv_fMovie);
            iv_fGender = (ImageView) v.findViewById(R.id.iv_fGender);
            iv_detail = (ImageButton) v.findViewById(R.id.iv_detail);
            iv_add = (ImageButton) v.findViewById(R.id.iv_add);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentsAdapter(List<MyFriends.Student> students) {
        this.students = students;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.student_card, parent, false);
//        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_fName.setText(students.get(i).fName);
        holder.iv_fGender.setImageResource(students.get(i).fGender);
        holder.tv_fSuburb.setText("  Suburb: " + students.get(i).fSuburb);
        holder.tv_fMovie.setText("  Movie: " + students.get(i).fMovie);

        holder.iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                Log.i("======","解析info格式化字符串，查电影");
                builder.setMessage(students.get(i).info);

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Add a new friend").setMessage("Are you sure you want to add " + students.get(i).fName + " as your friend");

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // do nothing
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("==========","服务器通信");
                        // User clicked OK button
                    }
                });
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }


}
