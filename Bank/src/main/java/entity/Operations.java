package entity;

import javax.persistence.*;

@Entity
@Table(name = "Transactions")
public class Operations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    @Column(name = "sender_first_name")
    private String senderFirstName;
    @Column(name = "sender_last_name")
    private String senderLastName;

    @Column(name = "reciever_first_name")
    private String recieverFirstName;

    @Column(name = "reciever_last_name")
    private String recieverLastName;

    @Column(name = "amount")
    private double amount;
    @Column(name = "currency")
    private String currency;

    public Operations(String senderFirstName, String senderLastName, String recieverFirstName, String recieverLastName, double amount, String currency) {
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.recieverFirstName = recieverFirstName;
        this.recieverLastName = recieverLastName;
        this.amount = amount;
        this.currency = currency;
    }



    public Operations() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getRecieverFirstName() {
        return recieverFirstName;
    }

    public void setRecieverFirstName(String recieverFirstName) {
        this.recieverFirstName = recieverFirstName;
    }

    public String getRecieverLastName() {
        return recieverLastName;
    }

    public void setRecieverLastName(String recieverLastName) {
        this.recieverLastName = recieverLastName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Transactions: " +
                "id = " + id +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", recieverFirstName='" + recieverFirstName + '\'' +
                ", recieverLastName='" + recieverLastName + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
