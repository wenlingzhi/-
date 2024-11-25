public class LinkStack<T> {
    // 节点类，用于链表的每个元素
    private class Node {
        public T data; // 节点存储的数据
        public Node next; // 指向下一个节点的引用

        public Node() {}

        public Node(T data, Node next) {
            this.next = next;
            this.data = data;
        }
    }

    private Node top; // 栈顶节点
    private int size; // 栈的大小

    // 入栈方法，将元素添加到栈顶
    public void push(T element) {
        top = new Node(element, top); // 创建新节点并将其置为栈顶
        size++; // 更新栈的大小
    }

    // 出栈方法，从栈顶移除并返回元素
    public T pop() {
        if (empty()) {
            throw new RuntimeException("栈为空，无法出栈"); // 如果栈为空，抛出异常
        }
        Node oldNode = top; // 记录当前栈顶节点
        top = top.next; // 更新栈顶为下一个节点
        oldNode.next = null; // 清空旧栈顶节点的引用
        size--; // 更新栈的大小
        return oldNode.data; // 返回旧栈顶节点的数据
    }

    // 获取栈顶元素的方法
    public T GetTop() {
        if (empty()) {
            throw new RuntimeException("栈为空，无法获取栈顶元素"); // 如果栈为空，抛出异常
        }
        return top.data; // 返回栈顶节点的数据
    }

    // 获取栈的长度
    public int length() {
        return size; // 返回栈的当前大小
    }

    // 判断栈是否为空
    public boolean empty() {
        return size == 0; // 如果大小为0，说明栈为空
    }

    // 将栈转换为字符串，方便调试
    public String toString() {
        if (empty()) {
            return "[]"; // 如果栈为空，返回空的字符串表示
        } else {
            StringBuilder sb = new StringBuilder("[");
            Node current = top; // 从栈顶开始遍历
            while (current != null) {
                sb.append(current.data).append(", "); // 将每个节点的数据添加到字符串中
                current = current.next; // 移动到下一个节点
            }
            int len = sb.length();
            return sb.delete(len - 2, len).append("]").toString(); // 删除最后多余的逗号和空格
        }
    }
}
