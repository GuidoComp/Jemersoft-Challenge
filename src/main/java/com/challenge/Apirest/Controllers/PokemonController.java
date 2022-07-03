package com.challenge.Apirest.Controllers;

import com.challenge.Apirest.Models.Ability;
import com.challenge.Apirest.Models.Pokemon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

@RestController
@RequestMapping("pokemons")
public class PokemonController {

    private static final String URLPOKE_API = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=60";
    private static final int HTTPSTATUS_OK = 200;
    private static final String POKEMON_JSONOBJECT_KEY = "url"; //clave de url de cada pokemon
    private static final String POKEMONS_JSONARRAY_KEY = "results"; //clave de jsonarray de pokemones
    private static final String PHOTO_OBJECT_KEY = "sprites";
    private static final String PHOTO_KEY = "front_default";

    private static final String TYPES_KEY = "types";
    private static final String TYPE_KEY = "type";
    private static final String TYPENAME_KEY = "name";
    private static final String WEIGHT_KEY = "weight";
    private static final String ORDER_KEY = "order";
    private static final String ABILITIES_KEY = "abilities";
    private static final String ABILITY_KEY = "ability";

    @GetMapping
    protected Pokemon probar() throws IOException {
        //ARRAY LIST DE DIRECCIONES DE CADA POKEMON
        ArrayList<String> listaURLsPokemons = this.jsonArrayToArrayList(this.convertURLToJsonArray(URLPOKE_API, POKEMONS_JSONARRAY_KEY), POKEMON_JSONOBJECT_KEY);
        System.out.println(listaURLsPokemons);
        //POKEMON EN OBJECTO JSON
        JSONObject pokeJsonObject = this.convertURLToJsonObject(listaURLsPokemons.get(0));
        //OBJETO JSON DE FOTOS DEL POKEMON
        JSONObject fotosJObject = (JSONObject) pokeJsonObject.get(PHOTO_OBJECT_KEY);
        //URL DE LA FOTO DEL POKEMON
        String urlPhoto = fotosJObject.get(PHOTO_KEY).toString();
        //System.out.println(fotosJObject);
        System.out.println(fotosJObject.get(PHOTO_KEY).toString());
        Pokemon pokemon = new Pokemon();
        pokemon.setPhoto(urlPhoto);
        //ARRAY DE TYPES (2 elementos)
        JSONArray typesArray = pokeJsonObject.getJSONArray(TYPES_KEY);
        System.out.println(typesArray);
        //456
        //conversion de json array de types a arrayList de types
        ArrayList<String> tipos = new ArrayList<>();
        for (int i = 0; i < typesArray.length(); i++) {
            JSONObject typeObject = typesArray.getJSONObject(i);
            JSONObject type = (JSONObject) typeObject.get(TYPE_KEY);
            String nameType = type.get(TYPENAME_KEY).toString();
            tipos.add(nameType);
        }
        //456
        System.out.println(tipos);
        pokemon.setTypes(tipos);
        /*JSONObject typeObect1 = typesArray.getJSONObject(0);

        System.out.println(typeObect1);

        JSONObject type1 = (JSONObject) typeObect1.get(TYPE_KEY);
        System.out.println(type1);

        String nameType1 = type1.get(TYPENAME_KEY).toString();
        System.out.println(nameType1);*/

        //peso
        int weight = (int) pokeJsonObject.get(WEIGHT_KEY);
        System.out.println("Peso: " + weight);
        pokemon.setWeight(weight);
        //order(id)
        int id = (int) pokeJsonObject.get(ORDER_KEY);
        System.out.println("Order: " + id);
        pokemon.setId(id);
        //abilities
        JSONArray abilitiesArray = pokeJsonObject.getJSONArray(ABILITIES_KEY);
        String abilityName = null;
        String abilityURL = null;
        boolean is_hidden = false;
        int abilitySlot = -1;
        ArrayList<Ability> pokemonAbilities = new ArrayList<>();
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
        pokemon.setAbilities(pokemonAbilities);
        //moves
        ArrayList<String> moveList = new ArrayList<>();
        JSONArray movesJArray = pokeJsonObject.getJSONArray("moves");
        for (int i = 0; i < movesJArray.length(); i++) {
            JSONObject moveObject = movesJArray.getJSONObject(i);
            JSONObject moveMoveObject = moveObject.getJSONObject("move");
            String moveName = moveMoveObject.get("name").toString();
            //Move move = new Move(moveName);
            moveList.add(moveName);
        }
        pokemon.setMoves(moveList);
        System.out.println(pokeJsonObject);

        /*JSONObject moveObject = movesJArray.getJSONObject(0);
        JSONObject moveMoveObject = moveObject.getJSONObject("move");
        String moveName = moveMoveObject.get("name").toString();

        moveList.add(moveName);*/

        return pokemon;
    }

    private JSONArray convertURLToJsonArray(String urlParam, String jsonArrayKey) throws IOException {
        return this.convertURLToJsonObject(urlParam).getJSONArray(jsonArrayKey);
    }

    private JSONObject convertURLToJsonObject (String urlParam) throws IOException {
        JSONObject jsonObject = null;
        StringBuilder informationString = null;
        Scanner input = null;

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

    private ArrayList<String> jsonArrayToArrayList(JSONArray jsonArray, String clave) {
        ArrayList<String> listaURLs = new ArrayList<>();
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            listaURLs.add(jsonObject.getString(clave));
        }
        return listaURLs;
    }

    /*private JSONObject generatePokemon(String listUrl) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(listUrl);
            StringBuilder informationString = new StringBuilder();
            Scanner input = new Scanner(url.openStream());
            while (input.hasNext()) {
                informationString.append(input.nextLine());
            }
            input.close();
            jsonObject = new JSONObject(informationString.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }*/
}
