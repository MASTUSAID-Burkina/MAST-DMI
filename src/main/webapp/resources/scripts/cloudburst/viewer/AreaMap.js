
var vertexTableList = [];
var neighbourList = null;
var activeProject = "";
var infoList = null;
var boundsList = null;
var bbox = "";

function generateAreaMap(usin) {
    jQuery.ajax({
        url: "landrecords/neighbour/" + usin,
        async: false,
        success: function (result) {
            tmpList = result;
        }
    });

    var formImage = getFormImage();

    var bbox;
    var wfsurl = "http://" + location.host + "/geoserver/wfs?";

    var featureRequest = new ol.format.WFS().writeGetFeature({
        srsName: 'EPSG:4326',
        featurePrefix: 'Mast',
        featureTypes: ['la_spatialunit_land'],
        outputFormat: 'application/json',
        filter: ol.format.filter.equalTo('landid', usin)
    });

    fetch(wfsurl, {
        method: 'POST',
        body: new XMLSerializer().serializeToString(featureRequest)
    }).then(function (response) {
        return response.json();
    }).then(function (json) {
        var imageWidth = 690;
        var boxIncrease = 0.002;
        var features = new ol.format.GeoJSON().readFeatures(json);
        var parcelExtent = features[0].getGeometry().getExtent();

        bbox = (parcelExtent[0] - boxIncrease) + ',' + (parcelExtent[1] - boxIncrease) + ','
                + (parcelExtent[2] + boxIncrease) + ',' + (parcelExtent[3] + boxIncrease);

        proj4.defs("EPSG:32630", "+proj=utm +zone=30 +ellps=WGS84 +datum=WGS84 +units=m +no_defs");
        proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");
        
        var newExtent = ol.extent.buffer(parcelExtent, boxIncrease);
        newExtent = ol.proj.transformExtent(newExtent, "EPSG:4326", "EPSG:32630");
        var extentWidth = ol.extent.getWidth(newExtent);

        var dpi = 96;
        var spi = dpi / 2.54;
        var smInPixel = (extentWidth * 100) / imageWidth;
        var scaleSize = Math.round(smInPixel * spi);

        // Round to nice number
        if (scaleSize % 10 > 0) {
            scaleSize = (scaleSize - scaleSize % 10) + 10;
        }

        var generateAreaMapUrl = "http://" + location.host + "/geoserver/wms?" + "bbox=" + bbox + "&FORMAT=image/png&REQUEST=GetMap&layers=Mast:HAB_Villages,Mast:HAB_Roads,Mast:HYD_Rivers,Mast:la_spatialunit_land&width=" + imageWidth + "&height=690&srs=EPSG:4326" + "&cql_filter=INCLUDE;INCLUDE;INCLUDE;landid=" + usin;
        
        jQuery.ajax({
            type: 'GET',
            url: 'resources/templates/viewer/areamap.html',
            dataType: 'html',
            success: function (data1) {
                jQuery("#printDiv div").empty();

                jQuery("#printDiv").append(data1);
                jQuery('#area_map_url').append('<img  src=' + generateAreaMapUrl + '>');

                $('.commune_logo').append("<img width='125' height='100' src='" + formImage + "'>");
                $('#region_area').text(tmpList.region);
                $('#commune_area').text(tmpList.commune);
                $('#province_area').text(tmpList.province);
                $("#lblScale").text("1:" + scaleSize);
                
                var printWindow = window.open('', 'popUpWindow', 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                printWindow.document.close();
                var html = $("#printDiv").html();
                printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-map.css" type="text/css" />' +
                        '<script src="../resources/scripts/cloudburst/viewer/AreaMap.js"></script>' +
                        '<script src="../resources/scripts/cloudburst/viewer/mapImage.js"></script>' +
                        '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                        '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                printWindow.focus();
            }
        });
    });
}
function printForm() {
    document.getElementsByClassName('print_form')[0].style.visibility = "hidden";
    location.reload();
    window.print();
}
