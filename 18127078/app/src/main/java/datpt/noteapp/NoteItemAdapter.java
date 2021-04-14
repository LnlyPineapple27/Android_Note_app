package datpt.noteapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.ViewHolder> {

    private ArrayList<NoteItem> courseModalArrayList;
    private Context context;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public NoteItemAdapter(ArrayList<NoteItem> courseModalArrayList, Context context) {
        this.courseModalArrayList = courseModalArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<NoteItem> filterllist) {
        courseModalArrayList = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemAdapter.ViewHolder holder, int position) {
        NoteItem curitem = courseModalArrayList.get(position);
        holder.note_nameTV.setText(curitem.getNote_Title());
        holder.note_dataTV.setText(curitem.getNote_Info());
        holder.note_tagTV.setText(curitem.getNote_Tag());
        holder.note_timestampTV.setText(this.formatter.format(curitem.getNote_lastModDate()));
        holder.note_rl.setBackgroundColor(curitem.getNote_color());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d1 = new AlertDialog.Builder(context)
                    .setMessage("Select an option to do with selected note item")
                    .setPositiveButton("View and Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent open_note = new Intent(context, NoteEditorActivity.class);
                            open_note.putExtra("notePos", position);
                            context.startActivity(open_note);
                        }})
                    .setNegativeButton("Change color", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new ColorPickerDialog.Builder(context)
                                    .setTitle("Choose color for selected note")
                                    .setPreferenceName("MyColorPickerDialog")
                                    .setPositiveButton("Select",
                                            new ColorEnvelopeListener() {
                                                @Override
                                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                                    //setLayoutColor(envelope);
                                                    //ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#" + envelope.getHexCode()));
                                                    //holder.itemView.setCardBackgroundColor(Color.parseColor("#" + envelope.getHexCode()));
                                                    curitem.setNote_color(Color.parseColor("#" + envelope.getHexCode()));
                                                    courseModalArrayList.remove(position);
                                                    courseModalArrayList.add(position, curitem);
                                                    holder.note_rl.setBackgroundColor(Color.parseColor("#" + envelope.getHexCode()));
                                                    //Toast.makeText(context,"Color selected: #" + envelope.getHexCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                    .attachAlphaSlideBar(true)
                                    .attachBrightnessSlideBar(true)
                                    .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                                    .show();
                        }
                    })
                    .show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog d2 = new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                courseModalArrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView note_nameTV, note_dataTV, note_tagTV, note_timestampTV;
        RelativeLayout note_rl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note_rl = itemView.findViewById(R.id.rel_layout);
            note_nameTV = itemView.findViewById(R.id.idNoteName);
            note_dataTV = itemView.findViewById(R.id.idNoteData);
            note_tagTV = itemView.findViewById(R.id.idNoteTag);
            note_timestampTV = itemView.findViewById(R.id.idNoteTimeStamp);
        }
    }
}