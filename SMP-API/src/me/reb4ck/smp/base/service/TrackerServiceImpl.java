package me.reb4ck.smp.base.service;

import com.google.inject.Singleton;
import me.reb4ck.smp.api.service.TrackerService;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.base.tracker.TrackID;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public final class TrackerServiceImpl implements TrackerService {
    private static final String url = "https://api.mojang.com/users/profiles/minecraft/%name%?at=0";
    private final Map<String, ITrackID> trackIDMap = new HashMap<>();

    @Override
    public ITrackID getPremiumUUID(String name) {
        if(!trackIDMap.containsKey(name)) trackIDMap.put(name, getResponse(url, name));

        return trackIDMap.get(name);
    }

    public ITrackID getResponse(String url, String name){
        return new TrackID(getResponse(url.replace("%name%", name)));
    }

    public String getResponse(String urlToRead) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
        }catch (Exception e){
            System.out.println("&cError getting Mojang response for player " + urlToRead);
        }

        String toReturn = UUID.randomUUID().toString();
        try {
            String[] separated = result.toString().split(",");
            toReturn = separated[1].substring(6,38);
        }catch (Exception ignored){

        }
        return toReturn;
    }
}
