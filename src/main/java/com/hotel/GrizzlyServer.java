package com.hotel;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.glassfish.jersey.jackson.JacksonFeature;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import org.glassfish.grizzly.http.server.StaticHttpHandler;


import java.net.URI;

public class GrizzlyServer {
    public static final String BASE_URI = "http://localhost:8080/api/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .packages("com.hotel.resources")
                .register(JacksonFeature.class)
                .register(ObjectMapperProvider.class)
                .register(com.hotel.config.CorsFilter.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        // ✅ Ajout du gestionnaire de fichiers statiques (pour servir index.html)
        StaticHttpHandler staticHandler = new StaticHttpHandler("src/main/webapp/");
        server.getServerConfiguration().addHttpHandler(staticHandler, "/");

        return server;
    }

    @Provider
    public static class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
        private final ObjectMapper objectMapper;

        public ObjectMapperProvider() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // ✅ Activation du support de LocalDate
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return objectMapper;
        }
    }

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.println("✅ Serveur Grizzly démarré sur " + BASE_URI);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
