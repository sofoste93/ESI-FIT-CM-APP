param(
    [ValidateSet("app-image", "exe", "msi")]
    [string]$PackageType = "app-image"
)

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location $ProjectRoot

& .\mvnw.cmd -B -DskipTests package
if ($LASTEXITCODE -ne 0) { throw "Maven package failed." }

$arguments = @(
    "--type", $PackageType,
    "--input", "target\package-input",
    "--main-jar", "esi-fit.jar",
    "--main-class", "com.esifit.Launcher",
    "--name", "ESI-FIT",
    "--dest", "target\dist",
    "--app-version", "2.0.0",
    "--vendor", "Enrico, Islam and Stephane",
    "--description", "Private fitness club member and attendance manager",
    "--copyright", "Copyright 2026 ESI-FIT team",
    "--java-options", "-Dfile.encoding=UTF-8",
    "--icon", "src\main\resources\com\esifit\app.ico"
)

if ($PackageType -in @("exe", "msi")) {
    $arguments += @("--win-menu", "--win-shortcut", "--win-dir-chooser", "--win-menu-group", "ESI-FIT")
}

New-Item -ItemType Directory -Path "target\dist" -Force | Out-Null
& jpackage @arguments
if ($LASTEXITCODE -ne 0) { throw "jpackage failed." }
