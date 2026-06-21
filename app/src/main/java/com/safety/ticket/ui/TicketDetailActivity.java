package com.safety.ticket.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.safety.ticket.R;
import com.safety.ticket.data.Photo;
import com.safety.ticket.data.Signature;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.utils.DateTimeUtil;

import java.util.List;

public class TicketDetailActivity extends AppCompatActivity {
    private long ticketId;
    private Ticket ticket;
    private TicketDao ticketDao;

    private TextView tvType, tvNo, tvStatus, tvApplyUnit, tvLocation, tvContent, tvRisk, tvTime;
    private LinearLayout layoutSignatures, layoutPhotos, layoutMeasures;
    private Button btnEdit, btnApprove, btnPhoto, btnPrint, btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        ticketId = getIntent().getLongExtra("ticket_id", -1);
        ticketDao = new TicketDao(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initViews() {
        tvType = findViewById(R.id.tvType);
        tvNo = findViewById(R.id.tvNo);
        tvStatus = findViewById(R.id.tvStatus);
        tvApplyUnit = findViewById(R.id.tvApplyUnit);
        tvLocation = findViewById(R.id.tvLocation);
        tvContent = findViewById(R.id.tvContent);
        tvRisk = findViewById(R.id.tvRisk);
        tvTime = findViewById(R.id.tvTime);
        layoutSignatures = findViewById(R.id.layoutSignatures);
        layoutPhotos = findViewById(R.id.layoutPhotos);
        layoutMeasures = findViewById(R.id.layoutMeasures);
        btnEdit = findViewById(R.id.btnEdit);
        btnApprove = findViewById(R.id.btnApprove);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnPrint = findViewById(R.id.btnPrint);
        btnComplete = findViewById(R.id.btnComplete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, TicketCreateActivity.class);
                intent.putExtra("ticket_id", ticketId);
                startActivity(intent);
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApproveDialog();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, PhotoActivity.class);
                intent.putExtra("ticket_id", ticketId);
                startActivity(intent);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, PrintActivity.class);
                intent.putExtra("ticket_id", ticketId);
                startActivity(intent);
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompleteDialog();
            }
        });
    }

    private void loadData() {
        ticket = ticketDao.getTicketById(ticketId);
        if (ticket == null) {
            Toast.makeText(this, "作业票不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ticketDao.loadTicketDetails(ticket);

        tvType.setText(ticket.getTypeText());
        tvNo.setText(ticket.getTicketNo());
        tvStatus.setText(ticket.getStatusText());
        tvApplyUnit.setText(ticket.getApplyUnit());
        tvLocation.setText(ticket.getLocation());
        tvContent.setText(ticket.getContent());
        tvRisk.setText(ticket.getRiskAnalysis());
        tvTime.setText("作业时间：" + ticket.getWorkTimeStart() + " 至 " + ticket.getWorkTimeEnd());

        // 更新按钮状态
        updateButtonStatus();
        // 显示签字
        showSignatures();
        // 显示照片
        showPhotos();
        // 显示安全措施
        showMeasures();
    }

    private void updateButtonStatus() {
        btnEdit.setVisibility(ticket.getStatus() == Ticket.STATUS_DRAFT ? View.VISIBLE : View.GONE);
        btnApprove.setVisibility(ticket.getStatus() == Ticket.STATUS_PENDING || ticket.getStatus() == Ticket.STATUS_APPROVING ? View.VISIBLE : View.GONE);
        btnComplete.setVisibility(ticket.getStatus() == Ticket.STATUS_APPROVED ? View.VISIBLE : View.GONE);
        btnPrint.setVisibility(View.VISIBLE);
    }

    private void showSignatures() {
        layoutSignatures.removeAllViews();
        List<Signature> signatures = ticket.getSignatures();
        for (Signature s : signatures) {
            View view = getLayoutInflater().inflate(R.layout.item_signature_detail, layoutSignatures, false);
            TextView tvType = view.findViewById(R.id.tvSignType);
            TextView tvPerson = view.findViewById(R.id.tvSignPerson);
            TextView tvTime = view.findViewById(R.id.tvSignTime);
            TextView tvComment = view.findViewById(R.id.tvSignComment);
            ImageView ivSign = view.findViewById(R.id.ivSignImage);

            tvType.setText(s.getTypeText());
            tvPerson.setText(s.getSignPerson());
            tvTime.setText(s.getSignTime());
            tvComment.setText(s.getSignComment());

            if (s.getSignImage() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(s.getSignImage(), 0, s.getSignImage().length);
                ivSign.setImageBitmap(bmp);
                ivSign.setVisibility(View.VISIBLE);
            } else {
                ivSign.setVisibility(View.GONE);
            }
            layoutSignatures.addView(view);
        }
    }

    private void showPhotos() {
        layoutPhotos.removeAllViews();
        List<Photo> photos = ticket.getPhotos();
        for (Photo p : photos) {
            View view = getLayoutInflater().inflate(R.layout.item_photo_detail, layoutPhotos, false);
            TextView tvDesc = view.findViewById(R.id.tvPhotoDesc);
            ImageView ivPhoto = view.findViewById(R.id.ivPhoto);
            tvDesc.setText(p.getPhotoDesc() + " " + p.getTakeTime());

            Bitmap bmp = BitmapFactory.decodeFile(p.getPhotoPath());
            if (bmp != null) {
                ivPhoto.setImageBitmap(bmp);
            }
            layoutPhotos.addView(view);
        }
    }

    private void showMeasures() {
        layoutMeasures.removeAllViews();
        for (com.safety.ticket.data.SafetyMeasure m : ticket.getMeasures()) {
            TextView tv = new TextView(this);
            tv.setText(m.getMeasureNo() + ". " + m.getMeasureContent() + " [" + m.getInvolvedText() + "] " + m.getConfirmPerson());
            tv.setPadding(8, 8, 8, 8);
            layoutMeasures.addView(tv);
        }
    }

    private void showApproveDialog() {
        String[] items = {"作业负责人审批", "所在单位（车间）审批", "安全管理部门审批", "厂领导/总工程师审批"};
        String[] types = {Signature.TYPE_APPROVER1, Signature.TYPE_APPROVER2, Signature.TYPE_APPROVER3, Signature.TYPE_APPROVER4};

        // 根据当前状态判断可审批环节
        int currentLevel = 0;
        for (String t : types) {
            if (ticketDao.getSignatureByType(ticketId, t) != null) {
                currentLevel++;
            }
        }

        if (currentLevel >= items.length) {
            Toast.makeText(this, "所有审批已完成", Toast.LENGTH_SHORT).show();
            return;
        }

        final int level = currentLevel;
        new AlertDialog.Builder(this)
                .setTitle("审批确认")
                .setMessage("当前进行：" + items[level])
                .setPositiveButton("去签字审批", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TicketDetailActivity.this, SignatureActivity.class);
                        intent.putExtra("ticket_id", ticketId);
                        intent.putExtra("sign_type", types[level]);
                        intent.putExtra("sign_title", items[level]);
                        intent.putExtra("is_approve", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showCompleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("完工验收")
                .setMessage("确认作业已完工并验收？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TicketDetailActivity.this, SignatureActivity.class);
                        intent.putExtra("ticket_id", ticketId);
                        intent.putExtra("sign_type", Signature.TYPE_FINAL);
                        intent.putExtra("sign_title", "完工验收");
                        intent.putExtra("is_approve", true);
                        intent.putExtra("is_complete", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
