package com.example.searchengine;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView resultsRecyclerView;
    private SearchResultAdapter adapter;
    private List<SearchResult> searchResults;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);

        searchResults = new ArrayList<>();
        adapter = new SearchResultAdapter(searchResults);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsRecyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        searchButton.setOnClickListener(v -> performSearch());

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://flask-app-vercel-phi.vercel.app/?q=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    searchResults.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);
                            String title = item.getString("title");
                            String body = item.getString("body");
                            String href = item.getString("href");
                            searchResults.add(new SearchResult(title, body, href));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }
}