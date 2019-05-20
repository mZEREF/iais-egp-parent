<input type="button" name="property::${id}::${name}" 
	{if defined('cssClass') && cssClass} class="${cssClass|escape}" {/if} 
	{if defined('value') && value} value="${value|escape}" {/if}
	/>