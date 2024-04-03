package de.adesso.iotgateway.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.stereotype.Controller;

@Controller
public class UDPController {

    @Bean
    public IntegrationFlow processBroadcastUdpMessage() {
        return IntegrationFlow
                .from(new UnicastReceivingChannelAdapter(61000))
                .handle("DNSService", "handleMessage")
                .get();
    }
}
