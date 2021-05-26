<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/25
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<script>

    var chartRegister  = function () {
        Chart.pluginService.register({
            beforeDraw: function (chart) {
                var width = chart.chart.width,
                    height = chart.chart.height,
                    ctx = chart.chart.ctx;
                ctx.restore();
                var fontSize = (height / 114).toFixed(2);
                ctx.font = fontSize + "em sans-serif";
                ctx.textBaseline = "middle";
                var text = chart.config.options.elements.center.text,
                    textX = Math.round((width - ctx.measureText(text).width) / 2),
                    textY = height / 2;
                ctx.fillText(text, textX, textY);
                ctx.save();
            }
        });
    };

    var initChart = function (canvasName) {
        var jsonStr = $('input[name="'+canvasName+'Val"]').val();

        var redSize = 0;
        var blueSize = 0;
        var yellowSize = 0;
        var redCount = '-';
        if(jsonStr != null && jsonStr != '' && jsonStr != undefined){
            var canvasJsonObj = eval(JSON.parse(jsonStr));
            redSize = canvasJsonObj.dashRedCount;
            blueSize = canvasJsonObj.dashBlueCount;
            yellowSize = canvasJsonObj.dashAmberCount;
            if('0' != canvasJsonObj.dashStageCount){
                redCount = canvasJsonObj.dashStageCount;
            }
        }
        var data = {
            labels: ["Red", "Blue", "Yellow"],
            datasets: [{
                data: [redSize, blueSize, yellowSize],
                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"]
            }]
        };

        var canvasId = canvasName+'Canvas';
        var promisedDeliveryChart = new Chart(document.getElementById(canvasId), {
            type: 'doughnut',
            data: data,
            options: {
                elements: {
                    center: {
                        text: redCount
                    }
                },
                cutoutPercentage: 75,
                legend: {
                    display: false
                }
            }
        });

        //reset chart title size
        var chart1Title = $('#'+canvasId).closest('div.dashboard-tile');
        chart1Title.css('width',$('#'+canvasId).width() * 1.2);
        chart1Title.css('height',$('#'+canvasId).height() * 1.4);
    };
</script>
