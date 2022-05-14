<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="bat" tagdir="/WEB-INF/tags/common" %>


<%@attribute name="activeNodeKey" required="true" type="java.lang.String" %>
<%@attribute name="scheduleOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="addressTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="batInfos" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo>" %>
<%@attribute name="firstScheduleOp" required="true" type="java.lang.String" %>
<%@attribute name="scheduleBatMap" required="true" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>" %>
<%@attribute name="nationalityOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>


<input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(batInfos.size())%>">



<div class="tab-content">
  <div role="tabpanel">
    <div class="form-horizontal">
      <div id="sectionGroup">
        <c:forEach var="info" items="${batInfos}" varStatus="status">
          <section id="batInfoSection--v--${status.index}" style="margin-bottom: 100px">
            <div class="form-group ">
              <div class="col-sm-5 control-label">
                <label for="schedule--v--${status.index}">Schedule</label>
                <span class="mandatory otherQualificationSpan">*</span>
              </div>
              <div class="col-sm-6" style="z-index: 30;">
                <iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
                  <%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
                <c:choose>
                  <c:when test="${activeNodeKey eq masterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}">
                    <label>Fifth Schedule</label>
                    <input type="hidden" name="schedule--v--${status.index}" id="schedule--v--${status.index}" value="${masterCodeConstants.FIFTH_SCHEDULE}"/>
                  </c:when>
                  <c:otherwise>
                    <select name="schedule--v--${status.index}" class="scheduleDropdown${status.index}" id="schedule--v--${status.index}" data-cascade-dropdown="schedule-bat">
                      <option value="">Please Select</option>
                      <c:forEach items="${scheduleOps}" var="schedule">
                        <option value="${schedule.value}" <c:if test="${info.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                      </c:forEach>
                    </select>
                    <span data-err-ind="schedule--v--${status.index}" class="error-msg"></span>
                  </c:otherwise>
                </c:choose>
              </div>
              <c:if test="${status.index gt 0}">
                <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
              </c:if>
            </div>
            <div class="form-group ">
              <div class="col-sm-5 control-label">
                <label for="batName--v--${status.index}">Name of ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"}</label>
                <span class="mandatory otherQualificationSpan">*</span>
              </div>
              <div class="col-sm-6" style="z-index: 20;">
                <select name="batName--v--${status.index}"  class="batNameDropdown${status.index}" id="batName--v--${status.index}">
                  <c:set var="batNameOps" value="${scheduleBatMap.get(empty info.schedule ? firstScheduleOp : info.schedule)}"/>
                  <c:forEach items="${batNameOps}" var="name">
                    <option value="${name.value}" <c:if test="${info.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                  </c:forEach>
                </select>
                <span data-err-ind="batName--v--${status.index}" class="error-msg"></span>
              </div>
            </div>
            <div class="form-group ">
              <div class="col-sm-5 control-label">
                <label>Types of samples that will be handled <span class="mandatory otherQualificationSpan">*</span></label>
              </div>
              <div class="col-sm-6" style="z-index: 10;">
                <div class="self-assessment-checkbox-gp">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleCultureIsolate--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_CULTURE_ISOLATE)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_CULTURE_ISOLATE}"/>
                    <label for="sampleCultureIsolate--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culture/isolate of biological agent</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="samplePureToxin--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_PURE_TOXIN)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_PURE_TOXIN}"/>
                    <label for="samplePureToxin--v--${status.index}" class="form-check-label"><span class="check-square"></span>Pure toxin</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleClinical--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_CLINICAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_CLINICAL}"/>
                    <label for="sampleClinical--v--${status.index}" class="form-check-label"><span class="check-square"></span>Clinical sample e.g. blood, serum, respiratory swab, containing biological agent or toxin</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleAnimal--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_ANIMAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_ANIMAL}"/>
                    <label for="sampleAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal sample containing biological agent or toxin</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleEnv--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_ENVIRONMENTAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_ENVIRONMENTAL}"/>
                    <label for="sampleEnv--v--${status.index}" class="form-check-label"><span class="check-square"></span>Environmental samples containing biological agent or toxin</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleFood--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_FOOD)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_FOOD}"/>
                    <label for="sampleFood--v--${status.index}" class="form-check-label"><span class="check-square"></span>Food sample containing biological agent or toxin</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleOthers--v--${status.index}" data-custom-ind="batOthersSampleType" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_OTHER}"/>
                    <label for="sampleOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details</label>
                  </div>
                </div>
                <span data-err-ind="sampleType--v--${status.index}" class="error-msg"></span>
              </div>
            </div>
            <div class="form-group ">
              <div class="col-sm-5 control-label">
                <label>Type of work that will be carried out involving the ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"} <span class="mandatory otherQualificationSpan">*</span></label>
              </div>
              <div class="col-sm-6" style="z-index: 10;">
                <div class="self-assessment-checkbox-gp">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workCultureIsolation--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_CULTURING_ISOLATION_BAT)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_CULTURING_ISOLATION_BAT}"/>
                    <label for="workCultureIsolation--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culturing/isolation of biological agent</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workSerological--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_SEROLOGICAL_TEST)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_SEROLOGICAL_TEST}"/>
                    <label for="workSerological--v--${status.index}" class="form-check-label"><span class="check-square"></span>Serological test</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workMolecular--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_MOLECULAR_TEST)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_MOLECULAR_TEST}"/>
                    <label for="workMolecular--v--${status.index}" class="form-check-label"><span class="check-square"></span>Molecular test</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workAnimal--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_ANIMAL_STUDIES)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_ANIMAL_STUDIES}"/>
                    <label for="workAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal studies (specify the type of animal under details)</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workBiomanufacturing--v--${status.index}" data-custom-ind="batBmfWorkType" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT}"/>
                    <label for="workBiomanufacturing--v--${status.index}" class="form-check-label"><span class="check-square"></span>Biomanufacturing involving biological agent. Please specify expected maximum handling volume under details</label>
                  </div>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workOthers--v--${status.index}" data-custom-ind="batOthersWorkType" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_OTHERS}"/>
                    <label for="workOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details.</label>
                  </div>
                </div>
                <span data-err-ind="workType--v--${status.index}" class="error-msg"></span>
              </div>
            </div>
            <div id="sampleWorkDetailDiv--v--${status.index}" class="form-group" <c:if test="${!info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) && !info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) && !info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}">style="display: none"</c:if>>
              <div class="col-sm-5 control-label">
                <label for="sampleWorkDetail--v--${status.index}">Details regarding the type of sample that will be handled and the intended work <span class="mandatory otherQualificationSpan">*</span></label>
              </div>
              <div class="col-sm-6">
                <textarea maxLength="250" class="col-xs-12" name="sampleWorkDetail--v--${status.index}" id="sampleWorkDetail--v--${status.index}" rows="3"><c:out value="${info.sampleWorkDetail}"/></textarea>
                <span data-err-ind="sampleWorkDetail--v--${status.index}" class="error-msg"></span>
              </div>
            </div>
            <bat:facilityDetail addressTypeOps="${addressTypeOps}" nationalityOps="${nationalityOps}" status="${status}" detail="${info.details}"/>
          </section>
        </c:forEach>
      </div>
      <div class="form-group">
        <div class="col-12">
          <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add New ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"}</a>
        </div>
      </div>


      <div class="modal fade" id="invalidPostalCodeModal" role="dialog">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-body">
              <div class="row">
                <div class="col-md-12"><span>The postal code is invalid</span></div>
              </div>
            </div>
            <div class="modal-footer" style="justify-content: center">
              <button type="button" class="btn btn-primary btn-lg" data-dismiss="modal">OK</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
