package com.example.patrice_musicapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.patrice_musicapp.databinding.ActivityOnboardingBinding;
import com.example.patrice_musicapp.fragments.onboarding.OnboardingFragment1;
import com.example.patrice_musicapp.fragments.onboarding.OnboardingFragment2;
import com.example.patrice_musicapp.fragments.onboarding.OnboardingFragment3;
import com.example.patrice_musicapp.fragments.onboarding.OnboardingFragment4;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    case 3 : return new OnboardingFragment4();
                    default: return null;
                }

            }

            @Override
            public int getCount() {
                //number of onboarding screens
                return 4;
            }
        };


        binding.pager.setAdapter(adapter);
        binding.indicator.setViewPager(binding.pager);


        //set on click listeners

        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.pager.getCurrentItem() == 3) { // The last screen
                    finishOnboarding();
                } else {
                    binding.pager.setCurrentItem(
                            binding.pager.getCurrentItem() + 1,
                            true
                    );
                }
            }
        });


        binding.indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 3){
                    binding.skip.setVisibility(View.GONE);
                    binding.next.setText("Done");
                } else {
                    binding.skip.setVisibility(View.VISIBLE);
                    binding.next.setText("Next");
                }
            }
        });

    }

    private void finishOnboarding() {
        // Get the shared preferences
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putBoolean("onboarding_complete",true).apply();

        // Launch the main Activity, called MainActivity
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        // Close the OnboardingActivity
        finish();
    }

}