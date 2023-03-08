package mongodb_demo;

import org.bson.types.ObjectId;

import java.util.StringJoiner;


public class Restaurant {

    public ObjectId id;
    public String restaurantName;
    public int rating;

    @Override
    public String toString() {
        StringJoiner s = new StringJoiner(", ");

        s.add(id.toString());
        s.add(restaurantName);
        s.add(String.valueOf(rating));

        return s.toString();
    }
}
