package com.example.patrice_musicapp.fragments.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OnboardingFragment3 extends Fragment {
    private User user;
    public static final String TAG = OnboardingFragment3.class.getSimpleName();
    private Button btnSave;
    String str = "";

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
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.getParseUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!= null){
                            Log.e(TAG, "Issues with saving instruments", e);
                        }
                        Log.i(TAG, "Instruments saved sucessfully");
                        btnSave.setText("Saved!");
                        btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));

                    }
                });
            }
        });

    }


    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton view, boolean checked) {
            btnSave.setText("Save");
            btnSave.setBackgroundColor(getResources().getColor(R.color.white));
            switch (view.getId()) {
                case R.id.cbBassGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.BASS_GUITAR);
                        str += String.valueOf(Instruments.BASS_GUITAR);
                    } else {
                        user.removeInstrument(Instruments.BASS_GUITAR);
                    }
                    break;
                case R.id.cbAcousticGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.ACOUSTIC_GUITAR);
                    }else {
                        user.removeInstrument(Instruments.ACOUSTIC_GUITAR);
                    }
                    break;
                case R.id.cbElectricGuitar:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.ELECTRIC_GUITAR);

                    }else {
                        user.removeInstrument(Instruments.ELECTRIC_GUITAR);
                    }
                    break;
                case R.id.cbSaxophone:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.SAXOPHONE);

                    }else {
                        user.removeInstrument(Instruments.SAXOPHONE);
                    }
                    break;
                case R.id.cbTrumpet:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.TRUMPET);

                    }else {
                        user.removeInstrument(Instruments.TRUMPET);
                    }
                    break;
                case R.id.cbTrombone:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.TROMBONE);

                    }else {
                        user.removeInstrument(Instruments.TROMBONE);
                    }
                    break;
                case R.id.cbViolin:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.VIOLIN);
                    }else {
                        user.removeInstrument(Instruments.VIOLIN);
                    }
                    break;
                case R.id.cbCello:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.CELLO);

                    }else {
                        user.removeInstrument(Instruments.CELLO);
                    }
                    break;
                case R.id.cbClarinet:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.CLARINET);

                    }else {
                        user.removeInstrument(Instruments.CLARINET);
                    }
                    break;
                case R.id.cbDrums:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.DRUMS);

                    }else {
                        user.removeInstrument(Instruments.DRUMS);
                    }
                    break;
                case R.id.cbPiano:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.PIANO);

                    }else {
                        user.removeInstrument(Instruments.PIANO);
                    }
                    break;
                case R.id.cbBongos:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.PERCUSSION);

                    }else {
                        user.removeInstrument(Instruments.PERCUSSION);
                    }
                    break;
                case R.id.cbSoundEngineer:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.SOUND_ENGINEER);

                    }else {
                        user.removeInstrument(Instruments.SOUND_ENGINEER);
                    }
                    break;
                case R.id.cbVocals:
                    if (checked) {
                        //add it to users instrument array
                        user.setInstrument(Instruments.VOCALS);

                    }else {
                        user.removeInstrument(Instruments.VOCALS);
                    }
                    break;
            }

            if (checked) {
                view.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.green));
            } else {
                view.setBackgroundTintList(null);
            }
         }
    };

    public void setupCheckboxes() {
        CheckBox cbBassGuitar = getView().findViewById(R.id.cbBassGuitar);
        CheckBox cbAcousticGuitar =  getView().findViewById(R.id.cbAcousticGuitar);
        CheckBox cbElectricGuitar =  getView().findViewById(R.id.cbElectricGuitar);
        CheckBox cbSaxophone=  getView().findViewById(R.id.cbSaxophone);
        CheckBox cbTrumpet =  getView().findViewById(R.id.cbTrumpet);
        CheckBox cbTrombone =  getView().findViewById(R.id.cbTrombone);
        CheckBox cbViolin =  getView().findViewById(R.id.cbViolin);
        CheckBox cbCello =  getView().findViewById(R.id.cbCello);
        CheckBox cbClarinet =  getView().findViewById(R.id.cbClarinet);
        CheckBox cbDrums =  getView().findViewById(R.id.cbDrums);
        CheckBox cbPiano =  getView().findViewById(R.id.cbPiano);
        CheckBox cbBongos =  getView().findViewById(R.id.cbBongos);
        CheckBox cbSoundEngineer =  getView().findViewById(R.id.cbSoundEngineer);
        CheckBox cbVocals =  getView().findViewById(R.id.cbVocals);

        cbBassGuitar.setOnCheckedChangeListener(checkListener);
        cbAcousticGuitar.setOnCheckedChangeListener(checkListener);
        cbElectricGuitar.setOnCheckedChangeListener(checkListener);
        cbSaxophone.setOnCheckedChangeListener(checkListener);
        cbTrumpet.setOnCheckedChangeListener(checkListener);
        cbTrombone.setOnCheckedChangeListener(checkListener);
        cbViolin.setOnCheckedChangeListener(checkListener);
        cbCello.setOnCheckedChangeListener(checkListener);
        cbClarinet.setOnCheckedChangeListener(checkListener);
        cbDrums.setOnCheckedChangeListener(checkListener);
        cbPiano.setOnCheckedChangeListener(checkListener);
        cbBongos.setOnCheckedChangeListener(checkListener);
        cbSoundEngineer.setOnCheckedChangeListener(checkListener);
        cbVocals.setOnCheckedChangeListener(checkListener);

    }

}