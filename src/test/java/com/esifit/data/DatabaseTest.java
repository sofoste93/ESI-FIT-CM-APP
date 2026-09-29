package com.esifit.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database() {
        return new Database("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
    }

    @Test
    void memberLifecycleAndSearchWork() {
        var database = database();
        var member = database.addMember("Enrico", "Strong", "enrico@example.com", "Unlimited");

        assertTrue(member.id().startsWith("EF-"));
        assertEquals(1, database.members("enrico").size());
        assertEquals("Unlimited", database.member(member.id()).plan());

        database.setMemberActive(member.id(), false);
        assertFalse(database.member(member.id()).active());
        database.deleteMember(member.id());
        assertEquals(0, database.memberCount());
    }

    @Test
    void attendancePreventsDuplicateOpenSessions() {
        var database = database();
        var member = database.addMember("Islam", "Runner", "", "Core");

        assertTrue(database.checkIn(member.id()));
        assertFalse(database.checkIn(member.id()));
        assertTrue(database.isCheckedIn(member.id()));
        assertEquals(1, database.metrics().checkedIn());
        assertEquals(1, database.metrics().visitsToday());

        assertTrue(database.checkOut(member.id()));
        assertFalse(database.checkOut(member.id()));
        assertFalse(database.isCheckedIn(member.id()));
        assertNotNull(database.visits(10).get(0).checkOut());
    }

    @Test
    void legacyFilesAreImportedOnce(@TempDir Path directory) throws Exception {
        Files.writeString(directory.resolve("clients.txt"), "A-01 | Stephane | Sob\nB-02 | Enrico | Dück\nmalformed\n");
        Files.writeString(directory.resolve("sessions.txt"), "A-01 | 2026-09-29T08:00:00 | 2026-09-29T09:15:00\n");
        var database = database();

        assertEquals(2, database.importLegacy(directory));
        assertEquals(2, database.memberCount());
        assertEquals(1, database.visits(10).size());
        assertEquals(0, database.importLegacy(directory));
    }
}
