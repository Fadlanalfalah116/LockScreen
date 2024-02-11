package com.example.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        // Inisialisasi DevicePolicyManager dan ComponentName
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);

        // Periksa apakah aplikasi memiliki izin administratif
        if (!mDevicePolicyManager.isAdminActive(mComponentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Izinkan aplikasi mengunci layar");
            startActivityForResult(intent, 0);
        } else {
            // Jika sudah memiliki izin, maka kunci layar
            lockScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Jika pengguna memberikan izin, maka kunci layar
                lockScreen();
            } else {
                // Jika pengguna menolak izin, tampilkan pesan
                Toast.makeText(this, "Izin ditolak, aplikasi tidak dapat berfungsi.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void lockScreen() {
        // Kunci layar perangkat
        mDevicePolicyManager.lockNow();
        finish();
    }
}
