package multitouch.android.vogella.com.rempas;

////////////////FOR ADDITION

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addition:
                return true;
            case R.id.retrieval:
                startActivity(new Intent(this, Main3Activity.class));
            case R.id.replacement:
                startActivity(new Intent(this, Main5Activity.class));
            case R.id.deletion:
                startActivity(new Intent(this, Main4Activity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickAddName(View view) {
        ContentValues values = new ContentValues();
        EditText nametext = (EditText)findViewById(R.id.txtName);
        EditText passtext = (EditText)findViewById(R.id.txtPassword);
        String search_query = nametext.getText().toString();
        String pass_query = passtext.getText().toString();

        if (search_query.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "The name field is blank, please re-enter.", Toast.LENGTH_LONG).show();
            return;
        }

        if (pass_query.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "The password field is blank, please re-enter.", Toast.LENGTH_LONG).show();
            return;
        }

        if (checkIfDataExists(search_query)) { //the password already exists in the database
            Toast.makeText(getBaseContext(),
                    "Application already exists in the database.", Toast.LENGTH_LONG).show();
        }

        else {
            values.put(PasswordProvider.NAME,
                    ((EditText) findViewById(R.id.txtName)).getText().toString()); //name of app
            values.put(PasswordProvider.PASSWORD,
                    ((EditText) findViewById(R.id.txtPassword)).getText().toString()); //password for app
            values.put(PasswordProvider.NOTE,
                    ((EditText) findViewById(R.id.txtNote)).getText().toString()); //optional note

            getContentResolver().insert(PasswordProvider.CONTENT_URI, values);

            Toast.makeText(getBaseContext(),
                    "Password added to the database.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfDataExists(String name) {
        String URL = "content://multitouch.android.vogella.com.rempas.PasswordProvider/passwords";
        Uri passwords = Uri.parse(URL);
        int check = 0;

        Cursor c = getContentResolver().query(passwords, null, null, null, "name");
        if (!c.moveToFirst()) {
            return false;
        }
        else {
            c.moveToFirst();
            do {
                String inTable = c.getString(c.getColumnIndex(PasswordProvider.NAME));
                if (inTable.equals(name)) {
                    check = 1;
                    break;
                }
            } while (c.moveToNext());
            c.close();
            return check != 0;
        }
    }

}
