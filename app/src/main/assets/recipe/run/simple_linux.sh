#!/bin/bash

. /opt/recipe/util/progress.sh

if [ "$(locale -a | grep $LC_ALL)" != "$LC_ALL" ]; then
    progress "-1" "Generating locale..."
    locale-gen --no-archive $LC_ALL
fi

export LINUX_LOG=/home/out.txt

echo "----- Exagear Desktop -----" >> $LINUX_LOG

# FIXME: No sudo command!!!

progress "-1" "Setting permission..."
sudo chmod 700 /bin
sudo chmod 700 /usr/bin
sudo chmod 700 /usr/sbin

progress "-1" "Updating packages..."
apt-get update >> $LINUX_LOG

progress "-1" "Installing required packages..."
apt-get --yes --force-yes install dropbear xterm >> $LINUX_LOG

progress "-1" "Launching application..."
xterm >> $LINUX_LOG

// eval "$@"
