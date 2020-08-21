<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title"></div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table class="control-grid columns1" style="width:100%;">
        <tbody>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--85" class="control control-caption-horizontal">
            </div>
          </td>
        </tr>
        <tr>
        </tr>
        <tr height="1">
          <td class="" style="width: 100%;">
            <div id="control--runtime--1" class="section control  container-s-1">
              <div class="control-set-font control-font-header control-font-header section-header">
                <c:choose>
                  <c:when test="${'BLB' ==currentSvcCode}">
                    <h4>The blood donation centre and/or mobile donation drive is/are under the supervision of</h4>
                  </c:when>
                  <c:when test="${'TCB' ==currentSvcCode}">
                    <h4>There is an appointed Laboratory Director who has the appropriate qualifications</h4>
                  </c:when>
                  <c:when test="${'NMI' ==currentSvcCode}">
                    <h4>The Nuclear Medicine Imagine Service have the following personnel that satisfy the minimum requirements at all times</h4>
                  </c:when>
                  <c:when test="${'NMA' ==currentSvcCode}">
                    <h4>The Nuclear Medicine Assay Service have the following personnel that satisfy the minimum requirements at all times</h4>
                  </c:when>
                </c:choose>
              </div>

              <div class="personnel-edit">
                <c:if test="${AppSubmissionDto.needEditController }">
                  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPSPN07' == clickEditPage}">
                      <c:set var="isClickEdit" value="true"/>
                    </c:if>
                  </c:forEach>
                  <c:choose>
                    <c:when test="${'true' != isClickEdit}">
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                      <p><div class="text-right app-font-size-16"><a class="back" id="RfcSkip">Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em></a></div></p>
                  </c:if>
                  <c:if test="${'true' != isClickEdit}">
                    <c:set var="locking" value="true"/>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                    <div id="edit-content">
                      <c:choose>
                        <c:when test="${'true' == canEdit}">
                            <p><div class="text-right app-font-size-16"><a id="edit"><em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit</a></div></p>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </c:if>
                </c:if>
              </div>

              <c:if test="${ServicePersonnelMandatory>0}">
                <c:set var="spMandatoryCount" value="${spHcsaSvcPersonnelDto.mandatoryCount}"/>
                <c:forEach begin="0" end="${ServicePersonnelMandatory-1}" step="1" varStatus="status">
                  <c:if test="${AppSvcPersonnelDtoList != null && AppSvcPersonnelDtoList.size()>0}">
                    <c:set value="${AppSvcPersonnelDtoList[status.index]}" var="appSvcPersonnelDto"/>
                  </c:if>
                  <table class="personnel-content" id="personnelRemoveId${status.index}">
                    <tbody>
                    <tr height="1" class="personnel-header">
                      <td class="" style="width: 100%;">
                        <div  class="personnel-header control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-5 control-label formtext ">
                              <div class="cgo-header">
                                <strong>Service Personnel <label class="assign-psn-item">${status.index+1}</label></strong>
                              </div>
                            </div>
                            <div class="col-sm-5 col-md-7 text-right">
                              <c:if test="${status.index - spMandatoryCount >=0}">
                                <h4 class="text-danger"><em class="fa fa-times-circle removeSpBtn cursorPointer"></em></h4>
                              </c:if>
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                    <%--<tr><td style="text-align:right;">
                      <c:if test="${status.count > 1}">
                        <h4 class="text-danger"><em class="fa fa-times-circle" onclick="$('#personnelRemoveId${status.index}').remove();removeSp()"></em></h4>
                      </c:if>
                    </td></tr>--%>
                    <tr height="1">
                      <td class="" >
                        <div id="control--runtime--2" class="control control-caption-horizontal svcPsnSel">
                          <div class=" form-group form-horizontal personnel-sel">
                            <div class="col-sm-5 control-label formtext ">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Select Service Personnel</label>
                              <span class="mandatory">*</span>
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5 col-md-7">
                              <div class="">
                                <iais:select cssClass="personnelSel"  name="personnelSel" options="ServicePersonnelType" value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
                                <span class="error-msg" name="iaisErrorMsg" id="error_personnelSelErrorMsg${status.index}" ></span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="new-svc-personnel-form">
                          <table class="control-grid" style="width:100%;">
                            <tbody>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-name hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Name</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="66" type="text" name="name"  value="${appSvcPersonnelDto.name}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="" style="width: 100%;">
                                <div  class="personnel-designation hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Designation</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:select  name="designation" options="NuclearMedicineImagingDesignation"  value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-qualification hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Qualification</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="100" type="text" name="qualification"  value="${appSvcPersonnelDto.qualification}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-regnNo hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Professional Regn No. </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="20" type="text" name="regnNo"  value="${appSvcPersonnelDto.profRegNo}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_regnNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-wrkExpYear hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Relevant working experience (Years)</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="2" type="text" name="wrkExpYear"  value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_wrkExpYear${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            </tbody>
                          </table>
                        </div>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </c:forEach>
              </c:if>
              <c:if test="${requestInformationConfig==null}">
                <c:choose>
                  <c:when test="${!empty AppSvcPersonnelDtoList}">
                    <c:set var="spDtoLength" value="${AppSvcPersonnelDtoList.size()}"/>
                  </c:when>
                  <c:otherwise>
                    <c:set var="spDtoLength" value="1"/>
                  </c:otherwise>
                </c:choose>
                <c:set var="needAddPsn" value="true"/>
                <c:choose>
                  <c:when test="${spHcsaSvcPersonnelDto.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                  <c:when test="${spDtoLength >= spHcsaSvcPersonnelDto.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                </c:choose>
                <div id="addPsnDiv" class="row <c:if test="${!needAddPsn}">hidden</c:if>">
                  <div class="col-sm-5">
                    <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Service Personnel</span>
                  </div>
                  <div  class="col-sm-5 col-md-5">
                    <span class="spErrorMsg" style="color: red;"></span>
                  </div>
                </div>
              </c:if>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
    var init = 0;
  $(document).ready(function () {

      pageController('');

      <%--if(${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && !isClickEdit}){--%>
          <%--disabledPage();--%>
          <%--$('.addListBtn').addClass('hidden');--%>
      <%--}--%>

      <!--for reload when have personnelSel-->
      <c:if test="${'NMI' ==currentSvcCode || 'NMA'== currentSvcCode}">
      $('.personnel-content').each(function (k,v) {
          var personnelSel = $(this).find('.personnelSel').val();
          var $personnelContentEle = $(this);
          personnelSelFun(personnelSel,$personnelContentEle);
      });
      </c:if>

      if(${AppSubmissionDto.needEditController && !isClickEdit}){
          disabledPage();
          $('.addListBtn').addClass('hidden');
          $('.text-danger').addClass('hidden');
      }

      doEdit();
      spRemove();

      init = 1;
  });

  var absencePsnSel = function (val) {
      $('.svcPsnSel').addClass('hidden');
      $('.personnel-content').each(function (k,v) {
          if('Blood Banking' == val){
              $(this).find('div.personnel-sel').addClass('hidden');
              $(this).find('div.new-svc-personnel-form').removeClass('hidden');
              $(this).find('div.personnel-designation').removeClass('hidden');
              $(this).find('div.personnel-name').removeClass('hidden');
              $(this).find('div.personnel-qualification').addClass('hidden');
              $(this).find('div.personnel-regnNo ').removeClass('hidden');
              $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
          }else if('Tissue Banking p1' == val){
              $(this).find('div.personnel-sel').addClass('hidden');
              $(this).find('div.new-svc-personnel-form').removeClass('hidden');
              $(this).find('div.personnel-designation').addClass('hidden');
              $(this).find('div.personnel-name').removeClass('hidden');
              $(this).find('div.personnel-qualification').removeClass('hidden');
              $(this).find('div.personnel-regnNo ').addClass('hidden');
              $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
          }
      });
  }

  var personnelSel = function(){
      $('.personnelSel').change(function () {
          var personnelSel = $(this).val();
          var $personnelContentEle = $(this).closest('table.personnel-content');
          if(init != 0){
              //clear data;
              $personnelContentEle.find('div.personnel-designation select[name="designation"]').val('');
              var designation = $personnelContentEle.find('div.personnel-designation  option[value=""]').html();
              $personnelContentEle.find('select[name="designation"]').next().find('.current').html(designation);

              $personnelContentEle.find('div.personnel-name input[name="name"]').val('');
              $personnelContentEle.find('div.personnel-regnNo input[name="regnNo"]').val('');
              $personnelContentEle.find('div.personnel-wrkExpYear input[name="wrkExpYear"]').val('');
              $personnelContentEle.find('div.personnel-qualification input[name="qualification"]').val('');
          }
          personnelSelFun(personnelSel,$personnelContentEle);
      });
  };

  var personnelSelFun = function(personnelSel,$personnelContentEle){
      $personnelContentEle.find('div.new-svc-personnel-form ').removeClass('hidden');

      if('SPPT001' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').removeClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT002' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT003' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT004' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').removeClass('hidden');
      }else if('' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').addClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
      }
  }


  $('.addListBtn').click(function () {
      var HasNumber = $(".personnel-content").size();
      console.log("HasNumber"+HasNumber);
      $.ajax({
          url:'${pageContext.request.contextPath}/nuclear-medicine-imaging-html',
          dataType:'json',
          data:{
              'HasNumber':HasNumber
          },
          type:'POST',
          success:function (data) {
              if ("success" == data.res){
                  $('.personnel-content:last').after(data.sucInfo);
                  pageController($('.personnel-content:last'));
                  spRemove();
                  //hidden add more
                  var psnLength = $('.personnel-content').length;
                  if(psnLength >='${spHcsaSvcPersonnelDto.maximumCount}'){
                      $('#addPsnDiv').addClass('hidden');
                  }
              }else{
                  $('.spErrorMsg').html(data.errInfo);
              }
          },
          error:function (data) {
          }
      });
  });

  function removeSp(){
      console.log("SP --->");
      $('.spErrorMsg').html("");
  }

  var pageController = function ($Ele) {
      <c:choose>
      <c:when test="${'NMI' ==currentSvcCode || 'NMA'== currentSvcCode}">
      personnelSel();
      if($Ele == ''){
          //triggering event
          $('.personnelSel').trigger('change');
      }else {
          $Ele.find('.personnelSel').trigger('change');
      }
      </c:when>
      <c:when test="${'BLB' ==currentSvcCode}">
      absencePsnSel('Blood Banking');
      </c:when>
      <c:when test="${'TCB'== currentSvcCode}">
      absencePsnSel('Tissue Banking p1');
      </c:when>
      </c:choose>
  }


  var doEdit = function () {
      $('#edit').click(function () {
          unDisabledPage();
          $('#isEditHiddenVal').val('1');
          $('#edit-content').addClass('hidden');
          $('.addListBtn').removeClass('hidden');
          $('.text-danger').removeClass('hidden');
      });
  }
var spRemove = function(){
    $('.removeSpBtn').click(function () {
        var $psnContentEle= $(this).closest('.personnel-content');
        $psnContentEle.remove();
        //reset number
        $('.personnel-content').each(function (k,v) {
            $(this).find('.assign-psn-item').html(k+1);
        });

        //show add more
        var psnLength = $('.personnel-content').length;
        if(psnLength <'${spHcsaSvcPersonnelDto.maximumCount}'){
            $('#addPsnDiv').removeClass('hidden');
        }
    });
}


</script>
