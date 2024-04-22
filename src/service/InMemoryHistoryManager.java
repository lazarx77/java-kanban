package service;

import model.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    //static final int HISTORY_LIST_LIMIT = 10;
    private Map<Integer, Node<Task>> history = new HashMap<>();

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (history.containsKey(id)) {
            //Node node = history.get(id);
            historyList.removeNode(history.get(id));
            historyList.linkLast(task);
            history.put(task.getId(), historyList.tail);
        }
        history.put(id, historyList.tail);

//
//        if (history.size() >= HISTORY_LIST_LIMIT) {
//            history.removeFirst();
//        }
//        history.add(task);
    }

    public class CustomLinkedList<T> {

//        class Node<E> {
//            public E data;
//            public Node<E> next;
//            public Node<E> prev;
//
//            public Node(Node<E> prev, E data, Node<E> next) {
//                this.data = data;
//                this.next = next;
//                this.prev = prev;
//            }
//        }

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public ArrayList<T> getTasks() {
            ArrayList<T> list = new ArrayList<>();
            Node<T> curHead = head;
            Node<T> curTail = tail;
            list.add(curHead.data);
            for (int i = 1; i <= historyList.size(); i++) {
                list.add(head.next.data);
            }
            list.add(curTail.data);
            return list;
        }

        public void linkLast(T element) {
            Node<T> oldTail = tail;
            Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }

        public void removeNode(Node<T> node) {
            //final Node<T> element = node.data;
            final Node<T> next = node.next;
            final Node<T> prev = node.prev;

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

        public int size() {
            return this.size;
        }
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public void clearHistory() {
        history.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }
}
