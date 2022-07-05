package com.challenge.Apirest.Services;

import com.challenge.Apirest.Models.Ability;
import com.challenge.Apirest.Models.Pokemon;
import com.challenge.Apirest.Repositories.PokemonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

@Service
public class PokemonService {
    private static final String URLPOKE_API = "https://pokeapi.co/api/v2/pokemon/";
    private static final int HTTPSTATUS_OK = 200;
    private static final String POKEMON_JSONOBJECT_KEY = "url"; //clave de url de cada pokemon
    private static final String POKEMONS_JSONARRAY_KEY = "results"; //clave de jsonarray de pokemones
    private static final String PHOTO_OBJECT_KEY = "sprites";
    private static final String PHOTO_KEY = "front_default";
    private static final String TYPES_KEY = "types";
    private static final String TYPE_KEY = "type";
    private static final String TYPENAME_KEY = "name";
    private static final String WEIGHT_KEY = "weight";
    private static final String ABILITIES_KEY = "abilities";
    private static final String ABILITY_KEY = "ability";

    public LinkedList<Pokemon> generatePokemons() throws IOException {
        LinkedList<Pokemon> lista = new LinkedList<>();
        LinkedList<String> pokemonsUrlsList = this.jsonArrayToArrayList(this.convertURLToJsonArray(URLPOKE_API, POKEMONS_JSONARRAY_KEY), POKEMON_JSONOBJECT_KEY);

        for (String pokemonUrl: pokemonsUrlsList) {
            JSONObject pokeJsonObject = this.convertURLToJsonObject(pokemonUrl);
            lista.add(this.generatePokemon(pokeJsonObject));
        }
        return lista;
    }

    private Pokemon generatePokemon(JSONObject pokeJsonObject) throws IOException {
        Pokemon pokemon = new Pokemon();

        pokemon.setPhoto(this.searchPhotoUrl(pokeJsonObject));
        pokemon.setTypes(this.searchTypes(pokeJsonObject));
        pokemon.setWeight(this.searchWeigth(pokeJsonObject));
        pokemon.setAbilities(this.searchAbilities(pokeJsonObject));
        pokemon.setDescription(this.searchName(pokeJsonObject));
        pokemon.setMoves(this.searchMoves(pokeJsonObject));

        return pokemon;
    }

    private String searchName(JSONObject pokeJsonObject) {
        return pokeJsonObject.get("name").toString();
    }

    private LinkedList<String> searchMoves(JSONObject pokeJsonObject) {
        LinkedList<String> moveList = new LinkedList<>();
        JSONArray movesJArray = pokeJsonObject.getJSONArray("moves");

        for (int i = 0; i < movesJArray.length(); i++) {
            JSONObject moveObject = movesJArray.getJSONObject(i);
            JSONObject moveMoveObject = moveObject.getJSONObject("move");
            String moveName = moveMoveObject.get("name").toString();
            moveList.add(moveName);
        }
        return moveList;
    }

    private LinkedList<Ability> searchAbilities(JSONObject pokeJsonObject) {
        LinkedList<Ability> pokemonAbilities = new LinkedList<>();
        JSONArray abilitiesArray = pokeJsonObject.getJSONArray(ABILITIES_KEY);
        String abilityName = null;
        String abilityURL = null;
        boolean is_hidden = false;
        int abilitySlot = -1;

        for (int i = 0; i < abilitiesArray.length(); i++) {
            Ability pokemonAbility = new Ability();
            JSONObject abilitiesObject = abilitiesArray.getJSONObject(i);
            JSONObject abilityObject = (JSONObject) abilitiesObject.get(ABILITY_KEY);
            abilityName = abilityObject.get("name").toString();
            abilityURL = abilityObject.get("url").toString();
            is_hidden = (boolean) abilitiesObject.get("is_hidden");
            abilitySlot = (int) abilitiesObject.get("slot");
            pokemonAbility.setName(abilityName);
            pokemonAbility.setUrl(abilityURL);
            pokemonAbility.set_hidden(is_hidden);
            pokemonAbility.setSlot(abilitySlot);

            pokemonAbilities.add(pokemonAbility);
        }
        return pokemonAbilities;
    }

    private int searchWeigth(JSONObject pokeJsonObject) {
        return (int) pokeJsonObject.get(WEIGHT_KEY);
    }

    private LinkedList<String> searchTypes(JSONObject pokeJsonObject) {
        LinkedList<String> types = new LinkedList<>();
        JSONArray typesArray = pokeJsonObject.getJSONArray(TYPES_KEY);
        for (int i = 0; i < typesArray.length(); i++) {
            JSONObject typeObject = typesArray.getJSONObject(i);
            JSONObject type = (JSONObject) typeObject.get(TYPE_KEY);
            String nameType = type.get(TYPENAME_KEY).toString();
            types.add(nameType);
        }
        return types;
    }

    private String searchPhotoUrl(JSONObject pokeJsonObject) throws IOException {
        JSONObject fotosJObject = (JSONObject) pokeJsonObject.get(PHOTO_OBJECT_KEY);
        String urlPhoto = fotosJObject.get(PHOTO_KEY).toString();
        return urlPhoto;
    }

    private JSONArray convertURLToJsonArray(String urlParam, String jsonArrayKey) throws IOException {
        return this.convertURLToJsonObject(urlParam).getJSONArray(jsonArrayKey);
    }

    private JSONObject convertURLToJsonObject (String urlParam) throws IOException {
        JSONObject jsonObject = null;
        StringBuilder informationString = null;
        Scanner input = null;

        //URL url = new URL(urlParam);
        URL url = this.checkURLResponse(urlParam);

        if (url != null) {
            informationString = new StringBuilder();
            input = new Scanner(url.openStream());
            while (input.hasNext()) {
                informationString.append(input.nextLine());
            }
            input.close();
            jsonObject = new JSONObject(informationString.toString());
        }
        return jsonObject;
    }

    private URL checkURLResponse(String urlParam) {
        URL url = null;
        int responseCode = 0;
        try {
            url = new URL(urlParam);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            responseCode = conn.getResponseCode();
            if (responseCode != HTTPSTATUS_OK) {
                url = null;
                throw new RuntimeException("Error " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private LinkedList<String> jsonArrayToArrayList(JSONArray jsonArray, String clave) {
        LinkedList<String> listaURLs = new LinkedList<>();
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            listaURLs.add(jsonObject.getString(clave));
        }
        return listaURLs;
    }
}
