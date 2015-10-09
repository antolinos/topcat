# TopCAT Configuration

TopCAT v2 is configured using 5 files:

 - topcat-setup.properties
 - topcat.properties
 - topcat.json
 - lang.json
 - topcat.css

## topcat-setup.properties

This properties file configures glassfish and database used by TopCAT.

This file is copied to the glassfish domain config directory on installation.

### Configuration properties

  - **driver** - is the name of the jdbc driver and must match the jar file for your database that you stored in the previous step.
  - **dbProperties** -
  identifies the icat database and how to connect to it.
  - **glassfish** - is the path to the top level of the glassfish installation. It must contain "glassfish/domains", and will be referred to here as GLASSFISH_HOME as if an environment variable had been set.
  - **port** - is the administration port of the chosen glassfish domain which is typically 4848.
  - **topcatUrlRoot** - is the context path where topcat will be deployed (e.g /topcat). Use / for root context.
  - **mail.host** - is the smtp host address
  - **mail.user** - is the name of mail acount user when connecting to the mail server
  - **mail.from** - is the mail from address
  - **mail.property** - is the javamail properties. See https://javamail.java.net/nonav/docs/api/ for list of properties
  - **adminUsername** - The basic authentication user name for the admin REST API
  - **adminPassword** - The basic authentication password for the admin REST API


## topcat.properties

This properties file configures the TopCAT application backend.

Here you configure the location of preparedfiles, enable email and various email templates.

### Configuration properties

  - **file.directory** - is the directory path for temporary prepared files
  - **mail.enable** - whether to enable mailing
  - **mail.subject** - the subject of the  the email. The following tokens are available:
      - **${userName}** - user username
      - **${email}** - user email
      - **${facilityName}** - the facility key name
      - **${preparedId}** - the prepared Id of the download request
      - **${fileName}** - the download name
      - **${downloadUrl}** - the download url
  - **mail.body.https** - is the email body message for https downloads. All subject tokens as above are available.
  - **mail.body.globus** - is the email body message for https downloads. All subject tokens as above are available.
  - **mail.body.smartclient** - is the email body message for smartclient downloads. All subject tokens as above are available.

## topcat.json

This json file configures the TopCAT frontend.

Here you configure how your icat data should be viewed. You can also create static pages in combination with lang.json

This file is copied to the glassfish application web config directory on installation.

Please note this json file is deleted from the application web directory on topcat reinstallation. We recommend you not to edit this file directly in the web directory. Instead, edit the json file in the extracted install directory and do a reinstall. You will not lose your settings this way.

See [topcat.json options](topcat-json-configuration.html) for configurtion properties.

## lang.json

This json file configures all text on the TopCAT frontend. Static html pages can also be created using this file in addition to topcat.json

This file is copied to the glassfish application web languages directory on installation.

Please note this json file is deleted from the application web directory on topcat reinstallation. We recommend you not to edit this file directly in the web directory. Instead, edit the json file in the extracted install directory and do a reinstall. You will not lose your settings this way.

TopCAT v2 uses angular-translate to in order to store text in a single file. The file contains a single json object. Please see the "Teaching your app a language" section from the [angular-translate guide ](https://angular-translate.github.io/docs/#/guide/02_getting-started) for details. You should use the lang.json.example that comes with the topcat distro as your starting point.

## topcat.css

An empty style sheet to allow you to customise your TopCAT site.

This file is copied to the glassfish application web languages styles directory on installation.

Please note this css file is deleted from the application web directory on topcat reinstallation. We recommend you not to edit this file directly in the web directory. Instead, edit the css file in the extracted install directory and do a reinstall. You will not lose your settings this way.