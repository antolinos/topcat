# MetaTab Object Properties

A metaTab object corresponds to a single tab in the metatab area. It allows you to define what fields should be displayed for an entity or related entities.

## Property: _**displayName**_ (Optional. Either translateDisplayName or displayName must be set)

Value: string

The title of tab.

If both displayName and translateDisplayName are set, translateDisplayName is used.


## Property: _**translateDisplayName **_ (Optional. Either translateDisplayName or displayName must be set)

Value: string

Use a property from lang.json as the title of the tab.

If both displayName and translateDisplayName are set, translateDisplayName is used.


## Property: _**default **_ (required)

Value: boolean

Set this to true if the field matched the property name of the object else set it as false.


## Property: _**queryParams**_ (optional)

Value: array of strings (where string is a relationship class name to the current entity)

Example:

```
"queryParams": [
    "InvestigationUser",
    "User"
]

```

It is possible to add a tab containing metadata from a related entity. For example, if the current entity is an investigation we can add a tab to list all investigation users for the investigation. To do this, we add a queryParams properties and an array which maps the relationship from investigation to investigation user using the entity class name (see [icat schema](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html)) e.g.

[InvestigationUser](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#InvestigationUser) → [User](http://icatproject.org/mvn/site/icat/server/4.6.0-SNAPSHOT/schema.html#User)


## Property: _**data**_ (required)

Value: array of objects

Example:

```
"data": [
    {
        "translateDisplayName": "METATABS.INVESTIGATION.NAME",
        "field": "name"
    },
    {
        "translateDisplayName": "METATABS.INVESTIGATION.TITLE",
        "field": "title"
    },
    {
        "translateDisplayName": "METATABS.INVESTIGATION.SUMMARY",
        "field": "summary"
    },
    {
        "translateDisplayName": "METATABS.INVESTIGATION.START_DATE",
        "field": "startDate"
    },
    {
        "translateDisplayName": "METATABS.INVESTIGATION.END_DATE",
        "field": "endDate"
    }
]

```


Example (fields from a related entity) :

```
"data": [
    {
        "field": "user",
        "default": false,
        "data": [
            {
                "translateDisplayName": "METATABS.INVESTIGATION_USERS.NAME",
                "field": "fullName"
            }
        ]
    }
]
```

This sets the field data to display in a meta tab. The object has 4 possible properties:

  - **displayName** (string - optional) - the field label
  - **translateDisplayName** (string - optional) - Use a property from lang.json as the field label
  - **field** (string - required) - the entity field name whose value will be displayed
  - **data** (object - optional) - used when you wish to display relationship fields

Either displayName or translateDisplayName must be set. If both title and translateDisplayName are set, translateDisplayName is used.