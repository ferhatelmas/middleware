package mockstock.helper.json;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import middleware.common.StockProduct;
import mockstock.db.*;

/**
 *
 * @author felmas
 */
public class GsonWriter {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy kk:mm:ss a");

    private JsonWriter json;
    
    public GsonWriter(Writer out) {
        this.json = new JsonWriter(out);
    }
    
    public void writeTraders(List<Trader> traders) throws IOException {
        json.beginArray();
        for(Trader trader : traders) {
            writeTrader(trader);
        }
        json.endArray();
    }
    
    public void writeTrader(Trader trader) throws IOException {
        json.beginObject();
        json.name("id");
        
        if(trader == null) {
            json.nullValue();
            json.endObject();
            return;
        } 
        
        json.value(trader.getId());
        json.name("email").value(trader.getEmail());
        json.name("password").value(trader.getPassword());
        
        if(trader.getFirstname() == null) json.name("firstName").nullValue(); 
        else json.name("firstName").value(trader.getFirstname());
        
        if(trader.getLastname() == null) json.name("lastName").nullValue(); 
        else json.name("lastName").value(trader.getLastname());
        
        json.name("usrStatus").value(trader.getStatus().toString());
        json.name("usrGroup").value(trader.getUsrgroup().toString());
        
        json.name("portfolio");
        writePortfolioProducts(trader.getPortfolioproducts());
        
        json.name("transactions");
        writeTransactions(trader.getTransactions());
        
        json.endObject();
    }
    
    public void writeTransactions(List<TraderTransaction> transactions) throws IOException {
        json.beginArray();
        for(TraderTransaction t : transactions) {
            writeTraderTransaction(t);
        }
        json.endArray();
    }
    
    public void writePortfolioProducts(List<Portfolioproduct> portfolio) throws IOException {
        json.beginArray();
        for(Portfolioproduct p : portfolio) {
            writePortfolioProduct(p);
        }
        json.endArray();
    }
    
    public void writeSPHistorys(List<SPHistory> history) throws IOException {
        json.beginArray();
        for(SPHistory sph : history) {
            writeSPHistory(sph);
        }
        json.endArray();
    }
    
    public void writeStockProducts(HashMap<Integer, StockProduct> stocks) throws IOException {
        json.beginArray();
        for(int id : stocks.keySet()) {
            writeStockProductPrice(id, stocks.get(id));
        }
        json.endArray();
    }
    
    public void writeStockDiffs(HashMap<Integer, String> diffs) throws IOException {
        json.beginArray();
        for(int id : diffs.keySet()) {
            json.beginObject();
            json.name("id").value(id);
            json.name("name").value(diffs.get(id));
            json.endObject();
        }
        json.endArray();
    }
    
    public void writeStockProductPrice(int id, StockProduct sp) throws IOException {
        json.beginObject();
        json.name("id").value(id);
        json.name("name").value(sp.getStockName());
        json.name("price").value(sp.getStockPrice());
        json.endObject();
    }
    
    public void writePortfolioProduct(Portfolioproduct p) throws IOException {
        json.beginObject();
        json.name("id").value(p.getId());
        json.name("price").value(p.getPrice());
        json.name("quantity").value(p.getQuantity());
        json.name("stockResult").value(p.getStockresult());
        json.name("stockProduct");
        writeStockProduct(p.getStockproduct());
        json.endObject();
    }
    
    public void writeStockProduct(Stockproduct s) throws IOException {
        json.beginObject();
        json.name("id").value(s.getId());
        json.name("name").value(s.getName());
        json.endObject();
    }
    
    public void writeTraderTransaction(TraderTransaction t) throws IOException {
        json.beginObject();
        json.name("id").value(t.getId());
        json.name("quantity").value(t.getQuantitiy());
        json.name("type").value(t.getType().toString());
        json.name("date").value(dateFormat
            .format(new Date(t.getActiondate().getTime())));
        json.name("sphistory");
        writeSPHistory(t.getSphistory());
        json.endObject();
    }
    
    public void writeSPHistory(SPHistory sp) throws IOException {
        json.beginObject();
        json.name("id").value(sp.getId());
        json.name("price").value(sp.getPrice());
        json.name("date").value(dateFormat
            .format(new Date(sp.getHistoryDate().getTime())));
        json.name("name").value(sp.getStockproduct().getName());
        json.endObject();
    }
    
    public void writeBoolResponse(String name, boolean bool) throws IOException {
        json.beginObject();
        json.name(name).value(bool);
        json.endObject();
    }

    public void writeStringResponse(String name, String message) throws IOException {
        json.beginObject();
        json.name(name).value(message);
        json.endObject();
    }
    
    public void writeTransactionResult(double totalResult, List<Portfolioproduct> portfolio) throws IOException {
        json.beginObject();
        json.name("totalResult").value(totalResult);
        json.name("portfolio");
        writePortfolioProducts(portfolio);
        json.endObject();
    }
}
