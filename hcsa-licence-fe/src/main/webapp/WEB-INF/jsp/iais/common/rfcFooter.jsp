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
<iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>

<script>
    $(document).ready(function() {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        $('#RfcBack').click(function () {
            submit('jump','back',null);
        });

        $('#RfcSave').click(function () {
            submit('preview','next',null);
            submit('preview','saveDraft',null);
        });

        $('#RenewSave').click(function () {

            submit('jump','next',null);
            submit('jump','saveDraft',null);

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
</script>




