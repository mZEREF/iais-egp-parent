<%--@elvariable id="iais_Audit_Trail_dto_Attr" type="com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto"--%>
<%--@elvariable id="checklistInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ChecklistInfoDto"--%>
<%--@elvariable id="inspectionProcessDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InspectionProcessDto"--%>
<div class="col-xs-12">
    <div class="table-gp">
        <div class="form-horizontal" style="margin-left: auto; margin-right: auto; max-width: 1080px;">
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspection Date</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p><fmt:formatDate value="${checklistInfo.inspectionDate}" pattern="dd/MM/yyyy"/></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspector Lead</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p><c:out value="${checklistInfo.inspectorLead}"/></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspector</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p><c:out value="${checklistInfo.inspector}"/></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="otherInspector">Other Inspectors</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="otherInspector" name="otherInspector" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"><c:out value="${inspectionProcessDto.otherInspector}"/></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">No. of Non-Compliance</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p><c:out value="${checklistInfo.ncNum}"/></p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="internalRemarks">Internal Remarks</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="internalRemarks" name="internalRemarks" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"><c:out value="${inspectionProcessDto.internalRemarks}"/></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="observation">Observation</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="observation" name="observation" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"><c:out value="${inspectionProcessDto.observation}"/></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>

            <iais:action>
                <c:choose>
                    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
                    <c:when test="${goBackUrl ne null}">
                        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    </c:when>
                    <c:otherwise>
                        <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    </c:otherwise>
                </c:choose>
                <div style="text-align: right">
                    <button name="uploadChecklist" id="uploadChecklist" type="button" class="btn btn-primary">UPLOAD CHECKLIST</button>
                    <button name="downloadChecklist" id="downloadChecklist" type="button" class="btn btn-primary">DOWNLOAD CHECKLIST</button>
                    <button name="listAdhoc" id="listAdhoc" type="button" class="btn btn-primary">LIST ADHOC</button>
                    <button name="viewChecklist" id="viewChecklist" type="button" class="btn btn-primary">VIEW CHECKLIST</button>
                </div>
            </iais:action>
        </div>
    </div>
</div>
