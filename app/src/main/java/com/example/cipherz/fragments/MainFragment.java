package com.example.cipherz.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cipherz.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFragment extends Fragment {
    public static final String TAG = "CipherZTag";
    public static final String JOKES_URL = "https://official-joke-api.appspot.com/random_joke";
    protected View view;
    private TextView jokeLabel;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
        }
        initViews();
        generateJoke();

        return view;
    }

    private void initViews() {
        jokeLabel = view.findViewById(R.id.main_LBL_factOfTheDay);
        jokeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateJoke();
            }
        });
    }

    /**
     * A method to read the jokes api and display on the page
     */
    private void generateJoke() {
        Log.d(TAG, "initJokes: ");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(JOKES_URL)
                .header("Content-Type", "application/json")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String initialResponse = response.body().string();
                Log.d(TAG, "onResponse: " + initialResponse);
                try {
                    JSONObject baseObject = new JSONObject(initialResponse);
                    String setup = baseObject.getString("setup");
                    String punchline = baseObject.getString("punchline");
                    final String joke = setup + "\n" + punchline;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jokeLabel.setText(joke);
                            jokeLabel.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }
        });
    }
}