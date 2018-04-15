package com.dev.apds.helpers;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class AndroidPushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAkbttwxQ:APA91bFIwXO9wt4HyvKAicB80MmtcmlXQA8jI9lYVlnVNxArdXfv1t9v2Y8f1CGSG-aokGeNFiFi1HDceRPvy53uALb0BWta_PsCwI74jbz6F6IHEfaJ7IIZNRDoh0N3wRWN1pomFhjJ";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String response = restTemplate.postForObject("https://fcm.googleapis.com/fcm/send", entity, String.class);

        return CompletableFuture.completedFuture(response);
    }

}
