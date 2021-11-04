@echo off
call mvn clean package
call docker build -t ohashi/GlobalSession .
call docker rm -f GlobalSession
call docker run -d -p 9080:9080 -p 9443:9443 --name GlobalSession ohashi/GlobalSession