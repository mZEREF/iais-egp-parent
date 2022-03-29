<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<input type="hidden" name="current_page" value="${top_current_page}"/>
<input type="hidden" name="_contextPath" id="_contextPath" value="${pageContext.request.contextPath}"/>
<input type="hidden" name="printflag" id="printflag" value="${printflag}">

<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="/WEB-INF/jsp/iais/common/dashboardDropDown.jsp" %>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1 class="font-weight 0">New Data Submission</h1>
                        <h3 style="font-weight: normal">You are submitting for <strong>Termination Of Pregnancy</strong></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        if ($('#saveDraftSuccess').val() == 'success') {
            $('#saveDraft').modal('show');
        }

        if ($('#_draftModal').length > 0) {
            $('#_draftModal').modal('show');
        }
        console.log("1")
        let currPage = $('input[name="current_page"]').val();
        if (currPage === "confim") {
            console.log("2")
            $('#nextBtn').html('Submit');
        }

        $('.back').click(function () {
            if (currPage === "confim") {
                submit('page');
            } else {
                submit('return');
            }
        })

        $('#nextBtn').click(function () {
            if (currPage == "confim") {
                submit('submission');
            } else {
                submit('confim');
            }
        })

        $('#saveDraftBtn').click(function () {
            submit('draft');
        })
    })

    function cancelDraft() {
        $('#saveDraft').modal('hide');
    }

    function jumpToInbox() {
        showWaiting();
        var token = $('input[name="OWASP_CSRFTOKEN"]').val();
        var url = "/main-web/eservice/INTERNET/MohInternetInbox";
        if (!isEmpty(token)) {
            url += '?OWASP_CSRFTOKEN=' + token;
        }
        document.location = url;
    }

    function submit(action,value) {
        console.log("3")
        $("[name='crud_type']").val(action);
        $("[name='crud_action_value']").val(value);
        var mainForm = document.getElementById('mainForm');
        showWaiting();
        mainForm.submit();
        console.log("4")
    }
    function printData() {
        // window.print();
        clearErrorMsg();
        var url = $('#_contextPath').val() + '/eservice/INTERNET/MohDsPrint';
        var token = $('input[name="OWASP_CSRFTOKEN"]').val();
        if (!isEmpty(token)) {
            url += '?OWASP_CSRFTOKEN=' + token;
        }
        var printflag = $('#printflag').val();
        if (!isEmpty(printflag)) {
            if (url.indexOf('?') < 0) {
                url += '?printflag=' + printflag;
            } else {
                url += '&printflag=' + printflag;
            }
        }
        var data = getDataForPrinting();
        if (isEmpty(data)) {
            window.open(url, '_blank');
        } else {
            $.ajax({
                'url': $('#_contextPath').val() + '/ds/init-print',
                'dataType': 'json',
                'data': data,
                'type': 'POST',
                'success': function (data) {
                    window.open(url, '_blank');
                },
                'error': function (data) {
                    console.log("err: " + data);
                }
            });
        }
        function getDataForPrinting() {
            var declaration = $('input[name="declaration"]:checked').val();
            if (isEmpty(declaration)) {
                return null;
            }
            var printflag = $('#printflag').val();
            if (isEmpty(printflag)) {
                printflag = '';
            }
            return {declaration: declaration, printflag: printflag};
        }
    }
</script>
