package edu.uncc.sense49;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN_FINISHED = 1;
    private boolean compleatedLogin;

    private boolean checkCachedLogin() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Here", "");
        compleatedLogin = false;

        if (!checkCachedLogin()) {
            Log.d("Here", "");
            Intent intent = new Intent(this, MainLoginPageActivity.class);
            // https://stackoverflow.com/questions/11046810
            startActivityForResult(intent, REQUEST_CODE_LOGIN_FINISHED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOGIN_FINISHED:
                compleatedLogin = true;
                break;
                
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (compleatedLogin) {
            Intent intent = new Intent(this, MainNavDrawerActivity.class);
            startActivity(intent);
        }
    }
}
