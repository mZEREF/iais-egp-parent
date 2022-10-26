<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <div class="form-horizontal">
                <div class="form-group " style="margin-top: 50px">
                    <div class="col-sm-5 control-label">
                        <label>Has the facility appointed an Approved Facility Certifier </label>
                    </div>
                    <div class="col-sm-6 col-md-7">
                        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                            <label for="hasAppointedCertifier">Yes</label>
                            <input type="radio" name="appointed" id="hasAppointedCertifier" value="Y" <c:if test="${afcSelectionInfo.appointed eq 'Y'}">checked="checked"</c:if> disabled />
                        </div>
                        <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                            <label for="notAppointedCertifier">No</label>
                            <input type="radio" name="appointed" id="notAppointedCertifier" value="N" <c:if test="${afcSelectionInfo.appointed eq 'N'}">checked="checked"</c:if> disabled />
                        </div>
                    </div>
                </div>

                <c:if test="${afcSelectionInfo.appointed eq 'Y'}">
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label>Select Approved Facility Certifier </label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label><iais:code code="${afcSelectionInfo.afc}"/></label>
                        </div>
                    </div>

                    <c:if test="${afcSelectionInfo.reasonForChoose ne null && afcSelectionInfo.reasonForChoose ne ''}">
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label>Reasons for choosing this AFC </label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label><c:out value="${afcSelectionInfo.reasonForChoose}"/></label>
                            </div>
                        </div>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>