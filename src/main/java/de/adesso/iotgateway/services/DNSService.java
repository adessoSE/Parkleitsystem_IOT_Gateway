package de.adesso.iotgateway.services;

import de.adesso.iotgateway.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DNSService {

    private final ParkingIdService parkingIdService;
    private final Logger logger = Logger.getLogger(DNSService.class.getName());

    private String gatewayIp = "";

    @Autowired
    public DNSService(ParkingIdService parkingIdService){
        this.parkingIdService = parkingIdService;
    }

    public void handleMessage(Message message) {
        String data = new String((byte[]) message.getPayload());
        try {
            JSONObject json = new JSONObject(data);
            UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler((String) json.get("ip"), 61001);
            String payload = "{\"ip\":\"" + (gatewayIp.isEmpty() ? "unset" : gatewayIp) + "\"}";
            handler.handleMessage(MessageBuilder.withPayload(payload).build());
            parkingIdService.addWaitingPico((String) json.get("ip"));
            logger.setLevel(Level.INFO);
            logger.info("Added " + json.get("ip") + " to waiting Picos and send Gateway-IP");
        } catch (JSONException e) {
            logger.setLevel(Level.WARNING);
            logger.warning("Unsupported UDP-Payload: " + data);
        }
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public boolean setGatewayIp(String ip) {
        if(Utils.validateIp(ip)) {
            gatewayIp = ip;
            logger.setLevel(Level.CONFIG);
            logger.config("Gateway-IP set to " + ip);
            return true;
        }
        logger.setLevel(Level.WARNING);
        logger.warning("Given IP was invalid: " + ip);
        return false;
    }
}
