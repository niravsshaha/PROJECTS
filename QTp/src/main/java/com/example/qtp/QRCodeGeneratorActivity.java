package com.example.qtp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtp.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.qtp.models.User;
import com.example.qtp.utils.AESHelper;
import com.example.qtp.utils.Constants;
import com.example.qtp.utils.Utils;

public class QRCodeGeneratorActivity extends BaseActivity {

    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;

    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.etAmount)
    EditText etAmount;

    @BindView(R.id.etMPIN)
    EditText etMPIN;

    @BindView(R.id.llAccountDetails)
    LinearLayout llAccountDetails;

    @BindView(R.id.llQRCode)
    LinearLayout llQRCode;

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;

    @BindView(R.id.tvExpiry)
    TextView tvExpiry;

    @BindView(R.id.pbBalance)
    ProgressBar pbBalance;

    DatabaseReference userRef;
    ValueEventListener listener;
    private double availableAmount = -1;
    private String secureMPIN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QRCodeGeneratorActivity.this);

        String uid = sp.getString(Constants.SP_KEY_UID, null);
        final String balanceText = getResources().getString(R.string.text_balance);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    availableAmount = user.getBalance();
                    secureMPIN = user.getMpin();

                    pbBalance.setVisibility(View.GONE);
                    tvBalance.setText(String.format(balanceText, availableAmount));
                } else {
                    pbBalance.setVisibility(View.GONE);
                    tvBalance.setText(R.string.text_balance_error);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (uid != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference(Constants.USERS).child(uid).child(Constants.USER_INFO);

            userRef.addValueEventListener(listener);
        } else {
            Toast.makeText(QRCodeGeneratorActivity.this, "Please login", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(listener);
    }

    @OnClick(R.id.bCreateQrCode)
    void validateAccountDetails() {

        if (availableAmount == -1) {
            Toast.makeText(QRCodeGeneratorActivity.this, "Wait till balance gets updated...", Toast.LENGTH_SHORT).show();
        } else {
            String amount = etAmount.getText().toString();
            String mpin = etMPIN.getText().toString();

            etAmount.setError(null);
            etMPIN.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(amount)) {
                cancel = true;
                focusView = etAmount;
                etAmount.setError("Please enter amount");
            } else {
                double currentAmount = Double.parseDouble(amount);
                if (currentAmount > availableAmount) {
                    cancel = true;
                    focusView = etAmount;
                    etAmount.setError("You don`t have sufficient balance");
                } else {
                    if (TextUtils.isEmpty(mpin)) {
                        cancel = true;
                        focusView = etMPIN;
                        etMPIN.setError("Please enter MPIN");
                    } else if (!Utils.isMpinValid(mpin)) {
                        cancel = true;
                        focusView = etMPIN;
                        etMPIN.setError("MPIN is 4 characters long");
                    } else if (!Utils.isMpinAuthentic(mpin, secureMPIN)) {
                        cancel = true;
                        focusView = etMPIN;
                        etMPIN.setError("Incorrect MPIN");
                    }
                }
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                createQRCode(amount);
            }
        }
    }

    private void createQRCode(String amount) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(QRCodeGeneratorActivity.this);
        String uid = sp.getString(Constants.SP_KEY_UID, null);

        String content = Utils.getDebitInfo(uid, amount);
        content = AESHelper.encrypt(content);

        try {
            llAccountDetails.setVisibility(View.GONE);
            llQRCode.setVisibility(View.VISIBLE);

            Bitmap bitmap = generateQrCode(content);
            ivQRCode.setImageBitmap(bitmap);

            final String expiry = getString(R.string.text_code_expiry);

            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String countdown = String.format(expiry, millisUntilFinished / 1000);
                    tvExpiry.setText(countdown);
                }

                @Override
                public void onFinish() {
                    ivQRCode.setVisibility(View.GONE);
                    tvExpiry.setText("Code expired! Generate new code");
                }
            }.start();

        } catch (WriterException e) {
            e.printStackTrace();
            tvExpiry.setText("Please try again...");
        }
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    Bitmap generateQrCode(String content) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% damage

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int size = 256;

        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hintMap);
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }


}
