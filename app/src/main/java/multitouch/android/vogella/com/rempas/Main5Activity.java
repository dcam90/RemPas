package multitouch.android.vogella.com.rempas;

////////////////FOR UPDATE

import android.content.ContentValues;
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
                return true;
            case R.id.retrieval:
                startActivity(new Intent(this, Main3Activity.class));
                return true;
            case R.id.replacement:
                return true;
            case R.id.deletion:
                startActivity(new Intent(this, Main4Activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickReplacePass(View view) {
        EditText replacetext = (EditText)findViewById(R.id.replaceEdit);
        EditText oldtext = (EditText)findViewById(R.id.oldEdit);
        EditText newtext = (EditText)findViewById(R.id.newEdit);
        EditText newtextcon = (EditText)findViewById(R.id.newConfEdit);
        final String replace_query = replacetext.getText().toString();
        final String old_pass = oldtext.getText().toString();
        final String new_pass = newtext.getText().toString();
        final String new_pass_con = newtextcon.getText().toString();

        if (replace_query.length() == 0 || old_pass.length() == 0 || new_pass.length() == 0 || new_pass_con.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Please fill in all of the fields.", Toast.LENGTH_LONG).show();
        }

        else if (!checkIfDataExists(replace_query)) { //can't find the name
            Toast.makeText(getBaseContext(),
                    "Queried name could not be found.", Toast.LENGTH_LONG).show();
        }

        else if (!checkIfOldPassMatches(replace_query, old_pass)) {
            Toast.makeText(getBaseContext(),
                    "The old password does not match the provided name.", Toast.LENGTH_LONG).show();
        }

        else if (!new_pass.equals(new_pass_con)) {
            Toast.makeText(getBaseContext(),
                    "The new password and its confirmation do not match.", Toast.LENGTH_LONG).show();
        }

        else {
            ContentValues values = new ContentValues();
            values.put(PasswordProvider.PASSWORD, new_pass);
            getContentResolver().update(PasswordProvider.CONTENT_URI, values, "password = " + old_pass, null);
            Toast.makeText(getBaseContext(),
                    "The password for " + replace_query + " has been updated.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfOldPassMatches(String name, String password) {
        String URL = "content://multitouch.android.vogella.com.rempas.PasswordProvider/passwords";
        Uri passwords = Uri.parse(URL);
        String the_name = " ", the_pass = " ";

        Cursor c = getContentResolver().query(passwords, null, null, null, "name");
        if (!c.moveToFirst()) {
            return false;
        }
        else {
            c.moveToFirst();
            do {
                String inTable = c.getString(c.getColumnIndex(PasswordProvider.NAME));
                if (inTable.equals(name)) {
                    the_name = inTable;
                    the_pass = c.getString(c.getColumnIndex(PasswordProvider.PASSWORD));
                    break;
                }
            } while (c.moveToNext());
            c.close();
        }

        if (name.equals(the_name) && password.equals(the_pass)) {
            return true;
        }

        return false;
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