package com.esifit;

import com.esifit.data.Database;
import com.esifit.data.Metrics;
import com.esifit.model.Member;
import com.esifit.model.Visit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class App extends Application {
    private static final String VERSION = "2.0.0";
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm", Locale.ENGLISH);

    private Database database;
    private BorderPane shell;
    private VBox content;
    private Label pageTitle;
    private Label pageSubtitle;
    private Label clock;
    private Label status;
    private Button activeNavigation;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        database = new Database(dataDirectory());
        int imported = database.importLegacy(Path.of("").toAbsolutePath());

        shell = new BorderPane();
        shell.getStyleClass().add("app-shell");
        shell.setLeft(createSidebar());
        shell.setTop(createTopbar());
        content = new VBox();
        content.getStyleClass().add("content");
        shell.setCenter(content);
        shell.setBottom(createStatusbar());

        Scene scene = new Scene(shell, 1280, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/esifit/theme.css")).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ESI-FIT · Club Mission Control");
        stage.setMinWidth(1020);
        stage.setMinHeight(680);
        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/esifit/icon.png"))));
        } catch (Exception ignored) { }
        stage.show();
        showDashboard();
        if (imported > 0) setStatus("Legacy archive recovered · " + imported + " members imported", "success");
    }

    private VBox createSidebar() {
        var logoMark = new Label("EF");
        logoMark.getStyleClass().add("logo-mark");
        var logoText = new VBox(label("ESI-FIT", "logo-title"), label("CLUB MANAGER", "logo-caption"));
        logoText.setSpacing(1);
        var logo = new HBox(12, logoMark, logoText);
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.getStyleClass().add("logo");

        var navigationLabel = label("MISSION DECK", "section-label");
        var dashboard = navigationButton("⌁", "Overview", this::showDashboard);
        var members = navigationButton("◉", "Members", this::showMembers);
        var attendance = navigationButton("↗", "Attendance", this::showAttendance);
        var about = navigationButton("?", "Help & about", this::showAbout);
        activeNavigation = dashboard;
        dashboard.getStyleClass().add("active");

        var spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        var signal = new VBox(label("●  SYSTEM ONLINE", "online"), label("LOCAL DATABASE", "sidebar-meta"),
                label("v" + VERSION + " · 2026", "sidebar-meta"));
        signal.setSpacing(7);
        signal.getStyleClass().add("system-card");

        var sidebar = new VBox(logo, navigationLabel, dashboard, members, attendance, about, spacer, signal);
        sidebar.getStyleClass().add("sidebar");
        return sidebar;
    }

    private HBox createTopbar() {
        pageTitle = label("Overview", "page-title");
        pageSubtitle = label("Club activity at a glance", "page-subtitle");
        var titles = new VBox(2, pageTitle, pageSubtitle);
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        clock = label("", "clock");
        updateClock();
        var clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
        var profile = new HBox(10, label("E·I·S", "profile-avatar"),
                new VBox(label("Mission Control", "profile-title"), label("Enrico · Islam · Stephane", "profile-subtitle")));
        profile.setAlignment(Pos.CENTER_LEFT);
        var topbar = new HBox(28, titles, spacer, clock, profile);
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.getStyleClass().add("topbar");
        return topbar;
    }

    private HBox createStatusbar() {
        status = label("Ready for the next set.", "status-text");
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var privacy = label("PRIVATE · LOCAL · OFFLINE", "privacy");
        var bar = new HBox(status, spacer, privacy);
        bar.getStyleClass().add("statusbar");
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    private void showDashboard() {
        setPage("Overview", "Club activity at a glance");
        content.getChildren().setAll(createHero(), createMetrics(), dashboardBody());
    }

    private Node createHero() {
        var eyebrow = label("TODAY'S TRAINING ORBIT", "eyebrow");
        var headline = label("Stronger people.\nSmarter club.", "hero-title");
        var copy = label("Track every member and every visit from one private, focused command center.", "hero-copy");
        copy.setWrapText(true);
        var text = new VBox(10, eyebrow, headline, copy);
        text.setMaxWidth(620);
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var action = primaryButton("+  Add member", this::openAddMember);
        var hero = new HBox(text, spacer, action);
        hero.setAlignment(Pos.BOTTOM_LEFT);
        hero.getStyleClass().add("hero");
        return hero;
    }

    private HBox createMetrics() {
        Metrics metrics = database.metrics();
        var row = new HBox(14,
                metric("ACTIVE MEMBERS", Integer.toString(metrics.activeMembers()), "↗", "lime"),
                metric("IN THE CLUB", Integer.toString(metrics.checkedIn()), "●", "cyan"),
                metric("VISITS TODAY", Integer.toString(metrics.visitsToday()), "+", "orange"),
                metric("AVG. SESSION", formatMinutes(metrics.averageMinutes()), "◷", "violet"));
        for (Node node : row.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);
        return row;
    }

    private VBox metric(String title, String value, String symbol, String color) {
        var icon = label(symbol, "metric-icon");
        icon.getStyleClass().add(color);
        var header = new HBox(label(title, "metric-label"), new Region(), icon);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        var card = new VBox(14, header, label(value, "metric-value"));
        card.getStyleClass().add("metric-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private HBox dashboardBody() {
        var quick = createQuickAccess();
        var activity = createRecentActivity(6);
        HBox.setHgrow(quick, Priority.ALWAYS);
        HBox.setHgrow(activity, Priority.ALWAYS);
        quick.setMaxWidth(Double.MAX_VALUE);
        activity.setMaxWidth(Double.MAX_VALUE);
        return new HBox(14, quick, activity);
    }

    private VBox createQuickAccess() {
        var heading = panelHeading("Quick access", "Check members in or out");
        var memberBox = new ComboBox<Member>(FXCollections.observableArrayList(database.members("")));
        memberBox.setPromptText("Select a member…");
        memberBox.setMaxWidth(Double.MAX_VALUE);
        memberBox.setConverter(new StringConverter<>() {
            @Override public String toString(Member member) { return member == null ? "" : member.fullName() + "  ·  " + member.id(); }
            @Override public Member fromString(String value) { return null; }
        });
        var checkIn = primaryButton("CHECK IN  ↗", () -> {
            Member member = memberBox.getValue();
            if (member == null) { setStatus("Select a member first.", "warning"); return; }
            if (!member.active()) { setStatus("This membership is paused.", "warning"); return; }
            setStatus(database.checkIn(member.id()) ? member.fullName() + " checked in." : member.fullName() + " is already inside.", "success");
            showDashboard();
        });
        var checkOut = secondaryButton("CHECK OUT", () -> {
            Member member = memberBox.getValue();
            if (member == null) { setStatus("Select a member first.", "warning"); return; }
            setStatus(database.checkOut(member.id()) ? member.fullName() + " checked out." : "No open visit found for " + member.fullName() + ".", "success");
            showDashboard();
        });
        checkIn.setMaxWidth(Double.MAX_VALUE);
        checkOut.setMaxWidth(Double.MAX_VALUE);
        var buttons = new HBox(10, checkIn, checkOut);
        HBox.setHgrow(checkIn, Priority.ALWAYS);
        HBox.setHgrow(checkOut, Priority.ALWAYS);
        var hint = label("Tip: use the Members view to search by name or ID.", "panel-hint");
        var panel = new VBox(20, heading, memberBox, buttons, hint);
        panel.getStyleClass().add("panel");
        return panel;
    }

    private VBox createRecentActivity(int limit) {
        var panel = new VBox(14);
        panel.getStyleClass().add("panel");
        panel.getChildren().add(panelHeading("Recent activity", "Latest club movements"));
        List<Visit> visits = database.visits(limit);
        if (visits.isEmpty()) panel.getChildren().add(emptyState("No visits yet", "Check a member in to start the timeline."));
        else for (Visit visit : visits) panel.getChildren().add(activityRow(visit));
        return panel;
    }

    private HBox activityRow(Visit visit) {
        var avatar = label(initials(visit.memberName()), "member-avatar");
        var name = label(visit.memberName(), "activity-name");
        var description = label(visit.open() ? "Training now" : "Session · " + formatDuration(visit), "activity-detail");
        var copy = new VBox(2, name, description);
        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var time = label(visit.checkIn().format(TIME), "activity-time");
        var row = new HBox(12, avatar, copy, spacer, time);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("activity-row");
        return row;
    }

    private void showMembers() {
        setPage("Members", "Manage memberships and access");
        var search = new TextField();
        search.setPromptText("Search name, ID, or email…");
        search.getStyleClass().add("search-field");
        var add = primaryButton("+  Add member", this::openAddMember);
        var toolbar = new HBox(12, search, new Region(), add);
        HBox.setHgrow(search, Priority.ALWAYS);
        HBox.setHgrow(toolbar.getChildren().get(1), Priority.ALWAYS);

        var table = memberTable(database.members(""));
        search.textProperty().addListener((observable, oldValue, newValue) ->
                table.setItems(FXCollections.observableArrayList(database.members(newValue))));

        var checkIn = primaryButton("Check in", () -> memberAction(table, true));
        var checkOut = secondaryButton("Check out", () -> memberAction(table, false));
        var toggle = secondaryButton("Pause / resume", () -> {
            Member member = selected(table);
            if (member == null) return;
            database.setMemberActive(member.id(), !member.active());
            setStatus(member.fullName() + (member.active() ? " paused." : " resumed."), "success");
            showMembers();
        });
        var delete = dangerButton("Delete", () -> deleteMember(table));
        var actions = new HBox(10, checkIn, checkOut, toggle, new Region(), delete);
        HBox.setHgrow(actions.getChildren().get(3), Priority.ALWAYS);
        var tablePanel = new VBox(14, toolbar, table, actions);
        tablePanel.getStyleClass().add("table-panel");
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().setAll(tablePanel);
        VBox.setVgrow(tablePanel, Priority.ALWAYS);
    }

    private TableView<Member> memberTable(List<Member> members) {
        var table = new TableView<Member>(FXCollections.observableArrayList(members));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(emptyState("No members found", "Add a member or adjust your search."));
        table.getColumns().add(textColumn("ID", Member::id, 110));
        table.getColumns().add(textColumn("MEMBER", Member::fullName, 190));
        table.getColumns().add(textColumn("EMAIL", Member::email, 210));
        table.getColumns().add(textColumn("PLAN", Member::plan, 100));
        table.getColumns().add(textColumn("JOINED", member -> member.joinedOn().toString(), 110));
        table.getColumns().add(textColumn("STATUS", member -> database.isCheckedIn(member.id()) ? "IN CLUB" : member.active() ? "ACTIVE" : "PAUSED", 100));
        return table;
    }

    private void showAttendance() {
        setPage("Attendance", "Every visit, duration, and active session");
        var export = secondaryButton("Export CSV", this::exportVisits);
        var heading = new HBox(panelHeading("Session archive", "Most recent 500 visits"), new Region(), export);
        HBox.setHgrow(heading.getChildren().get(1), Priority.ALWAYS);
        heading.setAlignment(Pos.CENTER_LEFT);
        var table = new TableView<Visit>(FXCollections.observableArrayList(database.visits(500)));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(emptyState("No sessions recorded", "Attendance will appear here after the first check-in."));
        table.getColumns().add(textColumn("MEMBER", Visit::memberName, 210));
        table.getColumns().add(textColumn("MEMBER ID", Visit::memberId, 120));
        table.getColumns().add(textColumn("CHECK IN", visit -> visit.checkIn().format(DATE_TIME), 190));
        table.getColumns().add(textColumn("CHECK OUT", visit -> visit.open() ? "IN PROGRESS" : visit.checkOut().format(DATE_TIME), 190));
        table.getColumns().add(textColumn("DURATION", this::formatDuration, 110));
        var panel = new VBox(18, heading, table);
        panel.getStyleClass().add("table-panel");
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().setAll(panel);
        VBox.setVgrow(panel, Priority.ALWAYS);
    }

    private void showAbout() {
        setPage("Help & about", "Everything your crew needs");
        var intro = new VBox(10, label("ESI-FIT  /  MISSION 02", "eyebrow"),
                label("Built for stronger\nclub operations.", "about-title"),
                label("A private fitness club manager rebuilt for Enrico, Islam, and Stephane.", "hero-copy"));
        intro.getStyleClass().add("about-hero");
        var cards = new HBox(14,
                helpCard("01", "Members", "Create searchable member profiles, manage plans, and pause access without losing history."),
                helpCard("02", "Attendance", "Check members in and out, follow active sessions, and export the visit archive."),
                helpCard("03", "Private data", "The embedded database stays in your operating system's application-data folder."));
        for (Node node : cards.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);
        var thor = new HBox(label("THOR", "thor-label"), label("Transmission complete. Training orbit stable.", "thor-copy"));
        thor.getStyleClass().add("thor");
        content.getChildren().setAll(intro, cards, thor);
    }

    private VBox helpCard(String number, String title, String copy) {
        var body = label(copy, "help-copy");
        body.setWrapText(true);
        var card = new VBox(20, label(number, "help-number"), label(title, "help-title"), body);
        card.getStyleClass().add("help-card");
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private void openAddMember() {
        var dialog = new Dialog<Member>();
        dialog.setTitle("Add a member");
        dialog.setHeaderText("NEW MEMBER  /  ESI-FIT");
        dialog.initOwner(stage);
        var addType = new ButtonType("Add member", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/esifit/theme.css")).toExternalForm());

        var first = new TextField(); first.setPromptText("First name");
        var last = new TextField(); last.setPromptText("Last name");
        var email = new TextField(); email.setPromptText("name@example.com");
        var plan = new ComboBox<String>(FXCollections.observableArrayList("Flex", "Core", "Unlimited", "Student"));
        plan.setValue("Flex"); plan.setMaxWidth(Double.MAX_VALUE);
        var form = new GridPane(); form.setHgap(14); form.setVgap(14);
        form.addRow(0, label("FIRST NAME", "form-label"), first);
        form.addRow(1, label("LAST NAME", "form-label"), last);
        form.addRow(2, label("EMAIL", "form-label"), email);
        form.addRow(3, label("PLAN", "form-label"), plan);
        GridPane.setHgrow(first, Priority.ALWAYS); GridPane.setHgrow(last, Priority.ALWAYS); GridPane.setHgrow(email, Priority.ALWAYS);
        dialog.getDialogPane().setContent(form);
        Node submit = dialog.getDialogPane().lookupButton(addType);
        submit.disableProperty().bind(first.textProperty().isEmpty().or(last.textProperty().isEmpty()));
        dialog.setResultConverter(button -> button == addType ? database.addMember(first.getText(), last.getText(), email.getText(), plan.getValue()) : null);
        dialog.showAndWait().ifPresent(member -> {
            setStatus(member.fullName() + " joined ESI-FIT · " + member.id(), "success");
            showMembers();
        });
    }

    private void memberAction(TableView<Member> table, boolean entering) {
        Member member = selected(table);
        if (member == null) return;
        if (entering && !member.active()) { setStatus("Resume this membership before check-in.", "warning"); return; }
        boolean changed = entering ? database.checkIn(member.id()) : database.checkOut(member.id());
        setStatus(changed ? member.fullName() + (entering ? " checked in." : " checked out.") :
                member.fullName() + (entering ? " is already inside." : " has no active session."), changed ? "success" : "warning");
        showMembers();
    }

    private void deleteMember(TableView<Member> table) {
        Member member = selected(table);
        if (member == null) return;
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + member.fullName() + " and all attendance history?", ButtonType.CANCEL, ButtonType.OK);
        alert.setHeaderText("This action cannot be undone");
        alert.initOwner(stage);
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            database.deleteMember(member.id());
            setStatus(member.fullName() + " removed from the club.", "warning");
            showMembers();
        }
    }

    private Member selected(TableView<Member> table) {
        Member member = table.getSelectionModel().getSelectedItem();
        if (member == null) setStatus("Select a member from the table first.", "warning");
        return member;
    }

    private void exportVisits() {
        var chooser = new FileChooser();
        chooser.setTitle("Export attendance");
        chooser.setInitialFileName("esi-fit-attendance.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        var file = chooser.showSaveDialog(stage);
        if (file == null) return;
        var lines = new StringBuilder("member_id,member_name,check_in,check_out,duration_minutes\n");
        for (Visit visit : database.visits(100_000)) {
            lines.append(csv(visit.memberId())).append(',').append(csv(visit.memberName())).append(',')
                    .append(csv(visit.checkIn().toString())).append(',').append(csv(visit.checkOut() == null ? "" : visit.checkOut().toString())).append(',')
                    .append(visit.duration().toMinutes()).append('\n');
        }
        try {
            Files.writeString(file.toPath(), lines.toString(), StandardCharsets.UTF_8);
            setStatus("Attendance exported to " + file.getName(), "success");
        } catch (IOException error) {
            setStatus("Could not export the attendance file.", "warning");
        }
    }

    private Button navigationButton(String icon, String text, Runnable action) {
        var button = new Button(icon + "   " + text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> {
            if (activeNavigation != null) activeNavigation.getStyleClass().remove("active");
            button.getStyleClass().add("active");
            activeNavigation = button;
            action.run();
        });
        return button;
    }

    private Button primaryButton(String text, Runnable action) { return actionButton(text, "primary-button", action); }
    private Button secondaryButton(String text, Runnable action) { return actionButton(text, "secondary-button", action); }
    private Button dangerButton(String text, Runnable action) { return actionButton(text, "danger-button", action); }
    private Button actionButton(String text, String style, Runnable action) {
        var button = new Button(text); button.getStyleClass().add(style); button.setOnAction(event -> action.run()); return button;
    }

    private VBox panelHeading(String title, String subtitle) {
        return new VBox(3, label(title, "panel-title"), label(subtitle, "panel-subtitle"));
    }

    private VBox emptyState(String title, String subtitle) {
        var state = new VBox(7, label("⌁", "empty-icon"), label(title, "empty-title"), label(subtitle, "empty-copy"));
        state.setAlignment(Pos.CENTER); state.getStyleClass().add("empty-state"); return state;
    }

    private <T> TableColumn<T, String> textColumn(String title, java.util.function.Function<T, String> mapper, double width) {
        var column = new TableColumn<T, String>(title);
        column.setCellValueFactory(data -> new ReadOnlyStringWrapper(mapper.apply(data.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    private static Label label(String text, String style) {
        var label = new Label(text); label.getStyleClass().add(style); return label;
    }

    private void setPage(String title, String subtitle) {
        pageTitle.setText(title); pageSubtitle.setText(subtitle); content.getChildren().clear();
    }

    private void setStatus(String message, String type) {
        status.setText(message);
        status.getStyleClass().removeAll("success", "warning");
        status.getStyleClass().add(type);
    }

    private void updateClock() { clock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE · dd MMM\nHH:mm:ss", Locale.ENGLISH)).toUpperCase(Locale.ROOT)); }
    private static String formatMinutes(long minutes) { return minutes <= 0 ? "—" : minutes >= 60 ? (minutes / 60) + "h " + (minutes % 60) + "m" : minutes + "m"; }
    private String formatDuration(Visit visit) { return visit.open() ? "LIVE · " + formatMinutes(visit.duration().toMinutes()) : formatMinutes(visit.duration().toMinutes()); }
    private static String initials(String name) { String[] p = name.trim().split("\\s+"); return (p[0].substring(0, 1) + (p.length > 1 ? p[p.length - 1].substring(0, 1) : "")).toUpperCase(); }
    private static String csv(String value) { return "\"" + value.replace("\"", "\"\"") + "\""; }

    private static Path dataDirectory() {
        String home = System.getProperty("user.home");
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win")) {
            String local = System.getenv("LOCALAPPDATA");
            return Path.of(local == null ? home : local, "ESI-FIT");
        }
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac"))
            return Path.of(home, "Library", "Application Support", "ESI-FIT");
        String xdg = System.getenv("XDG_DATA_HOME");
        return xdg == null || xdg.isBlank() ? Path.of(home, ".local", "share", "esi-fit") : Path.of(xdg, "esi-fit");
    }

    public static void main(String[] args) {
        if (List.of(args).contains("--diagnostics")) {
            var probe = new Database("jdbc:h2:mem:diagnostics;DB_CLOSE_DELAY=-1");
            var member = probe.addMember("Thor", "Telemetry", "", "Core");
            probe.checkIn(member.id());
            if (probe.metrics().checkedIn() != 1) throw new IllegalStateException("Attendance diagnostic failed.");
            System.out.println("ESI-FIT " + VERSION + " · systems nominal · local database ready");
            return;
        }
        launch(args);
    }
}
