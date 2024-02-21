package com.bl4nk.weardrobe.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bl4nk.weardrobe.MainActivity;
import com.bl4nk.weardrobe.R;
import com.bl4nk.weardrobe.UserViewModel;

import java.io.File;
import java.util.Random;

public class Recomendation extends Fragment {

    private TextView selectedStyleTv;
    private Button acceptBtn;
    private Button declineBtn;
    private ImageView imageView;
    private ImageView topClothes;
    private ImageView bottomClothes;
    UserViewModel userViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Recomendation() {
        // Required empty public constructor
    }

    public static Recomendation newInstance(String param1, String param2) {
        Recomendation fragment = new Recomendation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        MainActivity activity = (MainActivity) requireActivity();
        userViewModel = activity.getSharedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recomendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedStyleTv = view.findViewById(R.id.selectedStyleTv);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        declineBtn = view.findViewById(R.id.declineBtn);
        imageView = view.findViewById(R.id.imageView);
        topClothes = view.findViewById(R.id.topClothes);
        bottomClothes = view.findViewById(R.id.bottomClothes);

        declineBtn.setOnClickListener(v -> {
            // Get the currently selected style
            String style = selectedStyleTv.getText().toString();
            // Load random images for top and bottom clothes again
            loadRandomImages(style);
        });

        userViewModel.getSelectedStyle().observe(getViewLifecycleOwner(), style -> {
            if (style != null) {
                selectedStyleTv.setText(style);
                // Load random images from the appropriate directory based on the selected style
                loadRandomImages(style);
            } else {
                // If style is null, display "Select style in home"
                selectedStyleTv.setText("Select style in home");
            }
        });

    }

    private void loadRandomImages(String style) {
        String topDirectory = "Top " + style;
        String bottomDirectory = "Bottom " + style;
        File topDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + topDirectory);
        File bottomDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WearDrobe/" + bottomDirectory);

        if (topDir.exists() && bottomDir.exists()) {
            File[] topFiles = topDir.listFiles();
            File[] bottomFiles = bottomDir.listFiles();

            if (topFiles != null && topFiles.length > 0 && bottomFiles != null && bottomFiles.length > 0) {
                // Get random images from top and bottom directories
                Random random = new Random();
                File randomTopFile = topFiles[random.nextInt(topFiles.length)];
                File randomBottomFile = bottomFiles[random.nextInt(bottomFiles.length)];

                // Load images into ImageViews
                topClothes.setImageURI(Uri.fromFile(randomTopFile));
                bottomClothes.setImageURI(Uri.fromFile(randomBottomFile));
            } else {
                Toast.makeText(getContext(), "No images found in " + style + " directories", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Directories for " + style + " do not exist", Toast.LENGTH_SHORT).show();
        }
    }
}
