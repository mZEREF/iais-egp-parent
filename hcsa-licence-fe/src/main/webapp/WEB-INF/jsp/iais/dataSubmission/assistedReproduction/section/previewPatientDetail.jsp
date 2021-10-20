<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--test start-->
<c:set var="name" value="Joelle Lim" />
<c:set var="idType" value="IDTYPE001" />
<c:set var="idNumber" value="S7812344D" />
<!--test end-->

<c:set var="headingSign" value="${not empty svcSecMap.licensee ? 'incompleted' : 'completed'}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row>
                    <iais:field width="5" value="Name (as per NRIC/Passport)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${idNumber}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>