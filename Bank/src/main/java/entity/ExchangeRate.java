package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Exchange_Rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "USD_buy")
    private double usdBuy;

    @Column(name = "USD_sell")
    private double usdSell;
    @Column(name = "EURO_buy")
    private double euroBuy;
    @Column(name = "EURO_sell")
    private double euroSell;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_info")
    private Date creationDate;

    public ExchangeRate(double usdBuy, double usdSell, double euroBuy, double euroSell) {
        this.usdBuy = usdBuy;
        this.usdSell = usdSell;
        this.euroBuy = euroBuy;
        this.euroSell = euroSell;
        this.creationDate = new Date();
    }

    public ExchangeRate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getUsdBuy() {
        return usdBuy;
    }

    public void setUsdBuy(double usdBuy) {
        this.usdBuy = usdBuy;
    }

    public double getUsdSell() {
        return usdSell;
    }

    public void setUsdSell(double usdSell) {
        this.usdSell = usdSell;
    }

    public double getEuroBuy() {
        return euroBuy;
    }

    public void setEuroBuy(double euroBuy) {
        this.euroBuy = euroBuy;
    }

    public double getEuroSell() {
        return euroSell;
    }

    public void setEuroSell(double euroSell) {
        this.euroSell = euroSell;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    @PrePersist
    public void onCreate() {
        creationDate = new Date();
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", usdBuy=" + usdBuy +
                ", usdSell=" + usdSell +
                ", euroBuy=" + euroBuy +
                ", euroSell=" + euroSell +
                ", creationDate=" + creationDate +
                '}';
    }
}
