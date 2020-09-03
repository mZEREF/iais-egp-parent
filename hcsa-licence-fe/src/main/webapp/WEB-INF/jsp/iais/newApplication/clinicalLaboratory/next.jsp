<style>
  .cgo-header{
    font-size:18px;
  }
</style>
<div class="application-tab-footer">
  <input type="text" style="display: none" id="selectDraftNo" value="${selectDraftNo}">
  <input type="text" style="display: none; " id="saveDraftSuccess" value="${saveDraftSuccess}">
  <c:choose>
  <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
  <div class="row">
    <div class="col-xs-12 col-sm-3"><a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a></div>
    <div class="col-xs-12 col-sm-9">
      <div class="button-group">
        <a id="RfcUndo" class="" href="#" >Undo All Changes</a>
        <c:choose>
          <c:when test="${serviceStepDto.isStepEnd() && serviceStepDto.isServiceEnd()}">
            <c:choose>
              <c:when test="${'APTY004' ==AppSubmissionDto.appType}">
                <a class="btn btn-primary next premiseId" id="RenewSave" >Save and Preview</a>
              </c:when>
              <c:when test="${'APTY005' ==AppSubmissionDto.appType}">
                <a class="btn btn-primary next premiseId" id="RfcSave" >Save and Preview</a>
              </c:when>
            </c:choose>
          </c:when>
          <c:otherwise>
            <a class="btn btn-primary" id="Next" >Next</a>
          </c:otherwise>
        </c:choose>
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
            <a class="btn btn-secondary" id = "SaveDraft"  >Save as Draft</a>
          </c:if>
          <c:choose>
            <c:when test="${serviceStepDto.isStepEnd() && serviceStepDto.isServiceEnd()}">
              <a class="btn btn-primary" id="Next" >Proceed to Preview & Submit</a>
            </c:when>
            <c:when test="${serviceStepDto.isStepEnd() && !serviceStepDto.isServiceEnd()}">
              <a class="btn btn-primary" id="Next" >Proceed to Next Service</a>
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
      <%--<c:if test="${ not empty selectDraftNo}">
        <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
      </c:if>--%>

<%--<iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>--%>

