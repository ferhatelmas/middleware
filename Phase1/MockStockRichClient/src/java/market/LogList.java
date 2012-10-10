package market;

import java.util.ArrayList;

/*
 * 
 * @uthor felmas
 */

// Your log list is modified while functionality is kept
public class LogList extends ArrayList{
    
    public LogList() {
        super();
    }
    
    public void addReverse(Object o) {
        int size = this.size();
        if(size == 0) {
            this.add(o);
        } else { 
            if(size == 1 && "Clear".equals(this.get(0))) {
                this.clear();
                this.add(o);
            } else {
                this.add(this.get(size-1));
                for(int i=size-1;i>0;i--){
                    this.set(i,this.get(i-1));
                }
                this.set(0,o);
            }
        }
    }
}
