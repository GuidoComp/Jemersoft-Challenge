package com.challenge.Apirest.Services;

import com.challenge.Apirest.Models.Ability;
import com.challenge.Apirest.Models.Pokemon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedList;

@Service
public class PokemonService {
    private static final String URLPOKE_API = "https://pokeapi.co/api/v2/pokemon/";
    private static final String POKEMON_JSONOBJECT_KEY = "url"; //clave de url de cada pokemon
    private static final String URL_PARAMS = "?limit=1000000&offset=0";
    private static final String ALL_POKEMONS = "https://pokeapi.co/api/v2/pokemon?limit=100000&offset=0";
    private static final String POKEMONS_JSONARRAY_KEY = "results"; //clave de jsonarray de pokemones
    private static final String PHOTO_OBJECT_KEY = "sprites";
    private static final String PHOTO_KEY = "front_default";
    private static final String TYPES_KEY = "types";
    private static final String TYPE_KEY = "type";
    private static final String TYPENAME_KEY = "name";
    private static final String WEIGHT_KEY = "weight";
    private static final String ABILITIES_KEY = "abilities";
    private static final String ABILITY_KEY = "ability";
    private final RestTemplate restTemplate;

    public PokemonService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    //Método más general, que abarca los métodos necesarios para devolver la lista de Pokemones con la información solicitada.
    public LinkedList<Pokemon> getBasicInfoAllPokemons() throws IOException {
        LinkedList<Pokemon> lista = new LinkedList<>();
        LinkedList<String> pokemonsUrlsList = this.readApi();

        for (int i = 0; i < pokemonsUrlsList.size(); i++) {
            lista.add(this.getBasicInfo(pokemonsUrlsList.get(i)));
        }
        return lista;
    }

    /*public Pokemon getBasicInfoAllPokemons2() throws IOException {
        LinkedList<Pokemon> lista = new LinkedList<>();
        //LinkedList<String> pokemonsUrlsList = this.readApi();
        String url = "https://pokeapi.co/api/v2/pokemon/1";
        Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
        //log.info("Result " + pokemon);



        return pokemon;
    }*/

    //Convierte la URL oficial a una colección que contiene la URL (en formato String) de cada pokemon
    private LinkedList<String> readApi() {
        LinkedList<String> orderedURLs = null;

        String unorderedPokemonsUrls = restTemplate.getForObject(URLPOKE_API, String.class);
        JSONObject jsonObject = new JSONObject(unorderedPokemonsUrls);
        JSONArray jsonArray = jsonObject.getJSONArray(POKEMONS_JSONARRAY_KEY);
        orderedURLs = this.jsonArrayToLinkedList(jsonArray, POKEMON_JSONOBJECT_KEY);

        return orderedURLs;
    }

    //Obtiene información requerida a partir de una URL de un pokemon. Devuelve un objeto Pokemon con tal información.
    private Pokemon getBasicInfo(String urlPoke) throws IOException {
        Pokemon pokemon = new Pokemon();
        String stringJsonObject = null;

        stringJsonObject = restTemplate.getForObject(urlPoke, String.class);
        JSONObject pokeJsonObject = new JSONObject(stringJsonObject);

        pokemon.setPhoto(this.searchPhotoUrl(pokeJsonObject));
        pokemon.setTypes(this.searchTypes(pokeJsonObject));
        pokemon.setWeight(this.searchWeigth(pokeJsonObject));
        pokemon.setAbilities(this.searchAbilities(pokeJsonObject));

        return pokemon;
    }

    //Método usado en la 2da llamada (get request "/{id}"). Reutiliza el método que devuelve la información básica, pero para un solo Pokemon.
    //Le agrega nueva información requerida (descripción y lista de movimientos).
    public Pokemon getAllDetails(String id) throws IOException {
        Pokemon pokemon = null;
        String jsonObjectString = null;
        pokemon = this.getBasicInfo(URLPOKE_API + id);

        jsonObjectString = restTemplate.getForObject(URLPOKE_API + id, String.class);
        JSONObject pokeJsonObject = new JSONObject(jsonObjectString);

        pokemon.setDescription(this.searchName(pokeJsonObject));
        pokemon.setMoves(this.searchMoves(pokeJsonObject));

        return pokemon;
    }


    private String searchPhotoUrl(JSONObject pokeJsonObject) throws IOException {
        JSONObject fotosJObject = (JSONObject) pokeJsonObject.get(PHOTO_OBJECT_KEY);
        String urlPhoto = fotosJObject.get(PHOTO_KEY).toString();
        return urlPhoto;
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

    private int searchWeigth(JSONObject pokeJsonObject) {
        return (int) pokeJsonObject.get(WEIGHT_KEY);
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

    /*private URL checkURLResponse(String urlParam) {
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
    }*/
    //Método usado para leer la API externa (readApi()). Genera la lista ordenada de URLs de cada pokemon,
    //iterando los elementos de un jsonArray.
    //Se usa para ordenar la lista oficial de Pokemones.
    private LinkedList<String> jsonArrayToLinkedList(JSONArray jsonArray, String clave) {
        LinkedList<String> listaURLs = new LinkedList<>();
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            listaURLs.add(jsonObject.getString(clave));
        }
        return listaURLs;
    }
}
