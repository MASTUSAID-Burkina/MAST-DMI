var form1Obj = null;
var form2attributeObject = null;
var poiList = null;
var URL = null;
var URL2 = null;
var establishmentDate = null;
var areaMapUrl = null;
var usinId = null;
var usin_key = null;

function generateform1(usin, id) {
    var generateForm = new generateForms();
    form1Obj = generateForm.Form1(usin);
    var formImage = getFormImage();

    var buildReport = function (mapUrl) {
        jQuery.ajax({
            type: 'GET',
            url: URL,
            dataType: 'html',
            success: function (data1)
            {
                jQuery("#printDiv div").empty();
                jQuery("#printDiv").append(data1);
                jQuery("#printDiv").i18n();
                
                var generateForm = new generateForms();
                var form1Obj = generateForm.Form1(usin);
                removeNulls(form1Obj);
                
                $('.commune_logo').append("<img width='125' height='100' src='" + formImage + "'>");
                $('#region_1').text(form1Obj.region);
                $('#province_1').text(form1Obj.province);
                $('.commune_1').text(form1Obj.commune);
                $('.village_1').text(form1Obj.village);
                //$('#villageno_1').text(form1attributeObject.villageno);
                $('.application_no1').text(form1Obj.applicationno);
                $('.application_date1').text(form1Obj.dateofapplication);
                $('.person_lastname1').text(form1Obj.lastname);
                $('.person_firstname1').text(form1Obj.firstname);
                $('#birthdate1').text(form1Obj.birthdate);
                $('#birthplace1').text(form1Obj.birthplace);
                $('#profession_1').text(form1Obj.profession);
                $('.person_address1').text(form1Obj.address);
                $('#refrenceId_1').text(form1Obj.referenceofId);
                $('#father_name1').text(form1Obj.fathername);
                $('#mother_name1').text(form1Obj.mothername);
                $('#marital_status1').text(form1Obj.maritialStatus);
                $('#nop1').text(form1Obj.natureofpower);
                $('#issuance_date1').text(form1Obj.issuancedate);
                //$('#location1').text(form1attributeObject.location);
                $('.parcel_location1').text(form1Obj.location);
                $('.area_1').text(((form1Obj.area) * area_constant).toFixed(2));
                $('.neighbour_north1').text(form1Obj.neighbour_north);
                $('.neighbour_east1').text(form1Obj.neighbour_east);
                $('.neighbour_south1').text(form1Obj.neighbour_south);
                $('.neighbour_west1').text(form1Obj.neighbour_west);
                //$('.noa_1').text(form1attributeObject.natureofapplication);  kamal 
                if (id === 2) {
                    $('.noa_1').text(form1Obj.natureofapplication);
                }
                $('#establishDate_1').text(form1Obj.idcardestablishment_date);
                $('#id_origin1').text(form1Obj.idcard_origin);
                $('#mandate_issuanceDate1').text(form1Obj.mandate_establishmentDate);
                $('#mandate_location1').text(form1Obj.mandate_location);
                $('#application_type1').text(form1Obj.typeoftenancy);

                if (form1Obj.tenancyId === 7)
                    $('#mandataire_1').text("requérant individuel");
                else if (form1Obj.tenancyId === 8)
                    $('#mandataire_1').text("mandataire");

                $('#user_CFV').text(form1Obj.cfvname);
                if (mapUrl !== "") {
                    jQuery('#areamap_url').append('<img  src=' + mapUrl + '>');
                }

                var printWindow = window.open('', 'form1' + usin, 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');
                var html = $("#printDiv").html();
                printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                        '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                        '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                        '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                printWindow.document.close();
                printWindow.focus();
            }
        });
    };

    if (id === 1) {
        URL = 'resources/templates/forms/form1.html';
        buildReport("");
    } else if (id === 2) {
        URL = 'resources/templates/forms/landrecord_individual.html';
        areaMapUrl = setAreaMap(usin, buildReport);
    }
}

function generateform2(usin, id) {
    var formImage = getFormImage();

    var buildReport = function (mapUrl) {
        jQuery.ajax(
                {
                    type: 'GET',
                    url: URL2,
                    dataType: 'html',
                    success: function (data1)
                    {
                        jQuery("#printDiv div").empty();
                        jQuery("#printDiv").append(data1);
                        jQuery("#printDiv").i18n();
                        
                        var fromTmp = new generateForms();
                        var form2Obj = fromTmp.Form2(usin);
                        removeNulls(form2Obj);

                        $('.commune_logo').append("<img width='125' height='100' src='" + formImage + "'>");
                        $('#region_2').text(form2Obj.region);
                        $('#province_2').text(form2Obj.province);
                        $('.commune_2').text(form2Obj.commune);
                        $('.village_2').text(form2Obj.village);
                        $('#familyname_2').text(form2Obj.family_name);
                        $('.mandate_date2').text(form2Obj.dateofMandate);
                        $('.mandate_commune').text(form2Obj.mandate_origin);
                        $('#person_lastname2').text(form2Obj.lastname);
                        $('#person_firstname2').text(form2Obj.firstname);
                        $('#birthdate_2').text(form2Obj.birthdate);
                        $('#birthplace_2').text(form2Obj.birthplace);
                        $('#refrenceId_2').text(form2Obj.refrenceID);
                        $('#idcard_date2').text(form2Obj.idEstablishDate);

                        jQuery("#poiTableRowData").empty();

                        poiList = [];
                        if (form2Obj.poiLst !== null && form2Obj.poiLst !== undefined && form2Obj.poiLst.length !== 0)
                        {
                            $('#family_address2').text(form2Obj.poiLst[0].address);
                            for (var i = 1; i <= form2Obj.poiLst.length; i++) {
                                var tempArray = [];
                                tempArray["sno"] = i;
                                tempArray["name"] = form2Obj.poiLst[i - 1].firstName + " " + form2Obj.poiLst[i - 1].lastName;
                                tempArray["refrenceId"] = form2Obj.poiLst[i - 1].idNumber;
                                tempArray["address"] = form2Obj.poiLst[i - 1].address;
                                poiList.push(tempArray);
                            }
                            jQuery("#poi_template").tmpl(poiList).appendTo("#poiTableRowData");
                        }

                        $('#user_CFV2').text(form2Obj.cfvname);
                        if (mapUrl !== "") {
                            jQuery('#areamap_url2').append('<img  src=' + mapUrl + '>');
                        }
                        
                        var printWindow = window.open('', 'form2' + usin, 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                        var html = $("#printDiv").html();
                        printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                                '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                                '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                                '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                        printWindow.document.close();
                        printWindow.focus();
                    }
                });
    };

    if (id === 1) {
        URL2 = 'resources/templates/forms/form2.html';
        buildReport("");
    } else if (id === 2) {
        URL2 = 'resources/templates/forms/landrecord_collective.html';
        areaMapUrl = setAreaMap(usin, buildReport);
    }
}

function generateform3(usin) {
    var formImage = getFormImage();
    jQuery.ajax(
            {
                type: 'GET',
                url: 'resources/templates/forms/form3.html',
                dataType: 'html',
                success: function (data1)
                {
                    jQuery("#printDiv div").empty();
                    jQuery("#printDiv").append(data1);

                    var fromTmp = new generateForms();
                    var form3obj = fromTmp.Form3(usin);

                    var str1 = "pour son propre compte";
                    var str2 = "pour le compte de la famille";
                    var str3 = "pour le compte de la famille";

                    var firstname_cfv = "";
                    var lastname_cfv = "";

                    if (cfv != null) {
                        var cfv = form3obj.cfv_president.split(" ");
                        firstname_cfv = cfv[0];
                        lastname_cfv = cfv[1];
                    }

                    var endDate = getCurrentDate();
                    var extendedDate = getExtendedDate();

                    $('.commune_logo').append("<img width='125' height='100' src='../../" + formImage + "'>");

                    $('#region3').text(form3obj.region);
                    $('#province3').text(form3obj.province);
                    $('.commune3').text(form3obj.commune);
                    $('.village3').text(form3obj.village);
                    $('#person_firstname3').text(form3obj.firstname);
                    $('#person_lastname3').text(form3obj.lastname);
                    $('#person_address3').text(form3obj.address);
                    $('#person_birthdate3').text(form3obj.dob);
                    $('#person_birthplace3').text(form3obj.birthplace);
                    if (form3obj.tennancytypeID == 7) {
                        $('#choice_ind3').text(str1);
                        $('#choice_col3').text(str2);
                        $('#choice_col3').css('textDecoration', 'line-through');
                    } else if (form3obj.tennancytypeID == 8) {
                        $('#choice_ind3').text(str1);
                        $('#choice_ind3').css('textDecoration', 'line-through');
                        $('#choice_col3').text(str3 + " " + form3obj.familyname);
                    }
                    $('#section_no3').text(form3obj.section_no);
                    $('#lotno3').text(form3obj.lot_no);
                    $('#parcel_no3').text(form3obj.parcel_no);
                    $('#area3').text(((form3obj.area) * area_constant).toFixed(2));
                    $('#neighbour_north3').text(form3obj.neighbor_north);
                    $('#neighbour_south3').text(form3obj.neighbor_south);
                    $('#neighbour_east3').text(form3obj.neighbor_east);
                    $('#neighbour_west3').text(form3obj.neighbor_west);
                    $('#public_notice_enddate3').text(endDate);
                    $('#cfv_lastname3').text(lastname_cfv);
                    $('#cfv_firstname3').text(firstname_cfv);
                    $('#extended_date').text(extendedDate);
                    if (form3obj.other_use == "null") {
                        $('#other_use3').text("");
                    } else {
                        $('#other_use3').text(form3obj.other_use);
                    }

                    $('#flag').text(form3obj.flag);


                    if (form3obj.existing_use != null) {
                        for (var i = 0; i < form3obj.existing_use.length; i++) {
                            jQuery("#existing_use-div").append("o " + form3obj.existing_use[i][2] + "<br>");
                        }
                    }
                    var printWindow = window.open('form3', 'popUpWindow', 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                    var html = null;
                    html = $("#printDiv").html();
                    printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                            '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                            '<script src="../resources/scripts/cloudburst/viewer/LandRecordTemp.js"></script>' +
                            '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                            '<script src="../resources/scripts/jquery-alert/jquery.alerts.js"></script>' +
                            ' <link rel="stylesheet" href="../resources/scripts/jquery-alert/jquery.alerts.css" type="text/css" />' +
                            '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                    printWindow.document.close();
                    printWindow.focus();
                }
            });
}


function generateform8(usin) {
    var formImage = getFormImage();

    jQuery.ajax(
            {
                type: 'GET',
                url: 'resources/templates/forms/form8.html',
                dataType: 'html',
                success: function (data1)
                {
                    jQuery("#printDiv div").empty();
                    jQuery("#printDiv").append(data1);

                    var fromTmp = new generateForms();
                    var form8obj = fromTmp.Form8(usin);
                    $('.commune_logo').append("<img width='125' height='100' src='../../" + formImage + "'>");
                    $('#region8').text(form8obj.region);
                    $('#province8').text(form8obj.province);
                    $('.commune8').text(form8obj.commune);
                    $('.village8').text(form8obj.village);
                    $('.village_no8').text(form8obj.village_no);
                    $('#apfrno8').text(form8obj.apfrno);
                    $('#apfrno_8').text(form8obj.apfrno);
                    $('.apfrdate8').text(form8obj.apfr_date);
                    $('.apfrdate_8').text(form8obj.apfr_date);  // kamal
                    $('.applicationdate8').text(form8obj.application_date);
                    $('.familyname8').text(form8obj.familyname);
                    $('#familyaddress8').text(form8obj.address);
                    $('.person_lastname8').text(form8obj.last_name);
                    $('.person_firstname8').text(form8obj.first_name);
                    $('#birthdate8').text(form8obj.dob);
                    $('#birthplace8').text(form8obj.birthplace);
                    $('#gender8').text(form8obj.sex);
                    $('#refrenceId8').text(form8obj.refrence_id_card);
                    $('#Idestablishmentdate8').text(form8obj.idcardEstbDate);
                    $('#IdestablishmentPlace8').text(" ");//to be updated
                    $('#person_profession8').text(form8obj.profession);
                    $('#person_address8').text(form8obj.address);
                    $('#village8').text(form8obj.village);
                    $('#area8').text(((form8obj.area) * area_constant).toFixed(2));
                    $('#neighnour_north8').text(form8obj.neighbour_north);
                    $('#neighbour_east8').text(form8obj.neighbour_east);
                    $('#neighbour_south8').text(form8obj.neighbour_south);
                    $('#neighbour_west8').text(form8obj.neighbour_west);
                    $('#mayor_name8').text(form8obj.mayor_name);
                    $('#pvnumber8').text(form8obj.pv_no);
                    $('#rightsDate8').text(form8obj.date_recognition_right);
                    $('#apfr_commune').text(form8obj.commune);
                    $('#mandateDate').text(form8obj.mandateDate);
                    $('#applicationno8').text(form8obj.application_no);
                    $('#mayor_firstname').text(form8obj.mayor_name);
                    if (form8obj.other_use == "0" || form8obj.other_use == "null" || form8obj.other_use == 0) {
                        $('#other_use8').text("");
                    } else {
                        $('#other_use8').text(form8obj.other_use);
                    }

                    if (form8obj.existing_use != null) {
                        for (var i = 0; i < form8obj.existing_use.length; i++) {
                            jQuery("#existing_use8").append("o " + form8obj.existing_use[i][2] + "<br>");
                        }
                    }

                    jQuery("#APFRTableRowData").empty();

                    APFRpoiList = [];
                    if (form8obj.poiLst != null)
                    {
                        for (var i = 1; i <= form8obj.poiLst.length; i++) {
                            var tempArray = [];
                            tempArray["sno"] = i;
                            tempArray["name"] = form8obj.poiLst[i - 1].personName + " " + form8obj.poiLst[i - 1].last_name;
                            tempArray["idcardref"] = form8obj.poiLst[i - 1].idcard_refrence;
                            tempArray["address"] = form8obj.poiLst[i - 1].address;
                            APFRpoiList.push(tempArray);
                        }


                        jQuery("#APFR_template").tmpl(APFRpoiList).appendTo("#APFRTableRowData");

                    }

                    var printWindow = window.open('form8', 'popUpWindow', 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                    var html = null;
                    html = $("#printDiv").html();
                    printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                            '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                            '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                            '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                    printWindow.document.close();
                    printWindow.focus();


                }
            });
}


function generateform7(usin) {
    var formImage = getFormImage();

    jQuery.ajax(
            {
                type: 'GET',
                url: 'resources/templates/forms/form7.html',
                dataType: 'html',
                success: function (data7)
                {
                    jQuery("#printDiv div").empty();
                    //$('#masterdiv div').empty();

                    jQuery("#printDiv").append(data7);

                    var fromTmp = new generateForms();
                    var form7attributeObject = fromTmp.Form7(usin);

                    $('.commune_logo').append("<img width='125' height='100' src='../../" + formImage + "'>");

                    $('#region_7').text(form7attributeObject.region);
                    $('#province_7').text(form7attributeObject.province);
                    $('.commune7').text(form7attributeObject.commune);
                    $('.village_7').text(form7attributeObject.village);
                    $('#villageno_7').text(form7attributeObject.village_no);
                    $('.familyname7').text(form7attributeObject.family_name);
                    $('.application_date7').text(form7attributeObject.application_date);
                    $('.application_no7').text(form7attributeObject.application_no);
                    $('#application_year7').text(form7attributeObject.application_year);
                    $('#application_dd7').text(form7attributeObject.application_dd);
                    $('#application_month7').text(form7attributeObject.application_month);
                    $('.name7').text(form7attributeObject.name);
                    $('.profession7').text(form7attributeObject.profession);
                    $('.address7').text(form7attributeObject.address);
                    $('#cfv_president7').text(form7attributeObject.cfv_president);
                    $('#area7').text(((form7attributeObject.area) * area_constant).toFixed(2));
                    $('#nr_north7').text(form7attributeObject.neighbour_north);
                    $('#nr_east7').text(form7attributeObject.neighbour_east);
                    $('#nr_south7').text(form7attributeObject.neighbour_south);
                    $('#nr_west7').text(form7attributeObject.neighbour_west);
                    $('#public_issue_date7').text(form7attributeObject.public_issuansedate);
                    $('#date_recognition7').text(form7attributeObject.date_recognition_rights);
                    $('#location').text(form7attributeObject.location);
                    $('#application_type7').text(form7attributeObject.application_type);

                    $('#pv_no7').text(form7attributeObject.pv_no);

                    jQuery("#poiTableRowData7").empty();

                    poiList = [];
                    if (form7attributeObject.poiLst != null && form7attributeObject.poiLst.length != undefined && form7attributeObject.poiLst.length != 0)
                    {
                        for (var i = 1; i <= form7attributeObject.poiLst.length; i++) {
                            var tempArray = [];
                            tempArray["sno"] = i;
                            tempArray["name"] = form7attributeObject.poiLst[i - 1].personName + " " + form7attributeObject.poiLst[i - 1].last_name;

                            poiList.push(tempArray);

                        }
                        //push neighbour north
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 1;
                        tempArray["name"] = form7attributeObject.neighbour_north;
                        poiList.push(tempArray);
                        //push neighbour south
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 2;
                        tempArray["name"] = form7attributeObject.neighbour_south;
                        poiList.push(tempArray);

                        //push neighbour east
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 3;
                        tempArray["name"] = form7attributeObject.neighbour_east;
                        poiList.push(tempArray);
                        //push neighbour west
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 4;
                        tempArray["name"] = form7attributeObject.neighbour_west;
                        poiList.push(tempArray);

                        for (var i = 1; i <= 4; i++) {
                            var tempArray1 = [];
                            tempArray1["sno"] = form7attributeObject.poiLst.length + 4 + i;
                            tempArray1["name"] = "";

                            poiList.push(tempArray1);

                        }

                    } else {
                        //push neighbour north
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 1;
                        tempArray["name"] = form7attributeObject.neighbour_north;
                        poiList.push(tempArray);
                        //push neighbour south
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 2;
                        tempArray["name"] = form7attributeObject.neighbour_south;
                        poiList.push(tempArray);

                        //push neighbour east
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 3;
                        tempArray["name"] = form7attributeObject.neighbour_east;
                        poiList.push(tempArray);
                        //push neighbour west
                        var tempArray = [];
                        tempArray["sno"] = form7attributeObject.poiLst.length + 4;
                        tempArray["name"] = form7attributeObject.neighbour_west;
                        poiList.push(tempArray);

                        for (var i = 1; i <= 4; i++) {
                            var tempArray1 = [];
                            tempArray1["sno"] = form7attributeObject.poiLst.length + 4 + i;
                            tempArray1["name"] = "";

                            poiList.push(tempArray1);
                        }
                    }
                    jQuery("#poi_template7").tmpl(poiList).appendTo("#poiTableRowData7");



                    var printWindow = window.open('', 'form7' + usin, 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                    var html = null;
                    html = $("#printDiv").html();
                    printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                            '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                            '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                            '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                    printWindow.document.close();
                    printWindow.focus();


                }
            });
}



function generateform5(usin) {

    var formImage = getFormImage();

    jQuery.ajax(
            {
                type: 'GET',
                url: 'resources/templates/forms/form5.html',
                dataType: 'html',
                success: function (data5)
                {
                    jQuery("#printDiv div").empty();
                    //$('#masterdiv div').empty();

                    jQuery("#printDiv").append(data5);

                    var fromTmp = new generateForms();
                    var form5attributeObject = fromTmp.Form5(usin);

                    $('.commune_logo').append("<img width='125' height='100' src='../../" + formImage + "'>");

                    $('#region_5').text(form5attributeObject.region);
                    $('#province_5').text(form5attributeObject.province);
                    $('.commune_5').text(form5attributeObject.commune);
                    $('.village_5').text(form5attributeObject.village);
                    $('.villageno_5').text(form5attributeObject.village_no);
                    $('.application_date5').text(form5attributeObject.application_date);
                    //$('.application_no5').text(form5attributeObject.application_no);
                    $('.apfrno5').text(form5attributeObject.application_no);
                    $('.apfrno_5').text(form5attributeObject.apfrno);  //kamal

                    $('#lastname_5').text(form5attributeObject.last_name);
                    $('#firstname_5').text(form5attributeObject.first_name);
                    $('#gender_5').text(form5attributeObject.sex);
                    $('#idcard5').text(form5attributeObject.refrence_id_card);
                    $('#birthdate5').text(form5attributeObject.dob);
                    $('#birthplace5').text(form5attributeObject.birthplace);
                    $('#profession5').text(form5attributeObject.profession);
                    $('#address_5').text(form5attributeObject.address);
                    $('#location5').text(form5attributeObject.location);
                    $('#section5').text(form5attributeObject.section);
                    $('#lot5').text(form5attributeObject.lot);
                    $('#parcel_no5').text(form5attributeObject.parcel_no);
                    $('#area5').text(((form5attributeObject.area) * area_constant).toFixed(2));
                    $('#nr-north5').text(form5attributeObject.neighbour_north);
                    $('#nr-east5').text(form5attributeObject.neighbour_east);
                    $('#nr-south5').text(form5attributeObject.neighbour_south);
                    $('#nr-west5').text(form5attributeObject.neighbour_west);
                    $('.mayor5').text(form5attributeObject.mayor_name);
                    $('#pv_no5').text(form5attributeObject.pv_no);
                    $('#recognition_date5').text(form5attributeObject.date_recognition_right);
                    // new changes
                    if (form5attributeObject.apfr_date == null) {
                        var generateForm = new generateForms();
                        $('#apfrdate5').text(generateForm.getCurrentDate());
                        $('#apfrdate_5').text(generateForm.getCurrentDate());  // kamal
                    } else {
                        $('#apfrdate5').text(form5attributeObject.apfr_date);
                        $('#apfrdate_5').text(form5attributeObject.apfr_date); // kamal
                    }
                    //$('#apfrdate5').text(form5attributeObject.apfr_date);
                    //$('#existing_use5').text(form5attributeObject.existing_use);
                    if (form5attributeObject.other_use == "null") {
                        $('#other_use5').text("");
                    } else {
                        $('#other_use5').text(form5attributeObject.other_use);
                    }


                    if (form5attributeObject.existing_use != null) {
                        for (var i = 0; i < form5attributeObject.existing_use.length; i++) {
                            jQuery("#existing_use_div5").append("o " + form5attributeObject.existing_use[i][2] + "<br>");
                        }
                    }

                    var printWindow = window.open('', 'form5' + usin, 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                    var html = null;
                    html = $("#printDiv").html();
                    printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                            '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                            '<script src="../resources/scripts/cloudburst/viewer/LandRecordTemp.js"></script>' +
                            '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                            '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                    printWindow.document.close();
                    printWindow.focus();


                }
            });
}
function generatePaymentLetter(usin) {
    var formImage = getFormImage();
    jQuery.ajax(
            {
                type: 'GET',
                url: 'resources/templates/forms/paymentLetter.html',
                dataType: 'html',
                success: function (data)
                {
                    jQuery("#printDiv div").empty();
                    jQuery("#printDiv").append(data);

                    var generateForm = new generateForms();
                    attrObject = generateForm.paymentDetails(usin);
                    $('.commune_logo').append("<img width='125' height='100' src='../../" + formImage + "'>");

                    $('#_region').text(attrObject.region);
                    $('#_province').text(attrObject.province);
                    $('.commune').text(attrObject.commune);
                    $('.application_no').text(attrObject.application_no);
                    $('.application_date').text(attrObject.applicationDate);
                    $('.village').text(attrObject.village);
                    $('#first_name').text(attrObject.firstname);
                    $('#last_name').text(attrObject.lastname);
                    $('#letter_date').text(attrObject.printDate);
                    $('#area_print').text(((attrObject.area) * area_constant).toFixed(2));


                    // Added for pv number and date

                    $('.pv_no').text(attrObject.pv_no);
                    $('.pv_date').text(attrObject.pv_date);

                    var printWindow = window.open('', 'paymentLetter' + usin, 'height=900,width=950,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, location=no');

                    var html = null;
                    html = $("#printDiv").html();
                    printWindow.document.write('<html><head><title>MAST</title>' + ' <link rel="stylesheet" href="../resources/styles/viewer/form.css" type="text/css" />' + ' <link rel="stylesheet" href="../resources/styles/viewer/style-new.css" type="text/css" />' +
                            '<script src="../resources/scripts/cloudburst/viewer/Form.js"></script>' +
                            '<script src="../resources/scripts/jquery-1.7.1/jquery-1.7.1.min.js"></script>' +
                            '<script src="../resources/scripts/jquery-alert/jquery.alerts.css"></script>' +
                            '</head><body> ' + html + '<input type="hidden" id="usin_primerykey" value=' + usin + '></body></html>');

                    printWindow.document.close();
                    printWindow.focus();


                }
            });
}

function printForm() {

    document.getElementsByClassName('print_form')[0].style.visibility = "hidden";
    location.reload();
    window.print();
}
function printAPFR_Ind_Form() {

    usin_key = $("#usin_primerykey").val();
    var generateForm = new generateForms();
    checkApfrDate = generateForm.checkApfrDate(usin_key);
    if (checkApfrDate) {
        document.getElementsByClassName('print_form')[0].style.visibility = "hidden";
        location.reload();
        window.print();
    } else {
        //set apfr date
        generateForm.setApfrDate(usin_key);
        document.getElementsByClassName('print_form')[0].style.visibility = "hidden";
        location.reload();
        window.print();
    }

}

function printNotice() {

    var noticeDate = null;
    var displayDate = null;
    var generateForm = new generateForms();
    usinId = $('#usin_primerykey').val();
    noticeDate = generateForm.checkDate(usinId);
    displayDate = noticeDate;

    var flag = $('#flag').text();
    jConfirm('Avis de publicité courant de : <strong>' + displayDate + '.</strong> Voulez-vous vraiment imprimer ?', 'Confirmer', function (response) {

        if (response) {
            if (flag == "true") {
                printForm();
            }
        }
    });
}

function setAreaMap(usin, callbackFunc) {
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
        var features = new ol.format.GeoJSON().readFeatures(json);
        var parcelExtent = features[0].getGeometry().getExtent();

        bbox = (parcelExtent[0] - 0.002) + ',' + (parcelExtent[1] - 0.002) + ','
                + (parcelExtent[2] + 0.002) + ',' + (parcelExtent[3] + 0.002);

        var mapUrl = "http://" + location.host + "/geoserver/wms?" + "bbox=" + bbox + "&FORMAT=image/png&styles=a,b,c,d,generate_map&REQUEST=GetMap&layers=Mast:HAB_Villages,Mast:HAB_Roads,Mast:HYD_Rivers,Mast:la_spatialunit_land,Mast:la_spatialunit_land&width=690&height=690&srs=EPSG:4326" + "&cql_filter=INCLUDE;INCLUDE;INCLUDE;INCLUDE;landid=" + usin;
        if (typeof callbackFunc !== 'undefined') {
            callbackFunc(mapUrl);
        }
    });
}

function getCurrentDate() {
    var myDate = new Date(new Date().getTime() + (45 * 24 * 60 * 60 * 1000));
    var dd = myDate.getDate();
    var mm = myDate.getMonth() + 1; //January is 0!
    var yyyy = myDate.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    var myDate = dd + '/' + mm + '/' + yyyy;
    return myDate;
}

function getExtendedDate() {
    var myDate = new Date(new Date().getTime() + (47 * 24 * 60 * 60 * 1000));
    var dd = myDate.getDate();
    var mm = myDate.getMonth() + 1; //January is 0!
    var yyyy = myDate.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    var myDate = dd + '/' + mm + '/' + yyyy;
    return myDate;
}
function getToday() {
    var myDate = new Date();
    var dd = myDate.getDate();
    var mm = myDate.getMonth() + 1; //January is 0!
    var yyyy = myDate.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }
    var myDate = dd + '/' + mm + '/' + yyyy;
    return myDate;

}

function getFormImage() {
    var resultImage = "";
    $.ajax({
        type: "GET",
        url: '/mast/viewer/projectdata/getformimage/' + activeProject,
        async: false,
        success: function (fileName) {
            resultImage = '/mast/studio/project/getsignature/' + fileName;
        }
    });
    return resultImage;
}