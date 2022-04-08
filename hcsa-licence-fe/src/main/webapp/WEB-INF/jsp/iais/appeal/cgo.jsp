<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;margin-left: 2%">
  <div id="wizard-page-title">A Clinical Governance Officer (CGO) is a suitably qualified person appointed by the licensee and who is responsible for the oversight of clinical and technical matters related to the <iais:code code="CDN001"/> provided.</div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table aria-describedby="" class="control-grid columns1 table.assignContent" style="width: 100%;">
        <thead style="display: none">
        <tr><th scope="col"></th></tr>
        </thead>
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
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="section control  container-s-1">
              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>
              <div class="assignContent hideen-div hidden"></div>
              <c:if test="${CgoMandatoryCount >0}">
                <c:forEach  begin="0" end="${CgoMandatoryCount-1}"  step="1" varStatus="status" >
                  <c:set value="${GovernanceOfficersList}" var="cgoList"/>
                  <c:set value="cgo-${status.index}-" var="cgoIndeNo"/>
                  <c:set value="${cgoList[status.index]}" var="currentCgo"/>
                  <c:set value="${errorMap_governanceOfficers[status.index]}" var="errorMap"/>
                  <c:set value="${status.index}" var="suffix" />
                  <div class="cgo-content">
                  <table aria-describedby="" class="assignContent control-grid" style="width:100%;">
                    <thead style="display: none">
                    <tr><th scope="col"></th></tr>
                    </thead>
                    <input type="hidden" name="isPartEdit" value="0"/>
                    <input type="hidden" name="indexNo" value="${currentCgo.indexNo}"/>
                    <input type="hidden" name="existingPsn" value="0"/>
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
                            <div id="control--runtime--" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap" <c:if test="${status.first}">style="width:194%;"</c:if> >
                                <div class="col-sm-4 control-label formtext control">
                                  <div class="cgo-header">
                                    <strong>Clinical Governance Officer </strong>
                                  </div>
                                </div>
                                <div class="col-sm-8 text-right">
                                  <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                    <div class="">

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
                                  <div >
                                    <iais:select cssClass="assignSel"  name="assignSelect"  options="CgoSelectList" value="${currentCgo.assignSelect}"></iais:select>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_assignSelect${status.index}"></span>
                                  </div>
                                </div>
                              </div>
                            </div>
                        <div class="profile-info-gp hidden"></div>
                        <div id="newOfficer" class="new-officer-form hidden">
                          <table aria-describedby="" class="control-grid" >
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
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
                                        <iais:input maxLength="66" cssClass="field-name" type="text" name="name" value="${currentCgo.name}"></iais:input>
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
                                        <iais:select cssClass="idTypeSel"  name="idType" value="${currentCgo.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_idTyp${status.index}"></span>
                                      </div>
                                    </div>
                                    <div class="col-sm-5 col-md-4">
                                      <div class="">
                                        <iais:input maxLength="20" type="text" name="idNo" value="${currentCgo.idNo}"></iais:input>
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
                            <tr height="1" class="nationalityDiv">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext">
                                      <label class="control-label control-set-font control-font-label">Country of issuance</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="nationality${suffix}">
                                      <div class="">
                                        <iais:select firstOption="Please Select" name="nationality" codeCategory="CATE_ID_NATIONALITY"
                                                     cssClass="nationality" value="${currentCgo.nationality}" needErrorSpan="false"/>
                                        <span class="error-msg" name="iaisErrorMsg"
                                              id="error_nationality${status.index}"></span>
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
                              <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                                <div class="form-group form-horizontal formgap">
                                  <div class="control-label formtext col-sm-5">
                                  </div>
                                  <div class="col-md-7 col-xs-5 col-sm-3">
                                    <div class="">
                                      <input type="text" name="otherDesignation" value="${currentCgo.otherDesignation}" class="otherDesignation" maxlength="100" autocomplete="off">
                                      <span class="error-msg" name="iaisErrorMsg" id="error_otherDesignation${status.index}"></span>
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
                                        Professional Regn. No.
                                      </label>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <div class="">
                                        <input maxlength="20"  name="professionRegoNo" value="${currentCgo.profRegNo}" type="text" onblur="profRegNoBlur()">
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
                                    <div class="control-label formtext col-sm-5">
                                      <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">Specialty</label>
                                    </div>
                                    <div class="col-xs-8 col-sm-4 col-md-7">
                                      <label class="control-label control-set-font control-font-label specialty-label">${currentCgo.speciality}</label>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>

                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="control-label formtext col-sm-5">
                                      <label class="control-label control-set-font control-font-label">Sub-specialty</label>
                                    </div>
                                    <div class="col-md-7 col-xs-8 col-sm-4">
                                      <label class="control-label control-set-font control-font-label sub-specialty-label">${currentCgo.subSpeciality}</label>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="control-label formtext col-sm-5">
                                      <label class="control-label control-set-font control-font-label">Qualification</label>
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5">
                                      <label class="control-label control-set-font control-font-label qualification-label">${currentCgo.qualification}</label>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>

                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="control-label formtext col-sm-5">
                                      <label class="control-label control-set-font control-font-label">Other Qualification</label>
                                      <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5">
                                      <div class="">
                                        <input type="text" name="otherQualification" value="${currentCgo.otherQualification}" class="otherQualification" maxlength="100" autocomplete="off">
                                        <span class="error-msg" name="iaisErrorMsg" id="error_otherQualification${status.index}"></span>
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
                                    <div class="col-md-7 col-xs-9 col-sm-5">
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
                                        <iais:input maxLength="320" type="text" name="emailAddress" value="${currentCgo.emailAddr}"></iais:input>
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
                  </div>
                  <c:if test="${!status.last}">
                    <hr/>
                  </c:if>
                </c:forEach>
              </c:if>
            </div>
          </td>
        </tr>
        <hr/>
          <tr id="addInfo" <c:if test="${CgoMandatoryCount >0}">hidden </c:if>>
            <td>
              <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Clinical Governance Officer</span>
              <div> <span class="error-msg" id="error_addCgo" name="iaisErrorMsg"></span></div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>

