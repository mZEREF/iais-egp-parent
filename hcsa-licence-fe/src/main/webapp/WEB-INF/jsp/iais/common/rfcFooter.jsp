<input type="hidden" name="crud_action_type_continue" value="">
<input type="hidden" name="crud_action_additional" value="">
<div class="row">
    <div class="col-xs-12 col-sm-3 ">
        <a id="RfcBack" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <div class="col-xs-12 col-sm-6">
        <div class="button-group">
            <a id="RfcUndo" class="" href="#" >Undo All Changes</a>
        </div>
    </div>
    <c:choose>
        <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
            <div class="col-xs-12 col-sm-3  text-right">
                <a class="btn btn-primary next premiseId" id="RenewSave" >Save and Preview</a>
            </div>
        </c:when>
        <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
            <div class="col-xs-12 col-sm-3  text-right">
                <a class="btn btn-primary next premiseId" id="RfcSave" >Save and Preview</a>
            </div>
        </c:when>
    </c:choose>
</div>
<c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
    <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
</c:if>
<input type="text" style="display:none;" value="${hciNameUsed}" name="hciNameUsedInput" id="hciNameUsedInput">
<iais:confirm msg="NEW_ACK011" needCancel="false" callBack="Continue()" popupOrder="hciNameUsed" yesBtnDesc="Continue" ></iais:confirm>
<input type="text" style="display:none;" name="continueStep" id="continueStep" value="${continueStep}">
<input type="text" style="display: none" name="crudActionTypeContinue" id="crudActionTypeContinue" value="${crudActionTypeContinue}">
<script>
    $(document).ready(function() {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        if($('#hciNameUsedInput').val()=='hciNameUsed'){
            $('#hciNameUsed').modal('show');
        }
        $('#RfcBack').click(function () {
            submit('jump','back',null);
        });

        $('#RfcSave').click(function () {
            submit('preview','next','rfcSaveDraft');
        });

        $('#RenewSave').click(function () {

            submit('jump','next','rfcSaveDraft');

        });

        $('#RfcSaveDraft').click(function () {
            <c:choose>
            <c:when test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
            submit('premises','saveDraft',null);
            </c:when>
            <c:when test="${AppSubmissionDto.appEditSelectDto.docEdit}">
            submit('documents','saveDraft',null);
            </c:when>
            </c:choose>

        });

        $('#RfcUndo').click(function () {
            submit('jump','undo',null);
        });
    });
    function cancel() {
        $('#saveDraft').modal('hide');
    }

    function jumpPage() {
        submit('premises','saveDraft','jumpPage');
    }
    function Continue() {
        $('#hciNameUsed').modal('hide');
        $("[name='crud_action_type_continue']").val("continue");
        doSubmitForm($('#continueStep').val(),'',$('#crudActionTypeContinue').val());
    }
</script>




