<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@taglib prefix="approvalApp" tagdir="/WEB-INF/tags/approvalApp" %>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <br>
    <div id="printContent">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <approvalApp:preview facProfileDto="${facProfileDto}" batInfo="${batInfo}" facAuthorisedList="${facAuthorisedDto.facAuthorisedDtoList}" processType="${processType}"
                                         facProfileEditJudge="true" batListEditJudge="true" docEditJudge="true">
                        <jsp:attribute name="editFrag"/>
                        <jsp:attribute name="docFrag">
                            <approvalApp:doc-preview docSettings="${docSettings}" savedFiles="${savedFiles}" newFiles="${newFiles}" otherDocTypes="${otherDocTypes}"/>
                        </jsp:attribute>
                    </approvalApp:preview>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        $('.collapse').collapse();
        var printRow = document.getElementById('printRow');
        printRow.parentNode.removeChild(printRow);
        $('a').each(function () {
            var newSpan = document.createElement("span");
            newSpan.innerText = this.innerText;
            this.parentNode.replaceChild(newSpan, this);
        });
        document.body.innerHTML = document.getElementById('printContent').innerHTML;
        setTimeout(function () {
            window.print();
        }, 1000);
    });
</script>