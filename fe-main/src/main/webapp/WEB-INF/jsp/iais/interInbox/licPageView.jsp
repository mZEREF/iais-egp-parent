<%@include file="common/commonImport.jsp"%>
<c:set var="tabCode" value="${licTab == 1 ? 'lic' : ''}"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="common/commonFile.jsp" %>
<script type="text/javascript">

    $(function () {
        if ('${licIsRenewed}' || '${licIsAppealed}' || '${licIsAmend}') {
            $('#isRenewedModal').modal('show');
        }

        if ('${ceasedErrResult}') {
            $('#ceasedModal').modal('show');
        }

        if ('${!empty licence_err_list}') {
            licClick();
        }
    });

    function licClick() {
        var checkedNum = $("[name='licenceNo']:checked").length;
        if ($('.licenceCheck').is(':checked')){
            if (checkedNum == 1){
                $("#lic-print").removeClass('disabled');
                $("#lic-renew").removeClass('disabled');
                $("#lic-cease").removeClass('disabled');
                $("#lic-amend").removeClass('disabled');
                $("#lic-appeal").removeClass('disabled');
            } else{
                var statusDuo = [];
                $("[name='licenceNo']:checked").each(function (k,v) {
                    var $currentTr = $(this).closest('tr');
                    statusDuo.push($currentTr.find('td').eq(3).find('p').eq(1).html());
                });
                if (!statusDuo.includes('Ceased')){
                    $("#lic-renew").removeClass('disabled');
                    $("#lic-cease").removeClass('disabled');
                    $("#lic-print").removeClass('disabled');
                }else{
                    $("#lic-renew").addClass('disabled');
                    $("#lic-cease").addClass('disabled');
                    $("#lic-print").removeClass('disabled');
                }
                console.log(statusDuo);
                $("#lic-amend").addClass('disabled');
                $("#lic-appeal").addClass('disabled');
            }
        }else {
            $("#lic-amend").addClass('disabled');
            $("#lic-print").addClass('disabled');
            $("#lic-renew").addClass('disabled');
            $("#lic-cease").addClass('disabled');
            $("#lic-appeal").addClass('disabled');
        }
    }

    function submit(action){
        $("[name='lic_action_type']").val(action);
        $("#licForm").submit();
        $("input[name='licenceNo']").prop("checked",false);
    }

    function licToMsgPage(){
        showWaiting();
        submit("licToMsg");
    }

    function licToAppPage(){
        showWaiting();
        submit("licToApp");
    }

    function doSearchLic(){
        showWaiting();
        submit("licSearch");
    }

    function jumpToPagechangePage() {
        showWaiting();
        submit('licPage');
    }

    function doLicAmend() {
        showWaiting();
        submit('licDoAmend');
    }

    function doLicRenew() {
        showWaiting();
        submit('licDoRenew');
    }

    function doLicAppeal(){
        showWaiting();
        submit('licDoAppeal');
    }
    function doLicCease(){
        showWaiting();
        submit('licDoCease');
    }

    function doLicPrint(){
        if ($("input[type='checkbox']").is(':checked')) {
            var arr = [];
            var r = document.getElementsByName("licenceNo");
            for(var i=0;i<r.length;i++) {
                if (r[i].checked) {
                    var lic = document.getElementsByName(r[i].value);
                    var str = r[i].value + '@' + lic[0].value;
                    arr.push(str);
               }
            }
            $.ajax({
                'url': '${pageContext.request.contextPath}/internetInbox/licenceNo',
                'dataType': 'json',
                'data': {licenceNos:arr},
                'type': 'POST',
                'traditional':true,
                'success': function (data) {
                    window.location= "/main-web/eservice/INTERNET/PrintLicence";
                },
                'error': function () {
                    console.log("fail");
                }
            });
        }
    }


    function doLicAction(licNo){
        showWaiting();
        $("[name='crud_action_value']").val(licNo);
        submit('licDoAmend');
    }

    function toLicView(licId){

    }

    $(".licToView").click(function () {
        var licId = $(this).closest("tr").find(".licId").html();
        showWaiting();
        $("[name='action_id_value']").val(licId);
        submit('licToView');
    });
    
    $(".licActions").change(function () {

    });
    
    function scrollIntoLicView() {
        $("#licForm")[0].scrollIntoView(true);
    }
    
    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }

    function doClearLic(){
        $("[name='licNoPath']").val("");
        $("[name='fExpiryDate']").val("");
        $("[name='eExpiryDate']").val("");
        $("[name='eStartDate']").val("");
        $("[name='fStartDate']").val("");
        $("#licType option:first").prop("selected", 'selected').val("");
        $("#licStatus option:first").prop("selected", 'selected').val("");
        $("#clearBody .current").text("All");
        $(".error-msg").text("")
    }
</script>