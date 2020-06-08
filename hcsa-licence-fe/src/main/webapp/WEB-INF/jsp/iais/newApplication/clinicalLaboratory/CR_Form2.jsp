<style>
  table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number], table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
    margin-bottom: 15px;margin-top: 25px;
  }

  .control-font-label{
    margin-top: 19px;
  }
</style>

<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <h4>A Clinical Governance Officer is responsible for the clinical and technical oversight of a medical service.</h4>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table class="control-grid columns1 " style="width: 100%;">
        <tbody>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--85" class="control control-caption-horizontal">
            </div>
          </td>
        </tr>
        <tr>
        </tr>
        <tr >
          <td class="first last" style="width: 100%;">
            <div class="section control  container-s-1">
              <table class="assignContent control-grid">
                <tbody>
                <tr height="1">
                  <td class="first last" style="width: 100%;">
                    <div id="" class="control control-caption-horizontal">
                      <div class="form-group form-horizontal formgap">
                        <div class="col-sm-4">
                          <strong style="font-size: 20px;">Clinical Governance Officers</strong>
                        </div>
                        <div class="col-sm-8 text-right">
                          <c:if test="${AppSubmissionDto.needEditController }">
                            <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                              <c:if test="${'APPSPN02' == clickEditPage}">
                                <c:set var="isClickEdit" value="true"/>
                              </c:if>
                            </c:forEach>
                            <c:choose>
                              <c:when test="${'true' != isClickEdit && !('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
                                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                              </c:when>
                              <c:otherwise>
                                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                              </c:otherwise>
                            </c:choose>
                            <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                              <div class="ack-font-16">
                                <a class="back" id="RfcSkip">Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em></a>
                              </div>
                            </c:if>
                            <c:if test="${'true' != isClickEdit}">
                              <c:set var="locking" value="true"/>
                              <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                            </c:if>
                          </c:if>
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
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="section control  container-s-1">
              <div class="control-set-font control-font-header section-header"></div>
              <c:if test="${CgoMandatoryCount >0}">
                <c:forEach  begin="0" end="${CgoMandatoryCount-1}"  step="1" varStatus="status" >
                  <c:set value="${GovernanceOfficersList}" var="cgoList"/>
                  <c:set value="cgo-${status.index}-" var="cgoIndeNo"/>
                  <c:set value="${cgoList[status.index]}" var="currentCgo"/>
                  <c:set value="${errorMap_governanceOfficers[status.index]}" var="errorMap"/>
                  <c:set value="${status.index}" var="suffix" />
                  <div class="cgo-content">
                  <table class="assignContent control-grid" style="width:100%;">
                    <input type="hidden" name="isPartEdit" value="0"/>
                    <input type="hidden" name="cgoIndexNo" value="${currentCgo.cgoIndexNo}"/>
                    <c:choose>
                      <c:when test="${currentCgo.licPerson}">
                        <input type="hidden" name="licPerson" value="1"/>
                      </c:when>
                      <c:otherwise>
                        <input type="hidden" name="licPerson" value="0"/>
                      </c:otherwise>
                    </c:choose>
                    <tbody>
                    <tr height="1">
                      <td class="first last" style="width: 100%;">
                        <c:choose>
                          <c:when test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                            <div id="control--runtime--2" class="control control-caption-horizontal">
                              <c:if test="${currentCgo != null}">
                                <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext control">
                                    <div class="cgo-header">
                                      <strong>Clinical Governance Office <label class="assign-psn-item">${status.index+1}</label></strong>
                                    </div>
                                  </div>
                                  <div class="col-sm-8 text-right">
                                    <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                      <div class="">
                                        <h4 class="text-danger"><em class="fa fa-times-circle removeBtn cursorPointer"></em></h4>
                                      </div>
                                    </c:if>
                                    <div class="hidden">
                                      <iais:select cssClass="assignSel"  name="assignSelect"  options="CgoSelectList" value="${currentCgo.assignSelect}"></iais:select>
                                    </div>
                                  </div>
                                  <div class="col-sm-10">
                                    <label class="control-font-label">${currentCgo.name}, ${currentCgo.idNo} (${currentCgo.idType})</label>
                                  </div>
                                  <div class="col-sm-2" style="margin-top:3%;">
                                    <div class="edit-content">
                                      <c:if test="${'true' == canEdit}">
                                        <p class="text-right"><a class="edit"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a></p>
                                      </c:if>
                                    </div>
                                  </div>
                                </div>
                              </c:if>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <div id="control--runtime--" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap" <c:if test="${status.first}">style="width:194%;"</c:if> >
                                <div class="col-sm-4 control-label formtext control">
                                  <div class="cgo-header">
                                    <strong>Clinical Governance Office <label class="assign-psn-item">${status.index+1}</label></strong>
                                  </div>
                                </div>
                                <div class="col-sm-8 text-right">
                                  <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                    <div class="">
                                      <h4 class="text-danger"><em class="fa fa-times-circle cursorPointer removeBtn"></em></h4>
                                    </div>
                                  </c:if>
                                </div>
                              </div>
                            </div>
                            <div id="control--runtime--2" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-5 control-label formtext ">
                                  <label id="control--runtime--2--label" class="control-label control-set-font control-font-label" >Add/Assign a Clinical Governance Officer</label>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5 col-md-7" id="assignSelect${suffix}">
                                  <div class="">
                                    <iais:select cssClass="assignSel"  name="assignSelect"  options="CgoSelectList" value="${currentCgo.assignSelect}"></iais:select>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_assignSelect${status.index}"></span>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </c:otherwise>
                        </c:choose>
                        <div class="profile-info-gp hidden"></div>
                        <div id="newOfficer" class="new-officer-form hidden">
                          <table class="control-grid" >
                            <tbody>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">
                                        Name
                                      </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-3" id="salutation${suffix}">
                                      <iais:select cssClass="salutationSel"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${currentCgo.salutation}" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_salutation${status.index}"></span>
                                    </div>
                                    <div class="col-sm-3 col-md-4" id="name${suffix}">
                                      <div class="">
                                        <iais:input maxLength="66" type="text" name="name" value="${currentCgo.name}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--28" class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                      <label id="control--runtime--28--label" class="control-label control-set-font control-font-label">
                                        ID No.
                                      </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-3" id="idType${suffix}">
                                      <div class="">
                                        <iais:select cssClass="idTypeSel"  name="idType" value="${currentCgo.idType}" options="IdTypeSelect"></iais:select>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_idTyp${status.index}"></span>
                                      </div>
                                    </div>
                                    <div class="col-sm-5 col-md-4">
                                      <div class="">
                                        <iais:input maxLength="9" type="text" name="idNo" value="${currentCgo.idNo}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMSg" id="error_idNo${status.index}"></span>
                                        <span class="error-msg" name="iaisErrorMSg" id="error_idNo"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="" class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <span class="error-msg" name="iaisErrorMSg" id="error_idTypeNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext">
                                      <label class="control-label control-set-font control-font-label">Designation</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="designation${suffix}">
                                      <div class="">
                                        <iais:select cssClass="designationSel" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${currentCgo.designation}" firstOption="Please Select"></iais:select>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext">
                                      <label  class="control-label control-set-font control-font-label">Professional Type</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="professionType${suffix}">
                                      <div class="professionRegoType">
                                        <iais:select cssClass="professionTypeSel" name="professionType" codeCategory="CATE_ID_PROFESSIONAL_TYPE" value="${currentCgo.professionType}" firstOption="Please Select"></iais:select>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_professionType${status.index}"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--31" class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                      <label id="control--runtime--31--label" class="control-label control-set-font control-font-label">
                                        Professional Regn No.
                                      </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <div class="">
                                        <iais:input maxLength="20" type="text" name="professionRegoNo" value="${currentCgo.profRegNo}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_professionRegoNo${status.index}"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--29" class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext">
                                      <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">Specialty</label>
                                      <span class="mandatory">*</span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="specialty${suffix}">
                                      <div class="specialtyContent">
                                        <div class="specialtyDiv">
                                          <c:choose>
                                            <c:when test="${currentCgo.needSpcOptList}">
                                              ${currentCgo.specialityHtml}
                                              <span class="error-msg" name="iaisErrorMsg" id="error_speciality${status.index}"></span>
                                            </c:when>
                                            <c:otherwise>
                                              <iais:select cssClass="specialty" name="specialty" options="SpecialtySelectList" value="${currentCgo.speciality}" ></iais:select>
                                              <span class="error-msg" name="iaisErrorMsg" id="error_speciality${status.index}"></span>
                                            </c:otherwise>
                                          </c:choose>
                                        </div>
                                        <div class="specialtyOtherDiv">
                                          <c:choose>
                                            <c:when test="${currentCgo.speciality eq 'other'}">
                                              <input name="specialtyOther" type="text"  class="form-control control-input control-set-font control-font-normal" value="${currentCgo.specialityOther}" maxlength="100">
                                              <span name="iaisErrorMsg" class="error-msg" id="error_other${status.index}"></span>
                                            </c:when>
                                            <c:otherwise>
                                              <input name="specialtyOther" type="text"  class="form-control control-input control-set-font control-font-normal hidden" value="" maxlength="100">
                                            </c:otherwise>
                                          </c:choose>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--32" class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                      <label id="control--runtime--32--label" class="control-label control-set-font control-font-label">
                                        Subspeciality or relevant qualification
                                      </label>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <div class="">
                                        <iais:input maxLength="100" type="text" name="qualification" value="${currentCgo.subSpeciality}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"><c:out value="${errorMap.qualification}"></c:out></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                        <label  class="control-label control-set-font control-font-label">Mobile No.</label>                                                                                                                                        <span class="mandatory">*</span>
                                        <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <div class="">
                                        <iais:input maxLength="8" type="text" name="mobileNo" value="${currentCgo.mobileNo}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_mobileNo${status.index}"></span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="">
                                <div id="control--runtime--33" class="control control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                        <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">Email Address</label>
                                        <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-4 col-md-7">
                                      <div class="">
                                        <iais:input maxLength="66" type="text" name="emailAddress" value="${currentCgo.emailAddr}"></iais:input>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr${status.index}"></span>
                                      </div>
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
                    <c:if test="${!status.last}">
                      <hr/>
                    </c:if>
                  </div>
                </c:forEach>
              </c:if>
            </div>
          </td>
        </tr>
        <c:if test="${'BLB'!=currentSvcCode && 'RDS'!=currentSvcCode && requestInformationConfig==null}">
          <tr id="addInfo">
            <td>
              <div class="col-sm-5 col-md-5">
                <span class="addListBtn" style="color:deepskyblue;cursor:pointer;"><span style="margin-left: -14px">+ Add Another Clinical Governance Officer</span></span>
              </div>
              <div  class="col-sm-5 col-md-5"><span class="errorMsg" style="color: red;"></span></div>
            </td>
          </tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
    var init;
    $(document).ready(function () {
        //init start
        init =0;
        $('.hideen-div').addClass('hidden');
        //init font-size
        $('.cgo-header').css('font-size',"18px");

        psnSelect();

        $('select.assignSel').trigger('change');

        // reLoadChange();

        showSpecialty();

        removeCgo();

        $('input[name="licPerson"]').each(function (k,v) {
            if('1' == $(this).val()){
                var $currentPsn = $(this).closest('.assignContent').find('.new-officer-form');
                disabledPartPage($currentPsn);
            }
        });

        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            disabledPage();
            //$('.addListBtn').addClass('hidden');
        }

        doEdit();

        //init end
        init =1;
    });

    var  psnSelect = function() {
        $('select.assignSel').change(function () {
            var $parentEle = $(this).closest('td.first');
            var $CurrentPsnEle = $(this).closest('table.assignContent');
            if ('newOfficer' == $(this).val()) {
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
                unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
                if(1 == init){
                    var emptyData = {};
                    $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
                    fillPsnForm($CurrentPsnEle,emptyData, 'CGO');
                    showSpecialty();
                    $CurrentPsnEle.find('input[name="licPerson"]').val('0');
                }
            } else if('-1' == $(this).val()) {
                $parentEle.find('> .profile-info-gp').removeClass('hidden');
                $parentEle.find('> .new-officer-form').addClass('hidden');
                if(1 == init){
                    var emptyData = {};
                    $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
                    fillPsnForm($CurrentPsnEle,emptyData, 'CGO');
                    showSpecialty();
                    $CurrentPsnEle.find('input[name="licPerson"]').val('0');
                }
            } else{
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
                if(1 == init){
                    var arr = $(this).val().split(',');
                    var idType = arr[0];
                    var idNo = arr[1];
                    loadSelectPsn($CurrentPsnEle, idType, idNo, 'CGO');
                }
            }
        });
    }

    var showSpecialty = function () {
        $('select.specialty').change(function () {
            var $specialtyEle = $(this).closest('.specialtyContent');
            var val = $(this).val();
            if('other' == val){
                $specialtyEle.find('input[name="specialtyOther"]').removeClass('hidden');
            }else{
                $specialtyEle.find('input[name="specialtyOther"]').addClass('hidden');
            }
        });
    }


    $('.addListBtn').click(function () {
        /*var assignContent = $('.assignContent:last').html();
        var appendHtml = '<hr/> <table class="testTable">'+ assignContent+'</table>';
        $('.assignContent:last').after(appendHtml);*/
        $('.hideen-div').addClass('hidden');
        var number = $('.assign-psn-item').size();
        var addNumber = ${HcsaSvcPersonnel.maximumCount} - number;
        $.ajax({
            url:'${pageContext.request.contextPath}/governance-officer-html',
            dataType:'json',
            data:{
                "HasNumber":number,
                "AddNumber":addNumber
            },
            type:'POST',
            success:function (data) {
                console.log(data.res);
                if ('success' == data.res) {
                    $('.assignContent:last').after(data.sucInfo);
                    showSpecialty();
                    psnSelect();
                    removeCgo();
                    //init font-size
                    $('.cgo-header').css('font-size',"18px");
                    <!--change psn item -->
                    changePsnItem();
                    <!--set Scrollbar -->
                    $("div.assignSel->ul").mCustomScrollbar({
                            advanced:{
                                updateOnContentResize: true
                            }
                        }
                    );
                }else{
                    $('.errorMsg').html(data.errInfo);
                }

            },
            error:function (data) {
                console.log("err");
            }
        });
    });

    var doEdit = function () {
        $('.edit').click(function () {
            var $contentEle = $(this).closest('.assignContent');
            $contentEle.find('input[name="isPartEdit"]').val('1');
            $contentEle.find('.edit-content').addClass('hidden');
            $contentEle.find('input[type="text"]').prop('disabled',false);
            $contentEle.find('div.nice-select').removeClass('disabled');
            $contentEle.find('input[type="text"]').css('border-color','');
            $contentEle.find('input[type="text"]').css('color','');
            $('#isEditHiddenVal').val('1');
        });
    }

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k,v) {
            $(this).html(k+1);
        });

    }
    var removeCgo = function () {
        $('.removeBtn').click(function () {
            var assignContentLength = $('table.assignContent').length -1;
            var $premContentEle= $(this).closest('div.cgo-content');
            if(assignContentLength <= 2){
                $('hr').remove();
            }
            $premContentEle.remove();
            $('.errorMsg').html("");

        });

    }
</script>