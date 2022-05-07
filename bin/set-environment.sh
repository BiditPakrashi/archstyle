#!/usr/bin/env bash

PASS=$(< /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c20)
echo "DB_PASS=${PASS}" > .env
echo "DB_USER=trashbet" >> .env
