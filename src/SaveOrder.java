
import com.mongodb.client.*;
import org.bson.Document;
// import java.util.ArrayList;
import java.util.List;

public class SaveOrder {
    private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static MongoDatabase database = mongoClient.getDatabase("BillingSystem");
    private static MongoCollection<Document> collection = database.getCollection("SavedOrders");

    // Save current order
    public static void saveOrder(String name, String number, List<Document> items, double total) {
        Document doc = new Document("CustomerName", name)
                .append("MobileNumber", number)
                .append("Items", items)
                .append("Total", total);
        collection.insertOne(doc);
        System.out.println("Order saved successfully!");
    }

    // Retrieve order by number
    public static Document retrieveOrder(String number) {
        return collection.find(new Document("MobileNumber", number)).first();
    }

    // Delete order after retrieval (optional)
    public static void deleteOrder(String number) {
        collection.deleteOne(new Document("MobileNumber", number));
    }
}
