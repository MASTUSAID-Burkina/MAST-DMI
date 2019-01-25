
var selectedItem = null;
var dataList = null;
var projList = null;
var spatialList = null;
var socialtenureList = null;
var associatednaturalPersonList = null;
var genderList = null;
var maritalList = null;
var tenuretypeList = null;
var multimediaList = null;
var educationsList = null;
var landUserList = null;
var tenureclassList = null;
var occtypeList = null;
var workflowhistoryList = null;
var socialEditTenureList = null;
var project = null;
var sourceDocList = null;
var usinId = null;
var read = null;
var soilQualityList = null;
var aquisitiontype = null;
var typeofLandList = null;
var slopeList = null;
var groupTypeList = null;
var attributeList = null;
var vectors = null;
var DeletedNaturalList = null;
var DeletedNonNaturalList = null;
var deletedAdminList = null;
var ProjectAreaList = null;
var personList = null;
var ID = null;
var year = null;
var activeProject = "";
var Personusin = "";
var URL = null;
var landId = null;
var resultDeleteNatural = null;
var records_from = 0;
var totalRecords = null;
var searchRecords = null;
var workflowRecords = null;
var eduList = null;
var naturalPerson_gid = null;
var administratorID = null;
var adminDataList = null;
var checkNewNatural = false;
var hamletList = null;
var person_subtype = null;
var selectedHamlet = null;
var deceasedPersonList = null;
var validator = null;
var adjudicatorList = null;
var claimTypes = null;
var acquisitionTypes = null;
var relationshipTypes = null;
var claimType = null;
var idTypesList = null;
var docTypesList = null;
var editList = null;
var disputeTypesList = null;
var disputeStatusList = null;
var _transactionid = 0;
var _parcelNumber = 0;
var RESPONSE_OK = "OK";
var CLAIM_TYPE_NEW = "newClaim";
var CLAIM_TYPE_EXISTING = "existingClaim";
var CLAIM_TYPE_DISPUTED = "dispute";
var CLAIM_TYPE_UNCLAIMED = "unclaimed";
var CLAIM_STATUS_NEW = 1;
var CLAIM_STATUS_APPROVED = 2;
var CLAIM_STATUS_VALIDATED = 3;
var CLAIM_STATUS_REFERRED = 4;
var CLAIM_STATUS_DENIED = 5;
var landRecordsInitialized = true;
var workFlowLst = null;
var actionList = null;

var communeList = null;
var titleTypes = null;
var natureOfPowers = null;
var appNatures = null;
var villages = null;

