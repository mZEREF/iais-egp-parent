<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<br><br>
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id="combinedli" class="complete ${(nowTabIn == null || 'Combined' == nowTabIn) ? 'active' : ''}" role="presentation" onclick="javascript:doChangeTab('Combined')"><a href="#combinedTab" aria-controls="combinedTab" role="tab" data-toggle="tab" >Combined</a></li>
    <c:forEach var = "item" items = "${inspectorsParticipant}" varStatus="status">
        <li id="${item.id}Tabli" class="complete ${nowTabIn == item.id ? 'active': ''}" role="presentation" onclick="javascript:doChangeTab('${item.id}')"><a href="#${item.id}Tab" aria-controls="${item.id}Tab" role="tab"
                                                             data-toggle="tab">${item.userId}</a></li>
    </c:forEach>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#combinedTab" aria-controls="combinedTab" role="tab" data-toggle="tab">Combined</a></div>
       <c:forEach var = "item" items = "${inspectorsParticipant}" varStatus="status">
           <div class="swiper-slide"><a href="#${item.id}Tab" aria-controls="${item.id}Tab" role="tab" data-toggle="tab">${item.userId}</a></div>
       </c:forEach>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
    });

    function doChangeTab(tabName) {
        var nowTabIn = $('#nowTabIn').val();
        var nowComTabIn = $('#nowComTabIn').val();
        var samTabCom = false;
        var samTabInp = false;
        var existCom = false;
        if(tabName == nowComTabIn ){
            samTabCom = true;
            existCom = true;
        }else if(tabName == "General" || tabName .indexOf("ServiceInfo") >= 0 ){
                $('#nowComTabIn').val(tabName);
                existCom = true;
        }

        if(!existCom && tabName == nowTabIn){
            samTabInp = true;
        }else if( !existCom ){
            $('#nowTabIn').val(tabName);
        }

        if(existCom && !samTabCom){
            $('#nowTabIn').val('Combined');
        }

        if( !samTabCom && !samTabInp){
            showWaiting();
          SOP.Crud.cfxSubmit("mainForm", "change");
        }


    }

</script>
