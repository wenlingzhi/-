//这个是测试我创建的LinkStack是否正确

public class LinkStackTester {
    // 简单测试LinkStack
    public static void main(String[] args) {
        // 创建链式栈实例
        LinkStack<Object> stack = new LinkStack<>();

        // 测试入栈操作
        System.out.println("测试入栈操作：");
        stack.push(1);
        stack.push(2.5);
        stack.push("+");
        System.out.println("当前栈内容: " + stack); // 输出栈的内容

        // 测试获取栈顶元素
        System.out.println("栈顶元素: " + stack.GetTop()); // 应该输出"+"

        // 测试出栈操作
        System.out.println("出栈元素: " + stack.pop()); // 应该输出"+"
        System.out.println("当前栈内容: " + stack); // 输出剩余的栈内容

        // 测试栈长度
        System.out.println("当前栈长度: " + stack.length()); // 应该输出2

        // 测试栈是否为空
        System.out.println("栈是否为空: " + stack.empty()); // 应该输出false

        // 继续出栈操作
        System.out.println("出栈元素: " + stack.pop()); // 应该输出2.5
        System.out.println("出栈元素: " + stack.pop()); // 应该输出1

        // 测试栈是否为空
        System.out.println("栈是否为空: " + stack.empty()); // 应该输出true

        // 尝试获取栈顶元素，应该抛出异常
        try {
            System.out.println("尝试获取栈顶元素: " + stack.GetTop());
        } catch (RuntimeException e) {
            System.out.println("异常信息: " + e.getMessage()); // 输出异常信息
        }
    }
}
