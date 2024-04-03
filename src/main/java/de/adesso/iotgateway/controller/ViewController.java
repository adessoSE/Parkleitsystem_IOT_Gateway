package de.adesso.iotgateway.controller;

import de.adesso.iotgateway.services.DNSService;
import de.adesso.iotgateway.services.ParkingIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final DNSService dnsService;
    private final ParkingIdService parkingIdService;

    @Autowired
    public ViewController(DNSService dnsService, ParkingIdService parkingIdService) {
        this.dnsService = dnsService;
        this.parkingIdService = parkingIdService;
    }

    @GetMapping("")
    public String showMainPage(Model model) {
        model.addAttribute("placeholder", dnsService.getGatewayIp().isEmpty()
                ? "x.x.x.x"
                : dnsService.getGatewayIp());
        model.addAttribute("waiting", parkingIdService.getWaitingPicos());
        return "index";
    }

    @PostMapping("/setGateway")
    public String setGatewayIp(@RequestParam String ip, Model model) {
        if(!dnsService.setGatewayIp(ip)) {
            model.addAttribute("invalidIp", true);
        }
        model.addAttribute("placeholder", dnsService.getGatewayIp().isEmpty()
                ? "x.x.x.x"
                : dnsService.getGatewayIp());
        return "index";
    }

    @PostMapping("/setParkingId")
    public String setParkingId(@RequestParam String ip, @RequestParam int id, Model model) {
        if(!parkingIdService.setParkingId(ip, id)) {
            model.addAttribute("invalidParkingId", ip);
        }
        model.addAttribute("waiting", parkingIdService.getWaitingPicos());
        model.addAttribute("placeholder", dnsService.getGatewayIp().isEmpty()
                ? "x.x.x.x"
                : dnsService.getGatewayIp());
        return "index";
    }

}
