#!/usr/bin/env bash
mvn clean install
heroku deploy:jar ./backend/target/transactrules-app.jar