package com.example.mars.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mars.R;
import com.example.mars.data.EntranceTestData;

import java.util.ArrayList;

public class AdapterEntranceTest extends RecyclerView.Adapter<AdapterEntranceTest.MyViewHolder> {
    Context _context;
    ArrayList<EntranceTestData> _qusData;

    public AdapterEntranceTest(Context entranceTestActivity, ArrayList<EntranceTestData> qusData) {
        this._context = entranceTestActivity;
        this._qusData = qusData;
    }

    @NonNull
    @Override
    public AdapterEntranceTest.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_entrance_course_list, parent, false);

        return new AdapterEntranceTest.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_question.setText(String.valueOf((position+1)) +" "+_qusData.get(position).getQuestion());
        for (int i = 0; i < holder.rg_options.getChildCount(); i++) {
            ((RadioButton) holder.rg_options.getChildAt(i)).setText(_qusData.get(position).getOptionList().get(i));
        }
        if (_qusData.get(position).getUserSubmitAnswer().equalsIgnoreCase("A")) {
            ((RadioButton) holder.rg_options.getChildAt(0)).setChecked(true);
        } else if (_qusData.get(position).getUserSubmitAnswer().equalsIgnoreCase("B")) {
            ((RadioButton) holder.rg_options.getChildAt(1)).setChecked(true);

        } else if (_qusData.get(position).getUserSubmitAnswer().equalsIgnoreCase("C")) {
            ((RadioButton) holder.rg_options.getChildAt(2)).setChecked(true);

        } else if (_qusData.get(position).getUserSubmitAnswer().equalsIgnoreCase("D")) {
            ((RadioButton) holder.rg_options.getChildAt(3)).setChecked(true);
        }
        holder.rg_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        _qusData.get(position).setUserSubmitAnswer("");
                        break;
                    case R.id.rb_op_1:
                        _qusData.get(position).setUserSubmitAnswer("A");
                        break;
                    case R.id.rb_op_2:
                        _qusData.get(position).setUserSubmitAnswer("B");
                        break;
                    case R.id.rb_op_3:
                        _qusData.get(position).setUserSubmitAnswer("C");
                        break;
                    case R.id.rb_op_4:
                        _qusData.get(position).setUserSubmitAnswer("D");
                        break;
                }
            }
        });


    }

    public ArrayList<EntranceTestData> getData() {
        return _qusData;
    }

    @Override
    public int getItemCount() {
        return _qusData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioGroup rg_options;
        TextView tv_question;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rg_options = itemView.findViewById(R.id.rg_options);
            tv_question = itemView.findViewById(R.id.tv_question);

        }
    }


}
