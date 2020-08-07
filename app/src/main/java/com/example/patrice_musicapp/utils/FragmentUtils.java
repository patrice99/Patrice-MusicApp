package com.example.patrice_musicapp.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.patrice_musicapp.R;

public class FragmentUtils {
    public static void displayFragment(FragmentManager fragmentManager, Fragment showFragment, Fragment hideFragment1, Fragment hideFragment2, Fragment hideFragment3) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (showFragment.isAdded()){
            ft.show(showFragment);
        } else {
            ft.add(R.id.flContainer, showFragment);
        }

        if(hideFragment1.isAdded()){
            ft.hide(hideFragment1);
        }

        if(hideFragment2.isAdded()){
            ft.hide(hideFragment2);
        }

        if(hideFragment3.isAdded()){
            ft.hide(hideFragment3);
        }

        ft.commit();

    }
}
