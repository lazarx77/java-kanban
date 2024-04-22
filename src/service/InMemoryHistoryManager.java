package service;

import model.Task;

import java.util.*;
//import ;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    //static final int HISTORY_LIST_LIMIT = 10;
    private Map<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
        history.put(task.getId(), historyList.tail);


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
            final Node<T> curHead = head;
            final Node<T> curTail = tail;
            list.add(curHead.data);
            for (int i = 1; i < history.size(); i++) {
                list.add(head.next.data);
            }
            list.add(curTail.data);
            return list;
        }

        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail != null)
                oldTail.next = newNode;
            else
                head = newNode;
            size++;
        }

        public void removeNode(Node x) {
            //final T element = x.data;
            final Node<T> next = x.next;
            final Node<T> prev = x.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.data = null;
            size--;
            //modCount++;
        }



//        public T getLast() {
//            // Реализуйте метод
//            final Node<T> curTail = tail;
//            if (curTail == null)
//                throw new NoSuchElementException();
//            return tail.data;
//        }

        public int size() {
            return this.size;
        }

//        public static void main(String[] args) {
//            HandMadeLinkedList<Integer> integers = new HandMadeLinkedList<>();
//
//            integers.addFirst(1);
//            integers.addFirst(2);
//            integers.addFirst(3);
//            integers.addLast(4);
//            integers.addLast(5);
//            integers.addFirst(1);
//
//            System.out.println(integers.getFirst());
//            System.out.println(integers.size());
//            System.out.println(integers.getLast());
//            System.out.println(integers.size());
//        }
    }


    @Override
    public void remove(int id) {
        history.remove(id);
    }

//    @Override
//    public void clearHistory() {
//        history.clear();
//    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }
}
