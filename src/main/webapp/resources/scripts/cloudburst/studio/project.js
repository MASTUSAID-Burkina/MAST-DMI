var selectedItem = null;
var optVals = [];
var projectAreas = [];
var userroledata = null;
var selectedText = null;
var content = null;
var hamletDetails = [];
var adjName = null;
var adjList = [];
var hamletList = null;
var checkAdjEdit = false;
var checkHamletEdit = false;
var HamletName = null;
var HamletAlias = null;
var HamletCode = null;
var hamletLeaderName = null;
var checkeditHam = false;


function Project(_selectedItem) {
    selectedItem = _selectedItem;
    checkAdjEdit = false;
    checkHamletEdit = false;

    jQuery.ajax({
        type: 'GET',
        url: "project/userbyrole/",
        success: function (popdata)
        {
            userroledata = popdata;
        }
    });

    if (jQuery("#ProjectFormDiv").length <= 0) {
        displayRefreshedProject();
    } else {
        displayProject();
        displayRefreshedProject();
    }
}

function displayRefreshedProject() {
    $('body').find("#hamlet-dialog-form").remove();
    $('body').find("#adjudicator-dialog-form").remove();

    $("#hamlet-dialog-form").remove();
    jQuery.ajax({
        url: "project/",
        success: function (data) {
            jQuery("#projects").empty();
            jQuery.get("resources/templates/studio/" + selectedItem + ".html", function (template) {

                jQuery("#projects").append(template);
                jQuery("#projects").i18n();
                jQuery('#ProjectFormDiv').css("visibility", "visible");
                jQuery("#projectDetails").hide();
                jQuery("#ProjectsRowData").empty();
                jQuery("#projectTable").show();
                jQuery("#project_accordion").hide();
                jQuery("#ProjectTemplate").tmpl(data).appendTo("#ProjectsRowData");
                $("#ProjectsRowData").i18n();
                jQuery("#project_btnSave").hide();
                jQuery("#project_btnBack").hide();
                jQuery("#project_btnNew").show();

                if (data.length > 0) {
                    $("#projectTable").tablesorter({
                        headers: {6: {sorter: false}, 7: {sorter: false}},
                        debug: false, sortList: [[0, 0]], widgets: ['zebra']})
                            .tablesorterPager({container: $("#project_pagerDiv"), positionFixed: false})
                            .tablesorterFilter({filterContainer: $("#project_txtSearch"),
                                filterColumns: [0],
                                filterCaseSensitive: false,
                                filterWaitTime: 1000
                            });
                }
            });
        }
    });
    // GetActiveLayer('default', '');
}

function displayProjectNew() {
    checkAdjEdit = false;
    checkHamletEdit = false;
    displayProject();
}

function displayProject() {
    jQuery("#project_accordion").hide();
    jQuery("#projectTable").show();
    jQuery("#project_btnSave").hide();
    jQuery("#project_btnBack").hide();
    jQuery("#project_btnNew").show();
}

var proj_units = null;
var proj_projections = null;
var proj_formats = null;
var proj_editData = null;
var proj_layerTypes = null;
var proj_layerGroup = null;
var sortedLyrGroup = null;
var proj_users = null;
var proj_baselayer = null;
var proj_country = null;
var editableProject;
var proj_projection = null;

