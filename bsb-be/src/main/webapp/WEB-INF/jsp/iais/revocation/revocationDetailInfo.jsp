<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <c:forEach var="item" items="${revocationDetail}">
                        <input type="text" value="${item.facility.id}" name="facilityId" hidden/>
                        <tr>
                            <td align="right">Active Approval No. to be revoked</td>
                            <td style="padding-left : 20px"><c:out value="approval01"/></td>
                        </tr>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="${item.facility.facilityName}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="${item.facility.facilityAddress}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="${item.facility.facilityClassification}"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Active Type</td>
                            <td style="padding-left : 20px"><iais:code code="${item.facility.activeType}"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Approval</td>
                            <td style="padding-left : 20px"><c:out value="${item.facility.approval}"/></td>
                        </tr>
                        <tr>
                            <td align="right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="${item.facility.approvalStatus}"></iais:code></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
<%--    <a class="back" href="#"><em class="fa fa-angle-left"></em>Back</a>--%>
</div>


