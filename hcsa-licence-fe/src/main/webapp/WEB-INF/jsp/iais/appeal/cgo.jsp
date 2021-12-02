<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>



<style>
  table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number], table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
    margin-bottom: 15px;margin-top: 25px;
  }

</style>
<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;margin-left: 2%">
  <div id="wizard-page-title">A Clinical Governance Officer is responsible for the clinical and technical oversight of a medical service.</div>
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
                                    <strong>Clinical Governance Officer <label class="assign-psn-item">${status.index+1}</label></strong>
                                  </div>
                                </div>
                                <div class="col-sm-8 text-right">
                                  <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                    <div class="">
                                      <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4>
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
                                        <iais:select cssClass="idTypeSel"  name="idType" value="${currentCgo.idType}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE"></iais:select>
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
        //get from cpl_custom_form_script
        $('select.assignSel').change(function () {
            $parentEle = $(this).closest('td.first');
            if ($(this).val() == "newOfficer") {
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
            } else {
                $parentEle.find('> .profile-info-gp').removeClass('hidden');
                $parentEle.find('> .new-officer-form').addClass('hidden');
            }
        });

        reLoadChange();

        showSpecialty();

        doEdit();

        $('#control--runtime--0').children().remove("hr")
        $('.assignSel ').trigger('change');
        init = 1;
        if($('.designationSel').val()=='DES999'){
            $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
        }else {
            $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
        }
    });

    var disabledAll = function () {
        $('input[type="text"]').prop('disabled',true);
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

                $('select.assignSel').change(function () {
                    $parentEle = $(this).closest('td.first');
                    if ($(this).val() == "newOfficer") {
                        $parentEle.find('> .new-officer-form').removeClass('hidden');
                        $parentEle.find('> .profile-info-gp').addClass('hidden');
                    } else {
                        $parentEle.find('> .profile-info-gp').removeClass('hidden');
                        $parentEle.find('> .new-officer-form').addClass('hidden');
                    }
                });
                <!--change psn item -->
                changePsnItem();
                designationChange();
                profRegNoBlur();
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
            $(this).html(k+1);
        });

    }

    function aaa(obj){
        var val = $(obj).val();
        $.getJSON("${pageContext.request.contextPath}/regNo-prs",{"regNo":val},function (data) {
            if (isEmpty(data)) {
                console.log("The return data is null for PRS");
            } else if('-1' == data.statusCode || '-2' == data.statusCode) {
                $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                $('#PRS_SERVICE_DOWN').modal('show');
            } else if (data.hasException) {
                $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                $('#PRS_SERVICE_DOWN').modal('show');
            } else if ('401' == data.statusCode) {
                $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                $('#PRS_SERVICE_DOWN').modal('show');
            } else {
                loadings(data,obj);
            }
        });
    };
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
    var prdLoading = function ($loadingContent,prgNo) {
        console.log('loading prs info ...');
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo($loadingContent);
            return;
        }
        var jsonData = {
            'prgNo': prgNo
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (isEmpty(data)) {
                    console.log("The return data is null for PRS");
                    clearPrsInfo($loadingContent);
                } else if('-1' == data.statusCode || '-2' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else if (data.hasException) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent);
                } else {
                    loadingData(data,$loadingContent);
                }
                dismissWaiting();
            },
            'error': function () {
                //
                clearPrsInfo($loadingContent);
            }
        });
    };

    var clearPrsInfo = function ($loadingContent) {
        $loadingContent.find('.specialty-label').html('');
        $loadingContent.find('.sub-specialty-label').html('');
        $loadingContent.find('.qualification-label').html('');
        $loadingContent.find('span.otherQualificationSpan').html('*');
    };

    var loadingData = function (data,$loadingContent) {
        loading(data.specialty,$loadingContent,'specialty-label');
        loading(data.subspecialty,$loadingContent,'sub-specialty-label');
        loading(data.qualification,$loadingContent,'qualification-label');

        addMandatoryForOtherQua(data.specialty,$loadingContent);
    };

    var addMandatoryForOtherQua = function (data,$loadingContent) {
        if(data == null || data == undefined || data == ''){
            $loadingContent.find('span.otherQualificationSpan').html('*');
        }else{
            $loadingContent.find('span.otherQualificationSpan').html('');
        }
    }

    var loading = function (dataArr,$loadingContent,labelClass) {
        var displayVal = "";
        if(dataArr != null && dataArr != undefined && dataArr != ''){
            $.each(dataArr,function (k,v) {
                displayVal = displayVal + v + ',';
            });
            var endLength = displayVal.length-1;
            displayVal = displayVal.substring(0,endLength);
        }
        $loadingContent.find('.'+labelClass).html(displayVal);
    }

</script>