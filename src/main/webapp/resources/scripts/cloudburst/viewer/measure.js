var map;
var selectedMeasure = 1;
var sketch;
var continuePolygonMsg;
var continueLineMsg;
var typeSelect;
var geodesicCheckbox;
var source;
var wgs84Sphere = new ol.Sphere(6378137);
var isMeters = false;

Cloudburst.Measure = function (map, _searchdiv) {

    continuePolygonMsg = $.i18n("viewer-click-to-draw-poly");
    continueLineMsg = $.i18n("viewer-click-to-draw-line");

    this.map = map;
    searchdiv = _searchdiv;

    $("#tabs-Tool").empty();

    jQuery.get('resources/templates/viewer/measure.html', function (template) {

        addTab($.i18n('viewer-measure'), template);

        // Event binding for options div
        $("#options-s-d").hide();
        $("#options-s-t").click(function () {
            $("#options-s-d").slideToggle('fast');
        });

        source = new ol.source.Vector();

        var vector = new ol.layer.Vector({
            source: source,
            style: new ol.style.Style({
                fill: new ol.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                }),
                stroke: new ol.style.Stroke({
                    color: '#ffcc33',
                    width: 2
                }),
                image: new ol.style.Circle({
                    radius: 7,
                    fill: new ol.style.Fill({
                        color: '#ffcc33'
                    })
                })
            })
        });
        vector.set('aname', 'measure');
        map.addLayer(vector);
        createHelpTooltip();
        createMeasureTooltip();

        var pointerMoveHandler = function (evt) {
            if (evt.dragging) {
                return;
            }
            var helpMsg = $.i18n("viewer-click-to-start-drawing");

            if (sketch) {
                var geom = (sketch.getGeometry());
                if (geom instanceof ol.geom.Polygon) {
                    helpMsg = continuePolygonMsg;
                } else if (geom instanceof ol.geom.LineString) {
                    helpMsg = continueLineMsg;
                }
            }

            helpTooltipElement.innerHTML = helpMsg;
            helpTooltip.setPosition(evt.coordinate);
            helpTooltipElement.classList.remove('hidden');
        };

        map.on('pointermove', pointerMoveHandler);

        map.getViewport().addEventListener('mouseout', function () {
            helpTooltipElement.classList.add('hidden');
        });

        addInteraction();

    });
};


function Typechange()
{
    map.removeInteraction(measure_draw);
    addInteraction();
}


function addInteraction() {
    typeSelect = $("#type").val();
    geodesicCheckbox = document.getElementById('geodesic');
    //var type = (typeSelect.value == 'area' ? 'Polygon' : 'LineString');
    var type;

    if (typeSelect == 'area') {
        type = "Polygon";
        isMeters = false;
    } else if (typeSelect == 'area-meters') {
        type = "Polygon";
        isMeters = true;
    } else if (typeSelect == 'length') {
        type = "LineString";
        isMeters = false;
    } else if (typeSelect == 'length-meters') {
        type = "LineString";
        isMeters = true;
    }

    measure_draw = new ol.interaction.Draw({
        source: source,
        type: /** @type {ol.geom.GeometryType} */ (type),
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0, 0, 0, 0.5)',
                lineDash: [10, 10],
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 5,
                stroke: new ol.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.7)'
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                })
            })
        })
    });

    map.addInteraction(measure_draw);

    var listener;
    measure_draw.on('drawstart', function (evt) {
        // set sketch
        sketch = evt.feature;

        /** @type {ol.Coordinate|undefined} */
        var tooltipCoord = evt.coordinate;

        listener = sketch.getGeometry().on('change', function (evt) {
            var geom = evt.target;
            var output;
            if (geom instanceof ol.geom.Polygon) {
                output = formatArea(geom) + " " + formatLength(new ol.geom.LineString(geom.getLinearRing(0).getCoordinates()));
                tooltipCoord = geom.getInteriorPoint().getCoordinates();
            } else if (geom instanceof ol.geom.LineString) {
                output = formatLength(geom);
                tooltipCoord = geom.getLastCoordinate();
            }
            measureTooltipElement.innerHTML = output;
            measureTooltip.setPosition(tooltipCoord);
        });
    }, this);

    measure_draw.on('drawend', function () {
        measureTooltipElement.className = 'tooltip tooltip-static';
        measureTooltip.setOffset([0, -7]);
        // unset sketch
        sketch = null;
        // unset tooltip so that a new one can be created
        measureTooltipElement = null;
        createMeasureTooltip();
        ol.Observable.unByKey(listener);
    }, this);
}

