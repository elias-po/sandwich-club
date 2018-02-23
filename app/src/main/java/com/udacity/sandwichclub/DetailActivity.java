package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    TextView placeOfOriginTv;
    TextView descriptionTv;
    TextView ingredientsTv;
    TextView alsoKnownAsTv;
    Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        placeOfOriginTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        alsoKnownAsTv = findViewById(R.id.also_known_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError(json);
            return;
        }

        populateUI();
        Toast.makeText(this, sandwich.getImage(), Toast.LENGTH_LONG).show();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
    private void closeOnError(String jsn) {
        finish();
        Toast.makeText(this, jsn, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        String unknownMessage = "¯\\_(ツ)_/¯ \n unknown";

        if(sandwich.getPlaceOfOrigin() != null){
            placeOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            placeOfOriginTv.setText(unknownMessage);
        }
        if(sandwich.getDescription() != null){
            descriptionTv.setText(sandwich.getDescription());
        } else {
            descriptionTv.setText(unknownMessage);
        }

        if(sandwich.getIngredients() != null) {
            for (String item : sandwich.getIngredients()) {
                ingredientsTv.append("* " + item + "\n\n");
            }
            //ingredientsTv.setText((android.text.SpannableStringBuilder)ingredientsTv.getText().subSequence(0, ingredientsTv.getText().length()-2));
        } else {
            ingredientsTv.setText(unknownMessage);
        }
        if(sandwich.getAlsoKnownAs() != null) {
            for (String name : sandwich.getAlsoKnownAs()) {
                alsoKnownAsTv.append(name + ",\n");
            }
            //alsoKnownAsTv.setText((android.text.SpannableStringBuilder)alsoKnownAsTv.getText().subSequence(0, alsoKnownAsTv.getText().length()-1));
        } else {
            alsoKnownAsTv.setText(" ---");
        }
    }
}
