package com.ecquaria.cloud.moh.iais.tags;

import java.io.Serializable;

/**
 * SelectOption.java
 *
 * @author jinhua
 *
 */
@SuppressWarnings("rawtypes")
public class SelectOption implements Serializable, Comparable {
    private static final long serialVersionUID = -6341061262926396901L;

    private String value;
    private String text;

    public SelectOption(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectOption other = (SelectOption) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			       return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			       return false;
		return true;
	}

    @Override
    public int hashCode() {
        int textHash = 0;
        int valueHash = 0;
        if (text != null)
            textHash = text.hashCode();
        
        if (value != null)
            valueHash = value.hashCode();

        return textHash + valueHash;
    }

    @Override
    public int compareTo(Object o) {
        SelectOption so = (SelectOption)o;
        String otherText = so.getText();
        return this.text.compareTo(otherText);
    }
	
}
