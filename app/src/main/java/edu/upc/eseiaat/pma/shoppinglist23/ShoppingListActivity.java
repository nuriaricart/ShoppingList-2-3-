package edu.upc.eseiaat.pma.shoppinglist23;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String FILENAME = "shopping_list.txt";

    private ArrayList<ShoppingItem> item_list;
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;


    private void writeItemList (){

        try {
            FileOutputStream fos = openFileOutput (FILENAME, Context.MODE_PRIVATE);
            for (int i=0; i<item_list.size(); i++){
                ShoppingItem it = item_list.get(i);
                String line = String.format("%s; %b\n", it.getText(), it.isChecked());
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("nuria", "wirteItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("nuria", "wirteItemList: IOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        writeItemList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = (ListView) findViewById(R.id.list);
        btn_add = (Button) findViewById(R.id.btn_add);
        edit_item = (EditText) findViewById(R.id.edit_item);

        item_list = new ArrayList<>();
        item_list.add(new ShoppingItem("Potatoes"));
        item_list.add(new ShoppingItem("Garrots"));
        item_list.add(new ShoppingItem("Milk"));
        item_list.add(new ShoppingItem("Cheese"));


        adapter = new ShoppingListAdapter(this, android.R.layout.simple_list_item_1, item_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                item_list.get(pos).toogleChecked();
                adapter.notifyDataSetChanged();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem ();
            }
        });

        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem();
                return true;
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem (pos);
                return true;
            }
        });


    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmation);
        String message = getResources().getString(R.string.mess);
        builder.setMessage(message + " " + item_list.get(pos).getText() + "?");
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item_list.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty()){
            item_list.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
        }
        list.smoothScrollToPosition(item_list.size()-1);
    }
}
