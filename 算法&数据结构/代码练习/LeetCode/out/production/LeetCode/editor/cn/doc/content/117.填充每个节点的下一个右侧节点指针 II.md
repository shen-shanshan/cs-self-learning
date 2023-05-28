<p>给定一个二叉树</p>

<pre>
struct Node {
  int val;
  Node *left;
  Node *right;
  Node *next;
}</pre>

<p>填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 <code>NULL</code>。</p>

<p>初始状态下，所有&nbsp;next 指针都被设置为 <code>NULL</code>。</p>

<p>&nbsp;</p>

<p><strong>进阶：</strong></p>

<ul> 
 <li>你只能使用常量级额外空间。</li> 
 <li>使用递归解题也符合要求，本题中递归程序占用的栈空间不算做额外的空间复杂度。</li> 
</ul>

<p>&nbsp;</p>

<p><strong>示例：</strong></p>

<p><img alt="" src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2019/02/15/117_sample.png" style="height: 218px; width: 640px;" /></p>

<pre>
<strong>输入</strong>：root = [1,2,3,4,5,null,7]
<strong>输出：</strong>[1,#,2,3,#,4,5,7,#]
<strong>解释：</strong>给定二叉树如图 A 所示，你的函数应该填充它的每个 next 指针，以指向其下一个右侧节点，如图 B 所示。序列化输出按层序遍历顺序（由 next 指针连接），'#' 表示每层的末尾。</pre>

<p>&nbsp;</p>

<p><strong>提示：</strong></p>

<ul> 
 <li>树中的节点数小于 <code>6000</code></li> 
 <li><code>-100&nbsp;&lt;= node.val &lt;= 100</code></li> 
</ul>

<p>&nbsp;</p>

<ul> 
</ul>

<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>链表</li><li>二叉树</li></div></div><br><div><li>👍 621</li><li>👎 0</li></div>