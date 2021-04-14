package datpt.noteapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NoteEditorActivity extends AppCompatActivity {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    int noteId;
    EditText note_title, note_tag, note_data;
    TextView note_date;
    NoteItem curr_note;
    int color;
    AppCompatButton save_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        note_date = findViewById(R.id.date);
        note_data = findViewById(R.id.editData);
        note_tag  = findViewById(R.id.editTag);
        note_title = findViewById(R.id.editTitle);
        save_button = findViewById(R.id.button);


        Intent intent = getIntent();

        noteId = intent.getIntExtra("notePos", -1);
        if (noteId != -1) {
            curr_note = MainActivity.NoteItemArrayList.get(noteId);
            note_title.setText(curr_note.getNote_Title());
            note_tag.setText(curr_note.getNote_Tag());
            note_data.setText(curr_note.getNote_Info());
            color = curr_note.getNote_color();
            note_date.setText("Last Edited: " + formatter.format(curr_note.getNote_lastModDate()));
        }
        else{
            note_date.setText("Created date: " + formatter.format(new Date()));
        }
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteItem n = new NoteItem(note_title.getText().toString(), note_data.getText().toString(), note_tag.getText().toString(), new Date());
                if(noteId != -1){
                    MainActivity.NoteItemArrayList.remove(noteId);
                    n.setNote_color(color);
                    MainActivity.NoteItemArrayList.add(noteId, n);
                }
                else
                    MainActivity.NoteItemArrayList.add(0, n);

                try {
                    Objects.requireNonNull(MainActivity.notelistRV.getAdapter()).notifyDataSetChanged();
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(),"Note saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}