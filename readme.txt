========== Calamari-Service ==========

This application provides a restful web service to the Calamari-UI front-end for Squid log file analysis. Users can build a war file using the maven build tool. "maven -Dmaven.test.skip=true package" This will create a Calamari-1.0.war file in the target directory. The file can be deployed to any Java web server such as JBoss, Tomcat or Glassfish. For the back end to work properly the following needs to be configured

1) Create a directory /var/lib/database to which the user the Java application server runs as has write and read permissions. This is where the calamari database will be created.

2) Obtain the location of the squid log files and make sure the app server has read permissions to this folder. You can simply copy the access.log files to a folder that you create for this purpose or an nfs share. If the files are compressed the application will untar/unzip before loading. You will specificy the folder location in the UI front-end to import the log files.

3)You will also need the url for the server. This will be set in the UI as well. eg. http://127.0.0.1:8080. The full url you will need for the front-end includes the path /Calamari-1.0/resources/ i.e http://127.0.0.1:8080/Calamari-1.0/resources/
