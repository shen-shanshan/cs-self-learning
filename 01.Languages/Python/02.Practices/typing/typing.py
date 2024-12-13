from typing import List


a = 1
a: int = "1"

a: List[int] = [1, 2, 3]
a: List[int] = ["str", "str", "str"]  # 虽然不是强制 runtime 报错，但是开发友好，非常推荐
