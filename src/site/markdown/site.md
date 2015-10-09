# Site Object Properties

## Property: _**topcatApiPath**_ (required)

Value: String

Example:

```
"site" : {
    "topcatApiPath" : "api/v1"
}
```

The relative or full path to the TopCAT api. You must leave this as **api/v1** unless you want the TopCAT api hosted on a different server from the web frontend.

****

## Property: _**home**_ (Required)

Value: String (must be **my-data**, **browse**, **cart** or **download**)

Example:

```
"site": {
    "home": "my-data"
}
```

TopCAT allows you define which main tab should be the home page.

Possible values are **home**, **browse**, **cart** or **download**.


## Property: _**paging**_ (Required)

Value: Paging Object (see [Paging](paging.html) object)

Example :

```
"site": {
    "paging": {
        "pagingType": "scroll",
        "scrollPageSize": 100,
        "scrollRowFromEnd" : 20
    }
}

```

This configures the pagination type used throughout the entire TopCAT frontend.


## Property: _**facilitiesGridOptions**_ (Required)

Value: GridOptions Object (see [GridOption](gridoption.html) object)

Example:

```
{
    "facilitiesGridOptions": {
        "enableFiltering": true,
        "columnDefs": [
            {
                "field": "fullName",
                "displayName": "Facility Full Name",
                "translateDisplayName": "BROWSE.COLUMN.FACILITY.FULLNAME",
                "filter": {
                    "condition": "starts_with",
                    "placeholder": "Containing...",
                    "type": "input"
                },
                "link": true
            },
            {
                "field": "name",
                "displayName": "Facility Name",
                "translateDisplayName": "BROWSE.COLUMN.FACILITY.NAME",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            }
        ]
    }
}


```

This configures the grid displayed on the browse facilities page. The browse facilities page is only displayed if multiple facilities are set.

