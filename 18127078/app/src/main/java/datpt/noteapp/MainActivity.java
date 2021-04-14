package datpt.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static RecyclerView notelistRV;
    private NoteItemAdapter adapter;
    static ArrayList<NoteItem> NoteItemArrayList;
    SearchView searchView;
    private boolean gridlayout, filter_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notelistRV = findViewById(R.id.id_recyclerview);

        //addDemoData();//Add sample items
        loadData();

        adapter = new NoteItemAdapter(NoteItemArrayList, MainActivity.this);

        notelistRV.setHasFixedSize(true);
        if(!gridlayout)
            notelistRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        else
            notelistRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

            //notelistRV.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        //gridlayout = !gridlayout;
        notelistRV.setAdapter(adapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        if (filter_title)
            Toast.makeText(this, "Search mode: NOTE TITLE", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Search mode: NOTE TAG", Toast.LENGTH_SHORT).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NoteItemArrayList.add(0, new NoteItem("DSA", "DSA Self Paced Course", "tag: new one", new Date()));
                //notelistRV.getAdapter().notifyDataSetChanged();
                Intent open_note = new Intent(MainActivity.this, NoteEditorActivity.class);
                open_note.putExtra("notePos", -1);
                startActivityForResult(open_note, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<NoteItem> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        if(filter_title) {
            for (NoteItem item : NoteItemArrayList) {

                if (item.getNote_Title().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        else{
            for (NoteItem item : NoteItemArrayList) {
                if (item.getNote_Tag().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        adapter.filterList(filteredlist);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            return true;
        }
        if(id == R.id.action_change_layout){
            if(gridlayout) {
                notelistRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                item.setIcon(R.drawable.ic_baseline_grid_24);
                gridlayout = false;
            }
            else {
                notelistRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                item.setIcon(R.drawable.ic_baseline_list_24);
                gridlayout = true;
            }
            //gridlayout = !gridlayout;
        }
        if (id == R.id.sub_filter_title){
            filter_title = true;
            Toast.makeText(this, "Search mode: NOTE TITLE", Toast.LENGTH_SHORT).show();
            searchView.setQuery("", false);
            searchView.clearFocus();
            return true;
        }
        if (id == R.id.sub_filter_tag){
            filter_title = false;
            Toast.makeText(this, "Search mode: NOTE TAG", Toast.LENGTH_SHORT).show();
            searchView.setQuery("", false);
            searchView.clearFocus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDemoData() {
        NoteItemArrayList = new ArrayList<>();
        NoteItemArrayList.add(new NoteItem("Datpt", "JAVA Self Paced Course", "tag: Edu", new Date()));
        NoteItemArrayList.add(new NoteItem("loc", "C++ Self Paced Course", "tag: Edu2", new Date()));
        NoteItemArrayList.add(new NoteItem("Pythonic", "Python Self Paced Course", "tag: Edu", new Date()));
        NoteItemArrayList.add(new NoteItem("Forking", "Fork CPP Self Paced Course", "tag: Edu", new Date()));
    }
    private void saveData(){
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(NoteItemArrayList);
        editor.putString("note list", json);
        editor.putBoolean("note layout", this.gridlayout);
        editor.putBoolean("note filter", this.filter_title);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("note list", null);
        this.filter_title = sharedPreferences.getBoolean("note filter", true);//defvalue - value to return if sharedPreferences key doesnt exist
        this.gridlayout = sharedPreferences.getBoolean("note layout", false);
        Type type = new TypeToken<ArrayList<NoteItem>>() {}.getType();
        NoteItemArrayList = gson.fromJson(json, type);
        if (NoteItemArrayList == null) {
            NoteItemArrayList = new ArrayList<>();
        }
    }
    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        saveData();
        super.onStop();
    }
    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}