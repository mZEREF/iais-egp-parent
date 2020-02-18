
<div class="application-tab-footer">
  <div class="row">
    <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a></div>
    <div class="col-xs-12 col-sm-6">
      <div class="button-group">
        <c:if test="${requestInformationConfig==null}">
        <a class="btn btn-secondary" id = "SaveDraft">Save as Draft</a>
        </c:if>
        <a class="next btn btn-primary" id = "Next">Next</a></div>
      <input name="nextStep" value="" type="hidden">
    </div>
  </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        var controlFormLi = $('#controlFormLi').val();
        //Binding method
        $('#Back').click(function(){
            if(${serviceStepDto.isStepFirst()}){
                if(${serviceStepDto.isServiceFirst()}){
                    submit('documents',null,null);
                }else{
                    submitFormTabs('${serviceStepDto.previousStep.stepCode}');
                }
            }else{
                submitForms('${serviceStepDto.previousStep.stepCode}',null,null,controlFormLi);
            }
        });
        $('#SaveDraft').click(function(){
            submitForms('${serviceStepDto.currentStep.stepCode}','saveDraft',null,controlFormLi);
        });
        $('#Next').click(function(){
            $("[name='nextStep']").val('next');
            if(${serviceStepDto.isStepEnd()}){
                if(${serviceStepDto.isServiceEnd()}){
                    submit('preview',null,null);
                }else{
                    submitFormTabs('${serviceStepDto.nextStep.stepCode}');
                }
            }else{
                submitForms('${serviceStepDto.nextStep.stepCode}',null,null,controlFormLi);
            }
        });

    });

</script>