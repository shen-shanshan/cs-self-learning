# post-draft

**XgrammarBackend:**

```
__init__():
    tokenizer
    vocab_size
    tokenizer_info = xgr.TokenizerInfo
    compiler = xgr.GrammarCompiler

compile_grammar(语法类型, 语法格式):
    根据不同语法类型，调用对应的 compile 方法
    ctx = compiler.compile_xxx(xxx)
    return XgrammarGrammar(matcher=xgr.GrammarMatcher(ctx), vocab_size, ctx)

allocate_token_bitmask(max_num_seqs):
    xgr.allocate_token_bitmask(max_num_seqs, vocab_size)
```

**XgrammarGrammar:**

```
accept_tokens(request_id, tokens):
    matcher.accept_token(token)

fill_bitmask():

```
