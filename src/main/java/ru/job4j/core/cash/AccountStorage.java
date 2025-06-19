package ru.job4j.core.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        synchronized (accounts) {
            return accounts.putIfAbsent(account.id(), account) == null;
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            return accounts.replace(account.id(), account) != null;
        }
    }

    public void delete(int id) {
        synchronized (accounts) {
            accounts.remove(id);
        }

    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (accounts) {
            Optional<Account> fromAccount = getById(fromId);
            Optional<Account> toAccount = getById(toId);
            boolean result = false;
            if (fromAccount.isPresent() && toAccount.isPresent() && fromAccount.get().amount() >= amount && amount > 0) {
                update(new Account(fromId, fromAccount.get().amount() - amount));
                update(new Account(toId, toAccount.get().amount() + amount));
                result = true;
            }
            return result;
        }
    }
}

