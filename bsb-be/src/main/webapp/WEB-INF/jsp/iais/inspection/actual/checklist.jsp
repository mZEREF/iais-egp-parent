<%--@elvariable id="insInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsInfoDto"--%>
<%--@elvariable id="iais_Audit_Trail_dto_Attr" type="com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto"--%>
<div class="col-xs-12">
    <div class="table-gp">
        <div class="form-horizontal" style="margin-left: auto; margin-right: auto; max-width: 1080px;">
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Date of Inspection</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Purpose of Inspection</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Facility Representative(s)</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <div><div class="col-sm-5" style="padding-left:0"><p>Main Administrator:</p></div><div class="col-sm-6 col-md-7"><p>${insInfo.adminName}</p></div></div>
                    <div><div class="col-sm-5" style="padding-left:0"><p>Alternate Administrator:</p></div><div class="col-sm-6 col-md-7"><p>${insInfo.alterAdminName}</p></div></div>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspector Officer</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <%-- This is wrong, the inspector should be saved in a table, the current user may not be
                      the inspector because of reassignment --%>
                    <p>${iais_Audit_Trail_dto_Attr.mohUserId}</p>
                </div>
                <div class="clear"></div>
            </div>
            <div style="text-align: right">
                <button name="viewChecklist" id="viewChecklist" type="button" class="btn btn-primary">VIEW CHECKLIST</button>
            </div>
        </div>
    </div>
</div>
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>