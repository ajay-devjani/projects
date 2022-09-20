package com.oss.proteger.ui.verify_apps;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.oss.proteger.R;
import com.oss.proteger.databinding.FragmentVerifyAppsBinding;

import java.util.List;

public class VerifyAppsFragment extends Fragment {

    private VerifyAppsModel homeViewModel;
    private FragmentVerifyAppsBinding binding;
    private String contentsList = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(VerifyAppsModel.class);

        binding = FragmentVerifyAppsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        Button verifyAppsButton = root.findViewById(R.id.verifyAppsButton);
        verifyAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView verifyAppsTextView = (TextView) root.findViewById(R.id.verifyAppsTextView);
                verifyAppsTextView.setMovementMethod(new ScrollingMovementMethod());
                //Settings.Secure.putInt(getActivity().getContentResolver(), "package_verifier_enable", 1);

                SafetyNet.getClient(getActivity())
                        .isVerifyAppsEnabled()
                        .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.VerifyAppsUserResponse>() {
                            @Override
                            public void onComplete(Task<SafetyNetApi.VerifyAppsUserResponse> task) {
                                if (task.isSuccessful()) {
                                    SafetyNetApi.VerifyAppsUserResponse result = task.getResult();
                                    if (result.isVerifyAppsEnabled()) {
                                        Log.d("MY_APP_TAG", "The Verify Apps feature is enabled.");
                                        contentsList += "The Verify Apps feature is enabled.";
                                    } else {
                                        Log.d("MY_APP_TAG", "The Verify Apps feature is disabled.");
                                        contentsList += "The Verify Apps feature is disabled.";
                                    }
                                } else {
                                    Log.e("MY_APP_TAG", "A general error occurred.");
                                    contentsList += "A general error occurred.";
                                }
                            }
                        });

                SafetyNet.getClient(getActivity())
                        .enableVerifyApps()
                        .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.VerifyAppsUserResponse>() {
                            @Override
                            public void onComplete(Task<SafetyNetApi.VerifyAppsUserResponse> task) {
                                if (task.isSuccessful()) {
                                    SafetyNetApi.VerifyAppsUserResponse result = task.getResult();

                                    if (result.isVerifyAppsEnabled()) {
                                        Log.d("MY_APP_TAG", "The user gave consent " +
                                                "to enable the Verify Apps feature.");
                                        contentsList += "The user gave consent " +
                                                "to enable the Verify Apps feature.";
                                    } else {
                                        Log.d("MY_APP_TAG", "The user didn't give consent " +
                                                "to enable the Verify Apps feature.");
                                        contentsList += "The user didn't give consent " +
                                                "to enable the Verify Apps feature.";
                                    }
                                } else {
                                    Log.e("MY_APP_TAG", "A general error occurred.");
                                    contentsList += "A general error occurred in enabling.\n" + task.toString()
                                            + "\n" + task.getException() + "\n";
                                }
                            }
                        });


                SafetyNet.getClient(getActivity())
                        .listHarmfulApps()
                        .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.HarmfulAppsResponse>() {
                            @Override
                            public void onComplete(Task<SafetyNetApi.HarmfulAppsResponse> task) {
                                Log.d("MY_APP_TAG", "Received listHarmfulApps() result");
                                contentsList += "Received listHarmfulApps() result";

                                if (task.isSuccessful()) {
                                    SafetyNetApi.HarmfulAppsResponse result = task.getResult();
                                    long scanTimeMs = result.getLastScanTimeMs();

                                    List<HarmfulAppsData> appList = result.getHarmfulAppsList();
                                    if (appList.isEmpty()) {
                                        Log.d("MY_APP_TAG", "There are no known " +
                                                "potentially harmful apps installed.");
                                        contentsList += "There are no known " +
                                                "potentially harmful apps installed.";
                                    } else {
                                        Log.e("MY_APP_TAG",
                                                "Potentially harmful apps are installed!");
                                        contentsList += "Potentially harmful apps are installed!";

                                        for (HarmfulAppsData harmfulApp : appList) {
                                            Log.e("MY_APP_TAG", "Information about a harmful app:");
                                            contentsList += "Information about a harmful app:";
                                            Log.e("MY_APP_TAG",
                                                    "  APK: " + harmfulApp.apkPackageName);
                                            contentsList += "  APK: " + harmfulApp.apkPackageName;
                                            Log.e("MY_APP_TAG",
                                                    "  SHA-256: " + harmfulApp.apkSha256);
                                            contentsList += "  SHA-256: " + harmfulApp.apkSha256;

                                            // Categories are defined in VerifyAppsConstants.
                                            Log.e("MY_APP_TAG",
                                                    "  Category: " + harmfulApp.apkCategory);
                                            contentsList += "  Category: " + harmfulApp.apkCategory;
                                        }
                                    }
                                } else {
                                    Log.d("MY_APP_TAG", "An error occurred. " +
                                            "Call isVerifyAppsEnabled() to ensure " +
                                            "that the user has consented.");
                                    contentsList += "An error occurred. " +
                                            "Call isVerifyAppsEnabled() to ensure " +
                                            "that the user has consented.";
                                }
                            }
                        });

                verifyAppsTextView.setText(contentsList);
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}