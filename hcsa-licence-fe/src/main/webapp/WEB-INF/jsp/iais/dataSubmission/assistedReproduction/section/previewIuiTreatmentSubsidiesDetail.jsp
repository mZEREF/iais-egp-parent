<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                IUI Treatment Subsidies
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Please indicate IUI Co-funding"/>
                    <iais:value width="7" cssClass="col-md-7">
                         <iais:code code="${PICF}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
