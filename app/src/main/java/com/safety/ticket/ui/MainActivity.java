package com.safety.ticket.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.safety.ticket.R;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.data.TicketTemplate;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private TicketAdapter adapter;
    private List<Ticket> ticketList = new ArrayList<>();
    private TicketDao ticketDao;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("安全管理作业票系统");

        ticketDao = new TicketDao(this);
        listView = findViewById(R.id.listView);
        tvEmpty = findViewById(R.id.tvEmpty);

        adapter = new TicketAdapter(this, ticketList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ticket ticket = ticketList.get(position);
                Intent intent = new Intent(MainActivity.this, TicketDetailActivity.class);
                intent.putExtra("ticket_id", ticket.getId());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(ticketList.get(position));
                return true;
            }
        });

        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTickets();
    }

    private void loadTickets() {
        ticketList.clear();
        ticketList.addAll(ticketDao.getAllTickets());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(ticketList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showCreateDialog() {
        final String[] items = {"高处作业票", "有限空间作业票", "动火作业票"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择作业票类型");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type;
                switch (which) {
                    case 0: type = Ticket.TYPE_HEIGHT; break;
                    case 1: type = Ticket.TYPE_SPACE; break;
                    case 2: type = Ticket.TYPE_FIRE; break;
                    default: type = Ticket.TYPE_HEIGHT;
                }
                createNewTicket(type);
            }
        });
        builder.show();
    }

    private void createNewTicket(String type) {
        Ticket ticket = new Ticket();
        ticket.setTicketType(type);
        ticket.setTicketNo(TicketTemplate.generateTicketNo(type));
        ticket.setApplyTime(DateTimeUtil.getNowFull());
        ticket.setStatus(Ticket.STATUS_DRAFT);
        ticket.setCreateTime(DateTimeUtil.getNowFull());
        ticket.setUpdateTime(DateTimeUtil.getNowFull());

        long ticketId = ticketDao.insertTicket(ticket);

        List<SafetyMeasure> measures;
        if (Ticket.TYPE_HEIGHT.equals(type)) {
            measures = TicketTemplate.getHeightMeasures();
        } else if (Ticket.TYPE_SPACE.equals(type)) {
            measures = TicketTemplate.getSpaceMeasures();
        } else {
            measures = TicketTemplate.getFireMeasures();
        }
        ticketDao.insertMeasures(ticketId, measures);

        Intent intent = new Intent(this, TicketCreateActivity.class);
        intent.putExtra("ticket_id", ticketId);
        startActivity(intent);
    }

    private void showDeleteDialog(final Ticket ticket) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage("删除后将无法恢复，是否确认删除？");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ticketDao.deleteTicket(ticket.getId());
                loadTickets();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTickets(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadTickets();
                } else {
                    searchTickets(newText);
                }
                return true;
            }
        });
        return true;
    }

    private void searchTickets(String keyword) {
        ticketList.clear();
        ticketList.addAll(ticketDao.searchTickets(keyword));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(ticketList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
