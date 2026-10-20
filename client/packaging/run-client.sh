#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"

if [ ! -f config.json ]; then
    cp config.example.json config.json
    echo "Created config.json from config.example.json. Edit it before connecting."
fi

exec java -jar 2009scape-client.jar
