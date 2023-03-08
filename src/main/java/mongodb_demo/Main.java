package mongodb_demo;

import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {
    private static String DB_NAME = "230308_BookingService";
    private static String RESTAURANTS_COLLECTION = "restaurants";

    public static void main(String[] args) {
        var restaurants = getRestaurants();

        System.out.println(restaurants);
    }

    private static MongoCollection<Restaurant> getRestaurantsCollection() {

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("se.distansakademin.mongodb_demo").build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        pojoCodecRegistry = fromRegistries(pojoCodecRegistry, fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        pojoCodecRegistry.get(Restaurant.class);

        MongoClient mongoClient = MongoClients.create();

        MongoDatabase database = mongoClient.getDatabase(DB_NAME).withCodecRegistry(pojoCodecRegistry);

        MongoCollection<Restaurant> collection = database
                .getCollection(RESTAURANTS_COLLECTION, Restaurant.class);

        return collection;
    }

    private static void insertRestaurant(String restaurantName, int rating){
        var restaurantCollection = getRestaurantsCollection();

        var restaurant = new Restaurant();

        restaurant.restaurantName = restaurantName;
        restaurant.rating = rating;

        restaurantCollection.insertOne(restaurant);
    }

    public static List<Restaurant> getRestaurants(){
        MongoCollection<Restaurant> restaurantCollection = getRestaurantsCollection();

        List<Restaurant> restaurantList = new ArrayList<>();
        try (MongoCursor<Restaurant> cursor = restaurantCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Restaurant restaurant = cursor.next();
                restaurantList.add(restaurant);
            }
        }

        return restaurantList;
    }
}

