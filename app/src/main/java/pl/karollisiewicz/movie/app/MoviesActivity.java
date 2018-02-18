package pl.karollisiewicz.movie.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.karollisiewicz.movie.R;

public class MoviesActivity extends AppCompatActivity {

    @BindView(R.id.tvHello)
    TextView textHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        textHello.setOnClickListener(v -> startActivity(new Intent(MoviesActivity.this, MovieDetailsActivity.class)));
    }
}
