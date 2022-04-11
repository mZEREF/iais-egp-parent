<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    .margin-btm {
        margin-bottom: 30px;
    }

    .ack-font-16{
        font-size: 16px;
    }
</style>

<%-- current page: ack --%>

<label class="col-xs-12" style="font-size: 20px">Submission successful</label>
<p class="col-xs-12 margin-btm">- <strong>Submit Patient Information</strong></p>
<div class="ack-font-16">
    <p class="col-xs-12">A notification email will be sent to ${emailAddress}.</p>
    <p class="col-xs-12 margin-btm"><iais:message key="DS_MSG004" escape="false"></iais:message></p>
</div>
<div class="ack-font-16">
    <p class="col-xs-12">Submission details:</p>
    <div class="col-xs-12 col-sm-12 margin-btm table-responsive">
        <table aria-describedby="" class="table">
            <thead>
            <tr>
                <th scope="col" >Submission ID</th>
                <th scope="col" >Submitted By</th>
                <th scope="col" >Submission Date and Time</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${topSuperDataSubmissionDto.dataSubmissionDto.submissionNo}</td>
                <td>${submittedBy}</td>
                <td><fmt:formatDate value="${topSuperDataSubmissionDto.dataSubmissionDto.submitDt}" pattern="dd/MM/yyyy HH:mm"/></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>