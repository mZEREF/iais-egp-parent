<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        <tr>
                            <td align="right">Active Approval No. to be revoked</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.approvalNo}"/></td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.facName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${revokeDto.facAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${revokeDto.facClassification}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Activity Type</td>
                            <td style="padding-left : 20px">
                                <iais:code code="${revokeDto.activityType}"/>
<%--                                <c:if test="${flag eq 'app'}">--%>
<%--                                    <iais:code code="${approval.activeType}"></iais:code>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${flag eq 'fac'}">--%>
<%--                                    <c:forEach var="activity" items="${approval.facilityActivities}" varStatus="status">--%>
<%--                                        <c:choose>--%>
<%--                                            <c:when test="${status.last}">--%>
<%--                                                <iais:code code="${activity.activityType}"></iais:code>--%>
<%--                                            </c:when>--%>
<%--                                            <c:otherwise>--%>
<%--                                                <iais:code code="${activity.activityType}"></iais:code>,--%>
<%--                                            </c:otherwise>--%>
<%--                                        </c:choose>--%>
<%--                                    </c:forEach>--%>
<%--                                </c:if>--%>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">Approval</td>
                            <td style="padding-left : 20px"></td>
                        </tr>
                        <tr>
                            <td align="right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="${revokeDto.approvalStatus}"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <c:if test="${back eq 'revocationFacility'}">
        <a class="back" href="/bsb-be/eservice/INTRANET/FacilityList"><em class="fa fa-angle-left"></em>Back</a>
    </c:if>
    <c:if test="${back eq 'revocationTaskList'}">
        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em>Back</a>
    </c:if>
</div>