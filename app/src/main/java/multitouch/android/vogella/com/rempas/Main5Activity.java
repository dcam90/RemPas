package multitouch.android.vogella.com.rempas;

////////////////FOR UPDATE

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main5);
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
                startActivity(new Intent(this, Main2Activity.class));
            case R.id.retrieval:
                startActivity(new Intent(this, Main3Activity.class));
            case R.id.replacement:
                return true;
            case R.id.deletion:
                startActivity(new Intent(this, Main4Activity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickReplacePass(View view) {
        EditText replacetext = (EditText)findViewById(R.id.replaceEdit);
        final String replace_query = replacetext.getText().toString();

        if (replace_query.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "The name field is blank, please re-enter.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!checkIfDataExists(replace_query)) { //can't find the password
            Toast.makeText(getBaseContext(),
                    "Queried password could not be found.", Toast.LENGTH_LONG).show();
        }

        else {

        }
    }

    private boolean checkIfOldPassMatches(String password) {
        String URL = "content://multitouch.android.vogella.com.rempas.PasswordProvider/passwords";
        Uri passwords = Uri.parse(URL);
        int check = 0;

        Cursor c = getContentResolver().query(passwords, null, null, null, "password");
        if (!c.moveToFirst()) {
            return false;
        }
        else {
            c.moveToFirst();
            do {
                String inTable = c.getString(c.getColumnIndex(PasswordProvider.PASSWORD));
                if (inTable.equals(password)) {
                    check = 1;
                    break;
                }
            } while (c.moveToNext());
            c.close();
            return check != 0;
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