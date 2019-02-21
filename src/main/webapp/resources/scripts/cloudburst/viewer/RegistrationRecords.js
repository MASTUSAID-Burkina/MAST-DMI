var selectedItem_R = null;
var dataList_R = null;
var projList_R = null;
var activeProject_R = "";
var records_from_R = 0;
var records_to_R = 0;
var records_all_R = 0;
var totalRecords_R = null;
var searchRecords_R = null;
var claimTypes_R = null;
var landRecordsInitialized_R = false;
var maritalStatus_R = null;
var genderStatus_R = null;
var surrendermortagagedata = null;
var idtype_R = null;
var country_r_id = 1;// would be null
var region_r_id = 2; // would be null
var allcountry = null;
var landtype_r = null;
var region_r = null;
var landsharetype_r = null;
var province_r = null;
var processdetails_R = null;
var selectedlandid = null;
var suReg = null;
var laMortgage_R = null;
var financialagency_R = null;
var monthoflease_R = null;
// var statusList_R = null;
var attributeEditDialog = null;
var documenttype_R = null;
var villagesList = null;
var currentdiv = null;
var isVisible = true;
var TransLandId = null;
var processid = 0;
var persontypeid = 0;
var transid = 0;
var relationShips = null;
var finallandid = null;
var firstselectedprocess = null;
var arry_Sellerbyprocessid = [];
var arry_Buyerbyprocessid = [];
var editlease = false;
var leaseData = null;
var process_id = 0;
var alertmessage = "";
var edit = 0;
var finaltransid = 0;
var finalbuyerarray = [];
var tenuretypeListR = null;
var mutationTypes = null;
var permissionApplicant = null;
var leTypes = null;

$("#transactionId").val(0);

