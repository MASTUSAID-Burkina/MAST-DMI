var selectedItem = null;
var records_from = 0;
var searchRecords = null;
var totalRecords = null;
var communeList = null;
var provinceList = null;

function Bookmark(_selectedItem) {
    selectedItem = _selectedItem;

    if (jQuery("#bookmarkFormDiv").length <= 0) {
        displayRefreshedVillage();
    } else {

        displayVillage();
    }
}

function displayRefreshedVillage() {
    records_from = 0;
    jQuery.ajax({
        type: "POST",
        async: false,
        url: "village/search/count/",
        data: jQuery("#villageForm").serialize(),
        success: function (result)
        {
            totalRecords = result;
            searchRecords = result;
        }
    });

    $("#village_txtSearch").val("");

    jQuery.ajax({
        type: "POST",
        url: "village/search/" + records_from,
        data: jQuery("#villageForm").serialize(),
        success: function (data) {
            jQuery("#tableGrid").empty();
            jQuery("#bookmarks").empty();
            jQuery.get("resources/templates/studio/" + selectedItem + ".html", function (template) {

                jQuery("#bookmarks").append(template);
                jQuery("#bookmarks").i18n();

                jQuery('#bookmarkFormDiv').css("visibility", "visible");

                jQuery("#bookmarkDetails").hide();
                jQuery("#BookmarksRowData").empty();
                jQuery("#bookmarkTable").show();

                jQuery("#bookmark_accordion").hide();

                jQuery("#BookmarkTemplate").tmpl(data).appendTo("#BookmarksRowData");
                $("#BookmarksRowData").i18n();

                jQuery("#bookmark_btnSave").hide();
                jQuery("#bookmark_btnBack").hide();
                jQuery("#bookmark_btnNew").show();

                jQuery("#records_from").val(records_from + 1);
                jQuery("#records_to").val(records_from + 10);
                jQuery('#records_all').val(totalRecords);


                $("#bookmarkTable").trigger("update");
                $("#bookmarkTable").tablesorter({sortList: [[0, 1], [1, 0]]});

            });
        }
    });
}

function displayVillage() {
    $("#village_txtSearch").val("");
    jQuery("#bookmark_accordion").hide();
    jQuery("#bookmarkDetails").hide();
    jQuery("#bookmarkTable").show();
    jQuery("#bookmark_btnSave").hide();
    jQuery("#bookmark_btnBack").hide();
    jQuery("#bookmark_btnNew").show();
}

var createEditVillage = function (_name) {
    jQuery("#bookmark_btnNew").hide();
    jQuery("#bookmark_btnSave").hide();
    jQuery("#bookmark_btnBack").hide();
    jQuery("#bookmarkTable").hide();
    jQuery("#bookmarkDetails").show();
    jQuery("#bookmarkDetailsBody").empty();

    jQuery.ajax({
        url: "AllProvince/",
        success: function (data) {
            provinceList = data;
        },
        async: false
    });

    if (_name) {
        jQuery.ajax({
            url: "village/" + _name + "?" + token,
            async: false,
            success: function (data) {

                jQuery("#BookmarkTemplateForm").tmpl(data, {}).appendTo("#bookmarkDetailsBody");
                $("#bookmarkDetailsBody").i18n();
                jQuery('#name').attr('readonly', true);
                jQuery.each(provinceList, function (i, province) {
                    jQuery("#province_id").append(jQuery("<option></option>").attr("value", province.hierarchyid).text(province.name));
                });
                jQuery("#province_id").val(data.provinceId);

                getCommunes(data.provinceId);
                jQuery("#commmune_id").val(data.communeId);
            },
            cache: false
        });
    } else {
        jQuery("#BookmarkTemplateForm").tmpl(null, {}).appendTo("#bookmarkDetailsBody");
        $("#bookmarkDetailsBody").i18n();
        jQuery.each(provinceList, function (i, province) {
            jQuery("#province_id").append(jQuery("<option></option>").attr("value", province.hierarchyid).text(province.name));
        });
    }

    jQuery("#bookmark_accordion").show();
    jQuery("#bookmark_accordion").accordion({fillSpace: true});

    jQuery("#bookmark_btnSave").show();
    jQuery("#bookmark_btnBack").show();
};

