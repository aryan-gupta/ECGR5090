package edu.uncc.sense49;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainLoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_page);
    }

    /** Called when the user taps the Send button */
    public void login(View view) {
        EditText usernameText = (EditText) findViewById(R.id.mainUsernameTextBox);
        EditText passwordText = (EditText) findViewById(R.id.mainPasswordTextBox);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        setResult(Activity.RESULT_OK, null);
        finish();
    }
}