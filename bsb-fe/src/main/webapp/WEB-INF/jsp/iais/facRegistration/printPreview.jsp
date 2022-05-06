<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <br>
    <div id="printContent">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <fac:preview compProfile="${organizationAddress}" facProfile="${facProfile}" facOperator="${facOperator}"
                                 facAuth="${facAuth}" facAdminOfficer="${facAdminOfficer}" facCommittee="${facCommittee}"
                                 batList="${batList}" afc="${afc}" declarationConfigList="${configList}" declarationAnswerMap="${answerMap}"
                                 containsAfcJudge="${isCertifiedFacility}" containsBatListJudge="${isUncertifiedFacility}">
                        <jsp:attribute name="editFrag"/>
                        <jsp:attribute name="docFrag">
                            <fac:doc-preview docSettings="${docSettings}" savedFiles="${savedFiles}" newFiles="${newFiles}" otherDocTypes="${otherDocTypes}"/>
                        </jsp:attribute>
                    </fac:preview>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        $('.collapse').collapse();
        document.body.innerHTML = document.getElementById('printContent').innerHTML;
        window.print();
    });
</script>