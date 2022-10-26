$(function () {
    $(".withdraw-next").click(function () {
        $("input[name='checkboxSample']:checked").each(function () {
            let appNoList = [];
            let withdrawContent$ = $(".withdraw-content-box");
            withdrawContent$.find(".withdraw-info p").each(function () {
                appNoList.push($(this).text())
            });
            let appNo = $(this).parent().parent().parent().find(".withdraw-info").find("p").eq(0).text();
            let appmask = "'" + $(this).parent().parent().parent().find(".withdraw-info").find("p").eq(1).text() + "'";
            if ($.inArray(appNo, appNoList) == -1) {
                let appStrNo = "'" + appNo + "'";
                withdrawContent$.last().parent().append('<div class="withdraw-content-box">\n' +
                    '                                    <div class="withdraw-info-gp">\n' +
                    '                                        <div class="withdraw-info-row">\n' +
                    '                                            <div class="withdraw-info">\n' +
                    '                                                <p><a href="javascript:void(0);" class="appNo" onclick="toApplicationView('+appmask+','+appStrNo+')">'+ appNo+'</a></p>\n'+
                    '                                            </div>\n' +
                    '                                            <div class="withdraw-delete">\n' +
                    '                                                <p ><a href="javascript:void(0);" onclick="deleteWithdraw(this)"><i class="fa fa-trash-o"></i>Delete</a></p>\n' +
                    '                                            </div>\n' +
                    '                                        </div>\n' +
                    '                                    </div>\n' +
                    '                                </div>')
            }
        });
        $('#newappModal').modal('hide');
    });
})

function isRemarksMandatory (){
    var reasonOption = $("#reason").val();
    if (reasonOption === 'WDREASN007'){
        $("#remarksDiv").show();
    }else{
        $("#remarksDiv").hide();
    }
}

function deleteWithdraw(it) {
    console.log("delete withdraw app");
    $(it).parent().parent().parent().parent().parent().remove();
}

function doSubmit() {
    showWaiting();
    let appNoList = "";
    let withdrawContent$ = $(".withdraw-content-box");
    withdrawContent$.find(".withdraw-info p").each(function () {
        appNoList = appNoList+$(this).find('a').text()+"#";
    });
    $("[name='withdraw_app_list']").val(appNoList);
    $("#mainForm").submit();
}

function validateOtherDocType() {
    var allSelected = true;
    $("#upload-other-doc-gp").find("select.other-doc-type-dropdown").each(function () {
        var docId = this.id.substring("docType".length);
        if (this.value) {
            $("span[data-err-ind=" + docId + "]").text("");
        } else {
            allSelected = false;
            $("span[data-err-ind=" + docId + "]").text("Please select one document type");
        }
    });
    if (!allSelected) {
        dismissWaiting();
    }
    return allSelected;
}
