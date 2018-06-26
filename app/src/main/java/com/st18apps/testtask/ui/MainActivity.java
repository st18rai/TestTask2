package com.st18apps.testtask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.st18apps.testtask.App;
import com.st18apps.testtask.R;
import com.st18apps.testtask.adapters.RecyclerAdapter;
import com.st18apps.testtask.api.APIInterface;
import com.st18apps.testtask.api.ApiClient;
import com.st18apps.testtask.model.User;
import com.st18apps.testtask.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);
        Call<List<User>> call = apiService.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                users.addAll(response.body());

                if (App.getInstance().getUserList().isEmpty())
                    App.getInstance().setUserList(users);

                final RecyclerAdapter adapter = new RecyclerAdapter(users);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                goToDetailScreen(position);
            }
        });

    }

    private void goToDetailScreen(int position) {
        Intent intent = new Intent(this, UserDetailsScreen.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
