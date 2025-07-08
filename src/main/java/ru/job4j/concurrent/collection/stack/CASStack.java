package ru.job4j.concurrent.collection.stack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CASStack<T> {

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T element) {
        Node<T> previousHead;
        Node<T> newNode;
        do {
            previousHead = head.get();
            newNode = new Node<>(element, previousHead);
        } while (!head.compareAndSet(previousHead, newNode));
    }

    public Optional<T> pop() {
        Node<T> previousHead;
        do {
            previousHead = head.get();
            if (previousHead == null) {
                return Optional.empty();
            }
        } while (!head.compareAndSet(previousHead, previousHead.next));
        return Optional.ofNullable(previousHead.value);
    }

    private record Node<T>(T value, CASStack.Node<T> next) {
    }
}
