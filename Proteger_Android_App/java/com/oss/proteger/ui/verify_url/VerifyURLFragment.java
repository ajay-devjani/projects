package com.oss.proteger.ui.verify_url;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafeBrowsingThreat;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.oss.proteger.R;
import com.oss.proteger.databinding.FragmentVerifyUrlBinding;

public class VerifyURLFragment extends Fragment {

    private VerifyURLModel verifyURLModel;
    private FragmentVerifyUrlBinding binding;
    final private String SAFE_BROWSING_API_KEY = "AIzaSyDHChHVUlHxE7hIekE635VmOG5DwoqeKgY";
    public static final int TYPE_POTENTIALLY_HARMFUL_APPLICATION = 4;
    public static final int TYPE_SOCIAL_ENGINEERING = 5;
    private String contentsList="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        verifyURLModel =
                new ViewModelProvider(this).get(VerifyURLModel.class);

        binding = FragmentVerifyUrlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        String url = "https://www.google.com";
        SafetyNet.getClient(getActivity()).lookupUri(url,
                SAFE_BROWSING_API_KEY,
                SafeBrowsingThreat.TYPE_POTENTIALLY_HARMFUL_APPLICATION,
                SafeBrowsingThreat.TYPE_SOCIAL_ENGINEERING)
                .addOnSuccessListener(getActivity(),
                        new OnSuccessListener<SafetyNetApi.SafeBrowsingResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.SafeBrowsingResponse sbResponse) {
                                // Indicates communication with the service was successful.
                                // Identify any detected threats.
                                contentsList += "Communication with Google API Successful.\n";
                                if (sbResponse.getDetectedThreats().isEmpty()) {
                                    // No threats found.
                                    contentsList += "No threats found in URL: " + url;
                                } else {
                                    // Threats found!
                                    contentsList += "Threats found.";
                                }
                            }
                        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while communicating with the service.
                        if (e instanceof ApiException) {
                            // An error with the Google Play Services API contains some
                            // additional details.
                            ApiException apiException = (ApiException) e;
                            contentsList += "Error: " + CommonStatusCodes
                                    .getStatusCodeString(apiException.getStatusCode());

                            // Note: If the status code, apiException.getStatusCode(),
                            // is SafetyNetstatusCode.SAFE_BROWSING_API_NOT_INITIALIZED,
                            // you need to call initSafeBrowsing(). It means either you
                            // haven't called initSafeBrowsing() before or that it needs
                            // to be called again due to an internal error.
                        } else {
                            // A different, unknown type of error occurred.
                            contentsList += "Error: " + e.getMessage();
                        }
                    }
                });

        SafetyNet.getClient(getActivity()).shutdownSafeBrowsing();
        TextView verifyURLTextView = (TextView) root.findViewById(R.id.verifyURLTextView);
        verifyURLTextView.setMovementMethod(new ScrollingMovementMethod());
        //Settings.Secure.putInt(getActivity().getContentResolver(), "package_verifier_enable", 1);

        verifyURLTextView.setText(contentsList);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}