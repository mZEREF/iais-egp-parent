<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#boardMember${index}">
                Board Member ${index}
            </a>
        </h4>
    </div>
    <div id="boardMember${index}" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Name"/>
                    <iais:value width="7" display="true">
                        <c:out value="${item.getName()}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code  code="${item.getIdType()}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${item.getIdNo()}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Designation"/>
                    <iais:value width="7" display="true">
                        <c:out value="${item.getDesignation()}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Designation Appointment Date"/>
                    <iais:value width="7" display="true">
                        <fmt:formatDate value="${item.getApptDt()}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Designation Cessation Date"/>
                    <iais:value width="7" display="true">
                        <fmt:formatDate value="${item.getCessationDt()}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Designation Cessation Reason"/>
                    <iais:value width="7" display="true">
                        <c:out value="${item.getCessationReason()}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>