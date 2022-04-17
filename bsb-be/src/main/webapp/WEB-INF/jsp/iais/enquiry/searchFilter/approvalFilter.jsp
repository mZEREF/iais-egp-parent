<div class="col-xs-12 col-sm-12">
    <label for="approvalFacClassification" class="col-sm-5 col-md-5 control-label">Facility Classification</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="approvalFacClassification" class="error-msg"></span>
        <select name="approvalFacClassification" class="approvalFacClassification-select" id="approvalFacClassification">
            <c:forEach var="item" items="${facClassificationOps}">
                <option value="${item.value}" <c:if test="${approvalSearchDto.facClassification eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="approvalFacName" class="col-sm-5 col-md-5 control-label">Facility Name</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="approvalFacName" class="error-msg"></span>
        <input type="text" id="approvalFacName" name="approvalFacName" value="${approvalSearchDto.facName}"/>
    </div>

    <label for="approvalType" class="col-sm-5 col-md-5 control-label">Approval Type</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="approvalType" class="error-msg"></span>
        <select name="approvalType" class="approvalType-select" id="approvalType">
            <c:forEach var="item" items="${approvalTypeOps}">
                <option value="${item.value}" <c:if test="${approvalSearchDto.approvalType eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="approvalStatus" class="col-sm-5 col-md-5 control-label">Approval Status</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="approvalStatus" class="error-msg"></span>
        <select name="approvalStatus" class="approvalStatus-select" id="approvalStatus">
            <c:forEach var="item" items="${approvalStatusOps}">
                <option value="${item.value}" <c:if test="${approvalSearchDto.approvalStatus eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="approvedDtFrom" class="col-sm-5 col-md-5 control-label">Approved Date Range</label>
    <div class="col-sm-7 col-md-5">
        <div class="row">
            <span data-err-ind="approvedDtFrom" class="error-msg"></span>
            <input type="text" autocomplete="off" name="approvedDtFrom" id="approvedDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvedDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
        </div>
        <div class="row">
            <span data-err-ind="approvedDtTo" class="error-msg"></span>
            <label for="approvedDtTo"></label><input type="text" autocomplete="off" name="approvedDtTo" id="approvedDtTo" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvedDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
        </div>
    </div>


</div>
