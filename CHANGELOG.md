# Changelog

## [2.0.0] - 2026-09-30

### Added

- New fitness club mission-control dashboard
- Searchable member directory with membership plans and status controls
- One-click check-in and check-out with duplicate-session protection
- Live occupancy, daily attendance, and average-duration metrics
- Attendance history with CSV export
- Embedded H2 database in the operating system's application-data folder
- Automatic migration from the original text-file storage
- Native installers and portable applications with bundled Java runtimes
- Windows, Linux, macOS Intel, and Apple Silicon release builds
- Automated database tests and packaged-runtime diagnostics

### Changed

- Rebuilt the UI as a responsive JavaFX desktop application
- Updated the project to a maintained Java 17 LTS and JavaFX 17 toolchain
- Replaced fragile working-directory text storage with transactional persistence
- Reworked repository hygiene, documentation, branding, and release automation

### Removed

- Requirement for users to install and configure a separate JRE
- Checked-in member records, session history, build output, and IDE metadata
- Fixed full-screen startup and the legacy multi-FXML navigation stack
