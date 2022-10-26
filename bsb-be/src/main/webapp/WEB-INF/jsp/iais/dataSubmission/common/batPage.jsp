<div class="panel-group" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
        <div class="col-xs-12">
            <div class="table-gp">
                <%--@elvariable id="processDto" type="sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohReviewDataSubmissionDto"--%>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td style="text-align: center">Facility Classification</td>
                        <td style="text-align: center"><iais:code code="${processDto.facilityClassification}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: center">Activity Type</td>
                        <td style="text-align: center"><iais:code code="${processDto.activityType}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div>&nbsp;</div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">First Schedule Part I Biological Agents</a>
            </h4>
        </div>
        <div id="previewFacInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal min-row">
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Approval No</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Biological Agents / Toxins</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Physical Possession</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Quantity</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Nature of samples</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Second Schedule Biological Agents</a>
            </h4>
        </div>
        <div id="previewBatInfo" class="panel-collapse collapse">
            <div class="panel-body">
                <%--                                                                <c:forEach var="info" items="${consumeNotification.consumptionNotList}" varStatus="status">--%>
                <div class="panel-main-content form-horizontal min-row">
                    <div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Approval No</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Biological Agents / Toxins</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Physical Possession</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Quantity</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Nature of samples</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value=""/></p>
                            </div>
                            <div class="clear"></div>
                        </div>
                    </div>
                    <%--                                                                        </c:forEach>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>