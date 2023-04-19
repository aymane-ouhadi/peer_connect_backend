package com.example.peerconnectbackend.utils;

import com.example.peerconnectbackend.entities.Event;
import com.example.peerconnectbackend.entities.Post;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Functions {

    public static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

        return email.matches(emailRegex);
    }

    public static List<Post> sortPostsByPublishedDate(List<Post> posts){

        Comparator<Post> byPublishedAtDesc = Comparator.comparing(Post::get_publishedAt).reversed();
        posts.sort(byPublishedAtDesc);

        return posts;

    }

    public static List<Event> sortEventsByPublishedDate(List<Event> events){

        Comparator<Event> byPublishedAtDesc = Comparator.comparing(Event::get_publishedAt).reversed();
        events.sort(byPublishedAtDesc);

        return events;

    }


}
