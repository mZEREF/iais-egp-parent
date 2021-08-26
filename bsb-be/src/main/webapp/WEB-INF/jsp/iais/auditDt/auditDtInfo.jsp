<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
<%--                    <c:forEach var="item" items="${revocationDetail}">--%>
                        <input type="text" value="" name="facilityId" hidden/>
                        <tr>
                            <td class="col-xs-6" align="right">Facility Name</td>
                            <td style="padding-left : 20px"><c:out value="Facility Name1"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Address</td>
                            <td style="padding-left : 20px"><c:out value="Facility Address1"/></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Classification</td>
                            <td style="padding-left : 20px"><iais:code code="Facility Classification1"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Facility Type</td>
                            <td style="padding-left : 20px"><iais:code code="Facility Classification1"></iais:code></td>
                        </tr>
                        <tr>
                            <td align="right">Approval</td>
                            <td style="padding-left : 20px"><c:out value="Approval1"/></td>
                        </tr>
                        <tr>
                            <td align="right">Approval Status</td>
                            <td style="padding-left : 20px"><iais:code code="Approval Status1"></iais:code></td>
                        </tr>
<%--                    </c:forEach>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div align="left">
    <a class="back" href="#"><em class="fa fa-angle-left"></em>Back</a>
</div>