var saveVillageData = function () {

    jQuery.ajax({
        type: "POST",
        url: "village/create" + "?" + token,
        data: jQuery("#villageForm").serialize(),
        success: function (result) {
            if (result == 'true')
            {
                jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
                displayRefreshedVillage();
            } else
            {
                jAlert('Error ', 'Village');
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert('err.Message');
        }
    });

}

function saveVillage() {

    jQuery("#villageForm").validate({
        rules: {
            nameEn: "required",
            nameFR: "required",
            cfV_agent: "required",
            VillageCode: "required",
            "province_id": "required",
            "commmune_id": "required"

        },
        messages: {
            nameEn: $.i18n("err-enter-name"),
            nameFR: $.i18n("err-enter-name-fr"),
            cfV_agent: $.i18n("err-enter-cfv-agent"),
            VillageCode: $.i18n("err-enter-village-code"),
            "province_id": $.i18n("err-select-province"),
            "commmune_id": $.i18n("err-select-commune")
        }
    });

    if (jQuery("#villageForm").valid()) {
        saveVillageData();
    }


}
function search(records_from) {
    jQuery.ajax({
        type: "POST",
        async: false,
        url: "village/search/count/",
        data: jQuery("#villageForm").serialize(),
        success: function (result)
        {
            searchRecords = result;
        }
    });

    var villageName = $('#village_txtSearch').val();

    if (villageName == '') {
        jAlert($.i18n("err-select-search-criteria"), $.i18n("err-alert"));
    } else {
        jQuery.ajax({
            type: "POST",
            async: false,
            url: "village/search/" + records_from,
            data: jQuery("#villageForm").serialize(),
            success: function (data)
            {
                if (data != null && data != "" && data != undefined) {
                    if (records_from + 10 <= searchRecords)
                    {
                        $('#records_to').val(records_from + 10);
                        $('#records_all').val(searchRecords);
                        $("#bookmarkTable").trigger("update");
                        $("#bookmarkTable").tablesorter({sortList: [[0, 1], [1, 0]]});
                    }

                    jQuery("#bookmarkDetails").hide();
                    jQuery("#BookmarksRowData").empty();
                    jQuery("#bookmarkTable").show();
                    jQuery("#bookmark_accordion").hide();
                    jQuery("#BookmarkTemplate").tmpl(data).appendTo("#BookmarksRowData");
                    jQuery("#bookmark_btnSave").hide();
                    jQuery("#bookmark_btnBack").hide();
                    jQuery("#bookmark_btnNew").show();
                    jQuery('#records_from').val(records_from + 1);
                    $('#records_to').val(searchRecords);
                } else {
                    jAlert($.i18n("err-no-records"));
                }
            }
        });
    }

}
var deleteVillage = function (_villageId) {

    jConfirm($.i18n("gen-delete-confirmation"), $.i18n("gen-confirm-delete"), function (response) {

        if (response) {
            jQuery.ajax({
                url: "village/delete/" + _villageId + "?" + token,
                success: function () {
                    jAlert($.i18n("gen-data-deleted"), $.i18n("gen-info"));
                    displayRefreshedVillage();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('err.Message');
                }
            });
        }
    });
};

function previousRecords() {
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    records_from = records_from - 11;

    if (records_from >= 0)
    {
        if (searchRecords !== null)
        {
            spatialSearch(records_from);
        } else {
            spatialSearch(records_from);
        }
    } else {
        alert($.i18n("err-failed-handling-request"));
    }
}

function nextRecords()
{
    records_from = $('#records_from').val();
    records_from = parseInt(records_from);
    records_from = records_from + 9;

    if (records_from <= totalRecords - 1)
    {
        if (searchRecords != null)
        {
            if (records_from <= searchRecords - 1)
                spatialSearch(records_from);
            else
                alert($.i18n("err-failed-handling-request"));
        } else {
            spatialSearch(records_from);
        }
    } else
    {
        alert($.i18n("err-failed-handling-request"));
    }

}

function spatialSearch(records_from)
{
    jQuery.ajax({
        type: "POST",
        async: false,
        url: "village/search/" + records_from,
        data: jQuery("#villageForm").serialize(),
        success: function (data)
        {
            jQuery("#bookmarkDetails").hide();
            jQuery("#BookmarksRowData").empty();
            jQuery("#bookmarkTable").show();
            jQuery("#bookmark_accordion").hide();
            jQuery("#BookmarkTemplate").tmpl(data).appendTo("#BookmarksRowData");
            jQuery("#bookmark_btnSave").hide();
            jQuery("#bookmark_btnBack").hide();
            jQuery("#bookmark_btnNew").show();
            $('#records_from').val(records_from + 1);
            $('#records_to').val(searchRecords);
            if (records_from + 10 <= searchRecords)
                $('#records_to').val(records_from + 10);
            $('#records_all').val(searchRecords);
            $("#bookmarkTable").trigger("update");
            $("#bookmarkTable").tablesorter({sortList: [[0, 1], [1, 0]]});

        }
    });
}

function getCommunes(provinceId)
{
    if (provinceId != '0') {
        jQuery("#commmune_id").empty();
        jQuery("#commmune_id").append(jQuery("<option></option>").attr("value", "").text($.i18n("gen-please-select")));

        jQuery.ajax({
            url: "communesbyprovince/" + provinceId,
            async: false,
            success: function (Communedata) {
                var proj_commune = Communedata;
                jQuery.each(proj_commune, function (i, value) {
                    jQuery("#commmune_id").append(jQuery("<option></option>").attr("value", value.hierarchyid).text(value.name));

                });
            }
        });
    }
}
