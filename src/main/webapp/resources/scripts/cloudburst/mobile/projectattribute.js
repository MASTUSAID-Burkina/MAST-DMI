
var selectedItem = null;
var ProjectCategoryList = null;
var DataList = null;
var attributeCategory = null;
var proj_name = null;
var projname = null;
var Mobilelist = null;
var idorder = [];
var AttributeCategorytypeList = null;

function ProjectAttribute(_selectedItem)
{

    proj_name = null;
    selectedItem = _selectedItem;


    jQuery.ajax({
        url: "projecttype/",
        async: false,
        success: function (data) {
            DataList = data;
        }
    });


    jQuery.ajax({
        url: "attribcategoryType/",
        async: false,
        success: function (data) {
            AttributeCategorytypeList = data;

        }
    });


    displayRefreshedProjectAttr("", "", "");
    $('body').find("#attr-dialog-form").remove();
}

function displayRefreshedProjectAttr(_saveType, _catType, _project)
{

    jQuery("#projectattribute-div").empty();
    jQuery.get("resources/templates/mobile/" + selectedItem + ".html", function (template)
    {

        jQuery("#projectattribute-div").append(template);
        jQuery("#projectattribute-div").i18n();

        jQuery('#projectAttrFormDiv').css("visibility", "visible");


        jQuery("#projectAttr_project").empty();



        jQuery("#projectAttr_project").append(jQuery("<option></option>").attr("value", 0).text($.i18n("gen-please-select")));
        jQuery.each(DataList, function (i, _project) {
            jQuery("#projectAttr_project").append(jQuery("<option></option>").attr("value", _project.name).text(_project.name));
        });


        jQuery.each(AttributeCategorytypeList, function (i, _categoryTypeobj) {
            jQuery("#category_type").append(jQuery("<option></option>").attr("value", _categoryTypeobj.categorytypeid).text(_categoryTypeobj.typename));

        });



        $('#projectAttr_project').val(proj_name);

        jQuery("#projectAttr-accordion").accordion({collapsible: true});
        jQuery("#projectAttr-accordion-resource").accordion({collapsible: true});
        jQuery("#projectAttr-accordion").hide();
        jQuery("#projectAttr-accordion-resource").hide();

        if (_saveType == "save")
        {
            if (_catType == 1) {
                $("#category_type").val(1);
                jQuery("#projectAttr-accordion").show();
                jQuery("#projectAttr-accordion-resource").hide();
            }



            if (_catType == 2) {
                $("#category_type").val(2);
                jQuery("#projectAttr-accordion").hide();
                jQuery("#projectAttr-accordion-resource").show();
            }

            if (_project != "")
            {
                $('#projectAttr_project').val(_project);
            }




        }




        if (proj_name != null)
            fillAccordians(proj_name);

    });


}

function fillAccordians(_proj_name)
{
    proj_name = _proj_name;


    projname = $('#projectAttr_project').val();
    if (projname == 0)
    {

        jQuery(".btnprojectattr").hide();
        jQuery("#generaltbl").empty();
        jQuery("#general_property_tbl").empty();
        jQuery("#natural_persontbl").empty();
        jQuery("#multimediatbl").empty();
        jQuery("#tenuretbl").empty();
        jQuery("#non_Natural_Persontbl").empty();
        jQuery("#customtbl").empty();
    } else {
        jQuery(".btnprojectattr").show();

        var _catType = $("#category_type").val()
        if (_catType == "")
        {
            jAlert($.i18n("mob-select-cat-type"), $.i18n("err-alert"));
        } else
        {
            if (_catType == 1) {

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/1/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#generaltbl").empty();

                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            var ab = jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#generaltbl");


                    }
                });
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/2/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#natural_persontbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#natural_persontbl");

                        //

                        jQuery("#upLayer").live('click', function () {
                            jQuery('#addedLayerList option:selected').each(function () {
                                var newPos = jQuery('#addedLayerList option').index(this) - 1;
                                if (newPos > -1) {
                                    jQuery('#addedLayerList option').eq(newPos).before("<option value='" + jQuery(this).val() + "' selected='selected'>" + jQuery(this).text() + "</option>");
                                    jQuery(this).remove();
                                }
                            });
                        });


                        jQuery("#downLayer").live('click', function () {
                            var countOptions = jQuery('#addedLayerList option').size();
                            jQuery('#addedLayerList option:selected').each(function () {
                                var newPos = jQuery('#addedLayerList option').index(this) + 1;
                                if (newPos < countOptions) {
                                    jQuery('#addedLayerList option').eq(newPos).after("<option value='" + jQuery(this).val() + "' selected='selected'>" + jQuery(this).text() + "</option>");
                                    jQuery(this).remove();
                                }
                            });
                        });

                        //

                    }
                });
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/3/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#multimediatbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#multimediatbl");
                    }
                });
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/4/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#tenuretbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#tenuretbl");
                    }
                });
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/5/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#non_Natural_Persontbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#non_Natural_Persontbl");
                    }
                });

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/6/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#customtbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#customtbl");
                    }
                });



                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/7/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        console.log(categorydata);
                        jQuery("#general_property_tbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#general_property_tbl");
                    }
                });

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/8/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#addPOItbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#addPOItbl");
                    }
                });

            } else if (_catType == 2) {


                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/9/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#opentbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#opentbl");
                    }
                });

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/10/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#privatetbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#privatetbl");
                    }
                });

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/11/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#commontbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#commontbl");
                    }
                });


                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/12/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#communitytbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#communitytbl");
                    }
                });


                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/13/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#public_statetbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#public_statetbl");
                    }
                });

                //  Private (jointly)

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/17/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#private_jointlytbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#private_jointlytbl");
                    }
                });

                // Organization (informal)

                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/18/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#organization_informaltbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#organization_informaltbl");
                    }
                });


                // Organization (formal)



                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/14/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#organization_formaltbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#organization_formaltbl");
                    }
                });


                // Collective


                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/display/11/" + proj_name + "/",
                    success: function (categorydata)
                    {
                        jQuery("#collectivetbl").empty();
                        if (categorydata != null && categorydata != "" && typeof categorydata != "undefined")
                            jQuery("#categoryattrTemplate").tmpl(categorydata).appendTo("#collectivetbl");
                    }
                });






            }



        }



    }



}


