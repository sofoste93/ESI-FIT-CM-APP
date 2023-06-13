package tls.sofoste.esifitapp.controller;

import tls.sofoste.esifitapp.model.Session;
import tls.sofoste.esifitapp.service.SessionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SessionController {
    private final SessionService sessionService = new SessionService();

    public Session startSession(String clientId) {
        return sessionService.startSession(clientId);
    }

    public Session endSession(String clientId) {
        sessionService.endSession(clientId);
        return null;
    }


    public List<Session> getClientSessions(String clientId) {
        return sessionService.getClientSessions(clientId);
    }

    public boolean updateSession(String clientId, String startTime, String endTime) {
        try {
            boolean updateSuccessful = sessionService.updateSession(clientId, LocalDateTime.parse(startTime,
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            return updateSuccessful;
        } catch (Exception e) {
            return false;
        }
    }
    public void deleteClientSessions(String clientId) {
        sessionService.deleteClientSessions(clientId);
    }
}