<div id="${id}" class="branch-container branchColumn">
    <div class="branchColumnHeader">
        <h5 class="brch-col-header formFlowName">
            {if properties.propertiesData && properties.propertiesData.stageName}
                ${properties.propertiesData.stageName}
            {else}
                ${id}
            {/if}
        </h5>   
        <span class="branchColumnCondition">
        </span>
        <span class="branchDefaultCondition">
        </span>
    </div>
    <div class="stagesList"/>
</div>