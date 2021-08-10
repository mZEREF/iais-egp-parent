$(function() {
    var controlLi = $('#controlLi').val();
    console.log(controlLi);
    var $tarSel = $('#'+controlLi+'li');
    if ($tarSel.length > 0) {
        $tarSel.addClass('active');
        if ($tarSel.attr("class").match("active")){
            $tarSel.removeClass("incomplete");
            $tarSel.removeClass("complete");
        }
    }
    $('#nav-tabs-ul a').click(function() {
        var currId = $(this).attr('id');
        if (controlLi == currId) {
            return;
        }else if(currId == "PrepareForms"){
            $("[name='crud_action_type']").val("PrepareForms");
            var mainForm = document.getElementById('mainForm');
            mainForm.submit();
        }else if(currId == "PrepareDocuments"){
            $("[name='crud_action_type']").val("PrepareDocuments");
            var mainForm = document.getElementById('mainForm');
            mainForm.submit();
        }else if(currId == "PreparePreview"){
            $("[name='crud_action_type']").val("PreparePreview");
            var mainForm = document.getElementById('mainForm');
            mainForm.submit();
        }
    });
})