<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-body" >
        <div class="row">
          <div class="col-md-12">
            <span style="font-size: 2rem;" id="prsErrorMsg">
              <%=MessageUtil.getMessageDesc("GENERAL_ERR0048")%>
            </span>
          </div>
        </div>
      </div>
      <div class="row " style="margin-top: 5%;margin-bottom: 5%">
        <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancel()">OK</button>
      </div>
    </div>
  </div>
</div>
<%@include file="../common/prsLoading.jsp"%>
<script>
    var init;
    $(document).ready(function () {
        $('.hideen-div').addClass('hidden');
        //coverage  cpl_custom_form_script init css
        $('.new-officer-form > table> tbody> tr:first-child > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-2');
        $('.new-officer-form > table> tbody> tr:first-child > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-3');

        $('.new-officer-form > table> tbody> tr:nth-child(3) > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-2');
        $('.new-officer-form > table> tbody> tr:nth-child(3) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-3');

        $('.new-officer-form > table> tbody> tr:nth-child(5) > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-3');
        $('.new-officer-form > table> tbody> tr:nth-child(5) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-4');


        reLoadChange();

        showSpecialty();

        doEdit();


        $('#control--runtime--0').children().remove("hr")
        psnSelect();
        $('.assignSel').trigger('change');
        init = 1;
        if($('.designationSel').val()=='DES999'){
            $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
        }else {
            $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
        }
        initNationality('div.cgo-content', 'select[name="idType"]', '.nationalityDiv');

    });



    var psnSelect = function () {
      $('select.assignSel').change(function () {
        var $parentEle = $(this).closest('td.first');
        var $CurrentPsnEle = $(this).closest('table.assignContent');
        if(init == 1){
          clearPrsInfo($CurrentPsnEle);
        }
        if ('newOfficer' == $(this).val()) {
          $parentEle.find('> .new-officer-form').removeClass('hidden');
          $parentEle.find('> .profile-info-gp').addClass('hidden');
          unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
          if (1 == init) {
            var emptyData = {};
            $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
            fillPsnForm($CurrentPsnEle, emptyData, 'CGO');
            showSpecialty();
            $CurrentPsnEle.find('input[name="licPerson"]').val('0');
            $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
          }
        } else if ('-1' == $(this).val()) {
          $parentEle.find('> .profile-info-gp').removeClass('hidden');
          $parentEle.find('> .new-officer-form').addClass('hidden');
          if (1 == init) {
            var emptyData = {};
            $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
            fillPsnForm($CurrentPsnEle, emptyData, 'CGO');
            showSpecialty();
            $CurrentPsnEle.find('input[name="licPerson"]').val('0');
            $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
          }
        } else {
          $parentEle.find('> .new-officer-form').removeClass('hidden');
          $parentEle.find('> .profile-info-gp').addClass('hidden');
          if (1 == init) {
            var arr = $(this).val().split(',');
            var nationality = arr[0];
            var idType = arr[1];
            var idNo = arr[2];
            loadSelectPsn($CurrentPsnEle, nationality, idType, idNo, 'CGO');
          }else {
            var $cgoPsnEle = $CurrentPsnEle.find('.new-officer-form');
            //add disabled not add input disabled style
            personDisable($cgoPsnEle,'','Y');
            $cgoPsnEle.find('div.designationSel').removeClass('disabled');
            $cgoPsnEle.find('div.professionTypeSel').removeClass('disabled');
            $cgoPsnEle.find('div.specialty').removeClass('disabled');

            $cgoPsnEle.find('input[name="professionRegoNo"]').prop('disabled',false);
            $cgoPsnEle.find('input[name="specialtyOther"]').prop('disabled',false);
            $cgoPsnEle.find('input[name="qualification"]').prop('disabled',false);
            $cgoPsnEle.find('input[name="otherQualification"]').prop('disabled',false);
            $cgoPsnEle.find('input[name="otherDesignation"]').prop('disabled',false);
            $cgoPsnEle.find('input[name="description"]').prop('disabled',false);


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
          }
        }
      });
    }

    var showSpecialty = function () {
        $('.specialty').change(function () {
            $specialtyEle = $(this).closest('.specialtyContent');
            var val = $(this).val();

            if('other' == val){
                $specialtyEle.find('input[name="specialtyOther"]').removeClass('hidden');
            }else{
                $specialtyEle.find('input[name="specialtyOther"]').addClass('hidden');
            }
        });
    }

    var reLoadChange = function () {
        var i=0;
        $('select.assignSel').each(function (k,v) {
            if("newOfficer" ==$(this).val()){
                var removeClass = '.cgo-'+i+'--new';
                console.log("removeClass"+removeClass);
                $(removeClass).removeClass('hidden');
                i++;
            }
        });
    };

    $('.addListBtn').click(function () {
        /*var assignContent = $('.assignContent:last').html();
        var appendHtml = '<hr/> <table aria-describedby="" class="testTable">'+ assignContent+'</table>';
        $('.assignContent:last').after(appendHtml);*/
        $('.hideen-div').addClass('hidden');
        $('.addListBtn').addClass('hidden');
        $.ajax({
            'url':'${pageContext.request.contextPath}/governance-officer',
            'dataType':'text',
            'type':'GET',
            'success':function (data) {
                console.log("suc");
                var length = $('.assignContent').length;
                if(length>0){
                    data = "<hr/>" + data;
                }
                $('.assignContent:last').after(data);
                showSpecialty();
                psnSelect();
                <!--change psn item -->
                changePsnItem();
                designationChange();
                profRegNoBlur();
                initNationality('div.cgo-content:last', 'select[name="idType"]', '.nationalityDiv');

            },
            'error':function (data) {
                console.log("err");
            }
        });
    });

    var doEdit = function () {
        $('#edit').click(function () {
            /*$assignContentEle = $(this).closest('div.assignContent');
            $assignContentEle.find('input[type="text"]').prop('disabled',false);
            $assignContentEle.find('div.nice-select').removeClass('disabled');*/
            $('input[type="text"]').prop('disabled',false);
            $('div.nice-select').removeClass('disabled');
            $('#isEditHiddenVal').val('1');
            $('#edit-content').addClass('hidden');
        });
    };

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k,v) {
          if(k!==0){
            $(this).html(k+1);
          }
        });

    }


    const loadings = function (data,obj) {
        const qualification = data.qualification[0];
        const specialty = data.specialty[0];
        const $CurrentPsnEle = $(obj).closest('table.assignContent');
        if (specialty == 'Pathology') {
            $CurrentPsnEle.find("input[name='specialty'] option[text =specialty]").val("selected", "selected");
            $CurrentPsnEle.find("select[name='specialty']").val(specialty);
            $CurrentPsnEle.find(".specialtyDiv .current").text(specialty);
            $CurrentPsnEle.find("input[name='specialtyOther']").addClass('hidden');
        } else if (specialty == 'Haematology') {
            $CurrentPsnEle.find("input[name='specialty'] option[text =specialty]").val("selected", "selected");
            $CurrentPsnEle.find("select[name='specialty']").val(specialty);
            $CurrentPsnEle.find(".specialtyDiv .current").text(specialty);
            $CurrentPsnEle.find("input[name='specialtyOther']").addClass('hidden');
        }else if (specialty == 'Diagnostic Radiology') {
            $CurrentPsnEle.find("input[name='specialty'] option[text =specialty]").val("selected", "selected");
            $CurrentPsnEle.find("select[name='specialty']").val(specialty);
            $CurrentPsnEle.find(".specialtyDiv .current").text(specialty);
            $CurrentPsnEle.find("input[name='specialtyOther']").addClass('hidden');
        }else if (specialty == 'Nuclear Medicine') {
            $CurrentPsnEle.find("input[name='specialty'] option[text =specialty]").val("selected", "selected");
            $CurrentPsnEle.find("select[name='specialty']").val(specialty);
            $CurrentPsnEle.find(".specialtyDiv .current").text(specialty);
            $CurrentPsnEle.find("input[name='specialtyOther']").addClass('hidden');
        } else {
            $CurrentPsnEle.find("input[name='specialty'] option[text =specialty]").val("selected", "selected");
            $CurrentPsnEle.find("select[name='specialty']").val('other');
            $CurrentPsnEle.find(".specialtyDiv .current").text("Others");
            $CurrentPsnEle.find("input[name='specialtyOther']").removeClass('hidden');
            $CurrentPsnEle.find("input[name='specialtyOther']").val(specialty);
        }
        $CurrentPsnEle.find('input[name="qualification"]').val(qualification);
    };
    function cancel() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }
    var designationChange = function () {
        $('.designationSel').change(function () {
            var thisVal = $(this).val();
            if("DES999" == thisVal){
                $(this).closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
            }else{
                $(this).closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
            }
        });
    };
    $('.designationSel').change(function () {
        var thisVal = $(this).val();
        if("DES999" == thisVal){
            $(this).closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
        }else{
            $(this).closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
        }
    });
    var profRegNoBlur = function () {
        $('input[name="professionRegoNo"]').blur(function(){
            var prgNo = $(this).val();
            var $currContent = $(this).closest('.new-officer-form');
            var $prsLoadingContent = $(this).closest('table.assignContent');
            var specialty = $prsLoadingContent.find('label.specialty-label').html();
            //prs loading
            if(init == 1){
                prdLoading($prsLoadingContent,prgNo);
            }
            //add Remark For Subspecialty
            if(prgNo.trim().length == 0 || specialty.trim().length == 0){
                $currContent.find('span.otherQualificationSpan').html('*');
            }
        });
    };

    var clearPrsInfo = function ($loadingContent) {
        $loadingContent.find('.specialty-label').html('');
        $loadingContent.find('.sub-specialty-label').html('');
        $loadingContent.find('.qualification-label').html('');
        $loadingContent.find('span.otherQualificationSpan').html('*');
    };



    function initNationality(parent, idTypeTag, nationalityDiv) {
      $(parent).find(idTypeTag).on('change', function () {
        var $content = $(this).closest(parent.replace(':last', ''));
        toggleIdType(this, $content.find(nationalityDiv));
      });
      $(parent).each(function (index, ele) {
        toggleIdType($(ele).find(idTypeTag), $(ele).find(nationalityDiv));
      });
    }

    function toggleIdType(sel, elem) {
      if (isEmpty(sel) || isEmpty(elem)) {
        return;
      }
      var $sel = $(sel);
      var $elem = $(elem);
      if ($sel.length == 0 || $elem.length == 0) {
        return;
      }
      console.log($sel.val());
      if ($sel.val() == 'IDTYPE003') {
        $elem.removeClass('hidden');
      } else {
        $elem.addClass('hidden');
        clearFields($elem);
      }
    }

    function unDisabledPartPage($Ele){
      $Ele.find('input[type="radio"]').prop('disabled',false);
      $Ele.find('input[type="text"]').prop('disabled',false);
      $Ele.find('input[type="file"]').prop('disabled',false);
      $Ele.find('input[type="checkbox"]').prop('disabled',false);
      $Ele.find('div.nice-select').removeClass('disabled');
      $Ele.find('input[type="text"]').css('border-color','');
      $Ele.find('input[type="text"]').css('color','');
      <!--multi -->
      $Ele.find('div.multi-select input').prop('disabled',false);
    }

    <!--cgo,medAlert -->
    var fillPsnForm = function ($CurrentPsnEle,data,psnType) {
      <!--salutation-->
      var salutation  = data.salutation;
      if( salutation == null || salutation =='undefined' || salutation == '' || salutation == '-'){
        salutation = '';
      }
      $CurrentPsnEle.find('select[name="salutation"]').val(salutation);
      var salutationVal = $CurrentPsnEle.find('option[value="' + salutation + '"]').html();
      $CurrentPsnEle.find('select[name="salutation"]').next().find('.current').html(salutationVal);
      <!--name-->
      $CurrentPsnEle.find('input[name="name"]').val(data.name);

      <!-- idType-->
      fillValue($CurrentPsnEle.find('select[name="idType"]'), data.idType);
      <!-- idNo-->
      $CurrentPsnEle.find('input[name="idNo"]').val(data.idNo);
      <!-- Nationality -->
      fillValue($CurrentPsnEle.find('select[name="nationality"]'), data.nationality);
      toggleIdType($CurrentPsnEle.find('select[name="idType"]'), $CurrentPsnEle.find('.nationalityDiv'));

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

      if('DES999' == designation){
        $CurrentPsnEle.find('div.otherDesignationDiv').removeClass('hidden');
        $CurrentPsnEle.find('input[name="otherDesignation"]').val(data.otherDesignation);
      }else{
        $CurrentPsnEle.find('div.otherDesignationDiv').addClass('hidden');
      }

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
      /*<!-- speciality-->
      var speciality = data.speciality;
      console.log('speciality'+speciality);
      $CurrentPsnEle.find('.specialty-label').html(speciality);
      <!--Subspeciality -->
      var subSpeciality = data.subSpeciality;
      $CurrentPsnEle.find('.sub-specialty-label').html(subSpeciality);
      <!--qualification -->
      var qualification = data.qualification;
      $CurrentPsnEle.find('.qualification-label').html(qualification);*/
      var otherQualification = data.otherQualification;
      if(otherQualification != null && otherQualification !='undefined' && otherQualification != ''){
        $CurrentPsnEle.find('input[name="otherQualification"]').val(otherQualification);
      }else{
        $CurrentPsnEle.find('input[name="otherQualification"]').val('');
      }

      <!--preferredMode -->
      var description = data.description;
      if(description != null && description !='undefined' && description != ''){
        $CurrentPsnEle.find('input[name="description"]').val(data.description);
      }else{
        $CurrentPsnEle.find('input[name="description"]').val('');
      }

      var isLicPerson = data.licPerson;
      if('1' == isLicPerson){
        if('CGO' == psnType){
          var $cgoPsnEle = $CurrentPsnEle.find('.new-officer-form');
          //add disabled not add input disabled style
          personDisable($cgoPsnEle,'','Y');
          var psnEditDto = data.psnEditDto;
          setPsnDisabled($cgoPsnEle,psnEditDto);
        }
        $CurrentPsnEle.find('input[name="licPerson"]').val('1');
        $CurrentPsnEle.find('input[name="existingPsn"]').val('1');
      }else{
        if('CGO' == psnType){
          unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
        }
        $CurrentPsnEle.find('input[name="licPerson"]').val('0');
        $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
      }
      //reload data by prs again
      if('CGO' == psnType) {
        $CurrentPsnEle.find('input[name="professionRegoNo"]').trigger('blur', 'psnSelect');
      }
    };
    <!--cgo,medAlert -->
    var loadSelectPsn = function ($CurrentPsnEle, nationality, idType, idNo, psnType, callback) {
      showWaiting();
      var spcEle = $CurrentPsnEle.find('.specialty');
      var jsonData = {
        'nationality':nationality,
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
          if (typeof callback === 'function') {
            callback($CurrentPsnEle, data, psnType);
          } else {
            fillPsnForm($CurrentPsnEle, data, psnType);
          }
          dismissWaiting();
        },
        'error':function () {
          dismissWaiting();
        }
      });
    };
    function personDisable($Ele,inpDisStyle,disableDiv){
      inputDisabled($Ele);
      if(inpDisStyle == 'Y'){
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');
      }
      if(disableDiv == 'Y'){
        $Ele.find('div.nice-select').addClass('disabled');
      }
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
      if(psnEditDto.nationality) {
        $cgoPsnEle.find('div.nationality').removeClass('disabled');
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
      if(psnEditDto.otherQualification){
        $cgoPsnEle.find('input[name="otherQualification"]').prop('disabled',false);
      }
      if(psnEditDto.otherDesignation){
        $cgoPsnEle.find('input[name="otherDesignation"]').prop('disabled',false);
      }
      //map->mode
      if(psnEditDto.description){
        $cgoPsnEle.find('input[name="description"]').prop('disabled',false);
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
    function inputDisabled($Ele) {
      $Ele.find('input[type="radio"]').prop('disabled',true);
      $Ele.find('input[type="text"]').prop('disabled',true);
      $Ele.find('input[type="file"]').prop('disabled',true);
      $Ele.find('input[type="checkbox"]').prop('disabled',true);
    }
</script>