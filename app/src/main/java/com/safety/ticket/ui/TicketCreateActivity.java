package com.safety.ticket.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.safety.ticket.R;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.Signature;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.data.TicketTemplate;
import com.safety.ticket.utils.DateTimeUtil;

import java.util.List;

public class TicketCreateActivity extends AppCompatActivity {
    private long ticketId;
    private Ticket ticket;
    private TicketDao ticketDao;

    private EditText etTicketNo, etApplyUnit, etLocation, etContent, etRiskAnalysis;
    private EditText etWorkTimeStart, etWorkTimeEnd;
    private Spinner spLevel, spFireMethod;
    private LinearLayout layoutFireMethod;
    private ListView listMeasures;
    private TextView tvTicketType;
    private Button btnSave, btnSubmit, btnPhoto, btnSign;

    private MeasureAdapter measureAdapter;
    private List<SafetyMeasure> measures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_create);

        ticketId = getIntent().getLongExtra("ticket_id", -1);
        if (ticketId == -1) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ticketDao = new TicketDao(this);
        ticket = ticketDao.getTicketById(ticketId);
        if (ticket == null) {
            Toast.makeText(this, "作业票不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ticketDao.loadTicketDetails(ticket);
        measures = ticket.getMeasures();

        initViews();
        bindData();
    }

    private void initViews() {
        tvTicketType = findViewById(R.id.tvTicketType);
        etTicketNo = findViewById(R.id.etTicketNo);
        etApplyUnit = findViewById(R.id.etApplyUnit);
        etLocation = findViewById(R.id.etLocation);
        etContent = findViewById(R.id.etContent);
        etRiskAnalysis = findViewById(R.id.etRiskAnalysis);
        etWorkTimeStart = findViewById(R.id.etWorkTimeStart);
        etWorkTimeEnd = findViewById(R.id.etWorkTimeEnd);
        spLevel = findViewById(R.id.spLevel);
        spFireMethod = findViewById(R.id.spFireMethod);
        layoutFireMethod = findViewById(R.id.layoutFireMethod);
        listMeasures = findViewById(R.id.listMeasures);
        btnSave = findViewById(R.id.btnSave);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnSign = findViewById(R.id.btnSign);

        tvTicketType.setText(ticket.getTypeText());

        // 设置作业级别下拉
        if (Ticket.TYPE_HEIGHT.equals(ticket.getTicketType())) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, TicketTemplate.getHeightLevels());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLevel.setAdapter(adapter);
            spLevel.setPrompt("选择高处作业级别");
        } else if (Ticket.TYPE_FIRE.equals(ticket.getTicketType())) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, TicketTemplate.getFireLevels());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLevel.setAdapter(adapter);
            spLevel.setPrompt("选择动火作业级别");

            layoutFireMethod.setVisibility(View.VISIBLE);
            ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, TicketTemplate.getFireMethods());
            methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFireMethod.setAdapter(methodAdapter);
        } else {
            spLevel.setVisibility(View.GONE);
        }

        measureAdapter = new MeasureAdapter(this, measures);
        listMeasures.setAdapter(measureAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTicket(false);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTicket();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketCreateActivity.this, PhotoActivity.class);
                intent.putExtra("ticket_id", ticketId);
                startActivity(intent);
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignDialog();
            }
        });
    }

    private void bindData() {
        etTicketNo.setText(ticket.getTicketNo());
        etApplyUnit.setText(ticket.getApplyUnit());
        etLocation.setText(ticket.getLocation());
        etContent.setText(ticket.getContent());
        etRiskAnalysis.setText(ticket.getRiskAnalysis());
        etWorkTimeStart.setText(ticket.getWorkTimeStart());
        etWorkTimeEnd.setText(ticket.getWorkTimeEnd());
    }

    private void saveTicket(boolean submit) {
        ticket.setApplyUnit(etApplyUnit.getText().toString().trim());
        ticket.setLocation(etLocation.getText().toString().trim());
        ticket.setContent(etContent.getText().toString().trim());
        ticket.setRiskAnalysis(etRiskAnalysis.getText().toString().trim());
        ticket.setWorkTimeStart(etWorkTimeStart.getText().toString().trim());
        ticket.setWorkTimeEnd(etWorkTimeEnd.getText().toString().trim());
        ticket.setUpdateTime(DateTimeUtil.getNowFull());

        if (submit) {
            ticket.setStatus(Ticket.STATUS_PENDING);
        }

        ticketDao.updateTicket(ticket);

        // 更新安全措施
        for (SafetyMeasure m : measures) {
            ticketDao.updateMeasure(m);
        }

        if (submit) {
            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitTicket() {
        if (etApplyUnit.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请填写申请单位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请填写作业地点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etContent.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请填写作业内容", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("确认提交")
                .setMessage("提交后将进入审批流程，是否确认？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTicket(true);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showSignDialog() {
        final String[] items = {"申请人签字", "监护人签字", "作业人签字"};
        final String[] types = {Signature.TYPE_CREATOR, Signature.TYPE_SUPERVISOR, Signature.TYPE_WORKER};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择签字类型");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TicketCreateActivity.this, SignatureActivity.class);
                intent.putExtra("ticket_id", ticketId);
                intent.putExtra("sign_type", types[which]);
                intent.putExtra("sign_title", items[which]);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
