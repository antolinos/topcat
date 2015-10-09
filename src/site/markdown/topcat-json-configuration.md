#topcat.json Properties


The topcat.json file is a json object with two properties:

```
{
    site: {

    },
    facilities: {

    }
}

```

TopCAT v2 allows the configuration of multiple facilities. These facilities can be on a single icat server or from multiple icat servers. If you wish to display a facility in TopCAT, you must add a new configuration facility object under the facilities property.

A simplified summary of the configuration object is shown below:


{<br />
&nbsp;&nbsp;&nbsp;&nbsp;[site](site.html):&nbsp;{<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[topcatApiPath](site.html#Property:_topcatApiPath_required)&quot;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[home](site.html#Property:_home_Required)&quot;&nbsp;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[paging](site.html#Property:_paging_Required)&quot;&nbsp;:&nbsp;[{}](paging.html)<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[facilitiesGridOptions](site.html#Property:_facilitiesGridOptions_Required)&quot;:&nbsp;[{}](gridoption.html),<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[cartGridOptions](site.html#Property:_cartGridOptions_Required)&quot;&nbsp;:&nbsp;[{}](gridoption.html)<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[myDataGridOptions](site.html#Property:_myDataGridOptions_Required)&quot;&nbsp;:&nbsp;[{}](gridoption.html)<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[myDownloadGridOptions](site.html#Property:_myDownloadGridOptions_Required)&quot;&nbsp;:&nbsp;[{}](gridoption.html),<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[metaTabs](site.html#Property:_metaTabs_required)&quot;&nbsp;:&nbsp;[{}](metatab.html)<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[pages](site.html#Property:_pages_Optional)&quot;&nbsp;:&nbsp;[{}](page.html)<br />
<br />
&nbsp;&nbsp;&nbsp;&nbsp;},<br />
&nbsp;&nbsp;&nbsp;&nbsp;[facilities](facility.html):&nbsp;{<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;string&quot;&nbsp;:&nbsp;{<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[facilityName](facility.html#Property:_facilityName_required)&quot;&nbsp;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[title](facility.html#Property:_title_required)&quot;&nbsp;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[icatUrl](facility.html#Property:_icatUrl_required)&quot;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[idsUrl](facility.html#Property:_idsUrl_required)&quot;:&nbsp;&quot;string&quot;,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[facilityId](facility.html#Property:_facilityId_required)&quot;:&nbsp;integer,<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[hierarchy](facility.html#Property:_hierarchy_required)&quot;&nbsp;:&nbsp;[<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;string&quot;<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;],<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[authenticationType](facility.html#Property:_authenticationType_required)&quot;:&nbsp;[],<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[downloadTransportType](facility.html#Property:_downloadTransportType_required)&quot;&nbsp;:&nbsp;[],<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[browseGridOptions](facility.html#Property:_browseGridOptions_required)&quot;&nbsp;:&nbsp;[{}](gridoption.html),<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&quot;[metaTabs](facility.html#Property:_metaTabs_Optional)&quot;&nbsp;:&nbsp;[{}](metatab.html)<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br />
&nbsp;&nbsp;&nbsp;&nbsp;}<br />
}<br />


For multiple facilities, you add an additional FACILITY\_KEY\_NAME property and a [facility object](facility.html) as its value. e.g.

```
facilities: {
    "FACILITY_KEY_NAME_1" : {

    },
    "FACILITY_KEY_NAME_2" : {

    }
}
```


## Site Configuration

The topcat.json configuration must contain one property named **site**.

The site object covers the configuration which are not specific to a particular facility. These includes:

  - The topcat API url
  - The home state name
  - The pagination across the whole site. Scoll or page.
  - The grid configuration for My Data tab
  - The grid configuration for facilities browse (shown when you have more than one facility)
  - The grid configuration for Cart tab
  - The grid cofniguration for Download tab
  - The metaTab panel for facilities browse
  - Html pages you wish to add to TopCAT such as About, Contact, Help pages etc.



## Facilities Configuration

For each facility you wish to add to TopCAT, you must add a property/key and a facility object to the facilities object.

Example:

```
"facilities": {
    "KEY_NAME_1" : {
        ....
    },
    "KEY_NAME_2" : {

    }
}
```

The property name is use by TopCAT to identify the facility and must be unique. It appears in urls so avoid using special characters or spaces.




