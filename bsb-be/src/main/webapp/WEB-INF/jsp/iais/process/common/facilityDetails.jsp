<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Facility Name</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${mohProcessDto.facilityName}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Facility Classification</td>
                        <td style="padding-left: 20px"><iais:code code="${mohProcessDto.facilityClassification}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Approved Facility Activity Type</td>
                        <td style="padding-left: 20px"><iais:code code="${mohProcessDto.approvedFacilityActivityType}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <c:if test="${approvalFacilityActivityDtoList ne null}">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="collapsed" data-toggle="collapse" href="#facilityActivityType">Recommendation for Approval for Facility Activity Type</a>
                            </h4>
                        </div>
                        <div id="facilityActivityType" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="row" style="font-weight: 700;text-align: center">
                                    <div class="col-md-1">No</div>
                                    <div class="col-md-8">Facility Activty Type</div>
                                    <div class="col-md-3">Approval(Yes/No)</div>
                                </div>
                                <c:forEach var="approvalFacilityActivity" items="${approvalFacilityActivityDtoList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                        <div class="col-md-8"><iais:code code="${approvalFacilityActivity.activityName}"/></div>
                                        <div class="col-md-3">
                                            <div class="row">
                                                <input type="radio" name="${approvalFacilityActivity.id}" data-radio-type="facilityActivityYes" <c:if test="${approvalFacilityActivity.approval eq 'yes'}">checked="checked"</c:if> value="yes"/>
                                                <label><span class="check-circle"></span>Yes</label>
                                                <input type="radio" name="${approvalFacilityActivity.id}" data-radio-type="facilityActivityNo" <c:if test="${approvalFacilityActivity.approval eq 'no'}">checked="checked"</c:if> value="no"/>
                                                <label><span class="check-circle"></span>No</label>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${approvalFacilityBatDtoList ne null}">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="collapsed" data-toggle="collapse" href="#facilityApprovals">Recommendation for Approval to Possess</a>
                            </h4>
                        </div>
                        <div id="facilityApprovals" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="row" style="font-weight: 700;text-align: center">
                                    <div class="col-md-1">No</div>
                                    <div class="col-md-3">Schedule</div>
                                    <div class="col-md-4">Name of BA/T</div>
                                    <div class="col-md-4">Approval(Yes/No)</div>
                                </div>
                                <c:forEach var="approvalFacilityBat" items="${approvalFacilityBatDtoList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                        <div class="col-md-3"><iais:code code="${approvalFacilityBat.schedule}"/></div>
                                        <div class="col-md-4"><c:out value="${approvalFacilityBat.batName}"/></div>
                                        <div class="col-md-4">
                                            <div class="row">
                                                <input type="radio" name="${approvalFacilityBat.id}" data-bat-activityId="${approvalFacilityBat.activityId}" <c:if test="${approvalFacilityBat.approval eq 'yes'}">checked="checked"</c:if> value="yes"/>
                                                <label><span class="check-circle"></span>Yes</label>
                                                <input type="radio" name="${approvalFacilityBat.id}" data-bat-activityId="${approvalFacilityBat.activityId}" <c:if test="${approvalFacilityBat.approval eq 'no'}">checked="checked"</c:if> value="no"/>
                                                <label><span class="check-circle"></span>No</label>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Previous</a>
</div>