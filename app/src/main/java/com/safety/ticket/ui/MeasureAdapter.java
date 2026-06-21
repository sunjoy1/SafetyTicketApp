package com.safety.ticket.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.safety.ticket.R;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.Ticket;

import java.util.List;

public class MeasureAdapter extends BaseAdapter {
    private Context context;
    private List<SafetyMeasure> measures;

    public MeasureAdapter(Context context, List<SafetyMeasure> measures) {
        this.context = context;
        this.measures = measures;
    }

    @Override
    public int getCount() {
        return measures.size();
    }

    @Override
    public Object getItem(int position) {
        return measures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return measures.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_measure, parent, false);
            holder = new ViewHolder();
            holder.tvNo = convertView.findViewById(R.id.tvNo);
            holder.tvContent = convertView.findViewById(R.id.tvContent);
            holder.rgInvolved = convertView.findViewById(R.id.rgInvolved);
            holder.etConfirm = convertView.findViewById(R.id.etConfirm);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SafetyMeasure measure = measures.get(position);
        holder.tvNo.setText(String.valueOf(measure.getMeasureNo()));
        holder.tvContent.setText(measure.getMeasureContent());

        switch (measure.getIsInvolved()) {
            case SafetyMeasure.INVOLVED_NO:
                holder.rgInvolved.check(R.id.rbNo);
                break;
            case SafetyMeasure.INVOLVED_YES:
                holder.rgInvolved.check(R.id.rbYes);
                break;
            case SafetyMeasure.INVOLVED_NA:
                holder.rgInvolved.check(R.id.rbNa);
                break;
        }
        holder.etConfirm.setText(measure.getConfirmPerson());

        holder.rgInvolved.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNo) {
                    measure.setIsInvolved(SafetyMeasure.INVOLVED_NO);
                } else if (checkedId == R.id.rbYes) {
                    measure.setIsInvolved(SafetyMeasure.INVOLVED_YES);
                } else if (checkedId == R.id.rbNa) {
                    measure.setIsInvolved(SafetyMeasure.INVOLVED_NA);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvNo, tvContent;
        RadioGroup rgInvolved;
        EditText etConfirm;
    }
}

