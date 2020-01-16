package com.blood.emirateslifedonation.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blood.emirateslifedonation.Fragement.ContactFragment;
import com.blood.emirateslifedonation.Fragement.DonerFragment;
import com.blood.emirateslifedonation.Fragement.RequestUserFragment;

public class HomeTabMaker extends FragmentPagerAdapter {
    public HomeTabMaker(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DonerFragment donerFragment = new DonerFragment();
                return donerFragment;

            case 1:
                RequestUserFragment requestUserFragment = new RequestUserFragment();
                return requestUserFragment;

            case 2:
                ContactFragment contactFragment = new ContactFragment();
                return  contactFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Donors";

            case 1:
                return "Request User";

            case 2:
                return "Contact";
        }

        return super.getPageTitle(position);
    }
}
