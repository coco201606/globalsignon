#!/bin/sh
mvn clean package && docker build -t ohashi/GlobalSession .
docker rm -f GlobalSession || true && docker run -d -p 9080:9080 -p 9443:9443 --name GlobalSession ohashi/GlobalSession