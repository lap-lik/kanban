package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        }
    }

    @Override
    public void remove(int taskId) {
        removeNode(history.remove(taskId));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        Node temp = last;
        Node newNode = new Node(temp, task, null);
        last = newNode;
        if (temp == null) {
            first = newNode;
        } else {
            temp.next = newNode;
            if (history.containsKey(task.getId())) {
                remove(task.getId());
            }
        }
        history.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node != null) {
            if (node.next == null && node.prev == null) {
                first = null;
            } else if (node.equals(first)) {
                first = node.next;
                first.prev = null;
            } else if (node.equals(last)) {
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