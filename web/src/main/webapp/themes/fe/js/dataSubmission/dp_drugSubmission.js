$(document).ready(function() {
    $('#drugType').change(function () {
        var drugtype= $('#drugType option:selected').val();
        if(drugtype == "DPD001"){
            $('#prescriptionDate').attr("style","display: block");
        }else {
            $('#prescriptionDate').attr("style","display: none");
        }
        if(drugtype == "DPD002"){
            $('#dispensingDate').attr("style","display: block");
        }else {
            $('#dispensingDate').attr("style","display: none");
        }
    });
});

$(document).ready(function() {
    console.log("showValidatePT!")
    if ("1" == $('#showValidatePT').val()) {
        $('#validatePT').modal('show');
    } else if ("1" == $('#showValidatePT').val()) {
        $('#noFoundDiv').modal('show');
    }
});

function retrieveValidateDrug() {
    showWaiting();
    var idType = $('#idType').val();
    var idNo = $('input[name="idNumber"]').val();
    var nationality = $('#nationality').val();
    var url = $('#_contextPath').val() + '/dp/retrieve-identification';
    var options = {
        idType: idType,
        idNo: idNo,
        nationality: nationality,
        url: url
    }
    callCommonAjax(options, validatePatientName);
}
function validatePatientName(data){
    clearErrorMsg();
    clearSelection();
    console.log("success!")
    if (isEmpty(data) || isEmpty(data.selection) || isEmpty(data.selection.name) || !isEmpty(data.errorMsg)) {

        if (!isEmpty(data.errorMsg)) {
            doValidationParse(data.errorMsg);
        } else {
            $('#noFoundDiv').modal('show');
        }
        return;
    }
    clearSelection();
    $('#name').find('p').text(data.selection.name);
    $('[name="name"]').val('1');
    $('#patientNameHidden').val(data.selection.name);

}
function clearSelection(){
    console.log("clearSelection!")
    clearErrorMsg();
    $('#name').find('p').text('');
    clearFields('.selectionHidden');
}

$(document).ready(function() {
    if($('#PRS_SERVICE_DOWN_INPUT').val()=='PRS_SERVICE_DOWN'){
        $('#PRS_SERVICE_DOWN').modal('show');
    }
});

function validateDoctors() {
    console.log('loading info ...');
    showWaiting();
    var prgNo =  $('input[name="doctorReignNo"]').val();
    console.log('1');
    if(prgNo == "" || prgNo == null || prgNo == undefined){
        clearPrsInfo();
        dismissWaiting();
        return;
    }
    var no = $('input[name="doctorReignNo"]').val();
    var jsonData = {
        'prgNo': no
    };
    console.log('2');
    $.ajax({
        'url': '${pageContext.request.contextPath}/dp/prg-input-info',
        'dataType': 'json',
        'data': jsonData,
        'type': 'GET',
        'success': function (data) {
            console.log('3');
            if (isEmpty(data)) {
                console.log("The return data is null");
            } else if('-1' == data.statusCode || '-2' == data.statusCode) {
                $('#prsErrorMsg').val($('#flagDocMessage').html());
                $('#PRS_SERVICE_DOWN').modal('show');
                clearPrsInfo();
            } else if (data.hasException) {
                $('#prsErrorMsg').val($('#flagInvaMessage').html());
                $('#PRS_SERVICE_DOWN').modal('show');
                clearPrsInfo();
            } else if ('401' == data.statusCode) {
                $('#prsErrorMsg').val($('#flagPrnMessage').html());
                $('#PRS_SERVICE_DOWN').modal('show');
                clearPrsInfo();
            } else {
                loadingSp(data);
            }
            dismissWaiting();
        },
    });
}

function cancels() {
    $('#PRS_SERVICE_DOWN').modal('hide');
}
var clearPrsInfo = function () {
    $('#names').find('p').text('');
};
function loadingSp(data) {
    const name = data.name;
    $('#names').find('p').text(name);
    $('#doctorNameHidden').val(name);
}