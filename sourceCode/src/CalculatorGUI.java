import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

// 请运行这个程序!!
public class CalculatorGUI extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton postfixButton;
    private JButton directButton;

    //创建gui
    public CalculatorGUI() {
        JOptionPane.showMessageDialog(this, "你好(^ ^)/! 我是表达式转换器\n本计算器支持整数或实数的以下符号的运算: +, -, *, /, ^, 单目负, 括号",
                "计算器说明", JOptionPane.INFORMATION_MESSAGE);
        //设置标题和大小等
        setTitle("表达式计算器");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口的布局管理器为BorderLayout
        setLayout(new BorderLayout());

        // 创建文本输入和输出框
        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        // 创建两个按钮，分别用于显示后缀表达式并计算和直接计算
        postfixButton = new JButton("显示后缀并计算");
        directButton = new JButton("直接计算");

        // 设置输入框和按钮的字体
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        postfixButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        directButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));

        // 创建一个面板用于放置按钮
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(postfixButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(directButton, gbc);

        // 将输入框、输出区域和按钮面板添加到窗口的不同区域
        add(inputField, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 为后缀计算按钮添加事件监听器
        postfixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String infix = inputField.getText();
                outputArea.setText("");
                PostfixAndCalculate(infix);
            }
        });

        // 同理监听
        directButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String infix = inputField.getText();
                outputArea.setText("");
                justCalculate(infix);
            }
        });

        setLocationRelativeTo(null);
    }

    // 先显示后缀表达式再计算结果
    public void PostfixAndCalculate(String infix) {
        //首先验证表达式是否合理
        if (validateExpression(infix)) {
            String postfix = infToPf(infix);
            outputArea.append("后缀表达式为: " + postfix + "\n");
            Double result = Calculate(postfix, outputArea);
            if (result != null) {
                String output = String.format("%.2f", result);
                outputArea.append("运算结果(保留小数点后二位): " + output + "\n");
            }
        } else {
            outputArea.append("算术表达式输入有误, 请重新输入!\n");
        }
    }

    // 不显示地计算后缀表达式, 直接计算结果
    public void justCalculate(String infix) {
        if (validateExpression(infix)) {
            Double result = Calculate(infToPf(infix), outputArea);
            if (result != null) {
                String output = String.format("%.2f", result);
                outputArea.append("运算结果(保留小数点后二位): " + output + "\n");
            }
        } else {
            outputArea.append("算术表达式输入有误, 请重新输入!\n");
        }
    }

    // 定义不同操作符的优先级
    public static int prec(char c) {
        if (c == '^') return 3;
        if (c == '*' || c == '/') return 2;
        if (c == '+' || c == '-') return 1;
        return 0;
    }

    // 判断是否属于操作符
    public static boolean isOperator(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')');
    }

    // 判断中缀表达式是否合理
    public static boolean validateExpression(String expression) {
        // 移除空格
        String trimmed = expression.replaceAll(" ", "");

        // 检查空表达式
        if (trimmed.isEmpty()) return false;

        // 检查是否包含非法字符
        if (!trimmed.matches("[0-9+\\-*/^().]*")) {
            // 如果包含非法字符，输出错误信息并返回false
            JOptionPane.showMessageDialog(null, "没有此操作符");
            return false;
        }

        // 检查是否有多余空格
        if (!expression.equals(trimmed)) {
            JOptionPane.showMessageDialog(null, "输入不能包含多余的空格！");
            return false;
        }

        // 检查括号匹配
        int num = 0;
        for (char temp : trimmed.toCharArray()) {
            if (temp == '(') num++;
            else if (temp == ')') num--;
            if (num < 0) return false;
        }
        if (num > 0) return false;

        // 移除括号进行进一步验证
        trimmed = trimmed.replaceAll("\\(|\\)", "");

        // 正则表达式验证
        return trimmed.matches("^[+-]?([0-9]+(\\.[0-9]+)?)([-+*/^][+-]?([0-9]+(\\.[0-9]+)?))*$");
    }


    // 中缀转后缀
    public static String infToPf(String infix) {

        //栈存放结果
        LinkStack<Character> s = new LinkStack<>();
        //记录后缀表达式的变量, 初始化空字符
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            // 1. 遇到 ( 左括号
            // 直接压入栈
            if (c == '(') {
                s.push(c);

                // 2. 遇到 ) 右括号
                // 一直pop栈顶元素, 直到遇到最近的(
                // 把pop出来的运算符加到postfix中
                // 最后再把 ( pop出来
            } else if (c == ')') {
                while (s.GetTop() != '(') {
                    postfix.append(' ').append(s.pop());
                }
                s.pop();

                // 3. 遇到操作数,直接添加到后缀变量末尾
            } else if (Character.isDigit(c) || c == '.') {
                postfix.append(c);

                //遇到操作符
            } else {
                // 如果是单目负,则加上一个0
                // 单目负的条件: 出现在第一个位置, 或是前面是操作符
                if (c == '-' && (i == 0 || isOperator(infix.charAt(i - 1)))) {
                    postfix.append('0').append(' ');
                    s.push(c);

                    // 如果当前的操作符优先级小于或等于栈顶的操作符优先级
                    // 就弹出栈顶, 直至栈顶的优先级小于当前优先级
                    // 这时便可以把当前操作符压入栈顶
                } else {
                    while (!s.empty() && prec(c) <= prec(s.GetTop())) {
                        postfix.append(' ').append(s.pop());
                    }
                    postfix.append(' ');
                    s.push(c);
                }
            }
            // 将栈中剩余操作符添加到后缀末尾
        }
        while (!s.empty()) {
            postfix.append(' ').append(s.pop());
        }
        return postfix.toString();
    }

    //由后缀表达式计算结果
    public static Double Calculate(String postfix, JTextArea outputArea) {
        // 创建一个栈存放结果
        LinkStack<Double> stack = new LinkStack<>();
        // 按空格分隔后缀中的字符, 依次遍历进行对应操作
        String[] list = postfix.split(" ");
        for (String e : list) {
            //如果输入的数字是整数或者小数的话, 就转为double压入栈
            if (e.matches("\\d+\\.\\d+|\\d+")) {
                stack.push(Double.parseDouble(e));
            } else {
                //如果是操作符,此时弹出两个数,进行相应的运算
                double num1 = stack.pop();
                double num2 = stack.pop();
                Double result = calTwoNums(num1, num2, e, outputArea);
                if (result == null) {
                    return null; // 如果返回 null，直接返回
                }
                stack.push(result);
            }
        }
        // 保留两位小数即可
        BigDecimal bd = new BigDecimal(Double.toString(stack.GetTop()));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // 遇到对应的操作符时,返回对应的结果
    public static Double calTwoNums(double n1, double n2, String operator, JTextArea outputArea) {
        switch (operator) {
            case "+":
                return n2 + n1;
            case "-":
                return n2 - n1;
            case "*":
                return n2 * n1;
            case "/":
                if (n1 == 0) {
                    JOptionPane.showMessageDialog(null, "除数不能为0!");
                    return null;
                }
                return n2 / n1;
            case "^":
                return Math.pow(n2, n1);
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
        });
    }
}
