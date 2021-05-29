package com.catas;


import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    private Node head;

    private Node end;

    private int capacity;

    private Map<Integer, Node> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    // 添加数据
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // 数据已存在
            Node node = map.get(key);
            node.value = value;
            refresh(node);
        }else {
            // 数据不存在
            if (map.size() >= capacity) {
                map.remove(head.key);
                this.removeNode(head);
            }

            Node node = new Node(key, value);
            map.put(key, node);
            this.addNode(node);
        }
    }

    // 获取值
    public int get(int key) {
        Node node = map.get(key);
        if (node == null)
            return -1;
        refresh(node);
        return node.value;
    }

    // 节点移至尾部
    public void refresh(Node node) {
        if (node == end)
            return;
        removeNode(node);
        addNode(node);
    }

    // 尾部加入节点
    public void addNode(Node node) {
        if (end != null) {
            end.next = node;
            node.prev = end;
        }
        end = node;
        if (head == null) {
            head = node;
        }
    }

    // 删除节点
    public void removeNode(Node node) {
        if (head == node) {
            // 删除头节点
            head = head.next;
            // 空指针
            // head.prev = null;
        }else if (end == node) {
            // 删除尾节点
            end = end.prev;
            // end.next = null;
        }else {
            // 删除中间节点
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}


class Node {
    public Node prev;
    public Node next;
    public Integer key;
    public Integer value;

    Node(Integer key, Integer value) {
        this.key = key;
        this.value = value;
    }
}