/**
 * Creates a new help tooltip
 */
function createHelpTooltip() {
    helpTooltipElement = document.createElement('div');
    helpTooltipElement.className = 'tooltip hidden';
    helpTooltip = new ol.Overlay({
        element: helpTooltipElement,
        offset: [15, 0],
        positioning: 'center-left'
    });
    map.addOverlay(helpTooltip);
}


/**
 * Creates a new measure tooltip
 */
function createMeasureTooltip() {
    measureTooltipElement = document.createElement('div');
    measureTooltipElement.className = 'tooltip tooltip-measure';
    measureTooltip = new ol.Overlay({
        element: measureTooltipElement,
        offset: [0, -15],
        positioning: 'bottom-center'
    });
    map.addOverlay(measureTooltip);
}

var formatLength = function (line) {
    var length;
    var coordinates = line.getCoordinates();
    length = 0;
    var sourceProj = map.getView().getProjection();
    for (var i = 0, ii = coordinates.length - 1; i < ii; ++i) {
        var c1 = ol.proj.transform(coordinates[i], sourceProj, 'EPSG:4326');
        var c2 = ol.proj.transform(coordinates[i + 1], sourceProj, 'EPSG:4326');
        length += wgs84Sphere.haversineDistance(c1, c2);
    }

    var output;

    if (!isMeters) {
        output = (Math.round(length / 1000 * 100) / 100) + ' ' + 'km';
    } else {
        output = Math.round(length) + ' ' + 'm';
    }
    return output;
};

var formatArea = function (polygon) {
    var area;
    var sourceProj = map.getView().getProjection();
    var geom = (polygon.clone().transform(sourceProj, 'EPSG:4326'));
    var coordinates = geom.getLinearRing(0).getCoordinates();
    area = Math.abs(wgs84Sphere.geodesicArea(coordinates));

    var output;

    if (!isMeters) {
        output = (Math.round(area / 10000 * 100) / 100) + ' ha';
    } else {
        output = Math.round(area) + ' m<sup>2</sup>';
    }
    return output;
};

function translateMeasureStrings() {
    $('#measure_unit').html($.i18n('measure_unit') + ":");
    $('#measureUnit').append($("<option></option>").attr("value", "m").text($.i18n('measure_meter')));
    $('#measureUnit').append($("<option></option>").attr("value", "km").text($.i18n('measure_km')));
    $('#measureUnit').append($("<option></option>").attr("value", "mi").text($.i18n('measure_mile')));
}

function getUnitChangeValue(_currentMeasure, _applyedUnit, _selected_unit) {
    var currMeasure = 0.000;
    var factor = 1;
    if (_applyedUnit == 'm' && _selected_unit == 'km') {
        if (selectedMeasure == 1) {
            factor = 0.001;
        } else {
            factor = 0.000001;
        }
        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == 'm' && _selected_unit == 'mi') {

        if (selectedMeasure == 1) {
            factor = 0.0006214;
            0
        } else {
            factor = 3.86102159e-7;
        }

        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == 'km' && _selected_unit == 'm') {
        if (selectedMeasure == 1) {
            factor = 1000;
        } else {
            factor = 1000000;
        }


        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == 'km' && _selected_unit == 'mi') {


        if (selectedMeasure == 1) {
            factor = 0.6214;
        } else {
            factor = 0.386102159;
        }

        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == 'mi' && _selected_unit == 'm') {


        if (selectedMeasure == 1) {
            factor = 1609;
        } else {
            factor = 2589988.11034;
        }
        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == 'mi' && _selected_unit == 'km') {


        if (selectedMeasure == 1) {
            factor = 1.6093;
        } else {
            factor = 2.58998811034;
        }
        currMeasure = _currentMeasure * factor;
    } else if (_applyedUnit == _selected_unit) {
        factor = 1;
        currMeasure = _currentMeasure * factor;
    }

    return currMeasure;
}

var segVal = 0.000;
var applyedUnit;
var selected_unit;
var currentMeasure = 0.000;
var evtOrder;

