{var id = control.getElementId(index)}
<div id="${id}" class="control ${control.properties.developerclass}">
	{if instructions}
		<div class="pf_instructions">
			{for instruction in instructions}
				<span class="control-input control-set-font control-font-normal">${instruction}</span><br/>
			{/for}
		</div>
	{/if}
	<div class="control-label-span control-set-alignment">
		<label id="${id}**label"
				class="control-label control-set-font control-font-label">${control.properties.caption|escape}</label>

		
	</div>
	<div class="control-input-span control-set-alignment">
		<span class="control-input control-set-font control-font-normal">${text}</span>
		<div id="${id}**errorMsg_right" style="display: none;" class="error_placements"></div>
	</div>
</div>
