#!/bin/bash

. /opt/recipe/util/progress.sh

progress "-1" "Launching application..."
eval "$@" -w
