<div class="row form-horizontal">
    <div class="col-lg-12 col-xs-12 cesform-box">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="table-gp tablebox">
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Facility Name</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.facilityName}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Facility Address</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.facilityAddress}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Facility Classification</label>
                        <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationFacilityDto.facilityClassification}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Reasons</label>
                        <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationFacilityDto.reasons}"/></div>
                    </div>
                    <div class="form-group ">
                        <label class="col-sm-5 control-label">Remarks</label>
                        <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationFacilityDto.remarks}"/></div>
                    </div>
                    <%@include file="previewDocuments.jsp" %>
                    <br>
                    <div class="panel panel-default">
                        <div class="panel-heading" style="text-align:center; background-color: #c6dff1"><strong>List of Approval(s)</strong></div>
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="table-gp">
                                    <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                                        <thead>
                                        <tr>
                                            <th scope="col" style="text-align:center;">S/N</th>
                                            <th scope="col" style="text-align:center;">Approval Type</th>
                                            <th scope="col" style="text-align:center;">Biological Agent/Toxin</th>
                                            <th scope="col" style="text-align:center;">Status</th>
                                            <th scope="col" style="text-align:center;">Physical Possession of BA/T in Facility</th>
                                        </tr>
                                        </thead>
                                        <tbody style="text-align:center;">
                                        <c:forEach var="item" items="${deRegistrationFacilityDto.approvalInfoList}" varStatus="status">
                                            <tr>
                                                <td>
                                                    <p><c:out value="${status.index + 1}"/></p>
                                                </td>
                                                <td>
                                                    <p><iais:code code="${item.approvalType}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.biologicalAgentToxin}"/></p>
                                                </td>
                                                <td>
                                                    <p><iais:code code="${item.status}"/></p>
                                                </td>
                                                <td>
                                                    <p><c:out value="${item.physicalPossession}"/></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>