function displayCategoryType(_this)
{

    $("#projectAttr_project")[0].selectedIndex = 0;

    var _id = _this.value;

    if (_id == 1) {

        jQuery("#projectAttr-accordion").show();
        jQuery("#projectAttr-accordion-resource").hide();

    } else if (_id == 2) {

        jQuery("#projectAttr-accordion").hide();
        jQuery("#projectAttr-accordion-resource").show();
    }
}

function openAttrDialog(_category)
{
    var category = _category;
    switch (category) {
        case "general":
            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("destroy");
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");

                }
            });



            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));

            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/1/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();

                        jQuery("#generalAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 1;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "naturalperson":
            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("destroy");
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");

                }
            });



            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/2/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#naturalpersonAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 2;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "multimedia":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("destroy");
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                }
            });



            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/3/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#propertyAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 3;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "tenure":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/4/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#tenureAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 4;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "nonnatural_person":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("destroy");
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/5/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#multimediaAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 5;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "custom":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/6/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 6;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        case "general_property":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/7/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 7;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;


        case "poi":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/8/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 16;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;
        case "open":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/9/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 9;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;

        case "private":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/10/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 10;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;

        case "common":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/11/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 7;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;
        case "community":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/12/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 12;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;
        case "public_state":

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/13/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 13;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;
        case "private_jointly": // private_jointly

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/17/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 17;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }

            break;
        case "organization_formal": // organization_informal

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/14/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 14;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;
        case "collective": // collective

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/11/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 11;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;
        case "organization_informal": // organization_informal

            genAttrDialog = $("#attr-dialog-form").dialog({
                title: $.i18n("gen-attribute"),
                autoOpen: false,
                height: 400,
                width: 300,
                resizable: false,
                modal: true,
                buttons: [{
                        text: $.i18n("gen-add"),
                        click: function ()
                        {
                            saveprojectAttr();
                            //genAttrDialog.dialog( "close" );
                        }}, {
                        text: $.i18n("gen-cancel"),
                        click: function ()
                        {
                            genAttrDialog.dialog("close");
                        }
                    }],
                close: function () {
                    genAttrDialog.dialog("destroy");
                    genAttrDialog.dialog("destroy");

                }
            });


            var project = $("#projectAttr_project").val();
            if (project == 0) {
                jQuery("#dialogBody").empty();
                jAlert($.i18n("err-select-proj-first"), $.i18n("err-alert"));
            } else {
                genAttrDialog.dialog("open");
                jQuery.ajax({
                    type: 'GET',
                    url: "projectattrib/displaypop/18/" + project + "/",
                    success: function (popdata)
                    {
                        jQuery("#dialogBody").empty();
                        jQuery("#customAttrTemplate").tmpl(popdata).appendTo("#dialogBody");
                        attributeCategory = 18;
                        $('.roleCheckbox').change(function ()
                        {
                            console.log(this);
                            if (this.checked) {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', true);
                            } else {
                                var chkid = this.id;
                                $('#uid' + chkid).prop('checked', false);
                            }
                        });
                    }
                });
            }
            break;

        default:
    }
}


