#!/usr/bin/env bash
set -euo pipefail

PACKAGE_TYPE="${1:-app-image}"
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

./mvnw -B -DskipTests package
mkdir -p target/dist

ARGS=(
  --type "$PACKAGE_TYPE"
  --input target/package-input
  --main-jar esi-fit.jar
  --main-class com.esifit.Launcher
  --name ESI-FIT
  --dest target/dist
  --app-version 2.0.0
  --vendor "Enrico, Islam and Stephane"
  --description "Private fitness club member and attendance manager"
  --copyright "Copyright 2026 ESI-FIT team"
  --java-options "-Dfile.encoding=UTF-8"
)

if [[ "$OSTYPE" == darwin* ]]; then
  ICONSET="target/ESI-FIT.iconset"
  rm -rf "$ICONSET"
  mkdir -p "$ICONSET"
  for size in 16 32 128 256 512; do
    sips -z "$size" "$size" src/main/resources/com/esifit/icon.png --out "$ICONSET/icon_${size}x${size}.png" >/dev/null
    double=$((size * 2))
    sips -z "$double" "$double" src/main/resources/com/esifit/icon.png --out "$ICONSET/icon_${size}x${size}@2x.png" >/dev/null
  done
  iconutil -c icns "$ICONSET" -o target/app.icns
  ARGS+=(--icon target/app.icns)
  [[ "$PACKAGE_TYPE" == "dmg" ]] && ARGS+=(--mac-package-name "ESI-FIT")
else
  ARGS+=(--icon src/main/resources/com/esifit/icon.png)
  [[ "$PACKAGE_TYPE" == "deb" ]] && ARGS+=(--linux-shortcut --linux-menu-group "Utility" --linux-app-category "Office")
fi

jpackage "${ARGS[@]}"
