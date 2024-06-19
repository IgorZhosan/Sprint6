package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> history = new HashMap<>();
    private static final int SIZE_HISTORY = 10;
    private int currentSize = 0;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        history.put(task.getId(), newNode);
        currentSize++;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        history.remove(node.task.getId());
        currentSize--;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        if (currentSize == SIZE_HISTORY) {
            removeNode(head);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
