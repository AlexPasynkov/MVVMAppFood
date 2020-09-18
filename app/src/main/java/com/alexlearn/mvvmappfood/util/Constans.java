package com.alexlearn.mvvmappfood.util;

public class Constans {


    //Оригинальный из ролика умер, взяли другой сайт
    public static final String BASE_URL = "https://recipesapi.herokuapp.com/";
    public static final int NETWORK_TIMEOUT = 3000;

    //Оригинальный сайт умер, заменили на другой и он не требует apikey. Но вообще ключ по зарез нужен.
    //Без него приложение не взлетит
    public static final String API_KEY = "";

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
              "barbeque",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };
}
