
package com.interview.quiz.signaling;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    /**
     * Endpoint for SDP offer from a client.
     * Broadcasts the received offer to all subscribers of /topic/offer.
     */
    @MessageMapping("/offer")
    @SendTo("/topic/offer")
    public String offer(String sdpOffer) {
        return sdpOffer;
    }

    /**
     * Endpoint for SDP answer from a client.
     * Broadcasts the received answer to all subscribers of /topic/answer.
     */
    @MessageMapping("/answer")
    @SendTo("/topic/answer")
    public String answer(String sdpAnswer) {
        return sdpAnswer;
    }

    /**
     * Endpoint for ICE candidates from a client.
     * Broadcasts the received ICE candidate to all subscribers of /topic/ice.
     */
    @MessageMapping("/ice")
    @SendTo("/topic/ice")
    public String ice(String iceCandidate) {
        return iceCandidate;
    }
    @MessageMapping("/offer/{roomId}")
    @SendTo("/topic/offer/{roomId}")
    public String offer(@DestinationVariable String roomId, String sdpOffer) {
        return sdpOffer;
    }

    @MessageMapping("/answer/{roomId}")
    @SendTo("/topic/answer/{roomId}")
    public String answer(@DestinationVariable String roomId, String sdpAnswer) {
        return sdpAnswer;
    }

    @MessageMapping("/ice/{roomId}")
    @SendTo("/topic/ice/{roomId}")
    public String ice(@DestinationVariable String roomId, String iceCandidate) {
        return iceCandidate;
    }

}
