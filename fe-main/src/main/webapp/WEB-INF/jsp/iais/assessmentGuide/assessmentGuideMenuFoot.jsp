</div>
<div class="col-lg-12 col-xs-12" id="commBackBtn" style="padding-left: 20px">
    <a href="/main-web/eservice/INTERNET/MohInternetInbox"><em
            class="fa fa-angle-left"></em> Back</a>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
<!-- Modal -->
<div class="modal fade" id="isRenewedModal" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg modal-dialog-centered" role="document" >
        <div class="modal-content">
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--            </div>--%>
            <div class="modal-body" style="text-align: center">
                <div class="row">
                    <div class="col-md-12"><span style="font-size: 2rem;">${LAEM}</span></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!--Modal End-->
<!-- Modal -->
<div class="modal fade" id="ceasedModal" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document" style="width: 760px;">
        <div class="modal-content">
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--            </div>--%>
            <div class="modal-body" style="text-align: center">
                <div class="row">
                    <div class="col-md-12"><span style="font-size: 2rem;">
                                        ${cessationError}
                                        </span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!--Modal End-->
<input type="hidden" value="" id="isNeedDelete" name="isNeedDelete">
<iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteRfcDraft()"></iais:confirm>
<iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftRenewByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteRenewDraft()"></iais:confirm>
<iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftAppealByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteAppealDraft()"></iais:confirm>
<iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftCeasdByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteCeasedDraft()"></iais:confirm>
<iais:confirm msg="${draftByLicAppId}" callBack="cancel()" popupOrder="draftAmendByLicAppId" yesBtnDesc="cancel" cancelBtnDesc="delete" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="deleteAmendDraft()"></iais:confirm>

</form>
</div>

<script>
    $(function () {

        $('.self-assessment-gp .self-assessment-item .table-gp .table input[type="radio"]').on('change', function() {
            $('.self-assessment-gp .self-assessment-item .table-gp .table tr.selectedRow').removeClass('selectedRow');
        });

        if ('${licIsRenewed}' || '${licIsAppealed}' || '${licIsAmend}'|| '${licIsWithdrawal}') {
            $('#isRenewedModal').modal('show');
        }

        if ('${ceasedErrResult}') {
            $('#ceasedModal').modal('show');
        }

        $('#draftByLicAppId').modal('hide');
        if('1' == '${isShow}'){
            $('#draftByLicAppId').modal('show');
        }
        $('#draftRenewByLicAppId').modal('hide');
        if('1' == '${isRenewShow}'){
            $('#draftRenewByLicAppId').modal('show');
        }
        $('#draftAppealByLicAppId').modal('hide');
        if('1' == '${isAppealShow}'){
            $('#draftAppealByLicAppId').modal('show');
        }
        $('#draftAmendByLicAppId').modal('hide');
        if('1' == '${isAmendShow}'){
            $('#draftAmendByLicAppId').modal('show');
        }
        $('#draftCeasdByLicAppId').modal('hide');
        if('1' == '${isCeasedShow}'){
            $('#draftCeasdByLicAppId').modal('show');
        }

    });

    function cancel() {
        $('#draftByLicAppId').modal('hide');
        $('#draftRenewByLicAppId').modal('hide');
        $('#draftAppealByLicAppId').modal('hide');
        $('#draftAmendByLicAppId').modal('hide');
        $('#draftCeasdByLicAppId').modal('hide');
    }

    // $(document).ready(function () {
    //
    // });

    function deleteAppealDraft() {
        $('#isNeedDelete').val('delete');
        doLicAppeal();
    }
    function deleteRfcDraft(){
        $('#isNeedDelete').val('delete');
        doLicAmend();
    }
    function deleteRenewDraft(){
        $('#isNeedDelete').val('delete');
        doLicRenew();
    }
    function deleteCeasedDraft() {
        $('#isNeedDelete').val('delete');
        $('.CeaseBtn').prop('disabled',false);
        doLicCease();
    }
    function deleteAmendDraft() {
        $('#isNeedDelete').val('delete');
        guideSubmit("amend1_1","main");
        // doLicAmend();
    }

    function guideSubmit(guideAction,toWhere){
        showWaiting();
        if("main" == toWhere){
            $("[name='crud_action_type']").val(guideAction);
        }else if ("second" == toWhere) {
            $("[name='guide_action_type']").val(guideAction);
        }
        $("#mainForm").submit();
    }

    $("#applyLicence").click(function(){
        guideSubmit("new1","main");
    });

    $("#withdrawApplication").click(function(){
        guideSubmit("withdraw","main");
    });

    $("#resumeDraftApplication").click(function () {
        guideSubmit("resume","main");
    });

    $("#renewLicence1").click(function () {
        guideSubmit("renew","main");
    });

    $("#renewLicence2").click(function () {
        guideSubmit("renewUp","main");
    });

    $("#ceaseLicence").click(function(){
        guideSubmit("cease","main");
    });

    $("#amendLicence1_1").click(function(){
        guideSubmit("amend1_1","main");
    });

    $("#amendLicence1_2").click(function(){
        guideSubmit("amend1_2","main");
    });

    $("#amendLicence2").click(function(){
        guideSubmit("amend2","main");
    });

    $("#amendLicence3_1").click(function(){
        guideSubmit("amend3_1","main");
    });

    $("#amendLicence3_2").click(function(){
        guideSubmit("amend3_2","main");
    });

    $("#amendLicence4_1").click(function(){
        guideSubmit("amend4_1","main");
    });

    $("#submitDataMoh").click(function(){
        guideSubmit("subDate","main");
    });

    $("#amendLicence4_2").click(function(){
        guideSubmit("amend4_2","main");
    });
    $("#amendLicence5").click(function(){
        guideSubmit("amend5","main");
    });
    $("#updateAdminPersonnel").click(function(){
        guideSubmit("upAdmin","main");
    });

    function submitDataMoh(){
        if ($('#submitDateMohLab').is(':checked')){
            guideSubmit("submieDateMoh","second");
        }else{
            console.log("checkbox is not check")
        }
    }
</script>