function RegistrationRecords(_selectedItem) {
    records_from_R = 0;
    searchRecords_R = null;
    selectedItem_R = _selectedItem;
    activeProject_R = activeProject;
    URL = "registryrecords/spatialunit/default" + 0 + "/" + Global.LANG;
    if (activeProject_R !== null && activeProject_R !== "") {
        URL = "registryrecords/spatialunit/" + activeProject_R + "/" + 0 + "/" + Global.LANG;
    }

    jQuery.ajax({
        url: URL,
        async: false,
        success: function (data) {
            dataList_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/maritalstatus/",
        async: false,
        success: function (data) {
            maritalStatus_R = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/grouptype/",
        async: false,
        success: function (data) {
            leTypes = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/claimtypes/",
        async: false,
        success: function (data) {
            claimTypes_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/genderstatus/",
        async: false,
        success: function (data) {
            genderStatus_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/idtype/",
        async: false,
        success: function (data) {
            idtype_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/landsharetypes/",
        async: false,
        success: function (data) {
            landsharetype_r = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/tenuretype/",
        async: false,
        success: function (data) {
            tenuretypeListR = data;
        }
    });

    if (villagesList === null) {
        $.ajax({
            url: "landrecords/projectvillages/" + activeProject_R,
            async: false,
            success: function (data) {
                villagesList = data;
            }
        });
    }

    jQuery.ajax({
        url: "registration/processdetails/",
        async: false,
        success: function (data) {
            processdetails_R = data;
        }
    });

    if (mutationTypes === null) {
        jQuery.ajax({
            url: "landrecords/mutationtypes/",
            async: false,
            success: function (data) {
                mutationTypes = data;
            }
        });
    }

    displayRefreshedRegistryRecords_ABC();
}

function displayRefreshedRegistryRecords_ABC() {
    $("#registryTab-div").empty();
    jQuery.get("resources/templates/viewer/" + selectedItem_R + ".html",
            function (template) {
                $("#registryTab-div").append(template);
                $("#registryTab-div").i18n();
                $('#registryRecordsFormdiv').css("visibility", "visible");
                $("#registryRecordsTable").show();

                jQuery.ajax({
                    url: "registryrecords/spatialunitcount/" + activeProject_R,
                    async: false,
                    success: function (data) {
                        totalRecords_R = data;
                    }
                });

                jQuery.ajax({
                    url: URL,
                    async: false,
                    success: function (data) {
                        dataList_R = data;
                        $("#registryRecordsRowData1").empty();
                        $("#cbx_app_type").empty();
                        $("#cbxRegVillage").empty();
                        $("#sale_transfer_type").empty();
                        $("#sale_ownership_type").empty();

                        $("#sale_ownership_type").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
                        $("#cbx_app_type").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
                        $("#sale_transfer_type").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
                        $("#cbxRegVillage").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

                        jQuery.each(villagesList, function (i, village) {
                            var displayName = village.name;
                            if (Global.LANG === "en") {
                                displayName = village.nameEn;
                            }
                            $("#cbxRegVillage").append($("<option></option>").attr("value", village.hierarchyid).text(displayName));
                        });

                        jQuery.each(tenuretypeListR, function (i, tenureType) {
                            var displayName = tenureType.landsharetype;
                            if (Global.LANG === "en") {
                                displayName = tenureType.landsharetypeEn;
                            }
                            $("#cbx_app_type").append($("<option></option>").attr("value", tenureType.landsharetypeid).text(displayName));
                        });

                        jQuery.each(tenuretypeListR, function (i, tenureType) {
                            var displayName = tenureType.landsharetype;
                            if (Global.LANG === "en") {
                                displayName = tenureType.landsharetypeEn;
                            }
                            $("#sale_ownership_type").append($("<option></option>").attr("value", tenureType.landsharetypeid).text(displayName));
                        });

                        jQuery.each(mutationTypes, function (i, mutationType) {
                            var displayName = mutationType.name;
                            if (Global.LANG === "en") {
                                displayName = mutationType.nameEn;
                            }
                            $("#sale_transfer_type").append($("<option></option>").attr("value", mutationType.id).text(displayName));
                        });

                        if (dataList_R.length != 0 && dataList_R.length != undefined) {
                            $("#registryRecordsAttrTemplate1").tmpl(dataList_R).appendTo("#registryRecordsRowData1");
                            $("#registryRecordsRowData1").i18n();
                            records_from_R = 0;
                            $('#records_from_R').val(records_from_R + 1);
                            $('#records_to_R').val(totalRecords_R);
                            if (records_from_R + 10 <= totalRecords_R)
                                $('#records_to_R').val(records_from_R + 10);
                            $('#records_all_R').val(totalRecords_R);
                        } else {
                            $('#records_from_R').val(0);
                            $('#records_to_R').val(0);
                            $('#records_all_R').val(0);
                        }

                        $("#claim_type_R").empty();
                        $("#claim_type_R").append(
                                $("<option></option>").attr("value", "").text(
                                $.i18n("gen-please-select")));
                        jQuery.each(claimTypes_R, function (i, claimType_R) {
                            $("#claim_type_R").append(
                                    $("<option></option>").attr("value",
                                    claimType_R.code)
                                    .text(claimType_R.name));
                        });

                    }
                });
            });
}

function displayRefreshedRegistryRecords() {
    var URL = "landrecords/";
    if (activeProject_R != "") {
        URL = "landrecords/" + activeProject_R;
    }

    if (!landRecordsInitialized_R) {
        landRecordsInitialized_R = true;
        $("#registryTab-div").empty();
        jQuery.get("resources/templates/viewer/" + selectedItem_R + ".html",
                function (template) {
                    $("#registryTab-div").append(template);
                    $('#registryRecordsFormdiv').css("visibility",
                            "visible");
                    $("#registryRecordsTable").show();
                });
    }

    jQuery.ajax({
        url: URL,
        success: function (data) {
            projList_R = data;

            $("#registryRecordsRowData").empty();

            if (dataList_R.length != 0 && dataList_R.length != undefined) {
                $("#registryRecordsAttrTemplate").tmpl(dataList_R[0])
                        .appendTo("#registryRecordsRowData");
            }

            $("#status_id").empty();

            $("#status_id").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));

            jQuery.each(statusList, function (i, statusobj) {
                $("#status_id").append(
                        $("<option></option>").attr("value",
                        statusobj.workflowStatusId).text(
                        statusobj.workflowStatus));
            });

            $("#records_from_R").val(records_from_R + 1);
            $("#records_to_R").val(records_from_R + 20);

            if (totalRecords_R <= records_from_R + 20)
                $("#records_to_R").val(totalRecords_R);

            $('#records_all_R').val(totalRecords_R);
            $("#projectName_R").text(data.name);
            $("#country_R").text(data.countryName);
            $("#region_R").text(data.region);
            $("#district_R").text(data.districtName);
            $("#village_R").text(data.village);
            $("#hamlet").text("--");

            if (data.hamlet != "" && data.hamlet != null) {
                $("#hamlet").text(data.hamlet);
            }
            if (dataList_R.length != 0 && dataList_R.length != undefined) {
                $("#registryRecordsTable").trigger("update");
                $("#registryRecordsTable").tablesorter({
                    sortList: [[0, 1], [1, 0]]
                });

            }
        }
    });
}

function previousRecords_R() {
    records_from_R = $('#records_from_R').val();
    records_from_R = parseInt(records_from_R);
    records_from_R = records_from_R - 11;
    if (records_from_R >= 0) {
        if (searchRecords_R != null) {
            RegistrationSearch_R(records_from_R);
        } else {
            RegistrationRecords_R(records_from_R);
        }
    } else {
        alert($.i18n("err-no-records"));
    }
}

function nextRecords_R() {
    records_from_R = $('#records_from_R').val();
    records_from_R = parseInt(records_from_R);
    records_from_R = records_from_R + 9;

    if (records_from_R <= totalRecords_R - 1) {
        if (searchRecords_R != null) {
            if (records_from_R <= searchRecords_R - 1) {
                RegistrationSearch_R(records_from_R);
            } else {
                alert($.i18n("err-no-records"));
            }
        }
        RegistrationRecords_R(records_from_R);
    } else {
        alert($.i18n("err-no-records"));
    }
}

function firstRecordsR() {
    records_from_R = $('#records_from_R').val();
    records_from_R = parseInt(records_from_R);
    if (records_from_R > 1) {
        if (searchRecords_R != null) {
            RegistrationSearch_R(0);
        } else {
            RegistrationRecords_R(0);
        }
    }
}

function lastRecordsR() {
    recordsAll_R = $('#records_all_R').val();
    recordsAll_R = parseInt(recordsAll_R);

    if ((recordsAll_R % 10) > 0) {
        records_from_R = recordsAll_R - (recordsAll_R % 10);
    } else {
        records_from_R = recordsAll_R - 10;
    }

    if (recordsAll_R > 10) {
        if (searchRecords_R != null) {
            if (records_from_R <= searchRecords_R - 1) {
                RegistrationSearch_R(records_from_R);
            }
        } else {
            RegistrationRecords_R(records_from_R);
        }
    }
}

function RegistrationRecords_R(records_from_R) {

    jQuery.ajax({
        url: "registryrecords/spatialunit/" + activeProject_R + "/" + records_from_R + "/" + Global.LANG,
        async: false,
        success: function (data) {
            $("#registryRecordsRowData1").empty();

            if (data.length != 0 && data.length != undefined) {
                $("#registryRecordsRowData1").empty();
                $("#registryRecordsAttrTemplate1").tmpl(data).appendTo(
                        "#registryRecordsRowData1");
                $("#registryRecordsRowData1").i18n();
                $('#records_from_R').val(records_from_R + 1);
                $('#records_to_R').val(totalRecords_R);
                if (records_from_R + 10 <= totalRecords_R)
                    $('#records_to_R').val(records_from_R + 10);
                $('#records_all_R').val(totalRecords_R);
            } else {
                $('#records_from_R').val(0);
                $('#records_to_R').val(0);
                $('#records_all_R').val(0);
            }

        }
    });

}

function clearSearch_R() {
    $("#cbx_app_type").val("");
    $("#txt_parcel_id").val("");
    $("#txt_app_num").val("");
    $("#txt_pv_num").val("");
    $("#txt_apfr_num").val("");
    $("#txt_first_name").val("");
    $("#cbxRegVillage").val(0);
    searchRecords_R = null;
    displayRefreshedRegistryRecords_ABC();
}

function search_R() {
    searchRecords_R = null;
    records_from_R = 0;

    $("#registryRecordsRowData1").empty();

    jQuery.ajax({
        type: "POST",
        async: false,
        url: "registration/search1Count/" + activeProject_R,
        data: $("#registryrecordsform").serialize(),
        success: function (result) {
            searchRecords_R = result;
            totalRecords_R = result;
        }
    });
    RegistrationSearch_R(records_from_R);
}

function RegistrationSearch_R(records_from_R) {
    $("#lang_search_registered").val(Global.LANG);
    jQuery.ajax({
        type: "POST",
        async: false,
        url: "registration/search1/" + activeProject_R + "/" + records_from_R,
        data: $("#registryrecordsform").serialize(),
        success: function (result) {
            if (result.length != undefined && result.length != 0) {
                $("#registryRecordsAttrTemplate1").tmpl(result).appendTo("#registryRecordsRowData1");
                $("#registryRecordsRowData1").i18n();

                $('#records_from_R').val(records_from_R + 1);
                $('#records_to_R').val(totalRecords_R);
                if (records_from_R + 10 <= totalRecords_R)
                    $('#records_to_R').val(records_from_R + 10);
                $('#records_all_R').val(totalRecords_R);
            } else {
                $('#records_from_R').val(0);
                $('#records_to_R').val(0);
                $('#records_all_R').val(0);
            }

        }
    });

}

$(document).ready(
        function () {
            // Add date field
            var DateField = function (config) {
                jsGrid.Field.call(this, config);
            };

            jQuery.ajax({
                url: "registration/landtype/",
                async: false,
                success: function (data) {
                    landtype_r = data;
                    if (data.length > 0) {

                    }
                }
            });

            jQuery.ajax({
                url: "registration/allcountry/",
                async: false,
                success: function (data) {
                    allcountry = data;
                    if (data.length > 0) {
                        // data.xyz.name_en for getting the data
                    }
                }
            });

            DateField.prototype = new jsGrid.Field({
                sorter: function (date1, date2) {
                    if ((date1 === null || date1 === "")
                            && (date2 === null || date2 === "")) {
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
                        return formatDate_R(value);
                    }
                    return "";
                },
                editTemplate: function (value) {
                    if (isEmpty(value))
                        return this._editPicker = $("<input>").datepicker({
                            dateFormat: "yy-mm-dd"
                        });
                    else
                        return this._editPicker = $("<input>").datepicker({
                            dateFormat: "yy-mm-dd"
                        }).datepicker("setDate", new Date(value));
                },
                insertValue: function () {
//					return this._insertPicker.datepicker("getDate");
                    alert("zdfdfssdbfgc");
                },
                editValue: function () {
                    return this._editPicker.datepicker("getDate");
                }
            });

            jsGrid.fields.date = DateField;
            hamletList = [];
        });

function isEmpty(obj) {
    return obj === null || typeof obj === 'undefined' || obj === '';
}

function leaseAttribute(landid) {
    edit = 0;

    $("#editflag").val(edit);
    $(".signin_menu").hide();
    var lease = document.getElementById("Tab_1");
    var sale = document.getElementById("Tab_2");
    var mortgage = document.getElementById("Tab_3");
    var split = document.getElementById("Tab_4");
    var tabPermission = document.getElementById("tabPermission");
    lease.style.display = "none";
    sale.style.display = "none";
    mortgage.style.display = "none";
    split.style.display = "none";
    tabPermission.style.display = "none";

    loadResPersonsOfEditingForEditing();
    clearBuyerDetails_sale();

    $("#registration_process").show();
    $("#buyersavebutton").prop("disabled", false).hide();
    selectedlandid = landid;
    $("#landidhide").val(landid);

    jQuery.ajax({
        url: "registration/doctype/",
        async: false,
        success: function (data) {
            documenttype_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/financialagency/",
        async: false,
        success: function (data) {
            financialagency_R = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/editattribute/" + landid,
        async: false,
        success: function (data) {
            suReg = data[0];
        }
    });

    jQuery.ajax({
        url: "registration/laMortgage/" + landid,
        async: false,
        success: function (data) {
            laMortgage_R = data;
            if (data.length > 0) {

            }
        }
    });

    jQuery.ajax({
        url: "registration/relationshiptypes/",
        async: false,
        success: function (data) {
            relationtypes_R = data;

        }
    });
    jQuery.ajax({
        type: "GET",
        url: "landrecords/landPOI/" + transid + "/" + landid,
        async: false,
        success: function (data) {

            $("#POIRecordsRowDataSale1").empty();
            $("#POIRecordsRowDataMortgage1").empty();
            $("#POIownerRecordsRowDataLease1").empty();
            $("#rowsPermissionOwnerPoi").empty();
            if (data.length > 0) {

                for (var i = 0; i < data.length; i++) {

                    var relationship = "";
                    var gender = "";
                    for (var j = 0; j < relationtypes_R.length; j++) {

                        if (data[i].relation == relationtypes_R[j].relationshiptypeid) {

                            relationship = relationtypes_R[j].relationshiptype;
                        }
                    }

                    for (var k = 0; k < genderStatus_R.length; k++) {

                        if (genderStatus_R[k].genderId == data[i].gender) {

                            gender = genderStatus_R[k].gender;
                        }
                    }

                    data[i].relation = relationship;
                    data[i].gender = gender;
                    $("#POIRecordsAttrTemplateSale1").tmpl(data[i]).appendTo("#POIRecordsRowDataSale1");
                    $("#POIRecordsAttrTemplateMortgage1").tmpl(data[i]).appendTo("#POIRecordsRowDataMortgage1");
                    $("#POIownerRecordsAttrTemplateLease1").tmpl(data[i]).appendTo("#POIownerRecordsRowDataLease1");
                    $("#rowPermissionOwnerPoi").tmpl(data[i]).appendTo("#rowsPermissionOwnerPoi");
                }
            }

        }
    });

//    jQuery.ajax({
//        type: "GET",
//        url: "registration/landLeaseePOI/" + landid,
//        async: false,
//        success: function (data) {
//            $("#POIRecordsRowDataLease1").empty();
//            if (data.length > 0) {
//                for (var i = 0; i < data.length; i++) {
//                    $("#POIRecordsAttrTemplateLease1").tmpl(data[i]).appendTo("#POIRecordsRowDataLease1");
//                }
//            }
//        }
//    });

    $.ajax({
        url: "resource/relationshipTypes/",
        async: false,
        success: function (data1) {

            relationShips = data1;
        }
    });

    jQuery.ajax({
        url: "registration/allregion/" + country_r_id,
        async: false,
        success: function (data) {
            region_r = data;
            if (data.length > 0) {
                // data.xyz.name_en for getting the data
            }
        }
    });

    jQuery.ajax({
        url: "registration/allprovince/" + region_r_id,
        async: false,
        success: function (data) {
            province_r = data;
        }
    });

    $("#registration_process").empty();
    $("#registration_process").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

    jQuery.each(processdetails_R, function (i, process) {
        var displayName = process.processname;
        if (Global.LANG === "en") {
            displayName = process.processname_en;
        }
        $("#registration_process").append($("<option></option>").attr("value", process.processid).text(displayName));
    });

    $(function () {
        $("#Tab_1").hide();
        $("#Tab_2").hide();
        $("#Tab_3").hide();
        attributeEditDialog = $("#lease-dialog-form").dialog({
            autoOpen: false,
            height: 700,
            width: 1000,
            resizable: true,
            modal: true,
            close: function () {
                attributeEditDialog.dialog("destroy");
                $("input,select,textarea").removeClass('addBg');
            },
            buttons: [{
                    text: $.i18n("gen-save-next"),
                    "id": "comment_Next",
                    click: function ()
                    {
                        $("input,select,textarea").removeClass('addBg');
                        if (currentdiv == "Sale")
                        {
                            if ($('#Seller_Details').css('display') == 'block')
                            {
                                $("#Seller_Details").hide();
                                $("#Land_Details_Sale").show();
                                $("#Buyer_Details").hide();
                                $("#regPoi").hide();
                                $("#Upload_Document_Sale").hide();
                                $("#selectedseller").removeClass("ui-tabs-active");
                                $("#selectedseller").removeClass("ui-state-active");
                                $("#selectedland").addClass("ui-tabs-active");
                                $("#selectedland").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Land_Details_Sale').css('display') == 'block')
                            {
                                $("#Buyer_Details").show();
                                $("#Seller_Details").hide();
                                $("#Land_Details_Sale").hide();
                                $("#Upload_Document_Sale").hide();

                                $("#selectedland").removeClass("ui-tabs-active");
                                $("#selectedland").removeClass("ui-state-active");
                                $("#selectedbuyer").addClass("ui-tabs-active");
                                $("#selectedbuyer").addClass("ui-state-active");



                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Buyer_Details').css('display') == 'block')
                            {
                                if (null != arry_Buyerbyprocessid && null != finalbuyerarray) {
                                    $("#Seller_Details").hide();
                                    $("#Buyer_Details").hide();
                                    $("#Land_Details_Sale").hide();
                                    $("#regPoi").show();
                                    $("#Upload_Document_Sale").hide();
                                    $("#selectedbuyer").removeClass("ui-tabs-active");
                                    $("#selectedbuyer").removeClass("ui-state-active");
                                    $("#selectedpoi").addClass("ui-tabs-active");
                                    $("#selectedpoi").addClass("ui-state-active");
                                    $("#comment_Save").hide();
                                    $("#comment_Next").show();
                                } else {

                                    jAlert($.i18n("err-save-buyer"), $.i18n("gen-info"));
                                }
                            } else if ($('#regPoi').css('display') == 'block')
                            {

                                $("#Seller_Details").hide();
                                $("#Buyer_Details").hide();
                                $("#Land_Details_Sale").hide();
                                $("#regPoi").hide();
                                $("#Upload_Document_Sale").show();
                                $("#selectedpoi").removeClass("ui-tabs-active");
                                $("#selectedpoi").removeClass("ui-state-active");
                                $("#selecteddoc").addClass("ui-tabs-active");
                                $("#selecteddoc").addClass("ui-state-active");
                                $("#comment_Save").show();
                                $("#comment_Next").hide();
                            }
                        } else if (currentdiv == "Lease") {
                            if ($('#Owner_Details').css('display') == 'block')
                            {
                                LeaseFormTabClick("selectedLanddetails", "Land_Details_lease");
                            } else if ($('#Land_Details_lease').css('display') == 'block')
                            {
                                LeaseFormTabClick("selectedApplicant", "Applicant_Details");
                            } else if ($('#Applicant_Details').css('display') == 'block')
                            {
                                if (processid == "5") {
                                    saveattributesSurrenderLease();
                                }
                                LeaseFormTabClick("selecteddocs", "Upload_Documents_Lease");
                            } else if ($('#regLeasePoi').css('display') == 'block')
                            {
                                LeaseFormTabClick("selectedsleasedetails", "Lease_Details");
                            } else if ($('#Upload_Documents_Lease').css('display') == 'block')
                            {
                                LeaseFormTabClick("selectedsleasedetails", "Lease_Details");
                            }
                        } else if (currentdiv == "Permission") {
                            if ($('#tabPermCurrentOwner').css('display') == 'block')
                            {
                                permissionTabClick("liPermLandInfo", "tabPermLandInfo");
                            } else if ($('#tabPermLandInfo').css('display') == 'block')
                            {
                                permissionTabClick("liPermApplicant", "tabPermApplicant");
                            } else if ($('#tabPermApplicant').css('display') == 'block')
                            {
                                if (processid == "12") {
                                    savePermissionSurrender();
                                }
                                permissionTabClick("liPermDocs", "tabPermDocs");
                            } else if ($('#tabPermDocs').css('display') == 'block')
                            {
                                permissionTabClick("liPermDetails", "tabPermDetails");
                            }
                        } else if (currentdiv == "Mortgage") {

                            if ($('#MortgageOwner_Details').css('display') == 'block')
                            {
                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").show();
                                $("#Mortgage_Details").hide();
                                $("#Upload_Document_Mortgage").hide();
                                $("#selectedownerdetails").removeClass("ui-tabs-active");
                                $("#selectedownerdetails").removeClass("ui-state-active");
                                $("#selectelandmortgage").addClass("ui-tabs-active");
                                $("#selectelandmortgage").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Land_Details_Mortgage').css('display') == 'block')
                            {
                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").hide();
                                $("#Mortgage_Details").show();
                                $("#Upload_Document_Mortgage").hide();
                                $("#selectelandmortgage").removeClass("ui-tabs-active");
                                $("#selectelandmortgage").removeClass("ui-state-active");
                                $("#selectemortgage").addClass("ui-tabs-active");
                                $("#selectemortgage").addClass("ui-state-active");



                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Mortgage_Details').css('display') == 'block')
                            {

                                if (processid == "9")
                                {
                                    saveattributesSurrenderMortagage();
                                } else
                                {
                                    saveMortgage();
                                }

                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").hide();
                                $("#Mortgage_Details").hide();
                                $("#Upload_Document_Mortgage").show();
                                $("#selectemortgage").removeClass("ui-tabs-active");
                                $("#selectemortgage").removeClass("ui-state-active");
                                $("#selectemortgagedocs").addClass("ui-tabs-active");
                                $("#selectemortgagedocs").addClass("ui-state-active");
                                $("#comment_Save").show();
                                $("#comment_Next").hide();
                            }

                        } else if (currentdiv == "split") {
                            $("#comment_Save").hide();
                            $("#comment_Next").show();
                            attributeEditDialog.dialog("close");
                            showOnMap(selectedlandid, 0, "split");
                        }
                    }

                },
                {
                    text: $.i18n("reg-reg"),
                    "id": "comment_Save",
                    click: function () {
                        if (currentdiv == "Sale") {
                            saveattributessale();
                        } else if (currentdiv == "Lease") {
                            saveLease();
                        } else if (currentdiv == "Permission") {
                            if (processid == 11) {
                                registerNewPermission();
                            } else if (processid == 12) {
                                registerPermissionSurrender();
                            }
                        } else {
                            saveattributesMortagage();
                        }
                    }
                },
                {
                    text: $.i18n("gen-cancel"),
                    "id": "comment_cancel",
                    click: function () {
                        $("input,select,textarea").removeClass('addBg');
                        setInterval(function () {
                        }, 4000);
                        attributeEditDialog.dialog("close");
                    }
                }
            ],
            Cancel: function () {
                attributeEditDialog.dialog("close");
                $("input,select,textarea").removeClass('addBg');
            }
        });
        $("#comment_cancel").html('<span class="ui-button-text">' + $.i18n("gen-cancel") + '</span>');
        attributeEditDialog.dialog("open");
        $("#comment_Save").hide();
        $("#comment_Next").hide();
    });
}

function FillDataforRegistration(selectedlandid) {
//	 isVisible = $('#buyersavebutton').is(':visible');

    if (laMortgage_R.length == 0) {
        $("#mortgage_to").val("");
        $("#mortgage_from").val("");

    }

    var lease = document.getElementById("Tab_1");
    var sale = document.getElementById("Tab_2");
    var mortgage = document.getElementById("Tab_3");
    lease.style.display = "none"; // lease.style.display = "block";
    sale.style.display = "none";
    mortgage.style.display = "none";

    firstselectedprocess = $("#registration_process").val();
    clearBuyerDetails_sale();


    $("#buyersavebutton").prop("disabled", false).hide();
    landid = selectedlandid;
    $("#landidhide").val(landid);

    jQuery.ajax({
        url: "registration/doctype/",
        async: false,
        success: function (data) {
            documenttype_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/monthoflease/",
        async: false,
        success: function (data) {
            monthoflease_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/financialagency/",
        async: false,
        success: function (data) {
            financialagency_R = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/editattribute/" + landid,
        async: false,
        success: function (data) {
            suReg = data[0];
        }
    });

//    jQuery.ajax({
//        url: "registration/laspatialunitland/" + landid,
//        async: false,
//        success: function (data) {
//            suReg = data;
//        }
//    });

    jQuery.ajax({
        url: "registration/relationshiptypes/",
        async: false,
        success: function (data) {
            relationtypes_R = data;

        }
    });

    jQuery.ajax({
        url: "registration/allregion/" + country_r_id,
        async: false,
        success: function (data) {
            region_r = data;
            if (data.length > 0) {
                // data.xyz.name_en for getting the data
            }
        }
    });

    jQuery.ajax({
        url: "registration/allprovince/" + region_r_id,
        async: false,
        success: function (data) {
            province_r = data;
        }
    });
    fillBuyerandSellerpage(landid);
}

function Hidedocumentgrid()
{
    $("#Upload_Document_Sale").hide();
}

function ShowSavebutton()
{
    $("#comment_Save").show();
    $("#comment_Next").hide();
}

function SellerTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#Seller_Details").show();
    $("#Upload_Document_Sale").hide();
    $("#Land_Details_Sale").hide();
    $("#Buyer_Details").hide();
    $("#regPoi").hide();
    $("#selectedseller").addClass("ui-tabs-active");
    $("#selectedseller").addClass("ui-state-active");
    $("#selectedland").removeClass("ui-tabs-active");
    $("#selectedland").removeClass("ui-state-active");
    $("#selecteddoc").removeClass("ui-tabs-active");
    $("#selecteddoc").removeClass("ui-state-active");
    $("#selectedbuyer").removeClass("ui-tabs-active");
    $("#selectedbuyer").removeClass("ui-state-active");
    $("#selectedpoi").removeClass("ui-tabs-active");
    $("#selectedpoi").removeClass("ui-state-active");
}

function LandTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();
    $("#Seller_Details").hide();
    $("#Upload_Document_Sale").hide();
    $("#Land_Details_Sale").show();
    $("#Buyer_Details").hide();
    $("#regPoi").hide();
    $("#selectedseller").removeClass("ui-tabs-active");
    $("#selectedseller").removeClass("ui-state-active");
    $("#selectedland").addClass("ui-tabs-active");
    $("#selectedland").addClass("ui-state-active");
    $("#selecteddoc").removeClass("ui-tabs-active");
    $("#selecteddoc").removeClass("ui-state-active");
    $("#selectedbuyer").removeClass("ui-tabs-active");
    $("#selectedbuyer").removeClass("ui-state-active");
    $("#selectedpoi").removeClass("ui-tabs-active");
    $("#selectedpoi").removeClass("ui-state-active");

}

function BuyerTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#Seller_Details").hide();
    $("#Upload_Document_Sale").hide();
    $("#Land_Details_Sale").hide();
    $("#Buyer_Details").show();
    $("#regPoi").hide();
    $("#selectedseller").removeClass("ui-tabs-active");
    $("#selectedseller").removeClass("ui-state-active");
    $("#selectedland").removeClass("ui-tabs-active");
    $("#selectedland").removeClass("ui-state-active");
    $("#selecteddoc").removeClass("ui-tabs-active");
    $("#selecteddoc").removeClass("ui-state-active");
    $("#selectedbuyer").addClass("ui-tabs-active");
    $("#selectedbuyer").addClass("ui-state-active");
    $("#selectedpoi").removeClass("ui-tabs-active");
    $("#selectedpoi").removeClass("ui-state-active");
}

function poiTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#Seller_Details").hide();
    $("#Upload_Document_Sale").hide();
    $("#Land_Details_Sale").hide();
    $("#Buyer_Details").hide();
    $("#regPoi").show();
    $("#selectedseller").removeClass("ui-tabs-active");
    $("#selectedseller").removeClass("ui-state-active");
    $("#selectedland").removeClass("ui-tabs-active");
    $("#selectedland").removeClass("ui-state-active");
    $("#selecteddoc").removeClass("ui-tabs-active");
    $("#selecteddoc").removeClass("ui-state-active");
    $("#selectedbuyer").removeClass("ui-tabs-active");
    $("#selectedbuyer").removeClass("ui-state-active");
    $("#selectedpoi").addClass("ui-tabs-active");
    $("#selectedpoi").addClass("ui-state-active");

}

function SaleDocTabClick()
{
    $("#comment_Save").show();
    $("#comment_Next").hide();

    $("#Seller_Details").hide();
    $("#Upload_Document_Sale").show();
    $("#Land_Details_Sale").hide();
    $("#Buyer_Details").hide();
    $("#regPoi").hide();
    $("#selectedseller").removeClass("ui-tabs-active");
    $("#selectedseller").removeClass("ui-state-active");
    $("#selectedland").removeClass("ui-tabs-active");
    $("#selectedland").removeClass("ui-state-active");
    $("#selecteddoc").addClass("ui-tabs-active");
    $("#selecteddoc").addClass("ui-state-active");
    $("#selectedbuyer").removeClass("ui-tabs-active");
    $("#selectedbuyer").removeClass("ui-state-active");
    $("#selectedpoi").removeClass("ui-tabs-active");
    $("#selectedpoi").removeClass("ui-state-active");
}

function fillMortgageDetails() {
    if (laMortgage_R.mortgagefrom != null || laMortgage_R.mortgagefrom != "") {
        $("#mortgage_from").val(formatDate_R(laMortgage_R.mortgagefrom));
    } else {
        $("#mortgage_from").val("");
    }
    if (laMortgage_R.mortgageto != null || laMortgage_R.mortgageto != "") {
        $("#mortgage_to").val(formatDate_R(laMortgage_R.mortgageto));
    } else {
        $("#mortgage_to").val("");
    }
    $("#amount_mortgage").val(laMortgage_R.mortgageamount);


    $("#Martial_sts_mortgage").empty();
    $("#gender_mortgage").empty();
    $("#mortgage_Financial_Agencies").empty();
    $("#doc_Type_Mortgage").empty();

    $("#Martial_sts_mortgage").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#gender_mortgage").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#mortgage_Financial_Agencies").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#doc_Type_Mortgage").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

    jQuery.each(documenttype_R, function (i, obj) {
        $("#doc_Type_Mortgage").append(
                $("<option></option>").attr("value", obj.code).text(
                obj.nameOtherLang));
    });

    jQuery.each(financialagency_R, function (i, obj1) {
        $("#mortgage_Financial_Agencies").append(
                $("<option></option>").attr("value",
                obj1.financialagencyid).text(obj1.financialagency_en));
    });

    if (laMortgage_R != "" && laMortgage_R != null)
    {
        if (laMortgage_R.laExtFinancialagency != null && laMortgage_R.laExtFinancialagency != "")
        {
            $("#mortgage_Financial_Agencies").val(laMortgage_R.laExtFinancialagency.financialagencyid);
        }
    }

    jQuery.each(maritalStatus_R, function (i, obj) {
        $("#Martial_sts_mortgage").append(
                $("<option></option>").attr("value", obj.maritalstatusid)
                .text(obj.maritalstatusEn));
    });

    if (!$("#mortgageLandDetailsContainer #pnlLandDetails").length) {
        $("#mortgageLandDetailsContainer").empty();
        $("#pnlLandDetails").appendTo($("#mortgageLandDetailsContainer"));
    }
    fillLandDetails();
}

function savebuyerdetails() {
    isVisible = true;
    var selectedprocess = $("#registration_process").val();
    var isPerson = $("#tblPersonBuyerInfo").is(":visible");

    if ($("#editprocessAttributeformID").valid()) {
        var errors = "";

        if (isPerson) {
            if (firstname_r_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-select-firstname") + "\n";
            }
            if (lastname_r_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-select-lastname") + "\n";
            }
            if (date_Of_birth_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-enter-dob") + "\n";
            }
            if (id_r1.value.length === 0) {
                errors += "-" + $.i18n("err-enter-idnumber") + "\n";
            }
            if (id_date_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-enter-id-date") + "\n";
            }
            if (father_name_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-enter-father-name") + "\n";
            }
            if (mother_name_sale1.value.length === 0) {
                errors += "-" + $.i18n("err-enter-mother-name") + "\n";
            }
        } else {
            if (txtLegalBuyerName.value.length === 0) {
                errors += "-" + $.i18n("err-enter-org-name") + "\n";
            }
            if (cbxLegalBuyerType.value.length === 0 || cbxLegalBuyerType.value == "0") {
                errors += "-" + $.i18n("err-select-type") + "\n";
            }
            if (txtLegalBuyerRegNum.value.length === 0) {
                errors += "-" + $.i18n("err-enter-regnum") + "\n";
            }
            if (txtLegalBuyerRepName.value.length === 0) {
                errors += "-" + $.i18n("err-enter-representative") + "\n";
            }

        }

        if (errors !== "") {
            jAlert(errors, $.i18n("err-alert"));
            return false;
        }

        if (isPerson) {
            jQuery.ajax({
                type: "POST",
                url: "registration/savebuyerdata",
                data: $("#editprocessAttributeformID").serialize(),
                success: function (result) {
                    if (result !== null && result !== undefined) {
                        $("#buyersavebutton").prop("disabled", false).hide();
                        $("#personid").val(0);
                        $("#firstname_r_sale1").val("");
                        $("#middlename_r_sale1").val("");
                        $("#lastname_r_sale1").val("");
                        $("#id_r1").val("");
                        $("#id_date_sale1").val("");
                        $("#address_sale1").val("");
                        $("#date_Of_birth_sale1").val("");
                        $("#sale_gender_buyer").val("0");
                        $("#place_of_birth_sale1").val("");
                        $("#sale_marital_buyer").val("0");
                        $("#cbxLegalBuyerType").val("0");
                        $("#profession_sale1").val("");
                        $("#father_name_sale1").val("");
                        $("#mother_name_sale1").val("");
                        $("#nop_sale1").val("0");
                        $("#mandate_date_sale1").val("");
                        $("#mandate_loc_sale1").val("");

                        SaleOwnerdBuyeretails(finallandid);
                        salebuyerdetails(selectedprocess);
                        $("#Seller_Details").hide();
                        $("#Buyer_Details").show();
                        $("#Land_Details_Sale").hide();
                        $("#regPoi").hide();
                        $("#Upload_Document_Sale").hide();
                        $("#selectedbuyer").addClass("ui-tabs-active");
                        $("#selectedbuyer").addClass("ui-state-active");
                        $("#selectedpoi").removeClass("ui-tabs-active");
                        $("#selectedpoi").removeClass("ui-state-active");
                        $("#comment_Save").hide();
                        $("#comment_Next").show();

                        jAlert($.i18n("reg-buyer-saved"));
                    } else {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                },
                error: function (XMLHttpRequest,
                        textStatus, errorThrown) {
                    jAlert($.i18n("err-request-not-completed"));
                }
            });
        } else {
            jQuery.ajax({
                type: "POST",
                url: "registration/savelegalbuyer",
                data: $("#editprocessAttributeformID").serialize(),
                success: function (result) {
                    if (result !== null && result !== undefined) {
                        $("#buyersavebutton").prop("disabled", false).hide();
                        $("#personid").val(0);
                        $("#txtLegalBuyerName").val("");
                        $("#cbxLegalBuyerType").val("0");
                        $("#txtLegalBuyerRegNum").val("");
                        $("#txtLegalBuyerEstDate").val("");
                        $("#txtLegalBuyerAddress").val("");
                        $("#txtLegalBuyerRepName").val("");
                        $("#txtLegalBuyerRepPhone").val("");

                        SaleOwnerdBuyeretails(finallandid);
                        salebuyerdetails(selectedprocess);
                        $("#Seller_Details").hide();
                        $("#Buyer_Details").show();
                        $("#Land_Details_Sale").hide();
                        $("#regPoi").hide();
                        $("#Upload_Document_Sale").hide();
                        $("#selectedbuyer").addClass("ui-tabs-active");
                        $("#selectedbuyer").addClass("ui-state-active");
                        $("#selectedpoi").removeClass("ui-tabs-active");
                        $("#selectedpoi").removeClass("ui-state-active");
                        $("#comment_Save").hide();
                        $("#comment_Next").show();

                        jAlert($.i18n("reg-buyer-saved"));
                    } else {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                },
                error: function (XMLHttpRequest,
                        textStatus, errorThrown) {
                    jAlert($.i18n("err-request-not-completed"));
                }
            });
        }
    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function saveattributessale() {
    if (isVisible) {
        var errors = "";
        if ($("#sale_ownership_type").val() == "0") {
            errors += "- " + $.i18n("err-select-ownership-type") + "\n";
        } else {
            if (arry_Buyerbyprocessid != null && arry_Buyerbyprocessid.length > 0) {
                var isPerson = arry_Buyerbyprocessid[0].hasOwnProperty("firstname");
                
                if ((!isPerson && $("#sale_ownership_type").val() != "9") || (isPerson && $("#sale_ownership_type").val() == "9")) {
                    errors += "-" + $.i18n("err-select-correct-ownership") + "\n";
                }
            }
        }
        if ($("#sale_transfer_type").val() == "0") {
            errors += "- " + $.i18n("err-select-transfer-type") + "\n";
        }
        if ($("#sale_contract_name").val() === "") {
            errors += "- " + $.i18n("err-enter-contract-name") + "\n";
        }
        if ($("#sale_contract_date").val() === "") {
            errors += "- " + $.i18n("err-enter-contract-date") + "\n";
        }
        if ($("#sale_contract_num").val() === "") {
            errors += "- " + $.i18n("err-enter-contract-num") + "\n";
        }

        if (errors !== "") {
            jAlert(errors, $.i18n("err-alert"));
            return false;
        }

        if ($("#editprocessAttributeformID").valid()) {

            if (arry_Buyerbyprocessid == null || arry_Buyerbyprocessid.length < 1) {
                jAlert($.i18n("err-add-new-owner"), $.i18n("err-alert"));
                return false;
            }

            jConfirm($.i18n("reg-reg-confirmation"), $.i18n("gen-confirmation"),
                    function (response) {
                        if (response) {
                            jQuery.ajax({
                                type: "POST",
                                url: "registration/savefinalsaledata",
                                data: $("#editprocessAttributeformID").serialize(),
                                success: function (result) {
                                    if (result != null && result != undefined) {
                                        landRecordsInitialized_R = false;
                                        displayRefreshedRegistryRecords_ABC();
                                        jAlert($.i18n("gen-data-saved"));
                                        attributeEditDialog.dialog("close");
                                    } else {
                                        jAlert($.i18n("err-request-not-completed"));
                                    }
                                },
                                error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    jAlert($.i18n("err-request-not-completed"));
                                }
                            });
                        }
                    });
        } else {
            jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
        }
    } else {
        jAlert($.i18n("err-save-new-owner"), $.i18n("gen-info"));
    }
}

function saveLease() {
    var selectedprocess = $("#registration_process").val();
    if (selectedprocess == 5)
    {
        if ($("#surrenderLeaseId").val() === "") {
            saveattributesSurrenderLease();
        }
        registerLeaseSurrender();
    } else
    {
        registerLease();
    }
}

function saveattributesMortagage()
{
    var selectedprocess = $("#registration_process").val();
    if (selectedprocess == 9)
    {
        approveSurrenderMortagage();
    } else
    {
        saveattributesMortgage();
    }
}

function registerLease() {
    if ($("#editprocessAttributeformID").valid()) {
        var errors = "";
        if ($("#leaseid").val() === "") {
            jAlert($.i18n("err-add-lessee-lender"), $.i18n("err-alert"));
            return false;
        }

        if ($("#leaseYears").val() === "" && $("#leaseMonths").val() === "") {
            errors += "- " + $.i18n("err-enter-years-months") + "\n";
        }
        if ($("#lease_Amount").val() === "") {
            errors += "- " + $.i18n("err-enter-amount") + "\n";
        }
        if ($("#leaseStartDate").val() === "") {
            errors += "- " + $.i18n("err-enter-start-date") + "\n";
        }
        if (errors.length > 0) {
            jAlert(errors, $.i18n("err-alert"));
            return false;
        }

        jConfirm($.i18n("reg-reg-confirmation"), $.i18n("gen-confirmation"),
                function (response) {
                    if (response) {
                        jQuery.ajax({
                            type: "POST",
                            url: "registration/saveleasedata",
                            data: $("#editprocessAttributeformID").serialize(),
                            success: function (result) {
                                jAlert($.i18n("reg-lessee-loan-registered"), $.i18n("gen-info"));
                                $('#leaseeperson').val(0);
                                $("#leaseid").val("");
                                landRecordsInitialized_R = false;
                                displayRefreshedRegistryRecords_ABC();
                                attributeEditDialog.dialog("close");
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                jAlert($.i18n("err-request-not-completed"));
                            }
                        });
                    }
                });

    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function saveattributesSurrenderLease() {
    jQuery.ajax({
        type: "POST",
        async: false,
        url: "registration/savesurrenderleasedata",
        data: $("#editprocessAttributeformID").serialize(),
        success: function (result) {
            if (result != null && result != undefined) {
                $('#leaseeperson').val(0);
                $("#surrenderLeaseId").val(result);
            } else {
                jAlert($.i18n("err-request-not-completed"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });
}

function registerLeaseSurrender() {
    if ($("#editprocessAttributeformID").valid()) {
        jConfirm($.i18n("reg-reg-confirmation"), $.i18n("gen-confirmation"),
                function (response) {
                    if (response) {
                        jQuery.ajax({
                            type: "POST",
                            url: "registration/approvesurrenderleasedata",
                            data: $("#editprocessAttributeformID").serialize(),
                            success: function (result) {
                                if (result != null && result != undefined) {
                                    $('#leaseeperson').val(0);
                                    landRecordsInitialized_R = false;
                                    displayRefreshedRegistryRecords_ABC();
                                    attributeEditDialog.dialog("close");
                                } else {
                                    jAlert($.i18n("err-request-not-completed"));
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                jAlert($.i18n("err-request-not-completed"));
                            }
                        });
                    }
                });

    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function saveattributesMortgage() {
    if ($("#editprocessAttributeformID").valid()) {
        if ((mortgage_from.value.length == 0)
                || (mortgage_to.value.length == 0)
                || (amount_mortgage.value.length == 0)) {
            setTimeout(function () {
                $("#Tab_3").tabs({
                    active: 2
                });
                $("#editprocessAttributeformID").validate({
                    rules: {
                        mortgage_from: "required",
                        mortgage_to: "required",
                        amount_mortgage: "required"

                    },
                    messages: {
                        mortgage_from: "Please Enter mortgage from",
                        mortgage_to: "Please enter mortgage to",
                        amount_mortgage: "Please enter amount of mortgage"
                    }
                });
            }, 500);
        } else if ((mortgage_Financial_Agencies.value == "0")) {
            setTimeout(function () {
                $("#Tab_3").tabs({
                    active: 2
                });
                $("#editprocessAttributeformID").validate({
                    rules: {
                        mortgage_Financial_Agencies: {
                            required: true
                        },
                    },
                    messages: {
                        mortgage_Financial_Agencies: "Enter First name"

                    }
                });
            }, 500);
        }
    }

    if ($("#editprocessAttributeformID").valid()) {

        jConfirm(
                $.i18n("gen-save-confirmation"),
                $.i18n("gen-confirmation"),
                function (response) {
                    if (response) {
                        jQuery
                                .ajax({
                                    type: "POST",
                                    url: "registration/updateMortgageData",
                                    data: $("#editprocessAttributeformID")
                                            .serialize(),
                                    success: function (result) {
                                        if (result != null && result != undefined) {
                                            landRecordsInitialized_R = false;
                                            displayRefreshedRegistryRecords_ABC();
                                            //RegistrationRecords("registrationRecords");
                                            attributeEditDialog.dialog("close");

                                        } else {
                                            jAlert($.i18n("err-request-not-completed"));
                                        }
                                    },
                                    error: function (XMLHttpRequest,
                                            textStatus, errorThrown) {
                                        jAlert($.i18n("err-request-not-completed"));
                                    }
                                });
                    }

                });

    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function getRegionOnChangeCountry(id) {
    if (id != '') {

        $("#sale_region").empty();
        $("#sale_region").append(
                $("<option></option>").attr("value", "").text($.i18n("gen-select")));
        jQuery.ajax({
            url: "registration/allregion/" + id,
            async: false,
            success: function (regiondata) {
                var proj_region = regiondata;
                jQuery.each(proj_region, function (i, value) {
                    $("#sale_region").append(
                            $("<option></option>").attr("value",
                            value.hierarchyid).text(value.nameEn));
                });
            }
        });
    }
}

function getprocessvalue(id) {

    FillDataforRegistration(selectedlandid);
    var lease = document.getElementById("Tab_1");
    var sale = document.getElementById("Tab_2");
    var mortgage = document.getElementById("Tab_3");
    var split = document.getElementById("Tab_4");
    var tabPermission = document.getElementById("tabPermission");

    lease.style.display = "none";
    sale.style.display = "none";
    mortgage.style.display = "none";
    split.style.display = "none";
    tabPermission.style.display = "none";

    $("#comment_Next").show();
    $("#comment_Save").hide();
    SaleOwnerdBuyeretails(selectedlandid);

    if (id == 1 || id == 10 || id == 5) // Lease / surrender
    {
        var hasActiveLeases = false;
        jQuery.ajax({
            url: "registration/checkforactivelease/" + selectedlandid + "/" + id,
            async: false,
            success: function (response) {
                hasActiveLeases = response;
            }
        });

        if ((id == 1 || id == 10) && hasActiveLeases) {
            jAlert($.i18n("err-active-lease-exists"), $.i18n("err-alert"));
            return;
        }
        if (id == 5 && !hasActiveLeases) {
            jAlert($.i18n("err-no-active-lease"), $.i18n("err-alert"));
            return;
        }

        $("#leaseeperson").val(0);
        $("#tableLeaseApplicantDetails input").val("");
        $("#Lease_Details input").val("");
        $("#tableLeaseApplicantDetails select").val(0);
        $("#doc_name_Lease").val('');
        $("#doc_date_Lease").val('');
        $("#doc_desc_Lease").val('');
        $("#tabs_registry").tabs({active: 0});
        $("#Owner_Details").show();
        $("#Land_Details_lease").hide();
        $("#Applicant_Details").hide();
        $("#Lease_Details").hide();
        $("#Upload_Documents_Lease").hide();
        $("#selectedowner").addClass("ui-tabs-active");
        $("#selectedowner").addClass("ui-state-active");
        $("#selectedLanddetails").removeClass("ui-tabs-active");
        $("#selectedLanddetails").removeClass("ui-state-active");
        $("#selectedApplicant").removeClass("ui-tabs-active");
        $("#selectedApplicant").removeClass("ui-state-active");
        $("#selectedsleasedetails").removeClass("ui-tabs-active");
        $("#selectedsleasedetails").removeClass("ui-state-active");
        $("#selecteddocs").removeClass("ui-tabs-active");
        $("#selecteddocs").removeClass("ui-state-active");
        $('.trhideclass1').hide();
        $("#surrender_reason").val('');

        $("#lease_land_use").val(suReg.landusetype_en);
        processid = id;
        isVisible = true;
        lease.style.display = "block";
        sale.style.display = "none";
        mortgage.style.display = "none";
        split.style.display = "none";
        clearBuyerDetails_sale();

        $("#divReqDocsLoan").hide();
        $("#divReqDocsLease").hide();

        if (id == 1) {
            $("#divReqDocsLoan").show();
        } else if (id == 10) {
            $("#divReqDocsLease").show();
        }

        currentdiv = "Lease";

        fetchDocument(selectedlandid, 1, id);
        loadLease();
    } else if (id == 11 || id == 12) // Permission / surrender
    {
        var hasRegisteredPermission = false;
        jQuery.ajax({
            url: "registration/checkForRegisteredPermission/" + selectedlandid,
            async: false,
            success: function (response) {
                hasRegisteredPermission = response;
            }
        });

        if ((id == 11) && hasRegisteredPermission) {
            jAlert($.i18n("err-active-permission-exists"), $.i18n("err-alert"));
            return;
        }
        if (id == 12 && !hasRegisteredPermission) {
            jAlert($.i18n("err-no-active-permission"), $.i18n("err-alert"));
            return;
        }

        $("#permissionApplicantId").val("");
        $("#tablePermissionApplicantDetails input").val("");
        $("#tablePermissionApplicantDetails select").val(0);
        $("#tablePermDoc input").val("");
        $("#tablePermissionDetails input").val("");

        $("#tabs_permission").tabs({active: 0});
        permissionTabClick('liPermCurrentOwner', 'tabPermCurrentOwner');

        processid = id;
        tabPermission.style.display = "block";

        if (id == 11) {
            $("#divReqDocsDevPerm").show();
        } else {
            $("#divReqDocsDevPerm").hide();
        }
        currentdiv = "Permission";
        loadPermission();
    } else if (id == 2) // "Sale"
    {
        salebuyerdetails(id);
        $("#tabs_registry1").tabs({active: 0});
        $("#Seller_Details").show();
        $("#Land_Details_Sale").hide();
        $("#regPoi").hide();
        $("#Buyer_Details").hide();
        $("#Upload_Document_Sale").hide();
        $("#selectedseller").addClass("ui-tabs-active");
        $("#selectedseller").addClass("ui-state-active");
        $("#selectedland").removeClass("ui-tabs-active");
        $("#selectedland").removeClass("ui-state-active");
        $("#selectedbuyer").removeClass("ui-tabs-active");
        $("#selectedbuyer").removeClass("ui-state-active");
        $("#selecteddoc").removeClass("ui-tabs-active");
        $("#selecteddoc").removeClass("ui-state-active");
        $("#selectedpoi").removeClass("ui-tabs-active");
        $("#selectedpoi").removeClass("ui-state-active");

        processid = id;
        persontypeid = 11;
        $("#buyersavebutton").prop("disabled", false).hide();
        lease.style.display = "none";
        sale.style.display = "block";
        mortgage.style.display = "none";
        split.style.display = "none";

        currentdiv = "Sale";
        fetchDocument(selectedlandid, 11, id);

        $("#selectedselleranchor").text($.i18n("reg-current-owner"));
        $("#selectedbuyeranchor").text($.i18n("reg-new-owner"));
        $("#selectedsellerlabel").text($.i18n("reg-current-owner-details"));
        $("#selectedbuyerlabel").text($.i18n("reg-buyer-details"));

        loadResPersonsOfEditingForEditing();
    } else if (id == 3) // "Mortgage"
    {
        $("#tabs_registry2").tabs({active: 0});
        $("#MortgageOwner_Details").show();
        $("#Land_Details_Mortgage").hide();
        $("#Mortgage_Details").hide();
        $("#Upload_Document_Mortgage").hide();
        $("#selectedownerdetails").addClass("ui-tabs-active");
        $("#selectedownerdetails").addClass("ui-state-active");
        $("#selectelandmortgage").removeClass("ui-tabs-active");
        $("#selectelandmortgage").removeClass("ui-state-active");
        $("#selectemortgage").removeClass("ui-tabs-active");
        $("#selectemortgage").removeClass("ui-state-active");
        $("#selectemortgagedocs").removeClass("ui-tabs-active");
        $("#selectemortgagedocs").removeClass("ui-state-active");

        $('.trhideclassmortgage2').hide();

        processid = id;
        persontypeid = 1;
        $("#buyersavebutton").prop("disabled", false).hide();
        lease.style.display = "none";
        sale.style.display = "none";
        mortgage.style.display = "block";
        split.style.display = "none";
        currentdiv = "Mortgage";

        fetchDocument(selectedlandid, 1, id);
        fillMortgageDetails();
        loadResPersonsOfEditingForEditing();

    } else if (id == 9) // "Surrender of Mortgage"
    {
        $("#tabs_registry2").tabs({active: 0});
        $("#MortgageOwner_Details").show();
        $("#Land_Details_Mortgage").hide();
        $("#Mortgage_Details").hide();
        $("#Upload_Document_Mortgage").hide();
        $("#selectedownerdetails").addClass("ui-tabs-active");
        $("#selectedownerdetails").addClass("ui-state-active");
        $("#selectelandmortgage").removeClass("ui-tabs-active");
        $("#selectelandmortgage").removeClass("ui-state-active");
        $("#selectemortgage").removeClass("ui-tabs-active");
        $("#selectemortgage").removeClass("ui-state-active");
        $("#selectemortgagedocs").removeClass("ui-tabs-active");
        $("#selectemortgagedocs").removeClass("ui-state-active");
        $('.trhideclassmortgage2').show();

        processid = id;
        persontypeid = 1;
        $("#buyersavebutton").prop("disabled", false).hide();
        lease.style.display = "none";
        sale.style.display = "none";
        mortgage.style.display = "block";
        split.style.display = "none";
        currentdiv = "Mortgage";

        fetchDocument(selectedlandid, 1, id);

        jQuery.ajax({
            url: "registration/SurrenderMortagagedata/" + selectedlandid + "/" + id,
            async: false,
            success: function (data) {
                surrendermortagagedata = data;
            }
        });

        $("#mortgagesurrender_reason").val(surrendermortagagedata.surrenderreason);
        loadResPersonsOfEditingForEditing();
    } else if (id == 8) //split
    {
        processid = id;
        $("#comment_Save").hide();

        $("#buyersavebutton").prop("disabled", false).hide();
        lease.style.display = "none";
        sale.style.display = "none";
        mortgage.style.display = "none";
        split.style.display = "block";
        currentdiv = "split";
        $("#doc_name_split").val("");
        $("#doc_date_split").val("");
        $("#doc_desc_split").val("");
        fetchDocumentSplit(selectedlandid);
    }
}

function clearBuyerDetails_sale() {
    $("#firstname_r_sale1").val('');
    $("#middlename_r_sale1").val('');
    $("#lastname_r_sale1").val('');
    $("#id_r1").val('');
    $("#sale_idtype_buyer").val('');
    $("#contact_no1").val('');
    $("#sale_gender_buyer").val('');
    $("#address_sale1").val('');
    $("#date_Of_birth_sale1").val('');
    $("#sale_marital_buyer").val('');
    $("#doc_name_sale").val('');
    $("#doc_date_sale").val('');
    $("#doc_desc_sale").val('');
    $("#Newfiles").val('');
    $("#fileUploadNewDowcumentss").val('');
    $("#remrks_sale").val('');

    // Legal entity
    $("#cbxLegalBuyerType").val("0");
    $("#txtLegalBuyerName").val("");
    $("#txtLegalBuyerRegNum").val("");
    $("#txtLegalBuyerEstDate").val("");
    $("#txtLegalBuyerAddress").val("");
    $("#txtLegalBuyerRepName").val("");
    $("#txtLegalBuyerRepPhone").val("");
}

function clear_Lease() {
    $("#tableLeaseApplicantDetails input").val("");
    $("#tableLeaseApplicantDetails select").val(0);

    $("#leaseMonths").val('');
    $("#leaseYears").val("");
    $("#leaseConditions").val("");
    $("#lease_Amount").val('');
    $("#NewfilesLease").val('');
    $("#fileUploadNewDowcumentsLease").val('');
    $("#doc_name_Lease").val('');
    $("#doc_date_Lease").val('');
    $("#doc_desc_Lease").val('');
    $("#remrks_lease").val('');
    $("#doc_Type_Lease").val('');
}

function clearMortgage() {

    $("#mortgage_Financial_Agencies").val('');
    $("#mortgage_from").val('');
    $("#mortgage_to").val('');
    $("#amount_mortgage").val('');
    $("#doc_name_mortgage").val('');
    $("#doc_date_mortgage").val('');
    $("#doc_desc_mortgage").val('');
    $("#NewfilesMortgage").val('');
    $("#fileUploadNewDowcumentsMortgage").val('');
    $("#remrks_mortgage").val('');
    $("#doc_Type_Mortgage").val('');

}

function formatDate_R(date) {
    return jQuery.datepicker.formatDate('yy-mm-dd', new Date(date));
}

function formatDate2_R(intDate) {
    return jQuery.datepicker
            .formatDate('yy-mm-dd', new Date(parseInt(intDate)));
}

function isNumber(evt) {

    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function validateSale() {
    $("#editprocessAttributeformID").validate({
        rules: {
            firstname_r_sale1: {
                required: true
            },
            // middlename_r_sale1: "required",
            lastname_r_sale1: "required",
            id_r1: "required",
            sale_idtype_buyer: {
                required: true
            },
            contact_no1: {
                required: true
            },
            date_Of_birth_sale1: {
                required: true
            },
            sale_gender_buyer: {
                required: true
            },
            sale_marital_buyer: "required",
            address_sale1: "required"
        },
        messages: {
            firstname_r_sale1: $.i18n("err-select-firstname"),
            lastname_r_sale1: $.i18n("err-select-lastname"),
            id_r1: $.i18n("err-enter-idnumber"),
            sale_idtype_buyer: $.i18n("err-select-id-type"),
            contact_no1: $.i18n("err-enter-phone-number"),
            date_Of_birth_sale1: $.i18n("err-enter-dob"),
            sale_gender_buyer: $.i18n("err-select-gender"),
            sale_marital_buyer: $.i18n("err-select-marital-status"),
            address_sale1: $.i18n("err-enter-address")
        }
    });

    if ($("#editprocessAttributeformID").valid()) {
        if ((firstname_r_sale1.value.length == 0)
                || (lastname_r_sale1.value.length == 0)
                || (id_r1.value.length == 0) || (contact_no1.value.length == 0)
                || (date_Of_birth_sale1.value.length == 0)
                || (address_sale1.value.length == 0)) { // ||
            // (middlename_r_sale1.value.length
            // == 0)
            setTimeout(function () {
                $("#Tab_2").tabs({
                    active: 2
                });

            }, 500);
            return false;
        } else if ((sale_idtype_buyer.value == "0")
                || (sale_gender_buyer.value == "0")) {
            setTimeout(function () {
                $("#Tab_2").tabs({
                    active: 2
                });

            }, 500);
            return false;
        }
    } else {

        setTimeout(function () {
            $("#Tab_2").tabs({
                active: 2
            });
            // do something special
        }, 500);
        // $("#Buyer_Details").show();
        return false;

    }
}

function validateMortgage() {
    $("#editprocessAttributeformID").validate({
        rules: {
            mortgage_from: "required",
            mortgage_to: "required",
            amount_mortgage: "required",
            mortgage_Financial_Agencies: {
                required: true
            },
            doc_name_mortgage: {
                required: true
            },
            doc_date_mortgage: {
                required: true
            },
            doc_desc_mortgage: {
                required: true
            },
            doc_Type_Mortgage: {
                required: true
            },
            NewfilesMortgage: {
                required: true
            },
            remrks_mortgage: "required"
        },
        messages: {
            mortgage_from: $.i18n("err-enter-mortgage-from"),
            mortgage_to: $.i18n("err-enter-mortgage-to"),
            amount_mortgage: $.i18n("err-enter-amount"),
            mortgage_Financial_Agencies: $.i18n("err-enter-fin-agency"),
            doc_name_mortgage: $.i18n("err-enter-doc-name"),
            doc_date_mortgage: $.i18n("err-enter-doc-date"),
            doc_desc_mortgage: $.i18n("err-enter-doc-desc"),
            doc_Type_Mortgage: $.i18n("err-select-doc-type"),
            NewfilesMortgage: $.i18n("err-select-file"),
            remrks_mortgage: $.i18n("err-enter-remarks")
        }
    });

    if ($("#editprocessAttributeformID").valid()) {
        if ((mortgage_from.value.length == 0)
                || (mortgage_to.value.length == 0)
                || (amount_mortgage.value.length == 0)) {
            setTimeout(function () {
                $("#Tab_3").tabs({
                    active: 2
                });
                // do something special
            }, 500);
        } else if ((mortgage_Financial_Agencies.value == "0")) {
            setTimeout(function () {
                $("#Tab_3").tabs({
                    active: 2
                });
                // do something special
            }, 500);
        }
    } else {

        setTimeout(function () {
            $("#Tab_3").tabs({
                active: 2
            });
            // do something special
        }, 500);

        return false;

    }
}

function validateLease() {
    $("#editprocessAttributeformID").validate({
        rules: {
            firstname_r_applicant: {
                required: true
            },
            // middlename_r_applicant: "required",
            lastname_r_applicant: "required",
            id_r_applicant: "required",
            id_type_applicant: {
                required: true
            },
            contact_no_applicant: {
                required: true
            },
            gender_type_applicant: {
                required: true
            },
            address_lease_applicant: {
                required: true
            },
            date_Of_birth_applicant: "required",
            martial_sts_applicant: "required"
                    /* NewfilesLease: {required: true} */

        },
        messages: {
            firstname_r_applicant: $.i18n("err-select-firstname"),
            lastname_r_applicant: $.i18n("err-select-lastname"),
            id_r_applicant: $.i18n("err-enter-idnumber"),
            id_type_applicant: $.i18n("err-select-id-type"),
            contact_no_applicant: $.i18n("err-enter-phone-number"),
            gender_type_applicant: $.i18n("err-select-gender"),
            address_lease_applicant: $.i18n("err-enter-address"),
            date_Of_birth_applicant: $.i18n("err-enter-dob"),
            martial_sts_applicant: $.i18n("err-select-marital-status")
        }
    });

    if ($("#editprocessAttributeformID").valid()) {
        if ((firstname_r_applicant.value.length == 0)
                || (lastname_r_applicant.value.length == 0)
                || (id_r_applicant.value.length == 0)
                || (contact_no_applicant.value.length == 0)
                || (date_Of_birth_applicant.value.length == 0)
                || (address_lease_applicant.value.length == 0)) { // ||
            // (middlename_r_applicant.value.length
            // == 0)
            setTimeout(function () {
                $("#Tab_1").tabs({
                    active: 1
                });

            }, 500);
        } else if ((id_type_applicant.value == "0")
                || (gender_type_applicant.value == "0")
                || (martial_sts_applicant.value == "0")) {
            setTimeout(function () {
                $("#Tab_1").tabs({
                    active: 1
                });

            }, 500);
        }
    } else {

        setTimeout(function () {
            $("#Tab_1").tabs({
                active: 1
            });
            // do something special
        }, 500);

        return false;

    }
}

function validateLeasedata() {
    $("#editprocessAttributeformID").validate({
        rules: {
            // no_Of_years_Lease: "required",
            leaseMonths: "required",
            lease_Amount: "required"
        },
        messages: {
            // no_Of_years_Lease: "Enter No of Years",
            leaseMonths: $.i18n("err-enter-no-of-month"),
            lease_Amount: $.i18n("err-enter-lease-amount")
        }
    });

    if ($("#editprocessAttributeformID").valid()) {

        if ((leaseMonths.value.length == 0)
                || (lease_Amount.value.length == 0)) {// (no_Of_years_Lease.value.length
            // == 0) ||
            setTimeout(function () {
                $("#Tab_1").tabs({
                    active: 2
                });

            }, 500);
        }

    } else {

        setTimeout(function () {
            $("#Tab_1").tabs({
                active: 2
            });
            // do something special
        }, 500);

        return false;

    }
}

function isDecNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}

function ViewHistory(landid) {
    $(".signin_menu").hide();
    TransLandId = landid;
    $('#PropertyHistTrackingBody').empty();
    $('#PropertyLeaseHistTrackingBody').empty();
    $('#PropertyMortageHistTrackingBody').empty();
    $('#TransactionHistTrackingBody').empty();
    jQuery.ajax({
        type: 'GET',
        url: "landrecords/parcelhistory/" + Global.LANG + "/" + landid,
        async: false,
        success: function (data) {
            if (data.length > 0) {
                if (data[0] != null) {
                    $("#commentsTablePop").show();
                    $.each(data[0], function (index, optionData) {
                        $("#PropertyHistTrackingTemplate").tmpl(optionData).appendTo("#PropertyHistTrackingBody");
                    });
                }

                if (data[3] != null) {
                    $("#commentsTablePopTransactionHist").show();
                    $.each(data[3], function (index, optionData) {
                        $("#TransactionHistTrackingTemplate").tmpl(optionData).appendTo("#TransactionHistTrackingBody");
                        $("#TransactionHistTrackingBody").i18n();
                    });
                }

                commentHistoryDialogPop = $("#commentsDialogpopup").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 800,
                    resizable: true,
                    modal: true,
                    buttons: [{
                            text: $.i18n("gen-cancel"),
                            "id": "comment_cancel",
                            click: function () {
                                setInterval(function () {}, 4000);
                                $('#PropertyHistTrackingBody').empty();
                                $('#PropertyLeaseHistTrackingBody').empty();
                                $('#PropertyMortageHistTrackingBody').empty();
                                $("#commentsTablePop").hide();
                                $("#commentsTablePopLease").hide();
                                $("#commentsTablePopMortage").hide();
                                $("#commentsTablePopTransactionHist").hide();
                                commentHistoryDialogPop.dialog("destroy");
                            }
                        }],
                    close: function () {
                        $("#commentsTablePop").hide();
                        $("#commentsTablePopLease").hide();
                        $("#commentsTablePopMortage").hide();
                    }
                });
                $("#comment_cancel").html('<span class="ui-button-text">' + $.i18n("gen-cancel") + '</span>');
                commentHistoryDialogPop.dialog("open");
                document.getElementById('btnInitTrans').blur();
            } else {
                jAlert("No Records", $.i18n("gen-info"));
            }
        }
    });
}


function viewleasedetail(transactionid, landid)
{
    jQuery.ajax({
        type: 'GET',
        url: 'landrecords/findleasedetailbylandid/' + transactionid + "/" + landid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-tran-details")
                        + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>' + $.i18n("reg-name") + '</th><th>' + $.i18n("reg-address") + '</th><th>' + $.i18n("reg-id-number") + '</th><th>' + $.i18n("reg-lease-start-date") + '</th><th>' + $.i18n("reg-lease-end-date") + '</th><th>' + $.i18n("reg-lease-year") + '</th><th>' + $.i18n("reg-lease-month") + '</th><th>' + $.i18n("reg-lease-amount") + '</th></tr></thead><tbody id="popupBody"></tbody></table></div>');

                var conditions = contactObj[0][0].conditions;
                if (conditions !== null || conditions.length > 0) {
                    conditions = conditions.replace(/(?:\r\n|\r|\n)/g, '<br />');
                }
                $('#ViewPopuupDiv').append('<h3 class="gridTitle"><span>' + $.i18n("reg-lease-conditions") + '</span></h3><hr><i>' + conditions + '</i><br><br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>' + $.i18n("reg-doc-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>' + $.i18n("reg-doc-name") + '</th><th>' + $.i18n("reg-doc-date") + '</th><th>' + $.i18n("reg-doc-desc") + '</th><th>' + $.i18n("reg-view-doc") + '</th></tr></thead><tbody id="popupBodydocument"></tbody></table></div></br>');

                if (contactObj[0] != null && contactObj[0].length > 0)
                {
                    $("#PropertyLeaseTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupBody");
                }

                if (contactObj[1] != null && contactObj[1].length > 0)
                {
                    $("#DocumentTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupBodydocument");
                    $("#popupBodydocument").i18n();
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 450,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-lease-loan"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}


function viewOwnerDetails(transactionid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findsaledetailbytransid/' + transactionid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-current-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupCurrentBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-old-owner")
                        + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupoldBody"></tbody></table></div></br>');


                if (contactObj[0] != null)
                {
                    $("#PropertyOldSaleTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupoldBody");
                }

                if (contactObj[1] != null)
                {
                    $("#PropertyCurrentSaleTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupCurrentBody");
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-changeof-owner"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}
function viewSaleDetails(transactionid) {
    jQuery.ajax({
        type: 'GET',
        url: 'landrecords/findsaledetailbytransid/' + transactionid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-current-owner")
                        + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupCurrentBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-old-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupoldBody"></tbody></table></div></br>');

                if (contactObj[0] != null)
                {
                    if (contactObj[0][0].hasOwnProperty("firstname")) {
                        $("#PropertyOldSaleTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupoldBody");
                    } else {
                        $("#PropertyOldLelgalSaleTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupoldBody");
                    }
                }

                if (contactObj[1] != null)
                {
                    if (contactObj[1][0].hasOwnProperty("firstname")) {
                        $("#PropertyOldSaleTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupCurrentBody");
                    } else {
                        $("#PropertyOldLelgalSaleTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupCurrentBody");
                    }
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-details"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}

function viewGiftDetails(transactionid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findsaledetailbytransid/' + transactionid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-current-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupCurrentBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-old-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupoldBody"></tbody></table></div></br>');

                if (contactObj[0] != null)
                {
                    $("#PropertyOldSaleTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupoldBody");
                }

                if (contactObj[1] != null)
                {
                    $("#PropertyCurrentSaleTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupCurrentBody");
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-gift"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}
function viewJointDetails(transactionid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findsaledetailbytransid/' + transactionid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-current-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date") + '</th></tr></thead><tbody id="popupCurrentBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-old-owner") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-ownership-date")
                        + '</th></tr></thead><tbody id="popupoldBody"></tbody></table></div></br>');

                if (contactObj[0] != null)
                {
                    $("#PropertyOldSaleTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupoldBody");
                }

                if (contactObj[1] != null)
                {
                    $("#PropertyCurrentSaleTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupCurrentBody");
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-change-of-joint-owner"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}
function viewsurrenderleasedetail(transactionid, landid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findsurrenderleasedetailbylandid/' + transactionid + "/" + landid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-tran-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-name") + '</th><th>'
                        + $.i18n("reg-address") + '</th><th>'
                        + $.i18n("reg-id-number") + '</th><th>'
                        + $.i18n("reg-lease-start-date") + '</th><th>'
                        + $.i18n("reg-lease-end-date") + '</th><th>'
                        + $.i18n("reg-lease-year") + '</th><th>'
                        + $.i18n("reg-lease-month") + '</th><th>'
                        + $.i18n("reg-lease-amount")
                        + '</th></tr></thead><tbody id="popupBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-doc-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-doc-name") + '</th><th>'
                        + $.i18n("reg-doc-date") + '</th><th>'
                        + $.i18n("reg-doc-desc") + '</th><th>'
                        + $.i18n("reg-view-doc")
                        + '</th></tr></thead><tbody id="popupBodydocument"></tbody></table></div></br>');

                if (contactObj[0] != null && contactObj[0].length > 0)
                {
                    $("#PropertyLeaseTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupBody");
                }

                if (contactObj[1] != null && contactObj[1].length > 0)
                {
                    $("#DocumentTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupBodydocument");
                    $("#popupBodydocument").i18n();
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 450,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-lease-loan"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}

function viewMortagagedetail(transactionid, landid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findmortagagedetailbylandid/' + transactionid + "/" + landid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-mortgage-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-fin-agency") + '</th><th>'
                        + $.i18n("reg-mortgage-from") + '</th><th>'
                        + $.i18n("reg-mortgage-to") + '</th><th>'
                        + $.i18n("reg-mortgage-amount")
                        + '</th></tr></thead><tbody id="popupBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-doc-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-doc-name") + '</th><th>'
                        + $.i18n("reg-doc-date") + '</th><th>'
                        + $.i18n("reg-doc-desc") + '</th><th>'
                        + $.i18n("reg-view-doc")
                        + '</th></tr></thead><tbody id="popupBodydocument"></tbody></table></div></br>');
                if (contactObj[0] != null && contactObj[0].length > 0)
                {
                    $("#PropertyMortageTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupBody");
                }

                if (contactObj[1] != null && contactObj[1].length > 0)
                {
                    $("#DocumentTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupBodydocument");
                    $("#popupBodydocument").i18n();
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-mortgage"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}


function viewSurrenderMortagagedetail(transactionid, landid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: 'landrecords/findSurrendermortagagedetailbylandid/' + transactionid + "/" + landid,
        async: false,
        success: function (contactObj) {

            if (contactObj != null && contactObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-mortgage-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-fin-agency") + '</th><th>'
                        + $.i18n("reg-mortgage-from") + '</th><th>'
                        + $.i18n("reg-mortgage-to") + '</th><th>'
                        + $.i18n("reg-mortgage-amount")
                        + '</th></tr></thead><tbody id="popupBody"></tbody></table></div></br>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-doc-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-doc-name") + '</th><th>'
                        + $.i18n("reg-doc-date") + '</th><th>'
                        + $.i18n("reg-doc-desc") + '</th><th>'
                        + $.i18n("reg-view-doc")
                        + '</th></tr></thead><tbody id="popupBodydocument"></tbody></table></div></br>');
                if (contactObj[0] != null && contactObj[0].length > 0)
                {
                    $("#PropertyMortageTrackingTemplate").tmpl(contactObj[0]).appendTo("#popupBody");
                }

                if (contactObj[1] != null && contactObj[1].length > 0)
                {
                    $("#DocumentTrackingTemplate").tmpl(contactObj[1]).appendTo("#popupBodydocument");
                    $("#popupBodydocument").i18n();
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 400,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-surrender-mortgage"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}



function viewdocumentdetailForTransaction(personid, transactionid) {
    window.open("registrationdetails/viewdoc/" + personid + "/" + transactionid, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
}

function viewdocumentdetail(transactionid)
{
    jQuery.ajax({
        type: 'GET',
        //url:'viewArchivalChangesDetail/'+requestid,
        url: "landrecords/viewdocumentdetail/" + transactionid,
        async: false,
        success: function (docObj) {

            if (docObj != null && docObj.length > 0)
            {
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-doc-details") + '</span></h3><table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01"><thead><tr id="LandownerPersonTR" ><th>'
                        + $.i18n("reg-doc-name") + '</th><th>'
                        + $.i18n("reg-doc-date") + '</th><th>'
                        + $.i18n("reg-doc-desc") + '</th><th>'
                        + $.i18n("reg-view-doc")
                        + '</th></tr></thead><tbody id="popupBody"></tbody></table></div></br>');

                if (docObj[0] != null && docObj[0].length > 0)
                {
                    $("#DocumentTrackingTemplate").tmpl(docObj[0]).appendTo("#popupBody");
                    $("#popupBody").i18n();
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 220,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-doc-details"));

            } else
            {
                alertMsg = $.i18n("err-no-contact-assosiated");
                jAlert('info', alertMsg, alertInfoHeader);
            }
        },
        error: function () {

        }
    });
}

function makeFileListLease() {
    var fi = document.getElementById('fileUploadNewDowcumentsLease');

    if (fi.files.length > 0) {

        for (var i = 0; i <= fi.files.length - 1; i++) {
            var fname = fi.files.item(i).name; // THE NAME OF THE FILE.
            // var fsize = fi.files.item(i).size; // THE SIZE OF THE FILE.

            document.getElementById('fp_lease').innerHTML = document
                    .getElementById('fp_lease').innerHTML
                    + fname + '<br /> ';
        }
    }
}
function makeFileList() {


    $('#landRegistration_docs').empty();

    var jsonObjItems = [];
    var val1 = 0;
    $.each($('#fileUploadNewDowcumentss')[0].files, function (ind2, obj2) {
        var myObject = new Object();
        $.each($('#fileUploadNewDowcumentss')[0].files[val1], function (ind3,
                obj3) {
            myObject.name = $("#doc_name_sale").val();
            myObject.description = $("#doc_desc_sale").val();
            myObject.docType = $("#doc_Type_Sale option:selected").text();
            jsonObjItems.push(myObject);

        });
        val1 = val1 + 1;
    });

    $("#landRegistration_docsTemplate").tmpl(jsonObjItems).appendTo(
            "#landRegistration_docs");

}

function makeFileList_mortgage() {
    var fi = document.getElementById('fileUploadNewDowcumentsMortgage');

    if (fi.files.length > 0) {

        for (var i = 0; i <= fi.files.length - 1; i++) {
            var fname = fi.files.item(i).name; // THE NAME OF THE FILE.
            // var fsize = fi.files.item(i).size; // THE SIZE OF THE FILE.

            document.getElementById('fp_mortgage').innerHTML = document
                    .getElementById('fp_mortgage').innerHTML
                    + fname + '<br /> ';
        }
    }
}

function ActionfillRegistration(landid, transactionid, apfrNo, shareTypeId) {
    transid = transactionid;
    var parcelnumwithpadding = pad(landid, 9);
    _parcelNumber = parcelnumwithpadding;

    if (claimtypeid === 3) {
        $("#liDisputes").show();
    } else {
        $("#liDisputes").hide();
    }

    $("#parcelnovalue").text(parcelnumwithpadding);
    $("#regnovalue").text(apfrNo);

    var appid = '#' + landid + "_registration";
    $("" + appid + "").empty();
    $(".containerDiv").empty();

    var html = "";
    html += "<li> <a title='" + $.i18n("reg-view-land-record") + "' href='#' onclick='editAttribute(" + landid + ", false)'>" + $.i18n("reg-view-land-record") + "</a></li>";
    html += "<li> <a title='" + $.i18n("reg-view-spatial-data") + "' href='#' onclick='showOnMap(" + landid + ", 0)'>" + $.i18n("reg-view-spatial-data") + "</a></li>";
    html += "<li> <a title='" + $.i18n("reg-print-apfr") + "' href='#' onclick='printApfr(" + landid + ", " + shareTypeId + ")'>" + $.i18n("reg-print-apfr") + "</a></li>";
    html += "<li> <a title='" + $.i18n("reg-print-permission") + "' href='#' onclick='printPermission(" + landid + ")'>" + $.i18n("reg-print-permission") + "</a></li>";
    html += "<li> <a title='" + $.i18n("reg-init-tran") + "' href='#' onclick='leaseAttribute(" + landid + ")'>" + $.i18n("reg-init-tran") + "</a></li>";
    html += "<li> <a title='" + $.i18n("reg-view-tran-history") + "' id='' name=''  href='#' onclick='ViewHistory(" + landid + ")'>" + $.i18n("reg-view-tran-history") + "</a></li>";


    $("" + appid + "").append('<div class="signin_menu"><div class="signin"><ul>' + html + '</ul></div></div>');

    $(".signin_menu").toggle();
    $(".signin").toggleClass("menu-open");

    $(".signin_menu").mouseup(function () {
        return false;
    });
    $(document).mouseup(function (e) {
        if ($(e.target).parent("a.signin").length == 0) {
            $(".signin").removeClass("menu-open");
            $(".signin_menu").hide();
        }
    });
}
var land_RV = null;

function printApfr(landid, shareTypeId) {
    if (shareTypeId === 7) {
        // Individual
        jQuery.ajax({
            url: "landrecords/spatialunit/form5/" + landid,
            async: false,
            success: function (data) {
                if (isEmpty(data.mutationType)) {
                    // Generate form 5
                    generateform5(landid, data);
                } else {
                    // Generate form 51
                    generateform51(landid, data);
                }
            }
        });
    } else if (shareTypeId === 9) {
        jQuery.ajax({
            url: "landrecords/spatialunit/form52le/" + landid,
            async: false,
            success: function (data) {
                generateform52Le(landid, data);
            }
        });
    } else {
        jQuery.ajax({
            url: "landrecords/spatialunit/form8/" + landid,
            async: false,
            success: function (data) {
                if (isEmpty(data.mutationType)) {
                    // Generate form 8
                    generateform8(landid, data);
                } else {
                    // Generate form 52
                    generateform52(landid, data);
                }
            }
        });
    }
}

function viewLandAttribute(landid) {
    $(".signin_menu").hide();
    selectedlandid = landid;
    FillRegistrationPersonDataNew();
    FillResourceNonNaturalPersonDataNew();
    loadPOIEditing();
    $("#landidhide").val(landid);
    province_r = null;
    region_r = null;
    jQuery.ajax({
        url: "registration/allregion/" + country_r_id,
        async: false,
        success: function (data) {
            region_r = data;
            if (data.length > 0) {
                // data.xyz.name_en for getting the data
            }
        }
    });

    jQuery.ajax({
        url: "registration/allprovince/" + region_r_id,
        async: false,
        success: function (data) {
            province_r = data;
        }
    });

    jQuery.ajax({
        url: "registration/laspatialunitland/" + landid,
        async: false,
        success: function (data) {
            land_RV = data;

        }
    });
    jQuery.ajax({
        url: "landrecords/landusertype/",
        async: false,
        success: function (data) {
            landUserList = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/claimtypes/",
        async: false,
        success: function (data) {
            claimTypes_R = data;
        }
    });

    // Land Details landtype_r
    $("#Land_Type_l_record").empty();
    $("#country_l_record").empty();
    $("#region_l_record").empty();
    $("#province_r").empty();
    $("#Ownership_Type_l_record").empty();
    $("#Claim_Type_l_record").empty();
    $("#provience_l_record").empty();
    $("#existing_use_LR").empty();
    $("#proposed_use_LR").empty();


    $("#country_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#region_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#provience_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#Land_Type_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#Ownership_Type_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#Claim_Type_l_record").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    jQuery.each(landtype_r, function (i, obj1) {
        $("#Land_Type_l_record").append(
                $("<option></option>").attr("value", obj1.landtypeid)
                .text(obj1.landtypeEn));
    });
    jQuery.each(allcountry, function (i, obj) {
        $("#country_l_record").append(
                $("<option></option>").attr("value", obj.hierarchyid)
                .text(obj.nameEn));
    });

    jQuery.each(region_r, function (i, obj) {
        $("#region_l_record").append(
                $("<option></option>").attr("value", obj.hierarchyid)
                .text(obj.nameEn));
    });
    jQuery.each(province_r, function (i, obj) {
        $("#provience_l_record").append(
                $("<option></option>").attr("value", obj.hierarchyid)
                .text(obj.nameEn));
    });

    jQuery.each(landsharetype_r, function (i, obj) {
        $("#Ownership_Type_l_record").append(
                $("<option></option>").attr("value", obj.landsharetypeid)
                .text(obj.landsharetypeEn));
    });

    jQuery.each(claimTypes_R, function (i, obj) {
        $("#Claim_Type_l_record").append(
                $("<option></option>").attr("value", obj.code)
                .text(obj.name));
    });

    if (land_RV != null && land_RV != null)
    {
        $("#Land_Type_l_record").val(land_RV.landtypeid);
        $("#Claim_Type_l_record").val(land_RV.claimtypeid);
        $("#country_l_record").val(land_RV.hierarchyid1);
        $("#region_l_record").val(land_RV.hierarchyid2);
        $("#provience_l_record").val(land_RV.hierarchyid3);

        $("#parcel_l_record").val("000000" + land_RV.landid);
        $("#Ownership_Type_l_record").val(land_RV.landsharetypeid);// landsharetypeid
        $("#area_LR").val(land_RV.area);
        if (land_RV.other_use != "" && land_RV.other_use != null) {
            $("#other_useregistrationpage").val(land_RV.other_use);
        } else {
            $("#other_useregistrationpage").val("");
        }

        $("#lease_area_View").val(land_RV.area);

        $("#neighbor_west_LR").val(land_RV.neighbor_west);
        $("#neighbor_north_LR").val(land_RV.neighbor_north);
        $("#neighbor_south_LR").val(land_RV.neighbor_south);
        $("#neighbor_east_LR").val(land_RV.neighbor_east);


        $("#existing_use_LR").empty();
        $("#existing_use_LR").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
        jQuery.each(landUserList, function (i, landuseobj) {
            if (landuseobj.landusetypeid != '9999')
                $("#existing_use_LR").append($("<option></option>").attr("value", landuseobj.landusetypeid).text(landuseobj.landusetypeEn));

        });
        $("#existing_use_LR").val(land_RV.landusetypeid);

        $("#proposed_use_LR").empty();
        $("#proposed_use_LR").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
        jQuery.each(landUserList, function (i, landuseobj) {
            if (landuseobj.landusetypeid != '9999')
                $("#proposed_use_LR").append($("<option></option>").attr("value", landuseobj.landusetypeid).text(landuseobj.landusetypeEn));

        });
        $("#proposed_use_LR").val(land_RV.proposedused);

    }

    commentviewLandDialogPop = $("#landrecordsview").dialog({
        autoOpen: false,
        height: 700,
        width: 1200,
        resizable: true,
        modal: true,
        buttons: [{
                text: $.i18n("gen-cancel"),
                "id": "comment_cancel",
                click: function () {
                    setInterval(function () {

                    }, 4000);
                    commentviewLandDialogPop.dialog("destroy");

                }
            }],
        close: function () {

        }
    });
    $("#comment_cancel").html('<span class="ui-button-text">' + $.i18n("gen-cancel") + '</span>');
    commentviewLandDialogPop.dialog("open");
}


function saveattributesLeasePersonData() {
    if (!editlease) {
        $("#leaseeperson").val(0);
    }
    if ($("#editprocessAttributeformID").valid()) {
        var errors = "";
        if (firstname_r_lease.value.length === 0) {
            errors += "-" + $.i18n("err-select-firstname") + "\n";
        }
        if (lastname_r_lease.value.length === 0) {
            errors += "-" + $.i18n("err-select-lastname") + "\n";
        }
        if (date_Of_birth_lease.value.length === 0) {
            errors += "-" + $.i18n("err-enter-dob") + "\n";
        }
        if (id_lease.value.length === 0) {
            errors += "-" + $.i18n("err-enter-idnumber") + "\n";
        }
        if (id_date_lease.value.length === 0) {
            errors += "-" + $.i18n("err-enter-id-date") + "\n";
        }

        if (errors !== "") {
            jAlert(errors, $.i18n("err-alert"));
            return false;
        }

        jQuery.ajax({
            type: "POST",
            url: "registration/saveLeaseeDetails/" + processid,
            data: $("#editprocessAttributeformID").serialize(),
            success: function (result) {
                if (result !== null && result !== undefined) {
                    $("#saveleasee").prop("disabled", false).hide();
                    $("#tableLeaseApplicantDetails input").val("");
                    $("#tableLeaseApplicantDetails select").val(0);
                    $("#leaseid").val(result);
                    loadLease();
                    jAlert($.i18n("reg-lessee-saved"));
                } else {
                    jAlert($.i18n("err-request-not-completed"));
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                jAlert($.i18n("err-request-not-completed"));
            }
        });
    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function initiateTransaction() {
    edit = 0;
    $("#registration_process").show();
    leaseAttribute(TransLandId);
    commentHistoryDialogPop.dialog("close");
}

function editTransactionDetails(id, landid) {
    edit = 1;
    $("#editflag").val(edit);
    editAttributeRegistration(id, landid);
    commentHistoryDialogPop.dialog("close");
}

function FillRegistrationPersonDataNew()
{
    // Init editing grid
    $("#registration_personsEditingGrid1").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: false,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: ResourcepersonsEditingControllerForPerson,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {name: "firstname", title: $.i18n("reg-firstname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middlename", title: $.i18n("reg-middlename"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-middle-name")}},
            {name: "lastname", title: $.i18n("reg-lastname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "address", title: $.i18n("reg-address"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-address")}},
            {name: "identityno", title: $.i18n("reg-id-number"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-idnumber")}},
            {name: "dateofbirth", title: $.i18n("reg-dob"), type: "date", width: 120, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "contactno", title: $.i18n("reg-mobile-num"), type: "text", width: 120},
            {name: "genderid", title: $.i18n("reg-gender"), align: "left", type: "select", items: [{id: 1, name: "Male"}, {id: 2, name: "Female"}], valueField: "id", textField: "name", width: 80, editing: false, filtering: false},
            {name: "laPartygroupIdentitytype.identitytypeid", title: $.i18n("reg-id-type"), type: "select", items: [{id: 1, name: "Voter ID"}, {id: 2, name: "Driving license"}, {id: 3, name: "Passport"}, {id: 4, name: "ID card"}, {id: 5, name: "Other"}, {id: 6, name: "None"}], valueField: "id", textField: "name", width: 80, editing: false, filtering: false},
            {name: "laPartygroupMaritalstatus.maritalstatusid", title: $.i18n("reg-marital-status"), type: "select", items: [{id: 1, name: "Single"}, {id: 2, name: "Married"}, {id: 3, name: "Divorced"}, {id: 4, name: "Widow"}, {id: 5, name: "Widower"}], valueField: "id", textField: "name", width: 80, editing: false, filtering: false},
            {name: "laPartygroupEducationlevel.educationlevelid", title: $.i18n("reg-edulevel"), type: "select", items: [{id: 1, name: "None"}, {id: 2, name: "Primary"}, {id: 3, name: "Secondary"}, {id: 4, name: "University"}], valueField: "id", textField: "name", width: 80, editing: false, filtering: false},
        ]
    });

    $("#registration_personsEditingGrid1 .jsgrid-table th:first-child :button").click();

    $("#registration_personsEditingGrid1").jsGrid("loadData");
}

var ResourcepersonsEditingControllerForPerson = {
    loadData: function (filter) {
        return $.ajax({
            type: "GET",
            url: "landrecords/personsDataNew/" + selectedlandid,
            data: filter,
            success: function (data)
            {

            }
        });
    },
    insertItem: function (item) {


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
            "mobileno": item.mobileno,
            "genderid": item.genderid,
            "identitytypeid": item.laPartygroupIdentitytype.identitytypeid,
            "maritalstatusid": item.laPartygroupMaritalstatus.maritalstatusid,
            "educationlevel": item.laPartygroupEducationlevel.educationlevelid,
            "transactionid": _transactionid,
            "landid": Personusin,
            "contactno": item.contactno
        }

        return $.ajax({
            type: "POST",
            //contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/updateNaturalPersonDataForEdit",
            //data: JSON.stringify(item),
            data: ajaxdata,
            success: function () {
                FillPersonDataNew();
//	            	 
            },
            error: function (request, textStatus, errorThrown) {
                jAlert(request.responseText);
            }
        });
    },
    deleteItem: function (item) {
        alert("removed");
        return false;
    }
};




function loadPOIEditing() {
    $("#registration_personsEditingGrid").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: false,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: ResourcepersonsEditingController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            // {name: "landid", title: "LandID", type: "number", width: 70, align: "left", editing: false, filtering: true},
            {name: "firstName", title: $.i18n("reg-firstname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middleName", title: $.i18n("reg-middlename"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-middle-name")}},
            {name: "lastName", title: $.i18n("reg-lastname"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "gender", title: $.i18n("reg-gender"), type: "select", items: [{id: 1, name: "Male"}, {id: 2, name: "Female"}, {id: 3, name: "Other"}], valueField: "id", textField: "name", width: 120, validate: {validator: "required", message: $.i18n("err-select-gender")}},
            {name: "dob", title: $.i18n("reg-dob"), type: "date", width: 120, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "relation", title: $.i18n("reg-relation"), type: "select", items: [{id: 1, name: "Spouse"}, {id: 2, name: "Son"}, {id: 3, name: "Daughter"}, {id: 4, name: "Grandson"}, {id: 5, name: "Granddaughter"}, {id: 6, name: "Brother"},
                    {id: 7, name: "Sister"}, {id: 8, name: "Father"}, {id: 9, name: "Mother"}, {id: 10, name: "Grandmother"}, {id: 11, name: "Grandfather"}, {id: 12, name: "Aunt"},
                    {id: 13, name: "Uncle"}, {id: 14, name: "Niece"}, {id: 15, name: "Nephew"}, {id: 16, name: "Other"}, {id: 17, name: "Other relatives"}, {id: 18, name: "Associate"},
                    {id: 19, name: "Parents and children"}, {id: 20, name: "Siblings"}], valueField: "id", textField: "name", width: 120, validate: {validator: "required", message: $.i18n("err-select-relation-type")}},
        ]
    });
    $("#registration_personsEditingGrid .jsgrid-table th:first-child :button").click();
    $("#registration_personsEditingGrid").jsGrid("loadData");
}

var ResourcepersonsEditingController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val($.i18n("gen-reload"));
        return $.ajax({
            type: "GET",
            url: "landrecords/personwithinterest/" + selectedlandid + "/" + transid,
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



function FillResourceNonNaturalPersonDataNew()
{
    // Init editing grid
    $("#registration_personsEditingGrid2").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: false,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: RegistrationNonNaturalPerson,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {name: "organizationname", title: $.i18n("reg-org-name"), type: "text", width: 120, validate: {validator: "required", message: $.i18n("err-enter-org-name")}},
            {name: "groupType.grouptypeid", title: $.i18n("reg-group-type"), type: "select", items: [{id: 1, name: "Civic"}, {id: 2, name: "Mosque"}, {id: 3, name: "Association"}, {id: 4, name: "Cooperative"}, {id: 5, name: "Informal"}], valueField: "id", textField: "name", width: 80, editing: false, filtering: false},
            {name: "contactno", title: $.i18n("reg-mobile-num"), type: "text", width: 120},
        ]
    });

    $("#registration_personsEditingGrid2 .jsgrid-table th:first-child :button").click();

    $("#registration_personsEditingGrid2").jsGrid("loadData");


}




var RegistrationNonNaturalPerson = {
    loadData: function (filter) {
        $("#btnLoadPersons").val($.i18n("gen-reload"));
        return $.ajax({
            type: "GET",
            url: "landrecords/NonNaturalpersonsDataNew/" + selectedlandid,
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



function  AddDocInfoSplit() {

    $("#split_selectedlandid").val(selectedlandid);
    jQuery.ajax({
        type: 'POST',
        url: "registration/savedocumentInfo/split",
        data: $("#editprocessAttributeformID").serialize(),
        async: false,
        success: function (data) {
            if (data) {
                jAlert($.i18n("reg-doc-added"), $.i18n("gen-info"));
                $("#doc_name_split").val("");
                $("#doc_date_split").val("");
                $("#doc_desc_split").val("");
                fetchDocumentSplit(selectedlandid);
            }
        }
    });
}

function AddDocInfoRegistration() {
    $("#landidhide").val(selectedlandid);
    $("#processidhide").val(processid);
    $("#editprocessAttributeformID").valid();
    var errors = "";

    // Validate
    if (processid == 1 || processid == 5 || processid == 10) {
        if ($("#leaseid").val() === "") {
            jAlert($.i18n("err-save-lessee-lender"), $.i18n("err-alert"));
            return;
        }
        if (processid == 5 && $("#surrenderLeaseId").val() === "") {
            //jAlert($.i18n("err-save-lessee-lender"), $.i18n("err-alert"));
            //return;
            saveattributesSurrenderLease();
        }
        if (doc_name_Lease.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-name") + "\n"
        }
        if (doc_date_Lease.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-date") + "\n"
        }
        if (doc_desc_Lease.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-desc") + "\n"
        }
    } else if (processid == 11 || processid == 12) {
        if (processid == 11 && $("#permissionId").val() === "") {
            jAlert($.i18n("err-add-applicant"), $.i18n("err-alert"));
            return;
        }
        if (processid == 12 && $("#surrenderPermissionId").val() === "") {
            jAlert($.i18n("err-save-applicant"), $.i18n("err-alert"));
            return;
        }
        if (perm_doc_name.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-name") + "\n"
        }
        if (perm_doc_date.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-date") + "\n"
        }
        if (perm_doc_desc.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-desc") + "\n"
        }
    } else if (processid == 2 || processid == 4 || processid == 6 || processid == 7) {
        if (arry_Buyerbyprocessid == null || arry_Buyerbyprocessid.length < 1) {
            jAlert($.i18n("err-add-new-owner"), $.i18n("err-alert"));
            return false;
        }
        if (doc_name_sale.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-name") + "\n"
        }
        if (doc_date_sale.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-date") + "\n"
        }
        if (doc_desc_sale.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-desc") + "\n"
        }
    } else if (processid == 3 || processid == 9) {
        if (doc_name_mortgage.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-name") + "\n"
        }
        if (doc_date_mortgage.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-date") + "\n"
        }
        if (doc_desc_mortgage.value.length == 0) {
            errors += "- " + $.i18n("err-enter-doc-desc") + "\n"
        }
    }

    if (errors.length > 0) {
        jAlert(errors, $.i18n("err-alert"));
        return false;
    }

    jQuery.ajax({
        type: 'POST',
        url: "registration/savedocumentInfo/Registration",
        data: $("#editprocessAttributeformID").serialize(),
        async: false,
        success: function (data) {
            if (data) {
                jAlert($.i18n("reg-doc-added"), $.i18n("gen-info"));
                if (processid == 11 || processid == 12) {
                    loadPermissionDocs();
                } else {
                    fetchDocument(selectedlandid, data, processid);
                    fetchDocEdit();
                }
                clearDocuments();
            }
        }
    });
}


function clearDocuments()
{
    $("#doc_name_sale").val('');
    $("#doc_date_sale").val('');
    $("#doc_desc_sale").val('');
    $("#doc_name_Lease").val('');
    $("#doc_date_Lease").val('');
    $("#doc_desc_Lease").val('');
    $("#doc_name_mortgage").val('');
    $("#doc_date_mortgage").val('');
    $("#doc_desc_mortgage").val('');
    $("#perm_doc_name").val('');
    $("#perm_doc_date").val('');
    $("#perm_doc_desc").val('');
}


function fetchDocument(landId, TypeId, processId)
{
    jQuery.ajax({
        type: 'GET',
        url: "registryrecords/getprocessDocument/" + landId + "/" + TypeId + "/" + processId,
        async: false,
        success: function (data) {
            if (data != null && data != "")
            {
                if (processId == 1 || processId == 5 || processId == 10)
                {
                    if (edit == 0) {
                        $("#LeaseDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#LeaseDocRowData");
                    } else if (edit == 1) {
                        $("#LeaseDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#LeaseDocRowData");

                    }
                    $("#LeaseDocRowData").i18n();
                }

                if (processId == 2 || processId == 4 || processId == 6 || processId == 7)
                {

                    if (edit == 0) {
                        $("#salesDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#salesDocRowData");
                    } else if (edit == 1) {
                        $("#salesDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#salesDocRowData");

                    }
                    $("#salesDocRowData").i18n();
                }

                if (processId == 3 || processId == 9)
                {

                    if (edit == 0) {
                        $("#MortagageDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#MortagageDocRowData");
                    } else if (edit == 1) {
                        $("#MortagageDocRowData").empty();
                        $("#salesdocumentTemplate_add").tmpl(data).appendTo("#MortagageDocRowData");

                    }
                    $("#MortagageDocRowData").i18n();
                }

            } else {

                if (processId == 3 || processId == 9)
                {
                    $("#MortagageDocRowData").empty();
                }
                if (processId == 2 || processId == 4 || processId == 6 || processId == 7)
                {
                    $("#salesDocRowData").empty();
                }
                if (processId == 1 || processId == 5 || processId == 10)
                {
                    $("#LeaseDocRowData").empty();
                }

            }

        }
    });
}

function fetchDocumentSplit(landId) {
    jQuery.ajax({
        type: 'GET',
        url: "registryrecords/getsplitDocument/" + landId,
        async: false,
        success: function (data) {
            if (data != null) {
                $("#splitDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#splitDocRowData");
                $("#splitDocRowData").i18n();
            } else {
                $("#splitDocRowData").empty();
            }
        }
    });
}

function permissionTabClick(tabHeaderId, tabId) {
    $("#comment_Save").hide();
    $("#comment_Next").hide();

    $("#tabPermCurrentOwner").hide();
    $("#tabPermLandInfo").hide();
    $("#tabPermApplicant").hide();
    $("#tabPermDocs").hide();
    $("#tabPermDetails").hide();

    $("#liPermCurrentOwner").removeClass("ui-tabs-active");
    $("#liPermLandInfo").removeClass("ui-tabs-active");
    $("#liPermApplicant").removeClass("ui-tabs-active");
    $("#liPermDocs").removeClass("ui-tabs-active");
    $("#liPermDetails").removeClass("ui-tabs-active");

    $("#liPermCurrentOwner").removeClass("ui-state-active");
    $("#liPermLandInfo").removeClass("ui-state-active");
    $("#liPermApplicant").removeClass("ui-state-active");
    $("#liPermDocs").removeClass("ui-state-active");
    $("#liPermDetails").removeClass("ui-state-active");

    $("#" + tabId).show();
    $("#" + tabHeaderId).addClass("ui-tabs-active");
    $("#" + tabHeaderId).addClass("ui-state-active");

    if (tabHeaderId === "liPermDetails") {
        $("#comment_Save").show();
    } else {
        $("#comment_Next").show();
    }
}

function LeaseFormTabClick(tabHeaderId, tabId)
{
    $("#comment_Save").hide();
    $("#comment_Next").hide();

    $("#Owner_Details").hide();
    $("#Land_Details_lease").hide();
    $("#Applicant_Details").hide();
    $("#Lease_Details").hide();
    $("#Upload_Documents_Lease").hide();
    $("#regLeasePoi").hide();

    $("#selectedLeasepoi").removeClass("ui-tabs-active");
    $("#selectedLeasepoi").removeClass("ui-state-active");
    $("#selectedApplicant").removeClass("ui-tabs-active");
    $("#selectedApplicant").removeClass("ui-state-active");
    $("#selectedowner").removeClass("ui-tabs-active");
    $("#selectedowner").removeClass("ui-state-active");
    $("#selecteddocs").removeClass("ui-tabs-active");
    $("#selecteddocs").removeClass("ui-state-active");
    $("#selectedsleasedetails").removeClass("ui-tabs-active");
    $("#selectedsleasedetails").removeClass("ui-state-active");
    $("#selectedLanddetails").removeClass("ui-tabs-active");
    $("#selectedLanddetails").removeClass("ui-state-active");

    $("#" + tabId).show();
    $("#" + tabHeaderId).addClass("ui-tabs-active");
    $("#" + tabHeaderId).addClass("ui-state-active");

    if (tabHeaderId === "selectedsleasedetails") {
        $("#comment_Save").show();
    } else {
        $("#comment_Next").show();
    }
}

function saveattributesLeaseDetails() {


    if ($("#editprocessAttributeformID").valid()) {



        if ((leaseMonths.value.length == 0)
                || (lease_Amount.value.length == 0)) {
            jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
            return false;
        }



        jQuery
                .ajax({
                    type: "POST",
                    url: "registration/updateLeaseeDetails",
                    data: $("#editprocessAttributeformID")
                            .serialize(),
                    success: function (result) {

                        $('#leaseeperson').val(0);
                        $("#Lease_Details").hide();
                        $("#regLeasePoi").hide();
                        $("#Upload_Documents_Lease").show();


                    },
                    error: function (XMLHttpRequest,
                            textStatus, errorThrown) {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                });

    } else {

        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}




function saveMortgage() {

    if ($("#editprocessAttributeformID").valid()) {

        if ((mortgage_from.value.length == 0)
                || (mortgage_to.value.length == 0)
                || (amount_mortgage.value.length == 0)) {
            jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
            return false;
        } else if ((mortgage_Financial_Agencies.value == "0")) {
            jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
            return false;
        }
        jQuery
                .ajax({
                    type: "POST",
                    url: "registration/savemortgagedata",
                    data: $("#editprocessAttributeformID")
                            .serialize(),
                    success: function (result) {
                        if (result != null && result != undefined) {
//											
                        } else {
                            jAlert($.i18n("err-request-not-completed"));
                        }
                    },
                    error: function (XMLHttpRequest,
                            textStatus, errorThrown) {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                });


    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}


function MortgageTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#MortgageOwner_Details").hide();
    $("#Land_Details_Mortgage").hide();
    $("#Mortgage_Details").show();
    $("#Upload_Document_Mortgage").hide();

    $("#selectemortgage").addClass("ui-tabs-active");
    $("#selectemortgage").addClass("ui-state-active");
    $("#selectedownerdetails").removeClass("ui-tabs-active");
    $("#selectedownerdetails").removeClass("ui-state-active");
    $("#selectelandmortgage").removeClass("ui-tabs-active");
    $("#selectelandmortgage").removeClass("ui-state-active");
    $("#selectemortgagedocs").removeClass("ui-tabs-active");
    $("#selectemortgagedocs").removeClass("ui-state-active");
}



function MortgagelanddetailsTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#MortgageOwner_Details").hide();
    $("#Land_Details_Mortgage").show();
    $("#Mortgage_Details").hide();
    $("#Upload_Document_Mortgage").hide();

    $("#selectemortgage").removeClass("ui-tabs-active");
    $("#selectemortgage").removeClass("ui-state-active");
    $("#selectedownerdetails").removeClass("ui-tabs-active");
    $("#selectedownerdetails").removeClass("ui-state-active");
    $("#selectelandmortgage").addClass("ui-tabs-active");
    $("#selectelandmortgage").addClass("ui-state-active");
    $("#selectemortgagedocs").removeClass("ui-tabs-active");
    $("#selectemortgagedocs").removeClass("ui-state-active");
}

function MortgageOwnerTabClick()
{
    $("#comment_Save").hide();
    $("#comment_Next").show();

    $("#MortgageOwner_Details").show();
    $("#Land_Details_Mortgage").hide();
    $("#Mortgage_Details").hide();
    $("#Upload_Document_Mortgage").hide();

    $("#selectemortgage").removeClass("ui-tabs-active");
    $("#selectemortgage").removeClass("ui-state-active");
    $("#selectedownerdetails").addClass("ui-tabs-active");
    $("#selectedownerdetails").addClass("ui-state-active");
    $("#selectelandmortgage").removeClass("ui-tabs-active");
    $("#selectelandmortgage").removeClass("ui-state-active");
    $("#selectemortgagedocs").removeClass("ui-tabs-active");
    $("#selectemortgagedocs").removeClass("ui-state-active");
}

function MortgageDocsTabClick()
{
    $("#comment_Save").show();
    $("#comment_Next").hide();

    $("#MortgageOwner_Details").hide();
    $("#Land_Details_Mortgage").hide();
    $("#Mortgage_Details").hide();
    $("#Upload_Document_Mortgage").show();

    $("#selectemortgage").removeClass("ui-tabs-active");
    $("#selectemortgage").removeClass("ui-state-active");
    $("#selectedownerdetails").removeClass("ui-tabs-active");
    $("#selectedownerdetails").removeClass("ui-state-active");
    $("#selectelandmortgage").removeClass("ui-tabs-active");
    $("#selectelandmortgage").removeClass("ui-state-active");
    $("#selectemortgagedocs").addClass("ui-tabs-active");
    $("#selectemortgagedocs").addClass("ui-state-active");
}

function uploadMediaFile_sales(id) {
    var flag = false;
    var val1 = 0;
    var formData = new FormData();
    var appid = '#' + "mediafileUpload" + id;
    var file = $("" + appid + "")[0].files[0];

    if (typeof (file) === "undefined") {
        jAlert($.i18n("err-select-file-to-upload"), $.i18n("gen-info"));
    } else {
        $.each($("" + appid + "")[0].files,
                function (ind2, obj2) {
                    $.each($("" + appid + "")[0].files[val1],
                            function (ind3, obj3) {
                                if (ind3 == "type") {
                                    if (obj3 == "application/x-msdownload") {
                                        flag = false;
                                        jAlert($.i18n("err-file-mustbe-exe"));
                                    } else {
                                        flag = true;
                                    }
                                }
                            });
                    val1 = val1 + 1;
                });

        if (flag) {
            formData.append("spatialdata", file);
            formData.append("documentId", id);
            $.ajax({
                url: 'upload/media_sale/',
                type: 'POST',
                data: formData,
                mimeType: "multipart/form-data",
                contentType: false,
                cache: false,
                processData: false,
                success: function (data, textStatus, jqXHR)
                {
                    if (edit == 1)
                    {
                        documentEditFetch();
                        jAlert($.i18n("reg-file-uploaded"), $.i18n("gen-info"));
                        $('#fileUploadSpatial').val('');
                        $('#alias').val('');
                    } else {
                        fetchDocument(parseInt(data), persontypeid, processid);
                        jAlert($.i18n("reg-file-uploaded"), $.i18n("gen-info"));
                        $('#fileUploadSpatial').val('');
                        $('#alias').val('');
                    }
                }

            });
        }
    }
}

function viewMultimediaByTransid_sales(id) {

    var flag = false;
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: "registration/mediaavail/" + id,
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
        window.open("registration/download/" + id, 'popUp', 'height=500,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,titlebar=no,menubar=no,status=no,replace=false');
    } else {

        jAlert($.i18n("err-file-not-found"), $.i18n("gen-info"));
    }
}

function deleteMediaFile_sales(id) {
    var landid = 0;
    var formData = new FormData();
    formData.append("documentId", id);

    $.ajax({
        url: 'delete/media_sale/',
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data, textStatus, jqXHR)
        {
            landid = data;
            if (processid == 11 || processid == 12) {
                loadPermissionDocs();
            } else {
                fetchDocument(parseInt(data), persontypeid, processid);
                fetchDocumentSplit(landid);
            }

            jAlert($.i18n("reg-file-deleted"));
        }

    });
}

function saveattributesSurrenderMortagage() {
    if ($("#editprocessAttributeformID").valid()) {
        jQuery
                .ajax({
                    type: "POST",
                    url: "registration/savesurrenderMortgagedata",
                    data: $("#editprocessAttributeformID")
                            .serialize(),
                    success: function (result) {
                        if (result != null && result != undefined) {
                            $("#Lease_Details").hide();
                            $("#Upload_Documents_Lease").show();

                        } else {
                            jAlert($.i18n("err-request-not-completed"));
                        }
                    },
                    error: function (XMLHttpRequest,
                            textStatus, errorThrown) {
                        jAlert($.i18n("err-request-not-completed"));
                    }
                });


    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}

function approveSurrenderMortagage() {
    if ($("#editprocessAttributeformID").valid()) {

        if ($("#mortgagesurrender_reason").val() == "") {
            jAlert($.i18n("err-fill-surender-reason"), $.i18n("err-alert"));
            return false;
        }

        jConfirm(
                $.i18n("gen-save-confirmation"),
                $.i18n("gen-confirmation"),
                function (response) {
                    if (response) {
                        jQuery
                                .ajax({
                                    type: "POST",
                                    url: "registration/approveSurenderMortgageData",
                                    data: $("#editprocessAttributeformID")
                                            .serialize(),
                                    success: function (result) {
                                        if (result != null && result != undefined) {
                                            landRecordsInitialized_R = false;
                                            displayRefreshedRegistryRecords_ABC();
                                            //RegistrationRecords("registrationRecords");
                                            attributeEditDialog.dialog("close");

                                        } else {
                                            jAlert($.i18n("err-request-not-completed"));
                                        }
                                    },
                                    error: function (XMLHttpRequest,
                                            textStatus, errorThrown) {
                                        jAlert($.i18n("err-request-not-completed"));
                                    }
                                });
                    }

                });

    } else {
        jAlert($.i18n("err-fill-details"), $.i18n("err-alert"));
    }
}


function editNewBuyer(id) {
    if (arry_Buyerbyprocessid.length > 0) {
        for (var i = 0; i < arry_Buyerbyprocessid.length; i++) {
            if (id == arry_Buyerbyprocessid[i].personid) {
                $("#firstname_r_sale1").val(arry_Buyerbyprocessid[i].firstname);
                $("#middlename_r_sale1").val(arry_Buyerbyprocessid[i].middlename);
                $("#lastname_r_sale1").val(arry_Buyerbyprocessid[i].lastname);
                $("#id_r1").val(arry_Buyerbyprocessid[i].identityno);
                $("#address_sale1").val(arry_Buyerbyprocessid[i].address);
                $("#date_Of_birth_sale1").val(arry_Buyerbyprocessid[i].dob);
                $("#sale_gender_buyer").val(arry_Buyerbyprocessid[i].genderid);
                $("#sale_marital_buyer").val(arry_Buyerbyprocessid[i].maritalstatusid);
                $("#personid").val(arry_Buyerbyprocessid[i].personid);

                $("#id_date_sale1").val(arry_Buyerbyprocessid[i].idDate);
                $("#place_of_birth_sale1").val(arry_Buyerbyprocessid[i].birthplace);
                $("#profession_sale1").val(arry_Buyerbyprocessid[i].profession);
                $("#father_name_sale1").val(arry_Buyerbyprocessid[i].fathername);
                $("#mother_name_sale1").val(arry_Buyerbyprocessid[i].mothername);
                $("#nop_sale1").val(arry_Buyerbyprocessid[i].nopId);
                $("#mandate_date_sale1").val(arry_Buyerbyprocessid[i].mandateDate);
                $("#mandate_loc_sale1").val(arry_Buyerbyprocessid[i].mandateLocation);

                $("#savebuyer").text($.i18n("gen-save"));
                $("#savebuyer").show();
            }
        }
    }
}

function editLegalBuyer(id) {
    if (arry_Buyerbyprocessid.length > 0) {
        for (var i = 0; i < arry_Buyerbyprocessid.length; i++) {
            if (id == arry_Buyerbyprocessid[i].partyid) {
                $("#cbxLegalBuyerType").val(arry_Buyerbyprocessid[i].groupType.grouptypeid);
                $("#txtLegalBuyerName").val(arry_Buyerbyprocessid[i].organizationname);
                $("#txtLegalBuyerRegNum").val(arry_Buyerbyprocessid[i].identityregistrationno);
                $("#txtLegalBuyerEstDate").val(arry_Buyerbyprocessid[i].regdate);
                $("#txtLegalBuyerAddress").val(arry_Buyerbyprocessid[i].address);
                $("#txtLegalBuyerRepName").val(arry_Buyerbyprocessid[i].repname);
                $("#txtLegalBuyerRepPhone").val(arry_Buyerbyprocessid[i].contactno);

                $("#personid").val(arry_Buyerbyprocessid[i].partyid);

                $("#savebuyer").text($.i18n("gen-save"));
                $("#savebuyer").show();
            }
        }
    }
}

function editNewLeasee(id) {
    if (leaseData !== null) {
        if (id == leaseData.personid) {

            editlease = true;
            var person = leaseData.borrower;
            removeNulls(person);

            $("#leaseeperson").val(leaseData.personid);
            $("#firstname_r_lease").val(person.firstname);
            $("#middlename_r_lease").val(person.middlename);
            $("#lastname_r_lease").val(person.lastname);
            $("#id_lease").val(person.identityno);
            $("#id_date_lease").val(person.idDate);
            $("#address_lease").val(person.address);
            $("#date_Of_birth_lease").val(person.dob);
            $("#place_of_birth_lease").val(person.birthplace);
            $("#marital_lease").val(person.maritalstatusid);
            $("#gender_lease").val(person.genderid);
            $("#profession_lease").val(person.profession);
            $("#father_name_lease").val(person.fathername);
            $("#mother_name_lease").val(person.mothername);
            $("#nop_lease").val(person.nopId);
            $("#mandate_date_lease").val(person.mandateDate);
            $("#mandate_loc_lease").val(person.mandateLocation);

            $("#lease_Amount").val(leaseData.leaseamount);
            $("#leaseMonths").val(leaseData.months);
            $("#leaseYears").val(leaseData.years);
            $("#leaseStartDate").val(leaseData.leasestartdate);
            $("#leaseEndDate").val(leaseData.leaseenddate);


            $("#saveleasee").show();
        }
    }
}

function editSeller(id) {
    if (arry_Sellerbyprocessid.length > 0) {
        for (var i = 0; i < arry_Sellerbyprocessid.length; i++) {

            if (id == arry_Sellerbyprocessid[i].personid) {


                $("#firstname_r_sale1").val(arry_Sellerbyprocessid[i].firstname);
                $("#middlename_r_sale1").val(arry_Sellerbyprocessid[i].middlename);
                $("#lastname_r_sale1").val(arry_Sellerbyprocessid[i].lastname);
                $("#id_r1").val(arry_Sellerbyprocessid[i].identityno);
                $("#contact_no1").val(arry_Sellerbyprocessid[i].contactno);
                $("#address_sale1").val(arry_Sellerbyprocessid[i].address);
                $("#date_Of_birth_sale1").val(arry_Sellerbyprocessid[i].dob);

                $("#sale_gender_buyer").val(arry_Sellerbyprocessid[i].genderid);
                $("#sale_idtype_buyer").val(arry_Sellerbyprocessid[i].identitytypeid);
                $("#sale_marital_buyer").val(arry_Sellerbyprocessid[i].maritalstatusid);
                $("#cbxLegalBuyerType").val(arry_Sellerbyprocessid[i].groupType.grouptypeid);
                $("#personid").val(arry_Sellerbyprocessid[i].personid);
            }
        }
    }
}

function fillBuyerandSellerpage(landid) {
    finallandid = landid;
    $("#personid").val(0);

    if (natureOfPowers === null) {
        jQuery.ajax({
            url: "landrecords/powernature/",
            async: false,
            success: function (data) {
                natureOfPowers = data;
            }
        });
    }

    var arry_Seller = [];
    var arry_Buyer = [];
    jQuery.ajax({
        url: "registration/partydetails/filldetails/" + landid + "/" + firstselectedprocess,
        async: false,
        success: function (objdata) {
            jQuery.each(objdata, function (i, obj)
            {
                if (obj.persontype == 1)  // sler
                {
                    arry_Seller.push(obj);
                } else if (obj.persontype == 11)  //buyer
                {
                    arry_Buyer.push(obj);
                }
            });

            if (arry_Seller != null && arry_Seller.length == 1)
            {
                data = arry_Seller[0];
                (data);

                $("#firstname_r_sale").val(data.firstname);
                $("#middlename_r_sale").val(data.middlename);
                $("#lastname_r_sale").val(data.lastname);
                $("#id_r").val(data.identityno);
                $("#contact_no_sale").val(data.contactno);
                $("#address_sale").val(data.address);
                $("#date_Of_birth_sale").val(data.dob);

                $("#sale_martial_status").empty();
                $("#sale_gender").empty();
                $("#sale_id_type").empty();

                jQuery.each(maritalStatus_R, function (i, obj) {
                    $("#sale_martial_status").append(
                            $("<option></option>").attr("value",
                            obj.maritalstatusid).text(
                            obj.maritalstatusEn));
                });

                $("#sale_martial_status").val(data.maritalstatusid);

                jQuery.each(genderStatus_R, function (i, obj1) {
                    $("#sale_gender").append(
                            $("<option></option>").attr("value",
                            obj1.genderId).text(obj1.gender_en));
                });

                $("#sale_gender").val(data.genderid);

                jQuery.each(idtype_R, function (i, obj1) {
                    $("#sale_id_type").append(
                            $("<option></option>").attr("value",
                            obj1.identitytypeid).text(
                            obj1.identitytypeEn));
                });
                $("#sale_id_type").val(data.identitytypeid);
            }

            if (arry_Seller != null && arry_Seller.length > 1)
            {
                data = arry_Seller[0];
                (data);

                $("#firstname_r_sale").val(data.firstname);
                $("#middlename_r_sale").val(data.middlename);
                $("#lastname_r_sale").val(data.lastname);
                $("#id_r").val(data.identityno);
                $("#contact_no_sale").val(data.contactno);
                $("#address_sale").val(data.address);
                $("#date_Of_birth_sale").val(data.dob);

                $("#sale_martial_status").empty();
                $("#sale_gender").empty();
                $("#sale_id_type").empty();

                jQuery.each(maritalStatus_R, function (i, obj) {
                    $("#sale_martial_status").append(
                            $("<option></option>").attr("value",
                            obj.maritalstatusid).text(
                            obj.maritalstatusEn));
                });

                $("#sale_martial_status").val(data.maritalstatusid);

                jQuery.each(genderStatus_R, function (i, obj1) {
                    $("#sale_gender").append(
                            $("<option></option>").attr("value",
                            obj1.genderId).text(obj1.gender_en));
                });

                $("#sale_gender").val(data.genderid);

                jQuery.each(idtype_R, function (i, obj1) {
                    $("#sale_id_type").append(
                            $("<option></option>").attr("value",
                            obj1.identitytypeid).text(
                            obj1.identitytypeEn));
                });
                $("#sale_id_type").val(data.identitytypeid);

                if (arry_Seller[1] != null)
                {
                    data1 = arry_Seller[1];
                    $("#firstname_r_sale1second").val(data1.firstname);
                    $("#middlename_r_sale1second").val(data1.middlename);
                    $("#lastname_r_sale1second").val(data1.lastname);
                    $("#id_r1second").val(data1.identityno);
                    $("#contact_no_sale1second").val(data1.contactno);
                    $("#address_sale1second").val(data1.address);
                    $("#date_Of_birth_sale1second").val(data1.dob);

                    $("#sale_martial_status1second").empty();
                    $("#sale_gender1second").empty();
                    $("#sale_id_type1second").empty();

                    jQuery.each(maritalStatus_R, function (i, obj) {
                        $("#sale_martial_status1second").append(
                                $("<option></option>").attr("value",
                                obj.maritalstatusid).text(
                                obj.maritalstatusEn));
                    });

                    $("#sale_martial_status1second").val(data.maritalstatusid);

                    jQuery.each(genderStatus_R, function (i, obj1) {
                        $("#sale_gender1second").append(
                                $("<option></option>").attr("value",
                                obj1.genderId).text(obj1.gender_en));
                    });

                    $("#sale_gender1second").val(data.genderid);

                    jQuery.each(idtype_R, function (i, obj1) {
                        $("#sale_id_type1second").append(
                                $("<option></option>").attr("value",
                                obj1.identitytypeid).text(
                                obj1.identitytypeEn));
                    });
                    $("#sale_id_type1second").val(data.identitytypeid);

                }

                $("#Owner_Elimanated").empty();
                $("#Owner_Elimanated").append($("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

                jQuery.each(objdata, function (i, obj1)
                {
                    $("#Owner_Elimanated").append($("<option></option>").attr("value", obj1.personid).text(obj1.firstname));
                });

                $("#SecondOwner").css("display", "block");
            }

            if (arry_Seller != null && arry_Seller.length == 1)
            {
                $("#Owner_Elimanated").empty();
                $("#Owner_Elimanated").append($("<option></option>").attr("value", "").text($.i18n("gen-please-select")));
                jQuery.each(arry_Seller, function (i, obj1)
                {
                    $("#Owner_Elimanated").append($("<option></option>").attr("value", obj1.personid).text(obj1.firstname));
                });
                $("#Owner_Elimanated").val(objdata[0].personid);
            }

            // Buyer

            $("#sale_marital_buyer").empty();
            $("#sale_gender_buyer").empty();
            $("#sale_idtype_buyer").empty();
            $("#nop_sale1").empty();
            $("#doc_Type_Sale").empty();
            $("#cbxLegalBuyerType").empty();

            $("#sale_marital_buyer").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#cbxLegalBuyerType").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#sale_gender_buyer").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#nop_sale1").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#doc_Type_Sale").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

            jQuery.each(maritalStatus_R, function (i, obj1) {
                var displayName = obj1.maritalstatus;
                if (Global.LANG === "en") {
                    displayName = obj1.maritalstatusEn;
                }
                $("#sale_marital_buyer").append($("<option></option>").attr("value", obj1.maritalstatusid).text(displayName));
            });
            $.each(leTypes, function (i, t) {
                var displayName = t.grouptype;
                if (Global.LANG === "en") {
                    displayName = t.grouptypeEn;
                }
                $("#cbxLegalBuyerType").append($("<option></option>").attr("value", t.grouptypeid).text(displayName));
            });
            jQuery.each(genderStatus_R, function (i, obj1) {
                var displayName = obj1.gender;
                if (Global.LANG === "en") {
                    displayName = obj1.gender_en;
                }
                $("#sale_gender_buyer").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
            });
            jQuery.each(natureOfPowers, function (i, obj1) {
                var displayName = obj1.name;
                if (Global.LANG === "en") {
                    displayName = obj1.nameEn;
                }
                $("#nop_sale1").append($("<option></option>").attr("value", obj1.id).text(displayName));
            });

            jQuery.each(documenttype_R, function (i, obj) {
                $("#doc_Type_Sale").append(
                        $("<option></option>").attr("value",
                        obj.code).text(obj.nameOtherLang));
            });

            if (arry_Buyer !== null && arry_Buyer.length > 0)
            {
                databuyer = arry_Buyer[0];
            }
        }
    });
}

function fillLandDetails() {
    if (tenuretypeList === null) {
        jQuery.ajax({
            url: "landrecords/tenuretype/",
            async: false,
            success: function (data) {
                tenuretypeList = data;
            }
        });
    }

    if (tenureclassList === null) {
        jQuery.ajax({
            url: "landrecords/tenureclass/",
            async: false,
            success: function (data) {
                tenureclassList = data;
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

    if (landUserList === null) {
        jQuery.ajax({
            url: "landrecords/landusertype/",
            async: false,
            success: function (data) {
                landUserList = data;
            }
        });
    }

    $("#tran_primary").val(suReg.landid);
    $("#tran_tenure_type").empty();
    $("#tran_cbxAppNature").empty();

    jQuery.each(tenuretypeList, function (i, tenureobj) {
        var tenureDisplayName = tenureobj.landsharetype;
        if (Global.LANG === "en") {
            tenureDisplayName = tenureobj.landsharetypeEn;
        }
        $("#tran_tenure_type").append($("<option></option>").attr("value", tenureobj.landsharetypeid).text(tenureDisplayName));
    });

    if (appNatures !== null) {
        jQuery.each(appNatures, function (i, obj) {
            var displayName = obj.name;
            if (Global.LANG === "en") {
                displayName = obj.nameEn;
            }
            $("#tran_cbxAppNature").append($("<option></option>").attr("value", obj.id).text(displayName));
        });
    }

    $("#tran_other_use").val("");
    $("#tran_txtRegNumber").val("");
    $("#tran_txtParcelNumber").val("");
    $("#tran_txtReconRightDate").val("");
    $("#tran_txtContraDate").val("");
    $("#tran_txtAppIssueDate").val(suReg.applicationdate);
    $("#tran_neighbor_north").val(suReg.neighborNorth);
    $("#tran_neighbor_south").val(suReg.neighborSouth);
    $("#tran_neighbor_east").val(suReg.neighborEast);
    $("#tran_neighbor_west").val(suReg.neighborWest);
    $("#tran_existing_use").empty();

    if (suReg.other_use !== "") {
        $("#tran_other_use").val(suReg.other_use);
    }

    $("#tran_area").val(((suReg.area) * area_constant).toFixed(2));
    $("#tran_lblApfrNum").text("--");

    jQuery.each(landUserList, function (i, landuseobj) {
        var displayName = landuseobj.landusetype;
        if (Global.LANG === "en") {
            displayName = landuseobj.landusetypeEn;
        }
        $("#tran_existing_use").append($("<option></option>").attr("value", landuseobj.landusetypeid).text(displayName));
    });

    if (suReg.noaId !== null) {
        $("#tran_cbxAppNature").val(suReg.noaId);
    }

    if (suReg.rights !== null && suReg.rights.length > 0) {
        $.each(suReg.rights, function (i, landRight) {
            if (landRight.isactive && landRight.persontypeId === 1) {
                jQuery("#tran_tenure_type").val(landRight.shareTypeId);
            }
        });
    }

    if (suReg.registrationNum !== null) {
        $("#tran_txtRegNumber").val(suReg.registrationNum);
    }

    if (suReg.parcelExtensions !== null && suReg.parcelExtensions.length > 0) {
        var parcelExtension = suReg.parcelExtensions[0];
        if (parcelExtension.parcelno !== null) {
            $("#tran_txtParcelNumber").val(parcelExtension.parcelno);
        }
        if (parcelExtension.recognition_rights_date !== null) {
            $("#tran_txtReconRightDate").val(parcelExtension.recognition_rights_date);
        }
        if (parcelExtension.parcelno !== null) {
            $("#tran_txtContraDate").val(parcelExtension.contradictory_date);
        }
    }

    if (suReg.landusetypeid !== null) {
        $("#tran_existing_use").val(suReg.landusetypeid.split(","));
    }

    $('#tran_existing_use').multiselect({
        columns: 1,
        placeholder: $.i18n("gen-please-select")
    });

    $('#tran_existing_use').multiselect('reload');

    $("#tran_spatialid").text(_parcelNumber);

    $("#tran_claimType").text(getDisplayName(claimTypes, suReg.claimtypeid, "code", "name", "nameOtherLang"));
    $("#tran_lblAppNum").text("--");
    $("#tran_lblPvNum").text("--");

    if (!isEmpty(suReg.appNum)) {
        $("#tran_lblAppNum").text(suReg.appNum);
    }
    if (!isEmpty(suReg.pvNum)) {
        $("#tran_lblPvNum").text(suReg.pvNum);
    }

    $("#pnlLandDetails input").prop("disabled", true);
    $("#pnlLandDetails select").prop("disabled", true);
    $(".ms-options-wrap button").prop("disabled", true);
}

function SaleOwnerdBuyeretails(landid) {
    var arry_Seller = [];
    var arry_Buyer = [];
    var URL = "";
    if (edit == 0) {
        URL = "registration/partydetails/sale/" + landid;
    } else if (edit == 1) {
        URL = "registration/editpartydetails/" + finaltransid;
    }

    jQuery.ajax({
        url: URL,
        async: false,
        success: function (objdata) {
            jQuery.each(objdata, function (i, obj)
            {
                removeNulls(obj);
                if (obj.persontype == 11)  //buyer
                {
                    arry_Buyer.push(obj);
                } else {
                    arry_Seller.push(obj);
                }
            });
            arry_Sellerbyprocessid = arry_Seller;
            arry_Buyerbyprocessid = arry_Buyer;

            $("#OwnerRecordsRowDataSale").empty();
            $("#RowLegalOwnerLoanInfo").empty();
            $("#OwnerRecordsRowDataLease").empty();
            $("#OwnerRecordsRowDataMortgage").empty();

            $("#newOwnerRecordsRowDataSale").empty();
            $("#RowLegalBuyerInfo").empty();
            $("#rowsPermissionApplicant").empty();
            $("#RowLegalSellerInfo").empty();
            $("#OwnerRecordsRowDataSaleEdit").empty();

            $("#rowsPermissionOwner").empty();
            $("#RowLegalOwnerPermInfo").empty();
            $("#liPermCurrentOwner").show();
            $("#tabPermCurrentOwner").show();

            if (edit === 0) {
                $("#SellerEdit").hide();
                $("#newOwner").show();
                $("#RegpersonsEditingGrid").show();
                $("#editRegpersonsEditingGrid").hide();
                $("#RegLeasepersonsEditingGrid").show();
                $("#editRegpersonsEditingGridLeasee").hide();

                $("#salesDocRowData").empty();
                $("#MortagageDocRowData").empty();
                $("#LeaseDocRowData").empty();
                $("#editflag").val(edit);

                $("#Seller_Details").show();
                $("#Land_Details_Sale").show();
                $("#Owner_Details").show();
                $("#Land_Details_lease").show();
                $("#MortgageOwner_Details").show();
                $("#Land_Details_Mortgage").show();
                $("#selectedseller").show();
                $("#selectedland").show();
                $("#selectedowner").show();
                $("#selectedLanddetails").show();
                $("#selectedownerdetails").show();
                $("#selectelandmortgage").show();
            }
            if (edit == 1) {
                $("#SellerEdit").show();
                $("#newOwner").hide();
                $("#RegpersonsEditingGrid").hide();
                $("#editRegpersonsEditingGrid").show();
                $("#editflag").val(edit);

                if (process_id == 2 || process_id == 4 || process_id == 6 || process_id == 7) {
                    $("#Seller_Details").hide();
                    $("#Land_Details_Sale").hide();
                    $("#Buyer_Details").show();
                    $("#selectedseller").hide();
                    $("#selectedland").hide();
                } else if (process_id == 1 || process_id == 5 || process_id == 10) {
                    $("#Owner_Details").hide();
                    $("#Land_Details_lease").hide();
                    $("#selectedowner").hide();
                    $("#selectedLanddetails").hide();
                    $("#Applicant_Details").show();
                } else if (process_id == 3 || process_id == 9) {
                    $("#MortgageOwner_Details").hide();
                    $("#Land_Details_Mortgage").hide();
                    $("#Mortgage_Details").show();
                    $("#selectedownerdetails").hide();
                    $("#selectelandmortgage").hide();
                }
            }

            if (arry_Seller !== null && arry_Seller.length > 0) {
                for (var i = 0; i < arry_Seller.length; i++) {
                    if (arry_Seller[i].hasOwnProperty("firstname")) {
                        // Private person
                        $("#tblPrivateSellerInfo").show();
                        $("#divPrivateSellerPoi").show();
                        $("#tblPrivateOwnerLoanInfo").show();
                        $("#pnlPoiLoan").show();
                        $("#tblPrivateOwnerPermInfo").show();
                        $("#pnlPermPOI").show();
                        $("#tblLegalSellerInfo").hide();
                        $("#tblLegalOwnerLoanInfo").hide();
                        $("#tblLegalOwnerPermInfo").hide();

                        $("#OwnerRecordsAttrTemplateSale").tmpl(arry_Seller[i]).appendTo("#OwnerRecordsRowDataSale");
                        $("#OwnerRecordsAttrTemplateLease").tmpl(arry_Seller[i]).appendTo("#OwnerRecordsRowDataLease");
                        $("#OwnerRecordsAttrTemplateMortgage").tmpl(arry_Seller[i]).appendTo("#OwnerRecordsRowDataMortgage");

                        $("#OwnerRecordsAttrTemplateSaleEdit").tmpl(arry_Seller[i]).appendTo("#OwnerRecordsRowDataSaleEdit");
                        $("#rowPermissionOwner").tmpl(arry_Seller[i]).appendTo("#rowsPermissionOwner");
                    } else {
                        // Legal entity
                        $("#tblPrivateSellerInfo").hide();
                        $("#divPrivateSellerPoi").hide();
                        $("#tblPrivateOwnerLoanInfo").hide();
                        $("#pnlPoiLoan").hide();
                        $("#tblPrivateOwnerPermInfo").hide();
                        $("#pnlPermPOI").hide();
                        $("#tblLegalSellerInfo").show();
                        $("#tblLegalOwnerLoanInfo").show();
                        $("#tblLegalOwnerPermInfo").show();

                        $("#LegalEntityTableTemplate").tmpl(arry_Seller[i]).appendTo("#RowLegalSellerInfo");
                        $("#LegalEntityTableTemplate").tmpl(arry_Seller[i]).appendTo("#RowLegalOwnerLoanInfo");
                        $("#LegalEntityTableTemplate").tmpl(arry_Seller[i]).appendTo("#RowLegalOwnerPermInfo");
                    }
                    $("#OwnerRecordsRowDataSaleEdit").i18n();
                }
            }

            finalbuyerarray = arry_Buyer;

            // NOT USED  
            if (arry_Seller != null && arry_Seller.length > 0)
            {
                $("#SecondOwner").hide();

                data = arry_Seller[0];
                (data);

                $("#firstname_r_sale").val(data.firstname);
                $("#middlename_r_sale").val(data.middlename);
                $("#lastname_r_sale").val(data.lastname);
                $("#id_r").val(data.identityno);
                $("#contact_no_sale").val(data.contactno);
                $("#address_sale").val(data.address);
                $("#date_Of_birth_sale").val(data.dob);

                $("#sale_martial_status").empty();
                $("#sale_gender").empty();
                $("#sale_id_type").empty();

                jQuery.each(maritalStatus_R, function (i, obj) {
                    $("#sale_martial_status").append(
                            $("<option></option>").attr("value",
                            obj.maritalstatusid).text(
                            obj.maritalstatusEn));
                });

                $("#sale_martial_status").val(data.maritalstatusid);

                jQuery.each(genderStatus_R, function (i, obj1) {
                    $("#sale_gender").append(
                            $("<option></option>").attr("value",
                            obj1.genderId).text(obj1.gender_en));
                });

                $("#sale_gender").val(data.genderid);

                jQuery.each(idtype_R, function (i, obj1) {
                    $("#sale_id_type").append(
                            $("<option></option>").attr("value",
                            obj1.identitytypeid).text(
                            obj1.identitytypeEn));
                });
                $("#sale_id_type").val(data.identitytypeid);

                $("#Owner_Elimanated").empty();
                $("#Owner_Elimanated").append($("<option></option>").attr("value", "").text($.i18n("gen-please-select")));
                jQuery.each(arry_Seller, function (i, obj1)
                {
                    $("#Owner_Elimanated").append($("<option></option>").attr("value", obj1.personid).text(obj1.firstname));
                });
                $("#Owner_Elimanated").val(objdata[0].personid);
            }

            // Buyer
            $("#sale_marital_buyer").empty();
            $("#sale_gender_buyer").empty();
            $("#sale_idtype_buyer").empty();
            $("#doc_Type_Sale").empty();
            $("#cbxLegalBuyerType").empty();

            $("#sale_marital_buyer").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#cbxLegalBuyerType").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#sale_gender_buyer").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#sale_idtype_buyer").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#doc_Type_Sale").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

            jQuery.each(maritalStatus_R, function (i, obj1) {
                var displayName = obj1.maritalstatus;
                if (Global.LANG === "en") {
                    displayName = obj1.maritalstatusEn;
                }
                $("#sale_marital_buyer").append($("<option></option>").attr("value", obj1.maritalstatusid).text(displayName));
            });
            $.each(leTypes, function (i, t) {
                var displayName = t.grouptype;
                if (Global.LANG === "en") {
                    displayName = t.grouptypeEn;
                }
                $("#cbxLegalBuyerType").append($("<option></option>").attr("value", t.grouptypeid).text(displayName));
            });
            jQuery.each(genderStatus_R, function (i, obj1) {
                $("#sale_gender_buyer").append(
                        $("<option></option>").attr("value",
                        obj1.genderId).text(obj1.gender_en));
            });
            jQuery.each(idtype_R, function (i, obj1) {
                $("#sale_idtype_buyer").append(
                        $("<option></option>").attr("value",
                        obj1.identitytypeid).text(
                        obj1.identitytypeEn));
            });
            jQuery.each(documenttype_R, function (i, obj) {
                $("#doc_Type_Sale").append(
                        $("<option></option>").attr("value",
                        obj.code).text(obj.nameOtherLang));
            });

            if (arry_Buyer != null && arry_Buyer.length > 0)
            {
                databuyer = arry_Buyer[0];
            }

            // Land Details landtype_r
            $("#sale_land_type").empty();
            $("#sale_country").empty();
            $("#sale_region").empty();
            $("#province_r").empty();
            $("#sale_land_Share_type").empty();
            $("#sale_province").empty();

            $("#sale_country").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));
            $("#sale_region").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));
            $("#sale_province").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));
            $("#sale_land_type").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));
            $("#sale_land_Share_type").append(
                    $("<option></option>").attr("value", 0).text(
                    $.i18n("gen-please-select")));

            jQuery.each(landtype_r,
                    function (i, obj1) {
                        $("#sale_land_type").append(
                                $("<option></option>").attr(
                                "value", obj1.landtypeid).text(
                                obj1.landtypeEn));
                    });
            jQuery.each(allcountry, function (i, obj) {
                $("#sale_country").append(
                        $("<option></option>").attr("value",
                        obj.hierarchyid).text(obj.nameEn));
            });

            jQuery.each(region_r, function (i, obj) {
                $("#sale_region").append(
                        $("<option></option>").attr("value",
                        obj.hierarchyid).text(obj.nameEn));
            });
            jQuery.each(province_r, function (i, obj) {
                $("#sale_province").append(
                        $("<option></option>").attr("value",
                        obj.hierarchyid).text(obj.nameEn));
            });

            jQuery.each(landsharetype_r, function (i, obj) {
                $("#sale_land_Share_type").append(
                        $("<option></option>").attr("value",
                        obj.landsharetypeid).text(
                        obj.landsharetypeEn));
            });

            $("#sale_land_type").val(suReg.landtypeid);
            if (suReg.firstname != "") {
                $("#proposedUse_lease").val(suReg.firstname)
                $("#sale_proposeduse").val(suReg.firstname)
                $("#mortgage_proposed_use").val(suReg.firstname)
            }
            $("#sale_country").val(suReg.hierarchyid1);
            $("#sale_region").val(suReg.hierarchyid2);
            $("#sale_province").val(suReg.hierarchyid3);

            $("#parcel_r").val("000000" + suReg.landid);
            $("#sale_land_Share_type").val(suReg.landusetypeid);
            $("#sale_area").val(suReg.area);
            $("#sale_land_use").val(suReg.landusetype_en);
            $("#neighbor_west_sale").val(suReg.neighbor_west);
            $("#neighbor_north_sale").val(suReg.neighbor_north);
            $("#neighbor_south_sale").val(suReg.neighbor_south);
            $("#neighbor_east_sale").val(suReg.neighbor_east);
        }
    });
}

function editAttributeRegistration(transactionid, landid) {
//	 isVisible = $('#buyersavebutton').is(':visible');
    $(".signin_menu").hide();
    var lease = document.getElementById("Tab_1");
    var sale = document.getElementById("Tab_2");
    var mortgage = document.getElementById("Tab_3");
    var split = document.getElementById("Tab_4");
    lease.style.display = "none"; // lease.style.display = "block";
    sale.style.display = "none";
    mortgage.style.display = "none";
    split.style.display = "none";

    clearBuyerDetails_sale();

    var process = 0;
    $("#editflag").val(edit);

    $("#transactionId").val(transactionid);

    $("#registration_process").hide();

    finaltransid = transactionid;

    $("#buyersavebutton").prop("disabled", false).hide();
    selectedlandid = landid;
    $("#landidhide").val(landid);
    editPersonsOfEditingForEditing();
    editPersonsOfEditingForEditingLeasee();

    $("#RegLeasepersonsEditingGrid").hide();
    $("#editRegpersonsEditingGridLeasee").show();

    jQuery.ajax({
        url: "registration/doctype/",
        async: false,
        success: function (data) {
            documenttype_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/monthoflease/",
        async: false,
        success: function (data) {
            monthoflease_R = data;
        }
    });

    jQuery.ajax({
        url: "registration/financialagency/",
        async: false,
        success: function (data) {
            financialagency_R = data;
        }
    });

    jQuery.ajax({
        url: "landrecords/editattribute/" + landid,
        async: false,
        success: function (data) {
            suReg = data[0];
        }
    });

//    jQuery.ajax({
//        url: "registration/laspatialunitland/" + landid,
//        async: false,
//        success: function (data) {
//            suReg = data;
//        }
//    });

    jQuery.ajax({
        url: "registration/laMortgage/" + landid,
        async: false,
        success: function (data) {
            laMortgage_R = data;
            if (data.length > 0) {

            }
        }
    });

    jQuery.ajax({
        url: "registration/relationshiptypes/",
        async: false,
        success: function (data) {
            relationtypes_R = data;

        }
    });

    $.ajax({
        url: "resource/relationshipTypes/",
        async: false,
        success: function (data1) {

            relationShips = data1;
        }
    });

    jQuery.ajax({
        url: "registration/allregion/" + country_r_id,
        async: false,
        success: function (data) {
            region_r = data;
            if (data.length > 0) {
                // data.xyz.name_en for getting the data
            }
        }
    });

    jQuery.ajax({
        url: "registration/allprovince/" + region_r_id,
        async: false,
        success: function (data) {
            province_r = data;
        }
    });


    $("#registration_process").empty();
    $("#registration_process").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

    jQuery.each(processdetails_R, function (i, obj)
    {
        $("#registration_process").append($("<option></option>").attr("value", obj.processid).text(obj.processname_en));

    });


    jQuery.ajax({
        url: "registration/processid/" + transactionid,
        async: false,
        success: function (data) {
            process = data.processid;
            $("#registration_process").val(data.processid);
            process_id = process;


        }
    });

    $(function () {

        $("#Tab_1").hide();
        $("#Tab_2").hide();
        $("#Tab_3").hide();

        attributeEditDialog = $("#lease-dialog-form").dialog({
            autoOpen: false,
            height: 700,
            width: 1000,
            resizable: true,
            modal: true,
            close: function () {
                attributeEditDialog.dialog("destroy");
                $("input,select,textarea").removeClass('addBg');
            },
            buttons: [{
                    text: "Save & Next",
                    "id": "comment_Next",
                    click: function ()
                    {
                        $("input,select,textarea").removeClass('addBg');
                        if (currentdiv === "Sale")
                        {
                            if ($('#Seller_Details').css('display') == 'block')
                            {
                                $("#Seller_Details").hide();
                                $("#Land_Details_Sale").show();
                                $("#Buyer_Details").hide();
                                $("#Upload_Document_Sale").hide();
                                $("#selectedseller").removeClass("ui-tabs-active");
                                $("#selectedseller").removeClass("ui-state-active");
                                $("#selectedland").addClass("ui-tabs-active");
                                $("#selectedland").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Land_Details_Sale').css('display') == 'block')
                            {
                                $("#Buyer_Details").show();
                                $("#Seller_Details").hide();
                                $("#Land_Details_Sale").hide();
                                $("#Upload_Document_Sale").hide();

                                $("#selectedland").removeClass("ui-tabs-active");
                                $("#selectedland").removeClass("ui-state-active");
                                $("#selectedbuyer").addClass("ui-tabs-active");
                                $("#selectedbuyer").addClass("ui-state-active");



                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Buyer_Details').css('display') == 'block')
                            {
                                if (null != arry_Buyerbyprocessid) {
                                    $("#Seller_Details").hide();
                                    $("#Buyer_Details").hide();
                                    $("#Land_Details_Sale").hide();
                                    $("#regPoi").show();
                                    $("#Upload_Document_Sale").hide();
                                    $("#selectedbuyer").removeClass("ui-tabs-active");
                                    $("#selectedbuyer").removeClass("ui-state-active");
                                    $("#selectedpoi").addClass("ui-tabs-active");
                                    $("#selectedpoi").addClass("ui-state-active");
                                    $("#comment_Save").hide();
                                    $("#comment_Next").show();
                                } else {

                                    jAlert($.i18n("err-save-buyer"), $.i18n("gen-info"));
                                }
                            } else if ($('#regPoi').css('display') == 'block')
                            {

                                $("#Seller_Details").hide();
                                $("#Buyer_Details").hide();
                                $("#Land_Details_Sale").hide();
                                $("#regPoi").hide();
                                $("#Upload_Document_Sale").show();
                                $("#selectedpoi").removeClass("ui-tabs-active");
                                $("#selectedpoi").removeClass("ui-state-active");
                                $("#selecteddoc").addClass("ui-tabs-active");
                                $("#selecteddoc").addClass("ui-state-active");
                                $("#comment_Save").show();
                                $("#comment_Next").hide();
                            }
                        } else if (currentdiv == "Lease")
                        {
                            if ($('#Owner_Details').css('display') == 'block')
                            {
                                $("#Owner_Details").hide();
                                $("#Land_Details_lease").show();
                                $("#Applicant_Details").hide();
                                $("#Lease_Details").hide();
                                $("#regLeasePoi").hide();
                                $("#Upload_Documents_Lease").hide();
                                $("#selectedowner").removeClass("ui-tabs-active");
                                $("#selectedowner").removeClass("ui-state-active");
                                $("#selectedLanddetails").addClass("ui-tabs-active");
                                $("#selectedLanddetails").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();
                            } else if ($('#Land_Details_lease').css('display') == 'block')
                            {
                                $("#Owner_Details").hide();
                                $("#Land_Details_lease").hide();
                                $("#Applicant_Details").show();
                                $("#Lease_Details").hide();
                                $("#regLeasePoi").hide();
                                $("#Upload_Documents_Lease").hide();
                                $("#selectedLanddetails").removeClass("ui-tabs-active");
                                $("#selectedLanddetails").removeClass("ui-state-active");
                                $("#selectedApplicant").addClass("ui-tabs-active");
                                $("#selectedApplicant").addClass("ui-state-active");

                                $("#comment_Save").show();
                                $("#comment_Next").hide();

                            } else if ($('#Applicant_Details').css('display') == 'block')
                            {
                                $("#Owner_Details").hide();
                                $("#Land_Details_lease").hide();
                                $("#regLeasePoi").show();
                                $("#Lease_Details").hide();
                                $("#Applicant_Details").hide();

                                $("#Upload_Documents_Lease").hide();
                                $("#selectedApplicant").removeClass("ui-tabs-active");
                                $("#selectedApplicant").removeClass("ui-state-active");

                                $("#selectedLeasepoi").addClass("ui-tabs-active");
                                $("#selectedLeasepoi").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();
                            } else if ($('#regLeasePoi').css('display') == 'block')
                            {
                                $("#Owner_Details").hide();
                                $("#Land_Details_lease").hide();
                                $("#Applicant_Details").hide();
                                $("#Lease_Details").show();
                                $("#regLeasePoi").hide();

                                $("#Upload_Documents_Lease").hide();
                                $("#selectedLeasepoi").removeClass("ui-tabs-active");
                                $("#selectedLeasepoi").removeClass("ui-state-active");
                                $("#selectedsleasedetails").addClass("ui-tabs-active");
                                $("#selectedsleasedetails").addClass("ui-state-active");

                                $("#comment_Save").hide();
                                $("#comment_Next").show();
                            } else if ($('#Lease_Details').css('display') == 'block')
                            {
                                if (processid == "5")
                                {
                                    saveattributesSurrenderLease();
                                } else
                                {
                                    saveattributesLeaseDetails();
                                }

                                $("#Owner_Details").hide();
                                $("#Land_Details_lease").hide();
                                $("#Applicant_Details").hide();
                                $("#comment_Save").show();
                                $("#comment_Next").hide();
                            }

                        } else if (currentdiv == "Mortgage")
                        {

                            if ($('#MortgageOwner_Details').css('display') == 'block')
                            {
                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").show();
                                $("#Mortgage_Details").hide();
                                $("#Upload_Document_Mortgage").hide();
                                $("#selectedownerdetails").removeClass("ui-tabs-active");
                                $("#selectedownerdetails").removeClass("ui-state-active");
                                $("#selectelandmortgage").addClass("ui-tabs-active");
                                $("#selectelandmortgage").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Land_Details_Mortgage').css('display') == 'block')
                            {
                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").hide();
                                $("#Mortgage_Details").show();
                                $("#Upload_Document_Mortgage").hide();
                                $("#selectelandmortgage").removeClass("ui-tabs-active");
                                $("#selectelandmortgage").removeClass("ui-state-active");
                                $("#selectemortgage").addClass("ui-tabs-active");
                                $("#selectemortgage").addClass("ui-state-active");
                                $("#comment_Save").hide();
                                $("#comment_Next").show();

                            } else if ($('#Mortgage_Details').css('display') == 'block')
                            {

                                if (processid == "9")
                                {
                                    saveattributesSurrenderMortagage();
                                } else
                                {
                                    saveMortgage();
                                }

                                $("#MortgageOwner_Details").hide();
                                $("#Land_Details_Mortgage").hide();
                                $("#Mortgage_Details").hide();
                                $("#Upload_Document_Mortgage").show();
                                $("#selectemortgage").removeClass("ui-tabs-active");
                                $("#selectemortgage").removeClass("ui-state-active");
                                $("#selectemortgagedocs").addClass("ui-tabs-active");
                                $("#selectemortgagedocs").addClass("ui-state-active");
                                $("#comment_Save").show();
                                $("#comment_Next").hide();
                            }

                        } else if (currentdiv == "split") {
                            $("#comment_Save").hide();
                            $("#comment_Next").show();
                            attributeEditDialog.dialog("close");
                            showOnMap(selectedlandid, 0, "split");
                        }
                    }

                },
                {
                    text: $.i18n("reg-reg"),
                    "id": "comment_Save",
                    click: function ()
                    {
                        if (currentdiv == "Sale")
                        {
                            saveattributessale();
                        } else if (currentdiv == "Lease")
                        {
                            saveLease();
                        } else
                        {
                            saveattributesMortagage();
                        }
                    }
                },
                {
                    text: $.i18n("gen-cancel"),
                    "id": "comment_cancel",
                    click: function () {
                        $("input,select,textarea").removeClass('addBg');
                        setInterval(function () {

                        }, 4000);
                        attributeEditDialog.dialog("close");
                    }
                }
            ],
            Cancel: function () {

                attributeEditDialog.dialog("close");
                $("input,select,textarea").removeClass('addBg');

            }
        });
        $("#comment_cancel").html('<span class="ui-button-text">Cancel</span>');
        attributeEditDialog.dialog("open");
        $("#comment_Save").hide();
        $("#comment_Next").hide();

    });

    if (process == 2 || process == 4 || process == 6 || process == 7) {
        getprocessvalue(2);
        SaleOwnerdBuyeretails(landid);
        salebuyerdetails(process);

    } else if (process == 1 || process == 5 || process == 10) {
        getprocessvalue(1);
        SaleOwnerdBuyeretails(landid);
    } else if (process == 3 || process == 9) {
        getprocessvalue(3);
        SaleOwnerdBuyeretails(landid);
        fillMortgageDetails(landid);
    }

    jQuery.ajax({
        type: 'GET',
        url: "registryrecords/editDocuments/" + landid + "/" + transactionid,
        async: false,
        success: function (data) {
            if (data != null && data != "")
            {
                $("#salesDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#salesDocRowData");
                $("#salesDocRowData").i18n();

                $("#LeaseDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#LeaseDocRowData");
                $("#LeaseDocRowData").i18n();

                $("#MortagageDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#MortagageDocRowData");
                $("#MortagageDocRowData").i18n();
            }

        }
    });

    jQuery.ajax({
        url: "registration/financialagency/",
        async: false,
        success: function (data) {
            financialagency_R = data;
        }
    });

    $("#mortgage_to").val("");
    $("#mortgage_from").val("");
    $("#mortgage_Financial_Agencies").empty();
    $("#amount_mortgage").val("");

    $("#mortgage_Financial_Agencies").append(
            $("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    jQuery.each(financialagency_R, function (i, obj1) {
        $("#mortgage_Financial_Agencies").append(
                $("<option></option>").attr("value",
                obj1.financialagencyid).text(obj1.financialagency_en));
    });

    jQuery.ajax({
        url: "registration/editlaMortgage/" + landid + "/" + transactionid,
        async: false,
        success: function (data) {
            laMortgage_R = data;
            if (laMortgage_R != null) {


                if (laMortgage_R != "" && laMortgage_R != null)
                {
                    if (laMortgage_R.laExtFinancialagency != null && laMortgage_R.laExtFinancialagency != "")
                    {
                        $("#mortgage_Financial_Agencies").val(laMortgage_R.laExtFinancialagency.financialagencyid);
                    }

                }

                if (laMortgage_R.mortgagefrom != null || laMortgage_R.mortgagefrom != "") {
                    $("#mortgage_from").val(formatDate_R(laMortgage_R.mortgagefrom));
                } else {
                    $("#mortgage_from").val("");
                }
                if (laMortgage_R.mortgageto != null || laMortgage_R.mortgageto != "") {
                    $("#mortgage_to").val(formatDate_R(laMortgage_R.mortgageto));
                } else {
                    $("#mortgage_to").val("");
                }
                $("#amount_mortgage").val(laMortgage_R.mortgageamount);


            }
        }
    });

    jQuery.ajax({
        type: "GET",
        url: "landrecords/landPOI/" + finaltransid + "/" + landid,
        async: false,
        success: function (data) {


            $("#BuyerPOIRecordsRowDataSale").empty();
            $("#POIRecordsRowDataMortgage1").empty();
            $("#BuyerPOIRecordsRowDataLease").empty();


            if (null != data || data != "") {
                for (var i = 0; i < data.length; i++) {

                    var relation = "";
                    var gender = "";
                    for (var j = 0; j < relationtypes_R.length; j++) {

                        if (data[i].relation == relationtypes_R[j].relationshiptypeid) {

                            relation = relationtypes_R[j].relationshiptype;
                        }
                    }

                    for (var k = 0; k < genderStatus_R.length; k++) {

                        if (genderStatus_R[k].genderId == data[i].gender) {

                            gender = genderStatus_R[k].gender;
                        }
                    }

                    data[i].relation = relation;
                    data[i].gender = gender;
                    $("#BuyerPOIRecordsAttrTemplateSale").tmpl(data[i]).appendTo("#BuyerPOIRecordsRowDataSale");
                    $("#BuyerPOIRecordsRowDataSale").i18n();
                    $("#POIRecordsAttrTemplateMortgage1").tmpl(data[i]).appendTo("#POIRecordsRowDataMortgage1");
                    $("#BuyerPOIRecordsAttrTemplateLease").tmpl(data[i]).appendTo("#BuyerPOIRecordsRowDataLease");
                    $("#BuyerPOIRecordsRowDataLease").i18n();
                }
            }
        }
    });


    $("#Relationship_sale_POI").empty();
    $("#Relationship_sale_POI").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    jQuery.each(relationtypes_R, function (i, obj)
    {
        $("#Relationship_sale_POI").append($("<option></option>").attr("value", obj.relationshiptypeid).text(obj.relationshiptype));
    });

    $("#gender_sale_POI").empty();
    $("#gender_sale_POI").append(
            $("<option></option>").attr("value", 0).text(
            $.i18n("gen-please-select")));
    jQuery.each(genderStatus_R, function (i, obj1) {
        var displayName = obj1.gender;
        if (Global.LANG === "en") {
            displayName = obj1.gender_en;
        }
        $("#gender_sale_POI").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
    });

    $("#Relationship_POI").empty();
    $("#Relationship_POI").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    jQuery.each(relationtypes_R, function (i, obj)
    {
        $("#Relationship_POI").append($("<option></option>").attr("value", obj.relationshiptypeid).text(obj.relationshiptype));
    });

    $("#gender_type_POI").empty();
    $("#gender_type_POI").append(
            $("<option></option>").attr("value", 0).text(
            $.i18n("gen-please-select")));
    jQuery.each(genderStatus_R, function (i, obj1) {
        $("#gender_type_POI").append(
                $("<option></option>").attr("value",
                obj1.genderId).text(obj1.gender_en));
    });


}

function editPersonsOfEditingForEditing() {
    $("#editRegpersonsEditingGrid").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: true,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: editRegpersonsEditingController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", deleteButton: false},
            {name: "firstName", title: $.i18n("reg-firstname"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middleName", title: $.i18n("reg-middlename"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-enter-middle-name")}},
            {name: "lastName", title: $.i18n("reg-lastname"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "gender", title: $.i18n("reg-gender"), type: "select", items: [{id: 1, name: "Male"}, {id: 2, name: "Female"}, {id: 3, name: "Other"}], valueField: "id", textField: "name", width: 210, validate: {validator: "required", message: $.i18n("err-select-gender")}},
            {name: "dob", title: $.i18n("reg-dob"), type: "date", width: 210, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "relation", title: $.i18n("reg-relation"), type: "select", items: [{id: 1, name: "Spouse"}, {id: 2, name: "Son"}, {id: 3, name: "Daughter"}, {id: 4, name: "Grandson"}, {id: 5, name: "Granddaughter"}, {id: 6, name: "Brother"},
                    {id: 7, name: "Sister"}, {id: 8, name: "Father"}, {id: 9, name: "Mother"}, {id: 10, name: "Grandmother"}, {id: 11, name: "Grandfather"}, {id: 12, name: "Aunt"},
                    {id: 13, name: "Uncle"}, {id: 14, name: "Niece"}, {id: 15, name: "Nephew"}, {id: 16, name: "Other"}, {id: 17, name: "Other relatives"}, {id: 18, name: "Associate"},
                    {id: 19, name: "Parents and children"}, {id: 20, name: "Siblings"}], valueField: "id", textField: "name", width: 210, validate: {validator: "required", message: $.i18n("err-select-relation-type")}},
            {name: "landid", title: $.i18n("reg-landid"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false},
            {name: "id", title: $.i18n("reg-id"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false}
        ]
    });
    $("#editRegpersonsEditingGrid .jsgrid-table th:first-child :button").click();
    $("#editRegpersonsEditingGrid").jsGrid("loadData");
}

var editRegpersonsEditingController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/editlandPOIBuyer/" + selectedlandid + "/" + finaltransid,
            data: filter,
            success: function (data) {
                if (data == "" || data == null) {





                }
            }
        });
    },
    insertItem: function (item) {

    },
    updateItem: function (item) {

        var ajaxdata = {
            "firstName": item.firstName,
            "middleName": item.middleName,
            "lastName": item.lastName,
            "gender": item.gender,
            "dob": item.dob,
            "relation": item.relation,
            "id": item.id

        }

        return $.ajax({
            type: "POST",
//           contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/editRegPersonOfInterestForEditing/" + selectedlandid + "/" + finaltransid,
            data: ajaxdata,
            async: false,
            success: function (data) {
                if (data == "" || data == null) {
                    editPersonsOfEditingForEditing();
                    jAlert($.i18n("err-add-buyer-toadd-poi"));

                } else {
                    editPersonsOfEditingForEditing();
                    jAlert($.i18n("reg-poi-added"), $.i18n("gen-info"));
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

function addRegPOI() {
    if (edit == 1) {

        $("#editRegpersonsEditingGrid").jsGrid("insertItem", {});
    } else if (edit == 0) {
        $.ajax({
            type: "GET",
            url: "landrecords/landPOIstatus/" + selectedlandid + "/" + processid,
            data: filter,
            success: function (data) {

                alertmessage = data;

                if (alertmessage == "true") {
                    $("#RegpersonsEditingGrid").jsGrid("insertItem", {});
                } else {
                    jAlert(alertmessage, $.i18n("gen-info"));
                }
            }
        });
    }

}

function editPersonsOfEditingForEditingLeasee() {
    $("#editRegpersonsEditingGridLeasee").jsGrid({
        width: "100%",
        height: "200px",
        inserting: false,
        editing: true,
        sorting: false,
        filtering: false,
        paging: true,
        autoload: false,
        controller: editRegpersonsEditingLeaseeController,
        pageSize: 50,
        pageButtonCount: 20,
        fields: [
            {type: "control", deleteButton: false},
            {name: "firstName", title: $.i18n("reg-firstname"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-select-firstname")}},
            {name: "middleName", title: $.i18n("reg-middlename"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-select-middle-name")}},
            {name: "lastName", title: $.i18n("reg-lastname"), type: "text", width: 210, validate: {validator: "required", message: $.i18n("err-select-lastname")}},
            {name: "gender", title: $.i18n("reg-gender"), type: "select", items: [{id: 1, name: "Male"}, {id: 2, name: "Female"}, {id: 3, name: "Other"}], valueField: "id", textField: "name", width: 210, validate: {validator: "required", message: $.i18n("err-select-gender")}},
            {name: "dob", title: $.i18n("reg-dob"), type: "date", width: 210, validate: {validator: "required", message: $.i18n("err-enter-dob")}},
            {name: "relation", title: $.i18n("reg-relation"), type: "select", items: [{id: 1, name: "Spouse"}, {id: 2, name: "Son"}, {id: 3, name: "Daughter"}, {id: 4, name: "Grandson"}, {id: 5, name: "Granddaughter"}, {id: 6, name: "Brother"},
                    {id: 7, name: "Sister"}, {id: 8, name: "Father"}, {id: 9, name: "Mother"}, {id: 10, name: "Grandmother"}, {id: 11, name: "Grandfather"}, {id: 12, name: "Aunt"},
                    {id: 13, name: "Uncle"}, {id: 14, name: "Niece"}, {id: 15, name: "Nephew"}, {id: 16, name: "Other"}, {id: 17, name: "Other relatives"}, {id: 18, name: "Associate"},
                    {id: 19, name: "Parents and children"}, {id: 20, name: "Siblings"}], valueField: "id", textField: "name", width: 210, validate: {validator: "required", message: $.i18n("err-enter-relation")}},
            {name: "landid", title: $.i18n("reg-landid"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false},
            {name: "id", title: $.i18n("reg-id"), type: "number", width: 70, align: "left", editing: false, filtering: true, visible: false},
        ]
    });
    $("#editRegpersonsEditingGridLeasee .jsgrid-table th:first-child :button").click();
    $("#editRegpersonsEditingGridLeasee").jsGrid("loadData");
}

var editRegpersonsEditingLeaseeController = {
    loadData: function (filter) {
        $("#btnLoadPersons").val("Reload");
        return $.ajax({
            type: "GET",
            url: "landrecords/editlandPOIBuyer/" + selectedlandid + "/" + finaltransid,
            data: filter,
            success: function (data) {
                if (data == "" || data == null) {





                }
            }
        });
    },
    insertItem: function (item) {

    },
    updateItem: function (item) {

        var ajaxdata = {
            "firstName": item.firstName,
            "middleName": item.middleName,
            "lastName": item.lastName,
            "gender": item.gender,
            "dob": item.dob,
            "relation": item.relation,
            "id": item.id

        }

        return $.ajax({
            type: "POST",
//          contentType: "application/json; charset=utf-8",
            traditional: true,
            url: "landrecords/editRegPersonOfInterestForEditing/" + selectedlandid + "/" + finaltransid,
            data: ajaxdata,
            async: false,
            success: function (data) {
                if (data == "" || data == null) {
                    editPersonsOfEditingForEditingLeasee();
                    jAlert($.i18n("err-add-buyer-toadd-poi"));

                } else {
                    editPersonsOfEditingForEditingLeasee();
                    jAlert($.i18n("reg-poi-added"), $.i18n("gen-info"));
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





function addRegLeasePOI() {
    if (edit == 1) {

        $("#editRegpersonsEditingGridLeasee").jsGrid("insertItem", {});
    } else if (edit == 0) {
        $.ajax({
            type: "GET",
            url: "registrion/addLeaseePoi/" + selectedlandid,
            data: filter,
            success: function (data) {

                alertmessage = data;

                if (alertmessage == "true") {
                    $("#RegLeasepersonsEditingGrid").jsGrid("insertItem", {});
                } else {
                    jAlert(alertmessage, $.i18n("gen-info"));
                }
            }
        });

    }


}

function fetchDocEdit() {

    jQuery.ajax({
        type: 'GET',
        url: "registryrecords/editDocuments/" + selectedlandid + "/" + finaltransid,
        async: false,
        success: function (data) {
            if (data != null && data != "")
            {
                $("#salesDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#salesDocRowData");
                $("#salesDocRowData").i18n();

                $("#LeaseDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#LeaseDocRowData");
                $("#LeaseDocRowData").i18n();

                $("#MortagageDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#MortagageDocRowData");
                $("#MortagageDocRowData").i18n();
            }
        }
    });
}

function loadPermission() {
    if (!$("#permissionLandDetailsContainer #pnlLandDetails").length) {
        $("#permissionLandDetailsContainer").empty();
        $("#pnlLandDetails").appendTo($("#permissionLandDetailsContainer"));
    }
    fillLandDetails();

    $("#permissionId").val("");
    $("#surrenderPermissionId").val("");

    if (natureOfPowers === null) {
        jQuery.ajax({
            url: "landrecords/powernature/",
            async: false,
            success: function (data) {
                natureOfPowers = data;
            }
        });
    }

    // Init lists
    $("#perm_marital_status").empty();
    $("#perm_gender").empty();
    $("#perm_nop").empty();

    $("#perm_marital_status").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#perm_gender").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
    $("#perm_nop").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

    jQuery.each(maritalStatus_R, function (i, obj1) {
        var displayName = obj1.maritalstatus;
        if (Global.LANG === "en") {
            displayName = obj1.maritalstatusEn;
        }
        $("#perm_marital_status").append($("<option></option>").attr("value", obj1.maritalstatusid).text(displayName));
    });
    jQuery.each(genderStatus_R, function (i, obj1) {
        var displayName = obj1.gender;
        if (Global.LANG === "en") {
            displayName = obj1.gender_en;
        }
        $("#perm_gender").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
    });
    jQuery.each(natureOfPowers, function (i, obj1) {
        var displayName = obj1.name;
        if (Global.LANG === "en") {
            displayName = obj1.nameEn;
        }
        $("#perm_nop").append($("<option></option>").attr("value", obj1.id).text(displayName));
    });

    var permission = null;

    if (processid == 11) {
        // Try to get pending permission
        $.ajax({
            url: "registration/getPendingPermission/" + selectedlandid,
            async: false,
            success: function (data) {
                if (data != "" && data !== null) {
                    permission = data;
                    removeNulls(permission);
                    $("#permissionId").val(permission.id);
                }
            }
        });
    } else if (processid == 12) {
        // Try to get surrender lease
        $.ajax({
            url: "registration/getPendingPermissionSurrender/" + selectedlandid,
            async: false,
            success: function (data) {
                if (data != "" && data !== null) {
                    permission = data;
                    removeNulls(permission);
                    $("#permissionId").val(permission.terminatedid);
                    $("#surrenderPermissionId").val(permission.id);
                } else {
                    // Try to get registered permission
                    $.ajax({
                        url: "registration/getRegisteredPermission/" + selectedlandid,
                        async: false,
                        success: function (data) {
                            if (data != "" && data !== null) {
                                permission = data;
                                removeNulls(permission);
                                $("#permissionId").val(permission.id);
                            }
                        }
                    });
                }
            }
        });
    }

    $("#btnSavePermApplicant").show();
    $("#btnSavePermApplicant").text($.i18n("reg-add-person"));

    // Disable/hide fields if it's surrender. Other wise revert
    var disabled = false;
    if (processid == 12) {
        disabled = true;
    }

    $("#tablePermissionApplicantDetails input").prop("disabled", disabled);
    $("#tablePermissionApplicantDetails select").prop("disabled", disabled);
    $("#tablePermissionDetails input").prop("disabled", disabled);

    if (permission !== null) {
        showPermissionApplicant(permission.applicant);
        $("#permRegNum").val(permission.regnum);
        $("#permAppNum").val(permission.appnum);
        $("#permAppDate").val(permission.appdate);
        $("#permStartDate").val(permission.startdate);
        $("#permEndDate").val(permission.enddate);
        $("#permUsage").val(permission.usage);

        loadPermissionDocs();
    }
}

function printPermission(landid) {
    jQuery.ajax({
        url: "landrecords/spatialunit/form43/" + landid,
        async: false,
        success: function (data) {
            if (data != "" && data !== null) {
                generateform43(landid, data);
            } else {
                jAlert($.i18n("err-no-active-permission"), $.i18n("err-alert"));
            }
        }
    });
}

function showPermissionApplicant(applicant) {
    permissionApplicant = applicant;
    removeNulls(applicant);
    $("#btnSavePermApplicant").hide();
    $("#btnSavePermApplicant").text($.i18n("gen-save"));

    $("#rowsPermissionApplicant").empty();
    $('#rowPermissionApplicant').tmpl(applicant).appendTo('#rowsPermissionApplicant');
    if (processid == 12) {
        $("#rowsPermissionApplicant button").hide();
    } else {
        $("#rowsPermissionApplicant button").show();
    }
    $('#rowsPermissionApplicant').i18n();
}

function loadPermissionDocs() {
    var permissionId = $("#permissionId").val();
    if (processid == 12) {
        permissionId = $("#surrenderPermissionId").val();
    }

    $("#rowsPermissionDocs").empty();

    if (permissionId !== "") {
        $.ajax({
            url: "registration/getPermissionDocs/" + permissionId,
            async: false,
            success: function (data) {
                if (data != "" && data !== null) {
                    $("#salesdocumentTemplate_add").tmpl(data).appendTo("#rowsPermissionDocs");
                    $("#rowsPermissionDocs").i18n();
                }
            }
        });
    }
}

function savePermApplicant() {
    var errors = "";
    if (perm_firstname.value.length === 0) {
        errors += "-" + $.i18n("err-select-firstname") + "\n";
    }
    if (perm_lastname.value.length === 0) {
        errors += "-" + $.i18n("err-select-lastname") + "\n";
    }
    if (perm_dob.value.length === 0) {
        errors += "-" + $.i18n("err-enter-dob") + "\n";
    }
    if (perm_id_num.value.length === 0) {
        errors += "-" + $.i18n("err-enter-idnumber") + "\n";
    }
    if (perm_id_date.value.length === 0) {
        errors += "-" + $.i18n("err-enter-id-date") + "\n";
    }

    if (errors !== "") {
        jAlert(errors, $.i18n("err-alert"));
        return false;
    }

    jQuery.ajax({
        type: "POST",
        url: "registration/saveNewPermission",
        data: $("#editprocessAttributeformID").serialize(),
        success: function (result) {
            if (result !== null && result !== undefined) {
                $("#tablePermissionApplicantDetails input").val("");
                $("#tablePermissionApplicantDetails select").val(0);
                $("#permissionId").val(result.id);
                showPermissionApplicant(result.applicant);
                jAlert($.i18n("reg-applicant-saved"));
            } else {
                jAlert($.i18n("err-request-not-completed"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });
}

function editPermissionApplicant(id) {
    if (permissionApplicant !== null) {
        var person = permissionApplicant;

        $("#perm_firstname").val(person.firstname);
        $("#perm_middlename").val(person.middlename);
        $("#perm_lastname").val(person.lastname);
        $("#perm_id_num").val(person.identityno);
        $("#perm_id_date").val(person.idDate);
        $("#perm_address").val(person.address);
        $("#perm_dob").val(person.dob);
        $("#perm_place_of_birth").val(person.birthplace);
        $("#perm_marital_status").val(person.maritalstatusid);
        $("#perm_gender").val(person.genderid);
        $("#perm_profession").val(person.profession);
        $("#perm_father_name").val(person.fathername);
        $("#perm_mother_name").val(person.mothername);
        $("#perm_nop").val(person.nopId);
        $("#perm_mandate_date").val(person.mandateDate);
        $("#perm_mandate_loc").val(person.mandateLocation);
        $("#btnSavePermApplicant").show();
    }
}

function registerNewPermission() {
    if ($("#permissionId").val() === "") {
        jAlert($.i18n("err-add-applicant"), $.i18n("err-alert"));
        return;
    }
    // Validate
    var errors = "";
    if ($("#permRegNum").val() === "") {
        errors += "- " + $.i18n("err-enter-regnum") + "\n";
    }
    if ($("#permAppNum").val() === "") {
        errors += "- " + $.i18n("err-enter-appnum") + "\n";
    }
    if ($("#permAppDate").val() === "") {
        errors += "- " + $.i18n("err-enter-appdate") + "\n";
    }
    if ($("#permStartDate").val() === "") {
        errors += "- " + $.i18n("err-enter-start-date") + "\n";
    }
    if ($("#permEndDate").val() === "") {
        errors += "- " + $.i18n("err-enter-end-date") + "\n";
    }
    if (errors !== "") {
        jAlert(errors, $.i18n("err-alert"));
        return;
    }

    jConfirm($.i18n("reg-reg-confirmation"), $.i18n("gen-confirmation"),
            function (response) {
                if (response) {
                    jQuery.ajax({
                        type: "POST",
                        url: "registration/registerNewPermission",
                        data: $("#editprocessAttributeformID").serialize(),
                        success: function (result) {
                            jAlert($.i18n("reg-permission-registered"), $.i18n("gen-info"));
                            attributeEditDialog.dialog("close");
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            jAlert($.i18n("err-request-not-completed"));
                        }
                    });
                }
            });
}

function registerPermissionSurrender() {
    if ($("#surrenderPermissionId").val() === "") {
        jAlert($.i18n("err-save-applicant"), $.i18n("err-alert"));
        return;
    }

    jConfirm($.i18n("reg-reg-confirmation"), $.i18n("gen-confirmation"),
            function (response) {
                if (response) {
                    jQuery.ajax({
                        type: "POST",
                        url: "registration/registerSurrenderPermission",
                        data: $("#editprocessAttributeformID").serialize(),
                        success: function (result) {
                            jAlert($.i18n("reg-permission-surrender-registered"), $.i18n("gen-info"));
                            attributeEditDialog.dialog("close");
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            jAlert($.i18n("err-request-not-completed"));
                        }
                    });
                }
            });
}

function savePermissionSurrender() {
    if ($("#surrenderPermissionId").val() !== "") {
        // Skip save, because it already exists
        return;
    }

    jQuery.ajax({
        type: "POST",
        url: "registration/saveSurrenderPermission",
        data: $("#editprocessAttributeformID").serialize(),
        success: function (result) {
            if (result !== null && result !== undefined) {
                $("#surrenderPermissionId").val(result.id);
                showPermissionApplicant(result.applicant);
                jAlert($.i18n("reg-applicant-saved"));
            } else {
                jAlert($.i18n("err-request-not-completed"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            jAlert($.i18n("err-request-not-completed"));
        }
    });
}

function viewPermissionSummary(transactionid) {
    jQuery.ajax({
        type: 'GET',
        url: 'registration/getPermissionByTransaction/' + transactionid,
        async: false,
        success: function (perm) {
            if (perm !== null) {
                removeNulls(perm);
                $('#ViewPopuupDiv').empty();
                $('#ViewPopuupDiv').css("visibility", "visible");

                //To show person Property Changes
                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable"><h3 class="gridTitle"><span>'
                        + $.i18n("reg-applicant-details")
                        + '</span></h3>\n\
                            <table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01">\n\
                                <thead>\n\
                                    <tr>\n\
                                        <th>' + $.i18n("reg-name") + '</th>\n\
                                        <th>' + $.i18n("reg-address") + '</th>\n\
                                        <th>' + $.i18n("reg-id-number") + '</th>\n\
                                        <th>' + $.i18n("reg-id-date") + '</th>\n\
                                    </tr>\n\
                                </thead>\n\
                                <tbody id="popupBody"></tbody>\n\
                            </table>\n\
                            </div>');

                $('#ViewPopuupDiv').append('<div class="amendbeforeAfterTable">\n\
                        <h3 class="gridTitle"><span>' + $.i18n("reg-tran-details") + '</span></h3>\n\
                        <table width=100%" border="0" cellspacing="1" cellpadding="0" class="grid01">\n\
                            <thead>\n\
                                <tr>\n\
                                    <th>' + $.i18n("reg-reg-number2") + '</th>\n\
                                    <th>' + $.i18n("reg-app-number") + '</th>\n\
                                    <th>' + $.i18n("reg-app-date") + '</th>\n\
                                    <th>' + $.i18n("reg-lease-start-date") + '</th>\n\
                                    <th>' + $.i18n("reg-lease-end-date") + '</th>\n\
                                    <th>' + $.i18n("reg-usage") + '</th>\n\
                                </tr>\n\
                            </thead>\n\
                            <tbody>\n\
                                <tr>\n\
                                    <td>' + perm.regnum + '</td>\n\
                                    <td>' + perm.appnum + '</td>\n\
                                    <td>' + perm.appdate + '</td>\n\
                                    <td>' + perm.startdate + '</td>\n\
                                    <td>' + perm.enddate + '</td>\n\
                                    <td>' + perm.usage + '</td>\n\
                                </tr>\n\
                            </tbody>\n\
                        </table>\n\
                        </div></br>');

                if (perm.applicant != null) {
                    $("#permissionApplicantTemplate").tmpl(perm.applicant).appendTo("#popupBody");
                }

                $("#ViewPopuupDiv").dialog(
                        {
                            height: 450,
                            width: 700,
                            modal: true,
                            buttons:
                                    {
                                        "Cancel": {
                                            text: $.i18n("gen-cancel"),
                                            "id": "comment_Trans_cancel",
                                            click: function () {
                                                setInterval(function () {

                                                }, 4000);
                                                $(this).dialog("close");

                                            }
                                        }
                                    },
                            close: function (ev, ui)
                            {
                                $(this).dialog("close");
                            }
                        });
                $('#ViewPopuupDiv').dialog('option', 'title', $.i18n("reg-tran-details"));

            } else
            {
                jAlert($.i18n("err-request-not-completed"));
            }
        },
        error: function () {

        }
    });
}

function loadLease() {
    if (!$("#leaseLandDetailsContainer #pnlLandDetails").length) {
        $("#leaseLandDetailsContainer").empty();
        $("#pnlLandDetails").appendTo($("#leaseLandDetailsContainer"));
    }
    fillLandDetails();
    $("#surrenderLeaseId").val("");

    var leaseConditionsTemplateForm32 =
            "La parcelle est destinée aux  usages suivants: ..............................................................\n\
\n\
les modalités d’exploitations sont : \n\
	a. sont autorisées ce qui suit : ................................................................................\n\
	b. ne sont pas autorisées les pratiques suivantes : .................................................\n\
	c. les compensations sont les suivantes : ...............................................................\n\
\n\
Modalités de reprise de la terre  ................................................................................";

    var leaseConditionsTemplateForm35 = "Conditions de renouvellement: ......................................\n\
\n\
Modalités de paiement : .............................................\n\
\n\
Nature des activités : ..................................................\n\
\n\
Nature des investissements : ......................................\n\
\n\
Améliorations autorisées : .........................................";

    $("#rowUsage").hide();

    if (processid == 10) {
        if ($("#leaseConditions").val() === "") {
            $("#leaseConditions").val(leaseConditionsTemplateForm35);
        }
    } else if (processid == 1) {
        $("#leaseConditions").val(leaseConditionsTemplateForm32);
        $("#rowUsage").show();
    }

    $('#newLeaseeRecordsRowDataSale').empty();
    if (natureOfPowers === null) {
        jQuery.ajax({
            url: "landrecords/powernature/",
            async: false,
            success: function (data) {
                natureOfPowers = data;
            }
        });
    }

    var URL_lease = "";

    if (edit == 0) {
        URL_lease = "registration/getleasebylandid/" + selectedlandid;
    } else if (edit == 1) {
        URL_lease = "registration/getleasebytransactionid/" + finaltransid;
    }

    $("#saveleasee").show();
    $("#saveleasee").text($.i18n("reg-add-person"));

    // Disable/hide fields if it's surrender. Other wise revert
    var disabled = false;
    if (processid == 5) {
        disabled = true;
    }

    $("#tableLeaseApplicantDetails input").prop("disabled", disabled);
    $("#tableLeaseApplicantDetails select").prop("disabled", disabled);
    $("#Lease_Details input").prop("disabled", disabled);
    $("#Lease_Details select").prop("disabled", disabled);
    $("#Lease_Details textarea").prop("disabled", disabled);

    jQuery.ajax({
        url: URL_lease,
        async: false,
        success: function (data) {
            if (data != "" && data !== null) {

                leaseData = data;
                removeNulls(leaseData);

                $("#saveleasee").hide();
                $("#saveleasee").text($.i18n("gen-save"));

                $('#newLeaseeRecordsAttrTemplateSale').tmpl(data.borrower).appendTo('#newLeaseeRecordsRowDataSale');
                if (processid == 5) {
                    $("#newLeaseeRecordsRowDataSale button").hide();
                } else {
                    $("#newLeaseeRecordsRowDataSale button").show();
                }
                $('#newLeaseeRecordsRowDataSale').i18n();

                $("#leaseid").val(leaseData.leaseid);
                $("#lease_Amount").val(leaseData.leaseamount);
                $("#leaseMonths").val(leaseData.months);
                $("#leaseYears").val(leaseData.years);
                $("#leaseStartDate").val(leaseData.leasestartdate);
                $("#leaseEndDate").val(leaseData.leaseenddate);

                if (processid == 5) {
                    $("#leaseConditions").val(leaseData.conditions);
                    // Try to find surrender lease
                    jQuery.ajax({
                        url: "registration/getsurrenderlease/" + selectedlandid,
                        async: false,
                        success: function (data) {
                            if (data !== null) {
                                $("#surrenderLeaseId").val(data.leaseid);
                            }
                        }
                    });
                }
            }

            $("#marital_lease").empty();
            $("#gender_lease").empty();
            $("#nop_lease").empty();

            $("#marital_lease").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#gender_lease").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
            $("#nop_lease").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));

            jQuery.each(maritalStatus_R, function (i, obj1) {
                var displayName = obj1.maritalstatus;
                if (Global.LANG === "en") {
                    displayName = obj1.maritalstatusEn;
                }
                $("#marital_lease").append($("<option></option>").attr("value", obj1.maritalstatusid).text(displayName));
            });
            jQuery.each(genderStatus_R, function (i, obj1) {
                var displayName = obj1.gender;
                if (Global.LANG === "en") {
                    displayName = obj1.gender_en;
                }
                $("#gender_lease").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
            });
            jQuery.each(natureOfPowers, function (i, obj1) {
                var displayName = obj1.name;
                if (Global.LANG === "en") {
                    displayName = obj1.nameEn;
                }
                $("#nop_lease").append($("<option></option>").attr("value", obj1.id).text(displayName));
            });
        }
    });
}

function editLeaseePOI(id) {

    $.ajax({
        type: "GET",
        url: "registration/getPOIbyId/" + id,
        data: filter,
        success: function (data) {

            if (data != "") {
                $("#Relationship_POI").empty();
                $("#Relationship_POI").append($("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
                jQuery.each(relationtypes_R, function (i, obj)
                {
                    $("#Relationship_POI").append($("<option></option>").attr("value", obj.relationshiptypeid).text(obj.relationshiptype));
                });

                $("#gender_type_POI").empty();
                $("#gender_type_POI").append(
                        $("<option></option>").attr("value", 0).text(
                        $.i18n("gen-please-select")));
                jQuery.each(genderStatus_R, function (i, obj1) {
                    $("#gender_type_POI").append(
                            $("<option></option>").attr("value",
                            obj1.genderId).text(obj1.gender_en));
                });
                $('#firstname_r_poi').val(data.firstName);
                $('#middlename_r_poi').val(data.middleName);
                $('#lastname_r_poi').val(data.lastName);
                $('#gender_type_POI').val(data.gender);
                $('#Relationship_POI').val(data.relation);
                $('#date_Of_birthPOI').val(data.dob);
                $('#leaseepoiid').val(id);





            }
        }

    });

}

function editSalePOI(id) {
    $.ajax({
        type: "GET",
        url: "registration/getPOIbyId/" + id,
        data: filter,
        success: function (data) {
            if (data != "") {
                $("#gender_sale_POI").empty();
                $("#gender_sale_POI").append(
                        $("<option></option>").attr("value", 0).text(
                        $.i18n("gen-please-select")));
                jQuery.each(genderStatus_R, function (i, obj1) {
                    var displayName = obj1.gender;
                    if (Global.LANG === "en") {
                        displayName = obj1.gender_en;
                    }
                    $("#gender_sale_POI").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
                });
                $('#firstname_sale_poi').val(data.firstName);
                $('#middlename_sale_poi').val(data.middleName);
                $('#lastname_sale_poi').val(data.lastName);
                $('#gender_sale_POI').val(data.gender);
                $('#date_Of_birthPOI_sale').val(data.dob);
                $('#address_sale_poi').val(data.address);
                $('#poi_id_number_sale').val(data.identityno);
                $('#leaseepoiid').val(id);
            }
        }
    });
}

function loadResPersonsOfEditingForEditing() {
    if (edit == 0) {

        $.ajax({
            type: "GET",
            url: "landrecords/landPOIBuyer/" + selectedlandid + "/" + processid,
            data: filter,
            success: function (data) {
                $('#BuyerPOIRecordsRowDataSale').empty();
                $("#gender_sale_POI").empty();
                $("#gender_sale_POI").append(
                        $("<option></option>").attr("value", 0).text(
                        $.i18n("gen-please-select")));
                jQuery.each(genderStatus_R, function (i, obj1) {
                    var displayName = obj1.gender;
                    if (Global.LANG === "en") {
                        displayName = obj1.gender_en;
                    }
                    $("#gender_sale_POI").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
                });


                if (null != data || data != "") {
                    for (var i = 0; i < data.length; i++) {
                        var gender = "";
                        for (var k = 0; k < genderStatus_R.length; k++) {
                            if (genderStatus_R[k].genderId == data[i].gender) {
                                gender = genderStatus_R[k].gender;
                            }
                        }

                        data[i].gender = gender;
                        $('#BuyerPOIRecordsAttrTemplateSale').tmpl(data[i]).appendTo('#BuyerPOIRecordsRowDataSale');
                        $("#BuyerPOIRecordsRowDataSale").i18n();
                    }
                }
            }
        });

    } else if (edit == 1) {

        jQuery.ajax({
            type: "GET",
            url: "landrecords/landPOI/" + finaltransid + "/" + landid,
            async: false,
            success: function (data) {
                $("#BuyerPOIRecordsRowDataSale").empty();
                $("#POIRecordsRowDataMortgage1").empty();
                $("#BuyerPOIRecordsRowDataLease").empty();

                if (null != data || data != "") {
                    for (var i = 0; i < data.length; i++) {

                        var gender = "";

                        for (var k = 0; k < genderStatus_R.length; k++) {
                            if (genderStatus_R[k].genderId == data[i].gender) {
                                gender = genderStatus_R[k].gender;
                            }
                        }

                        data[i].gender = gender;
                        $("#BuyerPOIRecordsAttrTemplateSale").tmpl(data[i]).appendTo("#BuyerPOIRecordsRowDataSale");
                        $("#BuyerPOIRecordsRowDataSale").i18n();
                        $("#POIRecordsAttrTemplateMortgage1").tmpl(data[i]).appendTo("#POIRecordsRowDataMortgage1");
                        $("#BuyerPOIRecordsAttrTemplateLease").tmpl(data[i]).appendTo("#BuyerPOIRecordsRowDataLease");
                        $("#BuyerPOIRecordsRowDataLease").i18n();
                    }
                }
            }
        });
    }
}

function saveattributesSalePOIData() {
    if (arry_Buyerbyprocessid == null || arry_Buyerbyprocessid.length < 1) {
        jAlert($.i18n("err-add-new-owner"), $.i18n("err-alert"));
        return false;
    }

    if ($("#editprocessAttributeformID").valid()) {

        var errors = "";
        if (firstname_sale_poi.value.length == 0) {
            errors += "- " + $.i18n("err-select-firstname") + "\n";
        }
        if (lastname_sale_poi.value.length == 0) {
            errors += "- " + $.i18n("err-select-lastname") + "\n";
        }
        if (date_Of_birthPOI_sale.value.length == 0) {
            errors += "- " + $.i18n("err-enter-dob") + "\n";
        }
        if (address_sale_poi.value.length == 0) {
            errors += "- " + $.i18n("err-enter-address") + "\n";
        }
        if (poi_id_number_sale.value.length == 0) {
            errors += "- " + $.i18n("err-enter-idnumber") + "\n";
        }

        if (errors !== "") {
            jAlert(errors, $.i18n("err-alert"));
            return false;
        }

        if (edit == 0) {
            $.ajax({
                type: "POST",
                traditional: true,
                url: "landrecords/saveRegPersonOfInterestForEditing/" + selectedlandid + "/" + processid,
                data: $("#editprocessAttributeformID").serialize(),
                async: false,
                success: function (data) {
                    if (null != data && data != "") {
                        $("#gender_sale_POI").empty();
                        $("#gender_sale_POI").append(
                                $("<option></option>").attr("value", 0).text(
                                $.i18n("gen-please-select")));
                        jQuery.each(genderStatus_R, function (i, obj1) {
                            var displayName = obj1.gender;
                            if (Global.LANG === "en") {
                                displayName = obj1.gender_en;
                            }
                            $("#gender_sale_POI").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
                        });
                        $('#firstname_sale_poi').val("");
                        $('#middlename_sale_poi').val("");
                        $('#lastname_sale_poi').val("");
                        $('#poi_id_number_sale').val("");
                        $('#address_sale_poi').val("");
                        $('#date_Of_birthPOI_sale').val("");
                        $('#leaseepoiid').val("");

                        jAlert($.i18n("reg-poi-added"));
                        loadResPersonsOfEditingForEditing();
                    }
                }
            });
        } else if (edit == 1) {
            $.ajax({
                type: "POST",
                traditional: true,
                url: "landrecords/saveRegPersonOfInterestForEditing/" + selectedlandid + "/" + finaltransid,
                data: $("#editprocessAttributeformID")
                        .serialize(),
                async: false,
                success: function (data) {
                    if (null != data && data != "") {
                        $("#gender_sale_POI").empty();
                        $("#gender_sale_POI").append(
                                $("<option></option>").attr("value", 0).text(
                                $.i18n("gen-please-select")));
                        jQuery.each(genderStatus_R, function (i, obj1) {
                            var displayName = obj1.gender;
                            if (Global.LANG === "en") {
                                displayName = obj1.gender_en;
                            }
                            $("#gender_sale_POI").append($("<option></option>").attr("value", obj1.genderId).text(displayName));
                        });
                        $('#firstname_sale_poi').val("");
                        $('#middlename_sale_poi').val("");
                        $('#lastname_sale_poi').val("");
                        $('#poi_id_number_sale').val("");
                        $('#address_sale_poi').val("");
                        $('#date_Of_birthPOI_sale').val("");
                        $('#leaseepoiid').val("");

                        jAlert($.i18n("reg-poi-updated"));
                        loadResPersonsOfEditingForEditing();
                    }
                }
            });
        }
    }
}

function deleteSalePOI(poiId) {
    jConfirm($.i18n("gen-delete-confirmation"), $.i18n("gen-confirmation"),
            function (response) {
                if (response) {
                    jQuery.ajax({
                        type: "GET",
                        url: "landrecords/deletepOI/" + poiId,
                        success: function (result) {
                            jAlert($.i18n("reg-poi-deleted"));
                            loadResPersonsOfEditingForEditing();
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            jAlert($.i18n("err-request-not-completed"));
                        }
                    });
                }
            });
}

function salebuyerdetails(id) {
    $("#newOwnerRecordsRowDataSale").empty();
    $("#RowLegalBuyerInfo").empty();
    $("#tblNewPersonBuyer").hide();
    $("#tblPersonBuyerInfo").hide();
    $("#tblNewLegalBuyer").hide();
    $("#tblLegalBuyerInfo").hide();

    if (!$("#saleLandDetailsContainer #pnlLandDetails").length) {
        $("#saleLandDetailsContainer").empty();
        $("#pnlLandDetails").appendTo($("#saleLandDetailsContainer"));
    }

    fillLandDetails();
    $("#savebuyer").hide();
    $("#divBuyerType").hide();

    if (finalbuyerarray.length > 0) {
        for (var i = 0; i < finalbuyerarray.length; i++) {
            var isPerson = finalbuyerarray[i].hasOwnProperty("firstname");
            var personid;

            if (isPerson) {
                $("#tblNewPersonBuyer").show();
                $("#tblPersonBuyerInfo").show();
                personid = finalbuyerarray[i].personid;
            } else {
                $("#tblNewLegalBuyer").show();
                $("#tblLegalBuyerInfo").show();
                personid = finalbuyerarray[i].partyid;
            }

            jQuery.ajax({
                type: "GET",
                url: "registration/salebuyerdetails/" + personid + "/" + landid,
                async: false,
                success: function (data) {
                    if (data == id) {
                        if (isPerson) {
                            $("#newOwnerRecordsAttrTemplateSale").tmpl(finalbuyerarray[i]).appendTo("#newOwnerRecordsRowDataSale");
                            $("#newOwnerRecordsRowDataSale").i18n();
                        } else {
                            $("#LegalEntityEditTableTemplate").tmpl(finalbuyerarray[i]).appendTo("#RowLegalBuyerInfo");
                            $("#RowLegalBuyerInfo").i18n();
                        }
                    }
                }
            });
        }
    } else {
        $("#divBuyerType").show();
        $("#savebuyer").text($.i18n("gen-add"));
    }
}

function showAddPersonBuyer() {
    $("#divBuyerType").hide();
    $("#savebuyer").show();

    $("#tblNewPersonBuyer").show();
    $("#tblPersonBuyerInfo").show();
    $("#tblNewLegalBuyer").hide();
    $("#tblLegalBuyerInfo").hide();
}

function showAddNonNaturalBuyer() {
    $("#divBuyerType").hide();
    $("#savebuyer").show();

    $("#tblNewPersonBuyer").hide();
    $("#tblPersonBuyerInfo").hide();
    $("#tblNewLegalBuyer").show();
    $("#tblLegalBuyerInfo").show();
}

function documentEditFetch() {
    jQuery.ajax({
        type: 'GET',
        url: "registryrecords/editDocuments/" + selectedlandid + "/" + finaltransid,
        async: false,
        success: function (data) {
            if (data != null && data !== "")
            {
                $("#salesDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#salesDocRowData");
                $("#salesDocRowData").i18n();

                $("#LeaseDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#LeaseDocRowData");
                $("#LeaseDocRowData").i18n();

                $("#MortagageDocRowData").empty();
                $("#salesdocumentTemplate_add").tmpl(data).appendTo("#MortagageDocRowData");
                $("#MortagageDocRowData").i18n();
            }
        }
    });
}