package com.example.psafe.ui.gallery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psafe.R;
import com.example.psafe.data.model.BlankViewModel;
import com.example.psafe.data.model.News;
import com.example.psafe.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private static final String TAG = "gallery";
    private GalleryViewModel galleryViewModel;
    private RecyclerView.Adapter newsAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private RecyclerView recyclerView;
    private ArrayList<News> test;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        test = new ArrayList<>();


        //layout manager
        newsLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(newsLayoutManager);
        newsAdapter = new NewsRecyclerViewAdapter(getContext(),test);
        //Log.d(TAG,galleryViewModel.getAllNews().get(0).getId());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    test.add(child.getValue(News.class));
                    //test.get(test.size()-1).setImage(galleryViewModel.getRepository().getStorage().getReference().child("images").child(child.getValue(News.class).getId()).getPath().toString());
                    //Log.v(TAG,test.get(test.size()-1).getImage());
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        galleryViewModel.getRepository().getmDatabase().child("news").addValueEventListener(postListener);

        recyclerView.setAdapter(newsAdapter);

        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fix size


    }
}