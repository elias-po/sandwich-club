package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        String mainName = null;
        List<String> alsoKnownAs = new ArrayList<>();
        String placeOfOrigin = null;
        String description = null;
        String image = null;
        List<String> ingredients = new ArrayList<>();
        int curvedBrackets = 0, angledBrackets = 0; // counters to check if all brackets are closed

        String temp = "",   // used to store text from inside quotes
                var = "";   // used to store a name of a variable (that has been acquired from inside quotes)

        boolean ignoreSpecialChar = false, // types the next character like quote or slash instead of registering it's action
                closingQuotes = false,  // indicates if the quotes met are the closing ones (or not)
                expectValue = false,    // indicates if the text inside next quotes is a value of a variable
                expectListItem = false; // indicates if the text inside next quotes is an item of a list


        for(int i = 0; i < json.length(); i++){
            switch (Character.toString(json.charAt(i))){
                case "{":
                    curvedBrackets++;
                    expectValue = false;
                    break;
                case "[":
                    angledBrackets++;
                    expectListItem = true;
                    break;
                case "}":
                    curvedBrackets--;
                    break;
                case "]":
                    angledBrackets--;
                    expectListItem = false;
                    break;
                case ":":
                    if(closingQuotes)
                        temp += json.charAt(i);
                    else
                        expectValue = true;
                    break;
                case ",":
                    break;
                case "\\":
                    ignoreSpecialChar = true;
                    break;
                case "\"":
                    if(closingQuotes && !ignoreSpecialChar){
                        closingQuotes = false;
                        if(expectValue) {
                            //find the proper variable and assign the value to it
                            switch (var){
                                case "mainName":
                                    mainName = temp;
                                    break;
                                case "placeOfOrigin":
                                    placeOfOrigin = temp;
                                    break;
                                case "description":
                                    description = temp;
                                    break;
                                case "image":
                                    image = temp;
                                    break;
                            }
                            expectValue = false;
                        } else if (expectListItem) {
                            //find the proper variable and assign the value to it
                            switch (var){
                                case "alsoKnownAs":
                                    alsoKnownAs.add(temp);
                                    break;
                                case "ingredients":
                                    ingredients.add(temp);
                                    break;
                            }
                        } else {
                            var = temp;
                        }
                        temp = "";
                    } else if (ignoreSpecialChar) {
                        temp += json.charAt(i);
                        ignoreSpecialChar = false;
                    } else {
                        closingQuotes = true;
                    }
                    break;
                default:
                    temp += json.charAt(i);
            }
        }

        if (alsoKnownAs.size() == 0)
            alsoKnownAs = null;
        if (ingredients.size() == 0)
            ingredients = null;
        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }
}
