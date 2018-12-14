function mapImage(ID, callbackFunc) {
    var bbox;
    var wfsurl = "http://" + location.host + "/geoserver/wfs?";
    var wmsurl = "http://" + location.host + "/geoserver/wms?";
    var imageWidth = 350;
    var boxIncrease = 0.00017;
    
    var featureRequest = new ol.format.WFS().writeGetFeature({
        srsName: 'EPSG:4326',
        featurePrefix: 'Mast',
        featureTypes: ['la_spatialunit_land'],
        outputFormat: 'application/json',
        filter: ol.format.filter.equalTo('landid', ID)
    });

    fetch(wfsurl, {
        method: 'POST',
        body: new XMLSerializer().serializeToString(featureRequest)
    }).then(function (response) {
        return response.json();
    }).then(function (json) {
        var features = new ol.format.GeoJSON().readFeatures(json);
        var parcelExtent = features[0].getGeometry().getExtent();
        proj4.defs("EPSG:32630", "+proj=utm +zone=30 +ellps=WGS84 +datum=WGS84 +units=m +no_defs");
        proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

        var vertexlist1 = features[0].getGeometry().getCoordinates()[0];
        var tempStr = "";
        for (var i = 0; i < vertexlist1.length - 1; i++) {
            if (tempStr === "") {
                tempStr = vertexlist1[i][0] + "," + vertexlist1[i][1];
            } else {
                tempStr = tempStr + "," + vertexlist1[i][0] + "," + vertexlist1[i][1];
            }
        }

        var newExtent = ol.extent.buffer(parcelExtent, boxIncrease);
        newExtent = ol.proj.transformExtent(newExtent, "EPSG:4326", "EPSG:32630");
        var extentWidth = ol.extent.getWidth(newExtent);
        
        var dpi = 96;
        var spi = dpi / 2.54;
        var smInPixel = (extentWidth * 100) / imageWidth;
        scaleSize = Math.round(smInPixel * spi);
        
        // Round to nice number
        if(scaleSize % 10 > 0){
            scaleSize = (scaleSize - scaleSize % 10) + 10;
        }
        
        features[0].getGeometry().transform("EPSG:4326", "EPSG:32630");
        vertexlist = features[0].getGeometry().getCoordinates()[0];

        $.ajax({
            type: 'POST',
            url: "landrecords/vertexlabel",
            data: {"vertexList": tempStr},
            async: false,
            success: function (data) {
            }
        });

        var area = 0.000247105 * (features[0].getGeometry().getArea());
        area = area.toFixed(3);
        $("#area_id").text(area);
        $("#_idarea").text(area);

        bbox = (parcelExtent[0] - boxIncrease) + ',' + (parcelExtent[1] - boxIncrease) + ','
                + (parcelExtent[2] + boxIncrease) + ',' + (parcelExtent[3] + boxIncrease);

        var parcelMapUrl = wmsurl + "bbox=" + bbox + "&styles=generate_map,vertexlabel&FORMAT=image/png&REQUEST=GetMap&layers=Mast:la_spatialunit_land,Mast:vertexlabel&width=" + imageWidth + "&height=350&srs=EPSG:4326&cql_filter=landid=" + ID + ";INCLUDE";

        $('#boundary_map').empty();
        $('#boundary_map').append('<img id="theImg" src=' + parcelMapUrl + '>');
        
        if(typeof callbackFunc !== 'undefined'){
            callbackFunc();
        }
    });
}

function getStaticFilter(fieldName, fieldVal) {
    var filter;

    filter = new OpenLayers.Filter.Comparison({
        type: OpenLayers.Filter.Comparison.EQUAL_TO,
        matchCase: false,
        property: fieldName,
        value: fieldVal
    });

    return filter;
}

function getNS(layerName, url) {
    if (url === null)
        return url;
    var _wfsurl = url.replace(new RegExp("wms", "i"), "wfs");
    var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + layerName;

    var featureNS = '';
    $.ajax({
        url: _wfsSchema,
        dataType: "xml",
        async: false,
        success: function (data) {
            var featureTypesParser = new OpenLayers.Format.WFSDescribeFeatureType();
            var responseText = featureTypesParser.read(data);
            featureNS = responseText.targetNamespace;
        }
    });
    return featureNS;
}

function createSquareBounds(inBounds) {

    //get centroid of bounds
    var newLeft;
    var newRight;
    var newBottom;
    var newTop;
    var outBounds;
    var centroidLatLong = inBounds.getCenterLonLat();

    //get the larger side of the bound(Rectangle)
    if (inBounds.getWidth() > inBounds.getHeight()) {
        //calculate square bounds
        newLeft = inBounds.toArray()[0];
        newBottom = centroidLatLong.lat - inBounds.getWidth() / 2;
        newRight = inBounds.toArray()[2];
        newTop = centroidLatLong.lat + inBounds.getWidth() / 2;
        outBounds = new OpenLayers.Bounds(String(newLeft), String(newBottom), String(newRight), String(newTop));
        return outBounds;

    } else if (inBounds.getWidth() < inBounds.getHeight()) {
        //calculate square bounds
        newLeft = centroidLatLong.lon - inBounds.getHeight() / 2;
        newBottom = inBounds.toArray()[1];
        newRight = centroidLatLong.lon + inBounds.getHeight() / 2;
        newTop = inBounds.toArray()[3];
        outBounds = new OpenLayers.Bounds(String(newLeft), String(newBottom), String(newRight), String(newTop));
        return outBounds;

    } else {
        outBounds = inBounds;
        return outBounds;
    }
}

