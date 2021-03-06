package jawas.tripmarker.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import jawas.tripmarker.R;
import jawas.tripmarker.helpers.FirebaseRef;
import jawas.tripmarker.pojos.User;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void signUp(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText retypepass = (EditText) findViewById(R.id.retype_password);
        EditText name = (EditText) findViewById(R.id.name);

        if(password.getText().toString().equals(retypepass.getText().toString())
                && !email.getText().toString().isEmpty()
                && !name.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty())
        {
            final Firebase context = FirebaseRef.getDbContext();
            context.createUser(email.getText().toString(), password.getText().toString(),
                    new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            String uid = (String) result.get("uid");
                            String name = ((EditText) findViewById(R.id.name)).getText().toString();
                            String age = ((EditText) findViewById(R.id.age)).getText().toString();
                            String homeplace = ((EditText) findViewById(R.id.homeplace)).getText().toString();
                            String gender = ((Spinner) findViewById(R.id.gender)).getSelectedItem().toString();
                            context.child("users").child(uid).setValue(new User( name, Integer.parseInt(age), homeplace, gender));
                            finish();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            String error = firebaseError.toString();
                            if (error.length() != 147)
                                Toast.makeText(getApplicationContext(), error.substring(15), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(), "You don" + "\'" + "t have access to the Internet", Toast.LENGTH_LONG).show();
                        }
                    });

        }
        if(!password.getText().toString().equals(retypepass.getText().toString()))
            Toast.makeText(getApplicationContext(), "Typed passwords are different", Toast.LENGTH_LONG).show();
        int i=0;
        if(email.getText().toString().isEmpty() && name.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "You didn" + "\'" + "t type Email and Name", Toast.LENGTH_LONG).show();
            i=1;
        }
        if(email.getText().toString().isEmpty() && i==0)
            Toast.makeText(getApplicationContext(), "You didn" + "\'" + "t type Email", Toast.LENGTH_LONG).show();

        if(name.getText().toString().isEmpty() && i==0)
            Toast.makeText(getApplicationContext(), "You didn" + "\'" + "t type Name", Toast.LENGTH_LONG).show();

        if(password.getText().toString().isEmpty() && retypepass.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "You didn" + "\'" + "t type Passwords", Toast.LENGTH_LONG).show();
    }
}
