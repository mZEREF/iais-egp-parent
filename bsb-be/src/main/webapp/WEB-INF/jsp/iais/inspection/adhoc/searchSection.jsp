<div class="form-horizontal">
    <div class="row">
        <div class="col-xs-10 col-md-12">
            <div class="components">
                <a class="btn btn-secondary" data-toggle="collapse" data-target="#taskListSearchFilter">Filter</a>
            </div>
        </div>
    </div>
    <%--@elvariable id="searchDto" type="sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementSearchDto"--%>
    <div id="taskListSearchFilter" class="collapse">
        <div class="col-xs-12 col-sm-12"><%-- div for app date from and to --%>
            <label for="searchKeyword" class="col-sm-5 col-md-4 control-label">Keyword search of part of</label>
            <div class="col-sm-7 col-md-6">
                <input type="text" id="searchKeyword" name="searchKeyword" value="<c:out value="${searchDto.searchKeyword}"/>"/>
                <span data-err-ind="searchKeyword" class="error-msg"></span>
            </div>
            <div class="col-sm-12 col-md-12">
                <div class="col-sm-6 col-md-3">
                    <input type="radio" value="facilityName" name="searchKeywordType" <c:if test="${searchDto.searchKeywordType eq 'facilityName'}">checked</c:if>> Facility Name
                </div>
                <div class="col-sm-6 col-md-3">
                    <input type="radio" value="facilityNo" name="searchKeywordType" <c:if test="${searchDto.searchKeywordType eq 'facilityNo'}">checked</c:if>> Facility No.
                </div>
                <span data-err-ind="searchKeywordType" class="error-msg"></span><%--<c:if test="${count eq 'fac'}">checked</c:if>--%>
                <br>
                <br>
            </div>
            <label for="searchFacilityClassification" class="col-sm-5 col-md-4 control-label">Facility Classification</label>
            <div class="col-sm-7 col-md-6">
                <select id="searchFacilityClassification" class="searchFacilityClassificationDropdown" name="searchFacilityClassification">
                    <option value='<c:out value=""/>' <c:if test="${searchDto.searchFacilityClassification eq ''}">selected="selected"</c:if>>All</option>
                    <%--@elvariable id="facilityClassificationOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                    <c:forEach var="facClassificationItem" items="${facilityClassificationOps}">
                        <option value='<c:out value="${facClassificationItem.value}"/>' <c:if test="${searchDto.searchFacilityClassification eq facClassificationItem.value}">selected="selected"</c:if> ><c:out value="${facClassificationItem.text}"/></option>
                    </c:forEach>
                </select>
                <span data-err-ind="searchFacilityClassification" class="error-msg"></span>
            </div>
            <label for="searchFacilityActivityType" class="col-sm-5 col-md-4 control-label">Facility Activity Type</label>
            <div class="col-sm-7 col-md-6">
                <select id="searchFacilityActivityType" class="searchFacilityActivityTypeDropdown" name="searchFacilityActivityType">
                    <option value='<c:out value=""/>' <c:if test="${searchDto.searchFacilityActivityType eq ''}">selected="selected"</c:if>>All</option>
                    <%--@elvariable id="facilityActivityTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                    <c:forEach var="facActivityTypeItem" items="${facilityActivityTypeOps}">
                        <option value='<c:out value="${facActivityTypeItem.value}"/>' <c:if test="${searchDto.searchFacilityActivityType eq facActivityTypeItem.value}">selected="selected"</c:if> ><c:out value="${facActivityTypeItem.text}"/></option>
                    </c:forEach>
                </select>
                <span data-err-ind="searchFacilityActivityType" class="error-msg"></span>
            </div>
            <label for="searchFacilityStatus" class="col-sm-5 col-md-4 control-label">Facility Status</label>
            <div class="col-sm-7 col-md-6">
                <select id="searchFacilityStatus" class="searchFacilityStatusDropdown" name="searchFacilityStatus">
                    <option value='<c:out value=""/>' <c:if test="${searchDto.searchFacilityStatus eq ''}">selected="selected"</c:if>>All</option>
                    <%--@elvariable id="facilityStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                    <c:forEach var="facStatusItem" items="${facilityStatusOps}">
                        <option value='<c:out value="${facStatusItem.value}"/>' <c:if test="${searchDto.searchFacilityStatus eq facStatusItem.value}">selected="selected"</c:if> ><c:out value="${facStatusItem.text}"/></option>
                    </c:forEach>
                </select>
                <span data-err-ind="searchFacilityStatus" class="error-msg"></span>
            </div>
        </div>
        <div class="col-xs-12 col-sm-12" style="text-align:right;"><%-- div for btn --%>
            <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
            <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
        </div>
    </div>
</div>
