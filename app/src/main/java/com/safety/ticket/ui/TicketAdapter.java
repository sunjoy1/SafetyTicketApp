package com.safety.ticket.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.safety.ticket.R;
import com.safety.ticket.data.Ticket;

import java.util.List;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<Ticket> tickets;

    public TicketAdapter(Context context, List<Ticket> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tickets.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
            holder = new ViewHolder();
            holder.tvType = convertView.findViewById(R.id.tvType);
            holder.tvNo = convertView.findViewById(R.id.tvNo);
            holder.tvStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ticket ticket = tickets.get(position);
        holder.tvType.setText(ticket.getTypeText());
        holder.tvNo.setText(ticket.getTicketNo());
        holder.tvStatus.setText(ticket.getStatusText());
        holder.tvTime.setText(ticket.getCreateTime());

        int color;
        switch (ticket.getStatus()) {
            case Ticket.STATUS_DRAFT: color = 0xFF9E9E9E; break;
            case Ticket.STATUS_PENDING: color = 0xFF2196F3; break;
            case Ticket.STATUS_APPROVING: color = 0xFFFF9800; break;
            case Ticket.STATUS_APPROVED: color = 0xFF4CAF50; break;
            case Ticket.STATUS_REJECTED: color = 0xFFF44336; break;
            case Ticket.STATUS_COMPLETED: color = 0xFF673AB7; break;
            default: color = 0xFF9E9E9E;
        }
        holder.tvStatus.setTextColor(color);

        return convertView;
    }

    static class ViewHolder {
        TextView tvType, tvNo, tvStatus, tvTime;
    }
}
