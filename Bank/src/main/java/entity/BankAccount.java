package entity;

import javax.persistence.*;

@Entity
@Table(name = "Bank_Account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "card_number")
    private long cardNumber;
    @Column(name = "money_in_UAH")
    private double moneyInUAH;
    @Column(name = "money_in_USD")
    private double moneyInUSD;
    @Column(name = "money_in_EURO")
    private double moneyInEURO;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public BankAccount() {
    }

    public BankAccount(long cardNumber, double moneyInUAH, double moneyInUSD, double moneyInEURO) {
        this.cardNumber = cardNumber;
        this.moneyInUAH = moneyInUAH;
        this.moneyInUSD = moneyInUSD;
        this.moneyInEURO = moneyInEURO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getMoneyInUAH() {
        return moneyInUAH;
    }

    public void setMoneyInUAH(double moneyInUAH) {
        this.moneyInUAH = moneyInUAH;
    }

    public double getMoneyInUSD() {
        return moneyInUSD;
    }

    public void setMoneyInUSD(double moneyInUSD) {
        this.moneyInUSD = moneyInUSD;
    }

    public double getMoneyInEURO() {
        return moneyInEURO;
    }

    public void setMoneyInEURO(double moneyInEURO) {
        this.moneyInEURO = moneyInEURO;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMoneyIn(String currency, double amount) {
        if (currency.equalsIgnoreCase("UAH")) {
            this.moneyInUAH = amount;
        } else if (currency.equalsIgnoreCase("USD")) {
            this.moneyInUSD = amount;
        } else if (currency.equalsIgnoreCase("EURO")) {
            this.moneyInEURO = amount;
        }
    }

    public double getMoneyIn(String currency) {
        if (currency.equalsIgnoreCase("UAH")) {
            return moneyInUAH;
        } else if (currency.equalsIgnoreCase("USD")) {
            return moneyInUSD;
        } else if (currency.equalsIgnoreCase("EURO")) {
            return moneyInEURO;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", moneyInUAH=" + moneyInUAH +
                ", moneyInUSD=" + moneyInUSD +
                ", moneyInEURO=" + moneyInEURO +
                ", client=" + client +
                '}';
    }
}
