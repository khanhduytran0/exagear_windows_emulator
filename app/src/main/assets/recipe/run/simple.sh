#!/bin/bash

. /opt/recipe/util/progress.sh

if [ "$(locale -a | grep $LC_ALL)" != "$LC_ALL" ]; then
    progress "-1" "Generating locale..."
    locale-gen --no-archive $LC_ALL
fi

progress "-1" "Launching application..."
eval "$@"
