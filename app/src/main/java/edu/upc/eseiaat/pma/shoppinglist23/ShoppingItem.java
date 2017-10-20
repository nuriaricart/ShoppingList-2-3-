package edu.upc.eseiaat.pma.shoppinglist23;

public class ShoppingItem {
    private String text;
    private boolean checked;

    public ShoppingItem(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
