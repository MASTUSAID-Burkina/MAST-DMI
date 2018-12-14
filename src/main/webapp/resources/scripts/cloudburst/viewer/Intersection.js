var geometry;
var geomFieldName;
var clonedLayer;
var featureNS;
var activeLayer;
var resultFeatures;
var myfilter;
var activeProject = null;
var newTemp = null;
var resulttmp = [];
var tempInValid = [];
var validate = true;
var check_spatial;
var validation_dialog = false;

function getIntersections(features) {
    var tmp = features.slice(0);
    var reader = new jsts.io.WKTReader();
    var format = new ol.format.WKT();
    resulttmp = [];
    tempInValid = [];

    // Clear selection layer if it exists
    var validationLayer = getLayerByName("vector");
    if (validationLayer !== null) {
        validationLayer.getSource().clear();
        validationLayer.setVisible(true);
    }

    var i;
    i = tmp.length;

    while (i--) {
        var geomA = reader.read(format.writeGeometry(tmp[i].getGeometry()));
        if (geomA.isValid()) {
            for (var j = 0; j < tmp.length; j++) {
                if (i != j) {
                    var geomB = reader.read(format.writeGeometry(tmp[j].getGeometry()));
                    if (geomA.intersects(geomB)) {
                        try {
                            var tmpgeometry = geomA.intersection(geomB);
                            if (tmpgeometry.getNumGeometries() > 1)
                                resulttmp.push(tmpgeometry);
                            else if (tmpgeometry.getNumGeometries() == 1 && tmpgeometry.getArea() != 0)
                                resulttmp.push(tmpgeometry);
                        } catch (e) {
                            if ($.inArray(tmp[j], tempInValid) == -1)
                                tempInValid.push(tmp[j]);
                        }
                    }
                }
            }
        } else {
            if ($.inArray(tmp[i], tempInValid) == -1)
                tempInValid.push(tmp[i]);
        }
        tmp.splice(i, 1);
    }
    intersectionPopup(resulttmp, tempInValid);
}

