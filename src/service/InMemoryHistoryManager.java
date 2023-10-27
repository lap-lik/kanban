package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final Map<Integer, Node> history = new HashMap<>();
    private static Node first;
    private static Node last;

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int taskId) {
        removeNode(history.remove(taskId));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node temp = last;
        Node newNode = new Node(temp, task, null);
        last = newNode;
        if (temp == null) {
            first = newNode;
        } else {
            temp.next = newNode;
            removeNode(history.remove(task.getId()));
        }
        history.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (history.isEmpty()) {
                first = null;
            } else if (node.equals(first)) {
                first = node.next;
                first.prev = null;
            }else if (node.equals(last)) {
                last = node.prev;
                last.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node temp = first;
        while (temp != null) {
            tasks.add(temp.task);
            temp = temp.next;
        }
        return tasks;
    }

    static class Node {
        Node prev;
        Task task;
        Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}