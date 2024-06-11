package service;

/**
 * Вспомогательный класс для реализации связанного списка задач для истории задач
 */

class Node<E> {
    public E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(E data) {
        this.data = data;
    }
}
