========== Calamari-Service ==========

This application provides a restful web service to the Calamari-UI front-end for Squid log file analysis. You can build a deployable war file using the maven build tool. 

"maven -Dmaven.test.skip=true package" 

This will create a Calamari-1.0.war file in the "target" directory. The command above skips the unit tests. The file can be deployed to any java web server such as JBoss, Tomcat or Glassfish. For the back end to work properly you will need to configure the following:

1) Create a directory /var/lib/database to which the user the java application server runs as, has write and read permissions. This is where the calamari database will be created hence the need for read/write access.

2) Find the location of your squid access.log files. This is usually "/var/log/squid". Make sure the app server has read permissions to this folder. You can simply copy the access.log files to another folder that you create for this purpose, if getting read access is an issue. You can also mount the folder remotely over an nfs share. If the log files have been compressed by logrotate the application will untar/unzip before loading. You will need to specificy the folder location in the UI front-end to import the log files. The application will attempt to load any file that has access in the name, so please ensure that the default squid log name has not been changed and that when they are compressed by logrotate they name still contains the word "access"

3)Note down the url for the server. This will be set in the UI as well so the client can gain access to the necessary services. eg. http://127.0.0.1:8080. The full url you will need for the front-end includes the path /Calamari-1.0/resources/ i.e http://127.0.0.1:8080/Calamari-1.0/resources/

Please visit http://www.calamariproject.co.za for more info. This project is released under the GNU Open Source License. See gnu-lic.txt
