package com.asocialfingers.mp_list.Frontend.Tabs.About;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asocialfingers.mp_list.Backend.Adapters.Version.CustomVersionNotesAdapter;
import com.asocialfingers.mp_list.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AboutFragment extends Fragment {

    private LinearLayout layoutVersionNotes, layoutAboutDeveloper, layoutIzTech, layoutClock, layoutDate;
    private String[] versionNotesDescription = {"Bu versiyon çıkış versiyonudur."};
    private String[] versionNotesHeader = {"Versiyon 1.0"};
    private TextView dateTime, contactText;
    private RelativeLayout layoutContact;
    private int contactCurrentValue = 13;
    private ImageButton _next, _prev;
    private ListView _versionNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        methodCleaner(root);

        return root;
    }

    private void methodCleaner(View root) {

        utils(root);
        versionNotesEvents();
        colorEvents();
        misc();
        contactEvents();

    }

    private void utils(View root) {

        layoutAboutDeveloper = (LinearLayout) root.findViewById(R.id.layoutAboutDeveloper);
        layoutVersionNotes = (LinearLayout) root.findViewById(R.id.layoutVersionNotes);
        layoutIzTech = (LinearLayout) root.findViewById(R.id.layoutIzTech);
        layoutClock = (LinearLayout) root.findViewById(R.id.layoutClock);
        layoutDate = (LinearLayout) root.findViewById(R.id.layoutDate);

        layoutContact = (RelativeLayout) root.findViewById(R.id.layoutContact);

        _versionNotes = (ListView) root.findViewById(R.id.lstVersionNotes);

        contactText = (TextView) root.findViewById(R.id.txtContactObj);
        dateTime = (TextView) root.findViewById(R.id.txtCurrentDate);

        _prev = (ImageButton) root.findViewById(R.id.contactPrevious);
        _next = (ImageButton) root.findViewById(R.id.contactNext);

    }

    private void colorEvents() {

        Random random = new Random();
        int color1 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color2 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color3 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color4 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color5 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color6 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color7 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        //region Drawable

        GradientDrawable g1 = new GradientDrawable();
        g1.setCornerRadius(10);
        g1.setColor(color1);

        GradientDrawable g2 = new GradientDrawable();
        g2.setCornerRadius(10);
        g2.setColor(color2);

        GradientDrawable g3 = new GradientDrawable();
        g3.setCornerRadius(10);
        g3.setColor(color3);

        GradientDrawable g4 = new GradientDrawable();
        g4.setCornerRadius(10);
        g4.setColor(color4);

        GradientDrawable g5 = new GradientDrawable();
        g5.setCornerRadius(10);
        g5.setColor(color5);

        GradientDrawable g6 = new GradientDrawable();
        g6.setCornerRadius(10);
        g6.setColor(color6);

        GradientDrawable g7 = new GradientDrawable();
        g7.setCornerRadius(10);
        g7.setColor(color7);

        //endregion

        layoutAboutDeveloper.setBackground(g1);
        layoutVersionNotes.setBackground(g2);
        layoutContact.setBackground(g4);
        layoutIzTech.setBackground(g5);
        layoutClock.setBackground(g6);
        layoutDate.setBackground(g7);

    }

    private void misc() {

        Date datePicker = Calendar.getInstance().getTime();
        String _date = datePicker.toString();

        dateTime.setText(_date);

    }

    private void versionNotesEvents() {

        CustomVersionNotesAdapter customAdapter = new CustomVersionNotesAdapter(getActivity(), versionNotesHeader, versionNotesDescription);
        _versionNotes.setAdapter(customAdapter);

    }

    private void contactEvents() {

        _prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactCurrentValue == 13) {

                    contactCurrentValue--;
                    contactText.setText("servis@izteknoloji.com");

                } else {

                    contactCurrentValue = 13;
                    contactText.setText("+90 541 505 91 91");

                }

            }
        });

        _next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contactCurrentValue == 13) {

                    contactCurrentValue++;
                    contactText.setText("info@izteknoloji.com");

                } else {

                    contactCurrentValue = 13;
                    contactText.setText("+90 541 505 91 91");

                }

            }
        });

    }

}