<script type="text/javascript">
    var  v;
    $(document).ready(function() {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        var controlFormLi = $('#controlFormLi').val();
        v=controlFormLi;
        //Binding method
        $('#Back').click(function(){
            if(${serviceStepDto.isStepFirst()}){
                if(${serviceStepDto.isServiceFirst()}){
                    <c:choose>
                    <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
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
            submitForms('${serviceStepDto.currentStep.stepCode}','saveDraft',null,controlFormLi);
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

        $('#RfcSave').click(function () {
            $("[name='nextStep']").val('next');
            submit('preview','saveDraft',null);
        });

        $('#RenewSave').click(function () {
            $("[name='nextStep']").val('next');
            submit('jump','saveDraft',null);
        });

    });

    var nextFun = function () {
        if(${serviceStepDto.isStepEnd()}){
            if(${serviceStepDto.isServiceEnd()}){
                <c:choose>
                  <c:when test="${'APTY004' ==AppSubmissionDto.appType && requestInformationConfig == null}">
                    submit('jump',null,null);
                  </c:when>
                  <c:otherwise>
                    submit('preview',null,null);
                  </c:otherwise>
                </c:choose>
            }else{
                submitFormTabs('${serviceStepDto.nextStep.stepCode}');
            }
        }else{
            var controlFormLi = $('#controlFormLi').val();
            submitForms('${serviceStepDto.nextStep.stepCode}',null,null,controlFormLi);
        }
    }
    function saveDraft() {
        var controlFormLi = $('#controlFormLi').val();
        submitForms('${serviceStepDto.currentStep.stepCode}','saveDraft',$('#selectDraftNo').val(),controlFormLi);
    }
    function cancelSaveDraft() {
        submit('premises','saveDraft','cancelSaveDraft');
    }

    function cancel() {
        $('#saveDraft').modal('hide');

    }

    function jumpPage() {
        submit('premises','saveDraft','jumpPage');
    }


    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k,v) {
            $(this).html(k+1);
        });
    }

    <!--cgo,medAlert -->
    var fillPsnForm = function ($CurrentPsnEle,data,psnTYpe) {
        <!--salutation-->
        var salutation  = data.salutation;
        if( salutation == null || salutation =='undefined' || salutation == ''){
            salutation = '';
        }
        $CurrentPsnEle.find('select[name="salutation"]').val(salutation);
        var salutationVal = $CurrentPsnEle.find('option[value="' + salutation + '"]').html();
        $CurrentPsnEle.find('select[name="salutation"]').next().find('.current').html(salutationVal);
        <!--name-->
        $CurrentPsnEle.find('input[name="name"]').val(data.name);

        <!-- idType-->
        var idType  = data.idType;
        if(idType == null || idType =='undefined' || idType == ''){
            idType = '';
        }
        $CurrentPsnEle.find('select[name="idType"]').val(idType);
        var idTypeVal = $CurrentPsnEle.find('option[value="' + idType + '"]').html();
        $CurrentPsnEle.find('select[name="idType"]').next().find('.current').html(idTypeVal);
        <!-- idNo-->
        $CurrentPsnEle.find('input[name="idNo"]').val(data.idNo);

        $CurrentPsnEle.find('input[name="mobileNo"]').val(data.mobileNo);
        $CurrentPsnEle.find('input[name="emailAddress"]').val(data.emailAddr);


        <!--     ====================    -->
        <!--       diff page column      -->
        <!--     ====================    -->

        <!-- officeTelNo-->
        var officeTelNo = data.officeTelNo;
        if(officeTelNo != null && officeTelNo != ''){
            $CurrentPsnEle.find('input[name="officeTelNo"]').val(officeTelNo);
        }else{
            $CurrentPsnEle.find('input[name="officeTelNo"]').val('');
        }
        <!--Designation  -->
        var designation = data.designation;
        if(designation == null || designation == ''){
            designation = '';
        }
        $CurrentPsnEle.find('select[name="designation"]').val(designation);
        var designationVal = $CurrentPsnEle.find('option[value="' + designation + '"]').html();
        $CurrentPsnEle.find('select[name="designation"]').next().find('.current').html(designationVal);


        <!-- professionType-->
        var professionType = data.professionType;
        if(professionType == null || professionType =='undefined' || professionType == ''){
            professionType = '';
        }
        $CurrentPsnEle.find('select[name="professionType"]').val(professionType);
        var professionTypeVal = $CurrentPsnEle.find('option[value="' + professionType + '"]').html();
        $CurrentPsnEle.find('select[name="professionType"]').next().find('.current').html(professionTypeVal);
        <!-- professionRegoNo-->
        var professionRegoNo = data.profRegNo;
        if(professionRegoNo != null && professionRegoNo != ''){
            $CurrentPsnEle.find('input[name="professionRegoNo"]').val(professionRegoNo);
        }else{
            $CurrentPsnEle.find('input[name="professionRegoNo"]').val('');
        }
        <!-- speciality-->
        var speciality = data.speciality;
        if('CGO' == psnTYpe){
            $CurrentPsnEle.find('div.specialtyDiv').html(data.specialityHtml);
            showSpecialty();
        }else{
            if(speciality == null || speciality =='undefined' || speciality == ''){
                speciality = '-1';
            }
            var specialityVal = $CurrentPsnEle.find('option[value="' + speciality + '"]').html();
            if(specialityVal =='undefined'){
                speciality = '';
                specialityVal = $CurrentPsnEle.find('option[value="' + speciality + '"]').html();
            }
            $CurrentPsnEle.find('select[name="specialty"]').val(speciality);
            $CurrentPsnEle.find('select[name="specialty"]').next().find('.current').html(specialityVal);
        }
        if('other' == speciality){
            $CurrentPsnEle.find('input[name="specialtyOther"]').removeClass('hidden');
            var specialityOther = data.specialityOther;
            if(specialityOther != null && specialityOther != ''){
                $CurrentPsnEle.find('input[name="specialtyOther"]').val(specialityOther);
            }else{
                $CurrentPsnEle.find('input[name="specialtyOther"]').val('');
            }
        }else{
            $CurrentPsnEle.find('input[name="specialtyOther"]').addClass('hidden');
        }
        <!--Subspeciality or relevant qualification -->
        var qualification = data.subSpeciality;
        if(qualification != null && qualification != ''){
            $CurrentPsnEle.find('input[name="qualification"]').val(qualification);
        }else{
            $CurrentPsnEle.find('input[name="qualification"]').val('');
        }
        <!--preferredMode -->
        var preferredMode = data.preferredMode;
        if(preferredMode != null && preferredMode !='undefined' && preferredMode != ''){
            if('3' == preferredMode){
                $CurrentPsnEle.find('input.preferredMode').prop('checked',true);
            }else{
                $CurrentPsnEle.find('input.preferredMode').each(function () {
                    if(preferredMode == $(this).val()){
                        $(this).prop('checked',true);
                    }
                });
            }
        }else{
            $CurrentPsnEle.find('input.preferredMode').prop('checked',false);
        }

        var isLicPerson = data.licPerson;
        if('1' == isLicPerson){
            if('CGO' == psnTYpe){
                var $cgoPsnEle = $CurrentPsnEle.find('.new-officer-form');
                //add disabled not add input disabled style
                personDisable($cgoPsnEle,'','Y');
                var psnEditDto = data.psnEditDto;
                setPsnDisabled($cgoPsnEle,psnEditDto);
            }else{
                var $cgoPsnEle = $CurrentPsnEle.find('.medAlertPerson');
                //add disabled not add input disabled style
                personDisable($cgoPsnEle,'','Y');
                var psnEditDto = data.psnEditDto;
                setPsnDisabled($cgoPsnEle,psnEditDto);
            }
            $CurrentPsnEle.find('input[name="licPerson"]').val('1');
            $CurrentPsnEle.find('input[name="existingPsn"]').val('1');
        }else{
            if('CGO' == psnTYpe){
                unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
            }else{
                unDisabledPartPage($CurrentPsnEle.find('.medAlertPerson'));
            }
            $CurrentPsnEle.find('input[name="licPerson"]').val('0');
            $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
        }

    }
    <!--cgo,medAlert -->
    var loadSelectPsn = function ($CurrentPsnEle, idType, idNo, psnType) {
        var spcEle = $CurrentPsnEle.find('.specialty');
        var jsonData = {
            'idType':idType,
            'idNo':idNo,
            'psnType':psnType
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/svc-code',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                fillPsnForm($CurrentPsnEle,data,psnType);
            },
            'error':function () {
            }
        });
    }

    var setPsnDisabled = function ($cgoPsnEle,psnEditDto) {
        console.log("setPsnDisabled start...");
        console.log(psnEditDto);
        if(psnEditDto == 'undefined' || psnEditDto == '' || psnEditDto == null){
            console.log('psnEditDto is empty or undefind');
            return;
        }
        //dropdown
        if(psnEditDto.salutation){
            $cgoPsnEle.find('div.salutationSel').removeClass('disabled');
        }
        if(psnEditDto.idType){
            $cgoPsnEle.find('div.idTypeSel').removeClass('disabled');
        }
        if(psnEditDto.designation){
            $cgoPsnEle.find('div.designationSel').removeClass('disabled');
        }
        if(psnEditDto.professionType){
            $cgoPsnEle.find('div.professionTypeSel').removeClass('disabled');
        }
        if(psnEditDto.speciality){
            $cgoPsnEle.find('div.specialty').removeClass('disabled');
        }
        //input text
        if(psnEditDto.name){
            $cgoPsnEle.find('input[name="name"]').prop('disabled',false);
        }
        if(psnEditDto.idNo){
            $cgoPsnEle.find('input[name="idNo"]').prop('disabled',false);
        }
        if(psnEditDto.mobileNo){
            $cgoPsnEle.find('input[name="mobileNo"]').prop('disabled',false);
        }
        if(psnEditDto.profRegNo){
            $cgoPsnEle.find('input[name="professionRegoNo"]').prop('disabled',false);
        }
        if(psnEditDto.specialityOther){
            $cgoPsnEle.find('input[name="specialtyOther"]').prop('disabled',false);
        }
        if(psnEditDto.subSpeciality){
            $cgoPsnEle.find('input[name="qualification"]').prop('disabled',false);
        }
        if(psnEditDto.emailAddr){
            $cgoPsnEle.find('input[name="emailAddress"]').prop('disabled',false);
        }
        //map->mode
        if(psnEditDto.preferredMode){
            $cgoPsnEle.find('input[type="checkbox"]').prop('disabled',false);
        }

        //for disabled add style
        $cgoPsnEle.find('input[type="text"]').each(function () {
            if($(this).prop('disabled')){
                $(this).css('border-color','#ededed');
                $(this).css('color','#999');
            }else{
                $(this).css('border-color','');
                $(this).css('color','');
            }
        });
        console.log("setPsnDisabled end...");
    }

</script>