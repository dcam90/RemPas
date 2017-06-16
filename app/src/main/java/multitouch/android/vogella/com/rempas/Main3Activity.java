package multitouch.android.vogella.com.rempas;

////////////////FOR SEARCH

import android.content.ClipData;
import android.content.ClipboardManager;
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

public class Main3Activity extends Activity {
    String note_query = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
                return true;
            case R.id.replacement:
                startActivity(new Intent(this, Main5Activity.class));
            case R.id.deletion:
                startActivity(new Intent(this, Main4Activity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void searchFor(View view) { //USE THIS FOR RETRIEVAL AND COPY TO CLIPBOARD
        EditText nametext = (EditText)findViewById(R.id.searchEdit);
        String search_query = nametext.getText().toString();

        if (search_query.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "The query field is blank, please re-enter.", Toast.LENGTH_LONG).show();
            return;
        }

        if (checkIfDataExists(search_query)) {
            Toast.makeText(getBaseContext(),
                    "Password copied to the clipboard.", Toast.LENGTH_SHORT).show();

            if (note_query.length() != 0) {
                Toast.makeText(getBaseContext(),
                        "Note: " + note_query, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getBaseContext(),
                    "Password could not be found.", Toast.LENGTH_LONG).show();
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
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", c.getString(c.getColumnIndex(PasswordProvider.PASSWORD)));
                    clipboard.setPrimaryClip(clip);
                    ////
                    if (c.getString(c.getColumnIndex(PasswordProvider.NOTE)).length() != 0) {
                        note_query = c.getString(c.getColumnIndex(PasswordProvider.NOTE));
                    }
                    check = 1;
                    break;
                }
            } while (c.moveToNext());
            c.close();
            return check != 0;
        }
    }
}
