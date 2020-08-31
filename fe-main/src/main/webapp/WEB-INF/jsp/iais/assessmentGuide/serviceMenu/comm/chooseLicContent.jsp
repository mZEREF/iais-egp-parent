<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
    .align-lic-table{
        margin-left: -30px;
    }
</style>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <h3>
            You may choose to align the expiry date of your new licence(s) to any of your existing licences.
        </h3>
        <p><span>If you don't select a licence, MOH will assign an expiry date</span></p>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <table class="table">
            <thead>
            <tr style="font-size: 16px;">
                <td></td>
                <td><strong>Licence No.</strong></td>
                <td><strong>Type</strong></td>
                <td><strong>Premises</strong></td>
                <td style="width:17%;"><strong>Expires on</strong></td>
            </tr>
            </thead>
            <tbody id="licBodyDiv"></tbody>
        </table>
    </div>
</div>
<div class="row">
    <div class="col-xs-6 col-md-7">
        <div id="licPagDiv"></div>
    </div>
</div>