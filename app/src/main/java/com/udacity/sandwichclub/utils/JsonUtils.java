package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        String mainName = null;
        List<String> alsoKnownAs = null;
        String placeOfOrigin = null;
        String description = null;
        String image = null;
        List<String> ingredients = null;
        int curvedBrackets = 0, angledBrackets = 0; // counters to check if all brackets are closed

        String temp = "",   // used to store text from inside quotes
                var = "";   // used to store a name of a variable (that has been acquired from inside quotes)

        boolean closingQuotes = false,  // indicates if the quotes met are the closing ones (or not)
                expectValue = false,    // indicates if the text inside next quotes is a value of a variable
                expectListItem = false; // indicates if the text inside next quotes is an item of a list


        for(int i = 0; i < json.length(); i++){
            switch (temp){
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
                    expectValue = true;
                    break;
                case ",":
                    break;
                case "\"":
                    if(closingQuotes){
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
                    } else {
                        closingQuotes = true;
                    }
                    break;
                default:
                    temp += json.charAt(i);
            }
        }

        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }
}
