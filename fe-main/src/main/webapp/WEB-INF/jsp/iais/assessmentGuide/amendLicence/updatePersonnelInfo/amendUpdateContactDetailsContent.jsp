<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<div class="form-check-gp">
    <p class="form-check-title">Please select the personnel to amend his/her contact information</p>
    <div class="form-check-label" style="padding-bottom:67px;width: 50%;">
        <iais:select name="personnelOptions" options="personnelOptions" id="personnelId" value="${param.personnelOptions}" firstOption="Please Select"/>
    </div>
    <p class="form-check-title">The following licences will be affected by the change of personal Info</p>
    <iais:pagination  param="amendUpdateContactSearchParam" result="amendUpdateContactSearchResult"/>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col" style="display: none"></th>
                <iais:sortableHeader needSort="true"  field="T3.SVC_NAME" value="Type" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="T3.LICENCE_NO" value="Licence No." isFE="true"/>
                <iais:sortableHeader needSort="false"  field="T2.PSN_TYPE" value="Role" isFE="true"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdateContactSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdateContactSearchResult.rows}" varStatus="status">
                            <tr>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Type</p>
                                        ${pool.svcName}</td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                        ${pool.licenceNo}</td>
                                <td>
                                    <p class="visible-xs visible-sm table-row-title">Role</p>
                                    <c:forEach var="assessList" items="${pool.roles}" varStatus="assessStatus">
                                        <c:choose>
                                            <c:when test="${assessList == 'CD'}">
                                                <%=HcsaConsts.CLINICAL_DIRECTOR%><c:if test="${not assessStatus.last}">,</c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <iais:code code="${assessList}"/><c:if test="${not assessStatus.last}">,</c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty amendUpdateContactSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic7','second')" href="javascript:void(0);">NEXT</a>
        </c:when>
    </c:choose>
</div>
