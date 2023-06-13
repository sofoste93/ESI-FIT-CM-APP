package tls.sofoste.esifitapp.service;


import tls.sofoste.esifitapp.model.Session;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionService {
    private final Map<String, List<Session>> clientSessions = new HashMap<>();

    public SessionService() {
        loadSessionData();
    }

    public Session startSession(String clientId) {
        LocalDateTime now = LocalDateTime.now();
        Session newSession = new Session(clientId, now, null);
        clientSessions.computeIfAbsent(clientId, k -> new ArrayList<>()).add(newSession);

        saveSessionData();

        return newSession;
    }

    public Session endSession(String clientId) {
        List<Session> sessions = clientSessions.get(clientId);
        if (sessions != null && !sessions.isEmpty()) {
            Session lastSession = sessions.get(sessions.size() - 1);
            lastSession.setLogoutTime(LocalDateTime.now());

            saveSessionData();

            return lastSession;
        }
        return null;
    }

    public List<Session> getClientSessions(String clientId) {
        return clientSessions.get(clientId);
    }

    public void deleteClientSessions(String clientId) {
        clientSessions.remove(clientId);

        saveSessionData();
    }

    public boolean updateSession(String clientId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Session> sessions = clientSessions.get(clientId);
        if (sessions == null || sessions.isEmpty()) {
            // No sessions found for the given client ID.
            return false;
        }

        Session lastSession = sessions.get(sessions.size() - 1);
        lastSession.setLoginTime(startTime);
        lastSession.setLogoutTime(endTime);

        saveSessionData();

        // Session update was successful.
        return true;
    }

    // Auto-save session data to file
    private void saveSessionData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sessions.txt"))) {
            for (List<Session> sessions : clientSessions.values()) {
                for (Session session : sessions) {
                    writer.write(session.getClientId() + " | " + session.getLoginTime() + " | " + session.getLogoutTime());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load session data from file
    private void loadSessionData() {
        File file = new File("sessions.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String clientId = parts[0].trim();
                LocalDateTime loginTime = LocalDateTime.parse(parts[1].trim());
                LocalDateTime logoutTime = parts[2].trim().equals("null") ? null : LocalDateTime.parse(parts[2].trim());
                Session session = new Session(clientId, loginTime, logoutTime);
                clientSessions.computeIfAbsent(clientId, k -> new ArrayList<>()).add(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