var lastMeasure = 0.0;

function measurementToolActivated() {
    lastMeasure = 0.0;
}

function handleFinalMeasurement(event) {
    var units = event.units;
    var measure = event.measure;
    var outFinal = 0.000;
    var outSegment = 0.000;
    var order = event.order;
    evtOrder = order;
    selected_unit = $("#measureUnit").val();
    applyedUnit = units;

    if (evtOrder == 1) {
        //outFinal=measure.toFixed(3) + " " + units;
        outFinal = getUnitChangeValue(measure, applyedUnit, selected_unit);
        finalmeasure = outFinal;

        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit);

        if (lastMeasure > 0) {
            //out += "    last segment: " + (measure-lastMeasure).toFixed(3) + " " + units;
            //outSegment=(measure-lastMeasure).toFixed(3) + " " + units;
            outSegment = getUnitChangeValue((measure - lastMeasure), applyedUnit, selected_unit);
            segmeasure = outSegment;
            $('#segValue').html(outSegment.toFixed(3) + " " + selected_unit);
        }

    } else if (evtOrder == 2) {
        outFinal = getUnitChangeValue(measure, applyedUnit, selected_unit);
        finalmeasure = outFinal;
        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit + "2".sup());

        if (lastMeasure > 0) {
            //out += "    last segment: " + (measure-lastMeasure).toFixed(3) + " " + units;
            //outSegment=(measure-lastMeasure).toFixed(3) + " " + units +"2".sup();
            outSegment = getUnitChangeValue((measure - lastMeasure), applyedUnit, selected_unit);

            //$('#segValue').html(outSegment.toFixed(3)+" " + selected_unit);
        }
    }
    lastMeasure = 0.0;
    applyedUnit = selected_unit;
}

var segmeasure = 0.000;
var finalmeasure = 0.000;

function handlePartialMeasurement(event) {
    var units = event.units;
    var measure = event.measure;
    var outFinal = 0.000;
    var outSegment = 0.000;
    var order = event.order;
    evtOrder = order;
    selected_unit = $("#measureUnit").val();
    applyedUnit = units;
    if (evtOrder == 1) {
        //outFinal=measure.toFixed(3) + " " + units;
        outFinal = getUnitChangeValue(measure, applyedUnit, selected_unit);
        finalmeasure = outFinal;

        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit);
        $('#segValue').html(outFinal.toFixed(3) + " " + selected_unit);
        if (lastMeasure > 0) {

            //outSegment=(measure-lastMeasure).toFixed(3) + " " + units;
            outSegment = getUnitChangeValue((measure - lastMeasure), applyedUnit, selected_unit);
            segmeasure = outSegment;
            $('#segValue').html(outSegment.toFixed(3) + " " + selected_unit);
        }
    } else if (evtOrder == 2) {

        //outFinal=measure.toFixed(3) + " " + units +"2".sup();

        outFinal = getUnitChangeValue(measure, applyedUnit, selected_unit);
        finalmeasure = outFinal;
        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit + "2".sup());

        if (lastMeasure > 0) {

            //outSegment=(measure-lastMeasure).toFixed(3) + " " + units +"2".sup();
            outSegment = getUnitChangeValue((measure - lastMeasure), applyedUnit, selected_unit);

            //$('#segValue').html(outSegment.toFixed(3)+" " + selected_unit);
        }
    }

    lastMeasure = measure;
    applyedUnit = selected_unit;
}


function onUnitChange() {
    var outFinal = 0.000;
    var outSegment = 0.000;

    selected_unit = $("#measureUnit").val();

    outFinal = getUnitChangeValue(finalmeasure, applyedUnit, selected_unit);
    outSegment = getUnitChangeValue(segmeasure, applyedUnit, selected_unit);

    if (evtOrder == 1) {

        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit);
        $('#segValue').html(outSegment.toFixed(3) + " " + selected_unit);
    } else
    if (evtOrder == 2) {

        $('#totValue').html(outFinal.toFixed(3) + " " + selected_unit + "2".sup());
        //$('#segValue').html(outSegment.toFixed(3)+" " + selected_unit +"2".sup());
    }

    finalmeasure = outFinal;
    segmeasure = outSegment;
    applyedUnit = selected_unit;
}

