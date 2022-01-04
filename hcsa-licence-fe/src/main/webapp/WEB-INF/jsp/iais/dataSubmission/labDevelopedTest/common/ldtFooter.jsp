<div class="row">
    <div class="container">
        <div class="col-xs-12 col-sm-4 col-md-2 text-left">
            <a style="padding-left: 5px;" class="back" id="backBtn">
                <em class="fa fa-angle-left">&nbsp;</em> Back
            </a>
        </div>
        <div class="col-xs-12 col-sm-8 col-md-10">
            <div class="button-group text-right">
                <c:if test="${LdtSuperDataSubmissionDto.appType ne 'DSTY_005'}">
                    <a class="btn btn-secondary premiseSaveDraft" id="saveDraftBtn">Save as Draft</a>
                </c:if>
                <a class="btn btn-primary next premiseId" id="nextBtn">Preview</a></div>
        </div>
    </div>
</div>
<input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
<iais:confirm msg="This application has been saved successfully" callBack="cancelDraft();" popupOrder="saveDraft" yesBtnDesc="continue"
              cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpToInbox()" />
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }

        let currPage = $('input[name="current_page"]').val();
        if (currPage === "confirm") {
            $('#nextBtn').html('Submit');
        }

        $('.back').click(function () {
            if (currPage === "confirm") {
                submit('page');
            } else {
                submit('return');
            }
        })

        $('#nextBtn').click(function () {
            if (currPage == "confirm") {
                submit('submit');
            } else {
                submit('confirm');
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

    function submit(action) {
        $("[name='crud_action_type']").val(action);
        var mainForm = document.getElementById('mainForm');
        showWaiting();
        mainForm.submit();
    }
</script>