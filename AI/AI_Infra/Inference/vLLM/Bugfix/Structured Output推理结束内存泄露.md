```bash
Adding requests: 100%|██████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████| 1/1 [00:00<00:00, 69.44it/s]
Processed prompts: 100%|███████████████████████████████████████████████████████████████████████████████| 1/1 [00:02<00:00,  2.51s/it, est. speed input: 8.78 toks/s, output: 10.78 toks/s]
--------------------------------------------------
Structured outputs by JSON: {
  "brand": "Toyota",
  "model": "Supra",
  "car_type": "Coupe"
}
--------------------------------------------------
nanobind: leaked 2 instances!
 - leaked instance 0xfff8bda43198 of type "xgrammar.xgrammar_bindings.CompiledGrammar"
 - leaked instance 0xfff8bda97798 of type "xgrammar.xgrammar_bindings.GrammarMatcher"
nanobind: leaked 5 types!
 - leaked type "xgrammar.xgrammar_bindings.CompiledGrammar"
 - leaked type "xgrammar.xgrammar_bindings.TokenizerInfo"
 - leaked type "xgrammar.xgrammar_bindings.Grammar"
 - leaked type "xgrammar.xgrammar_bindings.GrammarMatcher"
 - leaked type "xgrammar.xgrammar_bindings.GrammarCompiler"
nanobind: leaked 47 functions!
 - leaked function ""
 - leaked function "from_ebnf"
 - leaked function "deserialize_json"
 - leaked function "__init__"
 - leaked function ""
 - leaked function "reset"
 - leaked function "builtin_json_grammar"
 - leaked function "__init__"
 - leaked function "get_cache_size_bytes"
 - leaked function "union"
 - leaked function ""
 - leaked function "accept_token"
 - leaked function "_debug_print_internal_state"
 - leaked function "from_structural_tag"
 - leaked function "compile_regex"
 - leaked function "from_regex"
 - leaked function ""
 - leaked function "serialize_json"
 - leaked function "is_terminated"
 - leaked function ""
 - leaked function "__init__"
 - leaked function ""
 - leaked function "rollback"
 - leaked function ""
 - leaked function ""
 - leaked function "serialize_json"
 - leaked function "concat"
 - leaked function "find_jump_forward_string"
 - leaked function "deserialize_json"
 - leaked function "accept_string"
 - leaked function "clear_cache"
 - leaked function "compile_builtin_json_grammar"
 - leaked function "to_string"
 - leaked function "_detect_metadata_from_hf"
 - leaked function "dump_metadata"
 - leaked function ""
 - leaked function "from_vocab_and_metadata"
 - leaked function ""
 - leaked function "from_json_schema"
 - leaked function "deserialize_json"
 - leaked function ""
 - leaked function "fill_next_token_bitmask"
 - leaked function ""
 - leaked function "compile_structural_tag"
 - leaked function "compile_grammar"
 - leaked function "serialize_json"
 - leaked function "compile_json_schema"
nanobind: this is likely caused by a reference counting issue in the binding code.
```
