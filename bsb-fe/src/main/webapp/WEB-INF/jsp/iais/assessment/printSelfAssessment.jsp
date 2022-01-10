<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <br>
    <div id="printContent">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <%@include file="../chklst/checkListAnswer.jsp"%>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    $(function () {
        document.body.innerHTML = document.getElementById('printContent').innerHTML;
        window.print();
    });
</script>