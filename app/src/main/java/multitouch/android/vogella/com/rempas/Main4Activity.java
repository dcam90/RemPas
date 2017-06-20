package multitouch.android.vogella.com.rempas;

////////////////FOR DELETION

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class Main4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
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
                startActivity(new Intent(this, Main5Activity.class));
                return true;
            case R.id.deletion:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickDeleteName(View view) {
        EditText deletetext = (EditText)findViewById(R.id.deleteEdit);
        final String delete_query = deletetext.getText().toString();

        if (delete_query.length() == 0) {
            Toast.makeText(getBaseContext(),
                    "The name field is blank, please re-enter.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!checkIfDataExists(delete_query)) { //can't find the password
            Toast.makeText(getBaseContext(),
                    "Queried password could not be found.", Toast.LENGTH_LONG).show();
        }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Proceed to delete " + delete_query + "?");
            builder.setCancelable(false);
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getContentResolver().delete(PasswordProvider.CONTENT_URI, "name = ?", new String[] {delete_query});
                    Toast.makeText(getBaseContext(),
                            "Password removed from the database.", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
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