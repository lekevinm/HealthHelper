package cs125.healthhelper;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecFragment extends Fragment {

    ArrayList<Recommendation> contacts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.activity_rec, container, false);

        View rootView = inflater.inflate(R.layout.activity_rec, container, false);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.NotifRecyclerView);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // this is data fro recycler view
        ArrayList<Recommendation> itemsData = new ArrayList<Recommendation>();

        itemsData.add(new Recommendation("One Handed Push Ups"));
        itemsData.add(new Recommendation("Taiyaki"));
        itemsData.add(new Recommendation("Melon Soda"));

        // 3. create an adapter
        RecAdapter mAdapter = new RecAdapter(getActivity(), itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
