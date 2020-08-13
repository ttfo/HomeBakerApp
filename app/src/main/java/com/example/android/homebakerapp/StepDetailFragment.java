package com.example.android.homebakerapp;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.homebakerapp.model.Step;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.homebakerapp.dummy.DummyContent;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

//    public static final String ARG_ITEM_ID = "item_id"; //

    private Step mStep;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(getContext().getResources().getString(R.string.ARG_ITEM_ID))) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle bundle = getArguments();
            Log.i("BUNDLE", "MY BUNDLE VALUE: " + getContext().getResources().getString(R.string.ARG_ITEM_ID));
            mStep = (Step) bundle.getSerializable(getContext().getResources().getString(R.string.ARG_ITEM_ID));

            Log.i("STEP FRAG", "STEP: " + mStep.getShortDescription());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mStep != null) {
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(mStep.getDescription());
        }

        return rootView;
    }
}