var saveprojectAttrData = function ()
{
    $('.funcCheckbox').show();
    var project = $("#projectAttr_project").val();
    $("#project").val(project);
    $('#attributecategory').val(attributeCategory);


    jQuery.ajax({
        type: "POST",
        url: "projectattrib/create/",
        data: jQuery("#addProjectAttributeformID").serialize(),
        success: function (result) {

            if (result == 'true') {
                genAttrDialog.dialog("destroy");
                $('body').find("#attr-dialog-form").remove();
                $("#attr-dialog-form").remove();
                proj_name = project;
                var _catType = $("#category_type").val();
                displayRefreshedProjectAttr("save", _catType, proj_name);
                jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
            } else if (result == 'false') {
                jAlert($.i18n("err-not-saved"), $.i18n("err-error"));
            } else if (result == 'mapping')
            {
                jAlert($.i18n("err-attr-cant-unmap"), $.i18n("gen-info"));
            } else if (result == 'null')
            {
                jAlert($.i18n("err-select-one-attr"), $.i18n("gen-info"));
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert('err.Message');
        }
    });
    $('.funcCheckbox').hide();
}




function saveprojectAttr()
{
    /* $("#addProjectAttributeformID").validate({
     rules: {
     alias: "required",
     fieldName: "required",
     type: "required",
     size: "required",
     category: "required",
     
     },
     messages: {
     alias: "Please enter Alias Name",
     fieldName: "Please enter  Field Name",
     type: "Please enter Attribute Type",
     size: "Please enter Attribute Size",
     category: "Please enter  Category",
     
     }
     
     });
     
     if ($("#addProjectAttributeformID").valid())
     {*/
    saveprojectAttrData();

    //}  
}


//added to up down for save  row by  RMSI NK  start

//case 1-6
function saveLayergroup(_option)
{

    var _attributecategory = 0;

    idorder = [];
    $('#_optionOrder').val('');



    if (_option == 'general') {


        $('#generaltbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);


        });

        _attributecategory = 1;

        $("#attributecategory").val(_attributecategory);




    }

    // case no 2 for natural personstart
    else if
            (_option == 'naturalperson') {

        $('#natural_persontbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 2;

        $("#attributecategory").val(_attributecategory);

    }

    // case no 2 for natural person end	


    // case no 3 for natural multimedia
    else if
            (_option == 'multimedia') {

        $('#multimediatbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 3;

        $("#attributecategory").val(_attributecategory);

    }

    // case no 3 for multimedian end	


    // case no 4 for tenure  start
    else if
            (_option == 'tenure') {

        $('#tenuretbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 4;

        $("#attributecategory").val(_attributecategory);

    }

    // case no 4 for tenure  end

    // case no 5 for nonnatural_person start
    else if
            (_option == 'nonnatural_person') {

        $('#non_Natural_Persontbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 5;

        $("#attributecategory").val(_attributecategory);

    }

    // case no 5 for nonnatural_person end

    // case no 6 for custom start
    else if
            (_option == 'custom') {

        $('#customtbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 6;

        $("#attributecategory").val(_attributecategory);

    } else if
            (_option == 'add_poi') {

        $('#addPOItbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 8;

        $("#attributecategory").val(_attributecategory);

    }

    // case no 6 for custom end
//case no 7 for general(property) start
    else if (_option == 'general_property') {

        $('#general_property_tbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 7;

        $("#attributecategory").val(_attributecategory);

    }


    //case no 7 for general(property) end

    else if (_option == 'open') {

        $('#opentbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 9;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'private') {

        $('#privatetbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 10;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'common') {

        $('#commontbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 11;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'community') {

        $('#communitytbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 12;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'public_state') {

        $('#public_statetbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 13;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'private_jointly') {   //  Private (jointly)

        $('#private_jointlytbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 17;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'organization_informal') {  // Organization (informal)

        $('#organization_informaltbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 18;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'organization_formal') {  // Organization (formal)

        $('#organization_formal tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 14;

        $("#attributecategory").val(_attributecategory);

    } else if (_option == 'collective') {  // Collective 

        $('#collectivetbl tr').each(function () {
            var test = $(this).find("td:first").attr("id");
            idorder.push(test);

            $("#_optionOrder").val(idorder);

        });

        _attributecategory = 11;

        $("#attributecategory").val(_attributecategory);

    }


    var project = $("#projectAttr_project").val();

    $("#project").val(project);

    jQuery.ajax({
        type: "POST",
        url: "projectattrib/update/",
        data: jQuery("#addProjectAttributeformID").serialize(),
        success: function (result) {

            if (result) {
                jAlert($.i18n("gen-data-saved"), $.i18n("gen-info"));
                fillAccordians(project);

            } else {
                alert($.i18n("err-not-saved"));
            }


            /*genAttrDialog.dialog("destroy");
             $('body').find("#attr-dialog-form").remove();
             $("#attr-dialog-form").remove();
             fillAccordians(project);*/

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {

            alert('err.Message');
        }
    });


    // add code end
    //alert("successfully save");



}


//for save up and down start
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
//for save up and down start