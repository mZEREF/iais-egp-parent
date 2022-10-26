<div class="col-xs-12 col-sm-12">
    <label for="orgName" class="col-sm-5 col-md-5 control-label">Organisation Name</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="orgName" class="error-msg"></span>
        <select name="orgName" class="orgName-dropdown" id="orgName">
            <option value=""></option>
        </select>
    </div>

    <label for="afcAdminName" class="col-sm-5 col-md-5 control-label">Facility Administrator</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="afcAdminName" class="error-msg"></span>
        <input type="text" id="afcAdminName" name="afcAdminName" value="${afcSearchDto.adminName}"/>
    </div>

    <label for="afcStatus" class="col-sm-5 col-md-5 control-label">AFC Status</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="afcStatus" class="error-msg"></span>
        <select name="afcStatus" class="afcStatusDropdown" id= "afcStatus">
            <c:forEach var="item" items="${afcStatusOps}">
                <option value="${item.value}" <c:if test="${afcSearchDto.afcStatus eq item.value}">selected="selected"</c:if>>${item.text}</option>
            </c:forEach>
        </select>
    </div>

    <label for="teamMemberName" class="col-sm-5 col-md-5 control-label">Team Member Name</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="teamMemberName" class="error-msg"></span>
        <input type="text" id="teamMemberName" name="teamMemberName" value="${afcSearchDto.teamMemberName}"/>
    </div>

    <label for="teamMemberId" class="col-sm-5 col-md-5 control-label">Team Member ID</label>
    <div class="col-sm-7 col-md-5">
        <span data-err-ind="teamMemberId" class="error-msg"></span>
        <input type="text" id="teamMemberId" name="teamMemberId" value="${afcSearchDto.teamMemberId}"/>
    </div>

    <label for="afcApprovedDtFrom" class="col-sm-5 col-md-5 control-label">Approved Date Range</label>
    <div class="col-sm-7 col-md-5">
        <div class="row">
            <span data-err-ind="afcApprovedDtFrom" class="error-msg"></span>
            <input type="text" autocomplete="off" name="afcApprovedDtFrom" id="afcApprovedDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${afcSearchDto.approvedDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
        </div>
        <div class="row">
            <span data-err-ind="approvedDtTo" class="error-msg"></span>
            <label for="afcApprovedDtTo"></label><input type="text" autocomplete="off" name="afcApprovedDtTo" id="afcApprovedDtTo" data-date-start-date="01/01/1900" value="<c:out value="${afcSearchDto.approvedDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
        </div>
    </div>

</div>