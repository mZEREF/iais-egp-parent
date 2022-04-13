<div class="col-xs-12 col-sm-12"><%-- div for app date from and to --%>
  <label for="appNo" class="col-sm-5 col-md-5 control-label">Application No.</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="appNo" class="error-msg"></span>
    <input type="text" id="appNo" name="appNo" value="${appSearchDto.appNo}"/>
  </div>

  <label for="appType" class="col-sm-5 col-md-5 control-label">Application Type</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="appType" class="error-msg"></span>
    <select name="appType" id="appType">
      <c:forEach var="item" items="${appTypeOps}">
        <option value="${item.value}" <c:if test="${appSearchDto.appType eq item.value}">selected="selected"</c:if>>${item.text}</option>
      </c:forEach>
    </select>
  </div>

  <label for="appStatus" class="col-sm-5 col-md-5 control-label">Application Status</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="appStatus" class="error-msg"></span>
    <select name="appStatus" id="appStatus">
      <c:forEach var="item" items="${appStatusOps}">
        <option value="${item.value}" <c:if test="${appSearchDto.appStatus eq item.value}">selected="selected"</c:if>>${item.text}</option>
      </c:forEach>
    </select>
  </div>

  <label for="appFacName" class="col-sm-5 col-md-5 control-label">Facility Name</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="appFacName" class="error-msg"></span>
    <input type="text" id="appFacName" name="appFacName" value="${appSearchDto.facName}" />
  </div>

  <label for="appFacClassification" class="col-sm-5 col-md-5 control-label">Facility Classification</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="appFacClassification" class="appFacClassification-msg"></span>
    <select name="appFacClassification" id="appFacClassification">
      <c:forEach var="item" items="${facClassificationOps}">
        <option value="${item.value}" <c:if test="${appSearchDto.facClassification eq item.value}">selected="selected"</c:if>>${item.text}</option>
      </c:forEach>
    </select>
  </div>

  <label for="appDtFrom" class="col-sm-5 col-md-5 control-label">Application Date Range</label>
  <div class="col-sm-7 col-md-5">
    <div class="row">
      <span data-err-ind="appDtFrom" class="error-msg"></span>
      <input type="text" autocomplete="off" name="appDtFrom" id="appDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${appSearchDto.appDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
    </div>
    <div class="row">
      <span data-err-ind="appDtTo" class="error-msg"></span>
      <label for="appDtTo"></label><input type="text" autocomplete="off" name="appDtTo" id="appDtTo" data-date-start-date="01/01/1900" value='<c:out value="${appSearchDto.appDtTo}"/>' placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
    </div>
  </div>

  <label for="processType" class="col-sm-5 col-md-5 control-label">Process Type</label>
  <div class="col-sm-7 col-md-5">
    <span data-err-ind="processType" class="error-msg"></span>
    <select name="processType" id="processType">
      <c:forEach var="item" items="${processTypeOps}">
        <option value="${item.value}" <c:if test="${appSearchDto.processType eq item.value}">selected="selected"</c:if>>${item.text}</option>
      </c:forEach>
    </select>
  </div>

</div>
