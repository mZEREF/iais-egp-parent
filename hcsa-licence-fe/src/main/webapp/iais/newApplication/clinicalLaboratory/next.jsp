
<div class="application-tab-footer">
  <input type="text" style="display: none" id="selectDraftNo" value="${selectDraftNo}">
<c:choose>
  <c:when test="${'APTY005' ==AppSubmissionDto.appType && requestInformationConfig == null}">
    <div class="row">
      <div class="col-xs-12 col-sm-3"><a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a></div>
      <div class="col-xs-12 col-sm-9">
        <div class="button-group">
          <c:choose>
            <c:when test="${serviceStepDto.isStepEnd() && serviceStepDto.isServiceEnd()}">
              <a class="btn btn-primary next premiseId" id="RfcSave" >Save and Preview</a>
            </c:when>
            <c:otherwise>
              <a class="btn btn-primary" id="Next" >Next</a>
            </c:otherwise>
          </c:choose>
          <c:if test="${requestInformationConfig==null}">
            <a class="btn btn-secondary" id = "SaveDraft" data-toggle="modal" data-target= "#saveDraft" >Save as Draft</a>
          </c:if>
          <a id="RfcUndo" class="" href="#" >Undo All Changes</a>
        <input name="nextStep" value="" type="hidden">
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <div class="row">
      <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a></div>
      <div class="col-xs-12 col-sm-6">
        <div class="button-group">
          <c:if test="${requestInformationConfig==null}">
            <a class="btn btn-secondary" id = "SaveDraft" data-toggle="modal" data-target= "#saveDraft" >Save as Draft</a>
          </c:if>
          <c:choose>
            <c:when test="${serviceStepDto.isStepEnd() && serviceStepDto.isServiceEnd()}">
              <a class="btn btn-primary" id="Next" >Proceed to Preview & Submit</a>
            </c:when>
            <c:otherwise>
              <a class="btn btn-primary" id="Next" >Next</a>
            </c:otherwise>
          </c:choose>
        <input name="nextStep" value="" type="hidden">
      </div>
    </div>
  </c:otherwise>
</c:choose>
</div>
      <c:if test="${ not empty selectDraftNo }">
        <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
      </c:if>
<script type="text/javascript">
    var  v;
    $(document).ready(function() {
        var controlFormLi = $('#controlFormLi').val();
        v=controlFormLi;
        //Binding method
        $('#Back').click(function(){
            if(${serviceStepDto.isStepFirst()}){
                if(${serviceStepDto.isServiceFirst()}){
                  <c:choose>
                    <c:when test="${'APTY005' ==AppSubmissionDto.appType && requestInformationConfig == null}">
                      submit('jump',null,null);
                    </c:when>
                    <c:otherwise>
                      submit('documents',null,null);
                    </c:otherwise>
                  </c:choose>
                }else{
                    submitFormTabs('${serviceStepDto.previousStep.stepCode}');
                }
            }else{
                submitForms('${serviceStepDto.previousStep.stepCode}',null,null,controlFormLi);
            }
        });
        $('#SaveDraft').click(function(){
            if($('#selectDraftNo').val()==''||$('#selectDraftNo').val()==null){
                submitForms('${serviceStepDto.currentStep.stepCode}','saveDraft',null,controlFormLi);
            }
        });
        $('#Next').click(function(){
            $("[name='nextStep']").val('next');
            nextFun();
        });

        $('#RfcUndo').click(function () {
            $("[name='nextStep']").val('undo');
            submit('jump',null,null);
        });

        $('#RfcSkip').click(function () {
            $("[name='nextStep']").val('skip');
            nextFun();
        });

    });

    var nextFun = function () {
        if(${serviceStepDto.isStepEnd()}){
            if(${serviceStepDto.isServiceEnd()}){
                submit('preview',null,null);
            }else{
                submitFormTabs('${serviceStepDto.nextStep.stepCode}');
            }
        }else{
            submitForms('${serviceStepDto.nextStep.stepCode}',null,null,controlFormLi);
        }
    }
    function saveDraft() {
        submitForms('${serviceStepDto.currentStep.stepCode}','saveDraft',$('#selectDraftNo').val(),controlFormLi);
    }
    function cancelSaveDraft() {
        submit('premises','saveDraft','cancelSaveDraft');
    }
</script>