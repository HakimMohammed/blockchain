package com.inventory.ui.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.utils.Env;
import okhttp3.*;

import java.io.IOException;

public class HttpService {
    private static final String API_URL = Env.get("API_URL");
    private final OkHttpClient client;

    public HttpService() {
        this.client = new OkHttpClient();
    }

    private Request.Builder addAuthHeader(Request.Builder requestBuilder) throws IOException {
        String accessToken = TokenStorageService.loadToken();
        return requestBuilder.header("Authorization", "Bearer " + accessToken);
    }

    public Response get(String endpoint) throws IOException {
        Request request = addAuthHeader(new Request.Builder())
                .url(API_URL + endpoint)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .get()
                .build();

        return client.newCall(request).execute();
    }

    public Response post(String endpoint, Object body) throws IOException {
        RequestBody requestBody = RequestBody.create(
                new ObjectMapper().writeValueAsString(body),
                MediaType.parse("application/json")
        );
        Request request = addAuthHeader(new Request.Builder())
                .url(API_URL + endpoint)
                .post(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public Response put(String endpoint, Object body) throws IOException {
        RequestBody requestBody = RequestBody.create(
                new ObjectMapper().writeValueAsString(body),
                MediaType.parse("application/json")
        );
        Request request = addAuthHeader(new Request.Builder())
                .url(API_URL + endpoint)
                .put(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public Response delete(String endpoint) throws IOException {
        Request request = addAuthHeader(new Request.Builder())
                .url(API_URL + endpoint)
                .delete()
                .build();
        return client.newCall(request).execute();
    }
}