function LandRecords(_selectedItem) {
    if (projList !== null && projList !== "") {
        return;
    }

    jQuery.ajax({
        url: "landrecords/spatialunit/totalRecord/" + activeProject,
        async: false,
        success: function (data) {
            totalRecords = data;
        }

    });


    jQuery.ajax({
        url: "landrecords/status/",
        async: false,
        success: function (data) {
            statusList = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/tenuretype/",
        async: false,
        success: function (data) {
            tenuretypeList = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/claimtypes/",
        async: false,
        success: function (data) {
            claimTypes = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/workflow/",
        async: false,
        success: function (data) {
            workFlowLst = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/spatialunit/commune/" + activeProject,
        async: false,
        success: function (data) {
            communeList = data;
        }
    });

    displayRefreshedLandRecords(_selectedItem);
}

function showSaveButton(show) {
    if (show && !$("#parcelsavebutton").prop("disabled")) {
        $("#parcelsavebutton").show();
    } else {
        $("#parcelsavebutton").hide();
    }
}


function displayRefreshedLandRecords(_selectedItem) {
    searchRecords = null
    workflowRecords = null
    selectedItem = _selectedItem;
    URL = "landrecords/spatialunit/landrecord/default/" + 0 + "/" + Global.LANG;
    if (activeProject !== null && activeProject !== "") {
        URL = "landrecords/spatialunit/landrecord/" + activeProject + "/" + 0 + "/" + Global.LANG;
    }
    jQuery.ajax({
        url: URL,
        async: false,
        success: function (data) {
            dataList = data;
            records_from = 0;
            searchRecords = null;

            jQuery("#landrecords-div").empty();
            jQuery.get("resources/templates/viewer/" + selectedItem + ".html", function (template) {
                jQuery("#landrecords-div").append(template);
                jQuery("#landrecords-div").i18n();
                jQuery('#landRecordsFormdiv').css("visibility", "visible");
                jQuery("#landRecordsTable").show();

                //add for applicatyon statu and claim type
                jQuery("#claim_type").empty();
                jQuery("#app_type").empty();

                jQuery("#app_type").append(jQuery("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
                jQuery("#claim_type").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));
                jQuery("#app_status").append(jQuery("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

                jQuery.each(tenuretypeList, function (i, tenureType) {
                    var displayName = tenureType.landsharetype;
                    if (Global.LANG === "en") {
                        displayName = tenureType.landsharetypeEn;
                    }
                    jQuery("#app_type").append(jQuery("<option></option>").attr("value", tenureType.landsharetypeid).text(displayName));
                });

                jQuery.each(claimTypes, function (i, claimType) {
                    jQuery("#claim_type").append(jQuery("<option></option>").attr("value", claimType.code).text(claimType.name));
                });

                jQuery.each(statusList, function (i, statusType) {
                    var displayName = statusType.workflowStatus;
                    if (Global.LANG === "en") {
                        displayName = statusType.applicationstatus_en;
                    }
                    jQuery("#app_status").append(jQuery("<option></option>").attr("value", statusType.workflowStatusId).text(displayName));
                });

                jQuery("#newtbl").empty();
                jQuery("#newtb2").empty();

                var wfStepsNew = [];
                var wfStepsExisting = [];

                if (workFlowLst !== null) {
                    $.each(workFlowLst, function (i, wfStep) {
                        if (wfStep.claimType === 1) {
                            wfStepsNew.push(wfStep);
                        } else {
                            wfStepsExisting.push(wfStep);
                        }
                    });
                }

                if (roles === "ROLE_DPI") {
                    $('#vtab').removeClass("vtab");
                    $('#workflowDiv').hide();
                } else {
                    jQuery("#workFlowTemplateNew").tmpl(wfStepsNew).appendTo("#newtbl");
                    jQuery("#workFlowTemplateExisting").tmpl(wfStepsExisting).appendTo("#newtb2");

                    $("#workflow-accordion").accordion();
                }

                jQuery("#landRecordsRowData").empty();

                if (dataList.length != 0 && dataList.length != undefined) {
                    jQuery("#landRecordsAttrTemplate").tmpl(dataList).appendTo("#landRecordsRowData");
                    $("#landRecordsRowData").i18n();
                    $('#records_from').val(records_from + 1);
                    $('#records_to').val(totalRecords);
                    if (records_from + 10 <= totalRecords)
                        $('#records_to').val(records_from + 10);
                    $('#records_all').val(totalRecords);
                } else {
                    $('#records_from').val(0);
                    $('#records_to').val(0);
                    $('#records_all').val(0);
                }

                $("#landRecordsTable").trigger("update");

                var dtFormat = "yy-mm-dd";
                var dtDisplyaFormat = "yyyy-mm-dd";
                $(".datepicker").datepicker({dateFormat: dtFormat});
                $(".datepicker").attr("placeholder", dtDisplyaFormat.toUpperCase());

            });
        }
    });
}

function displayRefreshedTenure() {
    var id = editList[0].usin;
    jQuery.ajax({
        url: "landrecords/socialtenure/" + id,
        async: false,
        success: function (data) {
            jQuery("#tenureRowData").empty();
            if (data != "") {
                jQuery("#tenureinfoTemplate").tmpl(data).appendTo("#tenureRowData");
            }
        }
    });
}

function personwithinterest(usin, editable) {
    landId = usin;
    loadPOIs(editable);
}

function deceasedperson(usin)
{
    jQuery.ajax({
        url: "landrecords/deceasedperson/" + usin,
        async: false,
        success: function (data) {
            deceasedPersonList = data;
            jQuery("#deceasedRowData").empty();
            if (deceasedPersonList.length != 0 && deceasedPersonList.length != undefined) {
                jQuery("#deceasedTemplate").tmpl(deceasedPersonList).appendTo("#deceasedRowData");
                $("#deceasedRowData").i18n();
            }
            jQuery("#hideTable").show();
        }
    });
}

function Person(usin, editable) {
    Personusin = usin;
    FillPersonDataNew(editable);
}

function editAttributeNew(usin, statusId, editable) {
    editAttribute(usin, editable);
}

function disputes(id)
{
    landId = id;
    loadDisputeForEditing();
}

function disputePerson(id) {
    landId = id;
    loadDisputePersonForEditing();

}

function media(id) {
    landId = id;
    loadMultimediaForEditing();

}

function editAttribute(id, editable) {
    disputes(id);
    disputePerson(id);
    landDocs(id);
    mapCoordinatesUrl(id);

    $("#pnlPoi").hide();

    jQuery.ajax({
        url: "landrecords/tenuretype/",
        async: false,
        success: function (data) {
            tenuretypeList = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/tenureclass/",
        async: false,
        success: function (data) {
            tenureclassList = data;
        }
    });

    if (natureOfPowers === null) {
        jQuery.ajax({
            url: "landrecords/powernature/",
            async: false,
            success: function (data) {
                natureOfPowers = data;
            }
        });
    }

    if (appNatures === null) {
        jQuery.ajax({
            url: "landrecords/appnature/",
            async: false,
            success: function (data) {
                appNatures = data;
            }
        });
    }

    if (genderList === null) {
        jQuery.ajax({
            url: "landrecords/gendertype/",
            async: false,
            success: function (data) {
                genderList = data;
            }
        });
    }

    if (titleTypes === null) {
        jQuery.ajax({
            url: "landrecords/titletype/",
            async: false,
            success: function (data) {
                titleTypes = data;
            }
        });
    }

    if (maritalList === null) {
        jQuery.ajax({
            url: "landrecords/maritalstatus/",
            async: false,
            success: function (data) {
                maritalList = data;
            }
        });
    }

    jQuery.ajax({
        url: "landrecords/landusertype/",
        async: false,
        success: function (data) {
            landUserList = data;
        }
    });
    
    if (villages === null) {
        jQuery.ajax({
            url: "landrecords/projectvillages/" + activeProject,
            async: false,
            success: function (data) {
                villages = data;
            }
        });
    }

    $("#primary").val(id);
    $('#liNonNatural').hide();
    $('#liNatural').hide();
    $("#btnNewTenure").show();
    $("#btnNewNonNatural").show();
    $("#btnAddExistingPerson").hide();
    $(".addRepresentative").hide();
    $("#divRegisterDispute").show();

    $("#tenure_type").empty();
    $("#cbxAppNature").empty();
    $("#cbxTitleType").empty();

    jQuery.each(tenuretypeList, function (i, tenureobj) {
        var tenureDisplayName = tenureobj.landsharetype;
        if (Global.LANG === "en") {
            tenureDisplayName = tenureobj.landsharetypeEn;
        }
        jQuery("#tenure_type").append(jQuery("<option></option>").attr("value", tenureobj.landsharetypeid).text(tenureDisplayName));
    });

    jQuery("#cbxAppNature").append(jQuery("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    jQuery("#cbxTitleType").append(jQuery("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

    if (appNatures !== null) {
        jQuery.each(appNatures, function (i, obj) {
            var displayName = obj.name;
            if (Global.LANG === "en") {
                displayName = obj.nameEn;
            }
            jQuery("#cbxAppNature").append(jQuery("<option></option>").attr("value", obj.id).text(displayName));
        });
    }

    if (titleTypes !== null) {
        jQuery.each(titleTypes, function (i, obj) {
            var displayName = obj.nameOtherLang;
            if (Global.LANG === "en") {
                displayName = obj.name;
            }
            jQuery("#cbxTitleType").append(jQuery("<option></option>").attr("value", obj.id).text(displayName));
        });
    }

    associatednaturalPersonList = null;
    jQuery.ajax({
        url: "landrecords/editattribute/" + id,
        async: false,
        success: function (data) {
            editList = data;
            $("#other_use").val("");
            $("#txtTitleNumber").val("");
            $("#txtTitleRegDate").val("");
            $("#cbxVillage").empty();
            $("#txtRegNumber").val("");
            $("#txtParcelNumber").val("");
            $("#txtReconRightDate").val("");
            $("#txtContraDate").val("");
            $("#txtAppIssueDate").val(data[0].applicationdate);
            $("#neighbor_north").val(data[0].neighborNorth);
            $("#neighbor_south").val(data[0].neighborSouth);
            $("#neighbor_east").val(data[0].neighborEast);
            $("#neighbor_west").val(data[0].neighborWest);
            jQuery("#existing_use").empty();
            
            if (data[0].other_use !== "") {
                jQuery("#other_use").val(data[0].other_use);
            }

            jQuery("#area").val(((data[0].area) * area_constant).toFixed(2));
            $("#lblApfrNum").text("--");

            jQuery.each(landUserList, function (i, landuseobj) {
                var displayName = landuseobj.landusetype;
                if (Global.LANG === "en") {
                    displayName = landuseobj.landusetypeEn;
                }
                jQuery("#existing_use").append(jQuery("<option></option>").attr("value", landuseobj.landusetypeid).text(displayName));
            });
            
            jQuery.each(villages, function (i, village) {
                var displayName = village.name;
                if (Global.LANG === "en") {
                    displayName = village.nameEn;
                }
                jQuery("#cbxVillage").append(jQuery("<option></option>").attr("value", village.hierarchyid).text(displayName));
            });

            if (data[0].laSpatialunitgroupHierarchy5 !== null) {
                jQuery("#cbxVillage").val(data[0].laSpatialunitgroupHierarchy5.hierarchyid);
            } else {
                jQuery("#cbxVillage").val("");
            }
            
            if (data[0].noaId !== null) {
                jQuery("#cbxAppNature").val(data[0].noaId);
            }

            if (data[0].rights !== null && data[0].rights.length > 0) {
                $.each(data[0].rights, function (i, landRight) {
                    if (landRight.isactive && landRight.persontypeId === 1) {
                        if (landRight.certTypeid !== null) {
                            jQuery("#cbxTitleType").val(landRight.certTypeid);
                        }
                        if (landRight.certIssueDate !== null) {
                            jQuery("#txtTitleRegDate").val(landRight.certIssueDate);
                        }
                        if (landRight.certNumber !== null) {
                            jQuery("#txtTitleNumber").val(landRight.certNumber);
                            $("#lblApfrNum").text(landRight.certNumber);
                        }
                        jQuery("#tenure_type").val(landRight.shareTypeId);
                        if (landRight.shareTypeId === 8) {
                            $("#pnlPoi").show();
                        }
                    }
                });
            }

            if (data[0].registrationNum !== null) {
                $("#txtRegNumber").val(data[0].registrationNum);
            }

            if (data[0].parcelExtensions !== null && data[0].parcelExtensions.length > 0) {
                var parcelExtension = data[0].parcelExtensions[0];
                if (parcelExtension.parcelno !== null) {
                    $("#txtParcelNumber").val(parcelExtension.parcelno);
                }
                if (parcelExtension.recognition_rights_date !== null) {
                    $("#txtReconRightDate").val(parcelExtension.recognition_rights_date);
                }
                if (parcelExtension.parcelno !== null) {
                    $("#txtContraDate").val(parcelExtension.contradictory_date);
                }
            }

            if (data[0].landusetypeid !== null) {
                jQuery("#existing_use").val(data[0].landusetypeid.split(","));
            } else {
                jQuery("#existing_use").val("");
            }
                        
            $('#existing_use').multiselect({
                columns: 1,
                placeholder: $.i18n("gen-please-select")
            });
            
            $('#existing_use').multiselect('reload');

            $("#spatialid").text(_parcelNumber);

            $("#claimType").text(getDisplayName(claimTypes, data[0].claimtypeid, "code", "name", "nameOtherLang"));
            $("#lblAppNum").text("--");
            $("#lblPvNum").text("--");

            if (data[0].claimtypeid === 2) {
                $("#trExitingTitleHeader").show();
                $("#trExitingTitleData").show();
            } else {
                $("#trExitingTitleHeader").hide();
                $("#trExitingTitleData").hide();
            }

            if (!isEmpty(data[0].appNum)) {
                $("#lblAppNum").text(data[0].appNum);
            }
            if (!isEmpty(data[0].pvNum)) {
                $("#lblPvNum").text(data[0].pvNum);
            }
        }
    });

    editAttrDialog = $("#editattribute-dialog-form").dialog({
        autoOpen: false,
        height: 630,
        closed: false,
        cache: false,
        width: 1000,
        resizable: false,
        modal: true,
        close: function () {
            editAttrDialog.dialog("destroy");
            $("input,select,textarea").removeClass('addBg');
            $('#tabs').tabs("option", "active", $('#tabs a[href="#tabGeneralInfo"]').parent().index());
        },
        buttons: [
            {
                text: $.i18n("gen-close"),
                "id": "info_cancel",
                click: function () {
                    editAttrDialog.dialog("destroy");
                    $('#tabs').tabs("option", "active", $('#tabs a[href="#tabGeneralInfo"]').parent().index());
                    $("input,select,textarea").removeClass('addBg');
                }
            },
            {
                text: $.i18n("gen-save"),
                "id": "parcelsavebutton",
                click: function () {
                    updateattributesGen();
                }
            }
        ]
    });

    Person(id, editable);
    personwithinterest(id, editable);

    editAttrDialog.dialog("open");
    if (editable) {
        $("#parcelsavebutton").prop("disabled", false).show();
        $("#cbxAppNature").prop("disabled", false);
        $("#area").prop("disabled", false);
        $("#txtRegNumber").prop("disabled", false);
        $("#neighbor_north").prop("disabled", false);
        $("#neighbor_south").prop("disabled", false);
        $("#neighbor_east").prop("disabled", false);
        $("#neighbor_west").prop("disabled", false);
        $("#tenure_type").prop("disabled", false);
        $("#existing_use").prop("disabled", false);
        $(".ms-options-wrap button").prop("disabled", false);
        $("#other_use").prop("disabled", false);
        $("#txtParcelNumber").prop("disabled", false);
        $("#txtReconRightDate").prop("disabled", false);
        $("#txtContraDate").prop("disabled", false);
        $("#txtTitleNumber").prop("disabled", false);
        $("#txtTitleRegDate").prop("disabled", false);
        $("#addPoi").show();
        $("#cbxTitleType").prop("disabled", false);
        $("#cbxVillage").prop("disabled", false);
        $("#genmultimediaRowData button").show();
        $("#genmultimediaRowData input").show();
    } else {
        $("#cbxAppNature").prop("disabled", true);
        $("#area").prop("disabled", true);
        $("#txtRegNumber").prop("disabled", true);
        $("#neighbor_north").prop("disabled", true);
        $("#neighbor_south").prop("disabled", true);
        $("#neighbor_east").prop("disabled", true);
        $("#neighbor_west").prop("disabled", true);
        $("#tenure_type").prop("disabled", true);
        $("#existing_use").prop("disabled", true);
        $(".ms-options-wrap button").prop("disabled", true);
        $("#other_use").prop("disabled", true);
        $("#txtParcelNumber").prop("disabled", true);
        $("#txtReconRightDate").prop("disabled", true);
        $("#txtContraDate").prop("disabled", true);
        $("#txtTitleNumber").prop("disabled", true);
        $("#txtTitleRegDate").prop("disabled", true);
        $("#cbxTitleType").prop("disabled", true);
        $("#cbxVillage").prop("disabled", true);
        $("#addPoi").hide();
        $("#genmultimediaRowData button").hide();
        $("#genmultimediaRowData input").hide();
        $("#parcelsavebutton").prop("disabled", true).hide();
    }
}

function isEmpty(obj) {
    return obj === null || typeof obj === 'undefined' || obj === '';
}

function getDisplayName(list, id, listFieldId, listFieldNameEn, listFieldNameOther) {
    var displayName = "";
    if (list !== null && list.length > 0) {
        for (i = 0; i < list.length; i++) {
            if (list[i][listFieldId] + "" === id + "") {
                if (Global.LANG === "en") {
                    displayName = list[i][listFieldNameEn];
                } else {
                    displayName = list[i][listFieldNameOther];
                }
            }
        }
    }
    return displayName;
}

function refreshLandRecords() {
    if (searchRecords !== null) {
        spatialSearch(records_from);
    } else {
        spatialRecords(records_from);
    }
}

function updateattributesGen() {
    $("#editAttributeformID").validate({
        rules: {
            neighbor_north: "required",
            neighbor_south: "required",
            neighbor_east: "required",
            neighbor_west: "required",
            area: {required: true, number: true},
            existing_use: "required",
            cbxVillage: "required"
        },
        messages: {
            neighbor_north: $.i18n("err-enter-neighbour-name"),
            neighbor_south: $.i18n("err-enter-neighbour-name"),
            neighbor_east: $.i18n("err-enter-neighbour-name"),
            neighbor_west: $.i18n("err-enter-neighbour-name"),
            area: {
                required: $.i18n("err-enter-plot-area"),
                number: jQuery.format($.i18n("err-only-numeric"))
            },
            existing_use: $.i18n("err-select-existing-landuse"),
            cbxVillage: $.i18n("err-select-village")
        }
    });

    if ($("#editAttributeformID").valid()) {
        updateattributes();
    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function updateattributes() {
    //  var length_gen = attributeList.length;
    jQuery("#general_length").val(0);
    var selected = $("#existing_use option:selected");
    var selected_use = "";
    selected.each(function () {
        selected_use += $(this).val() + ",";
    });

    if (selected_use.length > 0) {
        $("#existing_hidden").val(selected_use.substring(0, selected_use.length - 1));
    } else {
        $("#existing_hidden").val("");
    }
    jQuery.ajax({
        type: "POST",
        url: "landrecords/updateattributes",
        data: jQuery("#editAttributeformID").serialize(),
        success: function (result) {
            if (result) {
                jAlert($.i18n("reg-attrubutes-updated"), $.i18n("gen-info"));
            } else {
                jAlert($.i18n("err-not-saved"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-failed-handling-request"));
        }
    });
}

function approveClaim(id) {
    jConfirm($.i18n("reg-confirm-claim-approve"), $.i18n("gen-confirmation"), function (response) {
        if (response) {
            jQuery.ajax({
                type: "GET",
                url: "landrecords/approve/" + id,
                success: function (result) {
                    if (result === "" || result.length === 0) {
                        jAlert($.i18n("reg-claim-approved"), $.i18n("gen-info"));
                        if (searchRecords !== null) {
                            spatialSearch(records_from);
                        } else {
                            spatialRecords(records_from);
                        }
                    } else {
                        showErrors(result);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jAlert($.i18n("err-failed-claim-approve"), $.i18n("err-alert"));
                }
            });
        }
    });
}

function rejectClaim(id) {
    jConfirm($.i18n("reg-confirm-reject"), $.i18n("gen-confirmation"), function (response) {
        if (response) {
            jQuery.ajax({
                type: "GET",
                url: "landrecords/reject/" + id,
                success: function (result) {
                    if (result) {
                        jAlert($.i18n("reg-claim-denied"), $.i18n("err-alert"));
                        if (searchRecords !== null) {
                            spatialSearch(records_from);
                        } else {
                            spatialRecords(records_from);
                        }
                    } else {
                        jAlert($.i18n("err-failed-claim-deny"));
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jAlert($.i18n("err-failed-claim-deny"));
                }
            });
        }
    });
}

function showErrors(errors) {
    if (errors !== null && errors.length > 0) {
        combinedErrors = "";
        for (i = 0; i < errors.length; i++) {
            combinedErrors = combinedErrors + "- " + errors[i] + "<br>"
        }
        jAlert(combinedErrors);
    }
}

function clearSearch() {
    $("#claim_type").val("");
    $("#status_id").val("0");
    $("#transaction_id").val("");
    $("#parce_id").val("");

    displayRefreshedLandRecords("landRecords");
}

function search() {
    workflowRecords = null;
    records_from = 0;
    $("#lang_search").val(Global.LANG);

    jQuery.ajax({
        type: "POST",
        async: false,
        url: "landrecords/search1count" + "/" + activeProject,
        data: jQuery("#landrecordsform").serialize(),
        success: function (result) {
            searchRecords = result;
        }
    });

    spatialSearch(records_from);
}

function updateAttributeNaturalPerson(newPerson) {
    $("#editNaturalPersonformID").validate({
        rules: {
            fname: "required",
            lname: "required",
            idType: "required",
            idNumber: "required",
            dob: "required"
        },
        messages: {
            fname: $.i18n("err-select-firstname"),
            lname: $.i18n("err-select-lastname"),
            idType: $.i18n("err-select-id-type"),
            idNumber: $.i18n("err-enter-idnumber"),
            dob: $.i18n("err-enter-dob")
        },
        ignore: []
    });

    if ($("#editNaturalPersonformID").valid()) {
        if (calculateAge($("#dob").val()) < 18 && $('#person_subType').val() != "3") {
            jAlert($.i18n("err-age-more18"), $.i18n("gen-info"));
            return;
        }

        /*  if ($("#person_subType").val() == "0") {
         alert("Select Owner type");
         return;
         } */

        if ($("#idType").val() == "0") {
            alert($.i18n("err-select-id-type"));
        }

        if ($('#gender').val() == "0") {
            alert($.i18n("err-select-gender"));
            return;
        }

        if ($('#marital_status').val() == "0") {
            alert($.i18n("err-select-marital-status"));
            return;
        }

        if (newPerson) {
            updateNewNaturalPerson();
            naturalPersonDialog.dialog("destroy");
        } else {
            updateNaturalPerson();
        }

    } else {
        jAlert($.i18n("err-fill-details-intabs"), $.i18n("err-alert"));
    }
}


function updateAttributeNonNaturalPerson() {
    $("#editNonNaturalPersonformID").validate({
        rules: {
            institution: "required",
            mobile_no: {
                required: true,
                number: true
            },
            address: "required"
        },
        messages: {
            institution: $.i18n("err-enter-org-name"),
            address: $.i18n("err-enter-address"),
            mobile_no: $.i18n("err-numeric-phonenumber")
        },
        ignore: []
    });

    if ($("#editNonNaturalPersonformID").valid()) {
        if ($('#group_type').val() !== 0) {
            updateNonNaturalPerson();
        } else {
            alert($.i18n("err-select-type"));
        }
    } else {
        jAlert($.i18n("err-fill-details-intabs"), $.i18n("err-alert"));
    }
}

function updateAttributeTenure() {
    $("#edittenureinfoformID").validate({
        rules: {
            tenure_type: "required",
            tenureDuration: "required"
        },
        messages: {
            tenure_type: $.i18n("err-select-tenure-type"),
            tenureDuration: $.i18n("err-enter-occupancy-length")
        }
    });

    if ($("#edittenureinfoformID").valid()) {
        if ($("#tenure_type").val() == 0) {
            alert($.i18n("err-select-right-type"));
        } else if ($("#lstAcquisitionTypes").val() == 0) {
            alert($.i18n("err-select-aquisition-type"));
        } else {
            updateTenure();
        }
    } else {
        jAlert($.i18n("err-fill-details-intabs"), $.i18n("err-alert"));
    }
}

function updateTenure() {
    var id = editList[0].usin;
    var length = attributeList.length;
    jQuery("#tenure_length").val(0);
    if (length !== 0 && length !== undefined)
        jQuery("#tenure_length").val(length);

    jQuery("#projectname_key3").val(project);

    jQuery.ajax({
        type: "POST",
        url: "landrecords/updatetenure",
        data: jQuery("#edittenureinfoformID").serialize(),
        success: function (data) {
            if (data) {
                tenureDialog.dialog("destroy");
                editAttribute(id, true);
                jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
            } else {
                jAlert($.i18n("err-request-not-completed"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });
}


var deleteNatural = function (id, name) {
    var usinid = editList[0].landid;

    if (socialtenureList.length == 1) {
        jAlert($.i18n("err-cant-delete-person"), $.i18n("gen-info"));
    } else {
        jConfirm($.i18n("reg-confirm-person-delete", name), $.i18n("gen-confirm-delete"), function (response) {
            if (response) {
                jQuery.ajax({
                    type: 'GET',
                    url: "landrecords/deleteNatural/" + id,
                    success: function (result) {
                        resultDeleteNatural = result;
                        if (resultDeleteNatural == true) {
                            jAlert($.i18n("gen-data-deleted"), $.i18n("gen-info"));
                            Person(usinid);
                        }

                        if (resultDeleteNatural == false) {
                            jAlert($.i18n("err-cant-delete-used-by-proj"), $.i18n("err-alert"));
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                });
            }
        });
    }
};

var deleteNonNatural = function (id, name) {
    var usinid = editList[0].landid;
    jConfirm($.i18n("reg-confirm-org-delete", name), function (response) {
        if (response) {
            jQuery.ajax({
                type: 'GET',
                url: "landrecords/deletenonnatural/" + id,
                success: function (result) {
                    if (result == true) {
                        jAlert($.i18n("gen-data-deleted"), $.i18n("gen-info"));
                        Person(usinid)
                    }

                    if (result == false) {
                        jAlert($.i18n("err-cant-delete-used-by-proj"), $.i18n("gen-info"));
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jAlert($.i18n("err-request-not-completed"));
                }
            });
        }
    });
};

var deleteTenure = function (id) {
    var usinid = editList[0].usin;
    jConfirm($.i18n("gen-delete-confirmation"), $.i18n("gen-confirm-delete"), function (response) {
        if (response) {
            jQuery.ajax({
                type: 'GET',
                url: "landrecords/deleteTenure/" + id,
                success: function (result) {
                    if (result == true) {
                        jAlert($.i18n("gen-data-deleted"), $.i18n("gen-info"));
                        editAttribute(usinid, true);
                    }

                    if (result == false) {
                        jAlert($.i18n("err-cant-delete-used-by-proj"), $.i18n("gen-info"));
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jAlert($.i18n("err-request-not-completed"));
                }
            });
        }
    });
};


function clearUploadDialog() {
    uploadDialog.dialog("destroy");
    $('#uploaddocumentformID')[0].reset();
}

function viewMultimedia(id) {
    window.open("landrecords/download/" + id, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
}

function viewMultimediaByTransid(id) {

    var flag = false;
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: "landrecords/mediaavail/" + id + "/" + _transactionid + "/" + Personusin,
        success: function (result) {
            if (result == true) {
                flag = true;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });

    if (flag) {
        window.open("landrecords/download/" + id + "/" + _transactionid + "/" + Personusin, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
    } else {

        jAlert($.i18n("err-file-not-found"), $.i18n("err-alert"));
    }
}

function showOnMap(usin, statusId, split, edit) {
    $.ajaxSetup({
        cache: false
    });
    var relLayerName = "Mast:la_spatialunit_land";
    var fieldName = "landid";
    var fieldVal = usin;

    var layer = getLayerByAliesName("spatialUnitLand");
    layer.getSource().clear();

    if (split == "split")
    {

        jQuery.ajax({
            type: 'GET',
            async: false,
            url: "landrecords/splitupdate/" + usin,
            success: function (result) {
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                jAlert($.i18n("err-request-not-completed"));
            }
        });
    }
    zoomToLayerFeature(relLayerName, fieldName, fieldVal, edit);

}
function zoomToLayerFeature(relLayerName, fieldName, fieldVal, edit) {

    var _featureNS;
//Get the Layer object
    var layerName = "spatialUnitLand";
    objLayer = getLayerByAliesName(layerName);

    var _wfsurl = objLayer.values_.url;
    var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
    ;

    //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
    $.ajax({
        url: PROXY_PATH + _wfsSchema,
        async: false,
        success: function (data) {
            _featureNS = data.targetNamespace;

        }
    });


    var _featureTypes = [];
    _featureTypes.push("la_spatialunit_land");
    var _featurePrefix = "Mast";
    featureRequest = new ol.format.WFS().writeGetFeature({
        srsName: 'EPSG:4326',
        featureNS: _featureNS,
        featurePrefix: _featurePrefix,
        featureTypes: _featureTypes,
        outputFormat: 'application/json',
        filter: ol.format.filter.equalTo(fieldName, fieldVal)
    });


    var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
    fetch(_url, {
        method: 'POST',
        body: new XMLSerializer().serializeToString(featureRequest)
    }).then(function (response) {
        return response.json();
    }).then(function (json) {
        var features = new ol.format.GeoJSON().readFeatures(json);
        zoomToAnyFeature(features, edit);
    });


}
function zoomToAnyFeature(geom, edit) {
    if (geom != undefined && geom != null) {


        map.getLayers().forEach(function (layer) {
            if (layer.get('aname') != undefined & layer.get('aname') === 'featureWorkflow') {
                map.removeLayer(layer);
            }
        });


        var highlightStyle = new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: '00008B',
                width: 1
            }),
            fill: new ol.style.Fill({
                color: 'rgba(0,0,139,0.2)'
            })
        });

        var featureOverlay = new ol.layer.Vector({
            name: "featureWorkflow",
            source: new ol.source.Vector(),
            style: highlightStyle,
        });
        featureOverlay.set('aname', "featureWorkflow");
        featureOverlay.getSource().addFeature(geom[0]);
        map.addLayer(featureOverlay)
        //map.getView().fit(featureOverlay.getSource().getExtent(), map.getSize());
        //map.getView().setZoom(9);

        var projection = new ol.proj.Projection({
            code: 'EPSG:4326',
            units: 'degrees',
            axisOrientation: 'neu',
            global: true
        })

        var ext = featureOverlay.getSource().getExtent();
        var center = ol.extent.getCenter(ext);
        var coordMin = ol.proj.fromLonLat([center[0], center[1]], 'EPSG:4326');

        map.getView().animate({center: coordMin, zoom: 16});
        $('#mainTabs').tabs("option", "active", $('#mainTabs a[href="#map-tab"]').parent().index());
        $('#sidebar').show();
        $('#collapse').show();
        if (edit) {
            initEditing();
        }

    } else {

        $('#mainTabs').tabs("option", "active", $('#mainTabs a[href="#landrecords-div"]').parent().index());
        $('#sidebar').hide();
        $('#collapse').hide();
        jAlert($.i18n("err-site-not-found"), $.i18n("err-alert"));

    }
}
function adjudicationFormDialog(usinID, statusId) {
    if (statusId == 5 || statusId == 3) {
        jAlert($.i18n("err-no-adj-for-new"), $.i18n("err-alert"));
    } else {
        adjudicationDialog = $("#adjudication-dialog-form").dialog({
            autoOpen: false,
            height: 200,
            width: 232,
            resizable: false,
            modal: true,
            buttons: {
                "Ok": function () {
                    var lang = "";
                    var selected = $("#radioLang input[type='radio']:checked");
                    if (selected.length > 0) {
                        lang = selected.val();
                    }

                    if (lang != '0') {
                        get_Adjuticator_detail(usinID, lang);
                        adjudicationDialog.dialog("destroy");
                    } else {
                        jAlert($.i18n("err-select-lang"), $.i18n("err-alert"));
                    }
                },
                "Cancel": function () {
                    adjudicationDialog.dialog("destroy");
                }
            },
            close: function () {
                adjudicationDialog.dialog("destroy");
            }
        });

        $('input[type=radio][name=lang]').change(function () {
            if (this.value == 'Sw') {
                $('input:radio[name="lang"][value="Sw"]').prop('checked', true);
            } else if (this.value == 'En') {
                $('input:radio[name="lang"][value="En"]').prop('checked', true);
            }
        });

        $('input:radio[name="lang"][value="Sw"]').prop('checked', true);
        adjudicationDialog.dialog("open");
    }
}

function viewProjectName(project) {
    activeProject = project;
}

function defaultProject() {
    document.location.href = "http://" + location.host + "/mast/viewer/";
}

function updatespatialwork(_type) {
    records_from = 0;
    searchRecords = null;

    if ($('input[name="workflow"]:checked').length === 0) {
        jAlert($.i18n("err-select-filter"));
        return false;
    }

    $("#workflow_lang").val(Global.LANG);
    jQuery.ajax({
        type: 'POST',
        url: "landrecords/spatialunitbyworkflow/count/" + activeProject,
        async: false,
        data: jQuery("#updatebyWorkflow").serialize(),
        success: function (data) {
            workflowRecords = data;
        }

    });

    spatialSearchWorkfow(records_from);
}

function clearFilter() {
    searchRecords = null
    workflowRecords = null
    $(".roleCheckbox").prop("checked", false);
    $("#select_all").prop("checked", false);
    $(".roleCheckboxTrans").prop("checked", false);
    $(".roleCheckboxStatus").prop("checked", false);
    $("#select_all_as").prop("checked", false);

    displayRefreshedLandRecords("landRecords");

}


function previousRecords() {
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    records_from = records_from - 11;
    if (records_from >= 0) {
        if (searchRecords != null) {
            spatialSearch(records_from);
        } else if (workflowRecords != null) {
            spatialSearchWorkfow(records_from);
        } else {
            spatialRecords(records_from);
        }
    } else {
        alert($.i18n("err-no-records"));
    }
}

function nextRecords() {
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    records_from = records_from + 9;

    if (records_from < totalRecords - 1) {
        if (searchRecords != null) {
            if (records_from <= searchRecords - 1)
                spatialSearch(records_from);
            else
                alert($.i18n("err-no-records"));
        } else if (workflowRecords != null) {
            if (records_from <= workflowRecords - 1)
                spatialSearchWorkfow(records_from);
            else
                alert($.i18n("err-no-records"));

        } else {
            spatialRecords(records_from);
        }
    } else {
        alert($.i18n("err-no-records"));
    }
}

function firstRecords() {
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    
    if (records_from > 1) {
        if (searchRecords != null) {
            spatialSearch(0);
        } else if (workflowRecords != null) {
            spatialSearchWorkfow(0);
        } else {
            spatialRecords(0);
        }
    } 
}

function lastRecords() {
    recordsAll = $('#records_all').val();
    recordsAll = parseInt(recordsAll);
    records_from = recordsAll - (recordsAll % 10);

    if (recordsAll > 10) {
        if (searchRecords != null) {
            if (records_from <= searchRecords - 1)
                spatialSearch(records_from);
        } else if (workflowRecords != null) {
            if (records_from <= workflowRecords - 1)
                spatialSearchWorkfow(records_from);
        } else {
            spatialRecords(records_from);
        }
    } 
}

function spatialRecords(records_from) {
    jQuery.ajax({
        url: "landrecords/spatialunit/landrecord/" + activeProject + "/" + records_from + "/" + Global.LANG,
        async: false,
        success: function (data) {
            dataList = data;
            $('#landRecordsRowData').empty();
            if (dataList != "" && dataList != null) {
                jQuery("#landRecordsAttrTemplate").tmpl(dataList).appendTo("#landRecordsRowData");
                $("#landRecordsRowData").i18n();
                $("#landRecordsTable").trigger("update");
                $('#records_from').val(records_from + 1);
                $('#records_to').val(totalRecords);
                if (records_from + 10 <= totalRecords)
                    $('#records_to').val(records_from + 10);
                $('#records_all').val(totalRecords);
            } else {
                $('#records_from').val(0);
                $('#records_to').val(0);
                $('#records_all').val(0);

            }
        }
    });
}

function spatialSearch(records_from) {
    $("#lang_search").val(Global.LANG);
    jQuery.ajax({
        type: "POST",
        url: "landrecords/search1/" + activeProject + "/" + records_from,
        data: jQuery("#landrecordsform").serialize(),
        success: function (result) {
            dataList = null;
            dataList = result;
            $('#landRecordsRowData').empty();
            if (dataList != "" && dataList != null) {
                jQuery("#landRecordsAttrTemplate").tmpl(dataList).appendTo("#landRecordsRowData");
                $("#landRecordsRowData").i18n();
                $("#landRecordsTable").trigger("update");
                $('#records_from').val(records_from + 1);
                $('#records_to').val(searchRecords);
                if (records_from + 10 <= searchRecords)
                    $('#records_to').val(records_from + 10);
                $('#records_all').val(searchRecords);
            } else {
                $('#records_from').val(0);
                $('#records_to').val(searchRecords);
                $('#records_all').val(searchRecords);

            }
        }
    });
}

function spatialSearchWorkfow(records_from) {
    $("#workflow_lang").val(Global.LANG);
    jQuery.ajax({
        type: 'POST',
        url: "landrecords/spatialunitbyworkflow/" + activeProject + "/" + records_from,
        data: jQuery("#updatebyWorkflow").serialize(),
        async: false,
        success: function (data) {
            dataList = data;
            $('#landRecordsRowData').empty();
            if (dataList != "" && dataList != null) {
                jQuery("#landRecordsAttrTemplate").tmpl(dataList).appendTo("#landRecordsRowData");
                $("#landRecordsRowData").i18n();
                $("#landRecordsTable").trigger("update");
                $('#records_from').val(records_from + 1);
                $('#records_to').val(workflowRecords);
                if (records_from + 10 <= workflowRecords)
                    $('#records_to').val(records_from + 10);
                $('#records_all').val(workflowRecords);
            } else {
                $('#records_from').val(0);
                $('#records_to').val(workflowRecords);
                $('#records_all').val(workflowRecords);

            }

        }


    });

}






function naturalAdditonalAttributes() {
    jQuery.ajax({
        url: "landrecords/naturalcustom/" + activeProject,
        async: false,
        success: function (data) {
            customList = data;
            $(".datepicker").datepicker();
            $(".datepicker").live('click', function () {
                $(this).datepicker('destroy').datepicker({
                    dateFormat: 'yy-mm-dd'
                }).focus();
            });
        }
    });

    var length_new = (customList.length) / 3;
    var j = 0;

    jQuery("#customnatural-div").empty();
    jQuery("#customnewnatural-div").empty();
    jQuery("#customnewnatural-div").append('<div><input type="hidden" name="newnatural_length" value=' + length_new + '></div>');
    for (var i = 0; i < customList.length; i++) {
        j = j + 1;
        var selectedcustomText = customList[i];
        var datatype = customList[i + 2];
        var custom_uid = customList[i + 1];

        if (datatype == '2') {
            jQuery("#customnewnatural-div").append('<div class="fieldHolder"><div class="floatColumn02"> <label for="email" id="alias' + "" + j + "" + '" >' + "" + selectedcustomText + "" + '</label></div><div class="floatColumn01"><input  id="alias_nat_custom' + "" + j + "" + '" class="input-medium justread datepicker" readonly name="alias_nat_custom' + "" + j + "" + '" type="Date" value=""/><input  id="alias_uid' + "" + j + "" + '" class="inputField01 splitpropclass" name="alias_uid' + "" + j + "" + '" type="hidden" value="' + "" + custom_uid + "" + '"/></div></div>');
        } else if (datatype == '3') {
            jQuery("#customnewnatural-div").append('<div class="fieldHolder"><div class="floatColumn02"> <label for="email" id="alias' + "" + j + "" + '" >' + "" + selectedcustomText + "" + '</label></div><div class="floatColumn01"><select  id="alias_nat_custom' + "" + j + "" + '" class="input-medium justdisable" name="alias_nat_custom' + "" + j + "" + '" value=""><option value="Yes">Yes</option><option value="No">No</option></select><input  id="alias_uid' + "" + j + "" + '" class="inputField01 splitpropclass" name="alias_uid' + "" + j + "" + '" type="hidden" value="' + "" + custom_uid + "" + '"/></div></div>');
        } else if (datatype == '4') {
            jQuery("#customnewnatural-div").append('<div class="fieldHolder"><div class="floatColumn02"> <label for="email" id="alias' + "" + j + "" + '" >' + "" + selectedcustomText + "" + '</label></div><div class="floatColumn01"><input  id="alias_nat_custom' + "" + j + "" + '" type="number" pattern="[0-9]" class="input-medium justread" name="alias_nat_custom' + "" + j + "" + '" value=""/><input  id="alias_uid' + "" + j + "" + '" class="inputField01 splitpropclass" name="alias_uid' + "" + j + "" + '" type="hidden" value="' + "" + custom_uid + "" + '"/></div></div>');
        } else {
            jQuery("#customnewnatural-div").append('<div class="fieldHolder"><div class="floatColumn02"> <label for="email" id="alias' + "" + j + "" + '">' + "" + selectedcustomText + "" + '</label></div><div class="floatColumn01"><input  id="alias_nat_custom' + "" + j + "" + '" class="inputField01 splitpropclass" name="alias_nat_custom' + "" + j + "" + '" type="text" value=""/><input  id="alias_uid' + "" + j + "" + '" class="inputField01 splitpropclass" name="alias_uid' + "" + j + "" + '" type="hidden" value="' + custom_uid + '"/></div></div>');
        }
        i = i + 2;
    }
}


function printDenialLetter(usin) {
    window.open("landrecords/denialletter/" + usin, 'Report', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
}

function generateAdjudicationForm(usin) {
    $.ajax({
        type: "GET",
        url: "landrecords/checkvcdate/" + activeProject,
        async: false,
        success: function (result) {
            if (result === RESPONSE_OK) {
                var w = window.open("landrecords/adjudicationform/" + usin, 'AdjudicationForm', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
                if (window.focus) {
                    w.focus();
                }
            } else {
                jAlert(result, $.i18n("err-error"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-failed-validate-vcdate"));
        }
    });
}

function generateAdjudicationForms() {
    $.ajax({
        type: "GET",
        url: "landrecords/checkvcdate/" + activeProject,
        async: false,
        success: function (result) {
            if (result === RESPONSE_OK) {
                var fromRecord = $("#adjStart").val();
                var endRecord = $("#adjEnd").val();

                if (!checkIntNumber(fromRecord))
                    fromRecord = 1;

                if (!checkIntNumber(endRecord))
                    endRecord = 100000;

                var w = window.open("landrecords/adjudicationforms/" + activeProject + "/" + fromRecord + "/" + endRecord, 'AdjudicationForms', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
                if (window.focus) {
                    w.focus();
                }
            } else {
                jAlert(result, $.i18n("err-error"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-failed-validate-vcdate"));
        }
    });
}

function generateDataCorrectionReport(usin) {

    if (usin == null)
    {
        usin = $("#ccroStartTransid").val();
    }

    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        var w = window.open("landrecords/landverification/" + usin, 'CcroForm', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
        if (window.focus) {
            w.focus();
        }
    } else {
        jAlert(result, $.i18n("err-error"));
    }
}

function generateDataCorrectionReportnew(usin) {

    if (usin == null)
    {
        usin = $("#ccroStartTransid").val();
    }

    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        var w = window.open("landrecords/landverification/" + usin, 'CcroForm', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
        if (window.focus) {
            w.focus();
        }
    } else {
        jAlert(result, $.i18n("err-error"));
    }
}


function generateCcro(usin) {

    if (usin == null)
    {
        usin = $("#ccroStart").val();


    }

    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        var w = window.open("landrecords/ccroformladm/" + usin, 'CcroForm', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
        if (window.focus) {
            w.focus();
        }
    } else {
        jAlert(result, $.i18n("err-error"));
    }
}

function generateProjectTenureTypesLandUnitsSummaryReport()
{
    projectid = $("#selectProjectsForTenureTypesLandUnitsSummary").val();
    if (projectid == "" || projectid == null)
    {
        alert("Select Project");
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectTenureTypesLandUnitsSummaryReport("NEW", projectid, "1");
        // reportDialog.dialog( "destroy" );
    }

}

reports.prototype.ProjectTenureTypesLandUnitsSummaryReport = function (tag, projectid, villageId) {
    //alert ("Under Function");
    window.open("landrecords/projectdetailedTenureTypesLandUnitSummaryreport/" + projectid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');

}

function generateCcros()
{
    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        result = RESPONSE_OK;
        var fromRecord = $("#ccroStartbatch").val();
        var endRecord = $("#ccroEndbatch").val();

        $.ajax({
            type: "GET",
            url: "landrecords/batchlandcorrectionreport/" + fromRecord + "/" + endRecord + "/" + 0,
            async: false,
            success: function (newdata) {
                jQuery("#printDiv").empty();

                jQuery.get("resources/templates/report/batchland-certificate.html", function (template)
                {

                    jQuery("#printDiv").append(template);
                    for (var index = 0; index < newdata.length; index++) {
                        var flag = 0;
                        if (index > 0) {

                            flag = index - 1;
                        }
                        if (newdata[index] != null || newdata[index] != "" || newdata[index] != "undefined")
                        {

                            if (newdata[index] != null) {
                                var data = newdata[index];
                                if (data[0] != null && data[0].length != 0) {


                                    if (data[0][0].region != null || data[0][0].commune != null || data[0][0].province != null)
                                        data[0][0].compaddresstoadd = data[0][0].region + ", " + data[0][0].commune + ", " + data[0][0].province;



//								$('#dynamiclanddata').attr('id', 'dynamiclanddata-'+i);

                                    $("<div id='landdetails-" + data[0][0].transactionid + "'></div>").insertAfter("#forinsertafterdiv");
                                    jQuery("#dynamiclanddatatemplate").tmpl(data[0][0]).appendTo('#landdetails-' + data[0][0].transactionid);

                                    if (null != data[2] && data[2].length != 0) {
                                        var ownersname = "";

                                        $("<div id='persondetails-" + data[0][0].transactionid + "'></div>").insertAfter("#landdetails-" + data[0][0].transactionid);

                                        for (var i = 0; i < data[2].length; i++) {

                                            if (ownersname == "") {
                                                ownersname = data[2][i].firstname + " " + data[2][i].middlename + " " + data[2][i].lastname;

                                            } else {
                                                ownersname = ownersname + " and " + data[2][i].firstname + " " + data[2][i].middlename + " " + data[2][i].lastname;
                                            }
                                            /* if(data[2][i].ownertype=="Primary occupant /Point of contact"){
                                             $('#owner').text(data[2][i].firstname+" "+data[2][i].middlename+" "+data[2][i].lastname);
                                             }*/

                                            $('#owner').text(ownersname);
                                        }

                                        $.each(data[2], function (indexes, val) {
                                            val['transactionid'] = data[0][0].transactionid;
                                            if (indexes == 0) {


                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(val).appendTo("#persondetails-" + data[0][0].transactionid);
                                            } else {

                                                jQuery("#jointOwnerRecordsAttrTemplate1").tmpl(val).appendTo("#OwnerRecordsRowData1-" + data[0][0].transactionid);
                                            }


                                        });


                                    }

                                    if (null != data[5] && data[5].length != 0) {


                                        $("<div id='persondetails-" + data[0][0].transactionid + "'></div>").insertAfter("#landdetails-" + data[0][0].transactionid);

                                        for (var i = 0; i < data[5].length; i++) {

                                            $('#owner').text(data[5][i].organizationname);

                                        }

                                        $.each(data[5], function (indexes, val) {
                                            val['transactionid'] = data[0][0].transactionid;
                                            if (indexes == 0) {


                                                jQuery("#non-naturalRecordsAttrTemplate1").tmpl(val).appendTo("#persondetails-" + data[0][0].transactionid);
                                            } else {

                                                jQuery("#jointnonnaturalRecordsAttrTemplate1").tmpl(val).appendTo("#NonnaturalRecordsRowData1-" + data[0][0].transactionid);
                                            }


                                        });


                                    }

                                    if (null != data[1] && data[1].length != 0) {
                                        if (null != data[2] && data[2].length != 0) {
                                            var ind = data[2].length - 1;

                                            $("<div id='POIDetails-" + data[0][0].transactionid + "'></div>").insertAfter("#persondetails-" + data[0][0].transactionid);

                                            for (var i = 0; i < data[1].length; i++) {
                                                data[1][i]['transactionid'] = data[0][0].transactionid;
                                                if (i == 0) {
                                                    jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIDetails-" + data[0][0].transactionid);

                                                } else {

                                                    $('#batchPOIRecordsAttrTemplate1').tmpl(data[1][i]).appendTo("#POIRecordsRowData1-" + data[0][0].transactionid);
                                                }

                                                if (data[1][i] != null) {
//										$("<div id='POIDetails-" + index +  "'></div>").insertAfter("#persondetails-"+index);


                                                }
                                            }
                                        } else if (null != data[5] && data[5].length != 0) {
                                            var ind = data[5].length - 1;

                                            $("<div id='POIDetails-" + data[0][0].transactionid + "'></div>").insertAfter("#persondetails-" + data[0][0].transactionid);

                                            for (var i = 0; i < data[1].length; i++) {
                                                data[1][i]['transactionid'] = data[0][0].transactionid;
                                                if (i == 0) {
                                                    jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIDetails-" + data[0][0].transactionid);

                                                } else {

                                                    $('#batchPOIRecordsAttrTemplate1').tmpl(data[1][i]).appendTo("#POIRecordsRowData1-" + data[0][0].transactionid);
                                                }

                                                if (data[1][i] != null) {
//											
                                                }
                                            }
                                        }
                                    } else
                                    {
                                        $("<div id='POIDetails-" + data[0][0].transactionid + "'></div>").insertAfter("#persondetails-" + data[0][0].transactionid);
                                        var PoiEmptyJsonObject = {"firstName": "", "middleName": "", "lastName": "", "relationship": "", "gender": ""}
                                        jQuery("#POIRecordsAttrTemplate1").tmpl(PoiEmptyJsonObject).appendTo("#POIDetails-" + data[0][0].transactionid);
                                    }





                                    if (null != data[2] && data[2].length != 0) {
                                        if (data[2].length == 1)//Single
                                        {
                                            if (data[2][0] != null) {
                                                var ownername = {"ownername": data[2][0].firstname + " " + data[2][0].middlename + " " + data[2][0].lastname, "partyid": data[2][0].id};
                                                $("<div id='SingleOwnerDetailsDiv-" + data[0][0].transactionid + "'>" +
                                                        "</div>").insertAfter("#POIDetails-" + data[0][0].transactionid);
                                                jQuery("#SingleOwnerNameTemplate").tmpl(ownername).appendTo("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);
                                            }
                                        } else if (data[2].length > 1)//Joint
                                        {

                                            $.each(data[2], function (index, optiondata) {

                                                if (index == 0)
                                                {
                                                    var ownername = {"ownername": optiondata.firstname + " " + optiondata.middlename + " " + optiondata.lastname, "partyid": optiondata.id};
                                                    $("<div id='SingleOwnerDetailsDiv-" + data[0][0].transactionid + "'>" +
                                                            "</div>").insertAfter("#POIDetails-" + data[0][0].transactionid);
                                                    jQuery("#SingleOwnerNameTemplate").tmpl(ownername).appendTo("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);

                                                } else
                                                {
                                                    var jointownername = {"jointownername": optiondata.firstname + " " + optiondata.middlename + " " + optiondata.lastname, "partyid": optiondata.id};
                                                    $("<div id='jointOwnerDetailsDiv-" + index + "'></div>").insertAfter("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);
                                                    jQuery("#jointownertableNameTemplate").tmpl(jointownername).appendTo("#jointOwnerDetailsDiv-" + index);
                                                }
                                            });
                                        }
                                    }


                                    if (null != data[5] && data[5].length != 0) {
                                        if (data[5].length == 1)//Single
                                        {
                                            if (data[5][0] != null) {
                                                var ownername = {"ownername": data[5][0].firstname + " " + data[5][0].middlename + " " + data[5][0].lastname, "partyid": data[5][0].id};
                                                $("<div id='SingleOwnerDetailsDiv-" + data[0][0].transactionid + "'>" +
                                                        "</div>").insertAfter("#POIDetails-" + data[0][0].transactionid);
                                                jQuery("#SingleOwnerNameTemplate").tmpl(ownername).appendTo("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);
                                            }
                                        }

                                    }



                                    if (null != data[4] && data[4].length != 0) {
                                        if (null != data[2]) {

                                            if (data[2].length == 1) {
                                                $("<div  id='authrisedDetailsDiv-" + data[0][0].transactionid + "'></div>").insertAfter("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);
                                                var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                                var jsonSignImage = {"authrisedpersonName": data[4].authorizedmember, "imageUrl": url2};
                                                jQuery("#authrisedpersonTemplate").tmpl(jsonSignImage).appendTo("#authrisedDetailsDiv-" + data[0][0].transactionid);


                                            } else {
                                                var ind = data[2].length - 1;
                                                $("<div  id='authrisedDetailsDiv-" + data[0][0].transactionid + "'></div>").insertAfter("#jointOwnerDetailsDiv-" + 1);
                                                var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                                var jsonSignImage = {"authrisedpersonName": data[4].authorizedmember, "imageUrl": url2};
                                                jQuery("#authrisedpersonTemplate").tmpl(jsonSignImage).appendTo("#authrisedDetailsDiv-" + data[0][0].transactionid);

                                            }

                                        }

                                        if (null != data[5] && data[5].length != 0) {

                                            if (data[5].length == 1) {
                                                $("<div id='authrisedDetailsDiv-" + data[0][0].transactionid + "'></div>").insertAfter("#SingleOwnerDetailsDiv-" + data[0][0].transactionid);
                                                var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                                var jsonSignImage = {"authrisedpersonName": data[4].authorizedmember, "imageUrl": url2};
                                                jQuery("#authrisedpersonTemplate").tmpl(jsonSignImage).appendTo("#authrisedDetailsDiv-" + data[0][0].transactionid);


                                            } else {
                                                var ind = data[5].length - 1;
                                                $("<div id='authrisedDetailsDiv-" + data[0][0].transactionid + "'></div>").insertAfter("#jointOwnerDetailsDiv-" + data[0][0].transactionid);
                                                var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                                var jsonSignImage = {"authrisedpersonName": data[4].authorizedmember, "imageUrl": url2};
                                                jQuery("#authrisedpersonTemplate").tmpl(jsonSignImage).appendTo("#authrisedDetailsDiv-" + data[0][0].transactionid);

                                            }

                                        }




                                    }

                                    if (null != data[3]) {

                                        for (var i = 0; i < data[3].length; i++) {
                                            var url1 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                            var jsonpersonSignImage = {"imagePersonId": url1};
                                            if (null != data[3][i].laParty) {
                                                if (data[3][i].laParty.ownertype == 1) {
                                                    $("#imagesinglePersonId_" + data[3][i].laParty.partyid).append('<img  src=' + url1 + '>')
                                                } else if (data[3][i].laParty.ownertype == 2) {

                                                    $("#imagejontPersonId_" + data[3][i].laParty.partyid).append('<img  src=' + url1 + '>')

                                                }
                                            }
                                        }

                                    }
                                }

                            }

                            var layerName = "spatialUnitLand";
                            var objLayer = getLayerByAliesName(layerName);

                            var _wfsurl = objLayer.values_.url;
                            var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
                            ;

                            //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
                            $.ajax({
                                url: PROXY_PATH + _wfsSchema,
                                async: false,
                                success: function (data) {
                                    _featureNS = data.targetNamespace;

                                }
                            });

                            var relLayerName = "Mast:la_spatialunit_land";
                            var fieldName = "landid";
                            var fieldVal = data[0][0].landId;

                            var _featureTypes = [];
                            _featureTypes.push("la_spatialunit_land");
                            var _featurePrefix = "Mast";
                            var featureRequest1 = new ol.format.WFS().writeGetFeature({
                                srsName: 'EPSG:4326',
                                featureNS: _featureNS,
                                featurePrefix: _featurePrefix,
                                featureTypes: _featureTypes,
                                outputFormat: 'application/json',
                                filter: ol.format.filter.equalTo(fieldName, fieldVal)
                            });


                            var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
                            fetch(_url, {
                                method: 'POST',
                                async: false,
                                body: new XMLSerializer().serializeToString(featureRequest1)
                            }).then(function (response) {
                                return response.json();
                            }).then(function (json) {

                                var features = new ol.format.GeoJSON().readFeatures(json);
                                callback_function(features, features[0].values_.landid);
                            });

                        } else
                        {
                            jAlert($.i18n("err-fetch-details"), $.i18n("err-alert"));
                        }

                    }

                });
            }
        });

    } else {
        jAlert(result, $.i18n("err-error"));
    }

}


function callback_function(features, landId) {


    var vectorSource = new ol.source.Vector();
    vectorSource.addFeatures(features);
    extent = vectorSource.getExtent();
    var cqlFilter = 'landid=' + landId;
    var vertexlist1 = features[0].values_.geometry.clone().getCoordinates();

    var tempStr = "";
    for (var i = 0; i < vertexlist1[0].length; i++)
    {

        if (tempStr == "") {
            tempStr = vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];

        } else {
            tempStr = tempStr + "," + vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];
        }


    }

    var serverData = {"vertexList": tempStr};
    $.ajax({
        type: 'POST',
        url: "landrecords/vertexlabel",
        data: serverData,
        async: false,
        success: function (data) {

        }
    });

    var url1 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=245&height=243&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + landId + ";";

    $("#mapImageId_" + landId).empty();
    $("#mapImageId_" + landId).append('<img  src=' + url1 + '>')

    var html2 = $("#printdiv2").html();
    var printWindow = window.open('', 'popUpWindow', 'height=600,width=950,left=40,top=20,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
    printWindow.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN""http://www.w3.org/TR/html4/strict.dtd">' +
            '<html><head><title></title>' + ' <link rel="stylesheet" href="/mast/resources/styles/style.css" type="text/css" />'
            + '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>' + ' <link rel="stylesheet" href="/mast/resources/styles/complete-style1.css" type="text/css" />'
            + '<script src="../resources/scripts/cloudburst/viewer/Print.js"></script>'
            + '</head><body>' + html2 + '</body></html>');
    printWindow.document.close();


}
function generateLandForm()
{
    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        result = RESPONSE_OK;
        var fromRecord = $("#landformStart").val();
        var endRecord = $("#landformEnd").val();

        /*if (!checkIntNumber(fromRecord))
         fromRecord = 1;
         
         if (!checkIntNumber(endRecord))
         endRecord = 100000;*/

        var w = window.open("landrecords/landformsinbatch/" + activeProject + "/" + fromRecord + "/" + endRecord, 'CcroForms', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
        if (window.focus) {
            w.focus();
        }
    } else {
        jAlert(result, $.i18n("err-error"));
    }

}

function _generateLandForm(_transId)
{
    result = RESPONSE_OK;
    if (result === RESPONSE_OK) {
        result = RESPONSE_OK;

        var w = window.open("landrecords/landformsinbatch/" + activeProject + "/" + _transId + "/" + _transId, 'CcroForms', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
        if (window.focus) {
            w.focus();
        }
    } else {
        jAlert(result, $.i18n("err-error"));
    }

}


function generateDistrictRegBook() {
    var w = window.open("landrecords/districtregbook/" + activeProject, 'DistrictRegistryBook', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
    if (window.focus) {
        w.focus();
    }
}

function generateVillageRegBook() {
    var w = window.open("landrecords/villageregbook/" + activeProject, 'DistrictRegistryBook', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
    if (window.focus) {
        w.focus();
    }
}

function generateVillageIssuanceBook() {
    var w = window.open("landrecords/villageissuancebook/" + activeProject, 'VillageIssuanceBook', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
    if (window.focus) {
        w.focus();
    }
}

function generateTransactionSheet(usin) {
    var w = window.open("landrecords/transactionsheet/" + usin + "/" + activeProject, 'TransactionSheet', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
    if (window.focus) {
        w.focus();
    }
}

function generateClaimsProfile() {
    var w = window.open("landrecords/claimsprofile/" + $("#selectProjects").val(), 'ClaimsProfile', 'left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=true');
    if (window.focus) {
        w.focus();
    }
}

$(document).ready(function () {
    // Add date field
    var DateField = function (config) {
        jsGrid.Field.call(this, config);
    };

    DateField.prototype = new jsGrid.Field({
        sorter: function (date1, date2) {
            if ((date1 === null || date1 === "") && (date2 === null || date2 === "")) {
                return 0;
            }
            if (date1 === null || date1 === "") {
                return -1;
            }
            if (date2 === null || date2 === "") {
                return 1;
            }
            return new Date(date2) - new Date(date1);
        },
        itemTemplate: function (value) {
            if (!isEmpty(value)) {
                return formatDate(value);
            }
            return "";
        },
        editTemplate: function (value) {
            if (isEmpty(value))
                return this._editPicker = $("<input>").datepicker({dateFormat: "yy-mm-dd"});
            else
                return this._editPicker = $("<input>").datepicker({dateFormat: "yy-mm-dd"}).datepicker("setDate", new Date(value));
        },
        insertValue: function () {
            return this._insertPicker.datepicker("getDate");
        },
        editValue: function () {
            return this._editPicker.datepicker("getDate");
        }
    });

    jsGrid.fields.date = DateField;

    // init dropdown lists
    var idTypes;
    var maritalStatuses;
    hamletList = [];

    $.ajax({
        url: "landrecords/idtype/",
        async: false,
        success: function (data) {
            idTypes = data;
            if (idTypes !== null && idTypes.length > 0)
                idTypes = [{code: "", name: " "}].concat(idTypes);
        }
    });

    $.ajax({
        url: "landrecords/maritalstatus/",
        async: false,
        success: function (data) {
            maritalStatuses = data;
            if (maritalStatuses !== null && maritalStatuses.length > 0)
                maritalStatuses = [{maritalStatusId: "", maritalStatus: " "}].concat(maritalStatuses);
        }
    });

    // Init editing grid


    $("#personsEditingGrid .jsgrid-table th:first-child :button").click();
});



function formatDate(date) {
    return jQuery.datepicker.formatDate('yy-mm-dd', new Date(date));
}

function formatDate2(intDate) {
    return jQuery.datepicker.formatDate('yy-mm-dd', new Date(parseInt(intDate)));
}

function calculateAge(birthday) {
    var ageDifMs = Date.now() - (new Date(birthday)).getTime();
    var ageDate = new Date(ageDifMs);
    return Math.abs(ageDate.getUTCFullYear() - 1970);
}

function checkIntNumber(data) {
    if (data === "" + parseInt(data, 10))
        return true;
    else
        return false;
}

function Actionfill(usin, workflowId, transactionid, parcelnum, claimtypeid, shareTypeId) {
    _transactionid = 0;
    _parcelNumber = 0;
    _parcelNumber = parcelnum;
    parcelnum = "" + parcelnum + "";

    $(".containerDiv").empty();
    var appid = '#' + usin + "_land";
    jQuery.ajax({
        url: "landrecords/workflowAction/" + workflowId + "/" + activeProject + "/" + usin,
        async: false,
        success: function (data) {
            actionList = data;
        }
    });


    $("" + appid + "").empty();
    html = "";

    if (actionList.length == undefined) {
        jAlert($.i18n("err-no-actions"));
    } else {
        for (var i = 0; i < actionList.length; i++) {
            var displayName = actionList[i].actionname;
            if (Global.LANG === "en") {
                displayName = actionList[i].actionnameEn;
            }
            html += "<li> <a title='" + actionList[i].action + "' id=" + workflowId + " name=" + usin + "  href='#' onclick='CustomAction(this," + usin + "," + workflowId + " ," + transactionid + "," + parcelnum + " ," + claimtypeid + "," + shareTypeId + ")'>" + displayName + "</a></li>";
        }

        html = html += "<li> <a title='" + $.i18n("gen-view-comment") + "' id=" + workflowId + "  name=" + usin + "  href='#' onclick='commentsDialog(" + usin + ")'>" + $.i18n("gen-comments") + "</a></li>";
        $("" + appid + "").append('<div class="signin_menu"><div class="signin"><ul>' + html + '</ul></div></div>');


        $(".signin_menu").toggle();
        $(".signin").toggleClass("menu-open");

        $(".signin_menu").mouseup(function () {
            return false
        });
        $(document).mouseup(function (e) {
            if ($(e.target).parent("a.signin").length == 0) {
                $(".signin").removeClass("menu-open");
                $(".signin_menu").hide();
            }
        });
    }
}

function CustomAction(refrence, landid, workflowid, transactionid, parcelnum, claimtypeid, shareTypeId) {
    if (claimtypeid === 3) {
        $("#liDisputes").show();
    } else {
        $("#liDisputes").hide();
    }
    _transactionid = transactionid;
    $('#commentsStatusWorkflow').val("");

    if ((refrence.title).trim() == "approve") {
        dialogueAction(landid, workflowid, (refrence.title).trim(), claimtypeid)
    } else if ((refrence.title).trim() == "reject") {
        dialogueAction(landid, workflowid, (refrence.title).trim(), claimtypeid)
    } else if ((refrence.title).trim() == "generate map") {
        generateMap(landid, shareTypeId);
    } else if ((refrence.title).trim() == "generate forms") {
        showFormsList(landid, workflowid, shareTypeId);
    } else if ((refrence.title).trim() == "edit") {
        editAttributeNew(landid, workflowid, true);
    } else if ((refrence.title).trim() == "view") {
        editAttributeNew(landid, workflowid, false);
    } else if ((refrence.title).trim() == "verification") {
        dialogueAction(landid, workflowid, (refrence.title).trim(), claimtypeid)
    } else if ((refrence.title).trim() == "print") {
        _printReport(workflowid, transactionid, landid);
    } else if ((refrence.title).trim() == "register") {
        registerParcel(landid, workflowid, parcelnum)
    } else if ((refrence.title).trim() == "delete") {
        deleteParcel(landid, workflowid)
    } else if ((refrence.title).trim() === "edit  spatial") {
        showOnMap(landid, workflowid, "", true);
    } else if ((refrence.title).trim() === "view map") {
        showOnMap(landid, workflowid, "", false);
    } else if (refrence.title.trim() === "view parcel number") {
        showParcelNumber($("#hSection" + landid).val(), $("#hParcelInSection" + landid).val());
    } else if (refrence.title.trim() === "edit parcel number") {
        editParcelNumber(landid, $("#hSection" + landid).val(), $("#hParcelInSection" + landid).val());
    } else if ((refrence.title).trim() === "edit  parcel") {
        edituserDefineParcel(landid, workflowid);
    } else if ((refrence.title).trim() === "payment info") {
        paymentDialog(landid);
    } else if ((refrence.title).trim() === "signatory info") {
        signatoryDialog(landid);
    }
}

function generateMap(usin, sharetype) {
    if (sharetype === 7) {
        $(".checkType").hide();
        $('label[for="sel-4"]').hide();
    } else if (sharetype === 8) {
        $(".checkType").show();
        $('label[for="sel-4"]').show();
    }

    Dialog = $("#generatemap-dialog-form").dialog({
        autoOpen: false,
        height: 250,
        width: 280,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-ok"),
                "id": "generate_map_ok",
                click: function () {
                    var option1 = checkoptions;
                    if (option1 !== '0') {
                        if (option1 === "1") {
                            generateBoundaryMap(usin);
                            Dialog.dialog("destroy");
                        } else if (option1 === "2") {
                            generateAreaMap(usin);
                            Dialog.dialog("destroy");
                        } else if (option1 === "3") {
                            generateform1(usin, 2);
                            Dialog.dialog("destroy");
                        } else if (option1 === "4") {
                            generateform2(usin, 2);
                            Dialog.dialog("destroy");
                        }
                    } else {
                        jAlert($.i18n("err-select-option"), $.i18n("err-alert"));
                    }
                }
            }, {
                text: $.i18n("gen-cancel"),
                "id": "generate_map_cancel",
                click: function () {
                    Dialog.dialog("destroy");
                }
            }],
        close: function () {
            Dialog.dialog("destroy");
        }
    });
    Dialog.dialog("open");

    $('input[type=radio][name=map]').change(function () {
        $('input:radio[name="map"][value=' + this.value + ']').prop('checked', true);
        checkoptions = this.value;
    });

    checkoptions = "1";
    $('input:radio[name="map"][value="1"]').prop('checked', true);
}

function showParcelNumber(section, parcelNoInSection) {
    $('#section_no').text(section);
    $('#lot_no').text("000");
    $('#number_seq').text(parcelNoInSection);

    generateParcelDialog = $("#generate-parcelno-form").dialog({
        autoOpen: false,
        height: 250,
        width: 300,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-ok"),
                click: function () {
                    generateParcelDialog.dialog("destroy");
                }
            }],
        close: function () {
            generateParcelDialog.dialog("destroy");
        }
    });
    generateParcelDialog.dialog("open");
}

function _printReport(workflowid, transactionid, landid) {

    if (workflowid == 1) {
        FetchdataCorrectionReport(transactionid, landid, workflowid);
    } else if (workflowid == 2) {
        FetchdataCorrectionReport(transactionid, landid, workflowid);
    } else if (workflowid == 3) {
        FetchdataCorrectionReport(transactionid, landid, workflowid);
    } else if (workflowid == 4) {
        _generateFinalLandForm(transactionid, landid);
    } else if (workflowid == 5) {
        generateDataCorrectionReport(transactionid);
    }
}

function RefreshedLandRecordsgrid()
{
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    records_from = records_from - 1;
    if (records_from >= 0) {
        if (searchRecords != null) {
            spatialSearch(records_from);
        } else if (workflowRecords != null) {
            spatialSearchWorkfow(records_from);
        } else {
            spatialRecords(records_from);
        }
    } else {
        alert($.i18n("err-no-records"));
    }
}
function dialogueAction(usin, workId, actionName, claimtypeid) {
    if (claimtypeid === "3") {
        jAlert($.i18n("err-resolve-dispute"), $.i18n("err-alert"));
        return false;
    }

    $('#commentsStatusWorkflow').val($.i18n("reg-no-comments"));
    if (actionName === "reject" || roles === "ROLE_DPI") {
        $('#commentsStatusWorkflow').val("");
    }

    if (actionName === "approve") {
        if (workId === 2 || workId === 5 || workId === 7 || workId === 13) {
            var msg = "";
            if (workId === 2) {
                msg = $.i18n("reg-gen-form-validation");
            }
            if (workId === 5) {
                msg = $.i18n("reg-gen-form-prepadj");
            }
            if (workId === 7 || workId === 13) {
                msg = $.i18n("reg-gen-form-apfr");
            }

            $('#printFormsInfoDiv').html(msg).css("color", "blue");

            printFormInfoDialog = $("#printFormsInfo-dialog-form").dialog({
                autoOpen: false,
                height: 200,
                width: 370,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-ok"),
                        "id": "info_ok",
                        click: function () {
                            showActionDialog(usin, workId, actionName, claimtypeid);
                            printFormInfoDialog.dialog("destroy");
                            //printFormInfoDialog.dialog("close");
                        }
                    },
                    {
                        text: $.i18n("gen-cancel"),
                        "id": "info_cancel",
                        click: function () {
                            printFormInfoDialog.dialog("destroy");
                            //printFormInfoDialog.dialog("close");
                        }
                    }],
                close: function () {
                    printFormInfoDialog.dialog("destroy");
                }
            });
            printFormInfoDialog.dialog("open");
        } else {
            showActionDialog(usin, workId, actionName, claimtypeid);
        }
    } else {
        showActionDialog(usin, workId, actionName, claimtypeid);
    }
}

function showActionDialog(usin, workId, actionName, claimtypeid) {
    $('#commentsStatusWorkflow').val($.i18n("reg-no-comments"));
    if (actionName === "reject" || roles === "ROLE_DPI") {
        $('#commentsStatusWorkflow').val("");
    }

    approveInfoDialog = $("#approveInfo-dialog-form").dialog({
        autoOpen: false,
        height: 200,
        width: 390,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-ok"),
                "id": "info_ok",
                click: function () {
                    var comment = $('#commentsStatusWorkflow').val();
                    if ((comment === undefined || comment === '')) {
                        jAlert($.i18n("err-enter-comments"), $.i18n("err-alert"));
                    } else {
                        if (actionName === "approve") {
                            var status = approveParcel(usin, workId);
                            if (status > 0) {
                                approveInfoDialog.dialog("destroy");
                                RefreshedLandRecordsgrid();
                                jAlert($.i18n("reg-record-approved"));
                            } else if (status === 0) {
                                jAlert($.i18n("err-approve-failed"), $.i18n("err-error"));
                            }
                        } else if (actionName === "reject") {
                            jConfirm($.i18n("gen-confirm-question"), $.i18n("gen-confirmation"), function (result) {
                                if (result) {
                                    var status = rejectParcel(usin, workId);
                                    if (status > 0) {
                                        approveInfoDialog.dialog("destroy");
                                        RefreshedLandRecordsgrid();
                                        jAlert($.i18n("reg-record-rejected"));
                                    } else if (status === 0) {
                                        jAlert($.i18n("err-reject-failed"), $.i18n("err-error"));
                                    }
                                }
                            });
                        } else if (actionName === "verification") {
                            var status = verifyParcel(usin, workId);
                            if (status !== 0) {
                                approveInfoDialog.dialog("destroy");
                                RefreshedLandRecordsgrid();
                            }
                        }
                    }
                }
            },
            {
                text: $.i18n("gen-cancel"),
                "id": "info_cancel",
                click: function () {
                    approveInfoDialog.dialog("destroy");
                }
            }],
        close: function () {
            approveInfoDialog.dialog("destroy");
        }
    });
    approveInfoDialog.dialog("open");
}

function  approveParcel(usin, workId) {
    var approve = 0;
    var allowApprove = true;

    //check public notice 45 days
    if (workId === 4) {
        allowApprove = false;
        jQuery.ajax({
            url: "landrecords/comparedate/" + usin,
            async: false,
            success: function (data) {
                if (data.msg === "donotapproveparcel") {
                    jAlert($.i18n("err-public-notice-pending").format(data.date), $.i18n("err-alert"));
                    approve = -1;
                } else {
                    allowApprove = true;
                }
            }
        });
    }

    if (allowApprove) {
        jQuery.ajax({
            type: 'POST',
            url: "landrecords/action/approve/" + usin + "/" + workId,
            data: jQuery("#aworkflowformID").serialize(),
            async: false,
            success: function (data) {
                approve = data;
            }
        });
    }
    return approve;
}

function  rejectParcel(usin, workId) {
    var reject = 0;
    var allowReject = true;
    //check public notice 45 days
    if (workId === 4) {
        allowReject = false;
        jQuery.ajax({
            url: "landrecords/comparedate/reject/" + usin,
            async: false,
            success: function (data) {
                if (data.msg === "donotReject") {
                    jAlert($.i18n("err-public-notice-pending-cant-reject").format(data.date), $.i18n("err-alert"));
                    reject = -1;
                } else {
                    allowReject = true;
                }
            }
        });
    }

    if (allowReject) {
        jQuery.ajax({
            type: 'POST',
            url: "landrecords/action/reject/" + usin + "/" + workId,
            data: jQuery("#aworkflowformID").serialize(),
            async: false,
            success: function (data) {
                reject = data;
            }
        });
    }
    return reject;
}

function  verifyParcel(usin, workId) {

    var verify = false;

    jQuery.ajax({
        type: 'POST',
        url: "landrecords/action/verify/" + usin + "/" + workId,
        data: jQuery("#aworkflowformID").serialize(),
        async: false,
        success: function (data) {

            verify = data;
        }


    });


    return verify;
}

function edituserDefineParcel(usin, workId) {


    jQuery.ajax({
        type: 'GET',
        url: "landrecords/action/getUserParcel/" + usin,
        async: false,
        success: function (data) {

            approveInfoDialog = $("#attribute-dialog-form").dialog({
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-ok"),
                        "id": "info_ok",
                        click: function () {

                            jQuery.ajax({
                                type: 'POST',
                                url: "landrecords/action/savenewparcel/" + usin,
                                data: jQuery("#addAttributeformID").serialize(),
                                success: function (result) {
                                    jAlert($.i18n("reg-user-parcel-updated"), $.i18n("gen-info"));
                                    approveInfoDialog.dialog("destroy");
                                    RefreshedLandRecordsgrid();
                                },
                                error: function (XMLHttpRequest, textStatus, errorThrown) {

                                    alert('err.Message');
                                }
                            });

                        },
                    },
                    {
                        text: $.i18n("gen-cancel"),
                        "id": "info_cancel",
                        click: function () {

                            approveInfoDialog.dialog("destroy");
                        }
                    }],
                close: function () {

                    approveInfoDialog.dialog("destroy");

                }
            });
            $("#info_ok").html('<span class="ui-button-text"> ' + $.i18n("gen-save") + ' </span>');
            $("#info_cancel").html('<span class="ui-button-text"> ' + $.i18n("gen-cancel") + ' </span>');
            approveInfoDialog.dialog("open");

            $("#hierarchy1").val(data.hierarchy1);
            $("#hierarchy2").val(data.hierarchy2);
            $("#hierarchy3").val(data.hierarchy3);
            $("#parcelId").val(data.landid);
        }


    });

}

function pad(str, max)
{
    str = str.toString();
    return str.length < max ? pad("0" + str, max) : str;
}

function  registerParcel(usin, workId, parcelnum) {

    var parcelnumwithpadding = pad(usin, 9);

    jConfirm($.i18n("reg-confirm-rec-finalize", parcelnumwithpadding), $.i18n("gen-confirmation"), function (response) {

        if (response) {

            approveInfoDialog = $("#approveInfo-dialog-form").dialog({
                autoOpen: false,
                height: 200,
                width: 390,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-ok"),
                        "id": "info_ok",
                        click: function () {
                            var comment = $('#commentsStatusWorkflow').val();
                            if ((comment == undefined || comment == '')) {
                                jAlert($.i18n("err-enter-comments"), $.i18n("err-alert"));
                            } else
                            {
                                jQuery.ajax({
                                    type: 'POST',
                                    url: "landrecords/action/register/" + usin + "/" + workId,
                                    data: jQuery("#aworkflowformID").serialize(),
                                    success: function (result) {
                                        jAlert($.i18n("reg-record-registered"), $.i18n("gen-info"));
                                        approveInfoDialog.dialog("destroy");
                                        RefreshedLandRecordsgrid();
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                                        alert('err.Message');
                                    }
                                });

                            }


                        },
                    },
                    {
                        text: $.i18n("gen-cancel"),
                        "id": "info_cancel",
                        click: function () {

                            // approveInfoDialog.dialog( "close" ); 
                            approveInfoDialog.dialog("destroy");


                        }
                    }],
                close: function () {

                    approveInfoDialog.dialog("destroy");

                }
            });
            $("#info_ok").html('<span class="ui-button-text"> ' + $.i18n("gen-save") + ' </span>');
            $("#info_cancel").html('<span class="ui-button-text"> ' + $.i18n("gen-cancel") + ' </span>');
            approveInfoDialog.dialog("open");


        }

    });

}

function  deleteParcel(usin, workId) {

    jConfirm($.i18n("reg-confirm-delete-parcel"), $.i18n("gen-confirm-delete"), function (response) {

        if (response) {

            approveInfoDialog = $("#approveInfo-dialog-form").dialog({
                autoOpen: false,
                height: 200,
                width: 390,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-ok"),
                        "id": "info_ok",
                        click: function () {
                            var comment = $('#commentsStatusWorkflow').val();
                            if ((comment == undefined || comment == '')) {
                                jAlert($.i18n("err-enter-comments"), $.i18n("err-alert"));
                            } else
                            {
                                jQuery.ajax({
                                    type: 'POST',
                                    url: "landrecords/action/delete/" + usin + "/" + workId,
                                    data: jQuery("#aworkflowformID").serialize(),
                                    success: function (result) {
                                        jAlert($.i18n("gen-data-deleted"), $.i18n("gen-info"));
                                        approveInfoDialog.dialog("destroy");
                                        RefreshedLandRecordsgrid();
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                                        alert('err.Message');
                                    }
                                });

                            }


                        },
                    },
                    {
                        text: $.i18n("gen-cancel"),
                        "id": "info_cancel",
                        click: function () {

                            // approveInfoDialog.dialog( "close" ); 
                            approveInfoDialog.dialog("destroy");


                        }
                    }],
                close: function () {

                    approveInfoDialog.dialog("destroy");

                }
            });
            $("#info_ok").html('<span class="ui-button-text"> ' + $.i18n("gen-save") + ' </span>');
            $("#info_cancel").html('<span class="ui-button-text"> ' + $.i18n("gen-cancel") + ' </span>');
            approveInfoDialog.dialog("open");


        }

    });



}

function commentsDialog(usin) {
    jQuery.ajax({
        type: 'POST',
        url: "landrecords/workflow/comment/" + usin,
        async: false,
        success: function (data) {
            if (data.length > 0) {
                jQuery('#commentsHistoryTableBody').empty();
                jQuery("#commentsTable").show();
                if (data !== null || data !== undefined || data.toString() !== "") {
                    for (var i = 0; i < data.length; i++)
                    {
                        jQuery("#commentstemplate").tmpl(data[i]).appendTo("#commentsHistoryTableBody");
                        jQuery("#commentsTable").show();
                    }
                    commentHistoryDialog = $("#commentsDialog").dialog({
                        autoOpen: false,
                        height: 242,
                        width: 666,
                        resizable: true,
                        modal: true,
                        buttons: [{
                                text: $.i18n("gen-cancel"),
                                "id": "comment_cancel",
                                click: function () {
                                    setInterval(function () {
                                    }, 4000);
                                    jQuery('#commentsHistoryTableBody').empty();
                                    jQuery("#commentsTable").hide();
                                    commentHistoryDialog.dialog("destroy");
                                }
                            }],
                        close: function () {
                            commentHistoryDialog.dialog("destroy");
                        }
                    });
                    $("#comment_cancel").html('<span class="ui-button-text">' + $.i18n("gen-cancel") + '</span>');
                    commentHistoryDialog.dialog("open");
                }
            } else {
                jAlert($.i18n("err-no-records"), $.i18n("gen-info"));
            }
        }
    });
}


function generateSummaryReport()
{
    projectid = $("#selectProjectsForSummary").val();
    if (projectid === "" || projectid === null) {
        alert($.i18n("viewer-select-proj"));
        return false;
    }
    var reportTmp = new reports();
    reportTmp.ParcelCountByTenure("NEW", "1", projectid);
}

function reportByTenure() {
    var villageId = $("#cbxVillagesByTenure").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByTenure("NEW", villageId, activeProject);
}

function reportByTenureAppReg() {
    var villageId = $("#cbxVillagesByTenureAppReg").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByTenure("REGISTERED", villageId, activeProject);
}

function reportByTenureApfrReg() {
    var villageId = $("#cbxVillagesByTenureApfrReg").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByTenure("APFR", villageId, activeProject);
}

function reportSummaryApfrStat() {
    var reportsObj = new reports();
    reportsObj.ApfrStatSummary($("#cbxApfrSummaryVillages").val(), activeProject);
}

function reportSummaryTransactionsStat() {
    var reportsObj = new reports();
    reportsObj.TransStatSummary($("#cbxTransSummaryVillages").val(), activeProject);
}

function reportSummaryResourcesStat() {
    var reportsObj = new reports();
    reportsObj.ResourcesStatSummary(activeProject);
}

function reportByGender() {
    var villageId = $("#cbxVillagesByGender").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByGender("NEW", villageId);
}

function reportByGenderAppReg() {
    var villageId = $("#cbxVillagesByGEnderAppReg").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByGender("REGISTERED", villageId);
}

function reportByGenderApfrReg() {
    var villageId = $("#cbxVillagesByGEnderApfrReg").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.ParcelCountByGender("APFR", villageId);
}

function reportByAppProcess() {
    var villageId = $("#cbxVillagesByAppProccess").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.RegistryParcel("PROCESSEDAPPLICATION", villageId);
}

function reportByAppPublish() {
    var villageId = $("#cbxVillagesByAppPublish").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.RegistryParcel("PUBLISHEDAPPLICATION", villageId);
}

function reportByAppApfr() {
    var villageId = $("#cbxVillagesByAppApfr").val();
    if (isEmpty(villageId) || villageId < 1) {
        alert($.i18n("err-select-village"));
        return;
    }
    var reportsObj = new reports();
    reportsObj.RegistryParcel("PROCESSEDAPFR", villageId);
}

function reports() {
}

reports.prototype.ParcelCountByTenure = function (tag, villageId, proj) {
    window.open("landrecords/parcelcountbytenure/" + proj + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

reports.prototype.ParcelCountByGender = function (tag, villageId) {
    window.open("landrecords/parcelcountbygender/" + activeProject + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

reports.prototype.RegistryParcel = function (tag, villageId) {
    window.open("landrecords/registrytable/" + activeProject + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

reports.prototype.ApfrStatSummary = function (villageId, proj) {
    window.open("landrecords/apfrstatsummary/" + proj + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

reports.prototype.TransStatSummary = function (villageId, proj) {
    window.open("landrecords/transstatsummary/" + proj + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

reports.prototype.ResourcesStatSummary= function (proj) {
    window.open("landrecords/resoursestatsummary/" + proj, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

function reportButtonClick() {

    $('#reportByTenure').show();
    $('#registryReport').hide();
}

function registryButtonClick() {

    $('#reportByTenure').hide();
    $('#registryReport').show();
}

function generateProjectDetailedSummaryReport()
{
    projectid = $("#selectProjectsForDetailSummary").val();
    if (projectid == "" || projectid == null)
    {
        alert($.i18n("viewer-select-proj"));
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectDetailedSummaryReport("NEW", projectid, "1");
        // reportDialog.dialog( "destroy" );
    }

}

reports.prototype.ProjectDetailedSummaryReport = function (tag, projectid, villageId) {
    window.open("landrecords/projectdetailedsummaryreport/" + projectid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
};

function generateProjectAppStatusSummaryReport()
{
    projectid = $("#selectProjectsForAppStatusSummary").val();
    if (projectid == "" || projectid == null)
    {
        alert($.i18n("viewer-select-proj"));
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectAppStatusSummaryReport("NEW", projectid, "1");
        // reportDialog.dialog( "destroy" );
    }

}

reports.prototype.ProjectAppStatusSummaryReport = function (tag, projectid, villageId) {
    //alert ("Under Function");
    window.open("landrecords/projectdetailedappstatussummaryreport/" + projectid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');

}


function generateProjectTypeStatusSummaryReport()
{
    projectid = $("#selectProjectsForAppTypeSummary").val();
    if (projectid == "" || projectid == null)
    {
        alert($.i18n("viewer-select-proj"));
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectTypeStatusSummaryReport("NEW", projectid, "1");
        // reportDialog.dialog( "destroy" );
    }

}

reports.prototype.ProjectTypeStatusSummaryReport = function (tag, projectid, villageId) {
    //alert ("Under Function");
    window.open("landrecords/projectdetailedtypestatussummaryreport/" + projectid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');

}

function generateProjectWorkFlowSummaryReport()
{
    projectid = $("#selectProjectsForWorkFlowSummary").val();
    if (projectid == "" || projectid == null)
    {
        alert($.i18n("viewer-select-proj"));
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectWorkFlowSummaryReport("NEW", projectid, "1");
        // reportDialog.dialog( "destroy" );
    }

}

reports.prototype.ProjectWorkFlowSummaryReport = function (tag, projectid, villageId) {
    //alert ("Under Function");
    window.open("landrecords/projectdetailedworkflowsummaryreport/" + projectid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');

}


function generateProjectDetailedSummaryReportForCommune()
{
    communeidbyhierarchyid = $("#CommuneId").val();
    if (communeidbyhierarchyid == "" || communeidbyhierarchyid == null)
    {
        alert($.i18n("reg-select-commune"));
        return false;

    }
    var rep = "1";
    var reportTmp = new reports();
    if (rep == "1")
    {
        //console.log(villageSelected);
        reportTmp.ProjectDetailedSummaryReportForCommune("NEW", communeidbyhierarchyid, "1");
        // reportDialog.dialog( "destroy" );
    }

}


function getRegionOnCountryChange(id) {

    $("#districtId").empty();
    jQuery("#districtId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#CommuneId").empty();
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#regionId").empty();
    jQuery("#regionId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));


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
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#districtId").empty();
    jQuery("#districtId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));



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
    jQuery("#CommuneId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));

    $("#placeId").empty();
    jQuery("#placeId").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-select")));



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

    $("#villageId").empty();
    jQuery("#villageId").append(jQuery("<option></option>").attr("value", "").text("Select"));

    if (id != '') {

        jQuery.ajax({
            url: "projecthamlet/" + id,
            async: false,
            success: function (regiondata) {
                var proj_coomune = regiondata;
                jQuery.each(proj_coomune, function (i, value) {
                    jQuery("#villageId").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));
                });
            }
        });
    }
}

reports.prototype.ProjectDetailedSummaryReportForCommune = function (tag, communeidbyhierarchyid, villageId) {
    window.open("landrecords/projectdetailedsummaryreportForCommune/" + communeidbyhierarchyid + "/" + tag + "/" + villageId, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
}

function checkNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function loadDesceadPersonsForEditing() {

    $("#DesceadpersonsEditingGrid").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: true,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: DesceadpersonsEditingController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", deleteButton: false},
            {name: "landid", title: $.i18n("reg-usin"), type: "number", width: 70, align: "left", editing: false, filtering: true},
            {name: "firstName", title: $.i18n("reg-firstname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middleName", title: $.i18n("reg-middlename"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-middle-name")}},
            {name: "lastName", title: $.i18n("reg-lastname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "gender", title: $.i18n("reg-gender"), type: "number", width: 120, validate: {validator: "required", message: $.i18n("err-select-gender")}},
            {name: "dob", title: $.i18n("reg-dob"), type: "date", width: 120, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "relation", title: $.i18n("reg-relation"), type: "number", width: 120, validate: {validator: "required", message: $.i18n("err-enter-relation")}},
        ]
    });
    $("#DesceadpersonsEditingGrid .jsgrid-table th:first-child :button").click();
    $("#DesceadpersonsEditingGrid").jsGrid("loadData");
}

var DesceadpersonsEditingController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/deceasedperson/" + landId,
            data: filter
        });
    },
    insertItem: function (item) {
        return false;
    },
    updateItem: function (item) {
        return $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/savePersonOfInterestForEditing",
            data: JSON.stringify(item),
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        return false;
    }
};

function makeDropdownArray(list, id, name, nameEn) {
    var result = [];
    if (list !== null) {
        for (i = 0; i < list.length; i++) {
            var displayName = list[i][name];
            if (Global.LANG === "en") {
                displayName = list[i][nameEn];
            }
            result.push({id: list[i][id], name: displayName});
        }
    }
    return result;
}

function FillPersonDataNew(editable) {
    $("#personsEditingGrid1").jsGrid({
        width: "100%",
        height: "160px",
        inserting: false,
        editing: editable,
        sorting: false,
        filtering: false,
        confirmDeleting: true,
        paging: true,
        autoload: false,
        controller: personController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", editButton: editable, deleteButton: false, width: 50},
            {name: "firstname", title: $.i18n("reg-firstname"), type: "text", validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middlename", title: $.i18n("reg-middlename"), type: "text"},
            {name: "lastname", title: $.i18n("reg-lastname"), type: "text", validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "dateofbirth", title: $.i18n("reg-dob"), type: "date", validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "birthPlace", title: $.i18n("reg-birth-place"), type: "text"},
            {name: "address", title: $.i18n("reg-address"), type: "text"},
            {name: "contactno", title: $.i18n("reg-mobile-num"), type: "text"},
            {name: "profession", title: $.i18n("reg-profession"), type: "text"},
            {name: "genderid", title: $.i18n("reg-gender"), align: "left", type: "select", items: makeDropdownArray(genderList, "genderId", "gender", "gender_en"), valueField: "id", textField: "name", editing: true, filtering: false},
            {name: "laPartygroupMaritalstatus.maritalstatusid", title: $.i18n("reg-marital-status"), align: "left", type: "select", items: makeDropdownArray(maritalList, "maritalstatusid", "maritalstatus", "maritalstatusEn"), valueField: "id", textField: "name", editing: true, filtering: false},
            {name: "identityno", title: $.i18n("reg-id-number"), type: "text", validate: {validator: "required", message: $.i18n("err-enter-idnumber")}},
            {name: "idCardDate", title: $.i18n("reg-id-date"), type: "date", validate: {validator: "required", message: $.i18n("err-enter-id-date")}},
            {name: "fathername", title: $.i18n("reg-father-name"), type: "text", validate: {validator: "required", message: $.i18n("err-enter-father-name")}},
            {name: "mothername", title: $.i18n("reg-mother-name"), type: "text", validate: {validator: "required", message: $.i18n("err-enter-mother-name")}},
            {name: "nopId", title: $.i18n("reg-nature-of-power"), align: "left", type: "select", items: makeDropdownArray(natureOfPowers, "id", "name", "nameEn"), valueField: "id", textField: "name", editing: true, filtering: false},
            {name: "mandateDate", title: $.i18n("reg-mandate-date"), type: "date"},
            {name: "mandateLocation", title: $.i18n("reg-mandate-location"), type: "text"}
        ]
    });

    $("#personsEditingGrid1 .jsgrid-table th:first-child :button").click();
    $("#personsEditingGrid1").jsGrid("loadData");
}

var personController = {
    loadData: function (filter) {
        return $.ajax({
            type: "GET",
            url: "landrecords/personsDataNew/" + Personusin,
            data: filter,
            async: false,
            success: function (data)
            {
                $("#multimediaRowData").empty();
                $("#naturalpersonmediaTemplate_add").tmpl(data).appendTo("#multimediaRowData");
                $("#multimediaRowData").i18n();
            }
        });
    },
    insertItem: function (item) {


    },
    updateItem: function (item) {
        var dob = null;
        if (item !== null) {
            if (item.dateofbirth !== null && item.dateofbirth !== '') {
                var d = new Date(item.dateofbirth);
                dob = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
            }
        }

        var mandateDate = null;
        if (item !== null) {
            if (item.mandateDate !== null && item.mandateDate !== '') {
                var d = new Date(item.mandateDate);
                mandateDate = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
            }
        }

        var idCardDate = null;
        if (item !== null) {
            if (item.idCardDate !== null && item.idCardDate !== '') {
                var d = new Date(item.idCardDate);
                idCardDate = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
            }
        }

        var ajaxdata = {
            "personid": item.partyid,
            "firstname": item.firstname,
            "middlename": item.middlename,
            "lastname": item.lastname,
            "dateofbirth": dob,
            "birthplace": item.birthPlace,
            "address": item.address,
            "contactno": item.contactno,
            "profession": item.profession,
            "genderid": item.genderid,
            "maritalstatusid": item.laPartygroupMaritalstatus.maritalstatusid,
            "identityno": item.identityno,
            "idCardDate": idCardDate,
            "fathername": item.fathername,
            "mothername": item.mothername,
            "nopId": item.nopId,
            "mandateDate": mandateDate,
            "mandateLocation": item.mandateLocation,
            "transactionid": _transactionid,
            "landid": Personusin
        };

        return $.ajax({
            type: "POST",
            traditional: true,
            url: "landrecords/updateNaturalPersonDataForEdit",
            data: ajaxdata,
            success: function () {
                FillPersonDataNew();
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {

        var partyid = item.partyid;

        return   $.ajax({
            type: "GET",
            url: "landrecords/deleteperson/" + partyid + "/" + Personusin,
            data: filter,
            async: false,
            success: function (data) {
                if (data) {

                    FillPersonDataNew();
                    jAlert($.i18n("reg-owner-deleted"), $.i18n("gen-info"),
                            function (response) {
                                FillPersonDataNew();
                            });
                } else {
                    jAlert(
                            $.i18n("err-cant-delete-primary-owner"),
                            $.i18n("gen-info"),
                            function (response) {
                                if (response) {
                                    FillPersonDataNew();
                                }
                            });

                }
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });


    }
};

var personsEditingControllerForNonNaturalPerson = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/NonNaturalpersonsDataNew/" + landId,
            data: filter,
            success: function (data) {
                if (data.length > 0) {
                    $('#addPersonbutton').hide();
                }
//			            	 
            },
        });
    },
    insertItem: function (item) {
        return false;
    },
    updateItem: function (item) {
        return $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/saveNonNaturalPersonForEditing",
            data: JSON.stringify(item),
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        return false;
    }
};

$(window).resize(function () {


});


function loadDisputeForEditing()
{
    // Init editing grid
    $("#DisputesGrid").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: true,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: true,
        controller: DisputeEditingController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", deleteButton: false},
            {name: "landid", title: $.i18n("reg-landid"), type: "text", width: 120, editing: false, validate: {validator: "required", message: $.i18n("err-landid")}},
            {name: "disputetypeid", title: $.i18n("reg-dispute-type"), align: "left", type: "select", items: [{id: 1, name: "Boundary"}, {id: 2, name: "Counter claim (Inter family)"}, {id: 3, name: "Counter claim (Intra family)"}, {id: 4, name: "Counter claim (Others)"}, {id: 5, name: "Other interests"}, ], valueField: "id", textField: "name", width: 80, editing: true, filtering: false},
            {name: "comment", title: $.i18n("gen-comments"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-comments")}},
            {name: "status", title: $.i18n("gen-satatus"), type: "select", items: [{id: 1, name: "Active"}, {id: 2, name: "Resolved"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false}

        ]
    });

    $("#DisputesGrid .jsgrid-table th:first-child :button").click();

    $("#DisputesGrid").jsGrid("loadData");


}

var DisputeEditingController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/disputes/" + landId,
            async: false,
            data: filter,
            success: function (data)
            {
//				            	$("#personsEditingGrid1").jsGrid("loadData");
                //console.log(data);
            }
        }).then(function (result) {
            var persondataArray = [];
            if (result.disputeid) {
                persondataArray.push(result);
                return persondataArray;
            }

        });
    },
    insertItem: function (item) {
        return false;
    },
    updateItem: function (item) {

        var ajaxdata = {
            "disputeUsin": item.landid,
            "cbxDisputeTypes": item.disputetypeid,
            "txtDisputeDescription": item.comment,
            "Status": item.status
        }


        return $.ajax({
            type: "POST",
            traditional: true,
            url: "landrecords/updatedispute",
            data: ajaxdata,
            async: false,
            success: function (data) {
                if (data == "true") {
                    $("#personsEditingGrid1").jsGrid("loadData");
                    $("#DisputesPersonGrid").jsGrid("loadData");
                    loadDisputeForEditing();
                } else {
                    loadDisputeForEditing();
                    jAlert($.i18n("err-resolve-dispute"), $.i18n("err-alert"));
                }
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        return false;
    }
};



function loadDisputePersonForEditing()
{

    // Init editing grid
    $("#DisputesPersonGrid").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: true,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: DisputepersonsEditingController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", deleteButton: false},
            {name: "firstname", title: $.i18n("reg-firstname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middlename", title: $.i18n("reg-middlename"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-middle-name")}},
            {name: "lastname", title: $.i18n("reg-lastname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "address", title: $.i18n("reg-address"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-address")}},
            {name: "identityno", title: $.i18n("reg-id-number"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-idnumber")}},
            {name: "dateofbirth", title: $.i18n("reg-dob"), type: "date", width: 120, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "contactno", title: $.i18n("reg-mobile-num"), type: "text", width: 120},
            {name: "genderid", title: $.i18n("reg-gender"), align: "left", type: "select", items: [{id: 1, name: "Male"}, {id: 2, name: "Female"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false},
            {name: "laPartygroupIdentitytype.identitytypeid", title: $.i18n("reg-id-type"), type: "select", items: [{id: 1, name: "Voter ID"}, {id: 2, name: "Driving license"}, {id: 3, name: "Passport"}, {id: 4, name: "ID card"}, {id: 5, name: "Other"}, {id: 6, name: "None"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false},
            {name: "laPartygroupMaritalstatus.maritalstatusid", title: $.i18n("reg-marital-status"), type: "select", items: [{id: 1, name: "Un-Married"}, {id: 2, name: "Married"}, {id: 3, name: "Divorced"}, {id: 4, name: "Widow"}, {id: 5, name: "Widower"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false},
            {name: "laPartygroupEducationlevel.educationlevelid", title: $.i18n("reg-edulevel"), type: "select", items: [{id: 1, name: "None"}, {id: 2, name: "Primary"}, {id: 3, name: "Secondary"}, {id: 4, name: "University"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false},
            {name: "laPartygroupPersontype.persontypeid", title: $.i18n("reg-person-type"), type: "select", items: [{id: 3, name: "Owner"}, {id: 10, name: "Disputed Person"}], valueField: "id", textField: "name", width: 80, editing: true, filtering: false}
        ]
    });

    $("#DisputesPersonGrid .jsgrid-table th:first-child :button").click();

    $("#DisputesPersonGrid").jsGrid("loadData");


}

var DisputepersonsEditingController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/Disputeperson/" + landId,
            async: false,
            data: filter,
            success: function (data)
            {

            }
        })
    },
    insertItem: function (item) {
        return false;
    },
    updateItem: function (item) {
        var dob = null;
        if (item != null)
        {
            if (item.dateofbirth != null && item.dateofbirth != '')
            {
                var d = new Date(item.dateofbirth);
                dob = d.getFullYear() + '-' + d.getMonth() + '-' + d.getDate();
            }
        }


        var ajaxdata = {
            "personid": item.partyid,
            "firstname": item.firstname,
            "middlename": item.middlename,
            "lastname": item.lastname,
            "address": item.address,
            "identityno": item.identityno,
            "dateofbirth": dob,
            "contactno": item.contactno,
            "genderid": item.genderid,
            "identitytypeid": item.laPartygroupIdentitytype.identitytypeid,
            "maritalstatusid": item.laPartygroupMaritalstatus.maritalstatusid,
            "educationlevel": item.laPartygroupEducationlevel.educationlevelid,
            "persontypeid": item.laPartygroupPersontype.persontypeid,
        }

        return $.ajax({
            type: "POST",
            //contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/updateNaturalPersonDataForEdit",
            //data: JSON.stringify(item),
            data: ajaxdata,
            success: function () {
                $("#personsEditingGrid1").jsGrid("loadData");
                $("#DisputesPersonGrid").jsGrid("loadData");

            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        return false;
    }
};


function loadPOIs(editable) {
    $("#personsEditingGrid").jsGrid({
        width: "100%",
        height: "250px",
        inserting: false,
        editing: editable,
        sorting: false,
        filtering: false,
        paging: true,
        confirmDeleting: true,
        deleteConfirm: $.i18n("gen-delete-confirmation"),
        autoload: false,
        controller: poiController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", editButton: editable, deleteButton: editable, width: 50},
            {name: "firstName", title: $.i18n("reg-firstname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middleName", title: $.i18n("reg-middlename"), type: "text"},
            {name: "lastName", title: $.i18n("reg-lastname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "identityno", title: $.i18n("reg-id-number"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-idnumber")}},
            {name: "gender", title: $.i18n("reg-gender"), align: "left", type: "select", items: makeDropdownArray(genderList, "genderId", "gender", "gender_en"), valueField: "id", textField: "name", width: 120, validate: {validator: "required", message: $.i18n("err-select-gender")}},
            {name: "address", title: $.i18n("reg-address"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-address")}},
            {name: "dob", title: $.i18n("reg-dob"), type: "date", width: 120, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "landid", title: $.i18n("reg-landid"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false},
            {name: "id", title: $.i18n("reg-id"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false},
        ]
    });
    $("#personsEditingGrid .jsgrid-table th:first-child :button").click();
    $("#personsEditingGrid").jsGrid("loadData");
}

var poiController = {
    loadData: function (filter) {
        return $.ajax({
            type: "GET",
            url: "landrecords/personwithinterest/" + landId,
            async: false,
            success: function (data)
            {
                if (!isEmpty(data)) {
                    return data;
                } else {
                    $("#personsEditingGrid").jsGrid("option", "data", []);
                }
            }
        });
    },
    insertItem: function (item) {

    },
    updateItem: function (item) {
        return $.ajax({
            type: "POST",
            traditional: true,
            url: "landrecords/savePersonOfInterestForEditing/" + landId + "/" + _transactionid,
            data: item,
            success: function (data) {
                loadPOIs();
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        var PoiId = item.id;
        if (isEmpty(PoiId)) {
            return;
        }
        return $.ajax({
            type: "GET",
            url: "landrecords/deletepOI/" + PoiId,
            data: filter,
            success: function (data) {
                if (data) {
                    loadPOIs();
                    jAlert($.i18n("reg-poi-deleted"), $.i18n("gen-info"));
                    return data;
                } else {
                    loadPOIs();
                    jAlert($.i18n("err-cant-delete-poi"), $.i18n("gen-info"));
                }
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    }
};


function uploadMediaFile(id)
{


    var flag = false;
    var val1 = 0;
    var formData = new FormData();
    var appid = '#' + "mediafileUpload" + id;
    var file = $("" + appid + "")[0].files[0];

    var alias = $("#alias").val();

    if (typeof (file) === "undefined")
    {

        jAlert($.i18n("err-select-file-to-upload"), $.i18n("err-alert"));
    } else
    {

        $.each($("" + appid + "")[0].files,
                function (ind2, obj2) {
                    $.each($("" + appid + "")[0].files[val1],
                            function (ind3, obj3) {
                                if (ind3 == "type") {
                                    if (obj3 == "image/png"
                                            || obj3 == "image/jpeg"
                                            || obj3 == "image/gif") {
                                        flag = true;
                                    } else {
                                        flag = false;
                                        jAlert($.i18n("err-file-type"));

                                    }
                                }

                            });
                    val1 = val1 + 1;
                });

        if (flag) {
            formData.append("spatialdata", file);
            formData.append("partyid", id);
            formData.append("transid", _transactionid);
            formData.append("landid", Personusin);
            $.ajax({
                url: 'upload/media/',
                type: 'POST',
                data: formData,
                mimeType: "multipart/form-data",
                contentType: false,
                cache: false,
                processData: false,
                success: function (data, textStatus, jqXHR)
                {


                    if (data == "jpg") {
                        jAlert($.i18n("err-select-multimedia-file"), $.i18n("err-alert"));

                    } else
                    {
                        $("" + appid + "").val(null);

                        jAlert($.i18n("reg-file-uploaded"), $.i18n("gen-info"));
                        //displayRefreshedProjectData(project);
//						displaySelectedCategory(project);
                        $('#fileUploadSpatial').val('');
                        $('#alias').val('');
                    }

                }

            });
        }

    }
}



function deleteMediaFile(id) {

    var formData = new FormData();

    formData.append("partyid", id);
    formData.append("transid", _transactionid);
    formData.append("landid", Personusin);

    $.ajax({
        url: 'delete/media/',
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data, textStatus, jqXHR)
        {


            jAlert($.i18n("reg-file-deleted"));


        }

    });
}


function CheckSharetype() {
    var flag = "";
    jQuery.ajax({
        url: "landrecords/checkShareType/" + Personusin,
        async: false,
        success: function (data) {
            flag = data;
            if (flag == "true") {
//		            	$("#personsEditingGrid1").jsGrid("insertItem");
                $("#personsEditingGrid1").jsGrid("insertItem", {});
            } else {
                jAlert(flag, $.i18n("gen-info"));
            }

        }
    });

}

function addPOI() {
    $("#personsEditingGrid").jsGrid("insertItem", {});
}



function FetchdataCorrectionReport(trans_id, land_id, workflowid)
{
    var extent;
    var _extent;
    var _workflowid = workflowid;
    jQuery.ajax(
            {
                type: 'GET',
                url: 'landrecords/landcorrectionreport/' + trans_id + '/' + land_id,
                async: false,
                cache: false,
                success: function (data)
                {

                    if (data[5] != null && data[5].length != 0) {

                        jQuery.get("resources/templates/report/data-correctionnonnaturalperson.html", function (template)
                        {
                            jQuery("#printDiv").empty();
                            jQuery("#printDiv").append(template);

                            if (data != null || data != "" || data != "undefined")
                            {
                                jQuery('#reportNameId').empty();
                                if (_workflowid == 1) {
                                    jQuery('#reportNameId').text($.i18n("viewer-report-datacorrection"));
                                } else if (_workflowid == 2) {
                                    jQuery('#reportNameId').text($.i18n("viewer-landrec-verification-form"));
                                } else if (_workflowid == 3) {
                                    jQuery('#reportNameId').text($.i18n("viewer-landrec-verification-form"));
                                }

                                if (data[0] != null) {
                                    if (data[0][0] != null) {
                                        if (data[0][0].region != null)
                                            $('#regionId').html(data[0][0].region);

                                        if (data[0][0].commune != null)
                                            $('#CommunityId').html(data[0][0].commune);

                                        if (data[0][0].county != null)
                                            $('#countryId').html(data[0][0].county);

                                        if (data[0][0].projectName != null)
                                            $('#project_nameId').html(data[0][0].projectName);


                                        if (data[0][0].claimno != null)
                                            $('#claimNumberId').html(data[0][0].claimno);

                                        if (data[0][0].claimtype != null)
                                            $('#claimTypeId').html(data[0][0].claimtype);

                                        if (data[0][0].transactionid != null)
                                            $('#LandRecordNumberId').html(data[0][0].transactionid);

                                        if (data[0][0].claimdate != null)
                                            $('#claimDateId').html(data[0][0].claimdate);

                                        if (data[0][0].landusetype != null)
                                            $('#ExistingUseId').html(data[0][0].landusetype);

                                        if (data[0][0].proposedused != null)
                                            $('#ProposedUseId').html(data[0][0].proposedused);

                                        if (data[0][0].landtype != null)
                                            $('#LandTypeId').html(data[0][0].landtype);

                                        if (data[0][0].neighbor_east != null)
                                            $('#NeighborEastId').html(data[0][0].neighbor_east);

                                        if (data[0][0].neighbor_west != null)
                                            $('#NeighborWestId').html(data[0][0].neighbor_west);


                                        if (data[0][0].neighbor_north != null)
                                            $('#NeighborNorthId').html(data[0][0].neighbor_north);

                                        if (data[0][0].neighbor_south != null)
                                            $('#NeighborSouthId').html(data[0][0].neighbor_south);


                                        if (data[0][0].landsharetype != null)
                                            $('#TypeOftenureId').html(data[0][0].landsharetype);

                                        if (data[0][0].occupancylength != null)
                                            $('#YearsOfOccupancyId').html(data[0][0].occupancylength);

                                        if (data[0][0].tenureclasstype != null)
                                            $('#TypeofRightId').html(data[0][0].tenureclasstype);

                                        if (data[0][0].claimtype != null)
                                            $('#TypeOdClaimId').html(data[0][0].claimtype);

                                        if (data[0][0].landno != null)
                                            $('#plotId').html(data[0][0].landId);

                                        if (data[0][0].area != null)
                                            $('#correctionForm_SizeId').html(data[0][0].area);

                                        if (data[0][0].other_use != null) {
                                            $('#reportother_use').html(data[0][0].other_use);
                                        }



                                    }

                                }

                                if (data[1] != null) {
                                    for (var i = 0; i < data[1].length; i++) {

                                        if (data[1][i] != null) {

                                            jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIRecordsRowData1");


                                        }
                                    }
                                }

                                if (data[5] != null) {
                                    if (data[5].length > 2) {

                                        if (data[5][2] != null) {
                                            if (data[5][2].firstname != null || data[5][2].middlename != null || data[5][2].lastname != null) {

                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][2]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }
                                        }
                                        if (data[5][1] != null) {
                                            if (data[5][1].firstname != null || data[5][1].middlename != null || data[5][1].lastname != null) {
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][1]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                        if (data[5][0] != null) {
                                            if (data[5][0].firstname != null || data[5][0].middlename != null || data[5][0].lastname != null) {
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                    } else if (data[5].length > 1 && data[5].length < 3) {

                                        if (data[5][1] != null) {
                                            if (data[5][1].firstname != null || data[5][1].middlename != null || data[5][1].lastname != null) {
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][1]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }
                                        }
                                        if (data[5][0] != null) {
                                            if (data[5][0].firstname != null || data[5][0].middlename != null || data[5][0].lastname != null) {
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                    } else if (data[5].length == 1) {

                                        jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");

                                    }

                                }
                                var layerName = "spatialUnitLand";
                                var objLayer = getLayerByAliesName(layerName);

                                var _wfsurl = objLayer.values_.url;
                                var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
                                ;

                                //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
                                $.ajax({
                                    url: PROXY_PATH + _wfsSchema,
                                    async: false,
                                    success: function (data) {
                                        _featureNS = data.targetNamespace;

                                    }
                                });

                                var relLayerName = "Mast:la_spatialunit_land";
                                var fieldName = "landid";
                                var fieldVal = land_id;

                                var _featureTypes = [];
                                _featureTypes.push("la_spatialunit_land");
                                var _featurePrefix = "Mast";
                                var featureRequest1 = new ol.format.WFS().writeGetFeature({
                                    srsName: 'EPSG:4326',
                                    featureNS: _featureNS,
                                    featurePrefix: _featurePrefix,
                                    featureTypes: _featureTypes,
                                    outputFormat: 'application/json',
                                    filter: ol.format.filter.equalTo(fieldName, fieldVal)
                                });


                                var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
                                fetch(_url, {
                                    method: 'POST',
                                    body: new XMLSerializer().serializeToString(featureRequest1)
                                }).then(function (response) {
                                    return response.json();
                                }).then(function (json) {
                                    var features = new ol.format.GeoJSON().readFeatures(json);

                                    var vectorSource = new ol.source.Vector();
                                    vectorSource.addFeatures(features);
                                    extent = vectorSource.getExtent();
                                    var cqlFilter = 'landid=' + fieldVal;
                                    var _extent = extent.slice();

                                    extent[0] = extent[0] - 2.00017;
                                    extent[1] = extent[1] - 2.00017;
                                    extent[2] = extent[2] + 2.00017;
                                    extent[3] = extent[3] + 2.00017;



                                    var vertexlist1 = features[0].values_.geometry.clone().getCoordinates();

                                    var tempStr = "";
                                    for (var i = 0; i < vertexlist1[0].length; i++)
                                    {

                                        if (tempStr == "") {
                                            tempStr = vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];

                                        } else {
                                            tempStr = tempStr + "," + vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];
                                        }


                                    }


                                    var serverData = {"vertexList": tempStr};
                                    $.ajax({
                                        type: 'POST',
                                        url: "landrecords/vertexlabel",
                                        data: serverData,
                                        async: false,
                                        success: function (data) {

                                        }
                                    });

                                    var url1 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + _extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=245&height=243&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";
                                    var url2 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=600&height=350&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";



                                    jQuery('#mapImageId').empty();
                                    jQuery('#mapImageId').append('<img  src=' + url1 + '>');

                                    jQuery('#mapImageId1').empty();
                                    jQuery('#mapImageId1').append('<img  src=' + url2 + '>');


                                    jQuery('#vertexTableBody_map').empty();
                                    vertexTableList = [];
                                    var _index = 1;
                                    for (var i = 0; i < vertexlist1[0].length; i++) {
                                        var tempList = [];
                                        tempList["index"] = _index;
                                        tempList["x"] = (vertexlist1[0][i][0]).toFixed(5);
                                        tempList["y"] = (vertexlist1[0][i][1]).toFixed(5);
                                        vertexTableList.push(tempList);
                                        _index = _index + 1;
                                    }
                                    jQuery("#vertexTable_map").tmpl(vertexTableList).appendTo("#vertexTableBody_map");




                                    var html = $("#printDiv").html();
                                    var printWindow = window.open('', 'popUpWindow', 'height=600,width=950,left=40,top=20,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                                    printWindow.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN""http://www.w3.org/TR/html4/strict.dtd">' +
                                            '<html><head>' + ' <link rel="stylesheet" href="/mast/resources/styles/complete-style.css" type="text/css" />'
                                            + '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>' +
                                            '<script src="../resources/scripts/cloudburst/viewer/Print.js"></script>'
                                            + '</head><body>' + html + '</body></html>');

                                    printWindow.document.close();


                                });


                            } else
                            {
                                jAlert($.i18n("err-fetch-details"), $.i18n("gen-info"));
                            }
                        });


                    } else {
                        jQuery.get("resources/templates/report/data-correction.html", function (template)
                        {
                            jQuery("#printDiv").empty();
                            jQuery("#printDiv").append(template);

                            if (data != null || data != "" || data != "undefined")
                            {
                                jQuery('#reportNameId').empty();
                                if (_workflowid == 1) {
                                    jQuery('#reportNameId').text($.i18n("viewer-report-datacorrection"));
                                } else if (_workflowid == 2) {
                                    jQuery('#reportNameId').text($.i18n("viewer-landrec-verification-form"));
                                } else if (_workflowid == 3) {
                                    jQuery('#reportNameId').text($.i18n("viewer-landrec-verification-form"));
                                }

                                if (data[0] != null) {
                                    if (data[0][0] != null) {
                                        if (data[0][0].region != null)
                                            $('#regionId').html(data[0][0].region);

                                        if (data[0][0].commune != null)
                                            $('#CommunityId').html(data[0][0].commune);

                                        if (data[0][0].county != null)
                                            $('#countryId').html(data[0][0].county);

                                        if (data[0][0].projectName != null)
                                            $('#project_nameId').html(data[0][0].projectName);


                                        if (data[0][0].claimno != null)
                                            $('#claimNumberId').html(data[0][0].claimno);

                                        if (data[0][0].claimtype != null)
                                            $('#claimTypeId').html(data[0][0].claimtype);

                                        if (data[0][0].transactionid != null)
                                            $('#LandRecordNumberId').html(data[0][0].transactionid);

                                        if (data[0][0].claimdate != null)
                                            $('#claimDateId').html(data[0][0].claimdate);

                                        if (data[0][0].landusetype != null)
                                            $('#ExistingUseId').html(data[0][0].landusetype);

                                        if (data[0][0].proposedused != null)
                                            $('#ProposedUseId').html(data[0][0].proposedused);

                                        if (data[0][0].landtype != null)
                                            $('#LandTypeId').html(data[0][0].landtype);

                                        if (data[0][0].neighbor_east != null)
                                            $('#NeighborEastId').html(data[0][0].neighbor_east);

                                        if (data[0][0].neighbor_west != null)
                                            $('#NeighborWestId').html(data[0][0].neighbor_west);


                                        if (data[0][0].neighbor_north != null)
                                            $('#NeighborNorthId').html(data[0][0].neighbor_north);

                                        if (data[0][0].neighbor_south != null)
                                            $('#NeighborSouthId').html(data[0][0].neighbor_south);


                                        if (data[0][0].landsharetype != null)
                                            $('#TypeOftenureId').html(data[0][0].landsharetype);

                                        if (data[0][0].occupancylength != null)
                                            $('#YearsOfOccupancyId').html(data[0][0].occupancylength);

                                        if (data[0][0].tenureclasstype != null)
                                            $('#TypeofRightId').html(data[0][0].tenureclasstype);

                                        if (data[0][0].claimtype != null)
                                            $('#TypeOdClaimId').html(data[0][0].claimtype);

                                        if (data[0][0].landno != null)
                                            $('#plotId').html(data[0][0].landId);


                                        if (data[0][0].area != null)
                                            $('#correctionForm_SizeId').html(data[0][0].area);

                                        if (data[0][0].other_use != null) {
                                            $('#reportother_use').html(data[0][0].other_use);
                                        }




                                    }

                                }

                                if (data[1] != null) {
                                    for (var i = 0; i < data[1].length; i++) {

                                        if (data[1][i] != null) {

                                            jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIRecordsRowData1");


                                        }
                                    }
                                }

                                if (data[2] != null) {
                                    if (data[2].length > 2) {
                                        if (data[2][2] != null) {
                                            if (data[2][2].firstname != null || data[2][2].middlename != null || data[2][2].lastname != null) {
                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][2]).appendTo("#OwnerRecordsRowData1");
                                            }
                                        }
                                        if (data[2][1] != null) {
                                            if (data[2][1].firstname != null || data[2][1].middlename != null || data[2][1].lastname != null) {
                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][1]).appendTo("#OwnerRecordsRowData1");
                                            }

                                        }

                                        if (data[2][0] != null) {
                                            if (data[2][0].firstname != null || data[2][0].middlename != null || data[2][0].lastname != null) {
                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][0]).appendTo("#OwnerRecordsRowData1");
                                            }

                                        }

                                    } else if (data[2].length > 1 && data[2].length < 3) {
                                        if (data[2][1] != null) {
                                            if (data[2][1].firstname != null || data[2][1].middlename != null || data[2][1].lastname != null) {
                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][1]).appendTo("#OwnerRecordsRowData1");
                                            }
                                        }
                                        if (data[2][0] != null) {
                                            if (data[2][0].firstname != null || data[2][0].middlename != null || data[2][0].lastname != null) {
                                                jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][0]).appendTo("#OwnerRecordsRowData1");
                                            }

                                        }

                                    } else if (data[2].length == 1) {
                                        jQuery("#OwnerRecordsAttrTemplate1").val("");
                                        jQuery("#OwnerRecordsAttrTemplate1").tmpl(data[2][0]).appendTo("#OwnerRecordsRowData1");

                                    }

                                }


                                if (data[2] != null) {

                                    $('#OwnerRecordsRowData1').empty();


                                    for (var i = 0; i < data[2].length; i++) {
                                        $("#OwnerRecordsAttrTemplate1").tmpl(data[2][i]).appendTo("#OwnerRecordsRowData1");
                                    }

                                }
                                var layerName = "spatialUnitLand";
                                var objLayer = getLayerByAliesName(layerName);

                                var _wfsurl = objLayer.values_.url;
                                var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
                                ;

                                //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
                                $.ajax({
                                    url: PROXY_PATH + _wfsSchema,
                                    async: false,
                                    success: function (data) {
                                        _featureNS = data.targetNamespace;

                                    }
                                });

                                var relLayerName = "Mast:la_spatialunit_land";
                                var fieldName = "landid";
                                var fieldVal = land_id;

                                var _featureTypes = [];
                                _featureTypes.push("la_spatialunit_land");
                                var _featurePrefix = "Mast";
                                var featureRequest1 = new ol.format.WFS().writeGetFeature({
                                    srsName: 'EPSG:4326',
                                    featureNS: _featureNS,
                                    featurePrefix: _featurePrefix,
                                    featureTypes: _featureTypes,
                                    outputFormat: 'application/json',
                                    filter: ol.format.filter.equalTo(fieldName, fieldVal)
                                });


                                var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
                                fetch(_url, {
                                    method: 'POST',
                                    body: new XMLSerializer().serializeToString(featureRequest1)
                                }).then(function (response) {
                                    return response.json();
                                }).then(function (json) {
                                    var features = new ol.format.GeoJSON().readFeatures(json);

                                    var vectorSource = new ol.source.Vector();
                                    vectorSource.addFeatures(features);
                                    extent = vectorSource.getExtent();
                                    var cqlFilter = 'landid=' + fieldVal;

                                    var _extent = extent.slice();

                                    extent[0] = extent[0] - 2.00017;
                                    extent[1] = extent[1] - 2.00017;
                                    extent[2] = extent[2] + 2.00017;
                                    extent[3] = extent[3] + 2.00017;


                                    var vertexlist1 = features[0].values_.geometry.clone().getCoordinates();

                                    var tempStr = "";
                                    for (var i = 0; i < vertexlist1[0].length; i++)
                                    {

                                        if (tempStr == "") {
                                            tempStr = vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];

                                        } else {
                                            tempStr = tempStr + "," + vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];
                                        }


                                    }

                                    var serverData = {"vertexList": tempStr};
                                    $.ajax({
                                        type: 'POST',
                                        url: "landrecords/vertexlabel",
                                        data: serverData,
                                        async: false,
                                        success: function (data) {

                                        }
                                    });

                                    var url1 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + _extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=245&height=243&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";
                                    var url2 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=600&height=350&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";

                                    jQuery('#mapImageId').empty();
                                    jQuery('#mapImageId').append('<img  src=' + url1 + '>');

                                    jQuery('#mapImageId1').empty();
                                    jQuery('#mapImageId1').append('<img  src=' + url2 + '>');

                                    jQuery('#vertexTableBody_map').empty();
                                    vertexTableList = [];
                                    var _index = 1;
                                    for (var i = 0; i < vertexlist1[0].length; i++) {
                                        var tempList = [];
                                        tempList["index"] = _index;
                                        tempList["x"] = (vertexlist1[0][i][0]).toFixed(5);
                                        tempList["y"] = (vertexlist1[0][i][1]).toFixed(5);
                                        vertexTableList.push(tempList);
                                        _index = _index + 1;
                                    }
                                    jQuery("#vertexTable_map").tmpl(vertexTableList).appendTo("#vertexTableBody_map");



                                    var html = $("#printDiv").html();
                                    var printWindow = window.open('', 'popUpWindow', 'height=600,width=950,left=40,top=20,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                                    printWindow.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN""http://www.w3.org/TR/html4/strict.dtd">' +
                                            '<html><head>' + ' <link rel="stylesheet" href="/mast/resources/styles/complete-style.css" type="text/css" />'
                                            + '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>' +
                                            '<script src="../resources/scripts/cloudburst/viewer/Print.js"></script>'
                                            + '</head><body>' + html + '</body></html>');

                                    printWindow.document.close();


                                });


                            } else
                            {
                                jAlert($.i18n("err-fetch-details"), $.i18n("err-alert"));
                            }
                        });
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jAlert('info', "", "");
                }
            });
}


function _generateFinalLandForm(trans_id, land_id)
{

    jQuery.ajax(
            {
                type: 'GET',
                url: 'landrecords/landcorrectionreport/' + trans_id + '/' + land_id,
                async: false,
                cache: false,
                success: function (data)
                {
                    if (data[5] != null && data[5].length != 0) {

                        jQuery.get("resources/templates/report/land-certificatenonnaturalperson.html", function (template)
                        {
                            jQuery("#printDiv").empty();
                            jQuery("#printDiv").append(template);

                            if (data != null || data != "" || data != "undefined")
                            {

                                if (data[0] != null) {
                                    if (data[0][0] != null) {
                                        if (data[0][0].region != null || data[0][0].commune != null || data[0][0].province != null)
                                            $('#comp_address').text(data[0][0].region + ", " + data[0][0].commune + ", " + data[0][0].province);

                                        if (data[0][0].region != null)
                                            $('#Community').text(data[0][0].region);

                                        if (data[0][0].province != null)
                                            $('#provence').text(data[0][0].province);


                                        if (data[0][0].projectName != null)
                                            $('#project').text(data[0][0].projectName);

                                        if (data[0][0].area != null)
                                            $('#area_report').text(data[0][0].area);

                                        if (data[0][0].transactionid != null)
                                            $('#reg_nononnatural').text(data[0][0].transactionid);

                                        if (data[0][0].landno != "")
                                            $('#parcel_no').text(data[0][0].landno);
                                        /* if(data[0][0].claimdate!=null)
                                         $('#claimDateId').html(data[0][0].claimdate); */

                                        if (data[0][0].landusetype != null)
                                            $('#cert_existing_use').text(data[0][0].landusetype);

                                        if (data[0][0].proposedused != null)
                                            $('#cert_proposed_use').text(data[0][0].proposedused);

                                        /*if(data[0][0].landtype!=null)
                                         $('#LandTypeId').html(data[0][0].landtype); */

                                        if (data[0][0].neighbor_east != null)
                                            $('#East_boundary').text(data[0][0].neighbor_east);

                                        if (data[0][0].neighbor_west != null)
                                            $('#West_boundary').text(data[0][0].neighbor_west);


                                        if (data[0][0].neighbor_north != null)
                                            $('#North_boundary').text(data[0][0].neighbor_north);

                                        if (data[0][0].neighbor_south != null)
                                            $('#South_boundary').text(data[0][0].neighbor_south);

                                        if (data[0][0].claimdate != null)
                                            $('#date').text(data[0][0].claimdate);

                                        if (data[0][0].landsharetype != null)
                                            if (data[0][0].landsharetype == "Single Tenancy") {
                                                $('#jointownertable').hide();
                                                $('#jointownertable_2').hide();

                                            } else if (data[0][0].landsharetype == "Joint Tenancy") {

                                                $('#jointownertable').show();
                                                $('#jointownertable_2').hide();
                                            } else {
                                                $('#jointownertable_2').show();
                                            }

                                    }

                                }

                                if (data[1] != null) {
                                    for (var i = 0; i < data[1].length; i++) {

                                        if (data[1][i] != null) {

                                            jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIRecordsRowData1");


                                        }
                                    }
                                }
                                if (data[5] != null) {
                                    if (data[5].length > 2) {
                                        $('#jointownertable').show();
                                        $('#jointownertable_2').show();
                                        if (data[5][2] != null) {
                                            if (data[5][2].firstname != null || data[5][2].middlename != null || data[5][2].lastname != null) {
                                                $('#Owner_nameNonperson').text(data[5][2].firstname + " " + data[5][2].middlename + " " + data[5][2].lastname);
                                                $('#owner_Nonperson').text(data[5][2].organizationname);
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][2]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }
                                        }
                                        if (data[5][1] != null) {
                                            if (data[5][1].firstname != null || data[5][1].middlename != null || data[5][1].lastname != null) {
                                                $('#jointOwner_nameNonperson').text(data[5][1].firstname + " " + data[5][1].middlename + " " + data[5][1].lastname);
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][1]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                        if (data[5][0] != null) {
                                            if (data[5][0].firstname != null || data[5][0].middlename != null || data[5][0].lastname != null) {
                                                $('#jointOwner_name2Nonperson').text(data[5][0].firstname + " " + data[5][0].middlename + " " + data[5][0].lastname);
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                    } else if (data[5].length > 1 && data[5].length < 3) {
                                        $('#jointownertable').hide();

                                        if (data[5][1] != null) {
                                            if (data[5][1].firstname != null || data[5][1].middlename != null || data[5][1].lastname != null) {
                                                $('#Owner_nameNonperson').text(data[5][1].firstname + " " + data[5][1].middlename + " " + data[5][1].lastname);
                                                $('#owner_Nonperson').text(data[5][1].organizationname);
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][1]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }
                                        }
                                        if (data[5][0] != null) {
                                            if (data[5][0].firstname != null || data[5][0].middlename != null || data[5][0].lastname != null) {
                                                $('#jointOwner_nameNonperson').text(data[5][0].firstname + " " + data[5][0].middlename + " " + data[5][0].lastname);
                                                jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");
                                            }

                                        }

                                    } else if (data[5].length == 1) {

                                        $('#jointownertable').hide();
                                        $('#jointownertable_2').hide();
                                        $('#Owner_nameNonperson').text(data[5][0].firstname + " " + data[5][0].middlename + " " + data[5][0].lastname);
                                        $('#owner_Nonperson').text(data[5][0].organizationname);
                                        jQuery("#OwnerNonpersonRecordsAttrTemplate1").tmpl(data[5][0]).appendTo("#OwnerNonpersonRecordsRowData1");

                                    }

                                }
                                /*	if(data[0][0].landsharetype!=null)
                                 $('#TypeOftenureId').html(data[0][0].landsharetype);  
                                 
                                 if(data[0][0].occupancylength!=null)
                                 $('#YearsOfOccupancyId').html(data[0][0].occupancylength); 
                                 
                                 if(data[0][0].tenureclasstype!=null)
                                 $('#TypeofRightId').html(data[0][0].tenureclasstype);  */


                                if (data[3] != null) {

                                    if (data[3].length > 2) {
                                        for (var i = 0; i < data[3].length; i++) {
                                            if (i == 2) {
                                                var url1 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                                jQuery('#imagePersonId').append('<img  src=' + url1 + '>');
                                            } else if (i == 1) {
                                                var url2 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                                jQuery('#imagejontPersonId').append('<img  src=' + url2 + '>');
                                            } else if (i == 0) {
                                                var url2 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                                jQuery('#imagejontPersonId_2').append('<img  src=' + url2 + '>');
                                            }
                                        }
                                    } else if (data[3].length > 1 && data[3].length < 3) {
                                        for (var i = 0; i < data[3].length; i++) {
                                            if (i == 1) {
                                                var url1 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                                jQuery('#imagePersonId').append('<img  src=' + url1 + '>');
                                            } else if (i == 0) {
                                                var url2 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;
                                                jQuery('#imagejontPersonId').append('<img  src=' + url2 + '>');
                                            }
                                        }
                                    } else if (data[3].length == 1) {
                                        var url1 = "http://" + location.host + "/mast_files" + data[3][0].documentlocation + "/" + data[3][0].documentname;
                                        jQuery('#imagePersonId').append('<img  src=' + url1 + '>');

                                    }

                                }

                                if (data[4] != null) {
                                    var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                    jQuery('#imageSignature').append('<img  src=' + url2 + '>');
                                    jQuery('#authorizedmember_Id').text(data[4].authorizedmember);

                                }



                                var layerName = "spatialUnitLand";
                                var objLayer = getLayerByAliesName(layerName);

                                var _wfsurl = objLayer.values_.url;
                                var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
                                ;

                                //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
                                $.ajax({
                                    url: PROXY_PATH + _wfsSchema,
                                    async: false,
                                    success: function (data) {
                                        _featureNS = data.targetNamespace;

                                    }
                                });

                                var relLayerName = "Mast:la_spatialunit_land";
                                var fieldName = "landid";
                                var fieldVal = land_id;

                                var _featureTypes = [];
                                _featureTypes.push("la_spatialunit_land");
                                var _featurePrefix = "Mast";
                                var featureRequest1 = new ol.format.WFS().writeGetFeature({
                                    srsName: 'EPSG:4326',
                                    featureNS: _featureNS,
                                    featurePrefix: _featurePrefix,
                                    featureTypes: _featureTypes,
                                    outputFormat: 'application/json',
                                    filter: ol.format.filter.equalTo(fieldName, fieldVal)
                                });


                                var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
                                fetch(_url, {
                                    method: 'POST',
                                    body: new XMLSerializer().serializeToString(featureRequest1)
                                }).then(function (response) {
                                    return response.json();
                                }).then(function (json) {
                                    var features = new ol.format.GeoJSON().readFeatures(json);

                                    var vectorSource = new ol.source.Vector();
                                    vectorSource.addFeatures(features);
                                    extent = vectorSource.getExtent();
                                    var cqlFilter = 'landid=' + fieldVal;
                                    var vertexlist1 = features[0].values_.geometry.clone().getCoordinates();

                                    var tempStr = "";
                                    for (var i = 0; i < vertexlist1[0].length; i++)
                                    {

                                        if (tempStr == "") {
                                            tempStr = vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];

                                        } else {
                                            tempStr = tempStr + "," + vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];
                                        }


                                    }

                                    var serverData = {"vertexList": tempStr};
                                    $.ajax({
                                        type: 'POST',
                                        url: "landrecords/vertexlabel",
                                        data: serverData,
                                        async: false,
                                        success: function (data) {

                                        }
                                    });

                                    var url1 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=245&height=243&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";

                                    jQuery('#mapImageId01').empty();
                                    jQuery('#mapImageId01').append('<img  src=' + url1 + '>');


                                    var html2 = $("#printdiv2").html();

                                    var printWindow = window.open('', 'popUpWindow', 'height=600,width=950,left=40,top=20,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                                    printWindow.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN""http://www.w3.org/TR/html4/strict.dtd">' +
                                            '<html><head><title>Report</title>' + ' <link rel="stylesheet" href="/mast/resources/styles/style.css" type="text/css" />'
                                            + '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>' + ' <link rel="stylesheet" href="/mast/resources/styles/complete-style1.css" type="text/css" />'
                                            + '<script src="../resources/scripts/cloudburst/viewer/Print.js"></script>'
                                            + '</head><body>' + html2 + '</body></html>');

                                    printWindow.document.close();

                                });



                            } else
                            {
                                jAlert($.i18n("err-fetch-details"), $.i18n("err-alert"));
                            }
                        });


                    } else {
                        jQuery.get("resources/templates/report/land-certificate.html", function (template)
                        {
                            jQuery("#printDiv").empty();
                            jQuery("#printDiv").append(template);
                            var ownersname = "";

                            if (data != null || data != "" || data != "undefined")
                            {

                                if (data[0] != null) {
                                    if (data[0][0] != null) {
                                        if (data[0][0].region != null || data[0][0].commune != null || data[0][0].province != null)
                                            $('#comp_address').text(data[0][0].region + ", " + data[0][0].commune + ", " + data[0][0].province);

                                        if (data[0][0].region != null)
                                            $('#Community').text(data[0][0].region);

                                        if (data[0][0].province != null)
                                            $('#provence').text(data[0][0].province);


                                        if (data[0][0].projectName != null)
                                            $('#project').text(data[0][0].projectName);

                                        if (data[0][0].area != null)
                                            $('#area_report').text(data[0][0].area);

                                        if (data[0][0].transactionid != null)
                                            $('#reg_noSingle').text(data[0][0].transactionid);

                                        if (data[0][0].landno != "")
                                            $('#parcel_no').text(data[0][0].landno);
                                        /* if(data[0][0].claimdate!=null)
                                         $('#claimDateId').html(data[0][0].claimdate); */

                                        if (data[0][0].landusetype != null)
                                            $('#cert_existing_use').text(data[0][0].landusetype);

                                        if (data[0][0].proposedused != null)
                                            $('#cert_proposed_use').text(data[0][0].proposedused);

                                        /*if(data[0][0].landtype!=null)
                                         $('#LandTypeId').html(data[0][0].landtype); */

                                        if (data[0][0].neighbor_east != null)
                                            $('#East_boundary').text(data[0][0].neighbor_east);

                                        if (data[0][0].neighbor_west != null)
                                            $('#West_boundary').text(data[0][0].neighbor_west);


                                        if (data[0][0].neighbor_north != null)
                                            $('#North_boundary').text(data[0][0].neighbor_north);

                                        if (data[0][0].neighbor_south != null)
                                            $('#South_boundary').text(data[0][0].neighbor_south);

                                        if (data[0][0].claimdate != null)
                                            $('#date').text(data[0][0].claimdate);

                                        if (data[0][0].landsharetype != null)
                                            if (data[0][0].landsharetype == "Single Tenancy") {
                                                $('#jointownertable').hide();
                                                $('#jointownertable_2').hide();

                                            } else if (data[0][0].landsharetype == "Joint Tenancy") {

                                                $('#jointownertable').show();
                                                $('#jointownertable_2').hide();
                                            } else {
                                                $('#jointownertable_2').show();
                                            }

                                    }

                                }

                                if (data[1] != null) {
                                    for (var i = 0; i < data[1].length; i++) {

                                        if (data[1][i] != null) {

                                            jQuery("#POIRecordsAttrTemplate1").tmpl(data[1][i]).appendTo("#POIRecordsRowData1");


                                        }
                                    }
                                }

                                if (data[2] != null) {

                                    $('#OwnerRecordsRowData1').empty();
                                    if (data[2].length > 2) {
                                        $('#jointownertable_2').show();

                                    } else {

                                        $('#jointownertable_2').hide();
                                    }

                                    for (var i = 0; i < data[2].length; i++) {
                                        if (ownersname == "") {
                                            ownersname = data[2][i].firstname + " " + data[2][i].middlename + " " + data[2][i].lastname;

                                        } else {
                                            ownersname = ownersname + " and " + data[2][i].firstname + " " + data[2][i].middlename + " " + data[2][i].lastname;
                                        }

                                        if (i == 0) {
//											$('#owner').text(data[2][i].firstname+" "+data[2][i].middlename+" "+data[2][i].lastname);
                                            $("#FirstOwnerTemplate").tmpl(data[2][i]).appendTo("#FirstOwnerRowData1");
                                        } else {
                                            $("#jointOwnerTemplate").tmpl(data[2][i]).appendTo("#jointOwnerRowData1");
                                        }

                                        /*else if(i==2){
                                         $('#jointOwner_name2').text(data[2][i].firstname+" "+data[2][i].middlename+" "+data[2][i].lastname);
                                         
                                         }
                                         */
                                        $("#OwnerRecordsAttrTemplate1").tmpl(data[2][i]).appendTo("#OwnerRecordsRowData1");
                                    }

                                }
                                $('#owner').text(ownersname);
                                if (data[3] != null) {

                                    for (var i = 0; i < data[3].length; i++) {
                                        if (null != data[3][i].laParty) {
                                            if (null != data[3][i].laParty.partyid) {
                                                var url1 = "http://" + location.host + "/mast_files" + data[3][i].documentlocation + "/" + data[3][i].documentname;

                                                jQuery('#imagePersonId_' + data[3][i].laParty.partyid).append('<img  src=' + url1 + '>');
                                            }
                                        }


                                    }
                                }

                                if (data[4] != null) {
                                    var url2 = "http://" + location.host + "/mast_files" + "/resources/signatures" + "/" + data[4].authorizedmembersignature;
                                    jQuery('#imageSignature').append('<img  src=' + url2 + '>');
                                    jQuery('#authorizedmember_Id').text(data[4].authorizedmember);
                                }

                                var layerName = "spatialUnitLand";
                                var objLayer = getLayerByAliesName(layerName);

                                var _wfsurl = objLayer.values_.url;
                                var _wfsSchema = _wfsurl + "request=DescribeFeatureType&version=1.1.0&typename=" + objLayer.values_.name + "&maxFeatures=1&outputFormat=application/json";
                                ;

                                //Get Geometry column name, featureTypes, targetNamespace for the selected layer object //
                                $.ajax({
                                    url: PROXY_PATH + _wfsSchema,
                                    async: false,
                                    success: function (data) {
                                        _featureNS = data.targetNamespace;

                                    }
                                });

                                var relLayerName = "Mast:la_spatialunit_land";
                                var fieldName = "landid";
                                var fieldVal = land_id;

                                var _featureTypes = [];
                                _featureTypes.push("la_spatialunit_land");
                                var _featurePrefix = "Mast";
                                var featureRequest1 = new ol.format.WFS().writeGetFeature({
                                    srsName: 'EPSG:4326',
                                    featureNS: _featureNS,
                                    featurePrefix: _featurePrefix,
                                    featureTypes: _featureTypes,
                                    outputFormat: 'application/json',
                                    filter: ol.format.filter.equalTo(fieldName, fieldVal)
                                });


                                var _url = window.location.protocol + '//' + window.location.host + '/geoserver/wfs';
                                fetch(_url, {
                                    method: 'POST',
                                    body: new XMLSerializer().serializeToString(featureRequest1)
                                }).then(function (response) {
                                    return response.json();
                                }).then(function (json) {
                                    var features = new ol.format.GeoJSON().readFeatures(json);

                                    var vectorSource = new ol.source.Vector();
                                    vectorSource.addFeatures(features);
                                    extent = vectorSource.getExtent();
                                    var cqlFilter = 'landid=' + fieldVal;

                                    var vertexlist1 = features[0].values_.geometry.clone().getCoordinates();

                                    var tempStr = "";
                                    for (var i = 0; i < vertexlist1[0].length; i++)
                                    {

                                        if (tempStr == "") {
                                            tempStr = vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];

                                        } else {
                                            tempStr = tempStr + "," + vertexlist1[0][i][0] + "," + vertexlist1[0][i][1];
                                        }


                                    }

                                    var serverData = {"vertexList": tempStr};
                                    $.ajax({
                                        type: 'POST',
                                        url: "landrecords/vertexlabel",
                                        data: serverData,
                                        async: false,
                                        success: function (data) {

                                        }
                                    });

                                    var url1 = "http://" + location.host + "/geoserver/wms?" + "bbox=" + extent + "&styles=&format_options=layout:getMap&FORMAT=image/png&REQUEST=GetMap&layers=Mast:LBR_district,Mast:vertexlabel,Mast:la_spatialunit_land&width=245&height=243&srs=EPSG:4326" + "&CQL_FILTER;;INCLUDE;INCLUDE;landid=" + fieldVal + ";";


                                    jQuery('#mapImageId01').empty();
                                    jQuery('#mapImageId01').append('<img  src=' + url1 + '>');


                                    var html2 = $("#printdiv2").html();

                                    var printWindow = window.open('', 'popUpWindow', 'height=600,width=950,left=40,top=20,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                                    printWindow.document.write('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN""http://www.w3.org/TR/html4/strict.dtd">' +
                                            '<html><head><title>Report</title>' + ' <link rel="stylesheet" href="/mast/resources/styles/style.css" type="text/css" />'
                                            + '<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>' + ' <link rel="stylesheet" href="/mast/resources/styles/complete-style1.css" type="text/css" />'
                                            + '<script src="../resources/scripts/cloudburst/viewer/Print.js"></script>'
                                            + '</head><body>' + html2 + '</body></html>');

                                    printWindow.document.close();

                                });



                            } else
                            {
                                jAlert($.i18n("err-fetch-details"), $.i18n("err-alert"));
                            }
                        });
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jAlert('info', "", "");
                }
            });
}

function landDocs(id) {
    $.ajax({
        type: "GET",
        url: "landrecords/landDocs/" + id,
        data: filter,
        success: function (data) {
            if (data.length > 0) {
                $("#genmultimediaRowData").empty();
                $("#landmediaTemplate_add").tmpl(data).appendTo("#genmultimediaRowData");
                $("#genmultimediaRowData").i18n();
            }
        }
    });
}

function viewMultimediaByLandId(id) {

    var flag = false;
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: "landrecords/landmediaavail/" + id,
        success: function (result) {
            if (result == true) {
                flag = true;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });

    if (flag) {
        window.open("landrecords/downloadlandmedia/" + id, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
    } else {

        jAlert($.i18n("err-file-not-found"), $.i18n("err-alert"));
    }
}


function uploadLandMediaFile(id) {
    var flag = false;
    var val1 = 0;
    var formData = new FormData();
    var appid = '#' + "landmediafileUpload" + id;
    var file = $("" + appid + "")[0].files[0];

    var alias = $("#alias").val();

    if (typeof (file) === "undefined")
    {

        jAlert($.i18n("err-select-file-to-upload"), $.i18n("err-alert"));
    } else
    {

        $.each($("" + appid + "")[0].files,
                function (ind2, obj2) {
                    $.each($("" + appid + "")[0].files[val1],
                            function (ind3, obj3) {
                                if (ind3 == "type") {
                                    if (obj3 == "image/png"
                                            || obj3 == "image/jpeg"
                                            || obj3 == "image/gif"
                                            || obj3 == "Video/mp4") {
                                        flag = true;
                                    } else {
                                        flag = false;
                                        jAlert($.i18n("err-file-type"));

                                    }
                                }

                            });
                    val1 = val1 + 1;
                });

        if (flag) {
            formData.append("spatialdata", file);
            formData.append("transid", _transactionid);
            formData.append("landid", Personusin);
            formData.append("docsid", id);

            $.ajax({
                url: 'upload/landmedia/',
                type: 'POST',
                data: formData,
                mimeType: "multipart/form-data",
                contentType: false,
                cache: false,
                processData: false,
                success: function (data, textStatus, jqXHR)
                {
                    if (data == "Success") {
                        landDocs(Personusin);
                        jAlert($.i18n("reg-file-deleted"), $.i18n("gen-info"));
                        $('#alias').val('');
                        $("" + appid + "").val(null);
                    } else {
                        jAlert($.i18n("err-failed-file-upload"), $.i18n("err-alert"));
                    }

                }

            });
        }

    }
}

function deleteLandMediaFile(id) {

    var formData = new FormData();

    formData.append("docid", id);
    formData.append("transid", _transactionid);
    formData.append("landid", Personusin);

    $.ajax({
        url: 'delete/landmedia/',
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data, textStatus, jqXHR)
        {
            jAlert($.i18n("reg-file-deleted"));
        }
    });
}

function mapCoordinatesUrl(usin) {
    var bbox;
    var wfsurl = "http://" + location.host + "/geoserver/wfs?";
    var wmsurl = "http://" + location.host + "/geoserver/wms?";

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
        var features = new ol.format.GeoJSON().readFeatures(json);
        var parcelExtent = features[0].getGeometry().getExtent();
        proj4.defs("EPSG:32630", "+proj=utm +zone=30 +ellps=WGS84 +datum=WGS84 +units=m +no_defs");
        proj4.defs("EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs");

        features[0].getGeometry().transform("EPSG:4326", "EPSG:32630");
        var vertexlist = features[0].getGeometry().getCoordinates()[0];

        vertexTempList = [];
        var vertices = "";
        for (var i = 1; i < vertexlist.length; i++) {
            var tmp = [];
            tmp["index"] = i;
            tmp["x"] = (vertexlist[i - 1][0]).toFixed(2);
            tmp["y"] = (vertexlist[i - 1][1]).toFixed(2);
            vertexTempList.push(tmp);
            if (vertices.length > 0) {
                vertices += ";";
            }
            vertices += i + "," + (vertexlist[i - 1][0]).toFixed(2) + "," + (vertexlist[i - 1][1]).toFixed(2);
        }
        $("#hVertices").val(vertices);

        $('#vertexTableBody').empty();
        $("#vertexRow").tmpl(vertexTempList).appendTo("#vertexTableBody");

        bbox = parcelExtent[0] + ',' + parcelExtent[1] + ',' + parcelExtent[2] + ',' + parcelExtent[3];

        var generateMapCoordinatesUrl = wmsurl + "bbox=" + bbox + "&FORMAT=image/png&REQUEST=GetMap&layers=Mast:la_spatialunit_land&width=245&height=245&srs=EPSG:4326" + "&cql_filter=landid=" + usin + "";
        $('#parcel_map').empty();
        $('#parcel_map').append('<img id="theImg" src=' + generateMapCoordinatesUrl + ' style="margin:5px;">');
    });
}

function exportData() {
    $("#dataExportForm").submit();
}

function generateForms() {
}

generateForms.prototype.Form1 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form1/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form2 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form2/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form3 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form3/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form5 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form5/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form43 = function (usin) {
    var formLst = null;
    jQuery.ajax({
        url: "landrecords/spatialunit/form43/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form7 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form7/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.Form8 = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/form8/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.paymentDetails = function (usin) {
    var formLst = [];
    jQuery.ajax({
        url: "landrecords/spatialunit/paymentdetail/" + usin,
        async: false,
        success: function (data) {
            formLst = data;
        }
    });
    return formLst;
};

generateForms.prototype.checkDate = function (usin) {
    var date = null;
    jQuery.ajax({
        url: "landrecords/checknoticedate/" + usin,
        async: false,
        success: function (data) {
            date = data;
        }
    });
    return date;
};

generateForms.prototype.checkApfrDate = function (usin) {
    var isApfrdate = null;
    jQuery.ajax({
        url: "landrecords/checkapfrdate/" + usin,
        async: false,
        success: function (data) {
            isApfrdate = data;
        }
    });
    return isApfrdate;
};

generateForms.prototype.setApfrDate = function (usin) {
    var result = null;
    jQuery.ajax({
        url: "landrecords/setapfrdate/" + usin,
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
};

generateForms.prototype.getCurrentDate = function () {
    var currentDate = null;
    jQuery.ajax({
        url: "landrecords/currentdate/",
        async: false,
        success: function (data) {
            currentDate = data;
        }
    });
    return currentDate;
};

function showFormsList(usin, workflowId, sharetype) {
    $('#radio2').show();
    $('#radio5').hide();
    $('#radio6').show();

    $('#applicationformID')[0].reset();
    formDialog = $("#applicationform-dialog-form").dialog({
        autoOpen: false,
        height: 370,
        width: 280,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-ok"),
                "id": "appform_ok",
                click: function () {
                    var option = checkForm;
                    if (option !== '0')
                    {
                        if (option === "1")
                        {
                            generateform1(usin, 1);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        } else if (option === "2")
                        {
                            generateform2(usin, 1);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        } else if (option === "3")
                        {
                            generateform3(usin);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        } else if (option === "4")
                        {
                            var fromTmp = new generateForms();
                            var form7attributeObject = fromTmp.Form7(usin);
                            if (form7attributeObject.application_date === null) {
                                jAlert($.i18n("err-print-public-notice-for-pv"), $.i18n("err-alert"));
                            } else {
                                generateform7(usin);
                                formDialog.dialog("destroy");
                                $('#applicationformID')[0].reset();
                            }
                        } else if (option === "5")
                        {
                            generateform5(usin);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        } else if (option === "6")
                        {
                            generateform8(usin);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        } else if (option === "7")
                        {
                            generatePaymentLetter(usin);
                            formDialog.dialog("destroy");
                            $('#applicationformID')[0].reset();
                        }
                    } else
                    {
                        jAlert($.i18n("err-select-option"), $.i18n("err-alert"));
                    }
                }
            },
            {
                text: $.i18n("gen-cancel"),
                "id": "appform_cancel",
                click: function () {
                    formDialog.dialog("destroy");
                    $('#applicationformID')[0].reset();
                }
            }],
        close: function () {
            formDialog.dialog("destroy");
            $('#applicationformID')[0].reset();
        }
    });


    $('input[type=radio][name=print]').change(function () {
        $('input:radio[name="print"][value=' + this.value + ']').prop('checked', true);
        checkForm = this.value;
    });

    checkForm = "0";

    $('input[type=radio][name="print"][value="1"]').css('display', 'none');
    $('input[type=radio][name="print"][value="2"]').css('display', 'none');
    $('input[type=radio][name="print"][value="3"]').css('display', 'none');
    $('input[type=radio][name="print"][value="4"]').css('display', 'none');
    $('input[type=radio][name="print"][value="5"]').css('display', 'none');
    $('input[type=radio][name="print"][value="6"]').css('display', 'none');
    $('input[type=radio][name="print"][value="7"]').css('display', 'none');
    $('input[type=radio][name="print"][value="8"]').css('display', 'none');
    $('input[type=radio][name="print"][value="9"]').css('display', 'none');
    $('#radio1').css('display', 'none');
    $('#radio2').css('display', 'none');
    $('#radio3').css('display', 'none');
    $('#radio4').css('display', 'none');
    $('#radio5').css('display', 'none');
    $('#radio6').css('display', 'none');
    $('#radio7').css('display', 'none');
    $('#radio8').css('display', 'none');
    $('#radio9').css('display', 'none');

    switch (workflowId) {
        case 1:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');

            break;
        case 2:
            $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
            if (sharetype === 8) {
                $('#radio2').css('display', 'inline');
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
            } else if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('#radio2').css('display', 'none');
            }
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'inline');

            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');

            break;

        case 3:
            $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
            if (sharetype === 8) {
                $('#radio2').css('display', 'inline');
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
            } else if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('#radio2').css('display', 'none');
            }
            $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'inline');
            //$('#radio2').css('display', 'inline');
            $('#radio3').css('display', 'inline');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');
            break;

        case 5:
            $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
            if (sharetype === 8) {
                $('#radio2').css('display', 'inline');
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
            } else if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('#radio2').css('display', 'none');
            }
            $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'inline');
            //$('#radio2').css('display', 'inline');
            $('#radio3').css('display', 'inline');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');
            break;
        case 6:
            $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
            if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('#radio2').css('display', 'none');
            }
            if (sharetype === 8) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
                $('#radio2').css('display', 'inline');
            }
            $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="4"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'inline');

            $('#radio3').css('display', 'inline');
            $('#radio4').css('display', 'inline');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');
            break;

        case 7:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            if (sharetype === 7) {
                $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
                $('#radio5').css('display', 'inline');
                $('input[type=radio][name="print"][value="6"]').css('display', 'none');
                $('#radio6').css('display', 'none');
            } else if (sharetype === 8) {
                $('input[type=radio][name="print"][value="5"]').css('display', 'none');
                $('#radio5').css('display', 'none');
                $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
                $('#radio6').css('display', 'inline');
            }

            $('input[type=radio][name="print"][value="7"]').css('display', 'inline');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');


            $('#radio7').css('display', 'inline');

            break;

        case 8:
            $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
            if (sharetype === 8) {
                $('#radio2').css('display', 'inline');
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
            } else if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('#radio2').css('display', 'none');
            }
            $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="4"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'inline');
            //$('#radio2').css('display', 'inline');
            $('#radio3').css('display', 'inline');
            $('#radio4').css('display', 'inline');
            $('#radio5').css('display', 'inline');
            $('#radio6').css('display', 'inline');
            $('#radio7').css('display', 'none');
            break;

        case 9:
            if (sharetype === 7) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'none');
                $('input[type=radio][name="print"][value="6"]').css('display', 'none');
                $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
                $('#radio5').css('display', 'inline');
                $('#radio6').css('display', 'none');
                $('#radio2').css('display', 'none');

                $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="4"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="7"]').css('display', 'inline');

                $('#radio1').css('display', 'inline');
                $('#radio3').css('display', 'inline');
                $('#radio4').css('display', 'inline');
                $('#radio7').css('display', 'inline');

            } else if (sharetype === 8) {
                $('input[type=radio][name="print"][value="2"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="5"]').css('display', 'none');
                $('#radio5').css('display', 'none');
                $('#radio6').css('display', 'inline');
                $('#radio2').css('display', 'inline');

                $('input[type=radio][name="print"][value="1"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="3"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="4"]').css('display', 'inline');
                $('input[type=radio][name="print"][value="7"]').css('display', 'inline');

                $('#radio1').css('display', 'inline');
                $('#radio3').css('display', 'inline');
                $('#radio4').css('display', 'inline');
                $('#radio7').css('display', 'inline');
            }
            break;

        case 10:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');
            break;

        case 11:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'none');
            $('input[type=radio][name="print"][value="6"]').css('display', 'none');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'none');
            $('#radio6').css('display', 'none');
            $('#radio7').css('display', 'none');
            break;

        case 12:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="7"]').css('display', 'none');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'inline');
            $('#radio6').css('display', 'inline');
            $('#radio7').css('display', 'none');

            break;
        case 13:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="7"]').css('display', 'inline');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'inline');
            $('#radio6').css('display', 'inline');
            $('#radio7').css('display', 'inline');

            break;
        case 14:
            $('input[type=radio][name="print"][value="1"]').css('display', 'none');
            $('input[type=radio][name="print"][value="2"]').css('display', 'none');
            $('input[type=radio][name="print"][value="3"]').css('display', 'none');
            $('input[type=radio][name="print"][value="4"]').css('display', 'none');
            $('input[type=radio][name="print"][value="5"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="6"]').css('display', 'inline');
            $('input[type=radio][name="print"][value="7"]').css('display', 'inline');
            $('#radio1').css('display', 'none');
            $('#radio2').css('display', 'none');
            $('#radio3').css('display', 'none');
            $('#radio4').css('display', 'none');
            $('#radio5').css('display', 'inline');
            $('#radio6').css('display', 'inline');
            $('#radio7').css('display', 'inline');
            break;
        default:
    }
    formDialog.dialog("open");
}
;

generateForms.prototype.FeatureInfo = function (usin) {
    var cql = "usin='" + usin + "'";
    var geomInfo = "http://localhost:8080/geoserver/wfs?request=GetFeature&typeName=mast:spatial_unit&CQL_FILTER=" + cql + "&version=1.0.0";

    console.log(geomInfo);

    var request = new OpenLayers.Request.GET({
        url: geomInfo,
        //data: postData,
        headers: {
            "Content-Type": "text/xml;charset=utf-8"
        },
        callback: function (response) {
            //read the response from GeoServer
            var gmlReader = new OpenLayers.Format.GML({extractAttributes: true});
            var features = gmlReader.read(response.responseText);
            console.log(features);
            // do what you want with the features returned...
        },
        failure: function (response) {
            alert("Something went wrong in the request");
        }
    });
};

function editParcelNumber(usin, section, parcelNum) {
    $('#section_no_edit').text(section);
    $('#lot_no_edit').text("000");
    $('#number_seq_edit').val(parcelNum);
    $("#usin_editId").val(usin);
    $("#commentsStatus_parcelEdit").val("");

    editParcelDialog = $("#edit-parcelno-form").dialog({
        autoOpen: false,
        height: 300,
        width: 320,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-save"),
                "id": "egp_ok",
                click: function () {
                    var parcel = $('#number_seq_edit').val();
                    if (parcel !== "" && !$.isNumeric(parcel)) {
                        jAlert($.i18n("err-enter-numeric-parcel-num"), $.i18n("err-alert"));
                        return false;
                    }

                    var comment = $('#commentsStatus_parcelEdit').val();
                    if (comment === "" || comment === " ") {
                        jAlert($.i18n("err-enter-comments"), $.i18n("err-alert"));
                        return false;
                    }

                    jQuery.ajax({
                        type: "POST",
                        url: "landrecords/updateparcelnumber",
                        data: jQuery("#parcelnoEditeformID").serialize(),
                        success: function (data) {
                            if (data) {
                                jAlert($.i18n("reg-parcel-num-updated"), $.i18n("gen-info"));
                                refreshLandRecords();
                                editParcelDialog.dialog("destroy");
                            } else {
                                jAlert($.i18n("err-parcel-number-exists"), $.i18n("err-alert"));
                            }
                        }
                    });
                }
            },
            {
                text: $.i18n("gen-cancel"),
                "id": "egp_cancel",
                click: function () {
                    editParcelDialog.dialog("destroy");
                }
            }

        ],
        close: function () {
            editParcelDialog.dialog("destroy");
        }
    });
    editParcelDialog.dialog("open");
}

function paymentDialog(usin) {
    $("#receiptId").val("");
    $("#paymentAmount").val("");
    $("#paymentDate").val("");
    $("#amount_comment").val("");
    paymentUpdateDialog = $("#paymentInfo-dialog-form").dialog({
        autoOpen: false,
        height: 500,
        width: 450,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-ok"),
                "id": "payment_ok",
                click: function () {
                    savePayment(usin);
                }
            },
            {
                text: $.i18n("gen-cancel"),
                "id": "payment_cancel",
                click: function () {
                    paymentUpdateDialog.dialog("destroy");
                }
            }],
        close: function () {
            paymentUpdateDialog.dialog("destroy");
        }
    });
    paymentUpdateDialog.dialog("open");
}

function savePayment(usin) {
    $("#paymentInfoID").validate({
        rules: {
            receiptId: "required",
            paymentAmount: "required",
            paymentDate: "required"
        },
        messages: {
            receiptId: $.i18n("err-fill-required-field"),
            paymentAmount: $.i18n("err-fill-required-field"),
            paymentDate: $.i18n("err-fill-required-field")
        }
    });

    if ($("#paymentInfoID").valid()) {
        jQuery.ajax({
            type: "POST",
            url: "landrecords/updatepayment/" + usin,
            data: jQuery("#paymentInfoID").serialize(),
            success: function (result)
            {
                if (result !== "") {
                    jAlert($.i18n("reg-payment-successful") + " " + result, $.i18n("gen-info"));
                    paymentUpdateDialog.dialog("destroy");
                } else {
                    jAlert($.i18n("err-failed-handling-request"));
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                jAlert($.i18n("err-failed-handling-request"));
            }
        });
    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function signatoryDialog(usin) {
    jQuery('#signatoryDate').val("");
    $.ajax({
        type: 'GET',
        url: "landrecords/signatory/" + usin,
        async: false,
        success: function (data) {
            if (!isEmpty(data)) {
                jQuery('#signatoryDate').val(data);
            }
        }
    });

    signatureDateUpdateDialog = $("#signatory-dialog-form").dialog({
        autoOpen: false,
        height: 370,
        width: 370,
        resizable: false,
        modal: true,
        buttons: [{
                text: $.i18n("gen-update"),
                "id": "signatory_update",
                click: function () {
                    saveSignatoryDate(usin);
                }
            },
            {
                text: $.i18n("gen-cancel"),
                "id": "signatory_cancel",
                click: function () {
                    signatureDateUpdateDialog.dialog("destroy");
                }
            }],
        close: function () {
            signatureDateUpdateDialog.dialog("destroy");
        }
    });
    signatureDateUpdateDialog.dialog("open");
}

function saveSignatoryDate(usin) {
    $("#signatoryformID").validate({
        rules: {
            signatoryDate: "required"
        },
        messages: {
            signatoryDate: $.i18n("err-fill-required-field")
        }
    });

    if ($("#signatoryformID").valid()) {
        jQuery.ajax({
            type: "POST",
            url: "landrecords/updatedate/" + usin,
            data: jQuery("#signatoryformID").serialize(),
            success: function (result)
            {
                if (result) {
                    jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
                    signatureDateUpdateDialog.dialog("destroy");
                } else {
                    jAlert($.i18n("err-failed-handling-request"));
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                jAlert($.i18n("err-failed-handling-request"));
            }
        });
    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}