package trader;

import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 *
 * @author felmas
 */
/*
 * This event type moves necessary information between mouse clicks of GUIs
 */
public class StockEvent extends MouseEvent {
    
    private String type;
    private int qty;
    private int stockId;
    private StockProductGUI sourceStock;

    public StockEvent(String type, int qty, int stockId, StockProductGUI sourceStock, Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger) {
        super(source, id, when, modifiers, x, y, clickCount, popupTrigger);
        this.type = type;
        this.qty = qty;
        this.stockId = stockId;
        this.sourceStock = sourceStock;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StockProductGUI getSourceStock() {
        return sourceStock;
    }

    public void setSourceStock(StockProductGUI sourceStock) {
        this.sourceStock = sourceStock;
    }

}