function intersectionPopup(resulttmp, tempInValid) {
    var wirter = new jsts.io.WKTWriter();
    var format = new ol.format.WKT();
    
    if (resulttmp.length > 0 || tempInValid.length > 0) {
        var popupInfo = '';
        popupInfo += '<html><body>';
        popupInfo += '<h3 class="rowhead" style="padding: 5px; padding-left: 26px;"> ' + $.i18n("gen-summary") + ' </h3>';
        popupInfo += '<div><p><strong>' + $.i18n("viewer-intersect-num") + ':' + resulttmp.length + '</strong></p>';
        popupInfo += '<p><strong>' + $.i18n("viewer-invalid-geom-num") + ':' + tempInValid.length + '</strong></p>';
        popupInfo += '<p><strong>' + $.i18n("viewer-selection-type") + ':' + spatial_validType + '</strong></p></div>';
        //Info diplay in popup
        popupInfo += '<h3 class="rowhead" style="padding: 5px; padding-left: 26px;"> ' + $.i18n("viewer-intersect-errors") + '</h3>';
        popupInfo += '<div class="setOverflow"> <table  class="featureInfo" style="margin-top: 10px; margin-bottom: 10px;">';
        $.each(resulttmp, function (i, dispField) {
            var k = i + 1;
            popupInfo += "<tr><td style=' width: 335px; font-size: 12px; padding: 5px;  '>";
            popupInfo += k + '</td>';
            popupInfo += '<td><button  style="float:right;" name="' + wirter.write(dispField) + '" title="invalidId' + k + '" onclick="javascript:toggleView(this);"><img id="invalidId' + k + '" src="resources/images/viewer/toggle.png" title="Display On" /></button></td>';
            popupInfo += '<td><button  style="float:right;" onclick="javascript:fixedIssues(' + i + ',1);"><img id="fixedId" src="resources/images/viewer/fixed.png" title="Errors Fixed" /></button></td></tr>';
        });
        popupInfo += '</table></div>';
        popupInfo += '<h3 class="rowhead" style="padding: 5px; padding-left: 26px;"> ' + $.i18n("viewer-invalid-geom-errors") + '</h3>';

        popupInfo += '<div class="setOverflow"><table class="featureInfo" style="margin-top: 10px; margin-bottom: 10;">';
        $.each(tempInValid, function (i, dispGeom) {
            var k = i + 1;
            popupInfo += "<tr><td style=' width: 335px; font-size: 12px; padding: 5px;'>";

            popupInfo += k + '</td>';
            popupInfo += '<td><button style="float:right;" name= "' + format.writeGeometry(dispGeom.getGeometry()) + '" title="toggleId' + k + '" onclick="javascript:toggleView(this);"><img id="toggleId' + k + '" src="resources/images/viewer/toggle.png" title="Display On" /></button></td>';
            popupInfo += '<td><button style="float:right;" onclick="javascript:fixedIssues(' + i + ',2);"><img id="fixedId" src="resources/images/viewer/fixed.png" title="Errors Fixed" /></button></td></tr>';
        });
        popupInfo += '</table> </div>';
        popupInfo += '</body></html>';

        jQuery.get('resources/templates/viewer/info.html', function (template) {

            jQuery("#intersectionDialog").empty();
            jQuery("#intersectionDialog").html(template);
            jQuery(".span-c").css("display", "none");

            intersectionDialog = $("#intersectionDialog").dialog({
                autoOpen: false,
                height: 350,
                width: 330,
                resizable: false,
                modal: false,
                close: function () {
                    intersectionDialog.dialog("destroy");
                    validation_dialog = false;
                    var validationLayer = getLayerByName("vector");
                    if (validationLayer !== null)
                        validationLayer.setVisible(false);
                }
            });

            $('#intersectionDialog').dialog({open: function (event, ui) {
                    $(this).parent().find('.ui-dialog-titlebar').append('<a href="#" title="Minimize" style="width: 12px; height: 12px; margin:0 5px; position: absolute; right: 45px; top: 8px;; "onclick="javascript:minimize();"><img src="resources/images/studio/min.png"/></a><a href="#" title="Maximize" style="width: 12px; height: 12px; margin:0 5px; position: absolute; right: 25px; top: 8px;" onclick="javascript:maximize();"><img src="resources/images/studio/max.png"/></a>');
                }});

            validation_dialog = true;
            intersectionDialog.dialog("open");

            jQuery("#info_accordion").empty();
            jQuery("#info_accordion").html(popupInfo);

            jQuery("#info_accordion").accordion({autoHeight: false});
        });
    } else {
        jAlert($.i18n("viewer-no-spatial-errors"), $.i18n("gen-info"));
    }
}

function getLayerByName(name) {
    var reuslt = null;
    map.getLayers().forEach(function (layer) {
        if (layer.get('name') != undefined && layer.get('name') === name) {
            reuslt = layer;
            return;
        }
    });
    return reuslt;
}

function showIntersection(featureGeom) {
    var validationLayer = getLayerByName("vector");
    if (validationLayer !== null) {
        validationLayer.getSource().clear();
    } else {
        var style = new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(248, 214, 153, 0.4)'
            }),
            stroke: new ol.style.Stroke({
                color: '#EE9900',
                width: 2
            })
        });
        
        var validationLayer = new ol.layer.Vector({
            source: new ol.source.Vector(),
            map: map,
            style: function (feature) {
                return style;
            }
        });
        validationLayer.set('name', 'vector');
        map.addLayer(validationLayer);
    }

    var format = new ol.format.WKT();
    var feature = format.readFeature(featureGeom);
    validationLayer.getSource().addFeature(feature);
    map.getView().fit(validationLayer.getSource().getExtent(), map.getSize());
}

function toggleView(id) {
    showIntersection(id.name);
}

function fixedIssues(geomIndex, type) {
    if (type == 1) {
        resulttmp.splice(geomIndex, 1);
        intersectionPopup(resulttmp, tempInValid);
    } else if (type == 2) {
        tempInValid.splice(geomIndex, 1);
        intersectionPopup(resulttmp, tempInValid);
    }
}

function minimize() {
    $("#intersectionDialog").dialog({
        width: 330,
        height: 50,
        resizable: true,
        modal: false,
    });
    $("#intersectionDialog").css('display', 'none');
}
function maximize() {
    $("#intersectionDialog").dialog({
        height: 350,
        width: 330,
        resizable: false,
        modal: false,
    });
}