package com.acfun.sort;

import java.util.Stack;

/**
 * 二叉树遍历
 */
public class TreeNodeRecurrence {
    /**
     * 构建一棵树开始遍历
     *
     * @param args
     */
    public static void main(String[] args) {
        //节点赋值
        TreeNode[] node = new TreeNode[10];
        for (int i = 0; i < 10; i++) {
            node[i] = new TreeNode(i);
        }
        for (int i = 0; i < 10; i++) {
            if (i * 2 + 1 < 10)
                node[i].left = node[i * 2 + 1];
            if (i * 2 + 2 < 10)
                node[i].right = node[i * 2 + 2];
        }
        postOrderRe(node[0]);
    }


    /**
     * 循环
     * 后序遍历
     * 1先访问左
     * 2再访问右
     * 3最后根
     *
     * @param biTree 节点
     */
    public static void postOrder(TreeNode biTree) {//后序遍历非递归实现
        int left = 1;//在辅助栈里表示左节点
        int right = 2;//在辅助栈里表示右节点
        Stack<TreeNode> stack = new Stack<TreeNode>();
        Stack<Integer> stack2 = new Stack<Integer>();//辅助栈，用来判断子节点返回父节点时处于左节点还是右节点。

        while (biTree != null || !stack.empty()) {
            while (biTree != null) {//将节点压入栈1，并在栈2将节点标记为左节点
                stack.push(biTree);
                stack2.push(left);
                biTree = biTree.left;
            }

            while (!stack.empty() && stack2.peek() == right) {//如果是从右子节点返回父节点，则任务完成，将两个栈的栈顶弹出
                stack2.pop();
                System.out.println(stack.pop().value);
            }

            if (!stack.empty() && stack2.peek() == left) {//如果是从左子节点返回父节点，则将标记改为右子节点
                stack2.pop();
                stack2.push(right);
                biTree = stack.peek().right;
            }
        }
    }

    /**
     * 递归
     * 后序遍历
     * 1先访问左
     * 2再访问右
     * 3最后根
     *
     * @param biTree 节点
     */
    private static void postOrderRe(TreeNode biTree) {
        if (biTree != null) {
            postOrderRe(biTree.left);
            postOrderRe(biTree.right);
            System.out.println(biTree.value);
        }
    }

    /**
     * 中序遍历
     * 1 先访问左节点
     * 2 再访问根节点
     * 3 最后访问右节点
     *
     * @param biTree 节点
     */
    private static void midOrderStack(TreeNode biTree) {
        Stack<TreeNode> stack = new Stack<>();
        while (biTree != null || !stack.isEmpty()) {
            while (biTree != null) {
                stack.push(biTree);
                biTree = biTree.left;
            }
            if (!stack.isEmpty()) {
                biTree = stack.pop();
                System.out.println(biTree.value);
                biTree = biTree.right;
            }
        }
    }

    /**
     * 中序遍历
     * 1 先访问左节点
     * 2 再访问根节点
     * 3 最后访问右节点
     *
     * @param biTree 节点
     */
    private static void midOrderRe(TreeNode biTree) {
        if (biTree != null) {
            midOrderRe(biTree.left);
            System.out.println(biTree.value);
            midOrderRe(biTree.right);
        }
    }

    /**
     * 循环前序遍历
     * 前序遍历
     * 1先遍历当前节点
     * 2 遍历当前节点左节点
     * 3遍历当前节点右节点
     *
     * @param biTree 节点
     */
    private static void preOrderReStack(TreeNode biTree) {
        Stack<TreeNode> stack = new Stack<>();
        while (biTree != null || !stack.isEmpty()) {
            while (biTree != null) {
                System.out.println(biTree.value);
                stack.push(biTree);
                biTree = biTree.left;
            }
            if (!stack.isEmpty()) {
                biTree = stack.pop();
                biTree = biTree.right;
            }
        }
    }

    /**
     * 递归前序遍历
     * 前序遍历
     * 1先遍历当前节点
     * 2 遍历当前节点左节点
     * 3遍历当前节点右节点
     *
     * @param treeNode 节点
     */
    private static void preOrderRe(TreeNode treeNode) {
        System.err.println(treeNode.value);
        if (treeNode.left != null) {
            preOrderRe(treeNode.left);
        }
        if (treeNode.right != null) {
            preOrderRe(treeNode.right);
        }
    }
}

class TreeNode {
    /**
     * 节点值
     */
    int value;
    /**
     * 左节点
     */
    TreeNode left;
    /**
     * 右节点
     */
    TreeNode right;

    TreeNode(int value) {
        this.value = value;
    }

}
