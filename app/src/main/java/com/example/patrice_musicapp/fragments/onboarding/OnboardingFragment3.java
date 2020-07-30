package com.example.patrice_musicapp.fragments.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseUser;

public class OnboardingFragment3 extends Fragment {
    private User user;
    public static final String TAG = OnboardingFragment3.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = new User(ParseUser.getCurrentUser());
        setupCheckboxes();

    }


    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean checked) {
            String str = "";
            switch (view.getId()) {
                case R.id.cbBassGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.BASS_GUITAR);
                        str += String.valueOf(Instruments.BASS_GUITAR);
                    }
                    break;
                case R.id.cbAcousticGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.ACOUSTIC_GUITAR);
                        str += String.valueOf(Instruments.ACOUSTIC_GUITAR);
                    }
                    break;
                case R.id.cbElectricGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.ELECTRIC_GUITAR);
                        str += String.valueOf(Instruments.ELECTRIC_GUITAR);

                    }
                    break;
                case R.id.cbSaxophone:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.SAXOPHONE);
                        str += String.valueOf(Instruments.SAXOPHONE);

                    }
                    break;
                case R.id.cbTrumpet:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.TRUMPET);
                        str += String.valueOf(Instruments.TRUMPET);

                    }
                    break;
                case R.id.cbTrombone:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.TROMBONE);
                        str += String.valueOf(Instruments.TROMBONE);

                    }
                    break;
                case R.id.cbViolin:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.VIOLIN);
                        str += String.valueOf(Instruments.VIOLIN);

                    }
                    break;
                case R.id.cbCello:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.CELLO);
                        str += String.valueOf(Instruments.CELLO);

                    }
                    break;
                case R.id.cbClarinet:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.CLARINET);
                        str += String.valueOf(Instruments.CLARINET);

                    }
                    break;
                case R.id.cbDrums:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.DRUMS);
                        str += String.valueOf(Instruments.DRUMS);

                    }
                    break;
                case R.id.cbPiano:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.PIANO);
                        str += String.valueOf(Instruments.PIANO);

                    }
                    break;
                case R.id.cbBongos:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.PERCUSSION);
                        str += String.valueOf(Instruments.PERCUSSION);

                    }
                    break;
                case R.id.cbSoundEngineer:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.SOUND_ENGINEER);
                        str += String.valueOf(Instruments.SOUND_ENGINEER);

                    }
                    break;
                case R.id.cbVocals:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.VOCALS);
                        str += String.valueOf(Instruments.VOCALS);

                    }
                    break;
            }
            view.setBackgroundColor(getResources().getColor(R.color.green));
            Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
        }
    };

    public void setupCheckboxes() {
        CheckBox cbBassGuitar = getView().findViewById(R.id.cbBassGuitar);
        CheckBox cbAcousticGuitar =  getView().findViewById(R.id.cbAcousticGuitar);
        cbBassGuitar.setOnCheckedChangeListener(checkListener);
        cbAcousticGuitar.setOnCheckedChangeListener(checkListener);
    }

}