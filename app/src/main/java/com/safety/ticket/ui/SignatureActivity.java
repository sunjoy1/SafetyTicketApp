package com.safety.ticket.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.safety.ticket.R;
import com.safety.ticket.data.Signature;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.utils.DateTimeUtil;

public class SignatureActivity extends AppCompatActivity {
    private long ticketId;
    private String signType;
    private String signTitle;
    private boolean isApprove;
    private boolean isComplete;
    private TicketDao ticketDao;

    private SignatureView signatureView;
    private EditText etPersonName, etComment;
    private TextView tvTitle;
    private Button btnClear, btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        ticketId = getIntent().getLongExtra("ticket_id", -1);
        signType = getIntent().getStringExtra("sign_type");
        signTitle = getIntent().getStringExtra("sign_title");
        isApprove = getIntent().getBooleanExtra("is_approve", false);
        isComplete = getIntent().getBooleanExtra("is_complete", false);

        if (ticketId == -1 || signType == null) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ticketDao = new TicketDao(this);

        initViews();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        signatureView = findViewById(R.id.signatureView);
        etPersonName = findViewById(R.id.etPersonName);
        etComment = findViewById(R.id.etComment);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tvTitle.setText(signTitle);
        if (isApprove) {
            etComment.setHint("请输入审批意见（如：同意作业）");
        }

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSignature();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveSignature() {
        String personName = etPersonName.getText().toString().trim();
        if (personName.isEmpty()) {
            Toast.makeText(this, "请输入签字人姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!signatureView.isSigned()) {
            Toast.makeText(this, "请手写签字", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] signBytes = signatureView.getSignatureBytes();
        if (signBytes == null) {
            Toast.makeText(this, "签字数据获取失败", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = etComment.getText().toString().trim();

        Signature existing = ticketDao.getSignatureByType(ticketId, signType);
        Signature signature = new Signature();
        signature.setTicketId(ticketId);
        signature.setSignType(signType);
        signature.setSignPerson(personName);
        signature.setSignTime(DateTimeUtil.getNowFull());
        signature.setSignImage(signBytes);
        signature.setSignComment(comment);

        if (existing != null) {
            signature.setId(existing.getId());
            ticketDao.updateSignature(signature);
        } else {
            ticketDao.insertSignature(signature);
        }

        // 更新作业票状态
        Ticket ticket = ticketDao.getTicketById(ticketId);
        if (ticket != null) {
            if (isComplete) {
                ticket.setStatus(Ticket.STATUS_COMPLETED);
            } else if (isApprove) {
                if (signType.equals(Signature.TYPE_APPROVER4)) {
                    ticket.setStatus(Ticket.STATUS_APPROVED);
                } else {
                    ticket.setStatus(Ticket.STATUS_APPROVING);
                }
            }
            ticket.setUpdateTime(DateTimeUtil.getNowFull());
            ticketDao.updateTicket(ticket);
        }

        Toast.makeText(this, "签字保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
