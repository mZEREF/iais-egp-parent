<%--@elvariable id="insInfo" type="sg.gov.moh.iais.egp.bsb.dto.inspection.InsInfoDto"--%>
<%--@elvariable id="iais_Audit_Trail_dto_Attr" type="com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto"--%>
<div class="col-xs-12">
    <div class="table-gp">
        <div class="form-horizontal" style="margin-left: auto; margin-right: auto; max-width: 1080px;">
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspection Date</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p>03/03/2022</p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspector Lead</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p>DO 1</p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">Inspector</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p>DO 1, DO 2</p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="otherInspectors">Other Inspectors</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="otherInspectors" name="otherInspectors" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal">No. of Non-Compliance</label>
                <div class="col-sm-7 col-md-5 col-xs-10">
                    <p>0</p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="internalRemarks">Other Inspectors</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="internalRemarks" name="internalRemarks" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="recommendation">Recommendation</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="recommendation" name="recommendation" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>
            <div class="form-group">
                <label class="col-xs-12 col-md-4 control-label label-normal" for="observation">Observation</label>
                <div class="col-sm-7 col-md-6 col-xs-10">
                    <p>
                        <textarea id="observation" name="observation" style="width: 100%;margin-bottom: 15px;" rows="2"  maxlength="500"></textarea>
                    </p>
                </div>
                <div class="clear"></div>
            </div>

            <div style="text-align: right">
                <button name="uploadChecklist" id="uploadChecklist" type="button" class="btn btn-primary">UPLOAD CHECKLIST</button>
                <button name="downloadChecklist" id="downloadChecklist" type="button" class="btn btn-primary">DOWNLOAD CHECKLIST</button>
                <button name="listAdhoc" id="listAdhoc" type="button" class="btn btn-primary">LIST ADHOC</button>
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