var createEditProject = function (_name) {
    checkeditHam = false;
    editableProject = _name;
    optVals = [];
    jQuery("#project_btnNew").hide();
    jQuery("#project_btnSave").hide();
    jQuery("#project_btnBack").hide();
    jQuery("#projectTable").hide();
    jQuery("#projectDetails").show();
    jQuery("#projectGeneralBody").empty();
    jQuery("#projectConfigurationBody").empty();
    jQuery("#projectLayergroupBody").empty();
    jQuery("#projectBaselayerBody").empty();
    jQuery("#projectDisclaimerBody").empty();
    jQuery("#projectUserList").empty();

    jQuery.ajax({
        url: "unit/",
        success: function (data) {
            proj_units = data;
        },
        async: false
    });

    jQuery.ajax({
        url: "layergroup/",
        success: function (lgdata) {
            proj_layerGroup = lgdata;
        },
        async: false
    });

    jQuery.ajax({
        url: "baselayer/",
        success: function (bldata) {
            proj_baselayer = bldata;
        },
        async: false
    });

    jQuery.ajax({
        url: "projection/",
        success: function (data) {
            proj_projections = data;
        },
        async: false
    });
    jQuery.ajax({
        url: "outputformat/",
        success: function (data) {
            proj_formats = data;
        },
        async: false
    });

    jQuery.ajax({
        url: "user/",
        success: function (userdata) {
            proj_users = userdata;
        },
        async: false
    });


    jQuery.ajax({
        url: "projectcontry/",
        success: function (userdata) {
            proj_country = userdata;
        },
        async: false
    });


    jQuery.ajax({
        url: "projection/",
        success: function (userdata) {
            proj_projection = userdata;
        },
        async: false
    });




    if (_name) {
        jQuery('#name').attr('readonly', true);
        jQuery.ajax({
            url: selectedItem + "/" + _name,
            async: false,
            success: function (data) {

                jQuery("#ProjectTemplateForm").tmpl(data, {}).appendTo("#projectGeneralBody");
                $("#projectGeneralBody").i18n();
                jQuery("#projectConfigurationTemplate").tmpl(data, {}).appendTo("#projectConfigurationBody");
                $("#projectConfigurationBody").i18n();
                jQuery("#projectLayergroupBody").empty();
                jQuery("#ProjectTemplateLayergroup").tmpl(proj_layerGroup, {}).appendTo("#projectLayergroupBody");
                jQuery("#projectBaselayerBody").empty();
                jQuery("#ProjectTemplateBaselayer").tmpl(proj_baselayer, {}).appendTo("#projectBaselayerBody");

                jQuery("#ProjectTemplateDisclaimer").tmpl(data, {}).appendTo("#projectDisclaimerBody");
                $("#projectDisclaimerBody").i18n();
                jQuery("#projectUserList").empty();
                jQuery("#ProjectTemplateUser").tmpl(userroledata).appendTo("#projectUserList");


                jQuery.each(proj_country, function (i, value) {
                    jQuery("#countryId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });

                jQuery.each(proj_units, function (i, value) {
                    jQuery("#project_unit").append(jQuery("<option></option>").attr("value", value.unitid).text(value.unitEn));
                });

                jQuery.each(proj_formats, function (i, value) {
                    jQuery("#project_outputFormat").append(jQuery("<option></option>").attr("value", value.documentformatid).text(value.documentformatEn));
                });

                jQuery.each(proj_projection, function (i, value) {
                    jQuery("#projection_code").append(jQuery("<option></option>").attr("value", value.projectionid).text(value.projection));
                    jQuery("#displayProjection_code").append(jQuery("<option></option>").attr("value", value.projectionid).text(value.projection));
                });


                jQuery("#project_outputFormat").val(data.outputformat.documentformatid);
                jQuery("#project_unit").val(data.unit.unitid);
                jQuery("#projection_code").val(data.projection.projectionid);
                jQuery("#displayProjection_code").val(data.projection.projectionid);


                jQuery("#hid_idseq").val(data.projectnameid);

                if (data.projectArea.length > 0) {

                    if (data.projectArea[0].laSpatialunitgroupHierarchy1 != null) {
                        jQuery("#countryId").val(data.projectArea[0].laSpatialunitgroupHierarchy1.hierarchyid);
                        getRegionOnCountryChange(data.projectArea[0].laSpatialunitgroupHierarchy1.hierarchyid);
                    }
                    jQuery("#hid_id").val(data.projectArea[0].projectareaid);

                    if (data.projectArea[0].laSpatialunitgroupHierarchy2 != null) {
                        jQuery("#regionId").val(data.projectArea[0].laSpatialunitgroupHierarchy2.hierarchyid);
                        getDistrictOnRegionChange(data.projectArea[0].laSpatialunitgroupHierarchy2.hierarchyid)
                    }

                    if (data.projectArea[0].laSpatialunitgroupHierarchy3 != null) {
                        jQuery("#districtId").val(data.projectArea[0].laSpatialunitgroupHierarchy3.hierarchyid);
                        getCommuneOnProvinceChange(data.projectArea[0].laSpatialunitgroupHierarchy3.hierarchyid)
                    }

                    if (data.projectArea[0].laSpatialunitgroupHierarchy4 != null) {
                        jQuery("#CommuneId").val(data.projectArea[0].laSpatialunitgroupHierarchy4.hierarchyid);
                        getHamletOnVillageChange(data.projectArea[0].laSpatialunitgroupHierarchy4.hierarchyid)
                    }


                    if (data.projectArea[0].laSpatialunitgroupHierarchy5 != null) {
                        jQuery("#placeId").val(data.projectArea[0].laSpatialunitgroupHierarchy5.hierarchyid);
                    }

                    $("#mayorname").val(data.projectArea[0].mayorname);
                    jQuery("#villagechairmanId").val(data.projectArea[0].authorizedmember);
                    showSignature("SignatureVillageChairman", data.projectArea[0].authorizedmembersignature);
                    showSignature("SignatureVillageExecutive", data.projectArea[0].executiveofficersignature);
                    showSignature("SignatureDistrictOfficer", data.projectArea[0].landofficersignature);
                    showSignature("ProjectLogo", data.projectArea[0].logo);
                    jQuery("#executiveofficerId").val(data.projectArea[0].executiveofficer);
                    jQuery("#districtofficerId").val(data.projectArea[0].landofficer);
                    jQuery("#villagecode").val(data.projectArea[0].certificatenumber);
                    jQuery("#villagepostalcode").val(data.projectArea[0].postalcode);
                    jQuery("#vcmeetingdate").val(data.projectArea[0].vcMeetingDate);
                }


                jQuery('#name').attr('readonly', true);

                $('[class^="tr-"]').hide();



                if (data.disclaimer != null && data.disclaimer != "") {
                    jQuery("#chkDisclaimer").attr('checked', true);
                    jQuery('#Disclaimer').css("visibility", "visible");
                    jQuery('#Disclaimer').val(data.disclaimer);
                } else {
                    jQuery("#chkDisclaimer").attr('checked', false);
                    jQuery('#Disclaimer').val("");
                    jQuery('#Disclaimer').css("visibility", "hidden");
                }

                $.each(data.projectLayergroups, function (i, ob) {
                    $.each(ob, function (ind, obj) {
                        if (ind == "layergroupBean") {
                            $.each(obj, function (ind1, obj1) {
                                if (ind1 != "layerLayergroups" && ind1 == "layergroupid") {
                                    jQuery("#" + obj1).attr('checked', true);

                                }

                            });
                        }

                    });
                });

                if (data.projectBaselayers.length > 0) {

                    for (_i = 0; _i < data.projectBaselayers.length; _i++) {
                        if (data.projectBaselayers[_i].baselayers.baselayerEn.indexOf("Google_") > -1) {
                            populatebaselayer('Google', data);
                        } else if (data.projectBaselayers[_i].baselayers.baselayerEn.indexOf("Bing") > -1) {
                            populatebaselayer('Bing', data);
                        } else if (data.projectBaselayers[_i].baselayers.baselayerEn.indexOf("Open_") > -1) {
                            populatebaselayer('Open', data);
                        } else if (data.projectBaselayers[_i].baselayers.baselayerEn.indexOf("MapQuest") > -1) {
                            populatebaselayer('MapQuest', data);
                        }
                    }

                } else {
                    populatebaselayer('Google', '');
                }

                $.each(data.projectBaselayers, function (i, ob) {
                    $.each(ob, function (ind, obj) {
                        if (ind == "baselayers") {
                            $.each(obj, function (ind1, obj1) {
                                if (ind1 == "baselayerid") {
                                    jQuery("#" + obj1).attr('checked', true);
                                }

                            });
                        }

                    });
                });
                // GetActiveLayer('default', '');

                //Setting activelayer and Overview layer
                $('#activelayer').val(data.activelayer);
                $('#overlaymap').val(data.overlaymap);

                jQuery.ajax({
                    url: selectedItem + "/" + _name + "/user" + "?" + token,
                    async: false,
                    success: function (users) {
                        jQuery.each(users, function (i, value) {
                            //jQuery('#'+value).attr('checked', true);
                            $("input[id='" + value + "']").attr('checked', true);
                            jQuery(".tr-" + value).show();
                        });

                    }
                });
            },
            cache: false
        });
    } else {
        jQuery("#ProjectTemplateForm").tmpl(null, {}).appendTo("#projectGeneralBody");
        $("#projectGeneralBody").i18n();
        jQuery("#ProjectTemplateLayergroup").tmpl(null, {}).appendTo("#projectLayergroupBody");
        jQuery("#projectConfigurationTemplate").tmpl(null, {}).appendTo("#projectConfigurationBody");
        $("#projectConfigurationBody").i18n();
        jQuery("#ProjectTemplateDisclaimer").tmpl(null, {}).appendTo("#projectDisclaimerBody");
        $("#projectDisclaimerBody").i18n();
        // add value for project configuration end 
        jQuery("#projectLayergroupBody").empty();
        jQuery("#ProjectTemplateLayergroup").tmpl(proj_layerGroup, {}).appendTo("#projectLayergroupBody");
        jQuery("#projectBaselayerBody").empty();
        jQuery("#ProjectTemplateBaselayer").tmpl(proj_baselayer, {}).appendTo("#projectBaselayerBody");

        //hide all base layer
        jQuery("#projectBaselayerBody tr").hide();
        $("#lyr_type").val('Google');
        populatebaselayer('Google');
        jQuery("#projectUserList").empty();

        jQuery("#ProjectTemplateUser").tmpl(userroledata).appendTo("#projectUserList");
        jQuery('#name').removeAttr('readonly');
        jQuery('#numzoomlevels').val(18);


        jQuery.each(proj_country, function (i, value) {
            jQuery("#countryId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
        });


        jQuery.each(proj_units, function (i, value) {
            jQuery("#project_unit").append(jQuery("<option></option>").attr("value", value.unitid).text(value.unitEn));
        });

        jQuery.each(proj_formats, function (i, value) {
            jQuery("#project_outputFormat").append(jQuery("<option></option>").attr("value", value.documentformatid).text(value.documentformatEn));
        });

        jQuery.each(proj_projection, function (i, value) {
            jQuery("#projection_code").append(jQuery("<option></option>").attr("value", value.projectionid).text(value.projection));
            jQuery("#displayProjection_code").append(jQuery("<option></option>").attr("value", value.projectionid).text(value.projection));
        });


        // jQuery("#temporaryAdjDiv").empty();
        //jQuery("#temporaryHamletDiv").empty();
    }

    $("#vcmeetingdate").live('click', function () {
        $(this).datepicker('destroy').datepicker({dateFormat: 'yy-mm-dd'}).focus();
    });
    jQuery("#project_accordion").show();
    jQuery("#project_accordion").accordion({fillSpace: true});
    jQuery("#project_btnSave").show();
    jQuery("#project_btnBack").show();
};

function changeActiveValue(_this) {
    if (_this.checked) {
        jQuery('#active').val("true");
    } else {
        jQuery('#active').val("false");
    }
}

function changeCosmeticValue(_this) {
    if (_this.checked) {
        jQuery('#cosmetic').val("true");
    } else {
        jQuery('#cosmetic').val("false");
    }

}
function toggleDisclaimer(_this) {
    if (_this.checked) {
        jQuery('#Disclaimer').css("visibility", "visible");
    } else {
        jQuery('#Disclaimer').css("visibility", "hidden");
    }
}

function populateActivelayer(_this) {
    if (_this.checked) {
        GetActiveLayer('add', _this.value);
    } else {
        GetActiveLayer('remove', _this.value);
    }
}

function GetActiveLayer(type, selectedGroup) {
    if (type == 'add' || type == 'remove') {
        jQuery.ajax({
            url: "layergroupName/" + selectedGroup + "?" + token,
            async: false,
            success: function (data) {
                //for (var j = 0; j < data[0].layers.length; j++) {
                for (var j = 0; j < data[0].layerLayergroups.length; j++) {
                    if (type == 'add') {
                        jQuery('#activelayer').empty();
                        jQuery('#overlaymap').empty();
                        if (!isTilecache(data[0].layerLayergroups[j].layers.alias)) { //data[0].layerLayergroups[0].layers   if (!isTilecache(data[0].layers[j].layer)) {
                            jQuery('#activelayer').append(jQuery("<option></option>").attr("value", data[0].layerLayergroups[j].layers.alias).text(data[0].layerLayergroups[j].layers.alias));
                        }
                        jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", data[0].layerLayergroups[j].layers.alias).text(data[0].layerLayergroups[j].layers.alias));
                    } else if (type == 'remove') {
                        jQuery("#activelayer option[value=" + data[0].layerLayergroups[j].layers.alias + "]").remove();
                        jQuery("#overlaymap option[value=" + data[0].layerLayergroups[j].layers.alias + "]").remove();
                    }
                }
            }
        });
    } else if (type == 'default') {
        jQuery('#activelayer').empty();
        jQuery('#overlaymap').empty();

        jQuery('#activelayer').append(jQuery("<option></option>").attr("value", '').text($.i18n("gen-please-select")));
        jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", '').text($.i18n("gen-please-select")));

        $('#projectLayergroupBody input[type="checkbox"]:checked').each(function () {
            var lyrGrp = $(this).val();
            jQuery.ajax({
                url: "layergroupName/" + lyrGrp + "?" + token,
                async: false,
                success: function (data) {
                    if (data.length > 0)
                    {
                        $.each(data, function (i, ob) {
                            $.each(ob, function (ind, obj) {
                                if (ind == "layerLayergroups") {
                                    $.each(obj, function (ind1, obj1) {
                                        if (ind1 == "layers") {
                                            $.each(obj1, function (ind2, obj2) {
                                                if (ind1 == "alias") {
                                                    jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", obj2).text(obj2));
                                                }
                                            });
                                        }

                                    });
                                }

                            });
                        });
                    }


                }
            });
        });
    }

    if (type == 'add') {
        optVals.push(selectedGroup);
    } else if (type == 'remove') {
        optVals.pop(selectedGroup);
    } else {
        $('#projectLayergroupBody input[type="checkbox"]:checked').each(function () {
            optVals.push($(this).val());
        });
    }

    jQuery('#activelayer').empty();
    jQuery('#overlaymap').empty();

    jQuery('#activelayer').append(jQuery("<option></option>").attr("value", '').text($.i18n("gen-please-select")));
    jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", '').text($.i18n("gen-please-select")));

    for (var i = 0; i < optVals.length; i++) {
        var lyrGrp = optVals[i];
        jQuery.ajax({
            url: "layergroupName/" + lyrGrp,
            async: false,
            success: function (data) {
                //for (var j = 0; j < data[0].layers.length; j++)
                //layers[j].layer

                for (var j = 0; j < data[0].layerLayergroups.length; j++)
                {

                    //jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", data[0].layerLayergroups[j].layers.alias).text(data[0].layerLayergroups[j].layers.alias));
                    jQuery('#activelayer').append(jQuery("<option></option>").attr("value", data[0].layerLayergroups[j].layers.alias).text(data[0].layerLayergroups[j].layers.alias));
                    jQuery('#overlaymap').append(jQuery("<option></option>").attr("value", data[0].layerLayergroups[j].layers.alias).text(data[0].layerLayergroups[j].layers.alias));
                }
            }
        });
    }
}

var saveProjectData = function () {
    jQuery.ajax({
        type: "POST",
        url: "project/create" + "?" + token + "&emailid=" + useremail,
        data: jQuery("#projectForm").serialize(),
        success: function (result) {
            if (result == 'ProjectAdded') {
                jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
                //return false;
                displayRefreshedProject();
            }

            if (result == 'DuplicateName') {
                jAlert($.i18n("err-proj-exists"), $.i18n("err-alert"));
            }

            if (result == 'EnterName') {
                jAlert($.i18n("err-enter-proj-name"), $.i18n("err-alert"));
            }

            if (result == 'false') {
                jAlert($.i18n("err-not-saved"), $.i18n("err-alert"));
                //return false;
                displayRefreshedProject();
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('err.Message');
        }
    });

};

var saveProject = function () {
    checkAdjEdit = false;
    checkHamletEdit = false;
    $("#projectForm").validate({
        ignore: [],
        rules: {
            "name": "required",
            "activelayer": "required",
            "projection_code": "required",
            "displayProjection_code": "required",
            "project_unit": "required",
            "maxextent": "required",
            "minextent": "required",
            "restrictedextent": "required",
            "numzoomlevels": {
                required: true,
                digits: true
            },
            "displayProjection.code": "required",
            "project_outputFormat": "required",
            "overlaymap": "required",
            "countryId": "required",
            "regionId": "required",
            "districtId": "required",
            "CommuneId": "required",
            "regioncode": "required"
        },
        messages: {
            "name": $.i18n("err-enter-name"),
            "projection_code": $.i18n("err-enter-proj"),
            "displayProjection_code": $.i18n("err-enter-display-proj"),
            "project_unit": $.i18n("err-select-layer-unit"),
            "maxextent": $.i18n("err-enter-max-ext"),
            "minextent": $.i18n("err-enter-min-ext"),
            "restrictedextent": $.i18n("err-enter-restr-ext"),
            "numzoomlevels": {
                required: $.i18n("err-enter-zoom-levels"),
                digits: $.i18n("err-enter-valid-num")
            },
            "displayProjection.code": $.i18n("err-enter-display-proj"),
            "project_outputFormat": $.i18n("err-enter-out-format"),
            "activelayer": $.i18n("err-select-active-layer"),
            "overlaymap": $.i18n("err-select-overview-layer"),
            "countryId": $.i18n("err-select-country"),
            "regionId": $.i18n("err-select-region"),
            "districtId": $.i18n("err-select-province"),
            "CommuneId": $.i18n("err-select-commune"),
            "regioncode": $.i18n("err-enter-region-code")
        }
    });

    if ($("#projectForm").valid()) {

        saveProjectData();

    }
    if (!$("#projectForm").valid() == true) {
        jAlert($.i18n("err-fill-details-intabs"), $.i18n("err-alert"));
    }
};

var deleteProject = function (_projectName, _projectId) {

    jConfirm($.i18n("viewer-confirm-delete-object", _projectName), $.i18n("gen-confirm-delete"), function (response) {
        if (response) {
            jQuery.ajax({
                url: "project/delete/" + _projectId + "?" + token,
                success: function () {
                    jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
                    //back to the list page 
                    displayRefreshedProject();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('err.Message');
                }
            });
        }

    });

}

jQuery(function () {
    jQuery("#addLayerGroup").live('click', function () {
        jQuery("#layerGroupList  option:selected").appendTo("#addedLayerGroupList");
    });

    jQuery("#removeLayerGroup").live('click', function () {
        jQuery("#addedLayerGroupList option:selected").appendTo("#layerGroupList");
    });

    jQuery("#upLayerGroup").live('click', function () {
        jQuery('#addedLayerGroupList option:selected').each(function () {
            var newPos = jQuery('#addedLayerGroupList option').index(this) - 1;
            if (newPos > -1) {
                jQuery('#addedLayerGroupList option').eq(newPos).before("<option value='" + jQuery(this).val() + "' selected='selected'>" + jQuery(this).text() + "</option>");
                jQuery(this).remove();
            }
        });
    });

    jQuery("#downLayerGroup").live('click', function () {
        var countOptions = jQuery('#addedLayerGroupList option').size();
        jQuery('#addedLayerGroupList option:selected').each(function () {
            var newPos = jQuery('#addedLayerGroupList option').index(this) + 1;
            if (newPos < countOptions) {
                jQuery('#addedLayerGroupList option').eq(newPos).after("<option value='" + jQuery(this).val() + "' selected='selected'>" + jQuery(this).text() + "</option>");
                jQuery(this).remove();
            }
        });
    });
});

function isTilecache(layeralias) {
    var response = false;
    jQuery.ajax({
        url: "layer/" + layeralias + "?" + token,
        async: false,
        success: function (layer) {
            if (layer.layertype.name == 'Tilecache') {
                response = true;
            } else {
                response = false;
            }
        }
    });
    return response;
}

jQuery(function () {
    $(".lgup,.lgdown").live('click', function () {
        var row = $(this).parents("tr:first");
        if ($(this).is(".lgup")) {
            row.insertBefore(row.prev());
        } else {
            row.insertAfter(row.next());
        }
    });

});

function populatebaselayer(_type) {

    jQuery("#lyr_type").val(_type);
    if (_type == 'Google') {
        jQuery('input[id^="Bing_"]').attr('checked', false);
        jQuery('input[id^="Open_"]').attr('checked', false);
        jQuery('input[id^="MapQuest"]').attr('checked', false);
    } else if (_type == 'Bing') {
        jQuery('input[id^="Google_"]').attr('checked', false);
        jQuery('input[id^="Open_"]').attr('checked', false);
        jQuery('input[id^="MapQuest"]').attr('checked', false);
    } else if (_type == 'Open') {
        jQuery('input[id^="Google_"]').attr('checked', false);
        jQuery('input[id^="Bing_"]').attr('checked', false);
        jQuery('input[id^="MapQuest"]').attr('checked', false);
    } else if (_type == 'MapQuest') {
        jQuery('input[id^="Google_"]').attr('checked', false);
        jQuery('input[id^="Bing_"]').attr('checked', false);
        jQuery('input[id^="Open_"]').attr('checked', false);
    }

    jQuery("#projectBaselayerBody tr").hide();
    jQuery('tr[id^=tr-' + _type + '_]').show();
}

function toggleUserChecked(status) {
    $(".userCheckbox").each(function () {
        $(this).attr("checked", status);
    });

    if (status) {
        $('[class^="tr-"]').show();
    } else {
        $('[class^="tr-"]').hide();
    }
}

function manageProjLink(_this) {
    if (_this.checked == true) {
        jQuery('.tr-' + _this.value).show();
    } else {
        jQuery('.tr-' + _this.value).hide();
    }
}

function getRegionOnCountryChange(id) {

    $("#districtId").empty();
    jQuery("#districtId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#CommuneId").empty();
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#regionId").empty();
    jQuery("#regionId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));


    if (id != '') {
        jQuery.ajax({
            url: "projectregion/" + id,
            async: false,
            success: function (regiondata) {
                var proj_region = regiondata;
                jQuery.each(proj_region, function (i, value) {
                    jQuery("#regionId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });
            }
        });
    }



}

function getDistrictOnRegionChange(id) {

    $("#CommuneId").empty();
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#districtId").empty();
    jQuery("#districtId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));



    if (id != '') {

        jQuery.ajax({
            url: "projectdistrict/" + id,
            async: false,
            success: function (regiondata) {
                var proj_region = regiondata;
                jQuery.each(proj_region, function (i, value) {
                    jQuery("#districtId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });
            }
        });
    }


}



function getCommuneOnProvinceChange(id) {

    $("#CommuneId").empty();
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));



    if (id != '') {


        jQuery.ajax({
            url: "projectvillage/" + id,
            async: false,
            success: function (regiondata) {
                var proj_coomune = regiondata;
                jQuery.each(proj_coomune, function (i, value) {
                    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });
            }
        });
    }


}
function getHamletOnVillageChange(id) {

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

    if (id != '') {

        jQuery.ajax({
            url: "projecthamlet/" + id,
            async: false,
            success: function (regiondata) {
                var proj_coomune = regiondata;
                jQuery.each(proj_coomune, function (i, value) {
                    jQuery("#placeId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });
            }
        });
    }


}


function addTempAdjudicator(id) {
    adjudicatorDialog = $("#adjudicator-dialog-form").dialog({
        autoOpen: false,
        height: 200,
        width: 232,
        resizable: false,
        modal: true,
        buttons: {
            "Save": function ()
            {
                validateAdjudicator(id);
            }
        }
    });
    if (id !== 'new')
        $('#adjudicator_name').val(adjList[id].adjudicatorName);
    else
        $('#adjudicator_name').val('');

    adjudicatorDialog.dialog("open");
}

function uploadSignature(name) {
    // Reset form
    $("#fileSignature").val('');

    signatureDialog = $("#signature-dialog-form").dialog({
        autoOpen: false,
        height: 200,
        width: 300,
        resizable: false,
        modal: true,
        buttons: {
            "Upload": function () {
                if ($("#fileSignature").val() === '') {
                    jAlert($.i18n("err-select-file"));
                    return;
                }

                var formData = new FormData();
                var flag = true;

                $.each($('#fileSignature')[0].files[0], function (ind3, obj3) {
                    if (ind3 == "type") {

                        if (obj3 == "image/png" || obj3 == "image/jpeg" || obj3 == "image/gif") {
                            flag = true;
                            $("#extention").val(obj3);
                            return false;

                        } else {
                            flag = true;
                            jAlert($.i18n("err-file-type"));
                            return false;
                        }
                    }

                });


                if (flag)
                {

                    formData.append("signature", $('#fileSignature')[0].files[0]);
                    $.ajax({
                        type: "POST",
                        url: "project/uploadsignature",
                        mimeType: "multipart/form-data",
                        contentType: false,
                        cache: false,
                        processData: false,
                        data: formData,
                        success: function (data) {
                            if (data) {
                                signatureDialog.dialog("close");
                                showSignature(name, data);
                            } else {
                                jAlert($.i18n("err-failed-upload"));
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            jAlert($.i18n("err-failed-upload"));
                        }
                    });
                }


            }
        }
    });

    signatureDialog.dialog("open");
}

function showSignature(name, fileName) {
    // Check if file exists
    result = false;

    if (fileName !== '') {
        $.ajax({
            type: 'GET',
            async: false,
            url: "project/signatureexists/" + fileName,
            success: function (response) {
                if (response) {
                    $("#img" + name).attr('src', '/mast/studio/project/getsignature/' + fileName);
                    $("#h" + name).val(fileName).trigger('change');
                    showHideSignature(false, name);
                    result = true;

                }
            }
        });
    }

    // Otherwise show add signature link
    if (!result) {
        $("#h" + name).val('').trigger('change');
        showHideSignature(true, name);
    }
}

function deleteSignature(name) {
    jConfirm($.i18n("gen-confirm-question"), $.i18n("gen-confirmation"), function (response) {
        if (response) {
            $.ajax({
                type: 'GET',
                async: false,
                url: "project/deletesignature/" + $("#h" + name).val(),
                success: function (response) {
                    if (response) {
                        $("#h" + name).val('').trigger('change');
                        showHideSignature(true, name);
                    } else {
                        jAlert($.i18n("err-not-deleted"));
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jAlert($.i18n("err-not-deleted"));
                }
            });
        }
    });
}

function showHideSignature(hide, name) {
    if (hide) {
        $("#img" + name).hide();
        $("#linkDelete" + name).hide();
        $("#linkAdd" + name).show();
    } else {
        $("#img" + name).show();
        $("#linkDelete" + name).show();
        $("#linkAdd" + name).hide();
    }
}

function RunTopologyChecks()
{
    activeProject = "New";
    $.ajax({
        type: "GET",
        url: "landrecords/runtopology/" + activeProject,
        async: false,
        success: function (result)
        {
            if (result === "OK")
            {
                //jAlert("Topology ran scuccessfully");  
                jAlert($.i18n("admin-topology-run-success"), $.i18n("gen-info"));
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-failed-run-topology"));
        }
    });
}
