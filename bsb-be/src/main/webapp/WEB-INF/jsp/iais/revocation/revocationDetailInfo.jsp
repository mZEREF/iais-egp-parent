<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
<%--                    <c:forEach var="item" items="${revocationDetail}">--%>
                        <input type="text" value="${facility.id}" name="facilityId" hidden/>
                        <tr>
                            <td align="right">Active Approval No. to be revoked</td>
                            <td style="padding-left : 20px"><c:out value="approval01"/></td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${facility.facilityName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${facility.facilityAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${facility.facilityClassification}"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Active Type</td>
                            <td style="padding-left : 20px">
                                <c:if test="${flag eq 'app'}">
                                    <iais:code code="${facility.activeType}"></iais:code>
                                </c:if>
                                <c:if test="${flag eq 'fac'}">
                                    <c:forEach var="activity" items="${facility.facilityActivities}" varStatus="status">
                                        <c:choose>
                                            <c:when test="${status.last}">
                                                <iais:code code="${activity.activityType}"></iais:code>
                                            </c:when>
                                            <c:otherwise>
                                                <iais:code code="${activity.activityType}"></iais:code>,
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">Approval</td>
                            <td style="padding-left : 20px"><c:out value="${facility.approval}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="${facility.approvalStatus}"></iais:code></td>
                        </tr>
<%--                    </c:forEach>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
<%--    <a class="back" href="#"><em class="fa fa-angle-left"></em>Back</a>--%>
</div>


