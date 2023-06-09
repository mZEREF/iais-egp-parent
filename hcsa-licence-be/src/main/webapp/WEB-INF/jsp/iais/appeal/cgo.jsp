
<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;margin-left: 2%">
  <div id="wizard-page-title" style="font-size: 2rem;">A Clinical Governance Officer (CGO) is a suitably qualified person appointed by the licensee and who is responsible for the oversight of clinical and technical matters related to the <iais:code code="CDN001"/> provided.</div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table aria-describedby="" class="control-grid columns1 " style="width: 100%;">
        <thead style="display: none">
        <tr>
          <th scope="col" ></th>
        </tr>
        </thead>
        <tbody>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--85" class="">
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
                  <table aria-describedby="" class="assignContent control-grid" style="width:100%;word-break:break-all">
                    <thead style="display: none">
                    <tr>
                      <th scope="col" ></th>
                    </tr>
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
                        <div id="control--runtime--" class="">
                          <div class=" form-group form-horizontal formgap" <c:if test="${status.first}">style="width:194%;"</c:if> >
                            <div class="col-sm-5 control-label formtext control">
                              <div class="cgo-header">
                                <strong>Clinical Governance Officer </strong>
                              </div>
                            </div>
                            <div class="col-sm-8 text-right">
                            </div>
                          </div>
                        </div>
                        <div id="control--runtime--2" class="">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-5 control-label formtext ">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label" style="display: block;">Add/Assign a Clinical Governance Officer</label>
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
                            <tr>
                              <th scope="col" ></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">
                                        Name
                                      </label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-3" id="salutation${suffix}">
                                      <label class="control-label control-set-font control-font-label"><iais:code code="${currentCgo.salutation}"/></label>
                                    </div>
                                    <div class="col-sm-3 col-md-4 img-show" id="name${suffix}">
                                      <label class="control-label control-set-font control-font-label"><c:out value="${currentCgo.name}"/></label>
                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                        <jsp:param name="profRegNo" value="${currentCgo.profRegNo}"/>
                                        <jsp:param name="personName" value="${currentCgo.name}"/>
                                        <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                      </jsp:include>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                      <jsp:param name="profRegNo" value="${currentCgo.profRegNo}"/>
                                      <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--28" class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label id="control--runtime--28--label" class="control-label control-set-font control-font-label">
                                        ID No.
                                      </label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-3" id="idType${suffix}">
                                      <label class="control-label control-set-font control-font-label"><iais:code code="${currentCgo.idType}"/></label>
                                    </div>
                                    <div class="col-sm-5 col-md-4 img-show">
                                      <label class="control-label control-set-font control-font-label"><c:out value="${currentCgo.idNo}"/></label>
                                      <c:if test="${viewPrint != 'Y'}">
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                          <jsp:param name="idNo" value="${currentCgo.idNo}"/>
                                          <jsp:param name="methodName" value="showThisTableNewService"/>
                                        </jsp:include>
                                      </c:if>

                                    </div>
                                    <c:if test="${viewPrint != 'Y'}">
                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${currentCgo.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                      </jsp:include>
                                    </c:if>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="" class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5">
                                      <span class="error-msg" name="iaisErrorMSg" id="error_idTypeNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <c:if test="${currentCgo.idType=='IDTYPE003'}">
                              <tr height="1" class="nationalityDiv">
                                <td class="first last" style="width: 100%;">
                                  <div class="">
                                    <div class="form-group form-horizontal formgap">
                                      <div class="col-sm-5 control-label formtext">
                                        <label class="control-label control-set-font control-font-label">Nationality</label>
                                        
                                        <span class="upload_controls"></span>
                                      </div>
                                      <div class="col-sm-5 col-md-7" id="nationality${suffix}">
                                        <label class="control-label control-set-font control-font-label"><iais:code code="${currentCgo.nationality}"/></label>
                                      </div>
                                    </div>
                                  </div>
                                </td>
                              </tr>
                            </c:if>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext">
                                      <label class="control-label control-set-font control-font-label">Designation</label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="designation${suffix}">
                                      <label class="control-label control-set-font control-font-label"><iais:code code="${currentCgo.designation}"/></label>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <c:if test="${currentCgo.designation=='DES999'}">
                              <tr height="1">
                                <td class="first last" style="width: 100%;">
                                  <div class=" otherDesignationDiv hidden ">
                                    <div class="form-group form-horizontal formgap">
                                      <div class="control-label formtext col-sm-5">
                                      </div>
                                      <div class="col-md-7 col-xs-5 col-sm-3">
                                        <label class="control-label control-set-font control-font-label">${currentCgo.otherDesignation}</label>
                                      </div>
                                    </div>
                                  </div>
                                </td>
                              </tr>
                            </c:if>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Professional Board</label>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:select cssClass="professionBoard" name="professionBoard" codeCategory="CATE_ID_PROFESSION_BOARD"
                                                 value="${currentCgo.professionBoard}" firstOption="Please Select"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_professionBoard${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext">
                                      <label  class="control-label control-set-font control-font-label">Professional Type</label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7" id="professionType${suffix}">
                                      <div class="professionRegoType">
                                        <label class="control-label control-set-font control-font-label"><iais:code code="${currentCgo.professionType}"/></label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--31" class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label id="control--runtime--31--label" class="control-label control-set-font control-font-label">
                                        Professional Regn. No.
                                      </label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5 img-show">
                                      <label class="control-label control-set-font control-font-label"><c:out value="${currentCgo.profRegNo}"/></label>
                                      <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                        <jsp:param name="profRegNo" value="${currentCgo.profRegNo}"/>
                                        <jsp:param name="methodName" value="showThisTableNewService"/>
                                      </jsp:include>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                      <jsp:param name="profRegNo" value="${currentCgo.profRegNo}"/>
                                      <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Type of Current Registration</label>

                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="50" type="text" needDisabled="true" cssClass="typeOfCurrRegi appeal-disabled" name="typeOfCurrRegi"
                                                value="${currentCgo.typeOfCurrRegi}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_typeOfCurrRegi${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>

                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Current Registration Date</label>

                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:datePicker cssClass="currRegiDate field-date appeal-disabled" disabled="true" name="currRegiDate" value="${currentCgo.currRegiDateStr}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_currRegiDate${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Practicing Certificate End Date</label>

                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:datePicker cssClass="praCerEndDate field-date appeal-disabled" disabled="true" name="praCerEndDate" value="${currentCgo.praCerEndDateStr}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_praCerEndDate${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>

                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Type of Register</label>

                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="50" type="text" needDisabled="true" cssClass="typeOfRegister appeal-disabled"  name="typeOfRegister"
                                                value="${currentCgo.typeOfRegister}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_typeOfRegister${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div id="control--runtime--29" class="">
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
                                <div class="">
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
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Other Specialties</label>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="100" type="text" needDisabled="true" cssClass="specialityOther appeal-disabled" name="specialityOther"
                                                value="${currentCgo.specialityOther}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_specialityOther${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="control control-caption-horizontal otherDesignationDiv hidden ">
                  <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-5 control-label formtext specialtyGetDateLabel">
                                    <label class="control-label control-set-font control-font-label">
                                      Date when specialty was obtained
                                    </label>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <iais:value width="7" cssClass="col-md-7">
                                    <iais:datePicker cssClass="specialtyGetDate field-date appeal-disabled" disabled="true" name="specialtyGetDate"
                                                     value="${currentCgo.specialtyGetDateStr}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_specialtyGetDate${status.index}"></span>
                                  </iais:value>
                                </div>
                  </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div class="">
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
                                <div class="">
                                  <div class="form-group form-horizontal formgap">
                                    <div class="control-label formtext col-sm-5">
                                      <label class="control-label control-set-font control-font-label">Other Qualification</label>
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5">
                                      <div class="">
                                        <label class="control-label control-set-font control-font-label">${currentCgo.otherQualification}</label>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="width: 100%;">
                                <div  class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Mobile No.</label>                                                                                                                                        
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-md-7 col-xs-9 col-sm-5">
                                      <label class="control-label control-set-font control-font-label">${currentCgo.mobileNo}</label>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1">
                              <td class="first last" style="">
                                <div id="control--runtime--33" class="">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">Email Address</label>
                                      
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-4 col-md-7">
                                      <label class="control-label control-set-font control-font-label">${currentCgo.emailAddr}</label>
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
        <c:if test="${'BLB'!=currentSvcCode && 'RDS'!=currentSvcCode && requestInformationConfig==null && 'APTY005' !=AppSubmissionDto.appType}">
          <tr id="addInfo" <c:if test="${CgoMandatoryCount >0}">hidden </c:if>>
            <td>
              <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Clinical Governance Officer</span>
            </td>
          </tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
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


        doEdit();

        $('#control--runtime--0').children().remove("hr")

        $('.assignSel ').trigger('change');

        $(".assignContent div.nice-select").addClass('disabled');
        if($('.designationSel').val()=='DES999'){
          $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
        }else {
          $('.designationSel').closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
        }
      $(".appeal-disabled").attr("disabled",true);
      $('.appeal-disabled').css('border-color','#ededed');
      $('.appeal-disabled').css('color','#999');
    });





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
    }

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
    }

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k,v) {
          if(k!==0){
            $(this).html(k+1);
          }
        });

    }

    function showThisTableNewService(obj) {
      var $target = $(obj).closest('td');
      var w1 = $target.css('width');
      var w2 = $target.prev().css('width');
      if (w1 == w2) {
        $target.find("div.disciplinary-record").children("div").css("margin-left", "-50%");
      } else {
        $target.find("div.disciplinary-record").children("div").css("margin-left", "1.5%");
      }
      $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
    }

    function showThisNameTableNewService(obj) {
      var $target = $(obj).closest('td');
      var h = $target.css('height');
      $target.find("div.disciplinary-record").children("div").css("margin-top", h);
      $target.find("div.disciplinary-record").children("div").css("margin-left", "50%");
      $(obj).closest('div.img-show').closest('td').find("div.new-img-show").show();
    }

    function closeThis(obj){
      $(obj).closest('div.disciplinary-record').hide();
    }
</script>
