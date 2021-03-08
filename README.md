# TIGER-WEBSERVER
A simple web server in java.
##Attetion
Please update the `tiger.config` file before using the server.

`tiger.version=0.01 Alpha` The version of the server. You don't have to change this. \
`tiger.port=80` Port of the application. Choose a free one as 80 or 8080. \
`tiger.host=localhost` Host of the application. Needed for an correct error page. \
`tiger.folder=C:/web` Folder of the "web root". Choose a folder in where you want to put your HTML pages. \
`tiger.threads=2` Threads handling the connections. Choose a number working with your CPU. \
`tiger.log=0` Log level. 0 -> Info; 1 -> Warning; 2 -> Severe

To run the application execute this command in the source folder: `java org.benjaminschmitz.tiger.Run` after compiling the code.
