# VerifOO
##### Verification and Optimization Orchestrator (VerifOO) component for joint optimization and verification
The component exploits the z3Opt engine for solving MaxSAT instance of VNE problem. VerifOO has been designed to work closely with an NFV orchestrator to act as a policy verification, refinement and deployment manager. The interaction with VerifOO is possible through the REST APIs.

How to deploy Verigraph on Apache Tomcat:
**Windows**
-  install jdk1.8.X YY(http://www.oracle.comntechnetwork/java/javase/downloads/jdk8-downloads-2133151.html);
-  set JAVA HOME environment variable to where you installed the jdk
   (e.g.   ``C:\Program Files\Java\jdk1.8.XYY``);
-  install Apache Tomcat 8 (https://tomcat.apache.org/download-80.cgi);
	-  set CATALINA HOME ambient variable to the directory where you  installed Apache (e.g.   ``C:\Program Files\Java\apache-tomcat-8.0.30``);
	-  (optional) configure Tomcat Manager:
	-  open the file ``%CATALINA_HOME%\conf\tomcat-users.xml``
	-  under the ``tomcat-users`` tag place, initialize an user with roles  "tomcat, manager-gui, manager-script".  An example is the following  content:
   ``xml   <role rolename="manager-gui"/>  <role rolename="manager-script"/>  <role rolename="admin-gui"/>   <role rolename="admin-script"/>  <user username="admin" password="admin" roles="manager-gui,manager-script,admin-scripts"/>``
	-  edit the "to\_be\_defined" fields in tomcat-build.xml with the before
   defined credentials;
-  execute the generate-war ant task in order to generate the .war;
-  launch Tomcat 8 with the startup script  ``%CATALINA_HOME%\bin\startup.bat`` or by the start-tomcat task ant;
-  (optional) if you previously configured Tomcat Manager you can open a  browser and navigate to `this link <http://localhost:8080/manager>`  and login using the proper username and password (e.g.,  ``admin/admin`` in the previous example);
-  (optional) you can deploy/undeploy/redeploy the downloaded WARs   through the web interface.



## Folder Structure:

-   docs/ --- Documentation of the code (including javadoc)
    -   VerifooDocs.pdf --- Documentation 
    -   verigraph\_doc.pdf --- Documentation of Verigraph for further details
-   lib/ --- All the external libraries (e.g. Z3 library)
    -   junit/ --- Libraries for running tests
    -   lib4j/ --- Libraries for managing the logging operations
-   log/ --- All the logs (for debugging purposes)
-   resources/ ---
    -   log4j2.xml --- Settings of the logs
-   src/ --- Java classes (for further information see the javadoc)
    -   it/polito/verifoo/components/ --- Basic VerifOO classes
    -   it/polito/verifoo/rest/app/ --- Classes to start the Rest application
    -   it/polito/verifoo/rest/common/ --- Classes that retrieve the informations from the JAXB class objects and pass them to VerifOO
    -   it/polito/verifoo/rest/jaxb/ --- Automatically generated JAXB classes
    -   it/polito/verifoo/rest/main/ --- Main class for debugging purposes
    -   it/polito/verifoo/rest/test/ --- Classes that manage all the tests
    -   it/polito/verifoo/rest/webservice/ --- Classes needed for the WebService
    -   it/polito/verifoo/test --- Simple examples on how VerifOO works
    -   it/polito/verigraph/\* --- Verigraph classes
-   target/ --- Folder for the war file
-   testfile/ --- XML files that are used to test the application
-   WebContent/ --- Files needed in order to deploy the service
-   xsd/ --- XML schemas needed for the application
    -   errorSchema.xsd --- XML schema of the response in case an error  occurred
    -   nfvSchema.xsd --- XML schema of Verifoo
    -   xml\_components.xsd --- XML schema of Verigraph (required for VerifOO)
    -   hateoasLinks.xsd --- XML schema used by the root resource to let the client know all the links of the REST WebService
-   build.xml --- Ant script to automate the compiling and the deployment


## Resources:

 * [VerifooDocs.pdf](https://github.com/netgroup-polito/verifoo/blob/rest-service/docs/VerifooDocs.pdf) for documentation.
