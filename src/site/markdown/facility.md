# Facility Object Properties

## Property: _**facilityName**_ (required)

Value: string

A facility name used internally by TopCAT. This must match the key/property name of the facility object.


## Property: _**title**_ (required)

Value: string

The name of the facility you wish to display to the user. This name appears in columns and notification messages.


## Property: _**icatUrl**_ (required)

Value: string

The icat server url for the facility.


## Property: _**facilityId**_ (required)

Value: integer

The id value of the facility in icat. You can retreive this by looking at the facility table in you icat database. Alternatively, the id can be retrieve using `https://YOUR_ICAT_SERVER_URL/icat/entityManager?query=SELECT+f+from+Facility+f&sessionId=SESSION_ID` (replace SESSION_ID with a valid sessionId).

You can get a valid sessionId using the following wget command (replace YOUR_USERNAME, YOUR_PASSWORD and YOUR_ICAT_URL. You must urlencode, your username or password if they contain special characters).

```
wget --no-check-certificate --post-data json=%7B%22plugin%22%3A%22ldap%22%2C%20%22credentials%22%3A%5B%7B%22username%22%3A%22YOUR_USERNAME%22%7D%2C%20%7B%22password%22%3A%22YOUR_PASSWORD%22
%7D%5D%7D -qO - https://YOUR_ICAT_URL/icat/session
```


## Property: _**idsUrl**_ (required)

Value: string

The url of the ids server for the facility.


## Property: _**hierarchy**_ (required)

Value: Array of strings

Example:

```
"hierarchy": [
    "facility",
    "proposal",
    "investigation",
    "dataset",
    "datafile"
],

```

The hierarchy property defines how your icat data is browsed in TopCAT. Your icat data in made of different entities (see [icat schema](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html)) with the root entity being facility. By defining a hierarchy, you specify the next entity that should be displayed when a user browse the data in a facility.

The supported entities are:

  - [facility](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Facility)
  - [instrument](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Instrument)
  - [facilityCycle](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#FacilityCycle)
  - proposal
  - [investigation](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Investigation)
  - [dataset](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Dataset)
  - [datafile](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Datafile)

**proposal** is not a real icat entity but a virtual one created for TopCAT. It represents the name column of the Investigation entity. It only has one field called **name** and can be used to group investigations using the same name value.

Hierarchy rules:

  - Facility must always be the first entity in the hierarchy array
  - All entities except facility are optional
  - The following orders are supported:
      - facility → instrument
      - facility → facilityCycle
      - facility → proposal
      - facility → investigation
      - facility → dataset
      - facility → datafile
      - instrument → facilityCycle
      - instrument → proposal
      - instrument → investigation
      - instrument → dataset
      - instrument → datafile
      - facilityCycle → proposal
      - facilityCycle → facilityCycle
      - facilityCycle → investigation
      - facilityCycle → dataset
      - facilityCycle → datafile
      - proposal → instrument
      - proposal → investigation
      - investigation → dataset
      - investigation → datafile
      - dataset → datafile


## Property: _**authenticationType**_ (required)

Value: array of authenticationType object

Example:

```
"authenticationType": [
    {
        "title": "Username/Password",
        "plugin": "ldap"
    },
    {
        "title": "DB",
        "plugin": "db"
    },
    {
        "title": "Anonymous",
        "plugin": "anon"
    }
]

```

The authenticationType property sets the possible authentication type that TopCAT can use to authenticate against icat.

An authenticationType object has the following properties:


  - **title** (string - required) - the dropdown menu option name. Dropdown menu  is displayed only if more than one authentication type is set.
  - **plugin** (string - required) - the icat plugin name used for authentication. i.e. **db**, **ldap**, **uows**, **anon** (depends on icat server).


## Property: _**downloadTransportType**_ (required)

Value: array of downloadTransportType object

Example:

```
"downloadTransportType": [
    {
        "displayName" : "Https",
        "type" : "https",
        "default" : true,
        "url": "https://fdsgos11.fds.rl.ac.uk"
    },
    {
        "displayName" : "Globus",
        "type" : "globus",
        "url": "https://fdsgos11.fds.rl.ac.uk"
    }
]
```

The downloadTransportType property sets the possible download transport type that TopCAT can use to download files. You must have at least one downloadTransportType object in the array.

A downloadTransportType object has the following properties:


  - **displayName** (string - required) - the dropdown menu option name the the user select
  - **type** (string - required) - the transport type. Must be **https** or **globus**. (For globus, a globus polling software (yet to be release) is required to be installed on the ids in addition to a globus server installation)
  - **default** (boolean - optional) - which downloadTransportType should be the default on the dropdown menu. Only one downloadTransportType should have this property
  - **url** (string - required) - the url of the ids server.


TopCAT also support a transport call smartclient which uses a desktop application call [IDS Smart Client](http://icatproject.org/user-documentation/ids-smartclient/) to download files. TopCAT automatically detects if the application is running. If it is running, TopCAT will add the option to the transport dropdown menu.


## Property: _**browseGridOptions**_ (required)

Value: object mapping hierarchy values to [GridOptions](gridoption) objects

Example:

```
"browseGridOptions": {
    "instrument": {
        "enableFiltering": true,
        "columnDefs": [
            {
                "field": "fullName",
                "displayName": "Full Name",
                "translateDisplayName": "BROWSE.COLUMN.INSTRUMENT.FULLNAME",
                "type": "string",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                },
                "sort": {
                    "direction": "asc"
                },
                "link": true
            }
        ]
    },
    "proposal": {
        "enableFiltering": true,
        "columnDefs": [
            {
                "field": "name",
                "displayName": "Name",
                "type": "string",
                "sort": {
                    "direction": "asc"
                },
                "filter": {
                    "condition": "starts_with",
                    "placeholder": "Containing...",
                    "type": "input"
                },
                "cellTooltip": true,
                "link": true
            }
        ]
    },
    .......
}
```
This configures the grid displayed on the browse tab. Browsing data is based on the hierarchy defined for a facility. Each facility can have its own hierarchy.

For each entity defined in the hierarchy array, you must add a matching property with a [GridOptions](gridoption.html) object as its value in the browseGridOptions object.


## Property: _**metaTabs**_ (Optional)

Value: object with a property for each hierarchy entity with an array of [metaTab object](metatab.html) as its value

Example:

```
"metaTabs": {
    "instrument": [
        {
            "translateDisplayName": "METATABS.INSTRUMENT.TABTITLE",
            "field": "instrument",
            "default": true,
            "data": [
                {
                    "translateDisplayName": "METATABS.INSTRUMENT.NAME",
                    "field": "fullName"
                },
                {
                    "translateDisplayName": "METATABS.INSTRUMENT.DESCRIPTION",
                    "field": "description"
                },
                {
                    "translateDisplayName": "METATABS.INSTRUMENT.TYPE",
                    "field": "type"
                },
                {
                    "translateDisplayName": "METATABS.INSTRUMENT.URL",
                    "field": "url"
                }
            ]
        }
    ]
}
```


For each entity defined in the hierachy, you should have a corresponding property (named the same as the entity) in the metaTab object. Each property must have a [metaTab object](#metatab-object-properties) as its value.