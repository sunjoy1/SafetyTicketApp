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
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.Signature;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.TicketDao;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.SafetyMeasure;
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
            Toast.makeText(this, "浣滀笟绁ㄤ笉瀛樺湪", Toast.LENGTH_SHORT).show();
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
        tvTime.setText("浣滀笟鏃堕棿锛? + ticket.getWorkTimeStart() + " 鑷?" + ticket.getWorkTimeEnd());

        // 鏇存柊鎸夐挳鐘舵€?        updateButtonStatus();
        // 鏄剧ず绛惧瓧
        showSignatures();
        // 鏄剧ず鐓х墖
        showPhotos();
        // 鏄剧ず瀹夊叏鎺柦
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
        String[] items = {"浣滀笟璐熻矗浜哄鎵?, "鎵€鍦ㄥ崟浣嶏紙杞﹂棿锛夊鎵?, "瀹夊叏绠＄悊閮ㄩ棬瀹℃壒", "鍘傞瀵?鎬诲伐绋嬪笀瀹℃壒"};
        String[] types = {Signature.TYPE_APPROVER1, Signature.TYPE_APPROVER2, Signature.TYPE_APPROVER3, Signature.TYPE_APPROVER4};

        // 鏍规嵁褰撳墠鐘舵€佸垽鏂彲瀹℃壒鐜妭
        int currentLevel = 0;
        for (String t : types) {
            if (ticketDao.getSignatureByType(ticketId, t) != null) {
                currentLevel++;
            }
        }

        if (currentLevel >= items.length) {
            Toast.makeText(this, "鎵€鏈夊鎵瑰凡瀹屾垚", Toast.LENGTH_SHORT).show();
            return;
        }

        final int level = currentLevel;
        new AlertDialog.Builder(this)
                .setTitle("瀹℃壒纭")
                .setMessage("褰撳墠杩涜锛? + items[level])
                .setPositiveButton("鍘荤瀛楀鎵?, new DialogInterface.OnClickListener() {
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
                .setNegativeButton("鍙栨秷", null)
                .show();
    }

    private void showCompleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("瀹屽伐楠屾敹")
                .setMessage("纭浣滀笟宸插畬宸ュ苟楠屾敹锛?)
                .setPositiveButton("纭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TicketDetailActivity.this, SignatureActivity.class);
                        intent.putExtra("ticket_id", ticketId);
                        intent.putExtra("sign_type", Signature.TYPE_FINAL);
                        intent.putExtra("sign_title", "瀹屽伐楠屾敹");
                        intent.putExtra("is_approve", true);
                        intent.putExtra("is_complete", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("鍙栨秷", null)
                .show();
    }
}

