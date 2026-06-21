package com.safety.ticket.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.safety.ticket.R;
import com.safety.ticket.data.Photo;
import com.safety.ticket.data.SafetyMeasure;
import com.safety.ticket.data.Signature;
import com.safety.ticket.data.Ticket;
import com.safety.ticket.data.TicketDao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PrintActivity extends AppCompatActivity {
    private long ticketId;
    private Ticket ticket;
    private TicketDao ticketDao;

    private Button btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        ticketId = getIntent().getLongExtra("ticket_id", -1);
        ticketDao = new TicketDao(this);
        ticket = ticketDao.getTicketById(ticketId);
        if (ticket != null) {
            ticketDao.loadTicketDetails(ticket);
        }

        btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrint();
            }
        });
    }

    private void doPrint() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = ticket.getTypeText() + "_" + ticket.getTicketNo();
        printManager.print(jobName, new TicketPrintDocumentAdapter(), null);
    }

    private class TicketPrintDocumentAdapter extends PrintDocumentAdapter {
        private PrintedPdfDocument pdfDocument;
        private int totalPages = 1;

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle extras) {
            pdfDocument = new PrintedPdfDocument(PrintActivity.this, newAttributes);
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            callback.onLayoutFinished(new PrintDocumentInfo.Builder("ticket.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalPages)
                    .build(), true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback callback) {
            PdfDocument.Page page = pdfDocument.startPage(0);
            Canvas canvas = page.getCanvas();
            drawTicketContent(canvas);
            pdfDocument.finishPage(page);

            try {
                pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.getMessage());
                return;
            } finally {
                pdfDocument.close();
            }
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }
    }

    private void drawTicketContent(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(28);

        int x = 40, y = 60;
        int lineHeight = 36;

        // 标题
        paint.setTextSize(36);
        paint.setFakeBoldText(true);
        canvas.drawText(ticket.getTypeText(), x, y, paint);
        y += lineHeight + 10;

        paint.setTextSize(24);
        paint.setFakeBoldText(false);

        // 基本信息
        canvas.drawText("编号：" + ticket.getTicketNo(), x, y, paint); y += lineHeight;
        canvas.drawText("申请单位：" + nvl(ticket.getApplyUnit()), x, y, paint); y += lineHeight;
        canvas.drawText("作业地点：" + nvl(ticket.getLocation()), x, y, paint); y += lineHeight;
        canvas.drawText("作业内容：" + nvl(ticket.getContent()), x, y, paint); y += lineHeight;
        canvas.drawText("风险辨识：" + nvl(ticket.getRiskAnalysis()), x, y, paint); y += lineHeight;
        canvas.drawText("作业时间：" + nvl(ticket.getWorkTimeStart()) + " 至 " + nvl(ticket.getWorkTimeEnd()), x, y, paint); y += lineHeight + 10;

        // 安全措施
        paint.setFakeBoldText(true);
        canvas.drawText("安全措施确认：", x, y, paint); y += lineHeight;
        paint.setFakeBoldText(false);

        List<SafetyMeasure> measures = ticket.getMeasures();
        for (SafetyMeasure m : measures) {
            String text = m.getMeasureNo() + ". " + m.getMeasureContent() + " [" + m.getInvolvedText() + "]";
            if (m.getConfirmPerson() != null && !m.getConfirmPerson().isEmpty()) {
                text += " 确认人：" + m.getConfirmPerson();
            }
            canvas.drawText(text, x + 10, y, paint);
            y += lineHeight;
            if (y > 700) {
                canvas.drawText("(后续内容见下一页)", x, y, paint);
                break;
            }
        }
        y += 10;

        // 签字区域（带签字图片）
        paint.setFakeBoldText(true);
        canvas.drawText("签字审批：", x, y, paint); y += lineHeight;
        paint.setFakeBoldText(false);

        List<Signature> signatures = ticket.getSignatures();
        for (Signature s : signatures) {
            String label = s.getTypeText() + "：" + nvl(s.getSignPerson()) + " " + nvl(s.getSignTime());
            if (s.getSignComment() != null && !s.getSignComment().isEmpty()) {
                label += " 意见：" + s.getSignComment();
            }
            canvas.drawText(label, x + 10, y, paint);
            y += lineHeight;

            // 绘制签字图片
            if (s.getSignImage() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(s.getSignImage(), 0, s.getSignImage().length);
                if (bmp != null) {
                    int signWidth = 200;
                    int signHeight = (int) (signWidth * ((float) bmp.getHeight() / bmp.getWidth()));
                    if (signHeight > 80) signHeight = 80;
                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, signWidth, signHeight, true);
                    canvas.drawBitmap(scaled, x + 400, y - lineHeight, paint);
                    scaled.recycle();
                }
            }

            if (y > 750) break;
        }

        // 现场照片区域
        if (y < 700) {
            y += 20;
            paint.setFakeBoldText(true);
            canvas.drawText("现场照片：", x, y, paint); y += lineHeight;
            paint.setFakeBoldText(false);

            List<Photo> photos = ticket.getPhotos();
            int photoX = x;
            for (Photo p : photos) {
                Bitmap bmp = BitmapFactory.decodeFile(p.getPhotoPath());
                if (bmp != null) {
                    int pw = 180;
                    int ph = (int) (pw * ((float) bmp.getHeight() / bmp.getWidth()));
                    if (ph > 140) ph = 140;
                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, pw, ph, true);
                    canvas.drawBitmap(scaled, photoX, y, paint);
                    canvas.drawText(p.getPhotoDesc(), photoX, y + ph + 20, paint);
                    photoX += 200;
                    if (photoX > 500) {
                        photoX = x;
                        y += 170;
                        if (y > 700) break;
                    }
                    scaled.recycle();
                }
            }
        }
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }
}
