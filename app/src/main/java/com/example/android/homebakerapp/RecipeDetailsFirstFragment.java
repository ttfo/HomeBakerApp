package com.example.android.homebakerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.homebakerapp.model.Recipe;

public class RecipeDetailsFirstFragment extends Fragment {

    public static final String RECIPE_OBJ_LABEL = "my_recipe";

    // private Button mButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Recipe clickedRecipeObj = new Recipe();

        // Check https://stackoverflow.com/questions/17076663/problems-with-settext-in-a-fragment-in-oncreateview
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_recipe_details_first, container, false);

        Bundle bundle = this.getArguments();
        //Log.i("FRAGMENT", "Fragment onCreateView"); <= test point
        if (bundle != null) {
            clickedRecipeObj = (Recipe) bundle.getSerializable(RECIPE_OBJ_LABEL);
            //Log.i("FRAGMENT", "Bundle is not null"); <= test point
            //Log.i("FRAGMENT", "Recipe name: " + clickedRecipeObj.getName()); <= test point
        }


        // Code below is just for testing purposes (would replace button text with the recipe name)
//        mButton = (Button) view.findViewById(R.id.button_first);
//        mButton.setText(clickedRecipeObj.getName());

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RecipeDetailsFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}