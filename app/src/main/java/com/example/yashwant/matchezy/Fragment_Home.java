package com.example.yashwant.matchezy;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scalified.fab.ActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends android.support.v4.app.Fragment {

   View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment__home, container, false);



        return myView;
    }

}


/*
    final ActionButton actionButton = (ActionButton) myView.findViewById(R.id.action_button_filter);
// actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
                actionButton.setSize(65.0f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                actionButton.setButtonColor(Color.parseColor("#EA5251"));
                }
                actionButton.setRippleEffectEnabled(true);
                actionButton.playShowAnimation();
                actionButton.setImageResource(R.drawable.filter);


        lstMatchedProfiles = new ArrayList<>();
        for (int i=0 ;i<16;i++)
            lstMatchedProfiles.add(new MatchedProfiles("Priya, 22","Categorie MatchedProfiles","Description book",R.drawable.first));

      /*  lstMatchedProfiles.add(new MatchedProfiles("The Wild Robot","Categorie MatchedProfiles","Description book",R.drawable.thewildrobot));
        lstMatchedProfiles.add(new MatchedProfiles("Maria Semples","Categorie MatchedProfiles","Description book",R.drawable.mariasemples));
        lstMatchedProfiles.add(new MatchedProfiles("The Martian","Categorie MatchedProfiles","Description book",R.drawable.themartian));
        lstMatchedProfiles.add(new MatchedProfiles("He Died with...","Categorie MatchedProfiles","Description book",R.drawable.hediedwith));
        lstMatchedProfiles.add(new MatchedProfiles("The Vegitarian","Categorie MatchedProfiles","Description book",R.drawable.thevigitarian));
        lstMatchedProfiles.add(new MatchedProfiles("The Wild Robot","Categorie MatchedProfiles","Description book",R.drawable.thewildrobot));
        lstMatchedProfiles.add(new MatchedProfiles("Maria Semples","Categorie MatchedProfiles","Description book",R.drawable.mariasemples));
        lstMatchedProfiles.add(new MatchedProfiles("The Martian","Categorie MatchedProfiles","Description book",R.drawable.themartian));
        lstMatchedProfiles.add(new MatchedProfiles("He Died with...","Categorie MatchedProfiles","Description book",R.drawable.hediedwith));
        lstMatchedProfiles.add(new MatchedProfiles("The Vegitarian","Categorie MatchedProfiles","Description book",R.drawable.thevigitarian));
        lstMatchedProfiles.add(new MatchedProfiles("The Wild Robot","Categorie MatchedProfiles","Description book",R.drawable.thewildrobot));
        lstMatchedProfiles.add(new MatchedProfiles("Maria Semples","Categorie MatchedProfiles","Description book",R.drawable.mariasemples));
        lstMatchedProfiles.add(new MatchedProfiles("The Martian","Categorie MatchedProfiles","Description book",R.drawable.themartian));
        lstMatchedProfiles.add(new MatchedProfiles("He Died with...","Categorie MatchedProfiles","Description book",R.drawable.hediedwith));
*//*
RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
    RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lstMatchedProfiles);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
                myrv.setAdapter(myAdapter);*/
