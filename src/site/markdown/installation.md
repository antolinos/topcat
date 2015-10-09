#TopCAT Installation Guide

## Compatibility

TopCAT requires icat.server 4.6 and ids.server 1.5.0

##Prerequisites

  - The TopCAT distribution: topcat-2.0.0-distro.zip
  - A suitable deployed container (here assumed to be glassfish) to support a web application. Testing has been carried out with Glassfish 4.0. [Glassfish installation instructions](http://icatproject.org/installation/glassfish/) are available.
  -  A database as described in [Database installation instructions]http://icatproject.org/installation/database/) installed on the server.
  - Python (version 2.4 to 2.7) installed on the server.

##Configure

See [configuration page](configure.html)


##Install

  1. Extract the topcat distro zip file and change directory to the unpacked distribution
  2. Rename all the *.example files by removing the .example extensions. The directory must have 5 files named:
    - topcat-setup.properties
    - topcat.properties
    - topcat.json
    - lang.json
    - topcat.css
  3. Configure each of the 5 files as required. See above.
  4. Change permission of the properties files to 0600
    `chmod 0600 *.properties`
  5. Make setup script executable:
    `chmod +x setup`
  6. Run configure
    `./setup configure`
  7. Run install
    `./setup install`

##Uninstall
  1. Run uninstall
    `./setup uninstall`