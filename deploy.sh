#!/bin/bash
sudo service tomcat7 stop
sudo rm -rf /var/lib/tomcat7/work/* /var/lib/tomcat7/webapps/workflow-*
sudo cp -r web/workflow-ui/target/workflow-ui-0.6.0-SNAPSHOT.war /var/lib/tomcat7/webapps/workflow-ui.war
sudo cp -r web-services/workflow-web-service/target/workflow-web-service-0.6.0-SNAPSHOT.war /var/lib/tomcat7/webapps/workflow-web-service.war
sudo service tomcat7 start