See the [icat schema for facility](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#Facility) for possible column fields.



## Property: _**cartGridOptions**_ (Required)

Value: GridOption Object (see [GridOption](gridoption.html) object)

Example:

```
{
    "cartGridOptions": {
        "enableFiltering": true,
        "columnDefs": [
            {
                "field": "name",
                "translateDisplayName": "CART.COLUMN.NAME",
                "type" : "string",
                "filter": {
                    "condition": "starts_with",
                    "placeholder": "Containing...",
                    "type": "input"
                },
                "cellTooltip": true
            },
            {
                "field": "entityType",
                "translateDisplayName": "CART.COLUMN.ENTITY_TYPE",
                "cellFilter": "entityTypeTitle",
                "type" : "string",
                "filter": {
                    "condition": "starts_with",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            },
            {
                "field": "size",
                "translateDisplayName": "CART.COLUMN.SIZE",
                "type" : "number",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            },
            {
                "field": "availability",
                "translateDisplayName": "CART.COLUMN.AVAILABILITY",
                "type" : "string",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            }
        ]
    }
}

```


This configures the grid displayed on the Cart tab.

Possible column fields are:

  - **name** (string) - the name of the item added to the cart
  - **entityType** (string) - the entity type of the item i.e. investigation, dataset or datafile
  - **size** (number) - displays the total size of the item. The size is retrieved from the ids server via an ajax call. A spinner is display while the result is being retrieved.
  - **availability** (string) - displays the availability of an item. The availability status is retrieved from the ids server via an ajax call. This is useful if the ids is configure for two level storage where files may have to be restored from tape. If files are always online, this field will not be of any use.


## Property: _**myDataGridOptions**_ (Required)

Value: GridOption Object (see [GridOption](gridoption.html) object)

Example:

```
{
    "myDataGridOptions": {
        "entityType" : "investigation",
        "investigation": {
            "enableFiltering": true,
            "enableSelection": true,
            "includes" : [
                "investigation.investigationInstruments.instrument"
            ],
            "columnDefs": [
                {
                    "field": "title",
                    "displayName": "Title",
                    "translateDisplayName": "BROWSE.COLUMN.INVESTIGATION.TITLE",
                    "type": "string",
                    "filter": {
                        "condition": "contains",
                        "placeholder": "Containing...",
                        "type": "input"
                    },
                    "cellTooltip": true,
                    "link": true
                },
                {
                    "field": "visitId",
                    "displayName": "Visit Id",
                    "translateDisplayName": "BROWSE.COLUMN.INVESTIGATION.VISIT_ID",
                    "type": "string",
                    "filter": {
                        "condition": "contains",
                        "placeholder": "Containing...",
                        "type": "input"
                    },
                    "link": true
                },
                {
                    "field": "investigationInstruments[0].instrument.fullName",
                    "displayName": "Instrument",
                    "type": "string",
                    "visible": true,
                    "filter": {
                        "condition": "starts_with",
                        "placeholder": "Containing...",
                        "type": "input"
                    }
                },
                {
                    "field": "size",
                    "displayName": "Size",
                    "translateDisplayName": "BROWSE.COLUMN.INVESTIGATION.SIZE",
                    "type": "number"
                },
                {
                    "field": "startDate",
                    "displayName": "Start Date",
                    "translateDisplayName": "BROWSE.COLUMN.INVESTIGATION.START_DATE",
                    "type": "date",
                    "cellFilter": "date: 'yyyy-MM-dd'",
                    "filters": [
                        {
                            "placeholder": "From...",
                            "type": "input"
                        },
                        {
                            "placeholder": "To...",
                            "type": "input"
                        }
                    ],
                    "sort": {
                        "direction": "desc"
                    }
                },
                {
                    "field": "endDate",
                    "displayName": "End Date",
                    "translateDisplayName": "BROWSE.COLUMN.INVESTIGATION.END_DATE",
                    "type": "date",
                    "cellFilter": "date: 'yyyy-MM-dd'",
                    "filters": [
                        {
                            "placeholder": "From...",
                            "type": "input"
                        },
                        {
                            "placeholder": "To...",
                            "type": "input"
                        }
                    ]
                }
            ]
        }
    }
}

```



This configures the grid displayed on the My Data tab. The My Data grid can list either investigation or dataset entity types. The type you want to list is set using the _**entityType**_ property in the GridOption object. This entityType property is specific to myDataGridOptions and is not used elsewhere.

Once entityType is set, the possible fields available are as per the [icat schema](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html) for investigation or dataset. In addition, the following fields are also available:

  - **size** (number) - displays the total size of the entity. The size is retrieved from the ids server via an ajax call. A spinner is display while the result is being retrieved.
  - **availability** (string) - displays the availability of an item. The availability status is retrieved from the ids server via an ajax call. This is useful if the ids is configure for two level storage where files may have to be restored from tape. If files are always online, this field will not be of any use.



## Property: _**myDownloadGridOptions**_ (Required)

Value: GridOption Object (see [GridOption](gridoption.html) object)

Example:

```
{
    "myDownloadGridOptions": {
        "enableFiltering": true,
        "columnDefs": [
            {
                "field": "fileName",
                "translateDisplayName": "DOWNLOAD.COLUMN.FILE_NAME",
                "type" : "string",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            },
            {
                "field": "transport",
                "translateDisplayName": "DOWNLOAD.COLUMN.TRANSPORT",
                "type" : "string",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            },
            {
                "field": "status",
                "translateDisplayName": "DOWNLOAD.COLUMN.STATUS",
                "type" : "string",
                "filter": {
                    "condition": "contains",
                    "placeholder": "Containing...",
                    "type": "input"
                }
            },
            {
                "field": "createdAt",
                "translateDisplayName": "DOWNLOAD.COLUMN.CREATED_AT",
                "type" : "date",
                "cellFilter": "date: 'yyyy-MM-dd HH:mm:ss'",
                "filter": {
                    "placeholder": "Date...",
                    "type": "input"
                },
                "sort": {
                    "direction": "desc"
                }
            }
        ]
    }
}

```

This configures the grid displayed on the Download tab.

Possible column fields are:

  - **fileName** (string) - the download name of the request
  - **transport** (string) - the transport type of the download request
  - **facilityName** (string) - the name of the facility the download request was made to
  - **status** (string) - the status of the download request
  - **createdAt** (date) - the timestamp when the download request was submitted


## Property: _**metaTabs**_ (required)

Value: object with a property named **facility** whose value is a array containing a [metaTab object](metatab.html)

Example:

```
"metaTabs": {
    "facility": [
        {
            "title": "Facility Details",
            "translateDisplayName": "METATABS.FACILITY.TABTITLE",
            "field": "facility",
            "default": true,
            "data": [
                {
                    "title": "Full Name",
                    "translateDisplayName": "METATABS.FACILITY.FULLNAME",
                    "field": "fullName"
                },
                {
                    "title": "Description",
                    "translateDisplayName": "METATABS.FACILITY.DESCRIPTION",
                    "field": "description"
                },
                {
                    "title": "Name",
                    "translateDisplayName": "METATABS.FACILITY.NAME",
                    "field": "name"
                },
                {
                    "title": "URL",
                    "translateDisplayName": "METATABS.FACILITY.URL",
                    "field": "url"
                }
            ]
        }
    ]
}

```

This property sets the metadata to be displayed in the metatab panel area (at the bottom half of the page) when a user click on a row on the browse facilties grid.

For the site object, the metaTabs properties must have a single property named **facility**


## Property: _**pages**_ (Optional)

Value : array of page objects (see [Page](page.html) object)

Example: See [Page](#Page-Object-Example) object

You can create simple html pages in TopCAT. Each page is defined using a page object which defines the url of the page. The html page content is defined in lang.json file.
