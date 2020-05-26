package com.example.calendarproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Event Title : " + title)
                .setMessage("Content : " + content)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
