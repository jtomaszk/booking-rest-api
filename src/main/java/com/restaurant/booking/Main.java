package com.restaurant.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BookingRepository bookingRepository = new BookingRepository();

        MuServer server = MuServerBuilder.httpServer()
                .withHttpPort(64741)
                .addHandler(Method.GET, "/{date}", (request, response, pathParams) -> {
                    LocalDate date = LocalDate.parse(pathParams.get("date"));
                    List<Booking> itemsForDate = bookingRepository.get(date);
                    response.write(getGson().toJson(itemsForDate));
                })
                .addHandler(Method.POST, "/", (request, response, pathParams) -> {
                    String bodyAsString = new String(request.inputStream().get().readAllBytes());
                    Booking booking = getGson().fromJson(bodyAsString, Booking.class);
                    bookingRepository.add(booking);
                })
                .start();
        System.out.println("Started server at " + server.uri());
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        @Override
        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString());
        }
    }

    static class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

        public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(time.format(DateTimeFormatter.ISO_TIME));
        }

        @Override
        public LocalTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalTime.parse(jsonElement.getAsJsonPrimitive().getAsString());
        }
    }
}
