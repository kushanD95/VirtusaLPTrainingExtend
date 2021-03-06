package lk.harvestsellingsystem.sellcloud.sellservice.model;

import lk.harvestsellingsystem.sellcloud.servicecloudmodel.model.sell.Sell;

public class SimpleResponse implements Response {

    private Sell sell;

    public SimpleResponse(Sell sell) {
        this.sell = sell;
    }

    public Sell getSell() {
        return sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }
}
