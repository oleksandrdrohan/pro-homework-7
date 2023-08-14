import entity.BankAccount;
import entity.Client;
import entity.ExchangeRate;
import entity.Operations;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("Bank");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add client");
                    System.out.println("2: delete client");
                    System.out.println("3: update client");
                    System.out.println("4: view clients");
                    System.out.println("5: view bank account");
                    System.out.println("6: add bank account");
                    System.out.println("7: refill");
                    System.out.println("8: money transfer");
                    System.out.println("9: view all exchange rates");
                    System.out.println("10: update exchange rate");
                    System.out.println("11: check all money of a client in UAH");
                    System.out.println("12: money exchange");
                    System.out.println("13: view transactions");
                    System.out.print("-------> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addClient(sc);
                            break;
                        case "2":
                            deleteClient(sc);
                            break;
                        case "3":
                            updateClient(sc);
                            break;
                        case "4":
                            viewClients();
                            break;
                        case "5":
                            viewBankAccounts();
                            break;
                        case "6":
                            addBankAccount(sc);
                            break;
                        case "7":
                            refill(sc);
                            break;
                        case "8":
                            moneyTransfer(sc);
                            break;
                        case "9":
                            viewAllExchangeRates();
                            break;
                        case "10":
                            updateExchangeRate(sc);
                            break;
                        case "11":
                            checkAllMoneyInUAH(sc);
                            break;
                        case "12":
                            moneyExchange(sc);
                            break;
                        case "13":
                            viewTransactions();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static void addClient(Scanner sc) {
        System.out.println("Enter first name: ");
        String firstName = sc.nextLine();
        System.out.println("Enter last name: ");
        String lastName = sc.nextLine();
        System.out.println("Enter age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);
        em.getTransaction().begin();
        try {
            Client client = new Client(firstName, lastName, age);
            em.persist(client);
            em.getTransaction().commit();
            System.out.println("New client add: " + client);
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
            em.getTransaction().rollback();
        }
    }

    private static void deleteClient(Scanner sc) {
        System.out.println("Enter client id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Client client = em.find(Client.class, id);
        if (client == null) {
            System.out.println("Client not found!");
            return;
        }
        System.out.println("Are you sure you want to delete this client? " + client);
        System.out.println("Write YES or NO: ");
        String answer = sc.nextLine();

        boolean shouldDelete = answer.equalsIgnoreCase("YES");

        if (shouldDelete) {
            em.getTransaction().begin();
            em.remove(client);
            em.getTransaction().commit();
            System.out.println("Client " + client + " has been deleted");
        } else {
            System.out.println("Deletion cancelled");
        }
    }

    private static void updateClient(Scanner sc) {
        try {
            System.out.println("Enter client id: ");
            String sId = sc.nextLine();
            long id = Long.parseLong(sId);

            Client client = em.find(Client.class, id);
            if (client == null) {
                System.out.println("Client not found!");
                return;
            }

            System.out.println("Enter new client's first name: ");
            String firstName = sc.nextLine();
            System.out.println("Enter new client's last name: ");
            String lastName = sc.nextLine();
            System.out.println("Enter new client's age: ");
            String sAge = sc.nextLine();
            int age = Integer.parseInt(sAge);

            em.getTransaction().begin();
            client.setLastName(lastName);
            client.setFirstName(firstName);
            client.setAge(age);
            em.getTransaction().commit();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numeric values.");
        } catch (Exception e) {
            System.out.println("An error occurred during updating the client: " + e.getMessage());
            em.getTransaction().rollback();
        }
    }

    private static void viewClients() {
        Query query = em.createQuery("SELECT c FROM Client c", Client.class);
        List<Client> list = (List<Client>) query.getResultList();

        for (Client cl : list) {
            System.out.println(cl);
        }
    }

    private static void viewAllExchangeRates() {
        Query query = em.createQuery("SELECT c FROM ExchangeRate c", ExchangeRate.class);
        List<ExchangeRate> list = (List<ExchangeRate>) query.getResultList();

        for (ExchangeRate er : list) {
            System.out.println(er);
        }
    }

    private static void addBankAccount(Scanner sc) {
        System.out.println("Enter card number: ");
        String cardNumberInput = sc.nextLine();

        System.out.println("Enter client's id to link account: ");
        String clientIdInput = sc.nextLine();

        try {
            int cardNumber = Integer.parseInt(cardNumberInput);
            long clientId = Long.parseLong(clientIdInput);

            Client client = em.find(Client.class, clientId);

            if (client != null) {
                BankAccount ba = new BankAccount();
                ba.setCardNumber(cardNumber);
                ba.setClient(client);

                em.getTransaction().begin();
                em.persist(ba);
                client.getBa().add(ba);
                em.getTransaction().commit();

                System.out.println("Bank account added successfully.");
            } else {
                System.out.println("Client with the provided id not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for card number or client id.");
        }
    }


    private static void viewBankAccounts() {
        Query query = em.createQuery("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.ba", Client.class);
        List<Client> clients = (List<Client>) query.getResultList();

        for (Client client : clients) {
            System.out.println(client);
            for (BankAccount account : client.getBa()) {
                System.out.println("    " + account);
            }
        }
    }

    private static void refill(Scanner sc) {
        try {
            System.out.println("Choose the currency in which you want to top up the account (UAH|USD|EURO): ");
            String currency = sc.nextLine();

            System.out.println("What bank account do you want to top up? (Enter card number): ");
            String sBankAccountCard = sc.nextLine();
            long cardNumber = Long.parseLong(sBankAccountCard);

            System.out.println("How much money do you want to top up the account?");
            String sMoney = sc.nextLine();

            Query queryBA = em.createQuery("SELECT x FROM BankAccount x WHERE x.cardNumber = :cardNumber");
            queryBA.setParameter("cardNumber", cardNumber);
            BankAccount bA = (BankAccount) queryBA.getSingleResult();

            double money = Double.parseDouble(sMoney);
            double moneyInAccount = 0;
            boolean isUAH = currency.equalsIgnoreCase("UAH");
            boolean isUSD = currency.equalsIgnoreCase("USD");
            boolean isEURO = currency.equalsIgnoreCase("EURO");

            em.getTransaction().begin();


            if (isUSD) {
                moneyInAccount = bA.getMoneyInUSD();
                bA.setMoneyInUSD(moneyInAccount + money);
            }
            if (isEURO) {
                moneyInAccount = bA.getMoneyInEURO();
                bA.setMoneyInEURO(moneyInAccount + money);
            }
            if (isUAH) {
                moneyInAccount = bA.getMoneyInUAH();
                bA.setMoneyInUAH(moneyInAccount + money);
            }

            em.getTransaction().commit();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter valid numbers.");
        } catch (NoResultException e) {
            System.out.println("Card number not found.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    private static void updateExchangeRate(Scanner sc) {
        System.out.println("Enter USD sell : ");
        String usdSellInput = sc.nextLine();
        double usdSell = Double.parseDouble(usdSellInput);
        System.out.println("Enter USD buy : ");
        String usdBuyInput = sc.nextLine();
        double usdBuy = Double.parseDouble(usdBuyInput);
        System.out.println("Enter EURO sell : ");
        String euroSellInput = sc.nextLine();
        double euroSell = Double.parseDouble(euroSellInput);
        System.out.println("Enter EURO buy : ");
        String euroBuylInput = sc.nextLine();
        double euroBuy = Double.parseDouble(euroBuylInput);
        em.getTransaction().begin();
        try {
            ExchangeRate exchangeRate = new ExchangeRate(usdBuy, usdSell, euroBuy, euroSell);
            em.persist(exchangeRate);
            em.getTransaction().commit();
            System.out.println("Exchange rate updated: " + exchangeRate);
        } catch (Exception e) {
            System.out.println("Something goes wrong!");
            em.getTransaction().rollback();
        }
    }

    public static void moneyTransfer(Scanner sc) {
        System.out.println("Enter card number of receiver: ");
        String receiverCardNumberInput = sc.nextLine();
        System.out.println("Enter the card from which you want to make a transfer: ");
        String senderCardNumberInput = sc.nextLine();
        System.out.println("Choose the currency in which you want to send money (UAH|USD|EURO): ");
        String currency = sc.nextLine();
        System.out.println("Enter how much money do you want to send: ");
        String moneyToTransferInput = sc.nextLine();

        try {
            long receiverCardNumber = Long.parseLong(receiverCardNumberInput);
            long senderCardNumber = Long.parseLong(senderCardNumberInput);
            double moneyToTransfer = Double.parseDouble(moneyToTransferInput);
            double senderMoney;

            Query query = em.createQuery("SELECT x FROM BankAccount x WHERE x.cardNumber = :cardNumber");
            query.setParameter("cardNumber", receiverCardNumber);
            BankAccount bankAccountReceiver = (BankAccount) query.getSingleResult();
            query.setParameter("cardNumber", senderCardNumber);
            BankAccount bankAccountSender = (BankAccount) query.getSingleResult();

            boolean isUAH = currency.equalsIgnoreCase("UAH");
            boolean isUSD = currency.equalsIgnoreCase("USD");
            boolean isEURO = currency.equalsIgnoreCase("EURO");

            em.getTransaction().begin();
            if (isUSD) {
                senderMoney = bankAccountSender.getMoneyInUSD();
                if (senderMoney > moneyToTransfer && (senderMoney - moneyToTransfer) > 0) {
                    senderMoney = senderMoney - moneyToTransfer;
                    bankAccountSender.setMoneyInUSD(senderMoney);
                    double receiverMoney = bankAccountReceiver.getMoneyInUSD();
                    receiverMoney = receiverMoney + moneyToTransfer;
                    bankAccountReceiver.setMoneyInUSD(receiverMoney);
                } else System.out.println("Not enough money at sender!");
            }
            if (isEURO) {
                senderMoney = bankAccountSender.getMoneyInEURO();
                if (senderMoney > moneyToTransfer && (senderMoney - moneyToTransfer) > 0) {
                    senderMoney = senderMoney - moneyToTransfer;
                    bankAccountSender.setMoneyInEURO(senderMoney);
                    double receiverMoney = bankAccountReceiver.getMoneyInEURO();
                    receiverMoney = receiverMoney + moneyToTransfer;
                    bankAccountReceiver.setMoneyInEURO(receiverMoney);
                } else System.out.println("Not enough money at sender!");
            }
            if (isUAH) {
                senderMoney = bankAccountSender.getMoneyInUAH();
                if (senderMoney > moneyToTransfer && (senderMoney - moneyToTransfer) > 0) {
                    senderMoney = senderMoney - moneyToTransfer;
                    bankAccountSender.setMoneyInUAH(senderMoney);
                    double receiverMoney = bankAccountReceiver.getMoneyInUAH();
                    receiverMoney = receiverMoney + moneyToTransfer;
                    bankAccountReceiver.setMoneyInUAH(receiverMoney);
                } else System.out.println("Not enough money at sender!");
            }

            Operations os = new Operations(bankAccountSender.getClient().getFirstName(), bankAccountSender.getClient().getLastName(), bankAccountReceiver.getClient().getFirstName(),
                    bankAccountReceiver.getClient().getLastName(), moneyToTransfer, currency);
            em.persist(os);
            em.getTransaction().commit();
        } catch (NumberFormatException e) {
            em.getTransaction().rollback();
            System.out.println("Invalid card number format. Please enter a valid number.");
        } catch (NoResultException e) {
            em.getTransaction().rollback();
            System.out.println("Bank account not found.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void checkAllMoneyInUAH(Scanner sc) {
        System.out.println("Enter client's card number: ");
        String cardNumberInput = sc.nextLine();
        long cardNumber = Long.parseLong(cardNumberInput);
        Query queryBA = em.createQuery("SELECT x FROM BankAccount x WHERE x.cardNumber = :cardNumber");
        queryBA.setParameter("cardNumber", cardNumber);
        BankAccount bankAccount = (BankAccount) queryBA.getSingleResult();

        Query queryER = em.createQuery("SELECT u FROM ExchangeRate u ORDER BY u.id DESC");
        queryER.setMaxResults(1);
        ExchangeRate er = (ExchangeRate) queryER.getSingleResult();

        double USDExchange = er.getUsdSell();
        double EUROExchange = er.getUsdSell();
        double bankAccountUSD = bankAccount.getMoneyInUSD();
        double bankAccountEURO = bankAccount.getMoneyInEURO();
        double bankAccountUAH = bankAccount.getMoneyInUAH();

        double sum = (bankAccountUSD * USDExchange) + (bankAccountEURO * EUROExchange) + bankAccountUAH;
        System.out.println("Total amount of money in  UAH at card number " + bankAccount.getCardNumber() + " = " + sum);
    }

    public static void moneyExchange(Scanner sc) {
        try {
            System.out.println("Enter your card number in which you want to do exchange: ");
            long cardNumber = Long.parseLong(sc.nextLine());

            System.out.println("Choose the currency you want to exchange from (UAH|USD|EURO): ");
            String sourceCurrencyInput = sc.nextLine();

            System.out.println("Choose the currency you want to exchange to (UAH|USD|EURO): ");
            String targetCurrencyInput = sc.nextLine();

            System.out.println("Enter amount of money to exchange: ");
            double amountToExchange = Double.parseDouble(sc.nextLine());

            em.getTransaction().begin();

            Query query = em.createQuery("SELECT x FROM BankAccount x WHERE x.cardNumber = :cardNumber");
            query.setParameter("cardNumber", cardNumber);
            BankAccount bankAccount = (BankAccount) query.getSingleResult();

            Query queryER = em.createQuery("SELECT u FROM ExchangeRate u ORDER BY u.id DESC");
            queryER.setMaxResults(1);
            ExchangeRate er = (ExchangeRate) queryER.getSingleResult();

            double sourceCurrencyRate = 0.0;
            double targetCurrencyRate = 0.0;

            if (sourceCurrencyInput.equalsIgnoreCase("UAH")) {
                sourceCurrencyRate = 1.0;
            } else if (sourceCurrencyInput.equalsIgnoreCase("USD")) {
                sourceCurrencyRate = er.getUsdBuy();
            } else if (sourceCurrencyInput.equalsIgnoreCase("EURO")) {
                sourceCurrencyRate = er.getEuroBuy();
            }

            if (targetCurrencyInput.equalsIgnoreCase("UAH")) {
                targetCurrencyRate = 1.0;
            } else if (targetCurrencyInput.equalsIgnoreCase("USD")) {
                targetCurrencyRate = er.getUsdSell();
            } else if (targetCurrencyInput.equalsIgnoreCase("EURO")) {
                targetCurrencyRate = er.getEuroSell();
            }

            if (sourceCurrencyRate == 0 || targetCurrencyRate == 0) {
                System.out.println("Exchange rates not available for selected currencies.");
                em.getTransaction().rollback();
                return;
            }

            double sourceAmountInUAH = amountToExchange / sourceCurrencyRate;
            double targetAmountInTargetCurrency = sourceAmountInUAH * targetCurrencyRate;

            if (sourceCurrencyInput.equalsIgnoreCase(targetCurrencyInput)) {
                System.out.println("You can't exchange the same currency!");
            } else if (sourceCurrencyInput.equalsIgnoreCase("UAH")) {
                bankAccount.setMoneyInUAH(bankAccount.getMoneyInUAH() - amountToExchange);
                bankAccount.setMoneyIn(targetCurrencyInput, bankAccount.getMoneyIn(targetCurrencyInput) + targetAmountInTargetCurrency);
            } else {
                bankAccount.setMoneyIn(sourceCurrencyInput, bankAccount.getMoneyIn(sourceCurrencyInput) - amountToExchange);
                bankAccount.setMoneyIn(targetCurrencyInput, bankAccount.getMoneyIn(targetCurrencyInput) + targetAmountInTargetCurrency);
            }

            em.getTransaction().commit();
        } catch (NumberFormatException e) {
            System.out.println("Invalid card number or amount format. Please enter valid numbers.");
            em.getTransaction().rollback();
        } catch (NoResultException e) {
            System.out.println("Bank account not found.");
            em.getTransaction().rollback();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            em.getTransaction().rollback();
        }
    }

    public static void viewTransactions(){
        Query query = em.createQuery("SELECT o FROM Operations o", Operations.class);
        List<Operations> list = (List<Operations>) query.getResultList();

        for (Operations os : list) {
            System.out.println(os);
        }
    }
}


