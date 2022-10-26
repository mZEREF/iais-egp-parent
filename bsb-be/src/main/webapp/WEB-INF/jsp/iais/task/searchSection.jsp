<div class="form-horizontal">
    <div class="form-group">
        <label for="commonRoleId" class="col-xs-12 col-md-4 control-label">Role</label>
        <div class="col-xs-8 col-sm-6 col-md-6">
            <select name="commonRoleId" class="commonRoleIdDropdown" id="commonRoleId">
                <%--@elvariable id="BsbRoleOptions" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                <%--@elvariable id="bsbCurRole" type="java.lang.String"--%>
                <c:forEach var="option" items="${BsbRoleOptions}">
                    <option value="${option.value}" <c:if test="${option.value eq bsbCurRole}">selected="selected"</c:if>>${option.text}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-10 col-md-12">
            <div class="components">
                <a class="btn btn-secondary" data-toggle="collapse" data-target="#taskListSearchFilter">Filter</a>
            </div>
        </div>
    </div>
    <%--@elvariable id="taskListSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto"--%>
    <div id="taskListSearchFilter" class="collapse">
        <div class="col-xs-12 col-sm-12"><%-- div for app date from and to --%>
            <label for="searchAppNo" class="col-sm-5 col-md-4 control-label">Application No.</label>
            <div class="col-sm-7 col-md-6">
                <span data-err-ind="searchAppNo" class="error-msg"></span>
                <input type="text" id="searchAppNo" name="searchAppNo" value="${taskListSearchDto.searchAppNo}"/>
            </div>
            <label for="searchAppType" class="col-sm-5 col-md-4 control-label">Application Type</label>
            <div class="col-sm-7 col-md-6">
                <span data-err-ind="searchAppType" class="error-msg"></span>
                <iais:select name="searchAppType" cssClass="searchAppTypeDropdown" id="searchAppType" options="appTypeOps"
                             firstOption="Please Select" value="${taskListSearchDto.searchAppType}"/>
            </div>

            <label for="searchAppSubType" class="col-sm-5 col-md-4 control-label">Application Sub-Type</label>
            <div class="col-sm-7 col-md-6">
                <span data-err-ind="searchAppSubType" class="error-msg"></span>
                <iais:select name="searchAppSubType" cssClass="searchAppSubTypeDropdown" id="searchAppSubType" options="appSubTypeOps"
                             firstOption="Please Select" value="${taskListSearchDto.searchAppSubType}"/>
            </div>
            <label for="searchSubmissionType" class="col-sm-5 col-md-4 control-label">Submission Type</label>
            <div class="col-sm-7 col-md-6">
                <span data-err-ind="searchSubmissionType" class="error-msg"></span>
                <iais:select name="searchSubmissionType" cssClass="searchSubmissionTypeDropdown" id="searchSubmissionType" options="submissionTypeOps"
                             firstOption="Please Select" value="${taskListSearchDto.searchSubmissionType}"/>
            </div>

            <label for="searchAppStatus" class="col-sm-5 col-md-4 control-label">Application Status</label>
            <div class="col-sm-7 col-md-6">
                <span data-err-ind="searchAppStatus" class="error-msg"></span>
                <iais:select name="searchAppStatus" cssClass="searchAppStatusDropdown" id="searchAppStatus" options="appStatusOps"
                             firstOption="Please Select" value="${taskListSearchDto.searchAppStatus}"/>
            </div>

            <div class="row">
                <div class="col-xs-12 col-sm-6">
                    <label for="searchAppDateFrom" class="col-xs-12 col-sm-5 control-label">Application Date From</label>
                    <div class="col-xs-12 col-sm-7">
                        <iais:datePicker id="searchAppDateFrom" name="searchAppDateFrom" value="${taskListSearchDto.searchAppDateFrom}"/>
                    </div>
                    <span data-err-ind="searchAppDateFrom" class="error-msg"></span>
                </div>
                <div class="col-xs-12 col-sm-6">
                    <label for="searchAppDateTo" class="col-xs-12 col-sm-5 control-label">Application Date to</label>
                    <div class="col-xs-12 col-sm-7">
                        <iais:datePicker id="searchAppDateTo" name="searchAppDateTo" value="${taskListSearchDto.searchAppDateTo}"/>
                    </div>
                    <span data-err-ind="searchAppDateTo" class="error-msg"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-6">
                    <label for="searchModifiedDateFrom" class="col-xs-12 col-sm-5 control-label">Last Modified Date From</label>
                    <div class="col-xs-12 col-sm-7">
                        <iais:datePicker id="searchModifiedDateFrom" name="searchModifiedDateFrom" value="${taskListSearchDto.searchModifiedDateFrom}"/>
                    </div>
                    <span data-err-ind="searchModifiedDateFrom" class="error-msg"></span>
                </div>
                <div class="col-xs-12 col-sm-6">
                    <label for="searchModifiedDateTo" class="col-xs-12 col-sm-5 control-label">Last Modified Date To</label>
                    <div class="col-xs-12 col-sm-7">
                        <iais:datePicker id="searchModifiedDateTo" name="searchModifiedDateTo" value="${taskListSearchDto.searchModifiedDateTo}"/>
                    </div>
                    <span data-err-ind="searchModifiedDateTo" class="error-msg"></span>
                </div>
            </div>
        </div>
        <div class="col-xs-12 col-sm-12" style="text-align:right;"><%-- div for btn --%>
            <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
            <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
        </div>
    </div>
</div>
