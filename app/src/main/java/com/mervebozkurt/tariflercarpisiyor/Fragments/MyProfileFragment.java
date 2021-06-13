package com.mervebozkurt.tariflercarpisiyor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mervebozkurt.tariflercarpisiyor.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MyProfileFragment extends Fragment {
    UploadTask uploadTask;


    private FirebaseAuth firebaseAuth;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    public String name;

    ImageView imageProfile;
    TextView tvName,tvSurname,tvPhone,tvInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseUser user =firebaseAuth.getCurrentUser();

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName= getView().findViewById(R.id.tv_name);
        tvInfo=view.findViewById(R.id.tv_info);
        imageProfile=view.findViewById(R.id.image_profile1);

        getuserinfo();

        //getviewpager and set it s pagerAdapter so that it can display items
        viewPager=view.findViewById(R.id.viewPager);
        tabLayout=view.findViewById(R.id.tablayout);
        appBarLayout=view.findViewById(R.id.appbarid1);

        PagerAdapter pA=new com.mervebozkurt.tariflercarpisiyor.Adapters.PagerAdapter(getFragmentManager());
       //adding Fragments
        ((com.mervebozkurt.tariflercarpisiyor.Adapters.PagerAdapter) pA).AddFragment(new AddedRecipes(),"Tariflerim");
        //((com.mervebozkurt.tariflercarpisiyor.Adapters.PagerAdapter) pA).AddFragment(new Favorite(),"");
        //adapter setup
        viewPager.setAdapter(pA);
        //give the tablayout the view pager
        tabLayout.setupWithViewPager(viewPager);

    }

    public void getuserinfo(){
         FirebaseUser user=firebaseAuth.getCurrentUser();
         String CurrentUserıd=user.getUid();
        System.out.println(CurrentUserıd);

        CollectionReference collectionReference=firebaseFirestore.collection("userinfo");
        collectionReference.whereEqualTo(FieldPath.documentId(),CurrentUserıd)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(getContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if (value!=null){
                    for (DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String ,Object> userdata= snapshot.getData();
                        name= (String) userdata.get("name");
                        String surname= (String) userdata.get("surname");
                        String info= (String) userdata.get("info");
                        String downloadurl= (String) userdata.get("url");

                    Picasso.get().load(downloadurl).into(imageProfile);
                    tvName.setText(name +"  "+surname);
                    tvInfo.setText(info);

                    }
                }
            }
        });

    }



}