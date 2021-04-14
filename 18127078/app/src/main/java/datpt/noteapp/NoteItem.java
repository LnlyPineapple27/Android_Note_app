package datpt.noteapp;

import android.graphics.Color;

import java.util.Date;

public class NoteItem {
    String note_Title = null, note_Info = null, note_Tag = null;
    Date note_lastModDate = null;
    int note_color;

    public NoteItem(String note_Title, String note_Info, String note_Tag, Date note_lastModDate) {
        this.note_Title = note_Title;
        this.note_Info = note_Info;
        this.note_lastModDate = note_lastModDate;
        this.note_Tag = note_Tag;
        note_color = Color.parseColor("#FFA1CCA3");
    }
    public NoteItem(String note_Title, String note_Info, String note_Tag, Date note_lastModDate, int note_color) {
        this.note_Title = note_Title;
        this.note_Info = note_Info;
        this.note_lastModDate = note_lastModDate;
        this.note_Tag = note_Tag;
        this.note_color = note_color;
    }

    public int getNote_color() {
        return note_color;
    }

    public void setNote_color(int note_color) {
        this.note_color = note_color;
    }

    public String getNote_Tag() {
        return note_Tag;
    }

    public void setNote_Tag(String note_Tag) {
        this.note_Tag = note_Tag;
    }

    public String getNote_Title() {
        return note_Title;
    }

    public void setNote_Title(String note_Title) {
        this.note_Title = note_Title;
    }

    public String getNote_Info() {
        return note_Info;
    }

    public void setNote_Info(String note_Info) {
        this.note_Info = note_Info;
    }

    public Date getNote_lastModDate() {
        return note_lastModDate;
    }

    public void setNote_lastModDate(Date note_lastModDate) {
        this.note_lastModDate = note_lastModDate;
    }
}
