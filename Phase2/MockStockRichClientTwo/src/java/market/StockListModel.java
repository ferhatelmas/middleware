package market;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author felmas
 */
/*
 * Custom List model for event list
 */
public class StockListModel implements ListModel {

    private LogList daList;

    public StockListModel() {
        this.daList = new LogList();
    }
    
    public void clear() {
        daList.clear();
        daList.add("Clear");
    }
    
    public void addFront(String s) {
        daList.addReverse(s);
    }
    
    @Override
    public Object getElementAt(int index) {
        return daList.get(index);
    }

    @Override
    public int getSize() {
        return daList.size();
    }

    // Listeners for model are unnecessary
    // since ui update is called by subscriber onMessage
    @Override
    public void addListDataListener(ListDataListener l) {
        // unnecessary
    }
    
    @Override
    public void removeListDataListener(ListDataListener l) {
        // unnecessary
    }
    
    public void clearNormal() {
        daList.clear();
    }
    
    
    
    
}
