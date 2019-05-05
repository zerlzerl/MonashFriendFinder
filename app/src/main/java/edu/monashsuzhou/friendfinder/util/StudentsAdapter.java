package edu.monashsuzhou.friendfinder.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public CardView cv_student;
        public TextView tv_fName;
        public TextView tv_fSuburb;
        public TextView tv_fMovie;
        public ImageView iv_fGender;

        public MyViewHolder(View v) {
            super(v);
            cv_student = (CardView) v.findViewById(R.id.cv_student);
            tv_fName = (TextView) v.findViewById(R.id.tv_fName);
            tv_fSuburb = (TextView) v.findViewById(R.id.tv_fSuburb);
            tv_fMovie = (TextView) v.findViewById(R.id.tv_fMovie);
            iv_fGender = (ImageView) v.findViewById(R.id.iv_fGender);
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
        holder.tv_fSuburb.setText(students.get(i).fSuburb);
        holder.tv_fMovie.setText(students.get(i).fMovie);
        holder.iv_fGender.setImageResource(students.get(i).fGender);
        holder.tv_fName.setText(students.get(i).fName);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }
}
