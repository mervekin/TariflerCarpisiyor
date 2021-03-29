package com.mervebozkurt.tariflercarpisiyor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mervebozkurt.tariflercarpisiyor.Fragments.CategoriesFragment;
import com.mervebozkurt.tariflercarpisiyor.Fragments.FindNewMealFragment;
import com.mervebozkurt.tariflercarpisiyor.Fragments.HomeFragment;
import com.mervebozkurt.tariflercarpisiyor.Fragments.MyProfileFragment;
import com.mervebozkurt.tariflercarpisiyor.Login.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    //private FirebaseFirestore firebaseFirestore;

    //navbar kısmı
    private BottomNavigationView NavbarView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment=null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment=new HomeFragment();
                    break;
                case R.id.navigation_categories:
                    fragment=new CategoriesFragment();
                    break;
                case R.id.navigation_findMeal:
                    fragment=new FindNewMealFragment();
                    break;
                case R.id.navigation_MyProfile:
                    fragment=new MyProfileFragment();
                    break;
                case R.id.navigation_AddRecipe:
                   startActivity(new Intent(getApplicationContext(),UploadRecipeActivity.class));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            return loadFragment(fragment);
        }
    };

    public boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
    //-------//



//Menu kısmı

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.recipe_option_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //menu seçeneklerine tıklayınca ne yapması gerektiğini göstereceğiz.
        if(item.getItemId()==R.id.UploadNewRecipe){
            startActivity(new Intent(MainActivity.this,UploadRecipeActivity.class));

        } else if(item.getItemId()==R.id.signout){
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment());
        //navbar
        NavbarView =findViewById(R.id.navbar_view);
        NavbarView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        NavbarView.setItemIconTintList(null);//bu ne imiş araştır

        firebaseAuth=FirebaseAuth.getInstance();


    }



}