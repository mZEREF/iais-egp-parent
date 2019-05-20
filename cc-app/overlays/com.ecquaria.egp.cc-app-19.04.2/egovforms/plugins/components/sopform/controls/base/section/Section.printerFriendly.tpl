{var id = control.getElementId()}
<div id="${id}" class="section control ${control.properties.developerclass}">
	{if instructions}
		<div class="pf_instructions">
			{for instruction in instructions}
				<span class="control-input control-set-font control-font-normal">${instruction}</span><br/>
			{/for}
		</div>
	{/if}
	{if control.properties.border == true}
	<fieldset>
		<legend class="control-set-font control-font-header">${control.properties.title}</legend>
	{/if}
	{if control.properties.border == false}
		<span class="control-set-font control-font-header">${control.properties.title|escape}</span>
	{/if}
		<div id="${id}**errorMsg_section_top" class="error_placements"></div>
		<table class="control-grid">
		</table>
		<div id="${id}**errorMsg_section_bottom" class="error_placements"></div>
	{if control.properties.border == true}
	</fieldset>
	{/if}
</div>
