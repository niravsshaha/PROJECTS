package com.example.qtp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtp.BaseActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.qtp.models.Transaction;
import com.example.qtp.models.User;
import com.example.qtp.utils.AESHelper;
import com.example.qtp.utils.Constants;
import com.example.qtp.utils.Utils;

public class ScannerActivity extends BaseActivity {

    @BindView(R.id.camera_view)
    SurfaceView cameraView;

    @BindView(R.id.code_info)
    TextView barcodeInfo;

    ProgressDialog progressDialog;
    String debitName;
    String debitEmail;
    double debitPreviousAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ButterKnife.bind(this);

        progressDialog = Utils.getProgressDialog(this, "Making payment", "Please wait...");

//        initScan();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a QR Code");
        //integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        //integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Toast.makeText(ScannerActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                progressDialog.show();

                String encryptedContent = result.getContents().trim();
                if (Utils.isQRContentStartsWithKey(encryptedContent)) {
                    encryptedContent = encryptedContent.replace(AESHelper.seed, "");

                    String decryptedContent = AESHelper.decrypt(encryptedContent);

                    //TODO remove this if or move the textview in else part
                    if (decryptedContent != null) {
                        if (Utils.isValidQRContent(decryptedContent)) {
                            String[] debitInfo = Utils.furnishQRContent(decryptedContent);
                            makePayment(debitInfo[0], debitInfo[1]);

                            Toast.makeText(ScannerActivity.this, "Payment done!", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
                            finish();

                        } else {
                            Toast.makeText(ScannerActivity.this, "Code expired!", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(ScannerActivity.this, "Wrong QR code!", Toast.LENGTH_SHORT).show();
                    startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
                    finish();
                }
            } else {
                Toast.makeText(ScannerActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
                finish();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
            startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
            finish();
        }
    }

    private void initScan() {
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        final CameraSource cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        //ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();

                if (barcode.size() != 0) {

                    progressDialog.show();

                    String encryptedContent = barcode.valueAt(0).displayValue.trim();
                    String decryptedContent = AESHelper.decrypt(encryptedContent);

                    //TODO remove this if or move the textview in else part
                    if (decryptedContent != null) {
                        if (Utils.isValidQRContent(decryptedContent)) {
                            String[] debitInfo = Utils.furnishQRContent(decryptedContent);
                            makePayment(debitInfo[0], debitInfo[1]);

                            Toast.makeText(ScannerActivity.this, "Payment done!", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.getIntent(ScannerActivity.this, 0));
                            finish();

                        } else {
                            encryptedContent = "Code expired!";
                        }
                    }

                    final String finalEncryptedContent = encryptedContent;

                    //show in textview
                    barcodeInfo.post(new Runnable() {
                        public void run() {
                            barcodeInfo.setText(finalEncryptedContent);
                        }
                    });
                }
            }
        });


    }

    private void makePayment(String debitUid, String amount) {
        final double finalAmount = Double.parseDouble(amount);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ScannerActivity.this);
        String creditUid = sp.getString(Constants.SP_KEY_UID, null);
        final String creditName = sp.getString(Constants.SP_KEY_DISPLAY_NAME, null);
        final String creditEmail = sp.getString(Constants.SP_KEY_EMAIL, null);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference creditUserRef = database.getReference(Constants.USERS).child(creditUid).child(Constants.USER_INFO).child(Constants.USER_BALANCE);
        final DatabaseReference creditUserTransactionRef = database.getReference(Constants.USERS).child(creditUid).child(Constants.USER_TRANSACTION);

        final DatabaseReference debitUserRef = database.getReference(Constants.USERS).child(debitUid).child(Constants.USER_INFO).child(Constants.USER_BALANCE);
        final DatabaseReference debitUserRef1 = database.getReference(Constants.USERS).child(debitUid).child(Constants.USER_INFO);
        final DatabaseReference debitUserTransactionRef = database.getReference(Constants.USERS).child(debitUid).child(Constants.USER_TRANSACTION);

        debitUserRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                setDebitUserInfo(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        creditUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double creditBalance = dataSnapshot.getValue(Double.class);
                double updateBalance = creditBalance + finalAmount;

                Transaction transaction = new Transaction(debitName, debitEmail, finalAmount, true);
                creditUserTransactionRef.push().setValue(transaction);

                creditUserRef.setValue(updateBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        debitUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                double debitBalance = dataSnapshot.getValue(Double.class);

                double updateBalance = debitBalance - finalAmount;

                Transaction transaction = new Transaction(creditName, creditEmail, finalAmount, false);
                debitUserTransactionRef.push().setValue(transaction);
                debitUserRef.setValue(updateBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog.dismiss();
        Toast.makeText(ScannerActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
    }

    private void setDebitUserInfo(User user) {
        debitName = user.getName();
        debitEmail = user.getEmail();
        debitPreviousAmount = user.getBalance();
    }
}