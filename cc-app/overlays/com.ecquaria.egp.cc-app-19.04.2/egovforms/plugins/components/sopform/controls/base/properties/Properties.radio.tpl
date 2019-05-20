<div>
{for row in options}
	<input type="button" value="${row[0]|escape}" title="${row[0]|escape}" 
	class="{if row[0] == value} active {/if} 
	{if row_index == 0} inner-lft-switch {/if} 
	{if row_index == options.length-1} inner-rgh-switch {/if} 
	{if row_index > 0 && row_index < options.length-1} inner-mid-switch {/if} 
	 " name="property::${id}::${name}" id="property::${id}::${name}_${row_index}">
{/for}
<input type="hidden" name="property::${id}::${name}" id="property::${id}::${name}_${row_index}" value="${value}">
</div>