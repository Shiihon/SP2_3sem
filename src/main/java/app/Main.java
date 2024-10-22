package app;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Main {
    public static void main(String[] args) {
        //EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("persons");

        // Initialize Javalin app
        Javalin app = Javalin.create().start(7000);


        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Define a simple route that returns "Hello World" as JSON
        app.get("/", ctx -> sendHelloWorld(ctx, objectMapper));
    }

    // Function to handle the context and use ObjectMapper to send JSON response
    private static void sendHelloWorld(Context ctx, ObjectMapper objectMapper) {
        try {
            // Create a simple message
            Message message = new Message("Hello World, this is happening");

            // Convert the message to JSON and send it as the response
            String jsonResponse = objectMapper.writeValueAsString(message);
            ctx.json(jsonResponse); // Send JSON response using Javalin's Context
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple Message class for demonstration
    public static class Message {
        private String message;

        public Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public int add(int a, int b) {
        return a + b;
    }
}