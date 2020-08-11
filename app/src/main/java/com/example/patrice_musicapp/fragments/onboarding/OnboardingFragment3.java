package com.example.patrice_musicapp.fragments.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.databinding.FragmentOnboarding3Binding;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OnboardingFragment3 extends Fragment {
    FragmentOnboarding3Binding binding;
    private User user;
    public static final String TAG = OnboardingFragment3.class.getSimpleName();
    private Button btnSave;
    String str = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding3Binding.inflate(getLayoutInflater(), container, false);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = new User(ParseUser.getCurrentUser());
        setupCheckboxes();
        btnSave = binding.btnSave;
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

        binding.cbBassGuitar.setOnCheckedChangeListener(checkListener);
        binding.cbAcousticGuitar.setOnCheckedChangeListener(checkListener);
        binding.cbElectricGuitar.setOnCheckedChangeListener(checkListener);
        binding.cbSaxophone.setOnCheckedChangeListener(checkListener);
        binding.cbTrumpet.setOnCheckedChangeListener(checkListener);
        binding.cbTrombone.setOnCheckedChangeListener(checkListener);
        binding.cbViolin.setOnCheckedChangeListener(checkListener);
        binding.cbCello.setOnCheckedChangeListener(checkListener);
        binding.cbClarinet.setOnCheckedChangeListener(checkListener);
        binding.cbDrums.setOnCheckedChangeListener(checkListener);
        binding.cbPiano.setOnCheckedChangeListener(checkListener);
        binding.cbBongos.setOnCheckedChangeListener(checkListener);
        binding.cbSoundEngineer.setOnCheckedChangeListener(checkListener);
        binding.cbVocals.setOnCheckedChangeListener(checkListener);

    }

}