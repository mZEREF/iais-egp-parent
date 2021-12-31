<div class="panel panel-default">
    <div class="alert alert-info" role="alert"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <c:set var="app" value="${processDto.applicationInfoDto}"/>
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td style="text-align: center">Application Type</td>
                        <td style="text-align: center"><iais:code code="${app.appType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: center">Reference No.</td>
                        <td style="text-align: center"><c:out value="${app.referenceNo}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: center">Submission Date</td>
                        <td style="text-align: center"><c:out value="${app.submissionDate}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: center">Current Status</td>
                        <td style="text-align: center"><iais:code code="${app.status}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="text-align: center;margin: 10px 0 20px 0">
    <a href="javascript:void(0);">
        <button id="viewAppBtn" type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
</div>
<c:set var="profile" value="${app.facilityProfileDto}"/>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Facility Profile</strong></div>
    <div class="panel-collapse in collapse">
        <div class="panel-main-content form-horizontal min-row">
            <div style="padding: 5px 20px">
                <div class="form-group">
                    <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                    <div class="col-sm-7 col-md-5 col-xs-7">
                        <p><c:out value="${profile.facName}"/></p>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                    <div class="col-sm-7 col-md-5 col-xs-7">
                        <p><c:out value="${profile.facAddress}"/></p>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="form-group">
                    <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                    <div class="col-sm-7 col-md-5 col-xs-7">
                        <p><c:out value="${profile.isProtected}"/></p>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div style="text-align: left">
    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
</div>