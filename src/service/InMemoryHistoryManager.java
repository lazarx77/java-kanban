package service;

import model.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private Map<Integer, Node<Task>> history = new HashMap<>();

    @Override
    public void add(Task task) {
        int id = task.getId();
        historyList.removeNode(history.get(id));
        historyList.linkLast(task);
        history.put(id, historyList.tail);
    }

    public class CustomLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        private int size;

        public CustomLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> list = new ArrayList<>();
            if (head != null) {
                Node<T> tempNode = head;
                while (tempNode != null) {
                    list.add(tempNode.data);
                    tempNode = tempNode.next;
                }
            }
            return list;
        }

        public void linkLast(T element) {
            Node<T> newNode = new Node<>(element);
            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;
            size++;
        }

        public void removeNode(Node<T> node) {

            if (history.containsValue(node)) {

                Node<T> next = node.next;
                Node<T> prev = node.prev;

                if (prev == null) {
                    head = next;
                } else {
                    prev.next = next;
                    node.prev = null;
                }

                if (next == null) {
                    tail = prev;
                } else {
                    next.prev = prev;
                    node.next = null;
                }

                node.data = null;
                size--;
            }
        }

        public int size() {
            return size;
        }
    }

    @Override
    public void remove (int id) {
        historyList.removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public void clearHistory() {
        history.clear();
        historyList = new CustomLinkedList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }
}
