package de.adesso.iotgateway.services;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


@Service
public class ParkingIdService {

    private ArrayList<String> waitingPicos = new ArrayList<>();

    public void addWaitingPico(String ip) {
        if(!waitingPicos.contains(ip)) {
            waitingPicos.add(ip);
        }
    }

    public ArrayList<String> getWaitingPicos() {
        return waitingPicos;
    }

    public boolean setParkingId(String ip, int id){
        if(!waitingPicos.contains(ip)) {
            return false;
        }
        String url = "http://" + ip + ":80";
        String payload = "{\"id\":" + id + "}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setConnection("close");
        HttpEntity<String> requestUpdate = new HttpEntity<>(payload, headers);
        var response = restTemplate.exchange(url + "/setParkingId", HttpMethod.POST,requestUpdate, String.class);

        waitingPicos.remove(ip);
        return true;
    }

}
